package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.TjnewyrkMonthly;
import com.naswork.model.TjnewyrkMonthlyKey;

@Component("tjnewyrkMonthlyDao")
public interface TjnewyrkMonthlyDao {
    int deleteByPrimaryKey(TjnewyrkMonthlyKey key);

    int insert(TjnewyrkMonthly record);

    int insertSelective(TjnewyrkMonthly record);

    TjnewyrkMonthly selectByPrimaryKey(TjnewyrkMonthlyKey key);

    int updateByPrimaryKeySelective(TjnewyrkMonthly record);

    int updateByPrimaryKey(TjnewyrkMonthly record);
    
    /**
     * 全市一日游接待人数-按月统计  
     * @param year
     * @return
     */
    public List<TjnewyrkMonthly> getQsyryjdrsMonthly(@Param("year") Integer year);
}