package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.configuration.RedisUtil;
import com.jessie.SHMarket.entity.*;
import com.jessie.SHMarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class AdminController
{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AdminOperationService adminOperationService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    GoodsReportService goodsReportService;

    @PostMapping(value = "/testAdminController", produces = "application/json;charset=UTF-8")
    public String testAdmin() throws Exception
    {
        return objectMapper.writeValueAsString(Result.success("您是管理员，可以进行管理员操作。"));
    }

    @PostMapping(value = "/deleteOrderTruly", produces = "application/json;charset=UTF-8")
    public String deleteOrderTruly(int oid, String reason, HttpServletRequest request) throws Exception
    {
        String token = request.getHeader("token");
        int operator = jwtTokenUtil.getUidFromToken(token);
        orderService.deleteOrder(oid);
        adminOperationService.newOperation(new AdminOperation(operator, "删除订单", 0, oid, LocalDateTime.now(), reason));
        return objectMapper.writeValueAsString(Result.success("订单已彻底删除"));
    }

    @PostMapping(value = "/getUncheckedGoods", produces = "application/json;charset=UTF-8")
    public PageInfo<Goods_Extended> getGoods(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        PageHelper.startPage(pageNum, 10);
        List<Goods_Extended> list = goodsService.getUncheckedGoods();
        return new PageInfo<Goods_Extended>(list);
    }

    @PostMapping(value = "/deleteGoods", produces = "application/json;charset=UTF-8")
    public String deleteGoods(int gid, String reason, HttpServletRequest request) throws Exception
    {
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        String token = request.getHeader("token");
        int operator = jwtTokenUtil.getUidFromToken(token);
        goodsService.deleteGoods(gid);
        adminOperationService.newOperation(new AdminOperation(operator, "删除不合格商品", goodsService.getGoods(gid).getUid(), gid, LocalDateTime.now(), reason));
        redisUtil.saveUserMessage(goodsService.getUid(gid), new UserMessage("你的一个商品" + "#{" + gid + "}" + "未能通过审核", "审核消息", LocalDateTime.now()));
        return objectMapper.writeValueAsString(Result.success("不合格商品已删除"));
    }

    @PostMapping(value = "/passGoods", produces = "application/json;charset=UTF-8")
    public String passGoods(int gid, HttpServletRequest request) throws Exception
    {
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        String token = request.getHeader("token");
        int uid = jwtTokenUtil.getUidFromToken(token);
        goodsService.updateGoods(1, gid);
        int target = goodsService.getUid(gid);
        adminOperationService.newOperation(new AdminOperation(uid, "通过合格商品", target, gid, LocalDateTime.now(), "PASS"));
        redisUtil.saveUserMessage(target, new UserMessage("你的一个商品" + "#{" + gid + "}" + "已经通过审核了", "审核消息", LocalDateTime.now()));
        mailService.newMessage("你的一个商品已经通过审核了", userService.getMailAddr(target), "你的商品通过审核了，快去APP里看看吧~");
        return objectMapper.writeValueAsString(Result.success("商品通过审核"));
    }

    @PostMapping(value = "/punishUser", produces = "application/json;charset=UTF-8")
    public String punishUser(int uid, @RequestParam(value = "score", defaultValue = "999") int score, String reason, HttpServletRequest request) throws Exception
    {
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        String token = request.getHeader("token");
        int operator = jwtTokenUtil.getUidFromToken(token);
        if (userService.getUser(uid).getRole().equals("admin"))
        {
            return objectMapper.writeValueAsString(Result.error("禁止管理员内斗"));
        }
        User user = userService.getUser(uid);
        adminOperationService.newOperation(new AdminOperation(operator, "信誉分操作", uid, score, LocalDateTime.now(), reason));
        if (score >= 999)
        {
            userService.setStatus(uid, 0);
            userService.updatePunishedScore(uid, score);
            mailService.newMessage(user.getUsername() + "你号没了", user.getMailAddr(), "你号被封了，详情联系客服");
            return objectMapper.writeValueAsString(Result.success("封号成功"));
        } else
        {
            redisUtil.saveUserMessage(uid, new UserMessage("管理员扣除了你的信誉分" + "#{" + score + "}" + "分，有疑问联系管理员", "信誉分消息", LocalDateTime.now()));
            mailService.newMessage(user.getUsername() + ",管理员操作了你的信誉分", user.getMailAddr(), "你号被扣了" + score + "，详情联系客服");
            userService.plusStatus(uid, -score);
        }
        adminOperationService.newOperation(new AdminOperation(operator, "信誉分操作", uid, score, LocalDateTime.now(), reason));
        return objectMapper.writeValueAsString(Result.success("操作成功"));
    }

    @PostMapping(value = "/recoverUser", produces = "application/json;charset=UTF-8")
    public String recoverUser(int uid, String reason, HttpServletRequest request) throws Exception
    {
        //前端要单独做一个页面给管理员用，不然误调用方法不好恢复
        String token = request.getHeader("token");
        int operator = jwtTokenUtil.getUidFromToken(token);
        User user = userService.getUser(uid);
        if (userService.getUser(uid).getStatus() > 25)
        {
            return objectMapper.writeValueAsString(Result.error("无需恢复用户身份"));
        }
        userService.setStatus(uid, 25);
        adminOperationService.newOperation(new AdminOperation(operator, "恢复用户", uid, 25, LocalDateTime.now(), reason));
        mailService.newMessage(user.getUsername() + "用户,管理员恢复了你的号", user.getMailAddr(), "下次不要再做违规操作,详情联系客服");
        return objectMapper.writeValueAsString(Result.success("操作成功"));
    }

    @PostMapping(value = "/getAnAdminOperations", produces = "application/json;charset=UTF-8")
    public PageInfo<AdminOperation> getAnAdminOperations(int uid, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        PageHelper.startPage(pageNum, 10);
        List<AdminOperation> list = adminOperationService.getAnAdminOperations(uid);
        return new PageInfo<AdminOperation>(list);
    }

    @PostMapping(value = "/getUserAllOperations", produces = "application/json;charset=UTF-8")
    public PageInfo<AdminOperation> getUserAllOperations(int uid, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        PageHelper.startPage(pageNum, 10);
        List<AdminOperation> list = adminOperationService.getAllOperations(uid);
        return new PageInfo<AdminOperation>(list);
    }

    @PostMapping(value = "/getOperationsByType", produces = "application/json;charset=UTF-8")
    public PageInfo<AdminOperation> getUserAllOperations(String operation, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        PageHelper.startPage(pageNum, 10);
        List<AdminOperation> list = adminOperationService.getOperationsByType(operation);
        return new PageInfo<AdminOperation>(list);
    }

    @PostMapping(value = "/getReports", produces = "application/json;charset=UTF-8")
    public PageInfo<GoodsReport> getReports(@RequestParam(defaultValue = "false") boolean solved, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, HttpServletRequest request)
    {
        PageHelper.startPage(pageNum, 10);
        List<GoodsReport> list;
        if (solved)
        {
            list = goodsReportService.getUnSolvedReports();
        } else
        {
            list = goodsReportService.getSolvedReports();
        }
        return new PageInfo<GoodsReport>(list);
    }

    @PostMapping(value = "/solveReports", produces = "application/json;charset=UTF-8")
    public String solveReport(int reportId, String result, HttpServletRequest request)
    {
        goodsReportService.finishReport(reportId, result, LocalDateTime.now());//时间还没写
        mailService.newMessage("你的举报进度有更新", userService.getMailAddr(goodsReportService.getReporter(reportId)), result);
        redisUtil.saveUserMessage(goodsReportService.getReporter(reportId), new UserMessage("你的举报#{" + reportId + "}结果处理如下" + result, "举报消息", LocalDateTime.now()));
        return JSON.toJSONString(Result.success("处理成功"));
    }

}
