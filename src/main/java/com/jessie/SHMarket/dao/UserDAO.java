package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO
{

    @Insert("insert into user (username,password,status,role) values (#{username},#{password},#{status},#{role})")
//里面是大括号
    void saveUser(User user);

    @Update("update user set img_path=#{img_path} where uid=#{uid} ")
    void saveImg(User user);

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where username= #{username}")
    User getUser(String username);

    @Select("select * from user where uid= #{uid}")
    User getUserByUid(int uid);//方法重载会报错。。。

    @Update("update user set password=#{password} where uid=#{uid}")
    void editPassword(@Param("uid") int uid, @Param("password") String password);

    @Update("update user set mailAddr=#{mailAddr} where uid=#{uid}")
    void setMailAddr(@Param("uid") int uid, @Param("mailAddr") String mailAddr);
    @Update("update user set status=#{status} where uid=#{uid}")
    void setStatus(@Param("uid") int uid, @Param("status") int status);
    @Update("update user set nickName=#{nickName} where uid=#{uid}")
    void setNickName(User user);
}
