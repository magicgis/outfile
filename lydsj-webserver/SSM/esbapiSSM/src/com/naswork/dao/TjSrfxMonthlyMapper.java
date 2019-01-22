package com.naswork.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjSrfxMonthly;

public interface TjSrfxMonthlyMapper {

	/**
	 * 
	 * @Description 获取某个企业所有年的年收入
	 * @param level 企业编码
	 * @return 某个企业所有年的年收入
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:02:52
	 */
	List<TjSrfxMonthly> getOneSumIncomeYearly(@Param(value="level") Integer level);

	/**
	 * 
	 * @Description 获取几个企业所有年的年收入
	 * @param levels 企业编码
	 * @return
	 *
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:04:53
	 */
	List<TjSrfxMonthly> getSumIncomeYearly(List<Integer> levels);

	/**
	 * 
	 * @Description 获取某年某企业所有月的月收入
	 * @param map key1=year 查询年份；key2=level 查询企业类型
	 * @return
	 *
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:06:26
	 */
	List<TjSrfxMonthly> getOneIncomeMonthly(Map<String, Integer> map);

}