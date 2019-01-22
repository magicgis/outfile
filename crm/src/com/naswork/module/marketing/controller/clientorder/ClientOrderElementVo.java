package com.naswork.module.marketing.controller.clientorder;

import java.util.Date;
import java.util.List;

import com.naswork.model.ClientOrderElement;

public class ClientOrderElementVo {
	private Integer clientId;

	private Integer complete;
	
	private String userName;
	
	private String orderDescription;
	
	private Double bankCharges;
	
	private String remark;
	
	private Integer spId;
	
	private Integer csn;
	
	private Double fixedCost;
	
	private Integer spzt;
	
	private String orderStatusValue;
	
	private Integer orderStatusId;
	
	private Double importAmount;

	private Integer clientOrderElementId;
	
	private Integer id;
	
	private String alterPartNumber;
	
	private Integer clientOrderId;
	
	private Integer item;
	
	private Integer clientQuoteElementId;
	
	private Integer clientQuoteId;
	
	private Integer clientInquiryElementId;
	
	private Integer supplierQuoteElementId;
	
	private Double exchangeRate;
	
	private String leadTime;
	
	private String detail;
	
	private Double clientQuotePrice;
	
	private Double supplierQuotePrice;
	
	private Double supplierQuoteExchangeRate;
	
	private Integer supplierId;
	
	private String supplierCode;
	
	private String supplierName;
	
	private Integer conditionId;
	
	private String conditionCode;
	
	private String conditionValue;
	
	private Integer certificationId;
	
	private String certificationCode;
	
	private String certificationValue;
	
	private Integer supplierQuoteStatusId;
	
	private String supplierQuoteStatusCode;
	
	private String supplierQuoteStatusValue;
	
	private Integer inquiryElementId;

	private Integer quoteElementId;
	
	private String inquiryPartNumber;
	
	private String quotePartNumber;
	
	private String inquiryUnit;
	
	private String quoteUnit;
	
	private Double inquiryAmount;
	
	private Double supplierQuoteAmount;

	private Double clientQuoteAmount;
	
	private String inquiryDescription;
	
	private String quoteDescription;
	
	private String inquiryRemark;
	
	private String quoteRemark;
	
	private String clientQuoteRemark;
	
	private Integer currencyId;

	private String currencyCode;
	
	private String currencyValue;
	
	private Double clientOrderAmount;
	
	private Double clientOrderPrice;
	
	private String orderNumber;
	
	private String clientOrderLeadTime;
	
	private Date clientOrderDeadline;
	
	private Double exportAmount;
	
	private Double storageAmount;
	
	private Double totalprice;
	
	private Date updateTimestamp;
	
	private String location;
	
	private Double amount;
	
	private String exportPackageAwb;
	
	private String supplierOrderAwb;
	
	private Date exportDate;
	
	private Double total;
	
	private Date receiveDate;
	
	private String exportAmountPercent;
	
	private Date orderDate;
	
	private Double orderPrice;
	
	private Integer receivePayPeriod;
	
	private Double receivePayRate;
	
	private Double weight;
	
	private String size;
	
	private String logisticsWayValue;
	
	private String destination;
	
	private String shipWayValue;
	
	private Date supplierOrderDeadline;
	
	private String bsn;
	
	private Integer splitReceive;
	
	private Double freight;
	
	private Double originalAmount;
	
	public String sourceNumber;
	
	public String terms;
	
	public String realSupplierCode;
	
	public String realSupplierQuotePrice;
	
	public String unit;
	
	public Integer supplierQuoteCurrencyId;
	
	private Integer shipPayPeriod;
	
	private Double shipPayRate;
	
	private Integer overDay;
	
	private Double incomeTotal;
	
	private String paymentType;
	
	private Double period;
	
	private Double remainTotal;
	
	private Double rate;

	private String supplierStatus;

