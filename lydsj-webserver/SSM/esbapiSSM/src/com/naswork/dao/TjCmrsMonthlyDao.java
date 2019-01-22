package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjCmrsMonthly;
import com.naswork.model.TjCmrsMonthlyKey;

public interface TjCmrsMonthlyDao{
    int deleteByPrimaryKey(TjCmrsMonthlyKey key);

    int insert(TjCmrsMonthly record);

    int insertSelective(TjCmrsMonthly record);

    TjCmrsMonthly selectByPrimaryKey(TjCmrsMonthlyKey key);

    int updateByPrimaryKeySelective(TjCmrsMonthly record);

    int updateByPrimaryKey(TjCmrsMonthly record);

	List<TjCmrsMonthly> selectByIdFromMonth(@Param("id") Integer id, @Param("idStr") String idStr);

	TjCmrsMonthly selectByIdAndIdstr(@Param("id") Integer id, @Param("idStr") String idStr);

	List<TjCmrsMonthly> selectSomeMonths(@Param("id") Integer id, @Param("maxNum") Integer monthsNum);
}