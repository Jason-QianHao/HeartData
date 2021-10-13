package com.qian.Entity.algorithm;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class DayReport {

	/*
	 * "d" : "1",
       "imgurl": "/static/imgs/reports/riqi1.png",
       "isUsed": "true",
       "HealthIndex" : "9.9",
       "fileLists": [FileReport1, FileReport2]
       
        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
		`y` int(10) NOT NULL COMMENT '年份名称',
  		`m` int(10) NOT NULL COMMENT '月份名称',
		`d` int(10) NOT NULL COMMENT '天数名称',
  		`imgurl` varchar(100)  NOT NULL COMMENT '图片url',
		`isUsed` tinyint(1) NOT NULL COMMENT '当日是否使用',
		`health_index` FLOAT(10) NOT NULL COMMENT '健康指标',
  		`pepole_id` int(11) NOT NULL COMMENT '所属人Id',
	 */
	private int id;
	private int y;
	private int m;
	private int d;
	private String imgurl;
	// 1 true 0 false
	private boolean isUsed;
	private float healthIndex;
	private int pepoleId;
	private List<FileReport> fileReportLists;
}
