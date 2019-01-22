package com.naswork.module.task.controller.orderapproval;

import java.util.Date;

public class OrderApprovalVo {
	private Integer clientId;
	
	private Integer supplierStatus;
	
	private Double finalFixedCost;
	
    private Integer clientWeatherOrderId;
	
	private Double bankCharges;
	
	private Double weatherOrderbankCharges;
	
	private Double weatherOrderfixedCost;
	
	private String orderRemark;
	
	private Double cqExchangeRate;
	
	private Double oaStorageAmount;
	
	private Integer occupy;
	
	private String remark;
	
	private Double weatherOrderAmount;
	
	private Double total;
	
	private Double grossProfitAmount;
	
	private Double grossProfit;
	
	private Date importDate;
	
	private Date inspectionDate;
	
	private Date manufactureDate;
	
	private String importPartNumber;
	
	private String importDescription;
	
	private String jbyj;
	
	private Double onpassStorageAmount;
	
	private Double storageAmount;
	
	private Integer supplierWeatherOrderElementId;
	
	private Integer item;
	
	private Integer csn;
	
	private String certificationValue;

	private Integer certificationId;

	private Integer orderStatusId;

	private Date updateTimestamp;
	
	private String orderStatusValue;
	
	private Double orderAmount;
	
	private String quoteNumber;
	
	private String quotePartNumber;
	
	private Double exchangeRate;
	
	private Integer currencyId;
	
	private Double price;
	
	private Double amount;
	
	private String leadTime;
	
	private Date deadline;
	
	private String destination;
	
	private String shipWayId;
	
	private String taskId;
	
	private Integer importPackageElementId;
	
	private String description;
	
	private Double supplierOrderPrice;
	
	private Double importPrice;
	
	private String processInstanceId;
	
	private Double clientQuoteProfitMargin;
	
	private String supplierinquirynumber;
	
	private Double fixedCost;
	
	private Double freight;
	
	private String inquiryNumber;
	
	private Integer clientOrderId;
	
	private Integer id;

	private Integer clientOrderElementId;

	private Integer type;

	private Date validity;

	private Integer state;

	private Double quotePrice;

	private String supplierQuoteNumber;

	private Integer clientQuoteElementId;

	private Double profitMargin;

	private Double orderPrice;

	private Double storagePrice;

	private Double clientProfitMargin;

	private Integer supplierQuoteElementId;

	private String partNumber;

	private String code;

	private Date quoteDate;

	private Double weatherOrderprofitMargin;
	
	private Double storageprofitMargin;
	
	private Double weatherQuoteprofitMargin;
	
	private Integer supplierOrderElementId;
	
	private String destinationValue;
	
	private String shipWayValue;
	
	private String  supplierCode;

    private String  conditionValue;
	
	private String conditionCode;
	 
	private String certificationCode;
	 
	private Integer conditionId;
	
	private String location;
	
	private Double feeForExchangeBill;
	
	private Double bankCost;
	
	private Double otherFee;
	
	private Double quoteFeeForExchangeBill;
	
	private Double quoteBankCost;
	
	private Double hazmatFee;
	
	private Double quoteHazmatFee;
	
	private Double quoteOtherFee;
	
	private Double finalBankCharges;
	
	private Double clientMoq;
	
	private String storageUnit;
	
	private String orderUnit;
	
	private Double storageUsedAmount;
	
	private Integer differentUnit;
	
	private Integer orderNumberIndex;
	
