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
	 * 添加用户
	 */
	public String addWxUser(WxUserEntity wxUserEntity) {
		if(wxUserEntity == null) {
			return Constants.FAILCODE;
		}
		try {
			wxUserMapping.insert(wxUserEntity);
			return Constants.SUCCESSCODE;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserService/addWxUser", e);
			return Constants.FAILCODE;
		}
	}
	
	/*
	 * 根据openId判断是否是新用户
	 */
	public String isNewWxUser(String openId) {
		// 注意这里null和""的判读不能写反了。
		if(openId == null || openId.equals("")) {
			return Constants.SUCCESSCODE;
		}
		try {
			String res = wxUserMapping.getOpenId(openId);
			if(res == null || res.equals("")) {
				return Constants.SUCCESSCODE;
			}else {
				return Constants.FAILCODE;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserService/isNewWxUser", e);
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
