package com.qian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.qian.Entity.algorithm.FileReport;

public interface FileReportMapping {

	/*
	 * 插入一个记录
	 */
	@Insert("insert into `file_report` values(null, #{y}, #{m}, #{d}, #{avgBeat}, "
			+ "#{startTime}, #{endTime}, #{pepoleId});")
	public void insert(FileReport filReport);
	
	/*
	 * 查询某天的所有文件记录
	 */
	@Select("select * from `file_report` where `y`=#{y} and `m`=#{m} and `d`=#{d} and "
			+ "`pepole_id`=#{pepoleId} ORDER BY `startTime`;")
	public List<FileReport> getAllFileReport(@Param("y") int year, @Param("m") int month, @Param("d") int day,
			@Param("pepoleId") int pepoleId);
}
