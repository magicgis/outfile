package com.naswork.service;

import java.util.Date;

import com.naswork.model.TjYkdlsjDaily;
import com.naswork.model.TjYkdlsjDailyKey;

public interface TjYkdlsjDailyService {
	int deleteByPrimaryKey(TjYkdlsjDailyKey key);

	int insert(TjYkdlsjDaily record);

	int insertSelective(TjYkdlsjDaily record);

	TjYkdlsjDaily selectByPrimaryKey(TjYkdlsjDailyKey key);

	int updateByPrimaryKeySelective(TjYkdlsjDaily record);

	int updateByPrimaryKey(TjYkdlsjDaily record);

	Date getMaxTime();

	Integer getNum(Integer id, String idStr, Integer i);
}
