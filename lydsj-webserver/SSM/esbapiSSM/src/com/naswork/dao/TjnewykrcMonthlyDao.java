package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.TjnewykrcMonthly;

@Component("tjnewykrcMonthlyDao")
public interface TjnewykrcMonthlyDao {
	/**
	 * 根据月份参数 和区域id查询所有月份和每月来梅旅游人数
	 * @param year
	 * @param id
	 * @return
	 */
	public List<TjnewykrcMonthly> getLyrsMonthly(@Param("year") Integer year,@Param("id") Integer id);
}