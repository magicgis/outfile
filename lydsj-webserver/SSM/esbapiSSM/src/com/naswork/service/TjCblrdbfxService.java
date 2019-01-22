package com.naswork.service;

import java.util.Map;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 涉旅企业成本利润对比分析
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 上午11:40:10
 *
 */
public interface TjCblrdbfxService {

	/**
	 * 
	 * @Description 获取所有涉旅企业/旅行社/宾馆酒店/景点的年 成本和:利润和
	 * @param year  查询年份
	 * @param level 查询类型
	 * @return 某年成本利润
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午11:41:09
	 */
	public Map<String, Object> getCostVSProfitYearly(Integer year,Integer level);

}
