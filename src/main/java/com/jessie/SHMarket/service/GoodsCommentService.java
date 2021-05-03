package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.GoodsComment;

import java.util.List;

public interface GoodsCommentService
{
    void newComment(GoodsComment goodsComment);

    List<GoodsComment> getComments(int gid);
}
