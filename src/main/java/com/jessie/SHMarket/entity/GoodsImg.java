package com.jessie.SHMarket.entity;


public class GoodsImg
{

  private int imageId;
  private String name;
  private int gid;
  private int uid;
  private String path;

  @Override
  public String toString()
  {
    return "GoodsImg{" +
            "imageId=" + imageId +
            ", name='" + name + '\'' +
            ", gid=" + gid +
            ", uid=" + uid +
            ", path='" + path + '\'' +
            '}';
  }

  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public int getGid() {
    return gid;
  }

  public void setGid(int gid) {
    this.gid = gid;
  }


  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
