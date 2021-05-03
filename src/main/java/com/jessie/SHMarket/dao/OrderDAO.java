package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderDAO
{
    @Insert("insert into user_order (generatedTime, status, buyer, seller, deliveryTime, cid, location, gid) values (#{generatedTime},#{status},#{buyer},#{seller},#{deliveryTime},#{cid},#{location},#{gid})")
    void newOrder(Order order);

    @Update("update user_order set doneTime=#{doneTime},status=#{status} where oid=#{oid}")
    void doneOrder(Order order);

    @Update("update user_order set status=-2 where oid=#{oid}")
    void expireOrder(int oid);

    @Delete("delete from user_order where oid=#{oid}")
    void deleteOrder(int oid);//修改订单好麻烦，让用户重新下单算了

    @Select("select * from user_order where buyer=#{buyer}")
    List<Order> getUserOrder(int uid);

    @Select("select * from user_order where oid=#{oid}")
    Order getOrder(int oid);

    @Select("select * from user_order where gid=#{gid}")
    Order getOrderByGid(int gid);

    @Select("select oid from user_order where gid=(SELECT LAST_INSERT_ID())")
    int newestOrder();
}
