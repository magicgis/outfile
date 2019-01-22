package com.naswork.service;

import java.math.BigDecimal;
import java.util.List;

import com.naswork.model.TjJdrsMonthly;
import com.naswork.model.TjJdrsMonthlyKey;

public interface TjJdrsMonthlyService {
	int deleteByPrimaryKey(TjJdrsMonthlyKey key);

	int insert(TjJdrsMonthly record);

	int insertSelective(TjJdrsMonthly record);

	TjJdrsMonthly selectByPrimaryKey(TjJdrsMonthlyKey key);

	int updateByPrimaryKeySelective(TjJdrsMonthly record);

	int updateByPrimaryKey(TjJdrsMonthly record);

	List<TjJdrsMonthly> selectById(Integer id, String year);

	TjJdrsMonthly selectByYearAndMonth(Integer id, String string, String string2);

	Integer getMonthNum(Integer id, String identifier);

	List<TjJdrsMonthly> selectSomeMonths(Integer id, Integer monthsnum);

	BigDecimal curMonthTrend(Integer id, String identifier);
	/**
	 * @author zhangwenwen
	 *统计本年接待总人数 
	 * @param id
	 * @param year
	 * @return
	 */
	public int getTotalJdNum(Integer id, Integer year);
}
