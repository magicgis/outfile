package com.naswork.vo.excel;

/**
 * 自定义导出报表时的报表标题的样式
 * @version 1.0
 */
public class TitleStyle {

	private int[] bgColor;
	private int[] fontColor;
	private int fontSize;
	public int[] getBgColor() {
		return bgColor;
	}
	public int[] getFontColor() {
		return fontColor;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setBgColor(int[] bgColor) {
		this.bgColor = bgColor;
	}
	public void setFontColor(int[] fontColor) {
		this.fontColor = fontColor;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
