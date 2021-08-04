package com.qian.algorithm;

import java.util.List;

import com.qian.Entity.algorithm.MonthReport;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class YearReport {

	/*
	 * "year": "2020",
       "month": [MonthReport1, MonthReport2...]
	 */
	private String year;
	private List<MonthReport> monthList;
}
