package com.naswork.service;

import java.util.List;

import com.naswork.model.TjCmrsDaily;
import com.naswork.model.TjCmrsDailyKey;

public interface TjCmrsDailyService {
	int deleteByPrimaryKey(TjCmrsDailyKey key);

	int insert(TjCmrsDaily record);

	int insertSelective(TjCmrsDaily record);

	TjCmrsDaily selectByPrimaryKey(TjCmrsDailyKey key);

	int updateByPrimaryKeySelective(TjCmrsDaily record);

	int updateByPrimaryKey(TjCmrsDaily record);

	List<TjCmrsDaily> selectByIdAndStartDay(Integer id, String startDay);
}
