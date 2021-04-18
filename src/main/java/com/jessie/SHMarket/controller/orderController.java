package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.dao.OrderDAO;
import com.jessie.SHMarket.entity.Order;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.GoodsService;
import com.jessie.SHMarket.service.OrderService;
import com.jessie.SHMarket.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class orderController
{
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/buy")
    public String newOrder(Order order, ModelMap modelMap) throws Exception
    {
        order.setBuyer((Integer) modelMap.get("uid"));
        order.setSeller(goodsService.getGoods(order.getGid()).getUid());
        order.setGeneratedTime(LocalDateTime.now());
        order.setStatus(0);
        orderService.newOrder(order);
        goodsService.updateGoods(2, order.getGid());
        return objectMapper.writeValueAsString(Result.success("下单成功", order.getGeneratedTime()));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/doneOrder")
    public String doneOrder(int oid, ModelMap modelMap) throws Exception
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
        if (theOrder.getBuyer() == uid)
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
    public PageInfo getUserOrders(@RequestParam(value = "pageNum", defaultValue="1") int pageNum) throws Exception{
        PageHelper.startPage(pageNum,5);
        User user=userService.getUser(UserController.getCurrentUsername());
        List<Order> list=orderService.getUserOrder(user.getUid());
        return new PageInfo<>(list);
    }
    public boolean checkIsTheUser(Order order){
        User buyer=userService.getUser(order.getBuyer());
        if(buyer.getUsername().equals(UserController.getCurrentUsername())){
            return true;
        }
        return false;
    }
}

