package com.naswork.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjLmlyrsMonthly;
import com.naswork.model.TjLmlyrsMonthlyKey;

public interface TjLmlyrsMonthlyDao {
    int deleteByPrimaryKey(TjLmlyrsMonthlyKey key);

    int insert(TjLmlyrsMonthly record);

    int insertSelective(TjLmlyrsMonthly record);

    TjLmlyrsMonthly selectByPrimaryKey(TjLmlyrsMonthlyKey key);

    int updateByPrimaryKeySelective(TjLmlyrsMonthly record);

    int updateByPrimaryKey(TjLmlyrsMonthly record);

	List<TjLmlyrsMonthly> selectByIdFromMonth(@Param("id") Integer id, @Param("idStr") String year);

	TjLmlyrsMonthly selectByIdAngYearAndMonth(@Param("id") Integer id, @Param("year") String year, @Param("month") String month);

	Integer curMonthNum(@Param("id") Integer id, @Param("idStr") String idStr);

	List<TjLmlyrsMonthly> selectSomeMonth(@Param("id") Integer id, @Param("maxNum") Integer monthsNum);

	List<TjLmlyrsMonthly> selectByLevel(@Param("level") Integer level, @Param("idStr") String idStr);

	BigDecimal curMonthTrend(@Param("id") Integer id, @Param("idStr") String identifier);

	List<KydfxTop5Data> selectTop5Xq(@Param("idStr") String idStr, @Param("id") Integer id);

	List<KydfxTop5Data> selectTop5Jq(@Param("idStr") String idStr);
	/**
	 * @author zhangwenwen
	 *统计本年来梅总人数 
	 * @param id
	 * @param year
	 * @return
	 */
	public int getTotalLmNum(@Param("id") Integer id,@Param("year") Integer year);
}