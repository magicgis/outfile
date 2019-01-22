package com.naswork.service;

import java.util.List;

import com.naswork.model.TjLmlyrsRealtime;
import com.naswork.model.TjLmlyrsRealtimeKey;

public interface TjLmlyrsRealtimeService {
	int deleteByPrimaryKey(TjLmlyrsRealtimeKey key);

	int insert(TjLmlyrsRealtime record);

	int insertSelective(TjLmlyrsRealtime record);

	TjLmlyrsRealtime selectByPrimaryKey(TjLmlyrsRealtimeKey key);

	int updateByPrimaryKeySelective(TjLmlyrsRealtime record);

	int updateByPrimaryKey(TjLmlyrsRealtime record);

	String getMaxIdentifier();

	List<TjLmlyrsRealtime> selectByIdStr(Integer id, String idStr);
}
