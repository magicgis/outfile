package com.naswork.service;

import java.util.List;

import com.naswork.model.TjnewgykMonthly;

public interface TjnewgykMonthlyService {
    /**
     * 根据year参数某年所有月全市过夜游接待人数
     * @param year
     * @return
     */
    public List<TjnewgykMonthly> getQsgyyjdrs(Integer year);
}
