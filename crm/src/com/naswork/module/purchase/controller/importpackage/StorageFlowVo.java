package com.naswork.module.purchase.controller.importpackage;

import java.util.Date;

public class StorageFlowVo {
	private String exportpackage;
	
	private Integer importPackageId;
	
	private Double boxWeight;
	
	private Integer tax;
	
	private Integer ipeSupplierOrderElementId;
	
	private Integer supplierOrderElementId;
	
	private Double importPackageAmount; 
	
	private Integer clientQuoteElementId;
	
	private Integer clientInquiryElementId;
	
	private Integer soeId;
	
	private Integer importPackageElementId;
	
	private Integer elementId;
	
	private Integer currencyId;
	
	private Integer clientOrderElementId;
	
	private Integer completeComplianceCertificate;
	
	private Integer complianceCertificate;
	
	private String importDateStart;
	
	private String importDateEnd;
	
	private Integer logisticsWay;

	private String logisticsNo;
	
	private String logisticsWayValue;

	private Integer id;

	private String importNumber;

	private Date importDate;

	private Double exchangeRate;

	private String partNumber;

	private String description;

	private String unit;

	private Double price;

	private Double basePrice;

	private Double totalBasePrice;
	
	private Integer total;
	
	private Double totalAmount;

	private Integer certificationId;

	private Integer conditionId;

	private String conditionCode;

	private String conditionValue;

	private String certificationCode;

	private String certificationValue;

	private String serialNumber;

	private Integer originalNumber;

	private Date certificationDate;

	private String remark;

	private String location;

	private Date updateTimestamp;

	private Double storageAmount;
	
	private String partNumberCode;
	
	private Double amount;
	
	private Integer supplierId;
	
	private String supplierCode;

	private Integer clientId;
	
	private String clientCode;
	
	private String exportNumber;
	
	private Date exportDate;
	
	private String orderNumber;
	
	private String storageType;
	
	private Date storageDate;
	
	private Double profitMargin;
	
	private Boolean correlation;
	
	private Double useAmount;

	public Double getProfitMargin() {
		return profitMargin;
	}

	public String getExportpackage() {
		return exportpackage;
	}

	public void setExportpackage(String exportpackage) {
		this.exportpackage = exportpackage;
	}

	public Integer getImportPackageId() {
		return importPackageId;
	}

	public void setImportPackageId(Integer importPackageId) {
		this.importPackageId = importPackageId;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getBoxWeight() {
		return boxWeight;
	}

	public void setBoxWeight(Double boxWeight) {
		this.boxWeight = boxWeight;
	}

	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	public String getImportNumber() {
		return importNumber;
	}

	public Integer getIpeSupplierOrderElementId() {
		return ipeSupplierOrderElementId;
	}

	public void setIpeSupplierOrderElementId(Integer ipeSupplierOrderElementId) {
		this.ipeSupplierOrderElementId = ipeSupplierOrderElementId;
	}

	public Double getImportPackageAmount() {
		return importPackageAmount;
	}

	public void setImportPackageAmount(Double importPackageAmount) {
		this.importPackageAmount = importPackageAmount;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getSupplierOrderElementId() {
		return supplierOrderElementId;
	}

	public void setSupplierOrderElementId(Integer supplierOrderElementId) {
		this.supplierOrderElementId = supplierOrderElementId;
	}

	public Integer getClientQuoteElementId() {
		return clientQuoteElementId;
	}

	public void setClientQuoteElementId(Integer clientQuoteElementId) {
		this.clientQuoteElementId = clientQuoteElementId;
	}

	public Integer getClientInquiryElementId() {
		return clientInquiryElementId;
	}

	public void setClientInquiryElementId(Integer clientInquiryElementId) {
		this.clientInquiryElementId = clientInquiryElementId;
	}

	public Integer getSoeId() {
		return soeId;
	}

	public void setSoeId(Integer soeId) {
		this.soeId = soeId;
	}

	public Integer getImportPackageElementId() {
		return importPackageElementId;
	}

	public void setImportPackageElementId(Integer importPackageElementId) {
		this.importPackageElementId = importPackageElementId;
	}

	public Integer getElementId() {
		return elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setImportNumber(String importNumber) {
		this.importNumber = importNumber;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getImportDate() {
		return importDate;
	}


	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getImportDateStart() {
		return importDateStart;
	}

	public void setImportDateStart(String importDateStart) {
		this.importDateStart = importDateStart;
	}

	public String getImportDateEnd() {
		return importDateEnd;
	}

	public void setImportDateEnd(String importDateEnd) {
		this.importDateEnd = importDateEnd;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Integer getLogisticsWay() {
		return logisticsWay;
	}

	public void setLogisticsWay(Integer logisticsWay) {
		this.logisticsWay = logisticsWay;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsWayValue() {
		return logisticsWayValue;
	}

	public void setLogisticsWayValue(String logisticsWayValue) {
		this.logisticsWayValue = logisticsWayValue;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getTotalBasePrice() {
		return totalBasePrice;
	}

	public void setTotalBasePrice(Double totalBasePrice) {
		this.totalBasePrice = totalBasePrice;
	}

	public Integer getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getOriginalNumber() {
		return originalNumber;
	}

	public void setOriginalNumber(Integer originalNumber) {
		this.originalNumber = originalNumber;
	}

	public Date getCertificationDate() {
		return certificationDate;
	}

	public String getPartNumberCode() {
		return partNumberCode;
	}

	public void setPartNumberCode(String partNumberCode) {
		this.partNumberCode = partNumberCode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getExportNumber() {
		return exportNumber;
	}

	public void setExportNumber(String exportNumber) {
		this.exportNumber = exportNumber;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}
	public Integer getCompleteComplianceCertificate() {
		return completeComplianceCertificate;
	}

	public void setCompleteComplianceCertificate(Integer completeComplianceCertificate) {
		this.completeComplianceCertificate = completeComplianceCertificate;
	}

	public Integer getComplianceCertificate() {
		return complianceCertificate;
	}

	public void setComplianceCertificate(Integer complianceCertificate) {
		this.complianceCertificate = complianceCertificate;
	}
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public Date getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}

	public void setCertificationDate(Date certificationDate) {
		this.certificationDate = certificationDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Double getStorageAmount() {
		return storageAmount;
	}

	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}

	/**
	 * @return the correlation
	 */
	public Boolean getCorrelation() {
		return correlation;
	}

	/**
	 * @param correlation the correlation to set
	 */
	public void setCorrelation(Boolean correlation) {
		this.correlation = correlation;
	}

	/**
	 * @return the useAmount
	 */
	public Double getUseAmount() {
		return useAmount;
	}

	/**
	 * @param useAmount the useAmount to set
	 */
	public void setUseAmount(Double useAmount) {
		this.useAmount = useAmount;
	}
}
