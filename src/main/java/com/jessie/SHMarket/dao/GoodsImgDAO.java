package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.GoodsImg;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsImgDAO
{
    @Insert("insert into goods_img (name,gid,uid,path) values (#{name},#{gid},#{uid},#{path})")
    void newImg(GoodsImg goodsImg);//艹｝写成了)idea还不报错。。。

    @Select("select * from goods_img where gid=#{gid}")
    List<GoodsImg> getGoodsImg(int gid);

    @Delete("delete from goods_img where name=#{name} and uid=#{uid}")
    void deleteImg(@Param("name") String name,@Param("uid") int uid);

    @Select("select * from (select * from goods_img where gid=#{gid}) as `gi*` limit 1 offset #{index}")
    GoodsImg getImgByIndex(@Param("index") int index, @Param("gid") int gid);
}
