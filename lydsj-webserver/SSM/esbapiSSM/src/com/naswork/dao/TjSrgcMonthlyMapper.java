package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.model.TjSrgcMonthly;

public interface TjSrgcMonthlyMapper {
	/**
	 * 
	* @Description 
	*获取某年某企业收入构成
	* @param map key1=year 年份；key2=level 企业类别
	* @return
	* @version v1.0.0
	* @author ChengAcer
	* @date 2018年11月2日 下午10:48:13
	 */
    List<TjSrgcMonthly> getOneCNYmlnYearly(Map<String,Integer> map);
}