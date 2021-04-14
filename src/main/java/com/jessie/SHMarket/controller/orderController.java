package com.jessie.SHMarket.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
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
    public String newOrder(Order order) throws Exception{
        if(!checkIsTheUser(order)){
            return objectMapper.writeValueAsString(Result.error("不可以用别人号下单",403));
        }

        order.setGeneratedTime(LocalDateTime.now());
        orderService.newOrder(order);
        return objectMapper.writeValueAsString(Result.success("下单成功",order.getGeneratedTime()));
    }
    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/editOrder")
    public String editOrder(Order order) throws Exception{
        if(!checkIsTheUser(order)){
            return objectMapper.writeValueAsString(Result.error("不可以用别人号下单",403));
        }//本来想写拦截器后面发现还挺麻烦的
        order.setDoneTime(LocalDateTime.now());
        orderService.doneOrder(order);
        return objectMapper.writeValueAsString(Result.success("订单状态已更新",order.getGeneratedTime()));
    }
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/deleteOrderTruly")
    public String deleteOrderTruly(int oid) throws Exception{
        orderService.deleteOrder(oid);
        return objectMapper.writeValueAsString(Result.success("订单已彻底删除"));
    }
    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/deleteOrder")
    public String deleteOrder(Order order) throws Exception{
        if(!checkIsTheUser(order)){
            return objectMapper.writeValueAsString(Result.error("不可以用别人号下单",403));
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

