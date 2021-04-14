package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.Permission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionService
{

    List<Integer> getUserPermission(int uid);

    void setUserPermission(@Param("uid")int uid, @Param("pid")int pid);

    boolean queryUserPermission(int uid,int pid);//注意重复数据好像会导致false

    List<Permission> getAllUserPermissions(int uid);//和All搭配是不是要加s啊。。。。我英语不太好
}
