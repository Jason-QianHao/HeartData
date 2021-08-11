package com.qian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.algorithm.Report;
import com.qian.mapper.DayReportMapping;
import com.qian.mapper.MonthReportMapping;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService extends BaseService {

	@Autowired
	protected DayReportMapping dayReportMapping;
	
	/*
	 * 1. 查询某个人所有文件，生成总体报告report 2. 包括每年的基本信息，每年前2个月的缩略信息 格式JSON { "data" : [ {
	 * "year": "2020", "month": [ { "m": "1", "imgurl":
	 * "/static/imgs/reports/riqi1.png", "title": "1月报告", "description": "1月报告，良好",
	 * "analysis": { "HealthIndex": "8.0", "Other": "" }, "dayLists":[ { "d" : "1",
	 * "imgurl": "/static/imgs/reports/riqi1.png", "isUsed": "true", "HealthIndex" :
	 * "9.9", "fileLists":[ { "id": "1", "createdTime": "2020-01-01 15:00:00",
	 * "avgBeat": "75" }, { "id": "2", "createdTime": "2020-01-01 16:00:00",
	 * "avgBeat": "80" }, { "id": "3", "createdTime": "2020-01-01 17:00:00",
	 * "avgBeat": "60" } ] } ] } ] } ] };
	 */
	public String getSummryReport(String openId) {
		try {
			// 从redis中，用openId提取pepoleId
			// 。。。
			// 这里先直接从数据库查询
			int pepoleId = wxUserService.getId(openId);
			if (pepoleId == -1) {
				log.info("ReportService/getSummryReport, 用户openid查询失败，获取健康报告失败");
				return Constants.ERROR;
			}
			// 查询用户使用年份
			List<String> allYearsByPepoleId = fileMapping.getAllYearsByPepoleId(pepoleId);
			// 查询前两个月的信息并封装
			JSONObject report = new JSONObject();
			JSONArray yearArray = new JSONArray();
			for (String year : allYearsByPepoleId) {
				// 组合简略报告
				JSONObject smallYearReport = new JSONObject();
				smallYearReport.put(Constants.YEAR, year);

				JSONArray monthArray = new JSONArray();
				List<MonthReport> front2months = monthReportMapping.getFront2months(Integer.valueOf(year), pepoleId);
				for (MonthReport mR : front2months) {
					monthArray.add(Report.monthReportJSON(mR));
				}

				smallYearReport.put(Constants.MONTH, monthArray);
				yearArray.add(smallYearReport);
			}
			report.put(Constants.DATA, yearArray);
			log.info("ReportService/getSummryReport, 获取健康报告成功" + report.toJSONString());
			// 算法分析
			return report.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			log.info("ReportService/getSummryReport, 获取健康报告失败", e);
			return Constants.ERROR;
		}
	}

	/*
	 * 根据openid/pepoleid查询某年所有月缩略报告
	 */
	public String getYearReport(String year, String openId) {
		try {
			// 从redis中，用openId提取pepoleId
			// 。。。
			// 这里先直接从数据库查询
			int pepoleId = wxUserService.getId(openId);
			if (pepoleId == -1) {
				log.info("ReportService/getYearReport, 用户openid查询失败，获取年度健康报告失败");
				return Constants.ERROR;
			}
			// 查询前两个月的信息并封装
			JSONObject report = new JSONObject();
			JSONArray yearArray = new JSONArray(); // 小程序端只使用数组的第一个值

			// 组合年度报告
			JSONObject yearReport = new JSONObject();
			yearReport.put(Constants.YEAR, year);

			JSONArray monthArray = new JSONArray();
			List<MonthReport> allMonthsByYear = monthReportMapping.getAllMonthsByYear(Integer.valueOf(year), pepoleId);
			for (MonthReport mR : allMonthsByYear) {
				monthArray.add(Report.monthReportJSON(mR));
			}

			yearReport.put(Constants.MONTH, monthArray);
			yearArray.add(yearReport);
			report.put(Constants.DATA, yearArray);
			log.info("ReportService/getYearReport, 获取年度健康报告成功" + report.toJSONString());
			// 算法分析
			return report.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			log.info("ReportService/getYearReport, 获取年度健康报告失败", e);
			return Constants.ERROR;
		}
	}
	
	/*
	 * 根据openid/pepoleid查询某年某月所有天缩略报告
	 */
	public String getMonthReport(String year, String month, String openId) {
		return "";
	}
}
