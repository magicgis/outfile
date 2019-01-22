package com.naswork.service;

import java.util.Date;
import java.util.List;

import com.naswork.model.TjJdrsDaily;
import com.naswork.model.TjJdrsDailyKey;

public interface TjJdrsDailyService {
	int deleteByPrimaryKey(TjJdrsDailyKey key);

	int insert(TjJdrsDaily record);

	int insertSelective(TjJdrsDaily record);

	TjJdrsDaily selectByPrimaryKey(TjJdrsDailyKey key);

	int updateByPrimaryKeySelective(TjJdrsDaily record);

	int updateByPrimaryKey(TjJdrsDaily record);

	List<TjJdrsDaily> selectById(Integer id, String startDate);

	Integer selectByIdAndDate(Integer id, Date date);
}
