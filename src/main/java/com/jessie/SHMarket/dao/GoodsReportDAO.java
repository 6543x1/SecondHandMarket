package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.GoodsReport;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface GoodsReportDAO
{
    @Insert("insert into goods_report (uid, target, reason, status) VALUES (#{uid},#{target},#{reason},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "reportId", keyColumn = "reportId")
    void newReport(GoodsReport goodsReport);

    @Select("select * from goods_report where status=0")
    @Result(property = "nickName", column = "uid", one = @One(select = "com.jessie.SHMarket.dao.UserDAO.getNickNameByUid"))
    List<GoodsReport> getUnSolvedReports();

    @Select("select * from goods_report where status=1 order by finishTime desc")
    @Result(property = "nickName", column = "uid", one = @One(select = "com.jessie.SHMarket.dao.UserDAO.getNickNameByUid"))
    List<GoodsReport> getSolvedReports();

    @Update("update goods_report set status=1,result=#{result},finishTime=#{finishTime} where reportId=#{reportId}")
    void finishReport(int reportId, String result, LocalDateTime finishTime);

    @Select("select uid from goods_report where reportId=#{reportId}")
    int getReporter(int reportId);

    @Select("select * from  goods_report where reportId=#{reportId}")
    @Result(property = "nickName", column = "uid", one = @One(select = "com.jessie.SHMarket.dao.UserDAO.getNickNameByUid"))
    GoodsReport getReport(int reportId);
}
