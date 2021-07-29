package com.qian.Entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileEntity {
	/*
	 *   
	  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
	  `file_name` varchar(150) NOT NULL COMMENT '文件名称',
	  `file_url` varchar(100)  NOT NULL COMMENT '文件url',
	  `pepole_Id` int(11) NOT NULL COMMENT '所属人Id',
	  `client_created` timestamp(6) NOT NULL COMMENT '客户端创建时间',
  	  `server_created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '自动插入，服务器创建时间',
	 */
	private int id;
	private String fileName;
	private String fileUrl;
	private int pepoleId;
	private Timestamp clientCreated;
	private Timestamp serverCreated;
}
