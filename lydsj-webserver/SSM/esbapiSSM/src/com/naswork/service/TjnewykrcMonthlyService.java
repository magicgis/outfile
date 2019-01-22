package com.naswork.service;

import java.util.List;

import com.naswork.model.TjnewykrcMonthly;

public interface TjnewykrcMonthlyService {
	/**
	 * 根据月份参数 和区域id查询所有月份和每月来梅旅游人数
	 * @param year
	 * @param id
	 * @return
	 */
	public List<TjnewykrcMonthly> getLyrsMonthly( Integer year, Integer id);
}
