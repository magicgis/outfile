package com.naswork.dao;

import java.util.Map;

import com.naswork.model.CostVSProfit;

public interface TjCblrdbfxMonthlyMapper {

	/**
	 * @Description 获取某年某企业年成本和、年利润和
	 * @param map key1=year 年份；key2=level 企业类型。
	 * @return
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:55:36
	 */
	CostVSProfit getCostVSProfitYearly(Map<String, Integer> map);
}