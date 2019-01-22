package com.naswork.service;

import java.util.List;

import com.naswork.model.TjYkdlsjRealtimeCur;
import com.naswork.model.TjYkdlsjRealtimeCurKey;

public interface TjYkdlsjRealtimeCurService {
	int deleteByPrimaryKey(TjYkdlsjRealtimeCurKey key);

	int insert(TjYkdlsjRealtimeCur record);

	int insertSelective(TjYkdlsjRealtimeCur record);

	TjYkdlsjRealtimeCur selectByPrimaryKey(TjYkdlsjRealtimeCurKey key);

	int updateByPrimaryKeySelective(TjYkdlsjRealtimeCur record);

	int updateByPrimaryKey(TjYkdlsjRealtimeCur record);

	List<TjYkdlsjRealtimeCur> selectById(Integer id);
}
