package com.naswork.module.purchase.controller.supplierorder;

import java.util.Date;

public class SupplierListVo {
	private String tagDate;
	
	private String warranty;
	
	private String serialNumber;
	
	private String tagSrc;
	
	private String trace;
	
	private Integer inquiryElementId;

	private Integer quoteElementId;
	
	private Integer clientOrderElementId;

	private Integer id;
	
	private String conditionCode;
	
	private String certificationCode;
	
	private Integer currencyId;
	
	private Double exchangeRate;
	
	private Integer supplierId;
	
	private String supplierCode;
	
	private String clientInquiryQuoteNumber;
	
	private String supplierInquiryQuoteNumber;
	
	private Date quoteDate;
	
	private String quotePartNumber;
	
	private String quoteDescription;
	
	private Double quoteAmount;
	
	private String quoteUnit;
	
	private Double price;
	
	private String quoteRemark;
	
	private Date updateTimestamp;
	
	private String leadTime;

	private Double storageAmount;
	
	private Double onPassageAmount;
	
	private Integer drawback;
	
	public Double getStorageAmount() {
		return storageAmount;
	}


	public String getTagDate() {
		return tagDate;
	}


	public void setTagDate(String tagDate) {
		this.tagDate = tagDate;
	}


	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getTagSrc() {
		return tagSrc;
	}

	public void setTagSrc(String tagSrc) {
		this.tagSrc = tagSrc;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}

	public Integer getId() {
		return id;
	}

	public Integer getInquiryElementId() {
		return inquiryElementId;
	}

	public void setInquiryElementId(Integer inquiryElementId) {
		this.inquiryElementId = inquiryElementId;
	}

	public Integer getQuoteElementId() {
		return quoteElementId;
	}

	public void setQuoteElementId(Integer quoteElementId) {
		this.quoteElementId = quoteElementId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getClientInquiryQuoteNumber() {
		return clientInquiryQuoteNumber;
	}

	public void setClientInquiryQuoteNumber(String clientInquiryQuoteNumber) {
		this.clientInquiryQuoteNumber = clientInquiryQuoteNumber;
	}

	public String getSupplierInquiryQuoteNumber() {
		return supplierInquiryQuoteNumber;
	}

	public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
		this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public String getQuotePartNumber() {
		return quotePartNumber;
	}

	public void setQuotePartNumber(String quotePartNumber) {
		this.quotePartNumber = quotePartNumber;
	}

	public String getQuoteDescription() {
		return quoteDescription;
	}

	public void setQuoteDescription(String quoteDescription) {
		this.quoteDescription = quoteDescription;
	}

	public Double getQuoteAmount() {
		return quoteAmount;
	}

	public void setQuoteAmount(Double quoteAmount) {
		this.quoteAmount = quoteAmount;
	}

	public String getQuoteUnit() {
		return quoteUnit;
	}

	public void setQuoteUnit(String quoteUnit) {
		this.quoteUnit = quoteUnit;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getQuoteRemark() {
		return quoteRemark;
	}

	public void setQuoteRemark(String quoteRemark) {
		this.quoteRemark = quoteRemark;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getCertificationCode() {
		return certificationCode;
	}

	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public Double getOnPassageAmount() {
		return onPassageAmount;
	}

	public void setOnPassageAmount(Double onPassageAmount) {
		this.onPassageAmount = onPassageAmount;
	}


	public Integer getDrawback() {
		return drawback;
	}


	public void setDrawback(Integer drawback) {
		this.drawback = drawback;
	}
	
}
