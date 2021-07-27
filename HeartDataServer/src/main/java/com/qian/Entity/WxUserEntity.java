package com.qian.Entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WxUserEntity {

	/*
	 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
		 `open_id` varchar(32) NOT NULL COMMENT '小程序openid',
	     `nick_name` varchar(50) NOT NULL COMMENT '用户名',
	     `avatar_url` varchar(32) NOT NULL COMMENT '密码，加密存储',
	     `gender` int(10) NULL DEFAULT NULL COMMENT '登录Id',
	     `city` varchar(32) NOT NULL COMMENT '城市',
	     `province` varchar(32) NOT NULL COMMENT '省份',
         `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '自动插入，创建时间',
	 */
	private int id;
	private String openId;
	private String nickName;
	private String avatarUrl;
	// 0 未知 1 男 2 女
	private int gender;
	private String city;
	private String province;
	private Timestamp created;
}
