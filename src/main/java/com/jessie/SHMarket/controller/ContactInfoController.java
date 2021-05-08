package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.jessie.SHMarket.entity.ContactInfo;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.ContactInfoService;
import com.jessie.SHMarket.service.UserService;
import com.jessie.SHMarket.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user/contact")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class ContactInfoController
{
    @Autowired
    ContactInfoService contactInfoService;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @RequestMapping("/new")
    public String newContactInfo(ContactInfo contactInfo, HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        contactInfo.setUid(uid);//防一手异常uid
        contactInfoService.newContactInfo(contactInfo);
        return JSON.toJSONString(Result.success("成功"));
    }

    @RequestMapping("/get")
    public List<ContactInfo> getContactInfo(HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        //这个就不分页了，不会真有人上百条地址信息吧
        //恶意攻击的话。。。恐怕也没法防御
        return contactInfoService.queryUserContactInfo(uid);
    }

    @RequestMapping("/delete")
    public String deleteContactInfo(int cid, HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        if (contactInfoService.getContactInfo(cid).getUid() != uid)
        {
            return JSON.toJSONString(Result.error("无权限", 403));
        }
        return JSON.toJSONString(Result.success("成功"));
    }
}
