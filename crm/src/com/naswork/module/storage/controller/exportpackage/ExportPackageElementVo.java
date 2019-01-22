package com.naswork.module.storage.controller.exportpackage;

import java.util.Date;

public class ExportPackageElementVo {
	private Double exportPackageInstructionsAmount;
	
	private Double boxWeight;
	
	private Double countWeight; 
	
	private String clientOrderNumber;
	
	private String completeComplianceCertificateValue;
	 
	private String complianceCertificateValue;

	private Integer completeComplianceCertificate;
	 
	private Integer complianceCertificate;
	
	private Integer clientOrderElementId;
	
	private Double prepayRate;

	private Double shipPayRate;
	
	private Integer shipPayPeriod;
	
	private Double receivePayRate;
	
	private Integer receivePayPeriod;

	private Integer id;
	
	private String partNumber;
	
	private String description;
	
	private String unit;
	
	private String conditionCode;
	
	private String certificationCode;
	
	private Double importAmount;

	private Double price;
	
	private Double importPrice;
	
	private String sourceOrderNumber;
	
	private String orderNumber;
	
	private String quoteNumber;
	
	private Date updateTimestamp;
	
	private Double basePrice;
	
	private Double totalBasePrice;
	
	private String location;
	
	private Date importDate;
	
	private Date exportDate;
	
	private String clientCode;
	
	private Double amount;
	
	private String sourceNumber;
	
	private String remark;
	
	private Double storageAmount;
	
	private Double importBasePrice;
	
	private Integer importPackageElementId;
	
	private String detail;
	
	private String conditionValue;

	private Integer exportRackageInstructionsElementId;
	
	private String taxReturnValue;
	
	private String taxValue;
	
	private Integer item;
	
	private Integer csn;
	
	private String orderDescription;
	
	private String orderDetail;
	
	private Double exportAmount;
	
	private Double orderPrice;
	
	private String clientOrderCurrencyValue;
	
	private Double orderTotal;
	
	private Double supplierOrderTotal;
	
	private String supplierOrderCurrencyValue;
	
	public Integer getId() {
		return id;
	}

	public Double getExportPackageInstructionsAmount() {
		return exportPackageInstructionsAmount;
	}

	public void setExportPackageInstructionsAmount(Double exportPackageInstructionsAmount) {
		this.exportPackageInstructionsAmount = exportPackageInstructionsAmount;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getClientOrderNumber() {
		return clientOrderNumber;
	}

	public void setClientOrderNumber(String clientOrderNumber) {
		this.clientOrderNumber = clientOrderNumber;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getImportAmount() {
		return importAmount;
	}

	public String getCompleteComplianceCertificateValue() {
		return completeComplianceCertificateValue;
	}

	public void setCompleteComplianceCertificateValue(String completeComplianceCertificateValue) {
		this.completeComplianceCertificateValue = completeComplianceCertificateValue;
	}

	public String getComplianceCertificateValue() {
		return complianceCertificateValue;
	}

	public Double getCountWeight() {
		return countWeight;
	}

	public void setCountWeight(Double countWeight) {
		this.countWeight = countWeight;
	}

	public void setComplianceCertificateValue(String complianceCertificateValue) {
		this.complianceCertificateValue = complianceCertificateValue;
	}

	public Integer getCompleteComplianceCertificate() {
		return completeComplianceCertificate;
	}

	public Double getBoxWeight() {
		return boxWeight;
	}

	public void setBoxWeight(Double boxWeight) {
		this.boxWeight = boxWeight;
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

	public void setImportAmount(Double importAmount) {
		this.importAmount = importAmount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getImportPrice() {
		return importPrice;
	}

	public void setImportPrice(Double importPrice) {
		this.importPrice = importPrice;
	}

	public String getSourceOrderNumber() {
		return sourceOrderNumber;
	}

	public void setSourceOrderNumber(String sourceOrderNumber) {
		this.sourceOrderNumber = sourceOrderNumber;
	}

	public Double getPrepayRate() {
		return prepayRate;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
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

	public Integer getShipPayPeriod() {
		return shipPayPeriod;
	}

	public void setShipPayPeriod(Integer shipPayPeriod) {
		this.shipPayPeriod = shipPayPeriod;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Double getStorageAmount() {
		return storageAmount;
	}

	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
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

	public Double getImportBasePrice() {
		return importBasePrice;
	}

	public void setImportBasePrice(Double importBasePrice) {
		this.importBasePrice = importBasePrice;
	}

	public Integer getImportPackageElementId() {
		return importPackageElementId;
	}

	public void setImportPackageElementId(Integer importPackageElementId) {
		this.importPackageElementId = importPackageElementId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public Integer getExportRackageInstructionsElementId() {
		return exportRackageInstructionsElementId;
	}

	public void setExportRackageInstructionsElementId(
			Integer exportRackageInstructionsElementId) {
		this.exportRackageInstructionsElementId = exportRackageInstructionsElementId;
	}

	public String getTaxReturnValue() {
		return taxReturnValue;
	}

	public void setTaxReturnValue(String taxReturnValue) {
		this.taxReturnValue = taxReturnValue;
	}

	public String getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(String taxValue) {
		this.taxValue = taxValue;
	}

	public Integer getItem() {
		return item;
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

	/**
	 * @return the orderDescription
	 */
	public String getOrderDescription() {
		return orderDescription;
	}

	/**
	 * @param orderDescription the orderDescription to set
	 */
	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	/**
	 * @return the orderDetail
	 */
	public String getOrderDetail() {
		return orderDetail;
	}

	/**
	 * @param orderDetail the orderDetail to set
	 */
	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	/**
	 * @return the exportAmount
	 */
	public Double getExportAmount() {
		return exportAmount;
	}

	/**
	 * @param exportAmount the exportAmount to set
	 */
	public void setExportAmount(Double exportAmount) {
		this.exportAmount = exportAmount;
	}

	/**
	 * @return the orderPrice
	 */
	public Double getOrderPrice() {
		return orderPrice;
	}

	/**
	 * @param orderPrice the orderPrice to set
	 */
	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	/**
	 * @return the clientOrderCurrencyValue
	 */
	public String getClientOrderCurrencyValue() {
		return clientOrderCurrencyValue;
	}

	/**
	 * @param clientOrderCurrencyValue the clientOrderCurrencyValue to set
	 */
	public void setClientOrderCurrencyValue(String clientOrderCurrencyValue) {
		this.clientOrderCurrencyValue = clientOrderCurrencyValue;
	}

	/**
	 * @return the orderTotal
	 */
	public Double getOrderTotal() {
		return orderTotal;
	}

	/**
	 * @param orderTotal the orderTotal to set
	 */
	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	/**
	 * @return the supplierOrderTotal
	 */
	public Double getSupplierOrderTotal() {
		return supplierOrderTotal;
	}

	/**
	 * @param supplierOrderTotal the supplierOrderTotal to set
	 */
	public void setSupplierOrderTotal(Double supplierOrderTotal) {
		this.supplierOrderTotal = supplierOrderTotal;
	}

	/**
	 * @return the supplierOrderCurrencyValue
	 */
	public String getSupplierOrderCurrencyValue() {
		return supplierOrderCurrencyValue;
	}

	/**
	 * @param supplierOrderCurrencyValue the supplierOrderCurrencyValue to set
	 */
	public void setSupplierOrderCurrencyValue(String supplierOrderCurrencyValue) {
		this.supplierOrderCurrencyValue = supplierOrderCurrencyValue;
	}
	
	
	
}
