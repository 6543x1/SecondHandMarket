package com.jessie.SHMarket.entity;

import java.io.Serializable;

public class Goods_Extended extends Goods implements Serializable
{
    private int sellerEva;
    private String nickName;

    public int getSellerEva()
    {
        return sellerEva;
    }

    public void setSellerEva(int sellerEva)
    {
        this.sellerEva = sellerEva;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Override
    public String toString()
    {
        return "Goods_More{" +
                "gid=" + gid +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                ", brand='" + brand + '\'' +
                ", quality='" + quality + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                ", uploadTime=" + uploadTime +
                ", price=" + price +
                ", imgNum=" + imgNum +
                ", nickName='" + nickName + '\'' +
                ", contact='" + contact + '\'' +
                ", sellerEva=" + sellerEva +
                '}';
    }
}
