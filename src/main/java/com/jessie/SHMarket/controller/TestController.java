package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessie.SHMarket.entity.*;
import com.jessie.SHMarket.service.*;
import com.jessie.SHMarket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.jessie.SHMarket.service.impl.MailServiceImpl.getRandomString;

//本类中方法仅用于测试或批量生成测试数据，正式上线后即使被重复调用也不会影响数据库
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
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/testReg", produces = "text/html;charset=UTF-8")
    public String test() throws Exception
    {
        User theUser = new User();
        theUser.setUsername("Jessie");
        theUser.setPassword("123456");
        theUser.setNickName(getRandomString());
        theUser.setStatus(0);
        System.out.println(theUser);
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
                // ;
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

    @RequestMapping(value = "/testPermission", produces = "application/json;charset=UTF-8")
    public String Test() throws Exception
    {
        List<Permission> theList = permissionService.getAllUserPermissions(1);
        for (Permission x : theList)
        {
            System.out.println(x.toString());
        }
        return objectMapper.writeValueAsString(Result.success("查好了", theList));
    }

    @GetMapping(value = "/testRedis")
    public void testRedis()
    {


        redisUtil.set("Task_Test" + "|" + "MyArgus", "b", 10); // redisUtil是@Autowired注入进来的
        //基本格式：业务类型|所需参数(JSON)

        System.out.println("已经设置了一个10秒的任务");
    }

    @GetMapping(value = "testCombinedQuery", produces = "application/json;charset=UTF-8")
    public String combinedQuery()
    {
        List<GoodsAndSeller> list = goodsService.getGoodsListWithBuyer();
        for (GoodsAndSeller goodsAndSeller : list)
        {
            System.out.println(goodsAndSeller.toString());
        }
        Goods_Extended goods_extended = goodsService.getGoodsFull(1);
        System.out.println(goods_extended.toString());
        return JSON.toJSONString(list);
    }


    @GetMapping(value = "testRedisSaveMap", produces = "application/json;charset=UTF-8")
    public String testRedisSaveMap()
    {

        redisUtil.set("mailCode", 8, 10);
        return null;
    }

    @GetMapping(value = "testOrderWithGoods", produces = "application/json;charset=UTF-8")
    public String testOrderWithGoods()
    {
        List<OrderWithGoods> list = orderService.getBuyerOrderWithGoods(2);
        System.out.println(list);
        return JSONArray.toJSONString(list);
    }

    @GetMapping(value = "testRecommendGoods", produces = "application/json;charset=UTF-8")
    public String testRecommend(String key)
    {
        Goods_Extended goods_extended = goodsService.getRecommendGoods(key);
        return JSON.toJSONString(goods_extended);
    }

    @GetMapping(value = "testJSON", produces = "application/json;charset=UTF-8")
    public String testMyJSON(String key)
    {
        Goods goods = new Goods();
        goods.setQuality(null);
        return JSON.toJSONString(goods);
    }

}
