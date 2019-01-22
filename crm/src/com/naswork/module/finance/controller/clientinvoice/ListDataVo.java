package com.naswork.module.finance.controller.clientinvoice;

import java.util.Date;

public class ListDataVo {
	private String exportNumber;
	private String exportPackageInstructionsNumber;
	private Double uncollected;
	private Double totalPrice;
	private Integer invoiceType;
	private Integer id;
	private String invoiceNumber;
	private Integer clientOrderId;
	private String remark;
	private Date updateTimestamp;
	private Date orderDate;
	private Date invoiceDate;
	private Integer invoiceTerms;
	private String sourceOrderNumber;
	private String orderNumber;
	private Integer terms;
	private Integer clientId;
	private String clientCode;
	private String clientName;
	private String address;
	private String shipAddress;
	private String contactAddress;
	private String contactShipAddress;
	private Integer currencyId;
	private String currencyCode;
	private String currencyValue;
	private Double clientInvoicePrice;
	private Double clientOrderPrice;
	private Integer invoiceStatusId;
	public Integer getInvoiceStatusId() {
		return invoiceStatusId;
	}
	public String getExportNumber() {
		return exportNumber;
	}
	public void setExportNumber(String exportNumber) {
		this.exportNumber = exportNumber;
	}
	public String getExportPackageInstructionsNumber() {
		return exportPackageInstructionsNumber;
	}
	public void setExportPackageInstructionsNumber(String exportPackageInstructionsNumber) {
		this.exportPackageInstructionsNumber = exportPackageInstructionsNumber;
	}
	public Double getUncollected() {
		return uncollected;
	}
	public void setUncollected(Double uncollected) {
		this.uncollected = uncollected;
	}
	public void setInvoiceStatusId(Integer invoiceStatusId) {
		this.invoiceStatusId = invoiceStatusId;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Integer getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(Integer clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Integer getInvoiceTerms() {
		return invoiceTerms;
	}
	public void setInvoiceTerms(Integer invoiceTerms) {
		this.invoiceTerms = invoiceTerms;
	}
	public String getSourceOrderNumber() {
		return sourceOrderNumber;
	}
	public void setSourceOrderNumber(String sourceOrderNumber) {
		this.sourceOrderNumber = sourceOrderNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getTerms() {
		return terms;
	}
	public void setTerms(Integer terms) {
		this.terms = terms;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getShipAddress() {
		return shipAddress;
	}
	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getContactShipAddress() {
		return contactShipAddress;
	}
	public void setContactShipAddress(String contactShipAddress) {
		this.contactShipAddress = contactShipAddress;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyValue() {
		return currencyValue;
	}
	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}
	public Double getClientInvoicePrice() {
		return clientInvoicePrice;
	}
	public void setClientInvoicePrice(Double clientInvoicePrice) {
		this.clientInvoicePrice = clientInvoicePrice;
	}
	public Double getClientOrderPrice() {
		return clientOrderPrice;
	}
	public void setClientOrderPrice(Double clientOrderPrice) {
		this.clientOrderPrice = clientOrderPrice;
	}
}
