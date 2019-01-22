package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.Tj3ajqtsblfxV2Monthly;

@Component("tj3ajqtsblfxV2MonthlyDao")
public interface Tj3ajqtsblfxV2MonthlyDao {

    /**
     * 根据传参的年份year查询3a景区对应的年份所有月的散团比例
     * @param year
     * @return
     */
    public List<Tj3ajqtsblfxV2Monthly> getJqtsblfx3a(@Param("year") String year);
    
}