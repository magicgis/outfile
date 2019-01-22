package com.naswork.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjJdrsDaily;
import com.naswork.model.TjJdrsDailyKey;

public interface TjJdrsDailyDao {
    int deleteByPrimaryKey(TjJdrsDailyKey key);

    int insert(TjJdrsDaily record);

    int insertSelective(TjJdrsDaily record);

    TjJdrsDaily selectByPrimaryKey(TjJdrsDailyKey key);

    int updateByPrimaryKeySelective(TjJdrsDaily record);

    int updateByPrimaryKey(TjJdrsDaily record);
    
    List<TjJdrsDaily> selectById(@Param("id") Integer id, @Param("startDate") String startDate);

	Integer selectByIdAndDate(@Param("id") Integer id, @Param("date") Date date);
}