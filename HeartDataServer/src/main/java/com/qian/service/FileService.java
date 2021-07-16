package com.qian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qian.Entity.FileEntity;
import com.qian.mapper.FileMapping;

@Service
public class FileService {

	@Autowired
	private FileMapping fileMapping;
	
	/*
	 * 查询某个人所有文件，生成总体报告
	 */
	public String getSummryReport(int pepoleId) {
		List<FileEntity> allFilesByPepoleId = fileMapping.getAllFilesByPepoleId(pepoleId);
		// 读取文件
		// 算法分析
		return "";
	}
}
