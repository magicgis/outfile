package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.JqIncomeMonthly;

@Component("jqIncomeMonthlyDao")
public interface JqIncomeMonthlyDao {
    int insert(JqIncomeMonthly record);

    int insertSelective(JqIncomeMonthly record);
    /**
     * 获取18年的景区所有月份收入
     * @param id
     * @return
     */
    public List<JqIncomeMonthly>getJqIncomeMonthly(@Param("id") Integer id);
    /**
     * 根据年份参数和景区id获取该年该景区所有月份的收入
     * @param year
     * @param id
     * @return
     */
    public List<JqIncomeMonthly>getJqIncomeMonthlyV2(@Param("year") String year,@Param("id") Integer id);
}