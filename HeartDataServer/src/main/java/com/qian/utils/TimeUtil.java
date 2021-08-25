package com.qian.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	/*
	 * 创建当前时间戳
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(new Date());
	}
}
