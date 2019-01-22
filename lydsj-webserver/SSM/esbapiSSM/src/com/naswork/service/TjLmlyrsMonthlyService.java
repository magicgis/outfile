package com.naswork.service;

import java.math.BigDecimal;
import java.util.List;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjLmlyrsMonthly;
import com.naswork.model.TjLmlyrsMonthlyKey;

public interface TjLmlyrsMonthlyService {
	int deleteByPrimaryKey(TjLmlyrsMonthlyKey key);

    int insert(TjLmlyrsMonthly record);

    int insertSelective(TjLmlyrsMonthly record);

    TjLmlyrsMonthly selectByPrimaryKey(TjLmlyrsMonthlyKey key);

    int updateByPrimaryKeySelective(TjLmlyrsMonthly record);

    int updateByPrimaryKey(TjLmlyrsMonthly record);

	List<TjLmlyrsMonthly> selectByIdFromMonth(Integer id, String str);

	TjLmlyrsMonthly selectByIdAngYearAndMonth(Integer id, String year, String month);

	Integer CurMonthNum(Integer id, String identifier);

	List<TjLmlyrsMonthly> selectSomeMonth(Integer id, Integer monthsNum);

	List<TjLmlyrsMonthly> selectByLevel(Integer level, String idStr);

	BigDecimal curMonthTrend(Integer id, String identifier);

	List<KydfxTop5Data> selectTop5Jq(String idStr);

	List<KydfxTop5Data> selectTop5Xq(String idStr, Integer id);
	/**
	 * @author zhangwenwen
	 *统计本年来梅总人数 
	 * @param id
	 * @param year
	 * @return
	 */
	public int getTotalLmNum(Integer id,Integer year);
}
