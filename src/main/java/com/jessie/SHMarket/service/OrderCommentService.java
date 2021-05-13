package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.OrderComment;
import com.jessie.SHMarket.entity.OrderComment_Extended;

import java.util.List;

public interface OrderCommentService

{
    OrderComment getOrder(int oid);

    void newOrderComment(OrderComment orderComment);

    void updateBuyerComment(OrderComment orderComment);

    void updateSellerComment(OrderComment orderComment);

    List<OrderComment_Extended> getUserReceivedComments(int uid);

    List<OrderComment_Extended> getUserSentComments(int uid);
}
