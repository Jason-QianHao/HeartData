package com.qian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.qian.Entity.algorithm.MonthReport;

public interface MonthReportMapping {

	/*
	 * 插入一个月份报告
	 */
	@Insert("insert into `month_report` values(null, #{y}, #{m}, #{imgurl}, #{title}, #{description}, #{healthIndex},"
			+ "#{pepoleId})")
	public void insertMonthReport(MonthReport monthReport);
	
	/*
	 * 判断月报告是否存在
	 */
	@Select("select id from `month_report` WHERE `y`=#{y} and `m`=#{m} and `pepole_id`=#{pepoleId}")
	public Integer isExist(@Param("y") int year, @Param("m") int month, @Param("pepoleId") int pepoleId);
	
	/*
	 * 根据openId查询一个人报告含有的：
	 * 所有年份
	 */
	@Select("select DISTINCT(`y`) from `month_report` where `pepole_id`=#{pepoleId}")
	public List<String> getAllYearsByPepoleId(@Param("pepoleId") int pepoleId);
	
	/*
	 * 根据年份查询所有月份前两个月报告
	 */
	@Select("select * from `month_report` WHERE `y`=#{y} and `pepole_id`=#{pepoleId}  ORDER BY m limit 2;")
	public List<MonthReport> getFront2months(@Param("y") int year, @Param("pepoleId") int pepoleId);
	
	/*
	 * 根据年份查询所有月份的月报告
	 */
	@Select("select * from `month_report` WHERE `y`=#{y} and `pepole_id`=#{pepoleId}  ORDER BY m;")
	public List<MonthReport> getAllMonthsByYear(@Param("y") int year, @Param("pepoleId") int pepoleId);
	
	/*
	 * 查询某月的月报告
	 */
	@Select("select * from `month_report` WHERE `y`=#{y} and `m`=#{m} and `pepole_id`=#{pepoleId}")
	public MonthReport getMonthReportByYearAndMonth(@Param("y") int year, @Param("m") int month, @Param("pepoleId") int pepoleId);
	
	/*
	 * 修改月健康指数
	 */
	@Update("update `month_report` set health_index=#{avg} where `y`=#{y} and `m`=#{m} and `pepole_id`=#{pepoleId}")
	public void updateMonthHealthIndex(@Param("avg")float avg, @Param("y") int year, @Param("m") int month, @Param("pepoleId") int pepoleId);
}
