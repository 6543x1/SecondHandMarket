package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.GoodsDAO;
import com.jessie.SHMarket.entity.Goods;
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
    public void saveGoods(Goods goods)
    {
        goodsDAO.saveGoods(goods);
    }

    @Override
    public Goods getGoods(int gid)
    {
        return goodsDAO.getGoods(gid);
    }

    @Override
    public List<Goods> queryGoods()
    {
        return goodsDAO.queryGoods();
    }

    @Override
    public List<Goods> getUncheckedGoods()
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
    public List<Goods> search(String keyValue)
    {
        return goodsDAO.search(keyValue);
    }
}
