package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Goods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsDAO
{
    @Insert("insert into goods (description,label,brand,quality,uid,status,uploadTime,price) values (#{description},#{label},#{brand},#{quality},#{uid},#{status},#{uploadTime},#{price})")
    void saveGoods(Goods goods);
    @Select("select * from goods where status=1")
    List<Goods> queryGoods();
    @Select("select * from goods where status=0")
    List<Goods> getUncheckedGoods();
    @Update("update goods set status=#{status} where gid =#{gid}")
    void updateGoods(@Param("status") int status,@Param("gid") int gid);
    @Update("update goods set status=-1 where gid=#{gid}")//假删除，实际设置status=-1，仅有在数据库中可查看
    void deleteGoods(int gid);
}
