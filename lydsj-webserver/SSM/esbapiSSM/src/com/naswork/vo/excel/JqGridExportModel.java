package com.naswork.vo.excel;

public class JqGridExportModel {
	//表头名称
	private String name;
	//字符代码
	private String property;
	//宽度
	private int width;
	//对齐方式
	private int align;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getAlign() {
		return align;
	}
	public void setAlign(int align) {
		this.align = align;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
}
