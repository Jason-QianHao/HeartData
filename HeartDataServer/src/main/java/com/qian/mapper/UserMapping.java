package com.qian.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.qian.Entity.UserEntity;

public interface UserMapping {

	/*
	 * 数据库插入新用户
	 */
	@Insert("insert into `users` (`username`, `password`, `phone`, `openId`) values(#{username}, #{password}, #{phone}, "
			+ "#{openId})")
	void addUser(UserEntity user);
	
	/*
	 * 根据openId查询用户
	 */
	@Select("select * from `users` where openId = #{openId}")
	UserEntity getUserByOpenId(@Param("openId") String openId);
	
	/*
	 * 根据username查询用户
	 */
	@Select("select * from `users` where username = #{username}")
	UserEntity getUserByUsername(@Param("username") String username);
	
	/*
	 * 根据phone查询用户
	 */
	@Select("select * from `users` where phone = #{phone}")
	UserEntity getUserByPhone(@Param("phone") String phone);
}
