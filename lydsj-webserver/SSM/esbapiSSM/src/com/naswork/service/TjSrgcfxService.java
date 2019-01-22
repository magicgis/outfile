package com.naswork.service;

import java.util.Map;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 收入构成
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 上午10:48:45
 *
 */
public interface TjSrgcfxService {

	/**
	 * 
	 * @Description 获取某个企业某年的收入构成
	 * @param year  年份
	 * @param level 企业类别
	 * @return
	 * @version v1.0.0
	 * @author ChengAcer
	 * @date 2018年11月2日 上午10:51:26
	 */
	public Map<String, Object> getOneCNYmlnYearly(Integer year, Integer level);
}
