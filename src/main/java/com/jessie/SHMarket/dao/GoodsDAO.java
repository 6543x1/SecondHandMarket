package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.Goods;
import com.jessie.SHMarket.entity.GoodsAndSeller;
import com.jessie.SHMarket.entity.Goods_Extended;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsDAO
{
    @Insert("insert into goods (description,label,brand,quality,uid,status,uploadTime,price,contact,imgNum) values (#{description},#{label},#{brand},#{quality},#{uid},#{status},#{uploadTime},#{price},#{contact},#{imgNum})")
    @Options(useGeneratedKeys = true, keyProperty = "gid", keyColumn = "gid")
    int saveGoods(Goods goods);

    @Select("select * from goods where gid=#{gid}")
    Goods getGoods(int gid);

    @Update("update goods set description=#{description},label=#{label},brand=#{brand},quality=#{quality},price=#{price},contact=#{contact} where gid=#{gid} ")
    void editGoods(Goods goods);

    @Select("select uid from goods where  gid=#{gid}")
    int getUid(int gid);

    @Select("select t1.*,u.evaluation,u.nickName from goods t1 join user u on u.uid = t1.uid where t1.status=1")
    @Results(id = "Goods_More_Map", value = {
            @Result(property = "sellerEva", column = "evaluation"),
            @Result(property = "nickName", column = "nickName")
    })
    List<Goods_Extended> queryGoods();

    @Select("select * from goods where uid=#{uid} and status>=1")
    List<Goods> getUserGoods(int uid);

    @Select("select * from goods where uid=#{uid}")
    List<Goods> getOwnGoods(int uid);

    @Select("select g.*,u.evaluation,u.nickName from goods g join user u on u.uid = g.uid where g.status=0")
    @ResultMap(value = "Goods_More_Map")
    List<Goods_Extended> getUncheckedGoods();

    @Update("update goods set status=#{status} where gid =#{gid}")
    void updateGoods(@Param("status") int status, @Param("gid") int gid);

    @Update("update goods set status=-2 where gid=#{gid}")
//假删除，实际设置status=-1，仅有在数据库中可查看
    void deleteGoods(int gid);

    @Select("select t1.*,u.evaluation,u.nickName from goods t1 join user u on t1.uid = u.uid where match(t1.description) against(#{keyValue}) and t1.status=1 order by u.evaluation")
    @ResultMap(value = "Goods_More_Map")
    List<Goods_Extended> search(String keyValue);//默认为自然语言

    @Select("select t1.*,u.evaluation,u.nickName from goods t1 join user u on t1.uid = u.uid where match(t1.description) against(#{keyValue}) and t1.status=1 order by t1.uploadTime desc")
    @ResultMap(value = "Goods_More_Map")
    List<Goods_Extended> searchOrderByTime(String keyValue);

    @Select("select t1.*,u.evaluation,u.nickName from goods t1 join user u on t1.uid = u.uid where match(t1.description) against(#{keyValue}) and t1.status=1 order by t1.price")
    @ResultMap(value = "Goods_More_Map")
    List<Goods_Extended> searchOrderByPrice(String keyValue);

    @Select("select count(*) from goods where to_days(uploadTime) = to_days(now()) and uid=#{uid}")
    int queryTodayGoods(int uid);

    @Select("select * from goods where gid=(SELECT LAST_INSERT_ID())")
    Goods newestGoods();

    @Select("select * from goods where label=#{label} and status=1")
    List<Goods> getGoodsByLabel(String label);

    @Update("update goods set status=-1 where uid=#{uid} and status<=1")
    void banUser(int uid);

    @Update("update goods set imgNum=#{imgNum} where gid=#{gid}")
    void updateImgNum(@Param("gid") int gid, @Param("imgNum") int imgNum);

    @Select("select a.*,b.* from goods a join user b on a.uid=b.uid where a.status =1")
    @Results(id = "combinedMap", value = {
            @Result(property = "goods", column = "gid", one = @One(select = "com.jessie.SHMarket.dao.GoodsDAO.getGoods", fetchType = FetchType.EAGER)),
            @Result(property = "seller", column = "uid", one = @One(select = "com.jessie.SHMarket.dao.UserDAO.getUserByUid", fetchType = FetchType.EAGER))
    })
    List<GoodsAndSeller> getGoodsListWithBuyer();

    @Select("select g.*,u.evaluation,u.nickName from goods g,user u where g.gid=#{gid} and g.uid=u.uid;")
    @ResultMap("Goods_More_Map")
    Goods_Extended getGoodsFull(int gid);

    //select t1.* from goods t1 join `user` t2 on t1.uid = t2.uid where match(t1.description) against('+华为 -小米' in boolean mode)  order by t2.status desc
    //我发现，如果把结果按信誉分来排名，这样好像会造成比较复杂的结果
    @Select("SELECT t1.*,u.evaluation,u.nickName FROM goods as t1 join user u on u.uid = t1.uid where t1.gid>=(RAND()*((SELECT MAX(gid) FROM \n" +
            "goods)-(SELECT MIN(gid) FROM goods))+(SELECT MIN(gid) FROM goods)) and label =#{key} LIMIT 1;")
    @ResultMap("Goods_More_Map")
    Goods_Extended getRecommendGoods(String key);

}
