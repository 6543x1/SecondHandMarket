package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.entity.UserPortrait;

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

    String getMailAddr(String username);

    String getMailAddr(int uid);

    String getNickName(int uid);

    String getNickName(String username);

    void editPassword(int uid, String password);

    void setStatus(int uid, int status);

    void plusStatus(int uid, int score);

    int getUid(String username);

    int calculateEvaluation(int status);

    void newUserPortrait(UserPortrait userPortrait);

    UserPortrait getUserPortrait(int uid);

    void updateAdditionalScore(int uid, int score);

    void updatePunishedScore(int uid, int score);

    int getAdditionalScore(int uid);

    String getImgPath(int uid);

    int newestUser();

    int getStatus(int uid);
}
