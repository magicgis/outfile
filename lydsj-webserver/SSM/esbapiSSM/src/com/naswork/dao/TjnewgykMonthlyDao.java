package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.TjnewgykMonthly;
import com.naswork.model.TjnewgykMonthlyKey;
@Component("TjnewgykMonthlyDao")
public interface TjnewgykMonthlyDao {
    int deleteByPrimaryKey(TjnewgykMonthlyKey key);

    int insert(TjnewgykMonthly record);

    int insertSelective(TjnewgykMonthly record);

    TjnewgykMonthly selectByPrimaryKey(TjnewgykMonthlyKey key);

    int updateByPrimaryKeySelective(TjnewgykMonthly record);

    int updateByPrimaryKey(TjnewgykMonthly record);
    /**
     * 根据year参数某年所有月全市过夜游接待人数
     * @param year
     * @return
     */
    public List<TjnewgykMonthly> getQsgyyjdrs(@Param("year") Integer year);
}