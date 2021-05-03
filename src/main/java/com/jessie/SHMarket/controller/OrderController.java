package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.configuration.RedisUtil;
import com.jessie.SHMarket.entity.Order;
import com.jessie.SHMarket.entity.OrderComment;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.GoodsService;
import com.jessie.SHMarket.service.OrderCommentService;
import com.jessie.SHMarket.service.OrderService;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
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
    ObjectMapper objectMapper;
    @Autowired
    OrderCommentService orderCommentService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisUtil redisUtil;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/buy", produces = "application/json;charset=UTF-8")
    public String newOrder(Order order, ModelMap modelMap) throws Exception
    {
        if (goodsService.getGoods(order.getGid()).getStatus() != 1)
        {
            return JSON.toJSONString(Result.error("该商品不存在或已被下单"));
        }
        order.setBuyer((Integer) modelMap.get("uid"));
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
        redisUtil.set("orderGenerated|" + orderService.newestOrder(), "7 DAYS", 60 * 60 * 24 * 7);
        return JSON.toJSONString(Result.success("下单成功", order));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/doneOrder")
    public String doneOrder(int oid, @RequestParam(value = "cancel", defaultValue = "false") boolean cancel, ModelMap modelMap) throws Exception
    {
        int uid = (int) modelMap.get("uid");
        Order theOrder = orderService.getOrder(oid);
        if (theOrder.getBuyer() != uid || theOrder.getSeller() != uid)
        {
            return objectMapper.writeValueAsString(Result.error("你没有这个订单", 403));
        }//本来想写拦截器后面发现还挺麻烦的
        if (theOrder.getStatus() >= 11)
        {
            return objectMapper.writeValueAsString(Result.error("订单已经完成了"));
        }
        if (cancel = true)
        {
            theOrder.setStatus(-1);
            goodsService.updateGoods(1, theOrder.getGid());
        } else if (theOrder.getBuyer() == uid)
        {
            theOrder.setStatus(theOrder.getStatus() + 10);
        } else
        {
            theOrder.setStatus(theOrder.getStatus() + 1);
        }
        theOrder.setDoneTime(LocalDateTime.now());
        orderService.doneOrder(theOrder);
        return JSON.toJSONString(Result.success(("订单状态更新"), orderService.getOrder(oid)));
    }
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/deleteOrderTruly")
    public String deleteOrderTruly(int oid) throws Exception{
        orderService.deleteOrder(oid);
        return objectMapper.writeValueAsString(Result.success("订单已彻底删除"));
    }
    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/deleteOrder")
    public String deleteOrder(Order order) throws Exception
    {
        if (!checkIsTheUser(order))
        {
            return objectMapper.writeValueAsString(Result.error("不可以用别人号下单", 403));
        }
        if (order.getStatus() != 0 && order.getStatus() != 1)
        {
            return objectMapper.writeValueAsString(Result.error("异常订单不可删除"));
        }
        order.setStatus(-1);
        orderService.doneOrder(order);
        return objectMapper.writeValueAsString(Result.success("订单已删除"));
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
    public String commentOrder(int oid, @RequestParam(defaultValue = "好评") String type, String comment, ModelMap modelMap)
    {
        int uid = (int) modelMap.get("uid");
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
                userService.plusStatus(order.getSeller(), 5);
                userService.updateAdditionalScore(uid, 5);
            } else if ("差评".equals(type))
            {
                userService.plusStatus(order.getSeller(), -5);
            }
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
        }
        return JSON.toJSONString(Result.success("评价成功"));
    }

    public boolean checkIsTheUser(Order order)
    {
        User buyer = userService.getUser(order.getBuyer());
        if (buyer.getUsername().equals(UserController.getCurrentUsername()))
        {
            return true;
        }
        return false;
    }
}

