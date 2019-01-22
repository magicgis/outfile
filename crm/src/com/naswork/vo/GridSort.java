package com.naswork.vo;

import java.io.Serializable;

public class GridSort implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5368624384740186518L;
	//排序字段
	private String name;
	//DESC ASC
	private String order;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public GridSort() {
		super();
	}
	
	public GridSort(String name, String order) {
		super();
		this.name = name;
		this.order = order;
	}
	
}
