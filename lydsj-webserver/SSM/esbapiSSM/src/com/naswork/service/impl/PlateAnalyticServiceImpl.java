package com.naswork.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.PlateAnalyticDao;
import com.naswork.dao.TjKydfxDailyDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.PlateAnalytic;
import com.naswork.model.Zjykydtop5;
import com.naswork.service.PlateAnalyticService;

@Service("plateAnalyticServiceImpl")
public class PlateAnalyticServiceImpl implements PlateAnalyticService {
	@Resource
	private PlateAnalyticDao plateAnalyticDao;
	
	@Resource
	private TjKydfxDailyDao tjKydfxDailyDao;
	
	@Override
	public int insert(PlateAnalytic record) {
		return plateAnalyticDao.insert(record);
	}

	@Override
	public int insertSelective(PlateAnalytic record) {
		return plateAnalyticDao.insertSelective(record);
	}

	@Override
	public List<Zjykydtop5> selectAll1(Integer area, Integer id, String startTime, String endTime) {
		return plateAnalyticDao.selectAll1(area, id, startTime, endTime);
	}
	
	@Override
	public List<Zjykydtop5> selectAll2(Integer area, String startTime, String endTime) {
		return plateAnalyticDao.selectAll2(area, startTime, endTime);
	}

	@Override
	public Integer getSqLastMonth(Integer area, String startTime, String endTime) {
		return plateAnalyticDao.getSqLastMonth(area, startTime, endTime);
	}

	@Override
	public Integer getJqLastMonth(Integer area, String startTime, String endTime, Integer id) {
		return plateAnalyticDao.getJqLastMonth(area, startTime, endTime, id);
	}

	@Override
	public Integer getXqLastMonth(Integer area, String startTime, String endTime, Integer id) {
		return plateAnalyticDao.getXqLastMonth(area, startTime, endTime, id);
	}
	
	public Integer[] getZjykydDaily(int sourcescope, String startTime, String endTime,Integer[] areaIds){
		return tjKydfxDailyDao.selectZjykydDaily(sourcescope, startTime, endTime,areaIds);
	}
	
	public List<Map<String,String>> getZjykydTop5(int sourcescope, String date,Integer[] areaIds){
		return tjKydfxDailyDao.selectZjykydTop5(sourcescope, date, areaIds);
	}
	
	public Integer[] getAreaIds(int id){
		Integer[] areaIds = {};
		if (id == 1000) {
			areaIds = tjKydfxDailyDao.getJqqdAreaIds();
		}else if(id > 1000){
			areaIds = tjKydfxDailyDao.getJqqdAreaIdsByDistrictId(id);
		}else{
			areaIds = tjKydfxDailyDao.getJqqdAreaIdsById(id);
		}
		return areaIds;
		
	}

	@Override
	public List<KydfxTop5Data> selectTop5Xq(Integer id,  String startTime, String endTime) {
		return plateAnalyticDao.selectTop5Xq(id, startTime, endTime);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Jq(Integer id,  String startTime, String endTime) {
		return plateAnalyticDao.selectTop5Jq(id, startTime, endTime);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Sq(String startTime, String endTime) {
		return plateAnalyticDao.selectTop5Sq(startTime, endTime);
	}

	@Override
	public List<KydfxTop5Data> selectTop5XqSn(Integer id, String startTime, String endTime) {
		return plateAnalyticDao.selectTop5XqSn(id,  startTime, endTime);
	}

	@Override
	public List<KydfxTop5Data> selectTop5JqSn(Integer id, String startTime, String endTime) {
		return plateAnalyticDao.selectTop5JqSn(id, startTime, endTime);
	}

	@Override
	public List<KydfxTop5Data> selectTop5SqSn( String startTime, String endTime) {
		return plateAnalyticDao.selectTop5SqSn( startTime, endTime);
	}

	@Override
	public List<Integer> getMonths(Integer year) {
		return plateAnalyticDao.getMonths(year);
	}

	@Override
	public Double getGuoneiPercent(Integer year) {
		return plateAnalyticDao.getGuoneiPercent(year);
	}

	@Override
	public Double getShengneiPercent(Integer year) {
		return plateAnalyticDao.getShengneiPercent(year);
	}

	@Override
	public Double getBenshiPercent(Integer year) {
		return plateAnalyticDao.getBenshiPercent(year);
	}

}
