package com.jessie.SHMarket.entity;

public class User
{
    String username;
    String password;
    int uid;
    String mailAddr;
    String nickName;
    int status;
    String role;
    String img_path;

    @Override
    public String toString()
    {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uid=" + uid +
                ", mailAddr='" + mailAddr + '\'' +
                ", nickName='" + nickName + '\'' +
                ", status=" + status +
                ", role='" + role + '\'' +
                ", img_path='" + img_path + '\'' +
                '}';
    }

    public String getImg_path()
    {
        return img_path;
    }

    public void setImg_path(String img_path)
    {
        this.img_path = img_path;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uid)
    {
        this.uid = uid;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
