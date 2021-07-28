package com.qian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages="com.qian.mapper")
public class HeartDataApp extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(HeartDataApp.class, args);
	}
	
	// 为springboot打包项目用的
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(this.getClass());
	}
}
