package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.GoodsImg;

import java.util.List;

public interface GoodsImgService
{

    void newImg(GoodsImg goodsImg);

    void deleteImg(String name,int uid);

    List<GoodsImg> getGoodsImg(int gid);

    GoodsImg getImgByIndex(int index, int gid);

}
