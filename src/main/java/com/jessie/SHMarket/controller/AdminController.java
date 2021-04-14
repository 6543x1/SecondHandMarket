package com.jessie.SHMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @PostMapping(value = "/testAdminController",produces = "application/json;charset=UTF-8")
    public String testAdmin(int oid) throws Exception{
        return objectMapper.writeValueAsString(Result.success("您是管理员，可以进行管理员操作。"));
    }
    @PostMapping(value = "/deleteOrderTruly",produces = "application/json;charset=UTF-8")
    public String deleteOrderTruly(int oid) throws Exception{
        orderService.deleteOrder(oid);
        return objectMapper.writeValueAsString(Result.success("订单已彻底删除"));
    }
    @PostMapping(value = "/getUncheckedGoods",produces = "application/json;charset=UTF-8")
    public PageInfo<Goods> getGoods(@RequestParam(value = "pageNum", defaultValue="1") int pageNum, ModelMap modelMap){
        PageHelper.startPage(pageNum,10);
        List<Goods> list=goodsService.getUncheckedGoods();
        return new PageInfo<>(list);
    }
    @PostMapping(value = "/deleteGoods",produces = "application/json;charset=UTF-8")
    public String deleteGoods(int gid) throws Exception{
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        goodsService.deleteGoods(gid);
        return objectMapper.writeValueAsString(Result.success("不合格商品已删除"));
    }
    @PostMapping(value = "/passGoods",produces = "application/json;charset=UTF-8")
    public String passGoods(int gid) throws Exception{
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        goodsService.updateGoods(1,gid);
        return objectMapper.writeValueAsString(Result.success("商品通过审核"));
    }
    @PostMapping(value = "/banUser",produces = "application/json;charset=UTF-8")
    public String banUser(int uid) throws Exception{
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        if(userService.getUser(uid).getRole().equals("admin")){
            return objectMapper.writeValueAsString(Result.error("禁止管理员内斗"));
        }
        userService.setStatus(uid,100);
        return objectMapper.writeValueAsString(Result.success("封号成功"));
    }

}
