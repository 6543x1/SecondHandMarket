package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.GoodsImg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsImgService
{

    void newImg(GoodsImg goodsImg);
    List<GoodsImg> getGoodsImg(int gid);
}
