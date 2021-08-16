package com.qian.algorithm;

import com.qian.Entity.FileEntity;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.mapper.MonthReportMapping;

public class ReportRunable implements Runnable{
	
	private FileEntity fileEntity;
	private MonthReportMapping monthReportMapping;

	public ReportRunable(FileEntity fileEntity, MonthReportMapping monthReportMapping) {
		this.fileEntity = fileEntity;
		this.monthReportMapping = monthReportMapping;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int y = Integer.valueOf(fileEntity.getYear());
		int m = Integer.valueOf(fileEntity.getMonth());
		int pepoleid = fileEntity.getPepoleId();
		// 算法分析...
		
		// 日报告生成和修改..
		
		// 月报告生成和修改
			// 1. 是否生成月报告
		Integer isExist = monthReportMapping.isExist(y, m, pepoleid);
		if(isExist == null) {
			// 2. 不存在，创建月报告. 暂时模拟参数，后面需要根据算法结果填入参数。
			MonthReport mR = new MonthReport();
			mR.setM(m);
			mR.setY(y);
			mR.setImgurl("/static/imgs/reports/riqi" + m + ".png");
			mR.setTitle(m + "月报告");
			mR.setDescription(m + "月报告, 良好");
			mR.setPepoleId(fileEntity.getPepoleId());
			mR.setHealthIndex((float)Math.random() * 10);
			monthReportMapping.insertMonthReport(mR);
		}else {
			// 2. 存在，修改月报告
		}
	}

}
