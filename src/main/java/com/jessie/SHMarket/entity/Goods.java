package com.jessie.SHMarket.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.time.LocalDateTime;

@Configuration
public class Goods implements Serializable
{

    protected int gid;
    protected String description;
    protected String label;
    protected String brand;
    protected String quality;
    protected int uid;
    protected int status;
    protected LocalDateTime uploadTime;
    protected double price;
    protected int imgNum;
    protected String contact;

    @Override
    public String toString()
    {
        return "Goods{" +
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
                ", cid=" + contact +
                '}';
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public int getImgNum()
    {
        return imgNum;
    }

    public void setImgNum(int imgNum)
    {
        this.imgNum = imgNum;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//原来这样就可以，之前坑死前端了。。
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  public LocalDateTime getUploadTime()
  {
    return uploadTime;
  }

  public void setUploadTime(LocalDateTime uploadTime)
  {
    this.uploadTime = uploadTime;
  }

  public int getGid() {
    return gid;
  }

  public void setGid(int gid) {
    this.gid = gid;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }


  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }


  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }


  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }


  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
