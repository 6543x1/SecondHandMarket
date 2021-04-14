package com.jessie.SHMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessie.SHMarket.entity.ContactInfo;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.ContactInfoService;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@RestController
@RequestMapping("/user/contact")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class ContactInfoController
{
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ContactInfoService contactInfoService;
    @Autowired
    UserService userService;
    @RequestMapping("/new")
    public String newContactInfo(ContactInfo contactInfo, ModelMap modelMap) throws Exception{
        int uid=(int)modelMap.get("uid");
        contactInfo.setUid(uid);//防一手异常uid
        contactInfoService.newContactInfo(contactInfo);
        return objectMapper.writeValueAsString(Result.success("成功"));
    }
    @RequestMapping("/get")
    public List<ContactInfo> getContactInfo(ModelMap modelMap) throws Exception{
        int uid=(int)modelMap.get("uid");//这个就不分页了，不会真有人上百条地址信息吧
        //恶意攻击的话。。。恐怕也没法防御
        return contactInfoService.queryUserContactInfo(uid);
    }
    @RequestMapping("/delete")
    public String deleteContactInfo(int cid, ModelMap modelMap) throws Exception{
        int uid=(int)modelMap.get("uid");
        if(contactInfoService.getContactInfo(cid).getUid()!=uid){
            return objectMapper.writeValueAsString(Result.error("无权限",403));
        }
        return objectMapper.writeValueAsString(Result.success("成功"));
    }
}
