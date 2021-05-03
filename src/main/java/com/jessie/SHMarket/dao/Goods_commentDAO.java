package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.GoodsComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Goods_commentDAO
{
    @Insert("insert into goods_comment (gid, reviewer, content, replyTo, reviewer_nickName, replyTo_nickName, visited) VALUES (#{gid},#{reviewer},#{content},#{replyTo},#{reviewer_nickName},#{replyTo_nickName},#{visited})")
    void newComment(GoodsComment goodsComment);

    @Select("select * from goods_comment where gid=#{gid}")
    List<GoodsComment> getComments(int gid);
}
