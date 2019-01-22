package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjYkrysjDaily;
import com.naswork.model.TjYkrysjDailyKey;

public interface TjYkrysjDailyDao {
    int deleteByPrimaryKey(TjYkrysjDailyKey key);

    int insert(TjYkrysjDaily record);

    int insertSelective(TjYkrysjDaily record);

    TjYkrysjDaily selectByPrimaryKey(TjYkrysjDailyKey key);

    int updateByPrimaryKeySelective(TjYkrysjDaily record);

    int updateByPrimaryKey(TjYkrysjDaily record);

	List<TjYkrysjDaily> selectByMonth(@Param("id") Integer id, @Param("idStr") String idStr);

	List<TjYkrysjDaily> selectOneDay(@Param("id") Integer id,  @Param("idStr") String idStr);
}