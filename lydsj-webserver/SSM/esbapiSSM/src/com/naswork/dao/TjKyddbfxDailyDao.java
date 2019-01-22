package com.naswork.dao;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.TjKyddbfxDaily;
import com.naswork.model.TjKyddbfxDailyKey;

public interface TjKyddbfxDailyDao {
    int deleteByPrimaryKey(TjKyddbfxDailyKey key);

    int insert(TjKyddbfxDaily record);

    int insertSelective(TjKyddbfxDaily record);

    TjKyddbfxDaily selectByPrimaryKey(TjKyddbfxDailyKey key);

    int updateByPrimaryKeySelective(TjKyddbfxDaily record);

    int updateByPrimaryKey(TjKyddbfxDaily record);

	TjKyddbfxDaily selectByIdAndDate4(@Param("id") Integer id, @Param("idStr") String idStr);

	TjKyddbfxDaily selectByIdAndDate3(@Param("id") Integer id, @Param("idStr") String idStr);

	TjKyddbfxDaily selectByIdAndDate2(@Param("id") Integer id, @Param("idStr") String idStr);
}