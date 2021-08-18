package com.qian.Entity.algorithm;


import java.sql.Timestamp;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FileReport {

	/*
	 * {
	 * 	"id": "1",
		"startTime": "2020-01-01 15:00:00",
		"endTime": "2020-01-01 15:00:00",
		"avgBeat": "75"
	 * }
	 * 
	 *  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
		`y` int(10) NOT NULL COMMENT '年份名称',
  		`m` int(10) NOT NULL COMMENT '月份名称',
		`d` int(10) NOT NULL COMMENT '天数名称',
		`avgBeat` FLOAT(10) NOT NULL COMMENT '健康指标',
		`startTime` timestamp(6) NOT NULL COMMENT '开始时间',
		`endTime` timestamp(6) NOT NULL COMMENT '结束时间',
	  	`pepole_id` int(11) NOT NULL COMMENT '所属人Id',
	 */
	private int id;
	private int y;
	private int m;
	private int d;
	private int avgBeat;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")//首先设定日期时间格式,HH指使用24小时制,hh是使用12小时制
	private Timestamp startTime;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;
	private int pepoleId;
}
