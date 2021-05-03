package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.Goods_commentDAO;
import com.jessie.SHMarket.entity.GoodsComment;
import com.jessie.SHMarket.service.GoodsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("goodsCommentService")
public class GoodsCommentServiceImpl implements GoodsCommentService
{
    @Autowired
    Goods_commentDAO goods_commentDAO;

    @Override
    public void newComment(GoodsComment goodsComment)
    {
        goods_commentDAO.newComment(goodsComment);
    }

    @Override
    public List<GoodsComment> getComments(int gid)
    {
        return goods_commentDAO.getComments(gid);
    }
}
