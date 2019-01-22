package com.naswork.service;

import java.util.List;

import com.naswork.model.TjYkrysjMonthly;
import com.naswork.model.TjYkrysjMonthlyKey;

public interface TjYkrysjMonthlyService {
	int deleteByPrimaryKey(TjYkrysjMonthlyKey key);

	int insert(TjYkrysjMonthly record);

	int insertSelective(TjYkrysjMonthly record);

	TjYkrysjMonthly selectByPrimaryKey(TjYkrysjMonthlyKey key);

	int updateByPrimaryKeySelective(TjYkrysjMonthly record);

	int updateByPrimaryKey(TjYkrysjMonthly record);

	List<TjYkrysjMonthly> selectByMonth(Integer id, String idStr);
}
