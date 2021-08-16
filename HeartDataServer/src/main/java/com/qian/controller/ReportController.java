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
	public String getReport(String openid) {
		// 查询用户是否存在
        String isNewWxUser = wxUserService.isNewWxUser(openid);
        if(isNewWxUser.equals(Constants.SUCCESSCODE)) {
        	// 是新用户           
            return Constants.FAILCODE;
        }else if(isNewWxUser.equals(Constants.ERROR)) {
        	// 查询失败
        	log.info("ReportController/getReport, 用户信息查询失败");
        	return Constants.ERROR;
        }
        String summryReport = reportService.getSummryReport(openid);
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
	public String getYearReport(String year, String openid) {
		// 查询用户是否存在
        String isNewWxUser = wxUserService.isNewWxUser(openid);
        if(isNewWxUser.equals(Constants.SUCCESSCODE)) {
        	// 是新用户           
            return Constants.FAILCODE;
        }else if(isNewWxUser.equals(Constants.ERROR)) {
        	// 查询失败
        	log.info("ReportController/getYearReport, 用户信息查询失败");
        	return Constants.ERROR;
        }
        String yearReport = reportService.getYearReport(year, openid);
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
	public String getMonthReport(String year, String month, String openid) {
		// 查询用户是否存在
        String isNewWxUser = wxUserService.isNewWxUser(openid);
        if(isNewWxUser.equals(Constants.SUCCESSCODE)) {
        	// 是新用户           
            return Constants.FAILCODE;
        }else if(isNewWxUser.equals(Constants.ERROR)) {
        	// 查询失败
        	log.info("ReportController/getMonthReport, 用户信息查询失败");
        	return Constants.ERROR;
        }
        String monthReport = reportService.getMonthReport(year, month, openid);
        if(monthReport.equals(Constants.ERROR)) {
        	log.info("ReportController/getMonthReport, 报告获取失败");
        	return Constants.ERROR;
        }
        log.info("ReportController/getMonthReport, 报告获取成功");
        return monthReport;
	}
}
