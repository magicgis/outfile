package com.naswork.service;

import java.util.List;

import com.naswork.model.TjnewyrkMonthly;

public interface TjnewyrkMonthlyService {
    /**
     * 全市一日游接待人数-按月统计  
     * @param year
     * @return
     */
    public List<TjnewyrkMonthly> getQsyryjdrsMonthly(Integer year);
}
