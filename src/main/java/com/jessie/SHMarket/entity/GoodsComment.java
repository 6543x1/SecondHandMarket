package com.jessie.SHMarket.entity;


import java.io.Serializable;

public class GoodsComment implements Serializable
{

    private int gid;
    private int reviewer;
    private String content;
    private int replyTo;
    private String reviewer_nickName;
    private String replyTo_nickName;
    private int visited;


    public int getGid()
    {
        return gid;
    }

    public void setGid(int gid)
    {
        this.gid = gid;
    }


    public int getReviewer()
    {
        return reviewer;
    }

    public void setReviewer(int reviewer)
    {
        this.reviewer = reviewer;
    }


    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }


    public int getReplyTo()
    {
        return replyTo;
    }

    public void setReplyTo(int replyTo)
    {
        this.replyTo = replyTo;
    }


    public String getReviewer_nickName()
    {
        return reviewer_nickName;
    }

    public void setReviewer_nickName(String reviewer_nickName)
    {
        this.reviewer_nickName = reviewer_nickName;
    }

    public String getReplyTo_nickName()
    {
        return replyTo_nickName;
    }

    public void setReplyTo_nickName(String replyTo_nickName)
    {
        this.replyTo_nickName = replyTo_nickName;
    }

    public int getVisited()
    {
        return visited;
    }

    public void setVisited(int visited)
    {
        this.visited = visited;
    }

}
