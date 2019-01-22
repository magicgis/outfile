package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjCmrsDaily;
import com.naswork.model.TjCmrsDailyKey;

public interface TjCmrsDailyDao {
    int deleteByPrimaryKey(TjCmrsDailyKey key);

    int insert(TjCmrsDaily record);

    int insertSelective(TjCmrsDaily record);

    TjCmrsDaily selectByPrimaryKey(TjCmrsDailyKey key);

    int updateByPrimaryKeySelective(TjCmrsDaily record);

    int updateByPrimaryKey(TjCmrsDaily record);

	List<TjCmrsDaily> selectByIdAndStartDay(@Param("id") Integer id, @Param("startDay") String startDay);
}