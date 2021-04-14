package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.PermissionDAO;
import com.jessie.SHMarket.entity.Permission;
import com.jessie.SHMarket.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService
{
    @Autowired
    private PermissionDAO permissionDAO;
    @Override
    public List<Integer> getUserPermission(int uid)
    {
        return permissionDAO.getUserPermission(uid);
    }

    @Override
    public void setUserPermission(int uid, int pid)
    {
        permissionDAO.setUserPermission(uid,pid);
    }

    @Override
    public boolean queryUserPermission(int uid, int pid)
    {
        return permissionDAO.queryUserPermission(uid,pid);
    }

    @Override
    public List<Permission> getAllUserPermissions(int uid)
    {
        return permissionDAO.getAllUserPermissions(uid);
    }
}
