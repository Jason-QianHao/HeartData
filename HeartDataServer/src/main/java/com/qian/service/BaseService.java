package com.qian.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.qian.mapper.FileMapping;
import com.qian.mapper.MonthReportMapping;

public class BaseService {

	@Autowired
	protected FileMapping fileMapping;
	@Autowired
	protected MonthReportMapping monthReportMapping;
	@Autowired
	protected WxUserService wxUserService;
}
