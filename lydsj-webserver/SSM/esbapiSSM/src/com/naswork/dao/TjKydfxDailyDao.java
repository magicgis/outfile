package com.naswork.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxDaily;
import com.naswork.model.TjKydfxDailyKey;

public interface TjKydfxDailyDao {
    int deleteByPrimaryKey(TjKydfxDailyKey key);

    int insert(TjKydfxDaily record);

    int insertSelective(TjKydfxDaily record);

    TjKydfxDaily selectByPrimaryKey(TjKydfxDailyKey key);

    int updateByPrimaryKeySelective(TjKydfxDaily record);

    int updateByPrimaryKey(TjKydfxDaily record);

	Date getMaxDate();
	
	Integer[] selectZjykydDaily(@Param("sourcescope") int sourcescope,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("areaIds") Integer[] areaIds);
	
	
	List<Map<String,String>> selectZjykydTop5(@Param("sourcescope") int sourcescope,@Param("date") String date,@Param("areaIds") Integer[] areaIds);

	List<MyModel> selectTop5ById1(@Param("maxDate") String maxDateStr, @Param("areaRange") Integer areaRange);

	List<MyModel> selectTop5ById2(@Param("id") Integer id, @Param("maxDate") String maxDateStr, @Param("areaRange") Integer areaRange);


	List<TjKydfxDaily> selectByScope(@Param("id") Integer id, @Param("scope") Integer scope, @Param("idStr") String idStr);

	List<KydfxTop5Data> selectTop5(@Param("id") Integer id, @Param("scope") Integer scope, @Param("idStr") String idStr);

	
	Integer[] getJqqdAreaIds();
	
	Integer[] getJqqdAreaIdsById(@Param("id") Integer id);
	
	Integer[] getJqqdAreaIdsByDistrictId(@Param("districtId") Integer districtId);

}