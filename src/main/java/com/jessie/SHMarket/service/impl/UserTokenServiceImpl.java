package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.TokenDAO;
import com.jessie.SHMarket.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userTokenService")
public class UserTokenServiceImpl implements UserTokenService
{
    @Autowired
    TokenDAO tokenDAO;

    @Override
    public void saveMailCode(String username, String mailCode, String mailAddr)
    {
        tokenDAO.saveMailCode(username, mailCode, mailAddr);
    }

    @Override
    public void saveToken(int uid, String username, String token)
    {
        tokenDAO.saveToken(uid, username, token);
    }

    @Override
    public String getToken(String username)
    {
        return tokenDAO.getToken(username);
    }

    @Override
    public String getMailCode(String username)
    {
        return tokenDAO.getMailCode(username);
    }

    @Override
    public String getTemp(String username)
    {
        return tokenDAO.getTemp(username);
    }

    @Override
    public void newUser(int uid, String username)
    {
        tokenDAO.newUser(uid, username);
    }
}
