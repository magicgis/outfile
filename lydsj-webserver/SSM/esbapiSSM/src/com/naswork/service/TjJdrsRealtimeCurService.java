package com.naswork.service;

import com.naswork.model.TjJdrsRealtimeCur;
import com.naswork.model.TjJdrsRealtimeCurKey;

public interface TjJdrsRealtimeCurService {
	int deleteByPrimaryKey(TjJdrsRealtimeCurKey key);

    int insert(TjJdrsRealtimeCur record);

    int insertSelective(TjJdrsRealtimeCur record);

    TjJdrsRealtimeCur selectByPrimaryKey(TjJdrsRealtimeCurKey key);

    int updateByPrimaryKeySelective(TjJdrsRealtimeCur record);

    int updateByPrimaryKey(TjJdrsRealtimeCur record);

	TjJdrsRealtimeCur selectCur(Integer id);
}
