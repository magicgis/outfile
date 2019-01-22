package com.naswork.service;

import java.util.List;

import com.naswork.model.TjCmrsMonthly;
import com.naswork.model.TjCmrsMonthlyKey;

public interface TjCmrsMonthlyService {
    int deleteByPrimaryKey(TjCmrsMonthlyKey key);

    int insert(TjCmrsMonthly record);

    int insertSelective(TjCmrsMonthly record);

    TjCmrsMonthly selectByPrimaryKey(TjCmrsMonthlyKey key);

    int updateByPrimaryKeySelective(TjCmrsMonthly record);

    int updateByPrimaryKey(TjCmrsMonthly record);

	List<TjCmrsMonthly> selectByIdFromMonth(Integer id, String idStr);

	TjCmrsMonthly selectByIdAndIdstr(Integer id, String idStr);

	List<TjCmrsMonthly> selectSomeMonths(Integer id, Integer monthsNum);
}
