package com.jessie.SHMarket.entity;


import java.io.Serializable;

public class ContactInfo implements Serializable
{

    private int cid;
    private String name;
    private String phoneNumber;
    private String location;
    private int uid;

    @Override
    public String toString()
    {
    return "ContactInfo{" +
            "cid=" + cid +
            ", name='" + name + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", location='" + location + '\'' +
            ", uid=" + uid +
            '}';
  }

  public int getCid() {
    return cid;
  }

  public void setCid(int cid) {
    this.cid = cid;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }


  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

}
