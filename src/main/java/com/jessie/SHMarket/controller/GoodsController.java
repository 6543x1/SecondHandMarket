package com.jessie.SHMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
    ObjectMapper objectMapper;
    @Autowired
    Map<Integer,String> theNewMap;
    @PreAuthorize("hasAnyAuthority('admin','user')")
    @PostMapping(value = "/newGoods",produces = "application/json;charset=UTF-8")
    public String newGoods(Goods goods,ModelMap modelMap)throws Exception{
        System.out.println("收到商品信息");
        int uid= (int) modelMap.get("uid");
        goods.setStatus(0);//0待审核 1未卖出 2卖出 -1非法商品
        goods.setUploadTime(LocalDateTime.now());
        goods.setUid((int)modelMap.get("uid"));
        goodsService.saveGoods(goods);
        return objectMapper.writeValueAsString(Result.success("上传商品信息成功"));
    }
    @RequestMapping(value = "/getGoods",produces = "application/json;charset=UTF-8")
    public PageInfo getGoods(@RequestParam(value = "pageNum", defaultValue="1") int pageNum, ModelMap modelMap){
        PageHelper.startPage(pageNum,10);
        List<Goods> list=goodsService.queryGoods();
        return new PageInfo<>(list);
    }
    @RequestMapping(value = "/getMaps",produces = "application/json;charset=UTF-8")
    public String getMaps(){
       for(String x: theNewMap.values()){
           System.out.println(x);
       }
       return "success";
    }
    @PreAuthorize("hasAnyAuthority('admin')")
    @RequestMapping(value = "/setTestGoods",produces = "application/json;charset=UTF-8")
    public String setTestGoods()  throws Exception{
       Goods goods=new Goods();
        goods.setPrice(999999.99);
        goods.setLabel("test/theGoodsTest");
        goods.setUploadTime(LocalDateTime.now());
        goods.setStatus(1);
        goods.setUid(1);
       for(int i=1;i<=15;i++){
       goods.setDescription("这是测试商品:"+i);
       goods.setQuality(theNewMap.get((int)(Math.random()*10)%5));
       goodsService.saveGoods(goods);
       }
       return objectMapper.writeValueAsString(Result.success("设置测试商品成功"));
    }
}
