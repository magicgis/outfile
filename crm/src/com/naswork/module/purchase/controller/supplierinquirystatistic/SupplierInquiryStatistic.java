package com.naswork.module.purchase.controller.supplierinquirystatistic;

import java.util.Date;

public class SupplierInquiryStatistic {
	private String quotationRate;
	
	private Integer quoteAmount;
	
	private	String userId;

	private String supplierCode;
	
	private String airTypeCode;
	
	private String bizTypeCode;
	
	private Integer supplierInquiryCount;
	
	private Integer supplierQuoteCount;
	
	private Integer supplierId;
	
	private Integer airTypeId;
	
	private Integer bizTypeId;
	
	private Double supplierQuoteSum;
	
	private Integer supplierOrderCount;
	
	private Double supplierOrderSum;
	
	private Integer supplierImportCount;
	
	private Double supplierImportSum;
	
	private String startDate;
	
	private String endDate;

	private Integer amount;


	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}


	public String getQuotationRate() {
		return quotationRate;
	}

	public void setQuotationRate(String quotationRate) {
		this.quotationRate = quotationRate;
	}

	public Integer getQuoteAmount() {
		return quoteAmount;
	}

	public void setQuoteAmount(Integer quoteAmount) {
		this.quoteAmount = quoteAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getAirTypeCode() {
		return airTypeCode;
	}

	public void setAirTypeCode(String airTypeCode) {
		this.airTypeCode = airTypeCode;
	}

	public String getBizTypeCode() {
		return bizTypeCode;
	}

	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	public Integer getSupplierInquiryCount() {
		return supplierInquiryCount;
	}

	public void setSupplierInquiryCount(Integer supplierInquiryCount) {
		this.supplierInquiryCount = supplierInquiryCount;
	}

	public Integer getSupplierQuoteCount() {
		return supplierQuoteCount;
	}

	public void setSupplierQuoteCount(Integer supplierQuoteCount) {
		this.supplierQuoteCount = supplierQuoteCount;
	}

	public Double getSupplierQuoteSum() {
		return supplierQuoteSum;
	}

	public void setSupplierQuoteSum(Double supplierQuoteSum) {
		this.supplierQuoteSum = supplierQuoteSum;
	}

	public Integer getSupplierOrderCount() {
		return supplierOrderCount;
	}

	public void setSupplierOrderCount(Integer supplierOrderCount) {
		this.supplierOrderCount = supplierOrderCount;
	}

	public Double getSupplierOrderSum() {
		return supplierOrderSum;
	}

	public void setSupplierOrderSum(Double supplierOrderSum) {
		this.supplierOrderSum = supplierOrderSum;
	}

	public Integer getSupplierImportCount() {
		return supplierImportCount;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getAirTypeId() {
		return airTypeId;
	}

	public void setAirTypeId(Integer airTypeId) {
		this.airTypeId = airTypeId;
	}

	public Integer getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(Integer bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public void setSupplierImportCount(Integer supplierImportCount) {
		this.supplierImportCount = supplierImportCount;
	}

	public Double getSupplierImportSum() {
		return supplierImportSum;
	}

	public void setSupplierImportSum(Double supplierImportSum) {
		this.supplierImportSum = supplierImportSum;
	}

}
