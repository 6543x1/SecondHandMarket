package com.jessie.SHMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessie.SHMarket.entity.Permission;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.MailService;
import com.jessie.SHMarket.service.PermissionService;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.jessie.SHMarket.service.impl.MailServiceImpl.getRandomString;

@RestController
@RequestMapping("/test")
public class TestController
{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;//从MD5直接升级Bcrypt，真香
    @Autowired
    private MailService mailService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/testReg", produces = "text/html;charset=UTF-8")
    public String test() throws Exception
    {
        User theUser = new User();
        theUser.setUsername("Jessie");
        theUser.setPassword("123456");
        theUser.setNickName(getRandomString());
        theUser.setStatus(0);
        System.out.println(theUser.toString());
        System.out.println("success create");
        //userService.saveUser(theUser);
        return objectMapper.writeValueAsString("success");
    }
    @PostMapping(value = "/Login", produces = "text/html;charset=UTF-8")
    public String login(User user, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
    {
        System.out.println("已经取得用户数据，正在向数据库查询。。。");
        System.out.println(user.toString());
        Result result = null;
        User thisUser;
        try
        {
            thisUser = userService.getUser(user.getUsername());
            if (bCryptPasswordEncoder.matches(user.getPassword(),thisUser.getPassword()))
            {//写哈希好像有点麻烦，用MD5会比较简单，会牺牲一些安全性但是加了盐后难度都比较高吧

                model.addAttribute("username", user.getUsername());
                if (session != null)
                {
                    System.out.println("NOT NULL SESSION");
                    session.setMaxInactiveInterval(30 * 60);
                }
                result = Result.success("loginSuccess", userService.getUser(user.getUsername()).getStatus());
            } else
            {
                System.out.println(thisUser.getPassword());
                System.out.println(DigestUtils.md5DigestAsHex((user.getPassword() + user.getNickName()).getBytes()));
                result = Result.error("用户名或者密码错误");
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();//用户不存在
            System.out.println("Wrong Password Or Username");
            result = Result.error("Wrong Password or Username");
        }
        return objectMapper.writeValueAsString(result);
    }
    @RequestMapping(value = "/testPermission",produces = "application/json;charset=UTF-8")
    public String Test()throws Exception{
        List<Permission> theList=permissionService.getAllUserPermissions(1);
        for(Permission x:theList){
            System.out.println(x.toString());
        }
        return objectMapper.writeValueAsString(Result.success("查好了",theList));
    }
}
