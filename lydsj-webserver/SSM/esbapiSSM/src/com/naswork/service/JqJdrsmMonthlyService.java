package com.naswork.service;

import java.util.List;

import com.naswork.model.JqJdrsmMonthly;

public interface JqJdrsmMonthlyService {
	
    /**
     * 根据id查找07年10月份以来团散比例
     * @param id
     * @return
     */
	public List<JqJdrsmMonthly> getTsPercent(Integer id);
    /**
     * 根据传参的年份year和景区id查询对应的年份所有月的散团比例
     * @param year
     * @param id
     * @return
     */
	public List<JqJdrsmMonthly> getTsPercentV2(String year,Integer id);
}
