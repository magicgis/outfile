package com.naswork.vo;

import java.io.Serializable;
import java.util.List;

public class ColumnVo  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2988098872160644196L;

	private List<String> columnDisplayNames;
	
	private List<String> columnKeyNames;

	public List<String> getColumnDisplayNames() {
		return columnDisplayNames;
	}

	public void setColumnDisplayNames(List<String> columnDisplayNames) {
		this.columnDisplayNames = columnDisplayNames;
	}

	public List<String> getColumnKeyNames() {
		return columnKeyNames;
	}

	public void setColumnKeyNames(List<String> columnKeyNames) {
		this.columnKeyNames = columnKeyNames;
	}
	
	
}
