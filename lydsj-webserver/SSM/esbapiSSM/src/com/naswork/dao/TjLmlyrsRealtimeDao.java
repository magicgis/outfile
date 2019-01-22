package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjLmlyrsRealtime;
import com.naswork.model.TjLmlyrsRealtimeKey;

public interface TjLmlyrsRealtimeDao {
    int deleteByPrimaryKey(TjLmlyrsRealtimeKey key);

    int insert(TjLmlyrsRealtime record);

    int insertSelective(TjLmlyrsRealtime record);

    TjLmlyrsRealtime selectByPrimaryKey(TjLmlyrsRealtimeKey key);

    int updateByPrimaryKeySelective(TjLmlyrsRealtime record);

    int updateByPrimaryKey(TjLmlyrsRealtime record);

	String getMaxIdentifier();

	List<TjLmlyrsRealtime> selectByIdStr(@Param("id") Integer id, @Param("idStr") String idStr);
}