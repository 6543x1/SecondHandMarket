package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.OrderComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderCommentService
{
    OrderComment getOrder(int oid);

    void newOrderComment(OrderComment orderComment);

    void updateBuyerComment(OrderComment orderComment);

    void updateSellerComment(OrderComment orderComment);
}
