package com.naswork.vo.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义导出报表时,导出Grid的列头模型
 * @version 1.0
 */
public class ReportGridHeaderModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String columnName;//列表
	private String label;//列标题
	private int fontSize;//字体大小
	private int[] bgColor;//背景颜色
	private int[] fontColor;//字体颜色
	private int level;//层级
	private int align;//表头水平对齐方式
	private int width;//列宽
	private int dataAlign;//数据水平对齐方式
	private ReportGridHeaderModel  parent;
	private List<ReportGridHeaderModel> headers=new ArrayList<ReportGridHeaderModel>();
	public String getColumnName() {
		return columnName;
	}
	public String getLabel() {
		return label;
	}
	public int getFontSize() {
		return fontSize;
	}
	public int[] getBgColor() {
		return bgColor;
	}
	public int[] getFontColor() {
		return fontColor;
	}
	public int getLevel() {
		return level;
	}
	public int getAlign() {
		return align;
	}
	public int getWidth() {
		return width;
	}
	public ReportGridHeaderModel getParent() {
		return parent;
	}
	public List<ReportGridHeaderModel> getHeaders() {
		return headers;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public void setBgColor(int[] bgColor) {
		this.bgColor = bgColor;
	}
	public void setFontColor(int[] fontColor) {
		this.fontColor = fontColor;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setAlign(int align) {
		this.align = align;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setParent(ReportGridHeaderModel parent) {
		this.parent = parent;
	}
	public void setHeaders(List<ReportGridHeaderModel> headers) {
		this.headers = headers;
	}
	public  void addGridHeader(ReportGridHeaderModel header){
    	headers.add(header);
    }
	public int getDataAlign() {
		return dataAlign;
	}
	public void setDataAlign(int dataAlign) {
		this.dataAlign = dataAlign;
	}

}
