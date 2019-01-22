package com.naswork.service;

import java.util.List;

import com.naswork.model.Tj3ajqtsblfxV2Monthly;

public interface Tj3ajqtsblfxV2MonthlyService {
    /**
     * 根据传参的年份year查询3a景区对应的年份所有月的散团比例
     * @param year
     * @return
     */
    public List<Tj3ajqtsblfxV2Monthly> getJqtsblfx3a(String year);
}
