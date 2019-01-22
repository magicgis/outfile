package com.naswork.service;

import java.util.List;
import java.util.Map;

import com.naswork.model.Holiday;


public interface HolidayService {

	public List<Map<String, Object>> selectjqjdrsTj(int areaRange,int timeRange,String[] ids);
	
	public List<Map<String, Object>> selectjqjdrsTop5(int areaRange,int timeRange);
	
	public List<Map<String, Object>> selectjjrjdrszl(int areaRange,int timeRange);
	
	public List<Map<String, Object>> selectjjrkydtopn(int areaRange,int timeRange);
	/**
	 * 查询当前月节假日，返回一个对象
	 * @return
	 */
	public Holiday selectCurMonthHoliday();
}
