package com.naswork.vo.excel;

import java.util.List;

/**
 * 自定义导出报表时的Grid表格模型
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class ReportGridModel {
	
	private List<ReportGridHeaderModel> gridHeaderModelList ;
	private ReportGridDataModel gridDataModel;
	private String fileName;
	private int columnCount;
	private int maxHeaderLevel;
	private boolean showBorder=true;
	private boolean showHeader=true;
	public List<ReportGridHeaderModel> getGridHeaderModelList() {
		return gridHeaderModelList;
	}
	public ReportGridDataModel getGridDataModel() {
		return gridDataModel;
	}
	public String getFileName() {
		return fileName;
	}
	public int getColumnCount() {
		return columnCount;
	}
	public int getMaxHeaderLevel() {
		return maxHeaderLevel;
	}
	public boolean isShowBorder() {
		return showBorder;
	}
	public boolean isShowHeader() {
		return showHeader;
	}
	public void setGridHeaderModelList(
			List<ReportGridHeaderModel> gridHeaderModelList) {
		this.gridHeaderModelList = gridHeaderModelList;
	}
	public void setGridDataModel(ReportGridDataModel gridDataModel) {
		this.gridDataModel = gridDataModel;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	public void setMaxHeaderLevel(int maxHeaderLevel) {
		this.maxHeaderLevel = maxHeaderLevel;
	}
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}
	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

}
