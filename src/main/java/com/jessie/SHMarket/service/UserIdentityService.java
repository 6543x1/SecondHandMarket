package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.UserIdentity;

public interface UserIdentityService
{
    void saveIdentity(UserIdentity userIdentity);

    UserIdentity userIdentity(int uid);

    UserIdentity userIdentity(String No);
}
