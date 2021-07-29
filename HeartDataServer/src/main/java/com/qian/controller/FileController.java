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
public class FileController {

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
}
