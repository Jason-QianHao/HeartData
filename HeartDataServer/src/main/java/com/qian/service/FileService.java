package com.qian.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qian.Entity.FileEntity;
import com.qian.mapper.FileMapping;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService {

	@Value("${fileBasePath}")
	private String fileBasePath;
	@Autowired
	private FileMapping fileMapping;

	/*
	 * 插入一个文件 文件名，以username命名 这里后面需要加入回滚操作。
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
		}
	}

	/*
	 * 查询某个人所有文件，生成总体报告 
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
	public String getSummryReport(int pepoleId) {
		List<FileEntity> allFilesByPepoleId = fileMapping.getAllFilesByPepoleId(pepoleId);
		// 读取文件
		// 算法分析
		return "";
	}

	/*
	 * fileName格式：pepoleId_yy-mm-dd_hh_mm_ss_.txt 0_2021-07-29_16_38_01_
	 */
	private FileEntity getEntityFromFile(String fileName) throws ParseException {
		String[] strs = fileName.split("_");
		String pepoleId = strs[0];
		String TimeStamp_Day = strs[1];
		String TimeStamp_Time = strs[2] + ":" + strs[3] + ":" + strs[4];
		// 分割时间戳，建立文件夹
		String[] arr = TimeStamp_Day.split("-"); // [yy, mm, dd]
//		isExist(fileBasePath + arr[0]);
//		isExist(fileBasePath + arr[0] + "/" + arr[1]);
		isExist(fileBasePath + arr[0] + "/" + arr[1] + "/" + arr[2]);
		// 组合文件类
		FileEntity fileEntity = new FileEntity();
		fileEntity.setPepoleId(Integer.valueOf(pepoleId));
		fileEntity.setFileUrl(fileBasePath + arr[0] + "/" + arr[1] + "/" + arr[2] + "/" + fileName);
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
