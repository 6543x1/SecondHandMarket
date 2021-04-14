package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PermissionDAO
{
    @Select("select pid from user_permissions where uid=#{uid}")
    List<Integer> getUserPermission(int uid);
    @Insert("insert into user_permission (uid,pid) values(#{uid},#{pid})")
    void setUserPermission(@Param("uid")int uid,@Param("pid")int pid);
    @Select("select * from user_permission values(#{uid},#{pid})")
    boolean queryUserPermission(int uid,int pid);//注意重复数据好像会导致false
    @Select("select * from permission where id in(select pid from user_permission where uid=#{uid})")
    List<Permission> getAllUserPermissions(int uid);
    @Delete("delete from permission where uid=#{uid} and pid=#{pid}")
    void deletePermission(int uid,int pid);
}

