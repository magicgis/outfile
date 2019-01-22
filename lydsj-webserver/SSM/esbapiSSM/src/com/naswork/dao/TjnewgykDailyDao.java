package com.naswork.dao;

import com.naswork.model.TjnewgykDaily;
import com.naswork.model.TjnewgykDailyKey;

public interface TjnewgykDailyDao {
    int deleteByPrimaryKey(TjnewgykDailyKey key);

    int insert(TjnewgykDaily record);

    int insertSelective(TjnewgykDaily record);

    TjnewgykDaily selectByPrimaryKey(TjnewgykDailyKey key);

    int updateByPrimaryKeySelective(TjnewgykDaily record);

    int updateByPrimaryKey(TjnewgykDaily record);
}