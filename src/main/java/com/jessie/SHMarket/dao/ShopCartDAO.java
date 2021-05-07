package com.jessie.SHMarket.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShopCartDAO
{
    @Insert("insert into shop_cart (uid, data) VALUES (#{uid},#{data}) on duplicate key update data=#{data}")
    void saveShopCart(int uid, int data);

    @Select("select data from shop_cart where uid=#{uid}")
    String getShopCart(int uid);

}
