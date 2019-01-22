package com.naswork.service;

import java.util.List;

import com.naswork.model.JqIncomeMonthly;

public interface JqIncomeMonthlyService {

    /**
     * 获取18年的景区所有月份收入
     * @param id
     * @return
     */
    public List<JqIncomeMonthly>getJqIncomeMonthly(Integer id);
    /**
     * 根据年份参数和景区id获取该年该景区所有月份的收入
     * @param year
     * @param id
     * @return
     */
    public List<JqIncomeMonthly>getJqIncomeMonthlyV2(String year,Integer id);
}
