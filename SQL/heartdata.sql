/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : heartdata

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 16/07/2021 09:52:37
*/

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
  `file_name` varchar(150) NOT NULL COMMENT '文件名称',
  `file_url` varchar(100) NOT NULL COMMENT '文件url',
  `pepole_id` int NOT NULL COMMENT '所属人Id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '自动插入，创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `file_name` (`file_name`) USING BTREE,
  KEY `pepole_Id` (`pepole_id`) USING BTREE
) ENGINE=InnoDB COMMENT='文件表';

SELECT * from `file_info`;
insert into `file_info`(`file_name`, `file_url`, `pepole_id`) values('qh_1', '/home', 1);
TRUNCATE TABLE `file_info`;


CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `openId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录Id',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '自动插入，创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  INDEX `openId`(`openId`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户表';

DROP TABLE `users`
SELECT * from `users`;


CREATE TABLE `wxusers`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键（自增长）',
  `open_id` varchar(32) NOT NULL COMMENT '小程序openid',
  `nick_name` varchar(50) NOT NULL COMMENT '用户名',
  `avatar_url` varchar(500) NOT NULL COMMENT '密码，加密存储',
  `gender` int(10) NULL DEFAULT NULL COMMENT '登录Id',
  `city` varchar(32) NOT NULL COMMENT '城市',
  `province` varchar(32) NOT NULL COMMENT '省份',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '自动插入，创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `open_id`(`open_id`) USING BTREE,
  INDEX `nick_name`(`nick_name`) USING BTREE
) ENGINE = InnoDB COMMENT = '微信用户表';

DROP TABLE `wxusers`;
SELECT * from `wxusers`;