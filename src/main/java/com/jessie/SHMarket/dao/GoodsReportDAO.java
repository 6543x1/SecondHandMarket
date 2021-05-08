package com.jessie.SHMarket.dao;

import com.jessie.SHMarket.entity.GoodsReport;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
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

    @Select("select g.*,u.nickName from goods_report g join user u on u.uid = g.uid where g.status=0")
    @Results(id = "Report_WithSeller", value = {
            @Result(property = "targetUser", column = "target", one = @One(select = "com.jessie.SHMarket.dao.GoodsDAO.getSeller", fetchType = FetchType.EAGER))
    })
//好像这样写（如果column=uid不是g.uid)会导致UID=0，不知道是什么bug
    List<GoodsReport> getUnSolvedReports();

    @Select("select *,u.nickName from goods_report g join user u on u.uid = g.uid where g.status=1 order by g.finishTime desc")
    List<GoodsReport> getSolvedReports();

    @Update("update goods_report set status=1,result=#{result},finishTime=#{finishTime} where reportId=#{reportId}")
    void finishReport(int reportId, String result, LocalDateTime finishTime);

    @Select("select uid from goods_report where reportId=#{reportId}")
    int getReporter(int reportId);

    @Select("select g.*,u.nickName from goods_report g join user u on u.uid = g.uid where g.reportId=#{reportId}")
    GoodsReport getReport(int reportId);
}
