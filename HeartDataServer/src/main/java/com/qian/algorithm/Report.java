package com.qian.algorithm;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.utils.Constants;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Report {

	/*	生成月报告简略JSON串
	 * 		  "m": "1",
	 * 		  "imgurl": "/static/imgs/reports/riqi1.png", 
	 * 		  "title": "1月报告", 
	 * 	  	  "description": "1月报告，良好", 
	 * 		  "analysis": { 
	 * 			"HealthIndex": "8.0", 
	 * 			"Other": "" 
	 * 		  }, 
	 */
	public static JSONObject monthReportJSON(MonthReport monthReport) {
		JSONObject month = new JSONObject();
		month.put(Constants.M, monthReport.getM());
		month.put(Constants.IMGURL, monthReport.getImgurl());
		month.put(Constants.TITLE, monthReport.getTitle());
		month.put(Constants.DESCRIPTION, monthReport.getDescription());
		JSONObject analysis = new JSONObject();
		analysis.put(Constants.HEALTHINDEX, monthReport.healthIndex);
		analysis.put(Constants.OTHER, monthReport.other);
		month.put(Constants.ANALYSIS, analysis);
		return month;
	}
}
