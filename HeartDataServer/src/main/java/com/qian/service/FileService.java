package com.qian.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
	 * 插入一个文件 文件名，以username命名
	 * 这里后面需要加入回顾操作。
	 */
	public String addFile(List<MultipartFile> files) {
		try {
			for (MultipartFile f : files) {
				FileEntity entityFromFile = getEntityFromFile(f.getOriginalFilename());
				// 根据每个人的pepoleId分文件夹存储
				// 根据day对每个人的文件分文件夹存储
				String url = entityFromFile.getFileName();
				try {
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(url)));
					out.write(f.getBytes());
					out.flush();
				} catch (FileNotFoundException e) {
					log.info("上传文件失败 FileNotFoundException：" + e.getMessage());
				} catch (IOException e) {
					log.info("上传文件失败 IOException：" + e.getMessage());
				}
				// 插入数据库
				fileMapping.addFile(entityFromFile);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("添加文件失败", e);
			return Constants.FAILCODE;
		}
		return Constants.SUCCESSCODE;
	}

	/*
	 * 查询某个人所有文件，生成总体报告
	 */
	public String getSummryReport(int pepoleId) {
		List<FileEntity> allFilesByPepoleId = fileMapping.getAllFilesByPepoleId(pepoleId);
		// 读取文件
		// 算法分析
		return "";
	}
	
	/*
	 * fileName格式：pepoleId-Day-count.txt
	 */
	private FileEntity getEntityFromFile(String fileName) {
		String[] arr = fileName.split("-");
		isExist(fileBasePath + arr[0]);
		isExist(fileBasePath + arr[0] + "/" + arr[1]);
		FileEntity fileEntity = new FileEntity();
		fileEntity.setPepoleId(Integer.valueOf(arr[0]));
		fileEntity.setFileUrl(fileBasePath + arr[0] + "/" + arr[1] + "/" +  fileName);
		fileEntity.setFileName(fileName);
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
