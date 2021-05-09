package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.GoodsDAO;
import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.GoodsAndSeller;
import com.jessie.SHMarket.entity.Goods_Extended;
import com.jessie.SHMarket.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService
{
    @Autowired
    private GoodsDAO goodsDAO;

    @Override
    public int saveGoods(Goods goods)
    {
        return goodsDAO.saveGoods(goods);
    }

    @Override
    public Goods_Extended getRecommendGoods(String key)
    {
        return goodsDAO.getRecommendGoods(key);
    }

    @Override
    public Goods getGoods(int gid)
    {
        return goodsDAO.getGoods(gid);
    }

    @Override
    public void editGoods(Goods goods)
    {
        goodsDAO.editGoods(goods);
    }

    @Override
    public int getUid(int gid)
    {
        return goodsDAO.getUid(gid);
    }

    @Override
    public List<Goods_Extended> queryGoods()
    {
        return goodsDAO.queryGoods();
    }

    @Override
    public List<Goods> getUserGoods(int uid, boolean isOwn)
    {
        if (!isOwn)
            return goodsDAO.getUserGoods(uid);
        else
            return goodsDAO.getOwnGoods(uid);
    }

    @Override
    public List<Goods_Extended> getUncheckedGoods()
    {
        return goodsDAO.getUncheckedGoods();
    }

    @Override
    public void updateGoods(int status, int gid)
    {
        goodsDAO.updateGoods(status, gid);
    }

    @Override
    public void deleteGoods(int gid)
    {
        goodsDAO.deleteGoods(gid);
    }

    @Override
    public List<Goods_Extended> search(String keyValue, String type)
    {
        if ("time".equals(type))
        {
            return goodsDAO.searchOrderByTime(keyValue);
        } else if ("price".equals(type))
        {
            return goodsDAO.searchOrderByPrice(keyValue);
        } else if ("relevancy".equals(type))
        {
            return goodsDAO.searchOrderByRelevancy(keyValue);
        } else
        {
            return goodsDAO.search(keyValue);
        }
    }

    @Override
    public int queryTodayGoods(int uid)
    {
        return goodsDAO.queryTodayGoods(uid);
    }

    @Override
    public Goods newestGoods()
    {
        return goodsDAO.newestGoods();
    }

    @Override
    public List<Goods> getGoodsByLabel(String label)
    {
        return goodsDAO.getGoodsByLabel(label);
    }

    @Override
    public void updateImgNum(int gid, int imgNum)
    {
        goodsDAO.updateImgNum(gid, imgNum);
    }

    @Override
    public List<GoodsAndSeller> getGoodsListWithBuyer()
    {
        return goodsDAO.getGoodsListWithBuyer();
    }

    @Override
    public Goods_Extended getGoodsFull(int gid)
    {
        return goodsDAO.getGoodsFull(gid);
    }
}
