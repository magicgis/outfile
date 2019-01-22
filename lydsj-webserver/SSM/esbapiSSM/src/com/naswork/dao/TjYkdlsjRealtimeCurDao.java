package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.Ring;
import com.naswork.model.TjYkdlsjRealtimeCur;
import com.naswork.model.TjYkdlsjRealtimeCurKey;

public interface TjYkdlsjRealtimeCurDao {
    int deleteByPrimaryKey(TjYkdlsjRealtimeCurKey key);

    int insert(TjYkdlsjRealtimeCur record);

    int insertSelective(TjYkdlsjRealtimeCur record);

    TjYkdlsjRealtimeCur selectByPrimaryKey(TjYkdlsjRealtimeCurKey key);

    int updateByPrimaryKeySelective(TjYkdlsjRealtimeCur record);

    int updateByPrimaryKey(TjYkdlsjRealtimeCur record);

	List<TjYkdlsjRealtimeCur> selectById(@Param("id") Integer id);
}