package com.jessie.SHMarket.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable
{

    private int oid;
    private LocalDateTime generatedTime;
    private int status;//0—任何一方没有确认，1——买家确认，10——卖家确认，11——双方确认。负数——异常。
    private int buyer;
    private int seller;
    private LocalDateTime doneTime;
    private LocalDateTime deliveryTime;
    private int cid;
    private String location;
    private int gid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getGeneratedTime()
    {
        return generatedTime;
    }

    public void setGeneratedTime(LocalDateTime generatedTime)
    {
        this.generatedTime = generatedTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDoneTime()
    {
        return doneTime;
    }

    public void setDoneTime(LocalDateTime doneTime)
    {
        this.doneTime = doneTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDeliveryTime()
    {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString()
    {
        return "Order{" +
                "oid=" + oid +
                ", generatedTime=" + generatedTime +
                ", status=" + status +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", doneTime=" + doneTime +
                ", deliveryTime=" + deliveryTime +
                ", cid=" + cid +
                ", location='" + location + '\'' +
                ", gid=" + gid +
                '}';
    }

    public int getOid()
    {
        return oid;
    }

    public void setOid(int oid)
    {
        this.oid = oid;
    }


    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }


    public int getBuyer()
    {
        return buyer;
    }

    public void setBuyer(int buyer)
    {
        this.buyer = buyer;
    }


    public int getSeller()
    {
        return seller;
    }

    public void setSeller(int seller)
    {
        this.seller = seller;
    }


    public int getCid()
    {
        return cid;
    }

    public void setCid(int cid)
    {
        this.cid = cid;
    }


    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }


    public int getGid()
    {
        return gid;
    }

    public void setGid(int gid)
    {
        this.gid = gid;
    }

}
