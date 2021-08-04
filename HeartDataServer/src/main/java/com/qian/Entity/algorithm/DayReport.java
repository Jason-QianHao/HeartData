package com.qian.Entity.algorithm;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class DayReport {

	/*
	 * "d" : "1",
       "imgurl": "/static/imgs/reports/riqi1.png",
       "isUsed": "true",
       "HealthIndex" : "9.9",
       "fileLists": [FileReport1, FileReport2]
	 */
	private int id;
	private int d;
	private String imgurl;
	private boolean isUsed;
	private int healthIndex;
	private List<FileReport> fileReportLists;
}
