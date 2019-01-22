package com.naswork.service;

import java.util.List;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxMonthly;
import com.naswork.model.TjKydfxMonthlyKey;
import com.naswork.vo.RankVo;

/**
 * @author white
 */
public interface TjKydfxMonthlyService {
	int deleteByPrimaryKey(TjKydfxMonthlyKey key);

    int insert(TjKydfxMonthly record);

    int insertSelective(TjKydfxMonthly record);

    TjKydfxMonthly selectByPrimaryKey(TjKydfxMonthlyKey key);

    int updateByPrimaryKeySelective(TjKydfxMonthly record);

    int updateByPrimaryKey(TjKydfxMonthly record);

	List<MyModel> selectTop5ById1(String idStr, Integer area_range);

	List<MyModel> selectTop5ById2(String idStr, Integer area_range, Integer id);

	List<TjKydfxMonthly> selectByScope(Integer id, Integer scope, String idStr);

	List<KydfxTop5Data> selectTop5(Integer id, Integer scope, String idStr);
	/**
	 * 根据景区id、年份参数、月份参数和区域参数查询客源地来梅人数并进行排名
	 * @param id
	 * @param year
	 * @param month
	 * @param area_range
	 * @return
	 */
	List<KydfxTop5Data> KydpmByMonth( Integer id,  Integer year, 
			 Integer month, Integer area_range);
	/**
	 * 根据景区id,年份参数、月份参数,区域参数查询对应的客源地来梅旅游人数
	 * @param id
	 * @param year
	 * @param month
	 * @param area_range
	 * @return
	 */
	List<TjKydfxMonthly>selectAllByMonth( Integer id,  Integer year, 
			 Integer month, Integer area_range);
	/**
	 * 根据year参数和area_range参数查询全市客源地排名
	 * @param year
	 * @param area_range
	 * @return
	 */
	List<KydfxTop5Data> getQskydpm( Integer year, Integer area_range );

	/**
	 * 客源地分析（上月） 地图控件
	 * @param id
	 * @param year_id
	 * @param month_id
	 * @param area_range
	 * @return
	 */
	List<RankVo> getkydfx(Integer id,Integer year_id,Integer month_id,Integer area_range);
}
