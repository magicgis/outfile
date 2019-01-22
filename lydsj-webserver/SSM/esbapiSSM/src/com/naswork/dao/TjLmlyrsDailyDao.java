package com.naswork.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjLmlyrsDaily;
import com.naswork.model.TjLmlyrsDailyKey;

public interface TjLmlyrsDailyDao {
    int deleteByPrimaryKey(TjLmlyrsDailyKey key);

    int insert(TjLmlyrsDaily record);

    int insertSelective(TjLmlyrsDaily record);

    TjLmlyrsDaily selectByPrimaryKey(TjLmlyrsDailyKey key);

    int updateByPrimaryKeySelective(TjLmlyrsDaily record);

    int updateByPrimaryKey(TjLmlyrsDaily record);

	List<TjLmlyrsDaily> selectByIdFromStartDay(@Param("id") Integer id,@Param("startDay") String startDay);

	Integer LastDayNum(@Param("id") Integer id, @Param("idStr") String identifier);

	List<TjLmlyrsDaily> selectByLevel(@Param("level") Integer level, @Param("idStr") String idStr);

	BigDecimal lastDayTrend(@Param("id") Integer id, @Param("idStr") String identifier);

	List<KydfxTop5Data> selectTop5Xq(@Param("idStr") String idStr, @Param("id") Integer id);

	List<KydfxTop5Data> selectTop5Jq(@Param("idStr") String idStr);
}