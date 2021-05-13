package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.Order_commentDAO;
import com.jessie.SHMarket.entity.OrderComment;
import com.jessie.SHMarket.entity.OrderComment_Extended;
import com.jessie.SHMarket.service.OrderCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderCommentService")
public class OrderCommentServiceImpl implements OrderCommentService
{
    @Autowired
    private Order_commentDAO order_commentDAO;

    @Override
    public OrderComment getOrder(int oid)
    {
        return order_commentDAO.getAnOrderComment(oid);
    }

    @Override
    public void newOrderComment(OrderComment orderComment)
    {
        order_commentDAO.newOrderComment(orderComment);
    }

    @Override
    public void updateBuyerComment(OrderComment orderComment)
    {
        order_commentDAO.updateBuyerComment(orderComment);
    }

    @Override
    public void updateSellerComment(OrderComment orderComment)
    {
        order_commentDAO.updateSellerComment(orderComment);
    }

    @Override
    public List<OrderComment_Extended> getUserReceivedComments(int uid)
    {
        return order_commentDAO.getUserReceivedComments(uid);
    }

    @Override
    public List<OrderComment_Extended> getUserSentComments(int uid)
    {
        return order_commentDAO.getUserSentComments(uid);
    }
}
