package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.GoodsImg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}
