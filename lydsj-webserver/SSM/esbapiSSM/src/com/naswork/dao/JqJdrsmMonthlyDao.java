package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.JqJdrsmMonthly;

@Component("jqJdrsmMonthlyDao")
public interface JqJdrsmMonthlyDao {
    int insert(JqJdrsmMonthly record);

    int insertSelective(JqJdrsmMonthly record);
    /**
     * 根据id查找07年10月份以来团散比例
     * @param id
     * @return
     */
    public List<JqJdrsmMonthly> getTsPercent(@Param("id") Integer id);
    /**
     * 根据传参的年份year和景区id查询对应的年份所有月的散团比例
     * @param year
     * @param id
     * @return
     */
    public List<JqJdrsmMonthly> getTsPercentV2(@Param("year") String year,@Param("id") Integer id);
    
}