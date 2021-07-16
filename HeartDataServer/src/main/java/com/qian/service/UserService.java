package com.qian.service;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qian.Entity.UserEntity;
import com.qian.mapper.UserMapping;
import com.qian.utils.Constants;
import com.qian.utils.MD5Util;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	@Autowired
	private UserMapping userMapping;

	@Autowired
	private RedisService redisService;

	/*
	 * 注册
	 */
	public void regist(UserEntity user, HttpServletRequest req) {
		String username = user.getUsername();
		String phone = user.getPhone();
		String password = user.getPassword();
		if (username.equals("") || phone.equals("") || password.equals("")) {
			req.setAttribute("error", "必填信息不能为空");
		}
		// 这里可以继续验证手机号是否正确
		try {
			// 使用手机号和原始密码，通过MD5算法加密成为新的密码插入数据库
			String newPassword = MD5Util.MD5(phone + password);
			user.setPassword(newPassword);
			// UUID产生用户登录Id
			user.setOpenId(UUID.randomUUID().toString());
			userMapping.addUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			// 这里可能是发现重复的用户名、邮箱或者手机号
			log.info("UserService.regist(...)，报错:", e);
			req.setAttribute("error", "用户名或手机号已注册");
		}
	}

	/*
	 * 登录
	 */
	public String login(UserEntity userEntity, HttpServletRequest req, HttpServletResponse resp) {
		// 默认使用的是username方式登录
		String loginStr = userEntity.getUsername();
		String password = userEntity.getPassword();
		/*
		 * 这里后面加上从Redis获取信息进行验证，不适用cookie了，所以更复杂一点
		 * Redis结构：
		 * {
		 * 	userOpenId: {username, phone, password}; 第一此登录存储， expire 3天
		 * 	username：userOpenId; 第一此登录存储， expire 3天
		 * 	phone: userOpenId; 第一此登录存储， expire 3天
		 * }
		 */
		
		// Redis缓存没有命中，开始查询MySQL
		UserEntity user = null;
		try {
			user = userMapping.getUserByUsername(loginStr);
			if (user == null) {
				user = userMapping.getUserByPhone(loginStr);
				if (user == null) {
					log.info("UserService/login, 账号不存在，请先注册，用户名：" + loginStr + " 密码：" + password);
					req.setAttribute("error", "账号不存在，请先注册");
					return Constants.SUCCESSCODE;
				} else {
					log.info("UserService/login, 使用手机号登录 : " + loginStr);
				}
			} else {
				log.info("UserService/login, 使用用户名登录 : " + loginStr);
			}
			// 查询到用户，进行登录密码校验
			String phone = user.getPhone();
			String loginPs = MD5Util.MD5(phone + password);
			if (loginPs.equals(user.getPassword())) {
				log.info("UserService/login, 登录成功");
				// 登录成功，写Redis
				try {
					setRedis(user);
				} catch (Exception e) {
					// TODO: handle exception
					log.info("UserService/login, redis设置登录信息失败，", e);
				}
				return Constants.SUCCESSCODE;
			} else {
				log.info("UserService/login, 登录失败，用户名或密码错误，用户名：" + loginStr + " 密码：" + password);
				req.setAttribute("error", "用户名或密码错误");
				return Constants.FAILCODE;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("UserService/login, 账号查询失败，请先注册或重新登录", e);
			req.setAttribute("error", "账号查询失败，请先注册或重新登录");
			return Constants.SUCCESSCODE;
		}
	}
	
	
	/*
	 * Redis设置
		 * Redis结构：
		 * {
		 * 	userOpenId: {username, phone, password}; 第一此登录存储， expire 3天
		 * 	username：userOpenId; 第一此登录存储， expire 3天
		 * 	phone: userOpenId; 第一此登录存储， expire 3天
		 * }
	 */
	public void setRedis(UserEntity user) {
		redisService.redisSetList(Constants.OPENID, user.getUsername());
		redisService.redisSetList(Constants.OPENID, user.getPhone());
		redisService.redisSetList(Constants.OPENID, user.getPassword());
		redisService.redisSetString(user.getUsername(), Constants.OPENID);
		redisService.redisSetString(user.getPhone(), Constants.OPENID);
		// 过期时间设置
		redisService.expire(Constants.OPENID, Constants.LoginTime);
		redisService.expire(user.getUsername(), Constants.LoginTime);
		redisService.expire(user.getPhone(), Constants.LoginTime);
	}
}
