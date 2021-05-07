package com.jessie.SHMarket.service;

import com.jessie.SHMarket.entity.GoodsReport;

import java.time.LocalDateTime;
import java.util.List;

public interface GoodsReportService
{
    void newReport(GoodsReport goodsReport);

    List<GoodsReport> getUnSolvedReports();

    List<GoodsReport> getSolvedReports();

    void finishReport(int reportId, String result, LocalDateTime finishTime);

    int getReporter(int reportId);

    GoodsReport getReport(int reportId);
}
