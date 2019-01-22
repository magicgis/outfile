package com.naswork.service;

import com.naswork.model.TjKyddbfxDaily;
import com.naswork.model.TjKyddbfxDailyKey;

public interface TjKyddbfxDailyService {
	int deleteByPrimaryKey(TjKyddbfxDailyKey key);

	int insert(TjKyddbfxDaily record);

	int insertSelective(TjKyddbfxDaily record);

	TjKyddbfxDaily selectByPrimaryKey(TjKyddbfxDailyKey key);

	int updateByPrimaryKeySelective(TjKyddbfxDaily record);

	int updateByPrimaryKey(TjKyddbfxDaily record);

	TjKyddbfxDaily selectByIdAndDate4(Integer id, String idStr);

	TjKyddbfxDaily selectByIdAndDate3(Integer id, String idStr);

	TjKyddbfxDaily selectByIdAndDate2(Integer id, String idStr);
}
