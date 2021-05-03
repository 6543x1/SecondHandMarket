package com.jessie.SHMarket.entity;


import java.io.Serializable;

public class OrderComment implements Serializable
{

    private int oid;
    private int buyer;
    private int seller;
    private String b_Comment;
    private String b_Type;
    private String s_Comment;
    private String s_Type;

    public OrderComment()
    {
    }

    public OrderComment(int oid, int buyer, int seller)
    {
        this.oid = oid;
        this.buyer = buyer;
        this.seller = seller;
    }

    public int getOid()
    {
        return oid;
    }

    public void setOid(int oid)
    {
        this.oid = oid;
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

    public String getB_Comment()
    {
        return b_Comment;
    }

    public void setB_Comment(String b_Comment)
    {
        this.b_Comment = b_Comment;
    }

    public String getB_Type()
    {
        return b_Type;
    }

    public void setB_Type(String b_Type)
    {
        this.b_Type = b_Type;
    }

    public String getS_Comment()
    {
        return s_Comment;
    }

    public void setS_Comment(String s_Comment)
    {
        this.s_Comment = s_Comment;
    }

    public String getS_Type()
    {
        return s_Type;
    }

    public void setS_Type(String s_Type)
    {
        this.s_Type = s_Type;
    }
}
