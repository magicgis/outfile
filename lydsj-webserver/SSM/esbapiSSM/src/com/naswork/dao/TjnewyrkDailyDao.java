package com.naswork.dao;

import com.naswork.model.TjnewyrkDaily;
import com.naswork.model.TjnewyrkDailyKey;

public interface TjnewyrkDailyDao {
    int deleteByPrimaryKey(TjnewyrkDailyKey key);

    int insert(TjnewyrkDaily record);

    int insertSelective(TjnewyrkDaily record);

    TjnewyrkDaily selectByPrimaryKey(TjnewyrkDailyKey key);

    int updateByPrimaryKeySelective(TjnewyrkDaily record);

    int updateByPrimaryKey(TjnewyrkDaily record);
}