	public String getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(String supplierStatus) {
		this.supplierStatus = supplierStatus;
	}

	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSpId() {
		return spId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Double getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public void setSpId(Integer spId) {
		this.spId = spId;
	}

	public String getOrderStatusValue() {
		return orderStatusValue;
	}

	public void setOrderStatusValue(String orderStatusValue) {
		this.orderStatusValue = orderStatusValue;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public Integer getSpzt() {
		return spzt;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public Double getImportAmount() {
		return importAmount;
	}

	public void setImportAmount(Double importAmount) {
		this.importAmount = importAmount;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientOrderId() {
		return clientOrderId;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	public void setClientOrderId(Integer clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getClientQuoteElementId() {
		return clientQuoteElementId;
	}

	public void setClientQuoteElementId(Integer clientQuoteElementId) {
		this.clientQuoteElementId = clientQuoteElementId;
	}

	public Integer getClientQuoteId() {
		return clientQuoteId;
	}

	public void setClientQuoteId(Integer clientQuoteId) {
		this.clientQuoteId = clientQuoteId;
	}

	public Integer getClientInquiryElementId() {
		return clientInquiryElementId;
	}

	public void setClientInquiryElementId(Integer clientInquiryElementId) {
		this.clientInquiryElementId = clientInquiryElementId;
	}

	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}

	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Double getClientQuotePrice() {
		return clientQuotePrice;
	}

	public void setClientQuotePrice(Double clientQuotePrice) {
		this.clientQuotePrice = clientQuotePrice;
	}

	public Double getSupplierQuotePrice() {
		return supplierQuotePrice;
	}

	public void setSupplierQuotePrice(Double supplierQuotePrice) {
		this.supplierQuotePrice = supplierQuotePrice;
	}

	public Double getSupplierQuoteExchangeRate() {
		return supplierQuoteExchangeRate;
	}

	public void setSupplierQuoteExchangeRate(Double supplierQuoteExchangeRate) {
		this.supplierQuoteExchangeRate = supplierQuoteExchangeRate;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public String getOnditionCode() {
		return conditionCode;
	}

	public void setOnditionCode(String onditionCode) {
		this.conditionCode = onditionCode;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public Integer getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}

	public String getCertificationCode() {
		return certificationCode;
	}

	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}

	public String getCertificationValue() {
		return certificationValue;
	}

	public void setCertificationValue(String certificationValue) {
		this.certificationValue = certificationValue;
	}

	public Integer getSupplierQuoteStatusId() {
		return supplierQuoteStatusId;
	}

	public void setSupplierQuoteStatusId(Integer supplierQuoteStatusId) {
		this.supplierQuoteStatusId = supplierQuoteStatusId;
	}

	public String getSupplierQuoteStatusCode() {
		return supplierQuoteStatusCode;
	}

	public void setSupplierQuoteStatusCode(String supplierQuoteStatusCode) {
		this.supplierQuoteStatusCode = supplierQuoteStatusCode;
	}

	public String getSupplierQuoteStatusValue() {
		return supplierQuoteStatusValue;
	}

	public void setSupplierQuoteStatusValue(String supplierQuoteStatusValue) {
		this.supplierQuoteStatusValue = supplierQuoteStatusValue;
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

	public String getInquiryPartNumber() {
		return inquiryPartNumber;
	}

	public void setInquiryPartNumber(String inquiryPartNumber) {
		this.inquiryPartNumber = inquiryPartNumber;
	}

	public String getQuotePartNumber() {
		return quotePartNumber;
	}

	public void setQuotePartNumber(String quotePartNumber) {
		this.quotePartNumber = quotePartNumber;
	}

	public String getInquiryUnit() {
		return inquiryUnit;
	}

	public void setInquiryUnit(String inquiryUnit) {
		this.inquiryUnit = inquiryUnit;
	}

	public String getQuoteUnit() {
		return quoteUnit;
	}

	public void setQuoteUnit(String quoteUnit) {
		this.quoteUnit = quoteUnit;
	}

	public Double getInquiryAmount() {
		return inquiryAmount;
	}

	public void setInquiryAmount(Double inquiryAmount) {
		this.inquiryAmount = inquiryAmount;
	}

	public Double getSupplierQuoteAmount() {
		return supplierQuoteAmount;
	}

	public void setSupplierQuoteAmount(Double supplierQuoteAmount) {
		this.supplierQuoteAmount = supplierQuoteAmount;
	}

	public Double getClientQuoteAmount() {
		return clientQuoteAmount;
	}

	public void setClientQuoteAmount(Double clientQuoteAmount) {
		this.clientQuoteAmount = clientQuoteAmount;
	}

	public String getInquiryDescription() {
		return inquiryDescription;
	}

	public void setInquiryDescription(String inquiryDescription) {
		this.inquiryDescription = inquiryDescription;
	}

	public String getQuoteDescription() {
		return quoteDescription;
	}

	public void setQuoteDescription(String quoteDescription) {
		this.quoteDescription = quoteDescription;
	}

	public String getInquiryRemark() {
		return inquiryRemark;
	}

	public void setInquiryRemark(String inquiryRemark) {
		this.inquiryRemark = inquiryRemark;
	}

	public String getQuoteRemark() {
		return quoteRemark;
	}

	public void setQuoteRemark(String quoteRemark) {
		this.quoteRemark = quoteRemark;
	}

	public String getClientQuoteRemark() {
		return clientQuoteRemark;
	}

	public void setClientQuoteRemark(String clientQuoteRemark) {
		this.clientQuoteRemark = clientQuoteRemark;
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

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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

	public Double getClientOrderAmount() {
		return clientOrderAmount;
	}

	public void setClientOrderAmount(Double clientOrderAmount) {
		this.clientOrderAmount = clientOrderAmount;
	}

	public Double getClientOrderPrice() {
		return clientOrderPrice;
	}

	public void setClientOrderPrice(Double clientOrderPrice) {
		this.clientOrderPrice = clientOrderPrice;
	}

	public String getClientOrderLeadTime() {
		return clientOrderLeadTime;
	}

	public void setClientOrderLeadTime(String clientOrderLeadTime) {
		this.clientOrderLeadTime = clientOrderLeadTime;
	}

	public Date getClientOrderDeadline() {
		return clientOrderDeadline;
	}

	public void setClientOrderDeadline(Date clientOrderDeadline) {
		this.clientOrderDeadline = clientOrderDeadline;
	}

	public Double getExportAmount() {
		return exportAmount;
	}

	public void setExportAmount(Double exportAmount) {
		this.exportAmount = exportAmount;
	}

	public Double getStorageAmount() {
		return storageAmount;
	}

	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public String getAlterPartNumber() {
		return alterPartNumber;
	}

	public void setAlterPartNumber(String alterPartNumber) {
		this.alterPartNumber = alterPartNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getExportAmountPercent() {
		return exportAmountPercent;
	}

	public void setExportAmountPercent(String exportAmountPercent) {
		this.exportAmountPercent = exportAmountPercent;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Integer receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
	}

	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLogisticsWayValue() {
		return logisticsWayValue;
	}

	public void setLogisticsWayValue(String logisticsWayValue) {
		this.logisticsWayValue = logisticsWayValue;
	}

	public String getExportPackageAwb() {
		return exportPackageAwb;
	}

	public void setExportPackageAwb(String exportPackageAwb) {
		this.exportPackageAwb = exportPackageAwb;
	}

	public String getSupplierOrderAwb() {
		return supplierOrderAwb;
	}

	public void setSupplierOrderAwb(String supplierOrderAwb) {
		this.supplierOrderAwb = supplierOrderAwb;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getShipWayValue() {
		return shipWayValue;
	}

	public void setShipWayValue(String shipWayValue) {
		this.shipWayValue = shipWayValue;
	}

	public Date getSupplierOrderDeadline() {
		return supplierOrderDeadline;
	}

	public void setSupplierOrderDeadline(Date supplierOrderDeadline) {
		this.supplierOrderDeadline = supplierOrderDeadline;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}
	
	/**
	 * @return the freight
	 */
	public Double getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(Double freight) {
		this.freight = freight;
	}

	/**
	 * @return the splitReceive
	 */
	public Integer getSplitReceive() {
		return splitReceive;
	}

	/**
	 * @param splitReceive the splitReceive to set
	 */
	public void setSplitReceive(Integer splitReceive) {
		this.splitReceive = splitReceive;
	}

	/**
	 * @return the originalAmount
	 */
	public Double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the sourceNumber
	 */
	public String getSourceNumber() {
		return sourceNumber;
	}

	/**
	 * @param sourceNumber the sourceNumber to set
	 */
	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	/**
	 * @return the terms
	 */
	public String getTerms() {
		return terms;
	}

	/**
	 * @param terms the terms to set
	 */
	public void setTerms(String terms) {
		this.terms = terms;
	}

	/**
	 * @return the realSupplierCode
	 */
	public String getRealSupplierCode() {
		return realSupplierCode;
	}

	/**
	 * @param realSupplierCode the realSupplierCode to set
	 */
	public void setRealSupplierCode(String realSupplierCode) {
		this.realSupplierCode = realSupplierCode;
	}

	/**
	 * @return the realSupplierQuotePrice
	 */
	public String getRealSupplierQuotePrice() {
		return realSupplierQuotePrice;
	}

	/**
	 * @param realSupplierQuotePrice the realSupplierQuotePrice to set
	 */
	public void setRealSupplierQuotePrice(String realSupplierQuotePrice) {
		this.realSupplierQuotePrice = realSupplierQuotePrice;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the supplierQuoteCurrencyId
	 */
	public Integer getSupplierQuoteCurrencyId() {
		return supplierQuoteCurrencyId;
	}

	/**
	 * @param supplierQuoteCurrencyId the supplierQuoteCurrencyId to set
	 */
	public void setSupplierQuoteCurrencyId(Integer supplierQuoteCurrencyId) {
		this.supplierQuoteCurrencyId = supplierQuoteCurrencyId;
	}

	/**
	 * @return the shipPayPeriod
	 */
	public Integer getShipPayPeriod() {
		return shipPayPeriod;
	}

	/**
	 * @param shipPayPeriod the shipPayPeriod to set
	 */
	public void setShipPayPeriod(Integer shipPayPeriod) {
		this.shipPayPeriod = shipPayPeriod;
	}

	/**
	 * @return the shipPayRate
	 */
	public Double getShipPayRate() {
		return shipPayRate;
	}

	/**
	 * @param shipPayRate the shipPayRate to set
	 */
	public void setShipPayRate(Double shipPayRate) {
		this.shipPayRate = shipPayRate;
	}

	/**
	 * @return the overDay
	 */
	public Integer getOverDay() {
		return overDay;
	}

	/**
	 * @param overDay the overDay to set
	 */
	public void setOverDay(Integer overDay) {
		this.overDay = overDay;
	}

	/**
	 * @return the incomeTotal
	 */
	public Double getIncomeTotal() {
		return incomeTotal;
	}

	/**
	 * @param incomeTotal the incomeTotal to set
	 */
	public void setIncomeTotal(Double incomeTotal) {
		this.incomeTotal = incomeTotal;
	}

	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the period
	 */
	public Double getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Double period) {
		this.period = period;
	}

	/**
	 * @return the remainTotal
	 */
	public Double getRemainTotal() {
		return remainTotal;
	}

	/**
	 * @param remainTotal the remainTotal to set
	 */
	public void setRemainTotal(Double remainTotal) {
		this.remainTotal = remainTotal;
	}

	/**
	 * @return the rate
	 */
	public Double getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}

	
}
