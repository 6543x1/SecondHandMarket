package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.OrderComment;
import com.jessie.SHMarket.entity.OrderComment_Extended;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface Order_commentDAO
{
    @Select("select * from order_comment where oid=#{oid}")
    OrderComment getAnOrderComment(int oid);

    @Insert("insert into order_comment (oid, buyer, seller) values (#{oid},#{buyer},#{seller})")
    void newOrderComment(OrderComment orderComment);

    @Update("update order_comment set b_Comment=#{b_Comment},b_Type=#{b_Type} where oid=#{oid}")
    void updateBuyerComment(OrderComment orderComment);

    @Update("update order_comment set s_Comment=#{b_Comment},s_Type=#{b_Type} where oid=#{oid}")
    void updateSellerComment(OrderComment orderComment);

    @Select("select o.*,uo.gid from order_comment o join user_order uo on uo.oid = o.oid where o.seller=#{uid}")
    @Results(id = "Comment_WithGoodsInfo", value = {
            @Result(property = "goods", column = "gid", one = @One(select = "com.jessie.SHMarket.dao.GoodsDAO.getGoods"))
    })
    List<OrderComment_Extended> getUserReceivedComments(int uid);

    @Select("select o.*,uo.gid from order_comment o join user_order uo on uo.oid = o.oid where o.buyer=#{uid}")
    @ResultMap("Comment_WithGoodsInfo")
    List<OrderComment_Extended> getUserSentComments(int uid);
}
