package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.GoodsReportDAO;
import com.jessie.SHMarket.entity.GoodsReport;
import com.jessie.SHMarket.service.GoodsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("goodsReportService")
public class GoodsReportServiceImpl implements GoodsReportService
{
    @Autowired
    GoodsReportDAO goodsReportDAO;

    @Override
    public void newReport(GoodsReport goodsReport)
    {
        goodsReportDAO.newReport(goodsReport);
    }

    @Override
    public int getReporter(int reportId)
    {
        return goodsReportDAO.getReporter(reportId);
    }

    @Override
    public List<GoodsReport> getSolvedReports()
    {
        return goodsReportDAO.getSolvedReports();
    }

    @Override
    public List<GoodsReport> getUnSolvedReports()
    {
        return goodsReportDAO.getUnSolvedReports();
    }

    @Override
    public GoodsReport getReport(int reportId)
    {
        return goodsReportDAO.getReport(reportId);
    }

    @Override
    public void finishReport(int reportId, String result, LocalDateTime finishTime)
    {
        goodsReportDAO.finishReport(reportId, result, finishTime);
    }
}
