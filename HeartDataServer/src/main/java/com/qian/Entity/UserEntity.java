package com.qian.Entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

	/*
	  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
	  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
	  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，加密存储',
	  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
	  `openId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录Id',
	  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '自动插入，创建时间',
	 */
	private int id;
	private String username;
	private String password;
	private String phone;
	private String openId;
	private Timestamp created;
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return username + " " + phone + " " + openId;
	}
}
