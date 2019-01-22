package com.naswork.vo;

public class EditRowResultVo extends ResultVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3857021170305908856L;

	public EditRowResultVo(boolean success, String message) {
		super(success, message);
	}
	
	private String rowKeyName;
	
	private String rowKeyValue;

	public String getRowKeyName() {
		return rowKeyName;
	}

	public void setRowKeyName(String rowKeyName) {
		this.rowKeyName = rowKeyName;
	}

	public String getRowKeyValue() {
		return rowKeyValue;
	}

	public void setRowKeyValue(String rowKeyValue) {
		this.rowKeyValue = rowKeyValue;
	}
	
	

}
