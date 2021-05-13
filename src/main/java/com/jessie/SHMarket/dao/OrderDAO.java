package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Order;
import com.jessie.SHMarket.entity.OrderWithGoods;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderDAO
{
    @Insert("insert into user_order (generatedTime, status, buyer, seller, deliveryTime, cid, location, gid) values (#{generatedTime},#{status},#{buyer},#{seller},#{deliveryTime},#{cid},#{location},#{gid})")
    @Options(useGeneratedKeys = true, keyProperty = "oid", keyColumn = "oid")
    void newOrder(Order order);

    @Update("update user_order set doneTime=#{doneTime},status=#{status} where oid=#{oid}")
    void doneOrder(Order order);

    @Update("update user_order set status=-2 where oid=#{oid}")
    void setGoodsStatusUnusual(int oid);


    @Delete("delete from user_order where oid=#{oid}")
    void deleteOrder(int oid);//修改订单好麻烦，让用户重新下单算了

    @Select("select * from user_order where buyer=#{buyer} order by generatedTime desc")
    List<Order> getUserOrder(int uid);

    @Select("select * from user_order where oid=#{oid}")
    Order getOrder(int oid);

    @Select("select * from user_order where gid=#{gid} and status>=0")
    Order getOrderByGid(int gid);//这个方法仅限于查找非异常的订单，避免查出多个来

    @Select("select buyer from user_order where oid=#{oid}")
    int getBuyer(int oid);

    @Select("select seller from user_order where oid=#{oid}")
    int getSeller(int oid);

    @Select("select oid,gid from user_order where buyer=#{uid} order by generatedTime desc")
    @Results(id = "OrderWithGoods", value = {
            @Result(property = "order", column = "oid", one = @One(select = "com.jessie.SHMarket.dao.OrderDAO.getAnOrderComment", fetchType = FetchType.EAGER)),
            @Result(property = "goods", column = "gid", one = @One(select = "com.jessie.SHMarket.dao.GoodsDAO.getGoods", fetchType = FetchType.EAGER))
    })
    List<OrderWithGoods> getBuyerOrderAndGoods(int uid);

    @Select("select oid,gid from user_order where seller=#{uid} order by generatedTime desc")
    @ResultMap("OrderWithGoods")
    List<OrderWithGoods> getSellerOrderWithGoods(int uid);
}
