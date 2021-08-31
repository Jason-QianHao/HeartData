package com.qian.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.algorithm.DayReport;
import com.qian.Entity.algorithm.FileReport;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.algorithm.Report;
import com.qian.mapper.DayReportMapping;
import com.qian.mapper.FileReportMapping;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService extends BaseService {

	@Autowired
	private DayReportMapping dayReportMapping;
	@Autowired
	private FileReportMapping fileReportMapping;
	
	/*
	 * 1. 查询某个人所有文件，生成总体报告report 
	 * 2. 包括每年的基本信息，每年前2个月的缩略信息 格式JSON 
	 */
	public String getSummryReport(int pepoleId) {
		try {
			// 查询用户使用年份
			List<String> allYearsByPepoleId = monthReportMapping.getAllYearsByPepoleId(pepoleId);
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
					monthArray.add(Report.toReportJSON(mR));
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
	public String getYearReport(String year, int pepoleId) {
		try {
			// 查询前两个月的信息并封装
			JSONObject report = new JSONObject();
			JSONArray yearArray = new JSONArray(); // 小程序端只使用数组的第一个值

			// 组合年度报告
			JSONObject yearReport = new JSONObject();
			yearReport.put(Constants.YEAR, year);

			JSONArray monthArray = new JSONArray();
			List<MonthReport> allMonthsByYear = monthReportMapping.getAllMonthsByYear(Integer.valueOf(year), pepoleId);
			for (MonthReport mR : allMonthsByYear) {
				monthArray.add(Report.toReportJSON(mR));
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
	public String getMonthReport(String year, String month, int pepoleId) {
		try {
			// 查询该月所有“天报告”的信息并封装
			JSONObject report = new JSONObject();
			JSONArray yearArray = new JSONArray(); // 小程序端只使用数组的第一个值

			// 组合详细月报告
			JSONObject yearReport = new JSONObject();
			yearReport.put(Constants.YEAR, year);

			JSONArray monthArray = new JSONArray();
				// 查询该月报告
			MonthReport monthReportByYearAndMonth = monthReportMapping.getMonthReportByYearAndMonth(Integer.valueOf(year), 
					Integer.valueOf(month), pepoleId);
				// 查询该月所有日报告
			List<DayReport> allDayReportByYearAndMonth = dayReportMapping.getAllDayReportByYearAndMonth(Integer.valueOf(year), 
					Integer.valueOf(month), pepoleId);
			monthReportByYearAndMonth.setDayLists(allDayReportByYearAndMonth);
			monthArray.add(Report.toReportJSON(monthReportByYearAndMonth));
			
			yearReport.put(Constants.MONTH, monthArray);
			yearArray.add(yearReport);
			report.put(Constants.DATA, yearArray);
			log.info("ReportService/getMonthReport, 获取详细月健康报告成功" + report.toJSONString());
			// 算法分析
			return report.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			log.info("ReportService/getMonthReport, 获取详细月健康报告失败", e);
			return Constants.ERROR;
		}
	}

	/*
	 * 根据openid/pepoleid查询某年某月某天所有的文件报告
	 */
	public String getDayReport(String year, String month, String day, int pepoleId) {
		try {
			// 查询某天所有文件报告的信息并封装
			JSONObject report = new JSONObject();
			
				// 组合年报告
			JSONObject yearReport = new JSONObject();
			yearReport.put(Constants.YEAR, year);
			
				// 组合月报告
					// 新建月报告，月报告信息不用传输客户端，不查询数据库
			MonthReport monthReportByYearAndMonth = new MonthReport();
					// 查询该月该日报告
			DayReport dayReportByYearAndMonthAndDay = dayReportMapping.getDayReportByYearAndMonthAndDay(Integer.valueOf(year), 
					Integer.valueOf(month), Integer.valueOf(day), pepoleId);
					// 查询改日所有文件报告
			List<FileReport> allFileReport = fileReportMapping.getAllFileReport(Integer.valueOf(year), 
					Integer.valueOf(month), Integer.valueOf(day), pepoleId);
					// 组合字段
			dayReportByYearAndMonthAndDay.setFileReportLists(allFileReport);
			List<DayReport> dayLists = new ArrayList<DayReport>();
			dayLists.add(dayReportByYearAndMonthAndDay);
			monthReportByYearAndMonth.setDayLists(dayLists);
			JSONArray monthArray = new JSONArray();
			monthArray.add(Report.toReportJSON(monthReportByYearAndMonth));
			
			yearReport.put(Constants.MONTH, monthArray);
			JSONArray yearArray = new JSONArray(); // 小程序端只使用数组的第一个值
			yearArray.add(yearReport);
			report.put(Constants.DATA, yearArray);
			log.info("ReportService/getDayReport, 获取详细日健康报告成功" + report.toJSONString());
			// 算法分析
			return report.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			log.info("ReportService/getMonthReport, 获取详细日健康报告失败", e);
			return Constants.ERROR;
		}
	}
}
