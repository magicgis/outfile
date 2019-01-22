package com.naswork.module.marketing.controller.clientorder;

import java.util.Date;

public class orderReview {
	private Double amount;
	
	private Integer clientId;
	
	private String partNumber;
	
	private Integer clientOrderElementId;
	
	private Integer supplierId;
	
	private Double freight;
	
	private Integer isBlacklist;
	
	private Date validity;
	
	private Integer certificationId;
	
	private Double profitMargin;
	
	private Double orderPrice;
	
	private Double fixedCost;
	
	private Double quotePrice;
	
	private Integer quotecertificationid;
	
	private Integer ordercertificationid;

	private String quoteNumber;
	
	private Integer clientQuoteElementId;
	
	private Integer supplierQuoteElementId;
	
	private Date quoteDate;


	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public Integer getClientQuoteElementId() {
		return clientQuoteElementId;
	}

	public void setClientQuoteElementId(Integer clientQuoteElementId) {
		this.clientQuoteElementId = clientQuoteElementId;
	}

	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}

	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Double getFreight() {
		return freight;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Integer getQuotecertificationid() {
		return quotecertificationid;
	}

	public void setQuotecertificationid(Integer quotecertificationid) {
		this.quotecertificationid = quotecertificationid;
	}

	public Integer getOrdercertificationid() {
		return ordercertificationid;
	}

	public void setOrdercertificationid(Integer ordercertificationid) {
		this.ordercertificationid = ordercertificationid;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Double getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(Double quotePrice) {
		this.quotePrice = quotePrice;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
	}

	public Date getValidity() {
		return validity;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}


	public Integer getCertificationId() {
		return certificationId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}

	public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

}
