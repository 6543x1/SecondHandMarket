package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.dao.Goods_commentDAO;
import com.jessie.SHMarket.entity.*;
import com.jessie.SHMarket.service.*;
import com.jessie.SHMarket.utils.JwtTokenUtil;
import com.jessie.SHMarket.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class GoodsController
{
    @Autowired
    GoodsService goodsService;
    @Autowired
    MailService mailService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    Map<Integer, String> theNewMap;
    @Autowired
    Goods_commentDAO goods_commentDAO;//不写中间那一坨接口了,反正这个功能也被砍了23333..。
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    GoodsReportService goodsReportService;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/newGoods", produces = "application/json;charset=UTF-8")
    public String newGoods(Goods goods, HttpServletRequest request) throws Exception
    {
        System.out.println("收到商品信息");
        if (goods.getPrice() < 0 || goods.getPrice() > 99999)
        {
            return JSON.toJSONString(Result.error("不允许价格超过99999或为负数"));
        }
        goods.setPrice((int) (goods.getPrice() * 100) / 100.0);
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        if (goodsService.queryTodayGoods(uid) > 300)
        {//为了方便测试就修改成300了
            return JSON.toJSONString(Result.error("每天最多上传3个商品"));
        }
        goods.setStatus(0);//0待审核 1未卖出 2卖出 -1非法商品
        goods.setUploadTime(LocalDateTime.now());
        goods.setUid(uid);
        //goods.setNickName(userService.getNickName(uid));
        try
        {
            goodsService.saveGoods(goods);
        } catch (Exception e)
        {
            e.printStackTrace();
            return JSONObject.toJSONString(Result.error("请前端看下是不是参数写错了或者没写"));
        }
        //System.out.println(goods.getGid());
        return JSONObject.toJSONString(Result.success("商品信息设置成功", goodsService.getGoods(goods.getGid())));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/deleteGoods", produces = "application/json;charset=UTF-8")
    public String deleteGoods(int gid, HttpServletRequest request) throws Exception
    {
        Goods goods = goodsService.getGoods(gid);
        if (goods.getUid() != jwtTokenUtil.getUidFromToken(request.getHeader("token")))
        {
            return JSON.toJSONString(Result.error("非本人商品", 403));
        }
        if (goods.getStatus() != 1 && orderService.getOrderByGid(gid).getStatus() != 11)
        {
            return JSON.toJSONString(Result.error("已卖出的商品但未完成交易或异常的商品不可删除", 403));
        }
        goodsService.deleteGoods(gid);
        return JSONObject.toJSONString(Result.success("商品删除成功"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/editGoods", produces = "application/json;charset=UTF-8")
    public String editGoods(Goods goods, HttpServletRequest request) throws Exception
    {
        if (goods.getPrice() < 0 || goods.getPrice() > 99999)
        {
            return JSON.toJSONString(Result.error("不允许价格超过99999或为负数"));
        }
        if (goodsService.getUid(goods.getGid()) != jwtTokenUtil.getUidFromToken(request.getHeader("token")))
        {
            return JSON.toJSONString(Result.error("非本人商品，禁止操作", 403));
        }
        goodsService.editGoods(goods);
        return JSON.toJSONString(Result.success("操作成功,商品信息更新完成"));
    }

    @RequestMapping(value = "/getGoods", produces = "application/json;charset=UTF-8")
    public PageInfo getGoods(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum)
    {
        PageHelper.startPage(pageNum, 10);
        List<Goods_Extended> list = goodsService.queryGoods();
        return new PageInfo<>(list);
    }

    @RequestMapping(value = "/getUserGoods", produces = "application/json;charset=UTF-8")
    public PageInfo getUserGoods(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, int uid, HttpServletRequest request)
    {

        PageHelper.startPage(pageNum, 10);
        List<Goods> list;
        if (uid == jwtTokenUtil.getUidFromToken(request.getHeader("token")))
            list = goodsService.getUserGoods(uid, true);
        else
        {
            list = goodsService.getUserGoods(uid, false);
        }
        return new PageInfo<>(list);
    }

    @RequestMapping(value = "/searchGoods", produces = "application/json;charset=UTF-8")
    public PageInfo searchGoods(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "keyValue") String keyValue,
                                @RequestParam(value = "keyValue", defaultValue = "default") String type, HttpServletRequest request)
    {
        System.out.println("KEY=" + keyValue);
        PageHelper.startPage(pageNum, 10);
        List<Goods_Extended> list = goodsService.search(keyValue, type);
        if (request.getHeader("token") != null && !"".equals(request.getHeader("token")))
        {
            int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
            redisUtil.saveUserHistoryKey(uid, list.get(0).getLabel());
        }
        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @RequestMapping(value = "/recommend", produces = "application/json;charset=UTF-8")
    public String recommend(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        LinkedHashMap<String, String> hashMap = redisUtil.getUserHistoryKey(uid);
        if (hashMap == null)
        {
            return null;
        } else
        {
            ArrayList<Goods_Extended> list = new ArrayList<>();
            for (String target : hashMap.values())
            {
                list.add(goodsService.getRecommendGoods(target));
            }
            return JSONArray.toJSONString(list);
        }

    }

    @RequestMapping(value = "/getMaps", produces = "application/json;charset=UTF-8")
    public String getMaps()
    {
        for (String x : theNewMap.values())
        {
            System.out.println(x);
        }
        return "success";
    }

    @GetMapping(value = "/getByLabel", produces = "application/json;charset=UTF-8")
    public PageInfo getMaps(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "label") String label)
    {
        PageHelper.startPage(pageNum, 10);
        List<Goods> list = goodsService.getGoodsByLabel(label);
        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @RequestMapping(value = "/setTestGoods", produces = "application/json;charset=UTF-8")
    public String setTestGoods() throws Exception
    {
        Goods goods = new Goods();
        goods.setPrice(999999.99);
        goods.setLabel("test/theGoodsTest");
        goods.setUploadTime(LocalDateTime.now());
        goods.setStatus(1);
        goods.setUid(1);
        for (int i = 1; i <= 15; i++)
        {
            goods.setDescription("这是测试商品:" + i);
            goods.setQuality(theNewMap.get((int) (Math.random() * 10) % 5));
            goodsService.saveGoods(goods);
        }
        return JSON.toJSONString(Result.success("设置测试商品成功"));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/commentGoods", produces = "application/json;charset=UTF-8")
    public String comment(String comment, int gid, @RequestParam(value = "visited", defaultValue = "0") int visited, HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        int replyTo = goodsService.getUid(gid);
        GoodsComment goodsComment = new GoodsComment();
        goodsComment.setContent(comment);
        goodsComment.setGid(gid);
        goodsComment.setReviewer(uid);
        goodsComment.setReplyTo(replyTo);
        goodsComment.setReviewer_nickName(userService.getNickName(uid));
        goodsComment.setReplyTo_nickName(userService.getNickName(replyTo));
        goodsComment.setVisited(visited);
        goods_commentDAO.newComment(goodsComment);
        mailService.newMessage("你发布的商品收到了新评论", userService.getMailAddr(replyTo),
                "你的商品收到了来自" + goodsComment.getReviewer_nickName()
                        + "的评论：”" + goodsComment.getContent() + "“;可以在应用中回复他哦。"
        );
        return JSON.toJSONString(Result.success("评论成功"));
    }

    @GetMapping(value = "/getComments", produces = "application/json;charset=UTF-8")
    public PageInfo getComments(int gid, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum)
    {
        PageHelper.startPage(pageNum, 10);
        List<GoodsComment> list = goods_commentDAO.getComments(gid);
        return new PageInfo<>(list);
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/report", produces = "application/json;charset=UTF-8")
    public String report(int gid, String reason, HttpServletRequest request)
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        GoodsReport goodsReport = new GoodsReport(uid, gid, 0, reason, 0, 0, "", LocalDateTime.now(), "");
        try
        {
            goodsReportService.newReport(goodsReport);
        } catch (Exception e)
        {
            return JSON.toJSONString(Result.error("发生错误,举报失败"));
        }

        return JSON.toJSONString(Result.success("举报成功", goodsReport));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/getReport", produces = "application/json;charset=UTF-8")
    public String getReport(int reportId, HttpServletRequest request)
    {
        int uid = jwtTokenUtil.getUidFromToken(request.getHeader("token"));
        if (goodsReportService.getReporter(reportId) != uid)
        {
            return JSON.toJSONString(Result.error("非本人举报", 403));
        }
        return JSON.toJSONString(goodsReportService.getReport(reportId));
    }

    @GetMapping(value = "/getAnGood", produces = "application/json;charset=UTF-8")
    public Goods getAnGood(int gid, HttpServletRequest request)
    {
        Goods goods = goodsService.getGoods(gid);
            return goods;
    }

}
