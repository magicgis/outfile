package com.naswork.model;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 定义企业类型
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 上午11:52:42
 * 
 */
public enum CompanyEnum {
	/** 旅行社 */
	LEVEL_LXS(1, "旅行社"),
	/** 景点 */
	LEVEL_JD(2, "景点"),
	/** 宾馆酒店 */
	LEVEL_BGJD(3, "宾馆酒店"),
	/** 全部企业和 */
	LEVEL_TOTAL(4, "全部企业和");

	private int val;
	private String type;

	CompanyEnum(int val, String type) {
		this.val = val;
		this.type = type;
	}

	public int val() {
		return val;
	}

	public String type() {
		return type;
	}
}
