package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.dao.Goods_commentDAO;
import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.GoodsComment;
import com.jessie.SHMarket.entity.Goods_More;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.GoodsService;
import com.jessie.SHMarket.service.MailService;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
    Goods_commentDAO goods_commentDAO;//不写中间那一坨接口了..
    @Autowired
    UserService userService;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/newGoods", produces = "application/json;charset=UTF-8")
    public String newGoods(Goods goods, HttpServletRequest request) throws Exception
    {
        System.out.println("收到商品信息");
        if (goods.getPrice() > 99999)
        {
            return JSON.toJSONString(Result.error("不允许价格超过99999"));
        }
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        if (goodsService.queryTodayGoods(uid) > 3)
        {
            return JSON.toJSONString(Result.error("每天最多上传3个商品"));
        }
        goods.setStatus(0);//0待审核 1未卖出 2卖出 -1非法商品
        goods.setUploadTime(LocalDateTime.now());
        goods.setUid(uid);
        //goods.setNickName(userService.getNickName(uid));
        int theGid = goodsService.saveGoods(goods);
        System.out.println(goods.getGid());
        return JSONObject.toJSONString(Result.success("商品信息设置成功", goodsService.getGoods(goods.getGid())));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/deleteGoods", produces = "application/json;charset=UTF-8")
    public String deleteGoods(int gid, HttpServletRequest request) throws Exception
    {
        if (goodsService.getUid(gid) != jwtTokenUtil.getUidFromToken(request.getHeader("token")))
        {
            return JSON.toJSONString(Result.error("非本人商品", 403));
        }
        goodsService.deleteGoods(gid);
        return JSONObject.toJSONString(Result.success("商品信息设置成功", goodsService.newestGoods()));
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/editGoods", produces = "application/json;charset=UTF-8")
    public String editGoods(Goods goods, HttpServletRequest request) throws Exception
    {
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
        List<Goods_More> list = goodsService.queryGoods();
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
                                @RequestParam(value = "keyValue") String keyValue)
    {
        PageHelper.startPage(pageNum, 10);
        List<Goods_More> list = goodsService.search(keyValue);
        return new PageInfo<>(list);
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

}
