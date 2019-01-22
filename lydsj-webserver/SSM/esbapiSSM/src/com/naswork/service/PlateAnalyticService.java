package com.naswork.service;

import java.util.List;
import java.util.Map;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.PlateAnalytic;
import com.naswork.model.Zjykydtop5;

public interface PlateAnalyticService {
	int insert(PlateAnalytic record);

	int insertSelective(PlateAnalytic record);

	List<Zjykydtop5> selectAll1(Integer area, Integer id, String startTime, String endTime);

	List<Zjykydtop5> selectAll2(Integer area, String startTime, String endTime);

	Integer getSqLastMonth(Integer area, String startTime, String endTime);
	
	Integer getJqLastMonth(Integer area, String startTime, String endTime, Integer id);
	
	Integer getXqLastMonth(Integer area, String startTime, String endTime, Integer id);
	
	Integer[] getZjykydDaily(int sourcescope, String startTime, String endTime,Integer[] areaIds);
	
	List<Map<String,String>> getZjykydTop5(int sourcescope, String date,Integer[] areaIds);
	
	Integer[] getAreaIds(int id);

	List<KydfxTop5Data> selectTop5Xq(Integer id,  String startTime, String endTime);

	List<KydfxTop5Data> selectTop5Jq(Integer id,  String startTime, String endTime);
	
	List<KydfxTop5Data> selectTop5Sq(String startTime, String endTime);
	
	List<KydfxTop5Data> selectTop5XqSn(Integer id, String startTime, String endTime);

	List<KydfxTop5Data> selectTop5JqSn(Integer id,  String startTime, String endTime);
	
	List<KydfxTop5Data> selectTop5SqSn(String startTime, String endTime);
	/**
	 * 查询所有某年所有数据的月
	 * @param year
	 * @return
	 */
	List<Integer> getMonths(Integer year);
	/**
	 * 根据year参数查询国内 某年某月自驾游客源占比
	 * @param year
	 * @return
	 */
	public Double getGuoneiPercent( Integer year);
	/**
	 * 根据year参数查询省内 某年某月自驾游客源占比
	 * @param year
	 * @return
	 */
	public Double getShengneiPercent( Integer year);
	/**
	 * 根据year参数查询本市某年某月自驾游客源占比
	 * @param year
	 * @return
	 */
	public Double getBenshiPercent( Integer year);
}
