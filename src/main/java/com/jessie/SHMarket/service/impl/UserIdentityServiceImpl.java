package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.UserIdentityDAO;
import com.jessie.SHMarket.entity.UserIdentity;
import com.jessie.SHMarket.service.UserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userIdentityService")
public class UserIdentityServiceImpl implements UserIdentityService
{
    @Autowired
    UserIdentityDAO userIdentityDAO;

    @Override
    public void saveIdentity(UserIdentity userIdentity)
    {
        userIdentityDAO.saveIdentity(userIdentity);
    }

    @Override
    public UserIdentity userIdentity(int uid)
    {
        return userIdentityDAO.userIdentity(uid);
    }
}
