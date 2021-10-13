package com.qian.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.qian.Entity.FileEntity;
import com.qian.Entity.algorithm.DayReport;
import com.qian.Entity.algorithm.FileReport;
import com.qian.Entity.algorithm.MonthReport;
import com.qian.mapper.DayReportMapping;
import com.qian.mapper.FileReportMapping;
import com.qian.mapper.MonthReportMapping;
import com.qian.service.BaseService;
import com.qian.service.WxUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportRunable implements Runnable {
	// 字段
	private List<FileEntity> filesToday;

	@Value("${fileBasePath}")
	private String fileBasePath;
	private FileReportMapping fileReportMapping;
	private DayReportMapping dayReportMapping;
	private MonthReportMapping monthReportMapping;
	private WxUserService wxUserService;

	public ReportRunable(List<FileEntity> filesToday, FileReportMapping fileReportMapping,
			DayReportMapping dayReportMapping, MonthReportMapping monthReportMapping, WxUserService wxUserService) {
		this.filesToday = filesToday;
		this.fileReportMapping = fileReportMapping;
		this.dayReportMapping = dayReportMapping;
		this.monthReportMapping = monthReportMapping;
		this.wxUserService = wxUserService;
	}

	/**
	 * 线程运行方法
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 遍历filesToday列表，插入file_report数据库表
		Map<Integer, List<FileReport>> map = new HashMap<Integer, List<FileReport>>();
		int[] ymd = new int[3];
		for (FileEntity f : this.filesToday) {
			// 封装fileReport
			FileReport fileReport = new FileReport();
			fileReport.setY(Integer.valueOf(f.getYear()));
			fileReport.setM(Integer.valueOf(f.getMonth()));
			fileReport.setD(Integer.valueOf(f.getDay()));
			ymd[0] = fileReport.getY();
			ymd[1] = fileReport.getM();
			ymd[2] = fileReport.getD();
			fileReport.setStartTime(f.getClientCreated());
			fileReport.setEndTime(f.getServerCreated());
			fileReport.setPepoleId(f.getPepoleId());
			// 读取磁盘文件，计算平均心率
			// try {
			fileReport.setAvgBeat(calAvgHeart(f.getFileUrl()));
			// } catch (Exception e) {
			// // TODO: handle exception
			// // 若文件不存在或读取失败，则直接赋值为-1进行报错提示。
			// fileReport.setAvgBeat(-1);
			// log.info("ReportRunable/run, 读取文件失败", e);
			// }
			// try {
			fileReportMapping.insert(fileReport);
			log.info("ReportRunable/run, 插入fileReport成功");
			// } catch (Exception e) {
			// // TODO: handle exception
			// log.info("ReportRunable/run, 插入fileReport失败", e);
			// }
			if (!map.containsKey(f.getPepoleId())) {
				map.put(f.getPepoleId(), new ArrayList<FileReport>());
			}
			map.get(f.getPepoleId()).add(fileReport);
		}
		// 获取数据库所有人的id
		List<Integer> allPepoleId = wxUserService.getAllId();
		// 日报告生成和修改..
		for (Integer pid : allPepoleId) {
			DayReport dayReport = new DayReport();
			dayReport.setY(ymd[0]);
			dayReport.setM(ymd[1]);
			dayReport.setD(ymd[2]);
			dayReport.setImgurl("/static/imgs/reports/riqi" + ymd[2] + ".png");
			dayReport.setPepoleId(pid);
			if (!map.containsKey(pid)) {
				// 今天没有新增
				dayReport.setUsed(false);
				dayReport.setHealthIndex(0);
			} else {
				// 今天新增
				dayReport.setUsed(true);
				// 计算平均心率
				float dayAvg = 0;
				for (FileReport fr : map.get(pid)) {
					if (fr.getAvgBeat() == -1) {
						dayAvg = -1;
						break;
					}
					dayAvg += fr.getAvgBeat();
				}
				if (dayAvg != -1) {
					dayReport.setHealthIndex(dayAvg / map.get(pid).size());
				}
			}
			// try {
			dayReportMapping.insertDayReport(dayReport);
			log.info("ReportRunable/run, 插入dayReport成功");
			// } catch (Exception e) {
			// // TODO: handle exception
			// log.info("ReportRunable/run, 插入dayReport失败", e);
			// }
		}
		// 月报告生成和修改
		// try {
		for (Integer pid : allPepoleId) {
			// 查询本月每天的记录
			List<DayReport> allDayReportByYearAndMonth = dayReportMapping.getAllDayReportByYearAndMonth(ymd[0], ymd[1],
					pid);
			// 计算平均心率
			float monthAvg = 0;
			int len = 0;
			for (DayReport dr : allDayReportByYearAndMonth) {
				if (dr.isUsed()) {
					if (dr.getHealthIndex() == -1) {
						monthAvg = -1;
						break;
					}
				}
				monthAvg += dr.getHealthIndex();
				len = len + 1;
			}
			if (monthAvg != -1) {
				monthAvg = monthAvg / len;
			}
			if (ymd[2] == 1) {
				// 今天是新的1个月，需要进行新增月报告
				MonthReport monthReport = new MonthReport();
				monthReport.setY(ymd[0]);
				monthReport.setM(ymd[1]);
				monthReport.setImgurl("/static/imgs/reports/riqi" + ymd[1] + ".png");
				monthReport.setTitle(ymd[1] + "月报告");
				monthReport.setDescription("良好");
				monthReport.setPepoleId(pid);
				monthReport.setHealthIndex(monthAvg);
				monthReportMapping.insertMonthReport(monthReport);
				log.info("ReportRunable/run, 插入monthReport成功");
			} else {
				// 本月月报告已经存在，需要进行修改
				monthReportMapping.updateMonthHealthIndex(monthAvg, ymd[0], ymd[1], pid);
				log.info("ReportRunable/run, 修改monthReport成功");
			}
		}
		
		// } catch (Exception e) {
		// // TODO: handle exception
		// log.info("ReportRunable/run, 插入monthReport失败", e);
		// }
	}

	/**
	 * 计算平均心率 备注：后续可以改进为对数据的算法分析
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	private float calAvgHeart(String filepath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String str = null;
			float avg = 0;
			int len = 0;
			while ((str = br.readLine()) != null) {
				avg += Float.valueOf(str);
				len = len + 1;
			}
			log.info("ReportRunable/calAvgHeart, 读取文件成功");
			return avg / len;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			log.info("ReportRunable/calAvgHeart, 读取文件失败");
			return -1;
		}
		// TODO 自动生成的 catch 块
	}

}
