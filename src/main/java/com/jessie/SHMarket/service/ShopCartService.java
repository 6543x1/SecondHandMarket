package com.jessie.SHMarket.service;


public interface ShopCartService
{
    void saveShopCart(int uid, int data);

    String getShopCart(int uid);
}
