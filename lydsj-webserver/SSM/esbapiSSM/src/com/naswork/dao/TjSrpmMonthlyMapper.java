package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.model.TjSrpmMonthly;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 收入排名
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午1:21:59
 * 
 */
public interface TjSrpmMonthlyMapper {

	/**
	 * 
	 * @Description 获取某年某月某企业的月收入排名前rank名
	 * @param map key1=year：查询年份；key2=month：查询月份；key3=level：查询企业类型；key4=rank：查询排名数量1-10，默认5
	 * @return
	 *
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 下午1:27:30
	 */
	List<TjSrpmMonthly> getOneIncomeRankingMonthly(Map<String, Integer> map);

	/**
	 * 
	 * @Description 获取某年某企业的年收入排名前rank名
	 * @param map key1=year：查询年份；key2=level：查询企业类型；key3=rank：查询排名数量1-10，默认5
	 * @return
	 *
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 下午1:30:26
	 */
	List<TjSrpmMonthly> getOneSumIncomeRankingYearly(Map<String, Integer> map);

}
