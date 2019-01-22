package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjYkrysjMonthly;
import com.naswork.model.TjYkrysjMonthlyKey;

public interface TjYkrysjMonthlyDao {
    int deleteByPrimaryKey(TjYkrysjMonthlyKey key);

    int insert(TjYkrysjMonthly record);

    int insertSelective(TjYkrysjMonthly record);

    TjYkrysjMonthly selectByPrimaryKey(TjYkrysjMonthlyKey key);

    int updateByPrimaryKeySelective(TjYkrysjMonthly record);

    int updateByPrimaryKey(TjYkrysjMonthly record);

	List<TjYkrysjMonthly> selectByMonth(@Param("id") Integer id, @Param("idStr") String idStr);
}