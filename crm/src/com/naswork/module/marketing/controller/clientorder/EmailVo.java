package com.naswork.module.marketing.controller.clientorder;

import java.util.Date;

public class EmailVo {
	private Integer id;
	
	private Integer clientId;
	
	private Integer supplierId;

	private Integer sn;
	
	private String partNumber;
	
	private String description;
	
	private Double amount;
	
	private String nowImportpackNumber;
	
	private String oldImportpackNumber;
	
	private String orderNumber;
	
	private String clientOrderNumber;
	
	private String supplierOrderNumber;
	
	private Double nowAmount;
	
	private Double oldAmount;
	
	private Integer userId;
	
	private Integer emailStatus;

    private Date updateTimestamp;

	public Double getNowAmount() {
		return nowAmount;
	}

	public void setNowAmount(Double nowAmount) {
		this.nowAmount = nowAmount;
	}

	public Double getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(Double oldAmount) {
		this.oldAmount = oldAmount;
	}

	public String getClientOrderNumber() {
		return clientOrderNumber;
	}

	public void setClientOrderNumber(String clientOrderNumber) {
		this.clientOrderNumber = clientOrderNumber;
	}

	public String getSupplierOrderNumber() {
		return supplierOrderNumber;
	}

	public void setSupplierOrderNumber(String supplierOrderNumber) {
		this.supplierOrderNumber = supplierOrderNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getSn() {
		return sn;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNowImportpackNumber() {
		return nowImportpackNumber;
	}

	public void setNowImportpackNumber(String nowImportpackNumber) {
		this.nowImportpackNumber = nowImportpackNumber;
	}

	public String getOldImportpackNumber() {
		return oldImportpackNumber;
	}

	public void setOldImportpackNumber(String oldImportpackNumber) {
		this.oldImportpackNumber = oldImportpackNumber;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the emailStatus
	 */
	public Integer getEmailStatus() {
		return emailStatus;
	}

	/**
	 * @param emailStatus the emailStatus to set
	 */
	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}

	/**
	 * @return the updateTimestamp
	 */
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * @param updateTimestamp the updateTimestamp to set
	 */
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
