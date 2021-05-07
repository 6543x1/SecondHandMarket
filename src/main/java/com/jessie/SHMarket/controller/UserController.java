package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.configuration.RedisUtil;
import com.jessie.SHMarket.entity.*;
import com.jessie.SHMarket.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;//从MD5直接升级Bcrypt，真香
    @Autowired
    private MailService mailService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserIdentityService userIdentityService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserTokenService userTokenService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private AdminOperationService adminOperationService;//仅用于查询用户被管理员操作记录

    // nohup java -jar /usr/shmarket-0.0.1-SNAPSHOT.jar &
    @PostMapping(value = "/ResetPw", produces = "application/json;charset=UTF-8")
    public String editPassword(String oldPassword, String newPassword) throws Exception
    {
        User thisUser = userService.getUser(getCurrentUsername());
        if (bCryptPasswordEncoder.matches(oldPassword, thisUser.getPassword()))
        {
            thisUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userService.editPassword(thisUser.getUid(), thisUser.getPassword());
            return JSON.toJSONString(Result.success("EditSuccess"));
        } else
        {
            return JSON.toJSONString(Result.error("WrongPassword", 403));
        }
    }

    //Spring Security获取当前用会话的户信息
    public static String getCurrentUsername()
    {
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal == null)
        {
            return null;
        }
        if (principal instanceof UserDetails)
        {
            UserDetails userDetails = (UserDetails) principal;
            username = userDetails.getUsername();
        } else
        {
            username = principal.toString();//????
        }
        return username;
    }

    @PostMapping(value = "/upload", produces = "text/html;charset=UTF-8")
    public String UploadImg(HttpServletRequest request, @RequestParam("upload") MultipartFile upload, HttpServletRequest HttpServletRequest) throws Exception
    {
//        System.out.println("上传头像");
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        String path = "/usr/tomcat/Img/" + getCurrentUsername();
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
            String userFileName = uid + getCurrentUsername() + "." + suffix;
            if (!theImgSuffix.containsValue(suffix))
            {
                throw new Exception("?");
            }
            User user = new User();
            user.setUid(uid);
            user.setImg_path(path + "/" + userFileName);
            upload.transferTo(new File(path, userFileName));
            userService.saveImg(user);
            System.out.println("头像保存成功，开始向数据库中更新用户数据");

        } catch (NullPointerException e)
        {
            e.printStackTrace();
            return JSON.toJSONString(Result.error("找不到文件的名字"));
        } catch (Exception e)
        {
            e.printStackTrace();
            return JSON.toJSONString(Result.error("未知错误"));
        }
        return JSON.toJSONString(Result.success("上传成功"));
    }

    @GetMapping(value = "/downPic", produces = "text/html;charset=UTF-8")
    public String down(@RequestParam(defaultValue = "0") int uid, HttpServletRequest request, HttpServletResponse response)
    {

        if (uid == 0)
        {
            try
            {
                String token = request.getHeader("token");
                uid = jwtTokenUtil.getUidFromToken(token);
            } catch (Exception e)
            {
                return JSON.toJSONString(Result.error("服务器不知道返回谁的头像"));
            }
        }
        User user = userService.getUser(uid);
        if ("".equals(user.getImg_path()) || user.getImg_path() == null)
        {
            return JSON.toJSONString(Result.error("当前请求用户没有头像", 404));
        }

        try
        {
            //获取页面输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //读取文件
            byte[] bytes = FileUtils.readFileToByteArray(new File(user.getImg_path()));
            //向输出流写文件
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition", "attachment;filename=" + uid + "_Img");
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

    @RequestMapping(value = "/loginSuccess", produces = "text/plain;charset=UTF-8")
    public String isLogin(Model model) throws Exception
    {
        String username = getCurrentUsername();
        if (username == null) return JSON.toJSONString(Result.error("服务器内部错误", 500));
        model.addAttribute("username", username);
        User user = userService.getUser(username);
        user.setPassword("加密也不给你看");
        model.addAttribute("uid", user.getUid());
        String userInfo = JSON.toJSONString(user);
        System.out.println(userInfo);
        Result result = Result.success("loginSuccess", user);
        return JSON.toJSONString(result);//jackson那个垃圾玩意老给我多出来\，还是阿里懂我
    }

    @PostMapping(value = "/userInfo", produces = "text/plain;charset=UTF-8")
    public String userInfo() throws Exception
    {
        String username = getCurrentUsername();
        User user = userService.getUser(username);
        return JSON.toJSONString(user);
    }

    @RequestMapping(value = "/loginError", produces = "text/plain;charset=UTF-8")
    public String loginError() throws Exception
    {
        return JSON.toJSONString(Result.error("密码不匹配或是已被封号", 400));
    }

    @PostMapping(value = "/Register", produces = "text/html;charset=UTF-8")
    public String register(User user) throws Exception
    {
        //说回来要是有人破解了链接给我服务器扔冲蝗核弹咋办 springboot有防御措施吗...
        if (user == null) return JSON.toJSONString(Result.error("无数据"));
        System.out.println(user);
        if (user.getUsername().length() >= 15 || user.getPassword().length() >= 30)
        {
            return JSON.toJSONString(Result.error("想扔内存核弹？"));
        }
        System.out.println("取得注册用数据，开始向数据库中写入数据...");
        if (userService.getUser(user.getUsername()) != null)
        {
            System.out.println("用户名已存在，请重试");
            return JSON.toJSONString(Result.error("用户名已存在", 500));
        }
        user.setStatus(100);
        user.setRole("user");
        if (user.getNickName() == null || "".equals(user.getNickName()))
        {
            user.setNickName(user.getUsername());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//换用Bcrypt，自动加盐真香
        user.setEvaluation(2);
        userService.saveUser(user);
        //int theUid = userService.newestUid();
        userTokenService.newUser(user.getUid(), user.getUsername());
        UserPortrait userPortrait = new UserPortrait(user.getUid());
        userService.newUserPortrait(userPortrait);
        redisUtil.set("ClearExtraStatus|" + userPortrait.getUid(), "ONE YEAR", 60 * 60 * 24 * 365);
        return JSON.toJSONString(Result.success("RegisterSuccess"));
    }

    @RequestMapping(value = "loginOut", produces = "application/json;charset=UTF-8")
    public String LoginOut(SessionStatus status) throws Exception
    {
        // status.setComplete();//将session重置
        return JSON.toJSONString(Result.success("loginOutSuccess"));
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value = "/checkAdmin", produces = "application/json;charset=UTF-8")
    public String IAMAdmin() throws Exception
    {
        return JSON.toJSONString(Result.success("你是管理员"));
    }

    @PostMapping(value = "/setMailAddr", produces = "application/json;charset=UTF-8")
    public String setMailAddress(String mailAddr, HttpServletRequest request) throws Exception
    {
        String username = getCurrentUsername();
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        if (username == null) return JSON.toJSONString(Result.error("找不到用户名(服务器出错联系管理员)"));
        if (mailAddr == null) return JSON.toJSONString(Result.error("邮箱是空的"));
        if (userService.getUser(username).getMailAddr() != null)
        {
            return JSON.toJSONString(Result.error("已经设置邮箱了，如果失效请联系管理员更改"));
        }
        String theCode = getRandomString();
        userTokenService.saveMailCode(username, theCode, mailAddr);
        mailService.sendResetPw(mailAddr, username + "的请求码(六位字符）:" + theCode);
        return JSON.toJSONString(Result.success("SuccessPlzConfirm"));
    }

    @PostMapping(value = "/confirmAddr", produces = "text/html;charset=UTF-8")
    public String confirmAddr(HttpServletRequest request, String mailCode) throws Exception
    {
        String token = request.getHeader("token");
        Result res;
        String username = getCurrentUsername();
        if (username == null) return JSON.toJSONString("EMPTY USERNAME");
        String trueCode = userTokenService.getMailCode(username);
        if (trueCode.equals(mailCode))
        {
            userService.setMailAddr(jwtTokenUtil.getUidFromToken(token), userTokenService.getTemp(username));//如果mailCode未清空则后续无法设置成功会被清掉
            res = Result.success("confirmSuccess");
            userTokenService.saveMailCode(username, "", "");
        } else
        {
            res = Result.error("Incorrect code", 400);
        }
        return JSON.toJSONString(res);
    }

    @PostMapping(value = "/sendMail", produces = "text/html;charset=UTF-8")
    public String SendPwMail(HttpServletRequest request, String username) throws Exception
    {
        if (username == null) return JSON.toJSONString(Result.error("找不到用户名(服务器出错联系管理员)"));
        String mailAddr;
        String theCode = getRandomString();
        try
        {
            mailAddr = userService.getUser(username).getMailAddr();
            if (mailAddr == null) throw new NullPointerException();
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            return JSON.toJSONString(Result.error("没有设置邮箱，联系客服找回", 404));
        }
        if (redisUtil.exists("MailCode|" + username))
        {
            ArrayList<String> data = redisUtil.get("MailCode|" + username, ArrayList.class);
            if (data.get(0) == "1")
            {
                redisUtil.delete("MailCode" + username);
                data.set(0, "2");
                redisUtil.set("MailCode|" + username, data, 6 * 60 * 60);
            } else
            {
                return JSON.toJSONString(Result.error("发过两次了不能再发（其实是懒），如果没收到联系管理员", 404));
            }
            //userTokenService.getMailCode(username) != null && ("2").equals(userTokenService.getTemp(username))
            return JSON.toJSONString(Result.success("已经发送两次了！请到邮箱查看邮件"));
        } else
        {
            ArrayList<String> data = new ArrayList<>();
            data.add("1");
            data.add(theCode);
            //userTokenService.saveMailCode(username, theCode, "1");
            redisUtil.set("MailCode|" + username, data, 2 * 60 * 60);
        }
        //mailService.sendResetPw(mailAddr, username + "的请求码(六位字符）:" + theCode);
        System.out.println(theCode);
        return JSON.toJSONString(Result.success("请到邮箱查看邮件"));
    }

    @PostMapping(value = "/ResetPwByMail", produces = "text/html;charset=UTF-8")
    public String editPasswordByMail(HttpServletRequest request, String mailCode, String username, String newPassword) throws Exception
    {
        Result res;
        if (username == null) return JSON.toJSONString("EMPTY USERNAME");
        try
        {


            ArrayList<String> data = redisUtil.get("MailCode|" + username, ArrayList.class);
            if (data.get(1).equals(mailCode))
            {
                userService.editPassword(userService.getUser(username).getUid(), bCryptPasswordEncoder.encode(newPassword));
                //userTokenService.saveMailCode(username, "", "");
                //我为什么要这么脑残的去用UID呢？？？？？？？？？？？？？？？？？？？？？？？？
                res = Result.success("confirmSuccess");
                redisUtil.delete("MailCode|" + username);
            } else
            {
                res = Result.error("Incorrect code", 400);
            }
        } catch (NullPointerException e)
        {
            res = Result.error("似乎验证码过期了或者不存在,请重新发送邮件试试", 404);
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(value = "/noAccess", produces = "application/json;charset=UTF-8")
    public String noAccess(SessionStatus status) throws Exception
    {
        return JSON.toJSONString(Result.error("权限不足", 403));
    }

    @RequestMapping(value = "/isLogin", produces = "application/json;charset=UTF-8")
    public String loginIN() throws Exception
    {
        try
        {
            return JSON.toJSONString(Result.success("是" + getCurrentUsername(), 403));
        } catch (NullPointerException e)
        {
            return JSON.toJSONString(Result.error("未检测到登录用户的信息"));
        }
    }

    @PostMapping(value = "/confirmFzu", produces = "application/json;charset=UTF-8")
    public String confirmFzu(String No, String Password, HttpServletRequest request) throws Exception
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
            return JSON.toJSONString(Result.error("该学号被认证"));
        }
        if (testJWCHPost(No, Password))
        {
            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setUid(uid);
            userIdentity.setNo(No);
            userIdentity.setSchool("福州大学");
            permissionService.setUserPermission(uid, 2);//授予普通用户的权限
            userIdentityService.saveIdentity(userIdentity);
            return JSON.toJSONString(Result.success("认证成功"));
        }
        return JSON.toJSONString(Result.error("认证失败,检查学号密码是否正确"));
    }

    @PostMapping(value = "/isIdentified", produces = "application/json;charset=UTF-8")
    public String isIdentified(HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        UserIdentity userIdentity = userIdentityService.userIdentity(uid);
        if (userIdentity != null)
        {
            return JSON.toJSONString(Result.success("用户已认证", userIdentity));
        }
        return JSON.toJSONString(Result.error("用户没有认证", 404));
    }

    @PostMapping(value = "/getAdminHistory", produces = "application/json;charset=UTF-8")
    public PageInfo<AdminOperation> getUserAllOperations(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        PageHelper.startPage(pageNum, 10);
        List<AdminOperation> list = adminOperationService.getAllOperations(uid);
        return new PageInfo<AdminOperation>(list);
    }

    @PostMapping(value = "/editNickName", produces = "application/json;charset=UTF-8")
    public String editNickName(String nickName, HttpServletRequest request)
    {
        if (nickName == null)
        {
            return JSON.toJSONString(Result.error("昵称不可以为空"));
        }
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        User user = new User();
        user.setUid(uid);
        user.setNickName(nickName);
        userService.setNickName(user);
        return JSON.toJSONString(Result.success("修改成功", userService.getUser(uid)));
    }

    @PostMapping(value = "/getUserMessages", produces = "application/json;charset=UTF-8")
    public String getMessage(HttpServletRequest request)
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        return redisUtil.getUserMessage(uid);
    }
}
