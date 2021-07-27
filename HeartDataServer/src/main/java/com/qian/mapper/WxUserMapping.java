package com.qian.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.qian.Entity.WxUserEntity;

public interface WxUserMapping {

	/*
	 * 插入一个微信用户
	 */
	@Insert("insert into `wxusers` (`open_id`, `nick_name`, `avatar_url`, `gender`, `city`, `province`) "
			+ "values(#{openId}, #{nickName}, #{avatarUrl}, #{gender}, #{city}, #{province})")
	public void insert(WxUserEntity wxUserEntity);
	
	/*
	 * 根据微信用户openId查询用户
	 * 这里不用返回用户所有信息，使用覆盖索引，提高效率
	 */
	@Select("select `open_id` from `wxusers` where `open_id`=#{openId}")
	public String getOpenId(@Param("openId") String openId);
}
