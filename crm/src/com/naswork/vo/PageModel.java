package com.naswork.vo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.naswork.common.constants.Constants;
//import com.naswork.common.constants.TableMapConstant;

public class PageModel<T> extends HashMap implements Map{

	private int pageNo = 0;	// 当前页数
	private int pageSize = Constants.PAGESIZE;	// 每页显示数
	private int pageCount;	//总页数
	private int recordCount; // 总记录条数
	private List<T> entities;
	Map map = null;

	public PageModel() {
		map = new HashMap();
		//setAllDblink();
	}
	public PageModel(HttpServletRequest request) {
		map = new HashMap();
		if(StringUtils.isNotEmpty(request.getParameter("page"))){
			this.pageNo = Integer.parseInt(request.getParameter("page"));
		}
		
		if(StringUtils.isNotEmpty(request.getParameter("rows"))){
			this.pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		
		if(StringUtils.isNotEmpty(request.getParameter("exportModel"))){
			this.pageSize =100000;
		}
		
		//setAllDblink();
	}

	public PageModel(int pageNo) {
		map = new HashMap();
		this.pageNo = pageNo;
		this.pageSize = Constants.PAGESIZE;
		//setAllDblink();
	}
	
	public PageModel(int pageNo, int pageSize) {
		super();
		if (pageNo > 1 && pageSize <= 0) {
			throw new IllegalArgumentException(
					"Illegal paging arguments. [pageSize=" + pageSize
							+ ", pageIndex=" + pageNo + "]");
		}

		if (pageSize < 0)
			pageSize = 0;
		if (pageNo < 1)
			pageNo = 1;

		this.pageSize = pageSize;
		this.pageNo = pageNo;
		//setAllDblink();
	}
	
	//返回总记录数
	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		if (recordCount < 0) {
			throw new IllegalArgumentException(
					"Illegal entityCount arguments. [entityCount="
							+ recordCount + "]");
		}

		this.recordCount = recordCount;
		this.pageCount = ((recordCount - 1) / pageSize) + 1;
	}
	//返回每一页的大小，即每页的记录数。
	public int getPageSize() {
		return pageSize == 0 ? Constants.PAGESIZE : pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;		
	}

	//返回序号，序号从1开始
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	//返回总页数。
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	//返回查询结果集
	public List<T> getEntities() {
		return (null != entities) ? entities : Collections.EMPTY_LIST;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}
	
	//获取当前页下标 下标从0开始
	public int getCurrentIndex(){
		return this.pageNo-1<=0 ? 1 : (this.pageNo-1)*this.pageSize+1;
	}
	
	@Override
	public Object get(Object key) {
		Object obj = map.get(key);
		return obj;
	}
	
	public String getString(Object key) {
		return (String)get(key);
	}
	
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map t) {
		map.putAll(t);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}
	
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	/*
	public void setAllDblink() {
		putAll(TableMapConstant.TABLE_MAP);
	}
	*/
}
