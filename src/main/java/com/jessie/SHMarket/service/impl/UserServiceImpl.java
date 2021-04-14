package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.UserDAO;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("userService")
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDAO userDAO;
    @Override
    public void saveUser(User user)
    {
        userDAO.saveUser(user);
    }

    @Override
    public List<User> findAllAccount()
    {
        return null;
    }

    @Override
    public User getUser(String username)
    {
        return userDAO.getUser(username);
    }

    @Override
    public User getUser(int uid)
    {
        return userDAO.getUserByUid(uid);
    }

    @Override
    public void setNickName(User user)
    {
        userDAO.setNickName(user);
    }

    @Override
    public void setMailAddr(int uid, String mailAddr)
    {
        userDAO.setMailAddr(uid,mailAddr);
    }

    @Override
    public void editPassword(int uid,String password)
    {
        userDAO.editPassword(uid,password);
    }

    @Override
    public void setStatus(int uid, int status)
    {
        userDAO.setStatus(uid,status);
    }
}
