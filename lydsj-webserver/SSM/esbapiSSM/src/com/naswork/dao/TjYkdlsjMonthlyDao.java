package com.naswork.dao;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjYkdlsjMonthly;
import com.naswork.model.TjYkdlsjMonthlyKey;

public interface TjYkdlsjMonthlyDao {
    int deleteByPrimaryKey(TjYkdlsjMonthlyKey key);

    int insert(TjYkdlsjMonthly record);

    int insertSelective(TjYkdlsjMonthly record);

    TjYkdlsjMonthly selectByPrimaryKey(TjYkdlsjMonthlyKey key);

    int updateByPrimaryKeySelective(TjYkdlsjMonthly record);

    int updateByPrimaryKey(TjYkdlsjMonthly record);

	Integer getNum(@Param("id") Integer id, @Param("idStr") String idStr, @Param("type") Integer type);

	/**
	 * 根据年份参数、月份参数、时间区间参数查询各时间段所占百分比 
	 * @param little
	 * @param great
	 * @param year
	 * @param month
	 * @return
	 */
	public Double getQsdlsjfx(@Param("little") Integer little,@Param("great") Integer great,@Param("year") Integer year, @Param("month") Integer month);
	/**
	 * 根据年份参数、月份参数、时间区间，景区id参数查询各时间段所占百分比 -景区逗留时间分析
	 * @param little
	 * @param great
	 * @param year
	 * @param month
	 * @param id
	 * @return
	 */
	
	public Double getJqdlsjfx(@Param("little") Integer little,@Param("great") Integer great,@Param("year") Integer year, @Param("month") Integer month,@Param("id") Integer id);
	
	
	
	
	
	
	
	
}