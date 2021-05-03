package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.GoodsAndSeller;
import com.jessie.SHMarket.entity.Goods_More;

import java.util.List;


public interface GoodsService
{
    //里面是大括号
    int saveGoods(Goods goods);

    Goods getGoods(int gid);

    void editGoods(Goods goods);

    int getUid(int gid);

    List<Goods_More> queryGoods();

    List<Goods> getUserGoods(int uid, boolean isOwn);

    List<Goods> getUncheckedGoods();

    void updateGoods(int status, int gid);

    void deleteGoods(int gid);

    List<Goods_More> search(String keyValue);

    int queryTodayGoods(int uid);

    Goods newestGoods();

    List<Goods> getGoodsByLabel(String label);

    void updateImgNum(int gid, int imgNum);

    List<GoodsAndSeller> getGoodsListWithBuyer();

    Goods_More getGoodsFull(int gid);


}
