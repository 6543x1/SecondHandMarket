package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.OrderComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface Order_commentDAO
{
    @Select("select * from order_comment where oid=#{oid}")
    OrderComment getOrder(int oid);

    @Insert("insert into order_comment (oid, buyer, seller) values (#{oid},#{buyer},#{seller})")
    void newOrderComment(OrderComment orderComment);

    @Update("update order_comment set b_Comment=#{b_Comment},b_Type=#{b_Type} where oid=#{oid}")
    void updateBuyerComment(OrderComment orderComment);

    @Update("update order_comment set s_Comment=#{b_Comment},s_Type=#{b_Type} where oid=#{oid}")
    void updateSellerComment(OrderComment orderComment);
}
