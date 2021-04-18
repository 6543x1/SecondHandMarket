package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.UserIdentity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserIdentityService
{
    void saveIdentity(UserIdentity userIdentity);

    UserIdentity userIdentity(int uid);
}
