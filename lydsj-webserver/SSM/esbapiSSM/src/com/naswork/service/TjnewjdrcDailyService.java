package com.naswork.service;

import java.util.List;

import com.naswork.model.TjnewjdrcDaily;

public interface TjnewjdrcDailyService {
	/**
	 * 根据year参数、month参数、id参数查询某年某月所有天接待人次-全市/景区每月接待人次
	 * @param year
	 * @param month
	 * @param id
	 * @return
	 */
	public List<TjnewjdrcDaily> getmrijdrcDaily( Integer year, Integer month, Integer id);
	/**
	 * 根据year参数、month参数、id参数查询某年某月所有天接待人次-3a景区每月接待人次
	 * @param year
	 * @param month
	 * @param level
	 * @return
	 */
	public List<TjnewjdrcDaily> getQs3amrijdrcDaily( Integer year, Integer month, Integer level);
}
