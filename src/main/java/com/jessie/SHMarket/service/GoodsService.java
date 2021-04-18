package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.Goods;

import java.util.List;


public interface GoodsService
{
//里面是大括号
void saveGoods(Goods goods);

    Goods getGoods(int gid);

    List<Goods> queryGoods();
    List<Goods> getUncheckedGoods();
    void updateGoods(int status,int gid);

    void deleteGoods(int gid);

    List<Goods> search(String keyValue);
}
