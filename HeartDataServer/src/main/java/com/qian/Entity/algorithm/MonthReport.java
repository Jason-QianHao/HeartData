package com.qian.Entity.algorithm;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MonthReport {

	/*
	 * "m": "1",
       "imgurl": "/static/imgs/reports/riqi1.png",
       "title": "1月报告",
       "description": "1月报告，良好",
       "analysis": {
	       "HealthIndex": "8.0",
	       "Other": ""
       },
       "dayLists": [DayReport1, DayReport2...]
	 */
	/*
	 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
		 `y` int(10) NOT NULL COMMENT '年份名称',
  		 `m` int(10) NOT NULL COMMENT '月份名称',
  		 `imgurl` varchar(100)  NOT NULL COMMENT '图片url',
		 `title` varchar(50) NOT NULL COMMENT '报告标题',
		 `description` varchar(100) NOT NULL COMMENT '简要描述',
		 `health_index` int(10) NOT NULL COMMENT '健康指标',
  		 `pepole_id` int(11) NOT NULL COMMENT '所属人Id',
	 */
	
	private int id;
	private int y;
	private int m;
	private String imgurl;
	private String title;
	private String description;
	// 分析
	public float healthIndex;
	public String other;
	
	private int pepoleId;
	private List<DayReport> dayLists;
}
