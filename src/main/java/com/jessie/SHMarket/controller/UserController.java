package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessie.SHMarket.entity.*;
import com.jessie.SHMarket.service.MailService;
import com.jessie.SHMarket.service.PermissionService;
import com.jessie.SHMarket.service.UserIdentityService;
import com.jessie.SHMarket.service.UserService;
import com.jessie.SHMarket.service.impl.MailServiceImpl;
import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.jessie.SHMarket.service.impl.MailServiceImpl.getRandomString;
import static com.jessie.SHMarket.service.impl.UserServiceImpl.testJWCHPost;

@RestController
@RequestMapping("/user")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class UserController
{
    @Resource(name = "theImgSuffix")
    HashMap<Integer, String> theImgSuffix;
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
    @Autowired
    private UserIdentityService userIdentityService;

    // nohup java -jar /usr/shmarket-0.0.1-SNAPSHOT.jar &
    @PostMapping(value = "/ResetPw", produces = "application/json;charset=UTF-8")
    public String editPassword(String oldPassword, String newPassword) throws Exception
    {
        User thisUser = userService.getUser(getCurrentUsername());
        if (bCryptPasswordEncoder.matches(oldPassword, thisUser.getPassword()))
        {
            thisUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userService.editPassword(thisUser.getUid(), thisUser.getPassword());
            return objectMapper.writeValueAsString(Result.success("EditSuccess"));
        } else
        {
            return objectMapper.writeValueAsString(Result.error("WrongPassword", 403));
        }
    }

    @PostMapping(value = "/upload", produces = "text/html;charset=UTF-8")
    public String UploadImg(HttpServletRequest request, @RequestParam("upload") MultipartFile upload, ModelMap modelmap) throws Exception
    {

        System.out.println("上传头像");
        int uid = (Integer) modelmap.get("uid");
        String path = "/usr/tomcat/Img/" + modelmap.get("username");
        System.out.println(path);
        //如果文件重名，应该覆盖原文件吧（是否覆盖由前端决定）
        //选的是war exploded 那么文件会在工程目录下
        //否则在tomcat目录下

        File file = new File(path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            String filename = upload.getOriginalFilename();
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            if (!theImgSuffix.containsValue(suffix))
            {
                throw new Exception("?");
            }
            User user = new User();
            user.setUid(uid);
            user.setImg_path(path + File.pathSeparator + upload.getOriginalFilename());
            upload.transferTo(new File(path, upload.getOriginalFilename()));
            userService.saveImg(user);
            System.out.println("头像保存成功，开始向数据库中更新用户数据");

        } catch (NullPointerException e)
        {
            e.printStackTrace();
            return objectMapper.writeValueAsString(Result.error("找不到文件的名字"));
        } catch (Exception e)
        {
            e.printStackTrace();
            return objectMapper.writeValueAsString(Result.error("未知错误"));
        }
        return objectMapper.writeValueAsString(Result.success("上传成功"));
    }


    @PostMapping (value = "/Register", produces = "text/html;charset=UTF-8")
    public String register(User user) throws Exception
    {
        //说回来要是有人破解了链接给我服务器扔冲蝗核弹咋办 springboot有防御措施吗...
        if(user==null) return objectMapper.writeValueAsString(Result.error("无数据"));
        System.out.println(user);
        if(user.getUsername().length()>=15||user.getPassword().length()>=30){
            return objectMapper.writeValueAsString(Result.error("想扔内存核弹？"));
        }
        System.out.println("取得注册用数据，开始向数据库中写入数据...");
        if (userService.getUser(user.getUsername()) != null)
        {
            System.out.println("用户名已存在，请重试");
            return objectMapper.writeValueAsString(Result.error("用户名已存在", 500));
        }
        user.setStatus(100);
        user.setRole("user");
        user.setNickName(getRandomString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//换用Bcrypt，自动加盐真香
        userService.saveUser(user);
        return objectMapper.writeValueAsString(Result.success("RegisterSuccess"));
    }

    @RequestMapping(value = "/loginSuccess", produces = "text/plain;charset=UTF-8")
    public String isLogin(Model model) throws Exception
    {
        String username = getCurrentUsername();
        if (username == null) return objectMapper.writeValueAsString(Result.error("服务器内部错误", 500));
        model.addAttribute("username", username);
        User user = userService.getUser(username);
        user.setPassword("加密也不给你看");
        model.addAttribute("uid", user.getUid());
        String userInfo = objectMapper.writeValueAsString(user);
        System.out.println(userInfo);
        Result result = Result.success("loginSuccess", user);
        return JSON.toJSONString(result);//jackson那个垃圾玩意老给我多出来\，还是阿里懂我
    }

    @PostMapping(value = "/userInfo", produces = "text/plain;charset=UTF-8")
    public String userInfo(Model model) throws Exception
    {
        String username = getCurrentUsername();
        User user = userService.getUser(username);
        return objectMapper.writeValueAsString(user);
    }

    @RequestMapping(value = "/loginError", produces = "text/plain;charset=UTF-8")
    public String loginError(Model model) throws Exception
    {
        return objectMapper.writeValueAsString(Result.error("密码不匹配或是已被封号", 400));
    }

    //Spring Security获取当前用会话的户信息
    public static String getCurrentUsername()
    {
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal=authentication.getPrincipal();
        if(principal==null){
            return null;
        }
        if(principal instanceof UserDetails){
            UserDetails userDetails=(UserDetails) principal;
            username=userDetails.getUsername();
        }
        else{
            username=principal.toString();//????
        }
        return username;
    }
    @RequestMapping(value = "loginOut",produces = "application/json;charset=UTF-8")
    public String LoginOut(SessionStatus status)throws Exception{
       // status.setComplete();//将session重置
        return objectMapper.writeValueAsString(Result.success("loginOutSuccess"));
    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value = "/checkAdmin",produces = "application/json;charset=UTF-8")
    public String IAMAdmin()throws Exception{
        return objectMapper.writeValueAsString(Result.success("你是管理员"));
    }
    @PostMapping(value = "/setMailAddr", produces = "application/json;charset=UTF-8")
    public String setMailAddress(String mailAddr, ModelMap modelMap, Model model) throws Exception
    {
        String username = (String) modelMap.get("username");
        if (username == null) return objectMapper.writeValueAsString(Result.error("找不到用户名(服务器出错联系管理员)"));
        if (mailAddr == null) return objectMapper.writeValueAsString(Result.error("邮箱是空的"));
        if (userService.getUser(username).getMailAddr() != null)
        {
            return objectMapper.writeValueAsString(Result.error("已经设置邮箱了，如果失效请联系管理员更改"));
        }
        model.addAttribute("resetCode", getRandomString());
        model.addAttribute("mailAddr", mailAddr);
        mailService.sendResetPw(mailAddr, username + "的请求码(六位字符）:" + modelMap.get("resetCode"));
        return objectMapper.writeValueAsString(Result.success("SuccessPlzConfirm"));
    }
    @RequestMapping(value = "/confirmAddr", produces = "text/html;charset=UTF-8")
    public String confirmAddr(ModelMap modelMap, Model model, String mailCode) throws Exception
    {
        Result res;
        String username = (String) modelMap.get("username");
        if (username == null) return objectMapper.writeValueAsString("EMPTY USERNAME");
        String trueCode = (String) modelMap.get("resetCode");
        if (trueCode.equals(mailCode))
        {
                userService.setMailAddr((int)modelMap.get("uid"), (String) modelMap.get("mailAddr"));
            res = Result.success("confirmSuccess");
            model.addAttribute("resetCode", "");
        } else
        {
            res = Result.error("Incorrect code", 400);
        }
        return objectMapper.writeValueAsString(res);
    }
    @RequestMapping(value = "/sendMail", produces = "text/html;charset=UTF-8")
    public String ResetPwByMail(ModelMap modelMap, Model model,String username) throws Exception
    {
        if (username == null) return objectMapper.writeValueAsString(Result.error("找不到用户名(服务器出错联系管理员)"));
        String mailAddr;
        try
        {
            mailAddr = userService.getUser(username).getMailAddr();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            return objectMapper.writeValueAsString(Result.error("没有设置邮箱", 404));
        }
        model.addAttribute("resetCode", getRandomString());
        mailService.sendResetPw(mailAddr, username + "的请求码(六位字符）:" + modelMap.get("resetCode"));
        return objectMapper.writeValueAsString(Result.success("请到邮箱查看邮件"));
    }
    @RequestMapping(value = "/ResetPwByMail", produces = "text/html;charset=UTF-8")
    public String editPasswordByMail(ModelMap modelMap, Model model, String mailCode,String username,String newPassword) throws Exception
    {
        Result res;
        if (username == null) return objectMapper.writeValueAsString("EMPTY USERNAME");
        String trueCode = (String) modelMap.get("resetCode");
        if (trueCode.equals(mailCode))
        {
            userService.editPassword((int)modelMap.get("uid"),newPassword);
            res = Result.success("confirmSuccess");
            model.addAttribute("resetCode", "");
        } else
        {
            res = Result.error("Incorrect code", 400);
        }
        return objectMapper.writeValueAsString(res);
    }

    @RequestMapping(value = "/noAccess", produces = "application/json;charset=UTF-8")
    public String noAccess(SessionStatus status) throws Exception
    {
        return objectMapper.writeValueAsString(Result.error("权限不足", 403));
    }

    @RequestMapping(value = "/isLogin", produces = "application/json;charset=UTF-8")
    public String loginIN(SessionStatus status) throws Exception
    {
        return objectMapper.writeValueAsString(Result.error("是" + getCurrentUsername(), 403));
    }

    @PostMapping(value = "/confirmFzu", produces = "application/json;charset=UTF-8")
    public String confirmFzu(String No, String password, ModelMap modelMap) throws Exception
    {
        int uid = (int) modelMap.get("uid");
        if (userIdentityService.userIdentity(uid) != null)
        {
            return objectMapper.writeValueAsString(Result.error("该学号已经被认证"));
        }
        if (testJWCHPost(No, password))
        {
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setUid(uid);
            userIdentity.setNo(No);
            userIdentity.setSchool("福州大学");
            userIdentityService.saveIdentity(userIdentity);
            return objectMapper.writeValueAsString(Result.success("认证成功"));
        }
        return objectMapper.writeValueAsString(Result.error("认证失败"));
    }
}
