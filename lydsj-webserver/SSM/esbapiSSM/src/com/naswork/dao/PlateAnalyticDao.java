package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.PlateAnalytic;
import com.naswork.model.Zjykydtop5;

public interface PlateAnalyticDao {
    int insert(PlateAnalytic record);

    int insertSelective(PlateAnalytic record);

	List<Zjykydtop5> selectAll1(@Param("area") Integer area, @Param("id") Integer id, @Param("startTime") String startTime, @Param("endTime")  String endTime);

	List<Zjykydtop5> selectAll2(@Param("area") Integer area, @Param("startTime") String startTime, @Param("endTime")  String endTime);

	Integer getSqLastMonth(@Param("area") Integer area,  @Param("startTime") String startTime,  @Param("endTime") String endTime);
	
	Integer getXqLastMonth(@Param("area") Integer area, 
			@Param("startTime") String startTime,  @Param("endTime") String endTime, @Param("id") Integer id);
	
	Integer getJqLastMonth(@Param("area") Integer area, 
			@Param("startTime") String startTime,  @Param("endTime") String endTime, @Param("id") Integer id);

	List<KydfxTop5Data> selectTop5Xq(@Param("id") Integer id, @Param("startTime") String startTime,  @Param("endTime") String endTime);

	List<KydfxTop5Data> selectTop5Jq(@Param("id") Integer id,  @Param("startTime") String startTime,  @Param("endTime") String endTime);

	List<KydfxTop5Data> selectTop5Sq( @Param("startTime") String startTime,  @Param("endTime") String endTime);

	List<KydfxTop5Data> selectTop5XqSn(@Param("id") Integer id, @Param("startTime") String startTime,  @Param("endTime") String endTime);

	List<KydfxTop5Data> selectTop5JqSn(@Param("id") Integer id, @Param("startTime") String startTime,  @Param("endTime") String endTime);

	List<KydfxTop5Data> selectTop5SqSn(@Param("startTime") String startTime,  @Param("endTime") String endTime);
	/**
	 * 查询所有某年所有数据的月
	 * @param year
	 * @return
	 */
	List<Integer> getMonths(@Param("year") Integer year);
	/**
	 * 根据year参数查询国内 某年某月自驾游客源占比
	 * @param year
	 * @return
	 */
	public Double getGuoneiPercent(@Param("year") Integer year);
	/**
	 * 根据year参数查询省内 某年某月自驾游客源占比
	 * @param year
	 * @return
	 */
	public Double getShengneiPercent(@Param("year") Integer year);
	/**
	 * 根据year参数查询本市某年某月自驾游客源占比
	 * @param year
	 * @return
	 */
	public Double getBenshiPercent(@Param("year") Integer year);
	
	
	
	
}