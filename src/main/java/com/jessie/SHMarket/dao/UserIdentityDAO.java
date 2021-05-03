package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.UserIdentity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserIdentityDAO
{
    @Insert("insert into user_identity (uid, No, school) VALUES (#{uid},#{No},#{school})")
    void saveIdentity(UserIdentity userIdentity);

    @Select("select * from user_identity where uid =#{uid}")
    UserIdentity userIdentity(int uid);

    @Select("select * from user_identity where No =#{No}")
    UserIdentity userIdentityByNo(String No);
}
