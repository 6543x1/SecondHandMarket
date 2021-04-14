package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderDAO
{
    @Insert("insert into order (generatedTime,status,buyer,seller,deliveryTime,cid,location,gid) values(#{generatedTime},#{status},#{buyer},#{seller},#{deliveryTime},#{cid},#{location},#{gid})")
    void newOrder(Order order);
    @Update("update order set doneTime=#{doneTime},status=#{status} where oid=#{oid}")
    void doneOrder(Order order);
    @Delete("delete from order where oid=#{oid}")
    void deleteOrder(int oid);//修改订单好麻烦，让用户重新下单算了
    @Select("select * from order where uid=#{uid}")
    List<Order> getUserOrder(int uid);
}
