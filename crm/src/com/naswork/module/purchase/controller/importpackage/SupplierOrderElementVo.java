package com.naswork.module.purchase.controller.importpackage;

import java.util.Date;

public class SupplierOrderElementVo {
	private String serialNumber;
	private String orderDescription;
	private Double clientOrderImportAmount;
	private String mainOrder;
	private String orderType;
	private Double supplierOrderImportAmount;
	private String inspectionDate;
	private String manufactureDate;
	private Integer clientOrderId;
	private String clientOrderSourceNumber;
	private Integer supplierOrderElementId;
	private Integer csn;
	private Integer item;
	private Integer id;
	private Integer supplierOrderId;
	private Integer clientOrderElementId;
	private Integer supplierQuoteElementId;
	private Double supplierOrderAmount;
	private Double supplierOrderPrice;;
	private Double supplierOrderTotalPrice;
	private String leadTime;
	private String supplierOrderLeadTime;
	private Date deadline;
	private Date supplierOrderDeadline;
	private Date updateTimestamp;
	private Double importAmount;
	private Double exchangeRate;
	private Date orderDate;
	private String supplierOrderNumber;
	private Integer terms;
	private String remark;
	private Integer supplierId;
	private String clientExchangeRate;
	private String clientOrderNumber;
	private String clientCode;
	private String clientName;
	private Integer clientId;
	private String supplierCode;
	private String supplierName;
	private Double clientOrderAmount;
	private Double clientOrderPrice;
	private String clientOrderLeadTime;
	private Date clientOrderDeadline;
	private Double quoteAmount;
	private String quoteRemark;
	private String clientQuotePrice;
	private Integer conditionId;
	private Integer certificationId;
	private Integer elementId;
	private String quoteDescription;
	private String quoteUnit;
	private Double supplierQuotePrice;
	private String detail;
	private String quotePartNumber;
	private String supplierQuoteNumber;
	private String partNumberCode;
	private Integer currencyId;
	private String currencyCode;
	private String currencyValue;
	private String conditionCode;
	private String conditionValue;
	private String certificationCode;
	private String certificationValue;
	private String rk;
	private String th;
	private String onPassageStatus;
	private Integer key;
	private String taxReturnValue;
	private String location;
	private String bsn;
	private String shelfLife;
	private String inquiryPartNumber;
	public String getBsn() {
		return bsn;
	}
	public void setBsn(String bsn) {
		this.bsn = bsn;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOrderDescription() {
		return orderDescription;
	}
	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}
	public String getInspectionDate() {
		return inspectionDate;
	}
	public Double getClientOrderImportAmount() {
		return clientOrderImportAmount;
	}
	public void setClientOrderImportAmount(Double clientOrderImportAmount) {
		this.clientOrderImportAmount = clientOrderImportAmount;
	}
	public String getMainOrder() {
		return mainOrder;
	}
	public void setMainOrder(String mainOrder) {
		this.mainOrder = mainOrder;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Double getSupplierOrderImportAmount() {
		return supplierOrderImportAmount;
	}
	public void setSupplierOrderImportAmount(Double supplierOrderImportAmount) {
		this.supplierOrderImportAmount = supplierOrderImportAmount;
	}
	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	private String expiryDate;
	
	public String getExpiryDate() {
		return expiryDate;
	}
	public Integer getCsn() {
		return csn;
	}
	public void setCsn(Integer csn) {
		this.csn = csn;
	}
	public Integer getItem() {
		return item;
	}
	public void setItem(Integer item) {
		this.item = item;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getRk() {
		return rk;
	}
	public void setRk(String rk) {
		this.rk = rk;
	}
	public String getTh() {
		return th;
	}
	public void setTh(String th) {
		this.th = th;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSupplierOrderId() {
		return supplierOrderId;
	}
	public void setSupplierOrderId(Integer supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}
	public String getClientOrderSourceNumber() {
		return clientOrderSourceNumber;
	}
	public void setClientOrderSourceNumber(String clientOrderSourceNumber) {
		this.clientOrderSourceNumber = clientOrderSourceNumber;
	}
	public Integer getSupplierOrderElementId() {
		return supplierOrderElementId;
	}
	public void setSupplierOrderElementId(Integer supplierOrderElementId) {
		this.supplierOrderElementId = supplierOrderElementId;
	}
	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}
	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}
	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}
	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}
	public Double getSupplierOrderAmount() {
		return supplierOrderAmount;
	}
	public void setSupplierOrderAmount(Double supplierOrderAmount) {
		this.supplierOrderAmount = supplierOrderAmount;
	}
	public Double getSupplierOrderPrice() {
		return supplierOrderPrice;
	}
	public void setSupplierOrderPrice(Double supplierOrderPrice) {
		this.supplierOrderPrice = supplierOrderPrice;
	}
	public Double getSupplierOrderTotalPrice() {
		return supplierOrderTotalPrice;
	}
	public void setSupplierOrderTotalPrice(Double supplierOrderTotalPrice) {
		this.supplierOrderTotalPrice = supplierOrderTotalPrice;
	}
	public String getLeadTime() {
		return leadTime;
	}
	public Integer getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(Integer clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}
	public String getSupplierOrderLeadTime() {
		return supplierOrderLeadTime;
	}
	public void setSupplierOrderLeadTime(String supplierOrderLeadTime) {
		this.supplierOrderLeadTime = supplierOrderLeadTime;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public Date getSupplierOrderDeadline() {
		return supplierOrderDeadline;
	}
	public void setSupplierOrderDeadline(Date supplierOrderDeadline) {
		this.supplierOrderDeadline = supplierOrderDeadline;
	}
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public Double getImportAmount() {
		return importAmount;
	}
	public void setImportAmount(Double importAmount) {
		this.importAmount = importAmount;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getSupplierOrderNumber() {
		return supplierOrderNumber;
	}
	public void setSupplierOrderNumber(String supplierOrderNumber) {
		this.supplierOrderNumber = supplierOrderNumber;
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
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getClientExchangeRate() {
		return clientExchangeRate;
	}
	public void setClientExchangeRate(String clientExchangeRate) {
		this.clientExchangeRate = clientExchangeRate;
	}
	public String getClientOrderNumber() {
		return clientOrderNumber;
	}
	public void setClientOrderNumber(String clientOrderNumber) {
		this.clientOrderNumber = clientOrderNumber;
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
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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
	public Double getQuoteAmount() {
		return quoteAmount;
	}
	public void setQuoteAmount(Double quoteAmount) {
		this.quoteAmount = quoteAmount;
	}
	public String getQuoteRemark() {
		return quoteRemark;
	}
	public void setQuoteRemark(String quoteRemark) {
		this.quoteRemark = quoteRemark;
	}
	public String getClientQuotePrice() {
		return clientQuotePrice;
	}
	public void setClientQuotePrice(String clientQuotePrice) {
		this.clientQuotePrice = clientQuotePrice;
	}
	public Integer getConditionId() {
		return conditionId;
	}
	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}
	public Integer getCertificationId() {
		return certificationId;
	}
	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}
	public Integer getElementId() {
		return elementId;
	}
	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}
	public String getQuoteDescription() {
		return quoteDescription;
	}
	public void setQuoteDescription(String quoteDescription) {
		this.quoteDescription = quoteDescription;
	}
	public String getQuoteUnit() {
		return quoteUnit;
	}
	public void setQuoteUnit(String quoteUnit) {
		this.quoteUnit = quoteUnit;
	}
	public Double getSupplierQuotePrice() {
		return supplierQuotePrice;
	}
	public void setSupplierQuotePrice(Double supplierQuotePrice) {
		this.supplierQuotePrice = supplierQuotePrice;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getQuotePartNumber() {
		return quotePartNumber;
	}
	public void setQuotePartNumber(String quotePartNumber) {
		this.quotePartNumber = quotePartNumber;
	}
	public String getSupplierQuoteNumber() {
		return supplierQuoteNumber;
	}
	public void setSupplierQuoteNumber(String supplierQuoteNumber) {
		this.supplierQuoteNumber = supplierQuoteNumber;
	}
	public String getPartNumberCode() {
		return partNumberCode;
	}
	public void setPartNumberCode(String partNumberCode) {
		this.partNumberCode = partNumberCode;
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
	public String getConditionCode() {
		return conditionCode;
	}
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
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
	public String getOnPassageStatus() {
		return onPassageStatus;
	}
	public void setOnPassageStatus(String onPassageStatus) {
		this.onPassageStatus = onPassageStatus;
	}
	public Integer getKey() {
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public String getTaxReturnValue() {
		return taxReturnValue;
	}
	public void setTaxReturnValue(String taxReturnValue) {
		this.taxReturnValue = taxReturnValue;
	}
	/**
	 * @return the shelfLife
	 */
	public String getShelfLife() {
		return shelfLife;
	}
	/**
	 * @param shelfLife the shelfLife to set
	 */
	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}
	/**
	 * @return the inquiryPartNumber
	 */
	public String getInquiryPartNumber() {
		return inquiryPartNumber;
	}
	/**
	 * @param inquiryPartNumber the inquiryPartNumber to set
	 */
	public void setInquiryPartNumber(String inquiryPartNumber) {
		this.inquiryPartNumber = inquiryPartNumber;
	}

	
	
	
	
}
