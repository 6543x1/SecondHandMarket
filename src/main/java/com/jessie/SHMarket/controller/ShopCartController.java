package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.configuration.RedisUtil;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.GoodsService;
import com.jessie.SHMarket.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shopCart")
public class ShopCartController
{
    @Autowired
    GoodsService goodsService;
    @Autowired
    ShopCartService shopCartService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisUtil redisUtil;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/saveShopCart")
    public String saveShopCart(String data, HttpServletRequest request) throws Exception
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        redisUtil.saveUserShopCart(uid, data);
        return JSON.toJSONString(Result.success("保存购物车成功"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping("/getShopCart")
    public String getShopCart(HttpServletRequest request) throws Exception
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        String data = redisUtil.getUserShopCart(uid);
        if (data == null)
        {
            data = shopCartService.getShopCart(uid);//先去redis中找，找不到去数据库找
        }
        return JSON.toJSONString(data);
    }
}
