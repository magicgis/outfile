package com.naswork.service;

import java.math.BigDecimal;
import java.util.List;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjLmlyrsDaily;
import com.naswork.model.TjLmlyrsDailyKey;

public interface TjLmlyrsDailyService {
	int deleteByPrimaryKey(TjLmlyrsDailyKey key);

    int insert(TjLmlyrsDaily record);

    int insertSelective(TjLmlyrsDaily record);

    TjLmlyrsDaily selectByPrimaryKey(TjLmlyrsDailyKey key);

    int updateByPrimaryKeySelective(TjLmlyrsDaily record);

    int updateByPrimaryKey(TjLmlyrsDaily record);

	List<TjLmlyrsDaily> selectByIdFromStartDay(Integer id, String startDay);

	Integer LastDayNum(Integer id, String identifier);

	List<TjLmlyrsDaily> selectByLevel(Integer level, String idStr);

	BigDecimal lastDayTrend(Integer id, String identifier);

	List<KydfxTop5Data> selectTop5Xq(String idStr, Integer id);

	List<KydfxTop5Data> selectTop5Jq(String idStr);
}
