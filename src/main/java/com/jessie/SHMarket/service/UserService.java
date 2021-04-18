package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService
{

    void saveUser(User user);

    void saveImg(User user);

    List<User> findAllAccount();

    User getUser(String username);

    User getUser(int uid);
    void setNickName(User user);
    void setMailAddr(int uid, String mailAddr);

    void editPassword(int uid, String password);
    void setStatus(int uid,int status);
}
