package com.naswork.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjYkdlsjDaily;
import com.naswork.model.TjYkdlsjDailyKey;

public interface TjYkdlsjDailyDao {
    int deleteByPrimaryKey(TjYkdlsjDailyKey key);

    int insert(TjYkdlsjDaily record);

    int insertSelective(TjYkdlsjDaily record);

    TjYkdlsjDaily selectByPrimaryKey(TjYkdlsjDailyKey key);

    int updateByPrimaryKeySelective(TjYkdlsjDaily record);

    int updateByPrimaryKey(TjYkdlsjDaily record);

	Date getMaxTime();

	Integer getNum(@Param("id") Integer id, @Param("idStr") String idStr, @Param("type") Integer type);
}