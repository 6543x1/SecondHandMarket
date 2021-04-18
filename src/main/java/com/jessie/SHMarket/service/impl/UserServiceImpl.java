package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.UserDAO;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.UserService;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
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

    public static boolean testJWCHPost(String No, String Password)
    {
        int code = 200;
        String result;
        try
        {
            String postURL = "http://59.77.226.32/logincheck.asp";
            PostMethod postMethod = null;
            postMethod = new PostMethod(postURL);
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            postMethod.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
            postMethod.setRequestHeader("Referer", "http://jwch.fzu.edu.cn/");
            postMethod.setRequestHeader("Origin", "http://jwch.fzu.edu.cn");
            postMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
            postMethod.setRequestHeader("DNT", "1");
            postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            NameValuePair[] data = {
                    new NameValuePair("muser", No),
                    new NameValuePair("passwd", Password)

            };
            //参数设置，需要注意的就是里边不能传NULL，要传空字符串
            postMethod.setRequestBody(data);

            org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
            int response = httpClient.executeMethod(postMethod); // 执行POST方法
            result = postMethod.getResponseBodyAsString();
            System.out.println(result);
            System.out.println(postMethod.getStatusCode());
            code = postMethod.getStatusCode();
        } catch (Exception e)
        {
            System.out.println("请求异常" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        if (code == 302)
        {
            return true;
        }
        return false;
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
    public void editPassword(int uid, String password)
    {
        userDAO.editPassword(uid, password);
    }

    @Override
    public void setStatus(int uid, int status)
    {
        userDAO.setStatus(uid, status);
    }

    @Override
    public void saveImg(User user)
    {
        userDAO.saveImg(user);
    }
}
