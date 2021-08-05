package com.qian.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qian.service.FileService;
import com.qian.utils.Constants;

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
        	log.info("FileController/getReport, 用户信息查询失败");
        	return Constants.ERROR;
        }
        String summryReport = fileService.getSummryReport(openid);
        if(summryReport.equals(Constants.ERROR)) {
        	log.info("FileController/getReport, 报告获取失败");
        	return Constants.ERROR;
        }
        log.info("FileController/getReport, 报告获取成功");
        return summryReport;
	}
}
