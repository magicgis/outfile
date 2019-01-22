package com.naswork.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.naswork.dao.HolidayDao;
import com.naswork.model.Holiday;
import com.naswork.service.HolidayService;

@Service("holidayService")
public class HolidayServiceImpl implements HolidayService{
	
	@Resource
	private HolidayDao holidayDao;

	public List<Map<String, Object>> selectjqjdrsTj(int areaRange,int timeRange, String[] ids) {
		String tableName = "tj_jdrs_holiday";
		if(areaRange == 3){
			tableName = "tj_lmlyrs_holiday";
		}
		return holidayDao.selectjqjdrsTj(tableName, timeRange, ids);
	}

	public List<Map<String, Object>> selectjqjdrsTop5(int areaRange,int timeRange) {
		String tableName = "tj_jdrs_holiday";
		if(areaRange == 3){
			tableName = "tj_lmlyrs_holiday";
		}
		return holidayDao.selectjqjdrsTop5(tableName, timeRange);
	}

	public List<Map<String, Object>> selectjjrjdrszl(int areaRange,int timeRange) {
		String tableName = "tj_jdrs_holiday";
		if(areaRange == 3){
			tableName = "tj_lmlyrs_holiday";
		}
		return holidayDao.selectjjrjdrszl(tableName, timeRange);
	}

	public List<Map<String, Object>> selectjjrkydtopn(int areaRange,int timeRange) {
		areaRange = areaRange==1?3:areaRange;
		return holidayDao.selectjjrkydtopn(areaRange,timeRange);
	}

	@Override
	public Holiday selectCurMonthHoliday() {
		return holidayDao.selectCurMonthHoliday();
	}

	
	
	
}
