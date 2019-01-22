package com.naswork.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjJdrsMonthly;
import com.naswork.model.TjJdrsMonthlyKey;

public interface TjJdrsMonthlyDao {
    int deleteByPrimaryKey(TjJdrsMonthlyKey key);

    int insert(TjJdrsMonthly record);

    int insertSelective(TjJdrsMonthly record);

    TjJdrsMonthly selectByPrimaryKey(TjJdrsMonthlyKey key);

    int updateByPrimaryKeySelective(TjJdrsMonthly record);

    int updateByPrimaryKey(TjJdrsMonthly record);

	List<TjJdrsMonthly> selectById(@Param("id") Integer id, @Param("idStr") String idStr);

	TjJdrsMonthly selectByYearAndMonth(@Param("id") Integer id, @Param("year") String year, @Param("month") String month);
	
	Integer getMonthNum(@Param("id") Integer id, @Param("idStr") String idStr);

	List<TjJdrsMonthly> selectSomeMonths(@Param("id") Integer id,  @Param("maxNum") Integer monthsnum);

	BigDecimal curMonthTrend(@Param("id") Integer id, @Param("idStr") String identifier);
	/**
	 * @author zhangwenwen
	 *统计本年接待总人数 
	 * @param id
	 * @param year
	 * @return
	 */
	public int getTotalJdNum(@Param("id") Integer id,@Param("year") Integer year);
}