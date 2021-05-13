package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.entity.*;
import com.jessie.SHMarket.service.*;
import com.jessie.SHMarket.utils.JwtTokenUtil;
import com.jessie.SHMarket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class OrderController
{
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    OrderCommentService orderCommentService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MailService mailService;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/buy", produces = "application/json;charset=UTF-8")
    public String newOrder(int gid, String buyCode, HttpServletRequest request) throws Exception
    {
        Order order = new Order();
        order.setGid(gid);
        if (!redisUtil.exists("Goods_BuyCode|" + gid))
        {
            return JSON.toJSONString(Result.error("卖家尚未生成商品购买码或是已经过期了"));
        } else
        {
            if (!buyCode.equals(redisUtil.get("Goods_BuyCode|" + gid)))
            {
                return JSON.toJSONString(Result.error("购买码不对"));
            }
        }
        if (goodsService.getGoods(order.getGid()).getStatus() != 1)
        {
            return JSON.toJSONString(Result.error("该商品不存在"));
        }
        order.setBuyer(jwtTokenUtil.getUidFromToken(request.getHeader("token")));
        order.setSeller(goodsService.getGoods(order.getGid()).getUid());
        if (order.getBuyer() == order.getSeller())
        {
            return JSON.toJSONString(Result.error("不能购买自己出售的商品"), 403);
        }
        if (userService.getUser(order.getSeller()).getStatus() <= 0)
        {
            return JSON.toJSONString(Result.error("卖家被封号了不能下单", 403));
        }
        order.setGeneratedTime(LocalDateTime.now());
        order.setStatus(0);
        orderService.newOrder(order);
        goodsService.updateGoods(2, order.getGid());
        OrderComment orderComment = new OrderComment();
        orderComment.setBuyer(order.getBuyer());
        orderComment.setSeller(order.getSeller());
        orderComment.setOid(order.getOid());
        orderCommentService.newOrderComment(orderComment);
        redisUtil.set("orderGenerated|" + order.getOid(), "7 DAYS", 60 * 60 * 24 * 7);
        redisUtil.saveUserMessage(order.getSeller(), new UserMessage("你的一个商品" + "#{" + order.getGid() + "}" + "已经被拍下了", "商品消息", LocalDateTime.now()));
        mailService.sendNewOrder(userService.getMailAddr(order.getSeller()), "你的一个商品被拍下了，快去看看吧");
        return JSON.toJSONString(Result.success("下单成功", order));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/doneOrder", produces = "text/plain;charset=UTF-8")
    public String doneOrder(int oid, @RequestParam(value = "cancel", defaultValue = "false") boolean cancel, HttpServletRequest request) throws Exception
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        Order theOrder = orderService.getOrder(oid);
        if (theOrder.getBuyer() != uid || theOrder.getSeller() != uid)
        {
            return JSON.toJSONString(Result.error("你没有这个订单", 403));
        }//本来想写拦截器后面发现还挺麻烦的
        if (theOrder.getStatus() >= 11 || theOrder.getStatus() < 0)
        {
            return JSON.toJSONString(Result.error("订单已完成或异常"));
        }
        if (cancel)
        {
            if (theOrder.getStatus() == 0)
            {
                theOrder.setStatus(-1);
                goodsService.updateGoods(1, theOrder.getGid());
            } else
            {
                return JSON.toJSONString(Result.error("该订单因为有一方确认完成而无法取消，请联系管理员处理"));
            }
        } else if (theOrder.getBuyer() == uid)
        {
            theOrder.setStatus(theOrder.getStatus() + 10);
            redisUtil.saveUserMessage(theOrder.getSeller(), new UserMessage("你的订单" + "#{" + theOrder.getOid() + "}" + "状态更新了", "订单消息", LocalDateTime.now()));
        } else
        {
            theOrder.setStatus(theOrder.getStatus() + 1);
            redisUtil.saveUserMessage(theOrder.getBuyer(), new UserMessage("你的订单" + "#{" + theOrder.getOid() + "}" + "状态更新了", "订单消息", LocalDateTime.now()));
        }
        theOrder.setDoneTime(LocalDateTime.now());
        orderService.doneOrder(theOrder);
        if(theOrder.getStatus()==11){
            redisUtil.delete("orderGenerated|"+oid);
        }
        return JSON.toJSONString(Result.success(("订单状态更新"), orderService.getOrder(oid)));
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(value = "/deleteOrderTruly", produces = "text/plain;charset=UTF-8")
    public String deleteOrderTruly(int oid) throws Exception
    {
        orderService.deleteOrder(oid);
        return JSON.toJSONString(Result.success("订单已彻底删除"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/deleteOrder", produces = "text/plain;charset=UTF-8")
    public String deleteOrder(int oid, HttpServletRequest request) throws Exception
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        Order order = orderService.getOrder(oid);
        if (order.getBuyer() != uid || order.getSeller() != uid)
        {
            return JSON.toJSONString(Result.error("不可以用别人号下单", 403));
        }
        if (order.getStatus() != 0 && order.getStatus() != 1)
        {
            return JSON.toJSONString(Result.error("异常订单不可删除"));
        }
        order.setStatus(-1);
        orderService.doneOrder(order);
        return JSON.toJSONString(Result.success("订单已删除"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/getUserOrders")
    public PageInfo getUserOrders(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) throws Exception
    {
        PageHelper.startPage(pageNum, 5);
        User user = userService.getUser(UserController.getCurrentUsername());
        List<Order> list = orderService.getUserOrder(user.getUid());
        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/getUserSellerOrders")
    public PageInfo getUserSellerOrders(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) throws Exception
    {
        PageHelper.startPage(pageNum, 5);
        int uid = userService.getUid(UserController.getCurrentUsername());
        List<OrderWithGoods> list = orderService.getSellerOrderWithGoods(uid);
        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/getUserBuyerOrders")
    public PageInfo getUserBuyerOrders(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum) throws Exception
    {
        PageHelper.startPage(pageNum, 5);
        int uid = userService.getUid(UserController.getCurrentUsername());
        List<OrderWithGoods> list = orderService.getBuyerOrderWithGoods(uid);
        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/getOrderByGid", produces = "application/json;charset=UTF-8")
    public String getOrder(int gid, HttpServletRequest request)
    {
        Order order = orderService.getOrderByGid(gid);
        if (order == null) return JSON.toJSONString(Result.error("订单不存在", 404));
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        if (order.getSeller() != uid && order.getBuyer() != uid)
        {
            return JSON.toJSONString(Result.error("您不可以查看这个订单", 403));
        }
        return JSON.toJSONString(order);
    }
    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/comment", produces = "application/json;charset=UTF-8")
    public String commentOrder(int oid, @RequestParam(defaultValue = "好评") String type, String comment,HttpServletRequest request)
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        Order order = orderService.getOrder(oid);
        if (order == null || (order.getSeller() != uid && order.getBuyer() != uid))
            return JSON.toJSONString(Result.error("订单不存在"));
        if (order.getStatus() != 11)
        {
            return JSON.toJSONString(Result.error("双方均确认完成订单后才能评价,异常订单无法评价"));
        }
        OrderComment orderComment = orderCommentService.getOrder(order.getOid());
        if (orderComment == null)
        {
            orderComment = new OrderComment(order.getOid(), order.getBuyer(), order.getSeller());
            orderCommentService.newOrderComment(orderComment);
        }
        if (order.getBuyer() == uid)
        {
            orderComment.setB_Comment(comment);
            orderComment.setB_Type(type);
            orderCommentService.updateBuyerComment(orderComment);
            if ("好评".equals(type))
            {
                int curStatus = userService.getStatus(order.getSeller());
                if (curStatus >= 40 && curStatus <= 70 && curStatus + 8 <= 70)
                {
                    userService.plusStatus(order.getSeller(), 8);
                }
                if (curStatus < 200)
                {
                    userService.plusStatus(order.getSeller(), 5);
                    userService.updateAdditionalScore(uid, 5);
                }
            } else if ("差评".equals(type))
            {
                userService.plusStatus(order.getSeller(), -5);

            }
            redisUtil.saveUserMessage(order.getSeller(),new UserMessage("你的订单" + "#{" + order.getOid() + "}" + "收到一个"+type, "订单消息", LocalDateTime.now()));
        } else
        {
            orderComment.setS_Comment(comment);
            orderComment.setS_Type(type);
            orderCommentService.updateSellerComment(orderComment);
            if ("好评".equals(type))
            {
                userService.plusStatus(order.getBuyer(), 5);
            } else if ("差评".equals(type))
            {
                userService.plusStatus(order.getBuyer(), -5);
            }
            redisUtil.saveUserMessage(order.getBuyer(),new UserMessage("你的订单" + "#{" + order.getOid() + "}" + "收到一个好评", "订单消息", LocalDateTime.now()));
        }
        return JSON.toJSONString(Result.success("评价成功"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/getComments", produces = "text/plain;charset=UTF-8")
    public String getComments(int oid, HttpServletRequest request)
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        OrderComment orderComment = orderCommentService.getOrder(oid);
        return JSON.toJSONString(orderComment);
    }

    @GetMapping(value = "/getUserReceivedComments")
    public PageInfo getUserReceivedComments(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, int uid) throws Exception
    {
        PageHelper.startPage(pageNum, 10);
        List<OrderComment_Extended> list = orderCommentService.getUserReceivedComments(uid);
        return new PageInfo<>(list);
    }

}

