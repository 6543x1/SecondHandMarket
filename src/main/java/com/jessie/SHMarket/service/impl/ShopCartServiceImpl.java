package com.jessie.SHMarket.service.impl;


import com.jessie.SHMarket.dao.ShopCartDAO;
import com.jessie.SHMarket.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shopCartService")
public class ShopCartServiceImpl implements ShopCartService
{
    @Autowired
    ShopCartDAO shopCartDAO;

    @Override
    public void saveShopCart(int uid, int data)
    {
        shopCartDAO.saveShopCart(uid, data);
    }

    @Override
    public String getShopCart(int uid)
    {
        return shopCartDAO.getShopCart(uid);
    }
}
