package com.naswork.service;

import com.naswork.model.TjYkdlsjMonthly;
import com.naswork.model.TjYkdlsjMonthlyKey;

public interface TjYkdlsjMonthlyService {
	int deleteByPrimaryKey(TjYkdlsjMonthlyKey key);

	int insert(TjYkdlsjMonthly record);

	int insertSelective(TjYkdlsjMonthly record);

	TjYkdlsjMonthly selectByPrimaryKey(TjYkdlsjMonthlyKey key);

	int updateByPrimaryKeySelective(TjYkdlsjMonthly record);

	int updateByPrimaryKey(TjYkdlsjMonthly record);

	Integer getNum(Integer id, String idStr, Integer i);

	/**
	 * 根据年份参数、月份参数、时间区间参数查询各时间段所占百分比 
	 * @param little
	 * @param great
	 * @param year
	 * @param month
	 * @return
	 */
	public Double getQsdlsjfx(Integer little,Integer great, Integer year, Integer month);
	/**
	 * 根据年份参数、月份参数、时间区间，景区id参数查询各时间段所占百分比 -景区逗留时间分析
	 * @param little
	 * @param great
	 * @param year
	 * @param month
	 * @param id
	 * @return
	 */
	
	public Double getJqdlsjfx( Integer little, Integer great,Integer year,  Integer month, Integer id);

}
