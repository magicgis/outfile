
package com.naswork.service;

import java.util.Map;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 收入分析
 *              get：获取；All：各个企业；Total：全部企业和；One：某个企业；income：月收入；SumIncome：年收入；Monthly：获取某年所有月；Yearly：获取所有年。
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 上午11:13:06
 *
 */
public interface TjSrfxService {

	/**
	 * 
	 * @Description 获取某年所有月、全部涉旅企业月收入。
	 * @param year 获取的年份
	 * @return 某年所有月数据。
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午11:13:34
	 */
	public Map<String, Object> getTotalIncomeMonthly(Integer year);

	/**
	 * 
	 * @Description 获取所有年、全部涉旅企业 月收入和（年收入）。
	 * @return 所有年数据。
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午11:15:06
	 */
	public Map<String, Object> getTotalSumIncomeYearly();

	/**
	 * 
	 * @Description 获取所有年、各个涉旅企业的年收入。
	 * @return 所有年数据
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午11:21:58
	 */
	public Map<String, Object> getAllSumIncomeYearly();

	/**
	 * 
	 * @Description 获取某年 景区/酒店/景点 所有月 月收入
	 * @param year  年份
	 * @param level 1、旅行社；2、宾馆酒店；3、景点；4、全部企业
	 * @return 返回某年所有月数据。
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午11:23:48
	 */
	public Map<String, Object> getOneIncomeMonthly(Integer year, Integer level);

	/**
	 * 
	 * @Description 获取 景区/酒店/景点 所有年 年收入
	 * @param level 1、旅行社；2、宾馆酒店；3、景点；4、全部企业
	 * @return 所有年 年收入
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午11:24:19
	 */
	public Map<String, Object> getOneSumIncomeYearly(Integer level);

}
