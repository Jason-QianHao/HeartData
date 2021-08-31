package com.qian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qian.service.ReportService;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ReportController extends BaseController{

	@Autowired
	private ReportService reportService;
	
	/*
	 * 查询用户健康报告首页
	 */
	@RequestMapping("/getReport")
	public String getReport(int pepoleid) {
		// 查询用户是否存在
        if(pepoleid == 0) {
        	// 是新用户           
            return Constants.FAILCODE;
        }
        String summryReport = reportService.getSummryReport(pepoleid);
        if(summryReport.equals(Constants.ERROR)) {
        	log.info("ReportController/getReport, 报告获取失败");
        	return Constants.ERROR;
        }
        log.info("ReportController/getReport, 报告获取成功");
        return summryReport;
	}
	
	/*
	 * 查询用户年度健康报告
	 */
	@RequestMapping("/getYearReport")
	public String getYearReport(String year, int pepoleid) {
		// 查询用户是否存在
        if(pepoleid == 0) {
        	// 是新用户           
            return Constants.FAILCODE;
        }
        String yearReport = reportService.getYearReport(year, pepoleid);
        if(yearReport.equals(Constants.ERROR)) {
        	log.info("ReportController/getYearReport, 报告获取失败");
        	return Constants.ERROR;
        }
        log.info("ReportController/getYearReport, 报告获取成功");
        return yearReport;
	}
	
	/*
	 * 查询用户详细月健康报告
	 */
	@RequestMapping("/getMonthReport")
	public String getMonthReport(String year, String month, int pepoleid) {
		// 查询用户是否存在
        if(pepoleid == 0) {
        	// 是新用户           
            return Constants.FAILCODE;
        }
        String monthReport = reportService.getMonthReport(year, month, pepoleid);
        if(monthReport.equals(Constants.ERROR)) {
        	log.info("ReportController/getMonthReport, 报告获取失败");
        	return Constants.ERROR;
        }
        log.info("ReportController/getMonthReport, 报告获取成功");
        return monthReport;
	}
	
	/*
	 * 查询用户详细日健康报告
	 */
	@RequestMapping("/getDayReport")
	public String getDayReport(String year, String month, String day, int pepoleid) {
		// 查询用户是否存在
        if(pepoleid == 0) {
        	// 是新用户           
            return Constants.FAILCODE;
        }
        String dayReport = reportService.getDayReport(year, month, day, pepoleid);
        if(dayReport.equals(Constants.ERROR)) {
        	log.info("ReportController/getDayReport, 报告获取失败");
        	return Constants.ERROR;
        }
        log.info("ReportController/getDayReport, 报告获取成功");
        return dayReport;
	}
}
