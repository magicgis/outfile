package com.naswork.service;

import java.util.List;

import com.naswork.model.TjYkrysjDaily;
import com.naswork.model.TjYkrysjDailyKey;

public interface TjYkrysjDailyService {
	int deleteByPrimaryKey(TjYkrysjDailyKey key);

    int insert(TjYkrysjDaily record);

    int insertSelective(TjYkrysjDaily record);

    TjYkrysjDaily selectByPrimaryKey(TjYkrysjDailyKey key);

    int updateByPrimaryKeySelective(TjYkrysjDaily record);

    int updateByPrimaryKey(TjYkrysjDaily record);

	List<TjYkrysjDaily> selectByMonth(Integer id, String idStr);

	List<TjYkrysjDaily> selectOneDay(Integer id, String idStr);
}
