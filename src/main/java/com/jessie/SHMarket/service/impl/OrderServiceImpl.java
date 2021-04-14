package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.OrderDAO;
import com.jessie.SHMarket.entity.Order;
import com.jessie.SHMarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService
{
    @Autowired
    OrderDAO orderDAO;
    @Override
    public void newOrder(Order order)//generatedTime,status,buyer,seller,deliveryTime,cid,location,gid
    {
        orderDAO.newOrder(order);
    }

    @Override
    public void doneOrder(Order order)
    {
        orderDAO.doneOrder(order);
    }

    @Override
    public void deleteOrder(int oid)
    {
        orderDAO.deleteOrder(oid);
    }

    @Override
    public List<Order> getUserOrder(int uid)
    {
        return orderDAO.getUserOrder(uid);
    }
}
