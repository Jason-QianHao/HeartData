package com.qian.Entity.algorithm;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FileReport {

	/*
	 * {
	 * 	"id": "1",
		"createdTime": "2020-01-01 15:00:00",
		"avgBeat": "75"
	 * }
	 */
	private int id;
	private Timestamp createdTime;
	private Timestamp endTime;
	private int avgBeat;
}
