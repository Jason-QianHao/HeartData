package com.qian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.qian.Entity.algorithm.MonthReport;

public interface MonthReportMapping {

	/*
	 * 插入一个月份报告
	 */
	@Insert("insert into `month_report` values(#{y}, #{m}, #{imgurl}, #{title}, #{description}, #{healthIndex},"
			+ "#{pepoleId})")
	public void insertMonthReport(MonthReport monthReport);
	
	/*
	 * 判断月报告是否存在
	 */
	@Select("select id from `month_report` WHERE `y`=#{y} and `m`=#{m}")
	public Integer isExist(@Param("y") int year, @Param("m") int month);
	
	/*
	 * 根据年份查询所有月份前两个月份
	 */
	@Select("select * from `month_report` WHERE `y`=#{y} and `pepole_id`=#{pepoleId}  ORDER BY m limit 2;")
	public List<MonthReport> getFront2months(@Param("y") int year, @Param("pepoleId") int pepoleId);
}
