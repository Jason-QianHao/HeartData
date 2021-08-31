package com.qian.service;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qian.Entity.FileEntity;
import com.qian.algorithm.ReportRunable;
import com.qian.utils.Constants;
import com.qian.utils.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService extends BaseService {

	@Value("${fileBasePath}")
	private String fileBasePath;
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;

	/*
	 * 插入一个文件 文件名，以username命名 支持回滚操作。
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
	 * 功能：将数据插入文件
	 * 返回：
	 * 	1. 插入成功 filepath
	 * 	2. 文件操作失败 Constants.FAILCODE
	 * 备注：小程序最后需要发送结束符号 -1
	 */
	public String saveData(HttpServletRequest req, int onedata, int pepoleid, String filepath) {
		// 判读是否需要新建文件
		String res = "";
		BufferedOutputStream out = null;
		try {
			if(filepath == null || filepath.equals("")) {
				String time = TimeUtil.getCurrentTime();
				String[] strs = time.split(" ");
				// fileName格式：pepoleId_yy-mm-dd_hh_mm_ss_.txt 1_2021-07-29_16_38_01_.txt
				String[] hours = strs[1].split(":");
				String filename = pepoleid + "_" + strs[0] + "_" + hours[0] + "_" 
							  + hours[1] + "_" + hours[2] + "_.txt";
				String[] arr = strs[0].split("-");
				String path = fileBasePath + pepoleid + "/" + arr[0] + "/" + arr[1] + "/" + arr[2];
				isExist(path);
				filepath = path + "/" + filename;
			}
			// 发送结束
			if(onedata == -1) {
				try {
					FileEntity addFileInfo = addFileInfo(filepath);
					// 生成结束时间戳
					String endTime = TimeUtil.getCurrentTime();
					addFileInfo.setServerCreated(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(endTime).getTime()));
					// 插入数据库
					fileMapping.addFile(addFileInfo);
					log.info("FileService/saveData, 插入数据库成功");
				} catch (Exception e) {
					// TODO: handle exception
					log.info("FileService/saveData, 插入数据库失败");
				}
			}else {
				out = new BufferedOutputStream(new FileOutputStream(new File(filepath), true));
				// 这里写入文件不能直接写int会没有反应
				out.write(String.valueOf(onedata + "\r\n").getBytes());
				out.flush();
				log.info("FileService/saveData, 写入文件成功");
				res = filepath;
			}
		} catch (Exception e) {
			log.info("FileService/saveData, 写入文件失败", e);
			res = Constants.FAILCODE;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				log.info("FileService/saveData, 关闭文件失败", e);
				res = Constants.FAILCODE;
			}
		}
		return res;
	}

	/*
	 * fileName格式：pepoleId_yy-mm-dd_hh_mm_ss_.txt 1_2021-07-29_16_38_01_.txt
	 */
	private FileEntity addFileInfo(String fileName) throws ParseException {
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
	
	/*
	 * 根据当日情况，生成报告， 并修改月报告数据。
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
			log.info("FileService/isExist, 创建文件夹: " + path);
		}
	}
}
