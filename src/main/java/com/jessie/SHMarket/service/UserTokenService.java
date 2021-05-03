package com.jessie.SHMarket.service;

public interface UserTokenService
{
    void saveMailCode(String username, String mailCode, String mailAddr);

    void saveToken(int uid, String username, String token);

    String getToken(String username);

    String getMailCode(String username);

    String getTemp(String username);

    void newUser(int uid, String username);

}
