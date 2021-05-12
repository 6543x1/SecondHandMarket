package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.UserIdentity;
import com.jessie.SHMarket.service.PermissionService;
import com.jessie.SHMarket.service.UserIdentityService;
import com.jessie.SHMarket.utils.JwcIdentifyUtil;
import com.jessie.SHMarket.utils.JwtTokenUtil;
import com.jessie.SHMarket.utils.RedisUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.jessie.SHMarket.service.impl.UserServiceImpl.testJWCHPost;

@RestController
@RequestMapping("/user")
public class UserIdentifyController
{
    @Autowired
    UserIdentityService userIdentityService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    PermissionService permissionService;
    @Autowired
    JwcIdentifyUtil jwcIdentifyUtil;
    @PostMapping(value = "/confirmFzu", produces = "application/json;charset=UTF-8")
    public String confirmFzu(String No, String Password, String VerifyCode,HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        if (No == null || Password == null)
        {
            return JSON.toJSONString(Result.error("账号或密码为空"));
        }
        if (userIdentityService.userIdentity(uid) != null)
        {
            return JSON.toJSONString(Result.error("该号已经认证"));
        }
        if (userIdentityService.userIdentity(No) != null)
        {
            System.out.println(JSON.toJSONString(Result.error("该学号被认证")));
        }
        if(redisUtil.exists("User_Jwc_Cookie|"+uid)){
            if(jwcIdentifyUtil.login(No,Password,VerifyCode,redisUtil.get("User_Jwc_Cookie|"+uid,Map.class))){
                UserIdentity userIdentity = new UserIdentity();
                userIdentity.setUid(uid);
                userIdentity.setNo(No);
                userIdentity.setSchool("福州大学");
                //permissionService.setUserPermission(uid, 2);//授予普通用户的权限
                //userIdentityService.saveIdentity(userIdentity);
                return JSON.toJSONString(Result.success("认证成功"));
            }
        }
        else{
            return JSON.toJSONString(Result.error("请重新获取一下验证码"));
        }

        return JSON.toJSONString(Result.error("认证失败,请检查学号密码或是验证码是否正确"));
    }
    @PostMapping(value = "/getVerifyCode", produces = "application/json;charset=UTF-8")
    public String getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        jwcIdentifyUtil.getVerifyCode("D:/usr/tomcat/Img",uid);
        File file=new File("D:/usr/tomcat/Img/imageYzm.png");
        if(!file.exists()){
            return JSON.toJSONString(Result.error("服务器发生了异常",500));
        }
        try
        {
            //获取页面输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //读取文件
            byte[] bytes = FileUtils.readFileToByteArray(file);
            //向输出流写文件
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition", "attachment;filename=" + uid + "_imageYzm.png");
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            return JSON.toJSONString(Result.error("服务器发生错误", 500));
        }
        return JSON.toJSONString(Result.success("开始下载"));
    }

}
