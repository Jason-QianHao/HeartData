package com.qian.algorithm;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
	public ThreadPoolExecutor threadPoolExecutor() {
		return new ThreadPoolExecutor(10, 15,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10000));
	}
}
