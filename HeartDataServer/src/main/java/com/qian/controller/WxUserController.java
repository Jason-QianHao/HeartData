package com.qian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.WxUserEntity;
import com.qian.service.WxUserService;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WxUserController {

	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private RestTemplate restTemplate;
	
	/*
	 * 微信用户登陆/注册
	 */
	@RequestMapping("/wxLogin")
	public String wxLogin(WxUserEntity wxUserEntity, String code) {
		// 查询Redis是否存在opendId
		//....
		
		// 请求微信接口
		String params = "appid=" + Constants.APPID + "&secret=" + Constants.APPSECRET + "&js_code="
				+ code + "&grant_type=authorization_code";//参数
        String url = "https://api.weixin.qq.com/sns/jscode2session?"+params;// 微信接口 用于查询oponid
        String response = restTemplate.getForObject(url,String.class);
        JSONObject jsonObject = JSON.parseObject(response);
        response = jsonObject.getString("openid");
        log.info("UserController/wxLogin, 微信用户接口返回openid：" + response);
        wxUserEntity.setOpenId(response);
        // 是否是新添加用户
        String isNewWxUser = wxUserService.isNewWxUser(wxUserEntity.getOpenId());
        if(isNewWxUser.equals(Constants.SUCCESSCODE)) {
        	// 是新用户
            String addResult = wxUserService.addWxUser(wxUserEntity);
            if(addResult.equals(Constants.SUCCESSCODE)) {
            	// 添加成功
            	log.info("UserController/wxLogin, 新用户添加成功");
            }else {
            	// 添加失败
            	log.info("UserController/wxLogin, 新用户注册失败");
            	return Constants.ERROR;
            }
        }else if(isNewWxUser.equals(Constants.ERROR)) {
        	// 查询失败
        	log.info("UserController/wxLogin, 用户信息查询失败");
        	return Constants.ERROR;
        }
        // OpenId写Redis
        // ...
        log.info("UserController/wxLogin, 登陆/注册成功返回openid");
        return response;
	}
}
