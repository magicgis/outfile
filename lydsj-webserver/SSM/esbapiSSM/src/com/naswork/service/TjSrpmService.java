package com.naswork.service;

import java.util.Map;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description
 *收入排名
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午10:48:56
 *
 */
public interface TjSrpmService {
	/**
	 * 
	 * @Description 获取某年某月某企业的月收入排名前rank名
	 * @param year  年份
	 * @param month 月份
	 * @param level 企业类型
	 * @param rank  排名数量1-10，默认5，非必须。
	 * @return
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:33:00
	 */
	public Map<String, Object> getOneIncomeRankingMonthly(Integer year, Integer month, Integer level, Integer rank);

	/**
	 * 
	 * @Description 获取某年某企业的年收入排名前rank名
	 * @param year  年份
	 * @param level 企业类型
	 * @param rank  排名数量1-10，默认5，非必须。
	 * @return
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:33:00
	 */
	public Map<String, Object> getOneSumIncomeRankingYearly(Integer year, Integer level, Integer rank);

}
