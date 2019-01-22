package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

public class ApprovalDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4349322062180598040L;
	
	public String approvalId;
	
	public String approvalDetailId;
	
	public String updaterId;
	
	public String updaterName;
	
	public Date updateDatetime;
	
	public Integer approvalStatus;
	
	public String comment;

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public String getApprovalDetailId() {
		return approvalDetailId;
	}

	public void setApprovalDetailId(String approvalDetailId) {
		this.approvalDetailId = approvalDetailId;
	}

	public String getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

}
