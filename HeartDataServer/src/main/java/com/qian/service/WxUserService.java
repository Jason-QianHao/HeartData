package com.qian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qian.Entity.WxUserEntity;
import com.qian.mapper.WxUserMapping;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WxUserService {

	@Autowired
	private WxUserMapping wxUserMapping;
	
	/*
	 * 功能：添加用户
	 * 返回：
	 * 	1. 错误信息
	 *  2. 数据库id
	 */
	public String addWxUser(WxUserEntity wxUserEntity) {
		if(wxUserEntity == null) {
			return Constants.FAILCODE;
		}
		try {
			wxUserMapping.insert(wxUserEntity);
			return wxUserMapping.getId(wxUserEntity.getOpenId()) + "";
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserService/addWxUser", e);
			return Constants.FAILCODE;
		}
	}
	
	/*
	 * 功能：根据openId判断是否是新用户
	 * 返回：
	 * 	1. 是新用户 Constants.SUCCESSCOD
	 *  2. 已存在账号 pepoleid
	 *  3. 查询错误 Constants.ERROR
	 */
	public String isNewWxUser(String openId) {
		// 注意这里null和""的判读不能写反了。
		if(openId == null || openId.equals("")) {
			return Constants.SUCCESSCODE;
		}
		try {
			Integer pepoleid = wxUserMapping.getId(openId);
			if(pepoleid == null || pepoleid == 0) {
				return Constants.SUCCESSCODE;
			}else {
				return String.valueOf(pepoleid);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserService/isNewWxUser", e);
			return Constants.ERROR;
		}
	}
	
	/*
	 * 功能：根据nickName判断是否是新用户
	 * 返回：
	 * 	1. 是新用户 Constants.SUCCESSCOD
	 *  2. 已存在账号 pepoleid
	 *  3. 查询错误 Constants.ERROR
	 */
	public String isNewWxUserByNickName(String nickName) {
		// 注意这里null和""的判读不能写反了。
		if(nickName == null || nickName.equals("")) {
			return Constants.SUCCESSCODE;
		}
		try {
			Integer pepoleid = wxUserMapping.getNickName(nickName);
			if(pepoleid == null || pepoleid == 0) {
				return Constants.SUCCESSCODE;
			}else {
				return String.valueOf(pepoleid);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserService/isNewWxUserByNickName 查询用户昵称错误", e);
			return Constants.ERROR;
		}
	}
	
	/*
	 * 根据用户openId查询id
	 */
	public int getId(String openId) {
		try {
			int id = wxUserMapping.getIdByOpenId(openId);
			log.info("WxUserService/getId, 查询ID成功," + id);
			return id;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserService/getId, 查询ID失败", e);
			return -1;
		}
	}
}
