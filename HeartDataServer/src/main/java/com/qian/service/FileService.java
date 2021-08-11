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

import com.qian.Entity.FileEntity;
import com.qian.algorithm.ReportRunable;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService extends BaseService{

	@Value("${fileBasePath}")
	private String fileBasePath;
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
