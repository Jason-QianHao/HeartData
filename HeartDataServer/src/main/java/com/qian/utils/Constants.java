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
	 * 小程序相关
	 */
	String APPID = "---";
	String APPSECRET = "----";
}
