package com.naswork.dao;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjJdrsRealtimeCur;
import com.naswork.model.TjJdrsRealtimeCurKey;

public interface TjJdrsRealtimeCurDao {
    int deleteByPrimaryKey(TjJdrsRealtimeCurKey key);

    int insert(TjJdrsRealtimeCur record);

    int insertSelective(TjJdrsRealtimeCur record);

    TjJdrsRealtimeCur selectByPrimaryKey(TjJdrsRealtimeCurKey key);

    int updateByPrimaryKeySelective(TjJdrsRealtimeCur record);

    int updateByPrimaryKey(TjJdrsRealtimeCur record);

	TjJdrsRealtimeCur selectCur(@Param("id") Integer id);
}