package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.Order;

import java.util.List;

public interface OrderService
{
    void newOrder(Order order);

    void doneOrder(Order order);

    void deleteOrder(int oid);//修改订单好麻烦，让用户重新下单算了

    List<Order> getUserOrder(int uid);

    Order getOrder(int oid);

    Order getOrderByGid(int gid);

    int newestOrder();

    void expireOrder(int oid);
}
