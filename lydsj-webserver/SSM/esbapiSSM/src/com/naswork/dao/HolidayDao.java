package com.naswork.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.Holiday;

public interface HolidayDao {
	
	public List<Map<String, Object>> selectjqjdrsTj(@Param("table_name") String tableName,@Param("holidayType") Integer holidayType,@Param("ids") String[] ids);

	public List<Map<String, Object>> selectjqjdrsTop5(@Param("table_name") String tableName,@Param("holidayType") Integer holidayType);
	
	public List<Map<String, Object>> selectjjrjdrszl(@Param("table_name") String tableName,@Param("holidayType") Integer holidayType);
	
	public List<Map<String, Object>> selectjjrkydtopn(@Param("sourceScope") Integer sourceScope,@Param("holidayType") Integer holidayType);
	/**
	 * 查询当前月节假日，返回一个对象
	 * @return
	 */
	public Holiday selectCurMonthHoliday();
	
}
