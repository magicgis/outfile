package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

public class Approval  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2382889267218206324L;

	private String approvalId;
	
	private String approvalName;
	
	private String approvalType;
	
	private String associationKey;
	
	private String requesterId;
	
	private String requesterName;
	
	private String approverRoleId;
	
	private String approverRoleName;
	
	private String approverId;
	
	private String approverName;
	
	private Integer approvalStatus;
	
	private Date createDatetime;
	
	private Date updateDatetime;

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public String getApprovalName() {
		return approvalName;
	}

	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public String getAssociationKey() {
		return associationKey;
	}

	public void setAssociationKey(String associationKey) {
		this.associationKey = associationKey;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getApproverRoleId() {
		return approverRoleId;
	}

	public void setApproverRoleId(String approverRoleId) {
		this.approverRoleId = approverRoleId;
	}

	public String getApproverRoleName() {
		return approverRoleName;
	}

	public void setApproverRoleName(String approverRoleName) {
		this.approverRoleName = approverRoleName;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	
	
}
