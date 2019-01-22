package com.naswork.vo;

public class PageVo {
	private Integer showSuggestion;//审核意见域 0不显示1显示
	private String buninessKey;//业务key
	private Integer showSubmit;//提交0不显示1显示
	private Integer showAgreeButton;//同意按钮0不显示1显示
	private Integer showRejectButton;//驳回按钮0不显示1显示
	private Integer showCommissionShall;//委托办理0不显示1显示
	private Integer staffSelections;//选择人员0组织树选择1角色选择
	private String owner;//所属人员id
	private String taskId;//任务id
	private String url;//form提交的表单值
	private String title;//流程节点名称
	
	public Integer getShowSuggestion() {
		return showSuggestion;
	}
	public void setShowSuggestion(Integer showSuggestion) {
		this.showSuggestion = showSuggestion;
	}
	public String getBuninessKey() {
		return buninessKey;
	}
	public void setBuninessKey(String buninessKey) {
		this.buninessKey = buninessKey;
	}
	public Integer getShowSubmit() {
		return showSubmit;
	}
	public void setShowSubmit(Integer showSubmit) {
		this.showSubmit = showSubmit;
	}
	public Integer getShowAgreeButton() {
		return showAgreeButton;
	}
	public void setShowAgreeButton(Integer showAgreeButton) {
		this.showAgreeButton = showAgreeButton;
	}
	public Integer getShowRejectButton() {
		return showRejectButton;
	}
	public void setShowRejectButton(Integer showRejectButton) {
		this.showRejectButton = showRejectButton;
	}
	public Integer getShowCommissionShall() {
		return showCommissionShall;
	}
	public void setShowCommissionShall(Integer showCommissionShall) {
		this.showCommissionShall = showCommissionShall;
	}
	public Integer getStaffSelections() {
		return staffSelections;
	}
	public void setStaffSelections(Integer staffSelections) {
		this.staffSelections = staffSelections;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
