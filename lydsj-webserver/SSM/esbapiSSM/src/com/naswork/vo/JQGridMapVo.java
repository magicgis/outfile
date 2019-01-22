package com.naswork.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JQGridMapVo implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4020914714233611459L;
	//当前页
	private int page;
	//总页数
	private int total;
	//总记录
	private int records;
	//用户自定义数据
	private Object userdata;
	//数据表格行
	private List<Map<String,Object>> rows;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public JQGridMapVo(){
		super();
	}
	
	public JQGridMapVo(int page, int total, int records) {
		super();
		this.page = page;
		this.total = total;
		this.records = records;
	}

	public Object getUserdata() {
		return userdata;
	}

	public void setUserdata(Object userdata) {
		this.userdata = userdata;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
}
