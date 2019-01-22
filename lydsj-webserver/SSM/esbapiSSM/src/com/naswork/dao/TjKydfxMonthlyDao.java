package com.naswork.dao;

import java.util.List;

import com.naswork.vo.RankVo;
import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxMonthly;
import com.naswork.model.TjKydfxMonthlyKey;

public interface TjKydfxMonthlyDao {
    int deleteByPrimaryKey(TjKydfxMonthlyKey key);

    int insert(TjKydfxMonthly record);

    int insertSelective(TjKydfxMonthly record);

    TjKydfxMonthly selectByPrimaryKey(TjKydfxMonthlyKey key);

    int updateByPrimaryKeySelective(TjKydfxMonthly record);

    int updateByPrimaryKey(TjKydfxMonthly record);

	List<MyModel> selectTop5ById1(@Param("idStr") String idStr, @Param("areaRange") Integer areaRange);

	List<MyModel> selectTop5ById2(@Param("idStr") String idStr, @Param("areaRange") Integer area_range, @Param("id") Integer id);

	List<TjKydfxMonthly> selectByScope(@Param("id") Integer id, @Param("scope") Integer scope, @Param("idStr") String idStr);

	List<KydfxTop5Data> selectTop5(@Param("id") Integer id, @Param("scope") Integer scope, @Param("idStr") String idStr);
	/**
	 * 根据景区id、年份参数、月份参数和区域参数查询客源地来梅人数并进行排名
	 * @param id
	 * @param year
	 * @param month
	 * @param area_range
	 * @return
	 */
	List<KydfxTop5Data> KydpmByMonth(@Param("id") Integer id, @Param("year") Integer year, 
			@Param("month") Integer month,@Param("area_range") Integer area_range);
	
	
	
	/**
	 * 根据景区id,年份参数、月份参数,区域参数查询对应的客源地来梅旅游人数
	 * @param id
	 * @param year
	 * @param month
	 * @param area_range
	 * @return
	 */
	List<TjKydfxMonthly>selectAllByMonth(@Param("id") Integer id, @Param("year") Integer year, 
			@Param("month") Integer month,@Param("area_range") Integer area_range);
	/**
	 * 根据year参数和area_range参数查询全市客源地排名
	 * @param year
	 * @param area_range
	 * @return
	 */
	List<KydfxTop5Data> getQskydpm(@Param("year") Integer year,@Param("area_range") Integer area_range ); 

	List<RankVo> getkydfx(@Param("Id") Integer Id,
						  @Param("yearId") Integer yearId,
						  @Param("monthId") Integer monthId,
						  @Param("area_range") Integer area_range);
	
}