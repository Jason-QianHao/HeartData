package com.qian.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.qian.service.WxUserService;

public class BaseController {

	@Autowired
	protected WxUserService wxUserService;
}
