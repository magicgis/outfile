package com.naswork.module.purchase.controller.supplierinquiry;

import java.util.Date;

public class ManageElementVo {

	private Integer id;
	
	private Integer supplierInquiryId;
	
	private String alterPartNumber;
	
	private String supplierInquiryQuoteNumber;
	
	private Integer clientInquiryElementId;
	
	private Integer item;
	
	private Integer csn;
	
	private Integer elementId;
	
	private String partNumber;
	
	private String description;
	
	private String remark;
	
	private String unit;
	
	private Double amount;
	
	private Date updateTimestamp;
	
	private String conditionCode;
	
	private Double bankCost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSupplierInquiryId() {
		return supplierInquiryId;
	}

	public void setSupplierInquiryId(Integer supplierInquiryId) {
		this.supplierInquiryId = supplierInquiryId;
	}

	public String getSupplierInquiryQuoteNumber() {
		return supplierInquiryQuoteNumber;
	}

	public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
		this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
	}

	public Integer getClientInquiryElementId() {
		return clientInquiryElementId;
	}

	public void setClientInquiryElementId(Integer clientInquiryElementId) {
		this.clientInquiryElementId = clientInquiryElementId;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getElementId() {
		return elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	public String getPartNumber() {
		return partNumber;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getAlterPartNumber() {
		return alterPartNumber;
	}

	public void setAlterPartNumber(String alterPartNumber) {
		this.alterPartNumber = alterPartNumber;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	/**
	 * @return the conditionCode
	 */
	public String getConditionCode() {
		return conditionCode;
	}

	/**
	 * @param conditionCode the conditionCode to set
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	/**
	 * @return the bankCost
	 */
	public Double getBankCost() {
		return bankCost;
	}

	/**
	 * @param bankCost the bankCost to set
	 */
	public void setBankCost(Double bankCost) {
		this.bankCost = bankCost;
	}

}
