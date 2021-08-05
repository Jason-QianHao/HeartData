package com.qian.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.FileEntity;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.algorithm.Report;
import com.qian.algorithm.ReportRunable;
import com.qian.mapper.FileMapping;
import com.qian.mapper.MonthReportMapping;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService {

	@Value("${fileBasePath}")
	private String fileBasePath;
	@Autowired
	private FileMapping fileMapping;
	@Autowired
	private MonthReportMapping monthReportMapping;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;

	/*
	 * 插入一个文件 文件名，以username命名 
	 * 支持回滚操作。
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addFile(List<MultipartFile> files) throws IOException, ParseException {
		for (MultipartFile f : files) {
			FileEntity entityFromFile = getEntityFromFile(f.getOriginalFilename());
			// 根据每个人的pepoleId分文件夹存储
			// 根据day对每个人的文件分文件夹存储
			String url = entityFromFile.getFileUrl();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(url)));
			out.write(f.getBytes());
			out.flush();
			// 插入数据库
			fileMapping.addFile(entityFromFile);
			// 后台生成报告
			generateReport(entityFromFile);
		}
	}
	
	/*
	 * 根据当日情况，生成报告，
	 * 并修改月报告数据。
	 * 
	 * 可以设置为定时任务每天晚上12点执行，并开启线程进行处理
	 */
	public void generateReport(FileEntity fileEntity) {
		Runnable reportRunable = new ReportRunable(fileEntity, monthReportMapping);
		threadPoolExecutor.execute(reportRunable);
	}

	/*
	 * 1. 查询某个人所有文件，生成总体报告report 
	 * 2. 包括每年的基本信息，每年前2个月的缩略信息
	 * 格式JSON 
	 * { "data" : [ 
	 * 	{ "year": "2020", 
	 * 	  "month": [ 
	 * 		{ "m": "1",
	 * 		  "imgurl": "/static/imgs/reports/riqi1.png", 
	 * 		  "title": "1月报告", 
	 * 	  	  "description": "1月报告，良好", 
	 * 		  "analysis": { 
	 * 			"HealthIndex": "8.0", 
	 * 			"Other": "" 
	 * 		  }, 
	 * 		  "dayLists":[ 
	 * 			{
	 * 			  "d" : "1", 
	 * 		 	  "imgurl": "/static/imgs/reports/riqi1.png", 
	 * 			  "isUsed": "true",
	 * 			  "HealthIndex" : "9.9", 
	 * 			  "fileLists":[ 
	 * 				{ "id": "1", 
	 * 				  "createdTime": "2020-01-01 15:00:00", 
	 * 				  "avgBeat": "75" 
	 * 				}, { "id": "2", "createdTime":
	 * "2020-01-01 16:00:00", "avgBeat": "80" }, { "id": "3", "createdTime":
	 * "2020-01-01 17:00:00", "avgBeat": "60" } ] } ] } ] } ] };
	 */
	public String getSummryReport(String openId) {
		try {
			// 从redis中，用openId提取pepoleId、
			// 。。。
			// 这里先直接从数据库查询
			int pepoleId = wxUserService.getId(openId);
			if(pepoleId == -1) {
				log.info("FileService/getSummryReport, 用户openid查询失败，获取健康报告失败");
				return Constants.ERROR;
			}
			// 查询用户使用年份
			List<String> allYearsByPepoleId = fileMapping.getAllYearsByPepoleId(pepoleId);
			// 查询前两个月的信息并封装
			JSONObject report = new JSONObject();
			JSONArray yearArray = new JSONArray();
			for(String year : allYearsByPepoleId) {
				// 组合简略报告
				JSONObject smallYearReport = new JSONObject();
				smallYearReport.put(Constants.YEAR, year);
				
				JSONArray monthArray = new JSONArray();
				List<MonthReport> front2months = monthReportMapping.getFront2months(Integer.valueOf(year), pepoleId);
				for(MonthReport mR : front2months) {
					monthArray.add(Report.monthReportJSON(mR));
				}
				
				smallYearReport.put(Constants.MONTH, monthArray);
				yearArray.add(smallYearReport);
			}
			report.put(Constants.DATA, yearArray);
			log.info("FileService/getSummryReport, 获取健康报告成功" + report.toJSONString());
			// 算法分析
			return report.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			log.info("FileService/getSummryReport, 获取健康报告失败", e);
			return Constants.ERROR;
		}
	}

	/*
	 * fileName格式：pepoleId_yy-mm-dd_hh_mm_ss_.txt 0_2021-07-29_16_38_01_.txt
	 */
	private FileEntity getEntityFromFile(String fileName) throws ParseException {
		String[] strs = fileName.split("_");
		String pepoleId = strs[0];
		String TimeStamp_Day = strs[1];
		String TimeStamp_Time = strs[2] + ":" + strs[3] + ":" + strs[4];
		// 分割时间戳，建立文件夹
		String[] arr = TimeStamp_Day.split("-"); // [yy, mm, dd]
		String filePath = fileBasePath + pepoleId + "/" + arr[0] + "/" + arr[1] + "/" + arr[2];
		isExist(filePath);
		// 组合文件类
		FileEntity fileEntity = new FileEntity();
		fileEntity.setYear(arr[0]);
		fileEntity.setMonth(arr[1]);
		fileEntity.setDay(arr[2]);
		fileEntity.setPepoleId(Integer.valueOf(pepoleId));
		fileEntity.setFileUrl(filePath + "/" + fileName);
		fileEntity.setFileName(fileName);
		fileEntity.setClientCreated(new Timestamp(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(TimeStamp_Day + " " + TimeStamp_Time).getTime()));
		return fileEntity;
	}

	private void isExist(String path) {
		File folder = new File(path);
		if (!folder.exists() && !folder.isDirectory()) {
			folder.mkdirs();
			log.info("创建文件夹: " + path);
		}
	}
}