	public Integer getSupplierStatus() {
		return supplierStatus;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public void setSupplierStatus(Integer supplierStatus) {
		this.supplierStatus = supplierStatus;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
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

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public Double getFinalFixedCost() {
		return finalFixedCost;
	}

	public void setFinalFixedCost(Double finalFixedCost) {
		this.finalFixedCost = finalFixedCost;
	}

	public Double getWeatherOrderbankCharges() {
		return weatherOrderbankCharges;
	}

	public void setWeatherOrderbankCharges(Double weatherOrderbankCharges) {
		this.weatherOrderbankCharges = weatherOrderbankCharges;
	}

	public Double getWeatherOrderfixedCost() {
		return weatherOrderfixedCost;
	}

	public void setWeatherOrderfixedCost(Double weatherOrderfixedCost) {
		this.weatherOrderfixedCost = weatherOrderfixedCost;
	}

	public Integer getClientWeatherOrderId() {
		return clientWeatherOrderId;
	}

	public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
		this.clientWeatherOrderId = clientWeatherOrderId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Double getCqExchangeRate() {
		return cqExchangeRate;
	}

	public void setCqExchangeRate(Double cqExchangeRate) {
		this.cqExchangeRate = cqExchangeRate;
	}

	public Integer getOccupy() {
		return occupy;
	}

	public void setOccupy(Integer occupy) {
		this.occupy = occupy;
	}

	public Double getTotal() {
		return total;
	}

	public Double getOaStorageAmount() {
		return oaStorageAmount;
	}

	public Double getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public void setOaStorageAmount(Double oaStorageAmount) {
		this.oaStorageAmount = oaStorageAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getWeatherOrderAmount() {
		return weatherOrderAmount;
	}

	public void setWeatherOrderAmount(Double weatherOrderAmount) {
		this.weatherOrderAmount = weatherOrderAmount;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getGrossProfitAmount() {
		return grossProfitAmount;
	}

	public void setGrossProfitAmount(Double grossProfitAmount) {
		this.grossProfitAmount = grossProfitAmount;
	}

	public Double getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public String getJbyj() {
		return jbyj;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Date getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public String getImportPartNumber() {
		return importPartNumber;
	}

	public void setImportPartNumber(String importPartNumber) {
		this.importPartNumber = importPartNumber;
	}

	public String getImportDescription() {
		return importDescription;
	}

	public void setImportDescription(String importDescription) {
		this.importDescription = importDescription;
	}

	public void setJbyj(String jbyj) {
		this.jbyj = jbyj;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getDestinationValue() {
		return destinationValue;
	}

	public void setDestinationValue(String destinationValue) {
		this.destinationValue = destinationValue;
	}

	public String getShipWayValue() {
		return shipWayValue;
	}

	public void setShipWayValue(String shipWayValue) {
		this.shipWayValue = shipWayValue;
	}

	public Integer getSupplierOrderElementId() {
		return supplierOrderElementId;
	}

	public Double getOnpassStorageAmount() {
		return onpassStorageAmount;
	}

	public void setOnpassStorageAmount(Double onpassStorageAmount) {
		this.onpassStorageAmount = onpassStorageAmount;
	}

	public void setSupplierOrderElementId(Integer supplierOrderElementId) {
		this.supplierOrderElementId = supplierOrderElementId;
	}

	public Double getWeatherOrderprofitMargin() {
		return weatherOrderprofitMargin;
	}

	public void setWeatherOrderprofitMargin(Double weatherOrderprofitMargin) {
		this.weatherOrderprofitMargin = weatherOrderprofitMargin;
	}

	public Double getStorageprofitMargin() {
		return storageprofitMargin;
	}

	public void setStorageprofitMargin(Double storageprofitMargin) {
		this.storageprofitMargin = storageprofitMargin;
	}

	public Double getWeatherQuoteprofitMargin() {
		return weatherQuoteprofitMargin;
	}

	public void setWeatherQuoteprofitMargin(Double weatherQuoteprofitMargin) {
		this.weatherQuoteprofitMargin = weatherQuoteprofitMargin;
	}

	public Integer getItem() {
		return item;
	}

	public Integer getSupplierWeatherOrderElementId() {
		return supplierWeatherOrderElementId;
	}

	public Double getStorageAmount() {
		return storageAmount;
	}

	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}

	public void setSupplierWeatherOrderElementId(Integer supplierWeatherOrderElementId) {
		this.supplierWeatherOrderElementId = supplierWeatherOrderElementId;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	public String getCertificationValue() {
		return certificationValue;
	}

	public void setCertificationValue(String certificationValue) {
		this.certificationValue = certificationValue;
	}

	public Integer getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getQuotePartNumber() {
		return quotePartNumber;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public String getOrderStatusValue() {
		return orderStatusValue;
	}

	public void setOrderStatusValue(String orderStatusValue) {
		this.orderStatusValue = orderStatusValue;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public void setQuotePartNumber(String quotePartNumber) {
		this.quotePartNumber = quotePartNumber;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getShipWayId() {
		return shipWayId;
	}

	public void setShipWayId(String shipWayId) {
		this.shipWayId = shipWayId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getImportPackageElementId() {
		return importPackageElementId;
	}

	public void setImportPackageElementId(Integer importPackageElementId) {
		this.importPackageElementId = importPackageElementId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getSupplierOrderPrice() {
		return supplierOrderPrice;
	}

	public void setSupplierOrderPrice(Double supplierOrderPrice) {
		this.supplierOrderPrice = supplierOrderPrice;
	}

	public Double getImportPrice() {
		return importPrice;
	}

	public void setImportPrice(Double importPrice) {
		this.importPrice = importPrice;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public Double getClientQuoteProfitMargin() {
		return clientQuoteProfitMargin;
	}

	public void setClientQuoteProfitMargin(Double clientQuoteProfitMargin) {
		this.clientQuoteProfitMargin = clientQuoteProfitMargin;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}


	public String getSupplierinquirynumber() {
		return supplierinquirynumber;
	}

	public void setSupplierinquirynumber(String supplierinquirynumber) {
		this.supplierinquirynumber = supplierinquirynumber;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Integer getType() {
		return type;
	}

	public String getInquiryNumber() {
		return inquiryNumber;
	}

	public void setInquiryNumber(String inquiryNumber) {
		this.inquiryNumber = inquiryNumber;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Double getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(Double quotePrice) {
		this.quotePrice = quotePrice;
	}

	public String getSupplierQuoteNumber() {
		return supplierQuoteNumber;
	}

	public void setSupplierQuoteNumber(String supplierQuoteNumber) {
		this.supplierQuoteNumber = supplierQuoteNumber;
	}

	public Integer getClientQuoteElementId() {
		return clientQuoteElementId;
	}

	public void setClientQuoteElementId(Integer clientQuoteElementId) {
		this.clientQuoteElementId = clientQuoteElementId;
	}

	public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Double getStoragePrice() {
		return storagePrice;
	}

	public Integer getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(Integer clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public void setStoragePrice(Double storagePrice) {
		this.storagePrice = storagePrice;
	}

	public Double getClientProfitMargin() {
		return clientProfitMargin;
	}

	public void setClientProfitMargin(Double clientProfitMargin) {
		this.clientProfitMargin = clientProfitMargin;
	}

	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}

	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}

	/**
	 * @return the feeForExchangeBill
	 */
	public Double getFeeForExchangeBill() {
		return feeForExchangeBill;
	}

	/**
	 * @param feeForExchangeBill the feeForExchangeBill to set
	 */
	public void setFeeForExchangeBill(Double feeForExchangeBill) {
		this.feeForExchangeBill = feeForExchangeBill;
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

	/**
	 * @return the otherFee
	 */
	public Double getOtherFee() {
		return otherFee;
	}

	/**
	 * @param otherFee the otherFee to set
	 */
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	/**
	 * @return the quoteFeeForExchangeBill
	 */
	public Double getQuoteFeeForExchangeBill() {
		return quoteFeeForExchangeBill;
	}

	/**
	 * @param quoteFeeForExchangeBill the quoteFeeForExchangeBill to set
	 */
	public void setQuoteFeeForExchangeBill(Double quoteFeeForExchangeBill) {
		this.quoteFeeForExchangeBill = quoteFeeForExchangeBill;
	}

	/**
	 * @return the quoteBankCost
	 */
	public Double getQuoteBankCost() {
		return quoteBankCost;
	}

	/**
	 * @param quoteBankCost the quoteBankCost to set
	 */
	public void setQuoteBankCost(Double quoteBankCost) {
		this.quoteBankCost = quoteBankCost;
	}

	/**
	 * @return the hazmatFee
	 */
	public Double getHazmatFee() {
		return hazmatFee;
	}

	/**
	 * @param hazmatFee the hazmatFee to set
	 */
	public void setHazmatFee(Double hazmatFee) {
		this.hazmatFee = hazmatFee;
	}

	/**
	 * @return the quoteHazmatFee
	 */
	public Double getQuoteHazmatFee() {
		return quoteHazmatFee;
	}

	/**
	 * @param quoteHazmatFee the quoteHazmatFee to set
	 */
	public void setQuoteHazmatFee(Double quoteHazmatFee) {
		this.quoteHazmatFee = quoteHazmatFee;
	}

	/**
	 * @return the quoteOtherFee
	 */
	public Double getQuoteOtherFee() {
		return quoteOtherFee;
	}

	/**
	 * @param quoteOtherFee the quoteOtherFee to set
	 */
	public void setQuoteOtherFee(Double quoteOtherFee) {
		this.quoteOtherFee = quoteOtherFee;
	}

	/**
	 * @return the finalBankCharges
	 */
	public Double getFinalBankCharges() {
		return finalBankCharges;
	}

	/**
	 * @param finalBankCharges the finalBankCharges to set
	 */
	public void setFinalBankCharges(Double finalBankCharges) {
		this.finalBankCharges = finalBankCharges;
	}

	/**
	 * @return the clientMoq
	 */
	public Double getClientMoq() {
		return clientMoq;
	}

	/**
	 * @param clientMoq the clientMoq to set
	 */
	public void setClientMoq(Double clientMoq) {
		this.clientMoq = clientMoq;
	}

	/**
	 * @return the storageUnit
	 */
	public String getStorageUnit() {
		return storageUnit;
	}

	/**
	 * @param storageUnit the storageUnit to set
	 */
	public void setStorageUnit(String storageUnit) {
		this.storageUnit = storageUnit;
	}

	/**
	 * @return the orderUnit
	 */
	public String getOrderUnit() {
		return orderUnit;
	}

	/**
	 * @param orderUnit the orderUnit to set
	 */
	public void setOrderUnit(String orderUnit) {
		this.orderUnit = orderUnit;
	}

	/**
	 * @return the storageUsedAmount
	 */
	public Double getStorageUsedAmount() {
		return storageUsedAmount;
	}

	/**
	 * @param storageUsedAmount the storageUsedAmount to set
	 */
	public void setStorageUsedAmount(Double storageUsedAmount) {
		this.storageUsedAmount = storageUsedAmount;
	}

	/**
	 * @return the differentUnit
	 */
	public Integer getDifferentUnit() {
		return differentUnit;
	}

	/**
	 * @param differentUnit the differentUnit to set
	 */
	public void setDifferentUnit(Integer differentUnit) {
		this.differentUnit = differentUnit;
	}

	/**
	 * @return the orderNumberIndex
	 */
	public Integer getOrderNumberIndex() {
		return orderNumberIndex;
	}

	/**
	 * @param orderNumberIndex the orderNumberIndex to set
	 */
	public void setOrderNumberIndex(Integer orderNumberIndex) {
		this.orderNumberIndex = orderNumberIndex;
	}

}
