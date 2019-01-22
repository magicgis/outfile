package com.naswork.vo.excel;

import java.util.List;

/**
 * 表头模型
 * @since 2013-5-24
 * @author chenguojun (mailto:cgj312@qq.com)
 * @version 1.00 2013-5-24
 */
public class ExcelColumnModel {
	private String label;
	private String columnName;
	private boolean hasChild;
	private List<ExcelColumnModel> child;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public List<ExcelColumnModel> getChild() {
		return child;
	}
	public void setChild(List<ExcelColumnModel> child) {
		this.child = child;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	
}
