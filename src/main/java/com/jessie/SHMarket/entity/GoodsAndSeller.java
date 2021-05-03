package com.jessie.SHMarket.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

public class GoodsAndSeller implements Serializable
{
    private Goods goods;
    private User seller;

    @Override
    public String toString()
    {
        return "GoodsAndSeller{" +
                "goods=" + goods +
                ", seller=" + seller +
                '}';
    }

    public Goods getGoods()
    {
        return goods;
    }

    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }

    @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
    public User getSeller()
    {
        return seller;
    }

    public void setSeller(User seller)
    {
        this.seller = seller;
    }
}
