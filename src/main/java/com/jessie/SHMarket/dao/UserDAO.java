package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO
{

    @Insert("insert into user (username,password,status,role,evaluation) values (#{username},#{password},#{status},#{role},#{evaluation})")
//里面是大括号
    void saveUser(User user);

    @Update("update user set img_path=#{img_path} where uid=#{uid} ")
    void saveImg(User user);

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where username= #{username}")
    User getUser(String username);

    @Select("select nickName from user where username=#{username}")
    String getNickNameByUsername(String username);

    @Select("select nickName from user where uid=#{uid}")
    String getNickNameByUid(int uid);

    @Select("select mailAddr from user where username=#{username}")
    String getMailAddrByUsername(String username);

    @Select("select mailAddr from user where uid=#{uid}")
    String getMailAddrByUid(int uid);

    @Select("select * from user where uid= #{uid}")
    User getUserByUid(int uid);//方法重载会报错。。。

    @Update("update user set password=#{password} where uid=#{uid}")
    void editPassword(@Param("uid") int uid, @Param("password") String password);

    @Update("update user set mailAddr=#{mailAddr} where uid=#{uid}")
    void setMailAddr(@Param("uid") int uid, @Param("mailAddr") String mailAddr);

    @Update("update user set status=#{status} where uid=#{uid}")
    void setStatus(@Param("uid") int uid, @Param("status") int status);

    @Select("select status from user where uid=#{uid}")
    int getStatus(int uid);

    @Update("update user set nickName=#{nickName} where uid=#{uid}")
    void setNickName(User user);

    @Select("SELECT LAST_INSERT_ID()")
    int newestUid();//说回来这东西是线程安全的吗？？？要不是线程安全那不是要出事了

    @Update("update user set evaluation=#{evaluation} where uid=#{uid}")
    void updateEvaluation(@Param("uid") int uid, @Param("evaluation") int evaluation);


}
