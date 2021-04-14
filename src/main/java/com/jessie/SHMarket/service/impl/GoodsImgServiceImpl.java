package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.GoodsImgDAO;
import com.jessie.SHMarket.entity.GoodsImg;
import com.jessie.SHMarket.service.GoodsImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("goodsImgService")
public class GoodsImgServiceImpl implements GoodsImgService{
    @Autowired
    GoodsImgDAO goodsImgDAO;
    @Override
    public void newImg(GoodsImg goodsImg)
    {
        goodsImgDAO.newImg(goodsImg);
    }

    @Override
    public List<GoodsImg> getGoodsImg(int gid)
    {
        return goodsImgDAO.getGoodsImg(gid);
    }
}
