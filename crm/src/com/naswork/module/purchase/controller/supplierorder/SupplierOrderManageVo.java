package com.naswork.module.purchase.controller.supplierorder;

import java.util.Date;

public class SupplierOrderManageVo {
	private String userName;
	
	private Integer orderType;
	
	private Double prepayRate;
	
	private Double shipPayRate;
	
	private Double receivePayRate;
	
	private Integer receivePayPeriod;

	private Integer id;
	
	private Integer currencyId;
	
	private String currencyCode;
	
	private String supplierCode;
	
	private String contactName;
	
	private String address;
	
	private String phone;
	
	private String fax;
	
	private String bank;
	
	private String paymentRule;
	
	private String bankAccountNumber;
	
	private String taxPayerNumber;
	
	private String supplierName;
	
	private String clientOrderNumber;
	
	private String supplierOrderNumber;
	
	private Date orderDate;
	
	private Integer terms;
	
	private String remark;
	
	private String currencyValue;
	
	private Double exchangeRate;
	
	private String orderStatusValue;
	
	private Date updateTimestamp;
	
	private Integer supplierId;
	
	private Double paymentAmount;
	
	private Double orderAmount;
	
	private Double paymentTotal;
	
	private Double orderTotal;
	
	private String amountPercent;
	
	private String totalPercent;
	
	private String urgentLevelValue;
	
	private Integer urgentLevelId;
	
	private Double total;

	private String supplierStatus;

	public String getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(String supplierStatus) {
		this.supplierStatus = supplierStatus;
	}

	public Integer getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getPrepayRate() {
		return prepayRate;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public void setPrepayRate(Double prepayRate) {
		this.prepayRate = prepayRate;
	}

	public Double getShipPayRate() {
		return shipPayRate;
	}

	public void setShipPayRate(Double shipPayRate) {
		this.shipPayRate = shipPayRate;
	}

	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}

	public Integer getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Integer receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
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

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getOrderStatusValue() {
		return orderStatusValue;
	}

	public void setOrderStatusValue(String orderStatusValue) {
		this.orderStatusValue = orderStatusValue;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Integer getTerms() {
		return terms;
	}

	public void setTerms(Integer terms) {
		this.terms = terms;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getTaxPayerNumber() {
		return taxPayerNumber;
	}

	public void setTaxPayerNumber(String taxPayerNumber) {
		this.taxPayerNumber = taxPayerNumber;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPaymentRule() {
		return paymentRule;
	}

	public void setPaymentRule(String paymentRule) {
		this.paymentRule = paymentRule;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(Double paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public Double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getAmountPercent() {
		return amountPercent;
	}

	public void setAmountPercent(String amountPercent) {
		this.amountPercent = amountPercent;
	}

	public String getTotalPercent() {
		return totalPercent;
	}

	public void setTotalPercent(String totalPercent) {
		this.totalPercent = totalPercent;
	}

	/**
	 * @return the urgentLevelValue
	 */
	public String getUrgentLevelValue() {
		return urgentLevelValue;
	}

	/**
	 * @param urgentLevelValue the urgentLevelValue to set
	 */
	public void setUrgentLevelValue(String urgentLevelValue) {
		this.urgentLevelValue = urgentLevelValue;
	}

	/**
	 * @return the urgentLevelId
	 */
	public Integer getUrgentLevelId() {
		return urgentLevelId;
	}

	/**
	 * @param urgentLevelId the urgentLevelId to set
	 */
	public void setUrgentLevelId(Integer urgentLevelId) {
		this.urgentLevelId = urgentLevelId;
	}

	/**
	 * @return the total
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}
	
	
	
}
