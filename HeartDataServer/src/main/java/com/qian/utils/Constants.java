package com.qian.utils;

public interface Constants {

	/*
	 * 返回值
	 */
	String SUCCESSCODE = "200";
	String FAILCODE = "500";
	String ERROR = "error";
	
	/*
	 * Redis
	 */
	long LoginTime = 3 * 24 * 60 * 60L; // Redis缓存3天
	String OPENID = "OPENID";
	
	/*
	 * 报告相关
	 */
	String DATA = "data";
	String YEAR = "year";
	String MONTH = "month";
	String M = "m";
	String IMGURL = "imgurl";
	String TITLE = "title";
	String DESCRIPTION = "description";
	String ANALYSIS = "analysis";
	String HEALTHINDEX = "HealthIndex";
	String OTHER = "Other";
	String DAYLISTS = "dayLists";
	String D = "d";
	String ISUSED = "isUsed";
	String FILELISTS = "fileLists";
	String ID = "id";
	String STARTTIME = "startTime";
	String ENDTIME = "endTime";
	String AVGBEAT = "avgBeat";
}
