package com.naswork.vo.excel;


/**
 * 自定义导出报表时的报表的Title数据模型
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportTitleModel {
	
	private boolean showPageNo=true;
	private boolean repeatHeader=false;
	private boolean showBorder=true;
	private boolean showTitle=false;
	private String title;
	private TitleStyle style;
	public boolean isShowPageNo() {
		return showPageNo;
	}
	public boolean isRepeatHeader() {
		return repeatHeader;
	}
	public boolean isShowBorder() {
		return showBorder;
	}
	public boolean isShowTitle() {
		return showTitle;
	}
	public TitleStyle getStyle() {
		return style;
	}
	public void setShowPageNo(boolean showPageNo) {
		this.showPageNo = showPageNo;
	}
	public void setRepeatHeader(boolean repeatHeader) {
		this.repeatHeader = repeatHeader;
	}
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	public void setStyle(TitleStyle style) {
		this.style = style;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
