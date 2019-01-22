package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.naswork.model.TjnewjdrcDaily;

@Component("tjnewjdrcDailyDao")
public interface TjnewjdrcDailyDao {
	/**
	 * 根据year参数、month参数、id参数查询某年某月所有天接待人次-全市/景区每月接待人次
	 * @param year
	 * @param month
	 * @param id
	 * @return
	 */
	public List<TjnewjdrcDaily> getmrijdrcDaily(@Param("year") Integer year,@Param("month") Integer month,@Param("id") Integer id);
	/**
	 * 根据year参数、month参数、id参数查询某年某月所有天接待人次-3a景区每月接待人次
	 * @param year
	 * @param month
	 * @param level
	 * @return
	 */
	public List<TjnewjdrcDaily> getQs3amrijdrcDaily( @Param("year") Integer year,@Param("month") Integer month,@Param("level") Integer level);
	
	
}