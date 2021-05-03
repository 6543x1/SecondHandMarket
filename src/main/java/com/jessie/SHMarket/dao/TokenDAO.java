package com.jessie.SHMarket.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TokenDAO
{
    @Update("update user_token set mailCode=#{mailCode},temp=#{mailAddr} where username=#{username}")
    void saveMailCode(@Param("username") String username, @Param("mailCode") String mailCode, @Param("mailAddr") String mailAddr);

    @Insert("Replace into user_token (uid,username,token) values (#{uid},#{username},#{token})")
    void saveToken(@Param("uid") int uid, @Param("username") String username, @Param("token") String token);

    @Insert("insert into user_token (uid,username) values (#{uid},#{username})")
    void newUser(@Param("uid") int uid, @Param("username") String username);

    @Select("select token from user_token where username=#{username}")
    String getToken(String username);

    @Select("select mailCode from user_token where  username=#{username}")
    String getMailCode(String username);

    @Select("select temp from user_token where  username=#{username}")
    String getTemp(String username);
}
