package com.qian.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qian.Entity.WxUserEntity;
import com.qian.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WxUserController extends BaseController{

	@Autowired
	private RestTemplate restTemplate;
	@Value("${appid}")
	private String APPID;
	@Value("${appsecret}")
	private String APPSECRET;
	
	/*
	 * 微信用户登陆/注册
	 */
	@RequestMapping("/wxLogin")
	public String wxLogin(WxUserEntity wxUserEntity, String code) {
		// 查询Redis是否存在用户
		// 这里好像不太能通过用户的微信信息来判断用户
		// 得想办法从客户端获取用户唯一识别码，方能通过redis缓存用户登陆信息。
		
		// 请求微信接口
		/*
		 * 20210918:
		 * 	1.部署在Linux上后，由于没有设置相应防火墙导致访问微信网关失败
		 *  2.由于网关api地址在不断变化，暂时不考虑将所有ip地址开启防火墙，故将openid改为uuid随机生成。
		 * 20210929:
		 *  部署在8080端口后，上述现象没有发生了
		*/
		try {
			String params = "appid=" + APPID + "&secret=" + APPSECRET + "&js_code="
					+ code + "&grant_type=authorization_code";//参数
	        String url = "https://api.weixin.qq.com/sns/jscode2session?"+params;// 微信接口 用于查询oponid
	        String response = restTemplate.getForObject(url,String.class);
	        JSONObject jsonObject = JSON.parseObject(response);
	        response = jsonObject.getString("openid");
	        log.info("WxUserController/wxLogin, 微信用户接口返回openid：" + response);
	        wxUserEntity.setOpenId(response);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("WxUserController/wxLogin, 请求微信网关失败", e);
        	return Constants.ERROR;
		}
		String resId = "";
		// 根据nickName是否是新添加用户
        String isNewWxUser = wxUserService.isNewWxUser(wxUserEntity.getOpenId());
        if(isNewWxUser.equals(Constants.SUCCESSCODE)) {
        	// 是新用户
            String addResult = wxUserService.addWxUser(wxUserEntity);
            if(!addResult.equals(Constants.FAILCODE)) {
            	// 添加成功
            	log.info("WxUserController/wxLogin, 新用户添加成功");
            	resId = addResult;
            }else {
            	// 添加失败
            	log.info("WxUserController/wxLogin, 新用户注册失败");
            	return Constants.ERROR;
            }
        }else if(isNewWxUser.equals(Constants.ERROR)) {
        	// 查询失败
        	log.info("WxUserController/wxLogin, 用户信息查询失败");
        	return Constants.ERROR;
        }else {
        	resId = isNewWxUser;
        }
        log.info("WxUserController/wxLogin, 登陆/注册成功返回peopleid");
        return resId;
	}
}
