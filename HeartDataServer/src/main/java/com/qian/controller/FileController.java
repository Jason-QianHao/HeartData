package com.qian.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qian.service.FileService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FileController extends BaseController{

	@Autowired
	private FileService fileService;
	
	/*
	 * 接收文件并存储
	 */
	@RequestMapping("/recieveFile")
	public String recieveFile(@RequestParam(value = "file", required = false) List<MultipartFile> files) {
		try {
			fileService.addFile(files);
			log.info("FileController/recieveFile, 数据传输成功");
			return "数据传输成功";
		} catch (Exception e) {
			// TODO: handle exception
			log.info("FileController/recieveFile, 数据传输失败", e);
			return "数据传输失败";
		}
	}
	
	/*
	 * 接收一个数据点
	 */
	@RequestMapping("/recieveData")
	public String recieveData(HttpServletRequest req, int onedata, int pepoleid, String filepath) {
		String res = fileService.saveData(req, onedata, pepoleid, filepath);
		return res;
	}
	
	/**
	 * 测试通过本地磁盘生成报告
	 */
	@RequestMapping("/generateReport")
	public String generateReport() {
		try {
			fileService.generateReport();
			log.info("FileController/generateReport, 生成报告中");
			return "生成报告中";
		} catch (Exception e) {
			// TODO: handle exception
			log.info("FileController/generateReport, 生成报告失败");
			return "生成报告失败";
		}
	}
}
