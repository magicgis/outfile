package com.naswork.vo.excel;

/**
 * @since 2013-5-24
 * @author chenguojun (mailto:cgj312@qq.com)
 * @version 1.00 2013-5-24
 */
public class ExcelColumnJsonModel {
	private String caption;//表头中文名
	private boolean hasParent;
	private String parent;
	private String property;//将此作为name
	private int width;
	private int align;
	
	public int getAlign() {
		return align;
	}
	public void setAlign(int align) {
		this.align = align;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	public boolean isHasParent() {
		return hasParent;
	}
	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public ExcelColumnJsonModel() {
		super();

	}
	
	public ExcelColumnJsonModel(String caption, String property, boolean hasParent,
			String parent) {
		super();
		this.caption = caption;
		this.hasParent = hasParent;
		this.parent = parent;
		this.property = property;
	}
}
