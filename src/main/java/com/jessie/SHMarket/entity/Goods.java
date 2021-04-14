package com.jessie.SHMarket.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class Goods {

  private int gid;
  private String description;
  private String label;
  private String brand;
  private String quality;
  private int uid;
  private int status;
  private LocalDateTime uploadTime;
  private double price;
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
            ", uploadTime=" + uploadTime.toString() +
            ", price=" + price +
            '}';
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
