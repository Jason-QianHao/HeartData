package com.qian.algorithm;


import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.utils.Constants;


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
       		  "dayLists": [
       		  	  {
       		  	  	"d" : "1",
                    "imgurl": "/static/imgs/reports/riqi1.png",
                    "isUsed": "true",
                    "HealthIndex" : "9.9",
                    "fileLists":[
                            {
  								"id": "1",
                              	"startTime": "2020-01-01 15:00:00",
                              	"endTime": "2020-01-01 16:00:00",
                              	"avgBeat": "75"
                            },
                            FileReport2...
                     ]
	       		  }, 
	       		  DayReport2...
       		  ]
	 */
	public static JSONObject toReportJSON(MonthReport monthReport){
		JSONObject month = new JSONObject();
		month.put(Constants.M, monthReport.getM());
		month.put(Constants.IMGURL, monthReport.getImgurl());
		month.put(Constants.TITLE, monthReport.getTitle());
		month.put(Constants.DESCRIPTION, monthReport.getDescription());
		
		JSONObject analysis = new JSONObject();
		analysis.put(Constants.HEALTHINDEX, monthReport.healthIndex);
		analysis.put(Constants.OTHER, monthReport.other);
		month.put(Constants.ANALYSIS, analysis);
	
		month.put(Constants.DAYLISTS, monthReport.getDayLists());
		return month;
	}
}
