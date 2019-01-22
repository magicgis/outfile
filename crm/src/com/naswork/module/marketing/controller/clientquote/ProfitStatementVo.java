package com.naswork.module.marketing.controller.clientquote;

import java.util.Date;

public class ProfitStatementVo {
	private Double totalProfitMargin;
	private Double counterFee;
	private Double bankCharges;
	private Integer clientOrderId;
	private Integer clientQuoteId;
	private Date paymentDate;
	private Double paymentSum;
	private String clientCode;
	private String orderNumber;
	private String supplierOrderNumber;
	private Date orderDate;
	private String partNumber;
	private String description;
	private String airType;
	private Double total;
	private Date receiveDate;
	private Double supplierOrderAmount;
	private Double supplierOrderPrice;
	private Double importAmount;
	private String cqCurrencyValue;
	private String coCurrencyValue;
	private String sqCurrencyValue;
	private String soCurrencyValue;
	private Integer currencyId;
	private Double coExchangeRate;
	private Double soExchangeRate;
	private Double cqExchangeRate;
	private Double sqExchangeRate;
	private Double freight;
	private Integer supplierQuoteElementId;
	private Integer id;
	private Integer clientInquiryElementId;
	private Integer item;
	private Integer supplierId;
	private Integer clientOrderElementId;
	private Double quoteAmount;
	private Double orderAmount;
	private Double storageAmount;
	private Double baseStoragePrice;
	private String inquiryPartNumber;
	private String alterPartNumber;
	private String inquiryDescription;
	private String supplierCode;
	private String supplierName;
	private Double quoteTotalPrice;
	private Double clientQuoteBasePrice;
	private Double orderBasePrice;
	private Double clientQuoteTotalPrice;
	private Double quoteBasePrice;
	private Double profitMargin;
	private Double fixedCost;
	private Double importFee;
	private Double importFreight;
	private Double exportFee;
	private Double exportFreight;
	private Double hazFee;
	private Double bankCost;
	private Double profitOtherFee;
	private Double supplierOrderElementPrice;
	private Integer supplierOrderCurrencyId;
	private Integer clientQuoteElementId;
	private Double supplierQuotePrice;
	private Double relativeRate;
	private Double feeForExchangeBill;
	private Double otherFee;
	private Double hazmatFee;
	private Double moq;
	
	public Double getTotalProfitMargin() {
		return totalProfitMargin;
	}
	public void setTotalProfitMargin(Double totalProfitMargin) {
		this.totalProfitMargin = totalProfitMargin;
	}
	public String getCoCurrencyValue() {
		return coCurrencyValue;
	}
	public Double getCounterFee() {
		return counterFee;
	}
	public void setCounterFee(Double counterFee) {
		this.counterFee = counterFee;
	}
	public Double getBankCharges() {
		return bankCharges;
	}
	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Double getPaymentSum() {
		return paymentSum;
	}
	public Integer getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(Integer clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public void setPaymentSum(Double paymentSum) {
		this.paymentSum = paymentSum;
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
	public String getSoCurrencyValue() {
		return soCurrencyValue;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	public String getAirType() {
		return airType;
	}
	public void setAirType(String airType) {
		this.airType = airType;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public void setSoCurrencyValue(String soCurrencyValue) {
		this.soCurrencyValue = soCurrencyValue;
	}
	public Double getSoExchangeRate() {
		return soExchangeRate;
	}
	public void setSoExchangeRate(Double soExchangeRate) {
		this.soExchangeRate = soExchangeRate;
	}
	public Double getImportAmount() {
		return importAmount;
	}
	public void setImportAmount(Double importAmount) {
		this.importAmount = importAmount;
	}
	public String getCqCurrencyValue() {
		return cqCurrencyValue;
	}
	public void setCqCurrencyValue(String cqCurrencyValue) {
		this.cqCurrencyValue = cqCurrencyValue;
	}
	public void setCoCurrencyValue(String coCurrencyValue) {
		this.coCurrencyValue = coCurrencyValue;
	}
	public String getSqCurrencyValue() {
		return sqCurrencyValue;
	}
	public Integer getClientQuoteId() {
		return clientQuoteId;
	}
	public void setClientQuoteId(Integer clientQuoteId) {
		this.clientQuoteId = clientQuoteId;
	}
	public void setSqCurrencyValue(String sqCurrencyValue) {
		this.sqCurrencyValue = sqCurrencyValue;
	}
	private Double orderTotalPrice;
	public Double getCqExchangeRate() {
		return cqExchangeRate;
	}
	public void setCqExchangeRate(Double cqExchangeRate) {
		this.cqExchangeRate = cqExchangeRate;
	}
	public Double getCoExchangeRate() {
		return coExchangeRate;
	}
	public void setCoExchangeRate(Double coExchangeRate) {
		this.coExchangeRate = coExchangeRate;
	}
	public Double getSqExchangeRate() {
		return sqExchangeRate;
	}
	public void setSqExchangeRate(Double sqExchangeRate) {
		this.sqExchangeRate = sqExchangeRate;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public Double getProfitMargin() {
		return profitMargin;
	}
	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}
	public Double getQuoteBasePrice() {
		return quoteBasePrice;
	}
	public void setQuoteBasePrice(Double quoteBasePrice) {
		this.quoteBasePrice = quoteBasePrice;
	}
	public Double getQuoteTotalPrice() {
		return quoteTotalPrice;
	}
	public void setQuoteTotalPrice(Double quoteTotalPrice) {
		this.quoteTotalPrice = quoteTotalPrice;
	}
	public Double getClientQuoteBasePrice() {
		return clientQuoteBasePrice;
	}
	public void setClientQuoteBasePrice(Double clientQuoteBasePrice) {
		this.clientQuoteBasePrice = clientQuoteBasePrice;
	}
	public Double getOrderBasePrice() {
		return orderBasePrice;
	}
	public void setOrderBasePrice(Double orderBasePrice) {
		this.orderBasePrice = orderBasePrice;
	}
	public Double getClientQuoteTotalPrice() {
		return clientQuoteTotalPrice;
	}
	public void setClientQuoteTotalPrice(Double clientQuoteTotalPrice) {
		this.clientQuoteTotalPrice = clientQuoteTotalPrice;
	}
	public Integer getId() {
		return id;
	}
	public String getAlterPartNumber() {
		return alterPartNumber;
	}
	public void setAlterPartNumber(String alterPartNumber) {
		this.alterPartNumber = alterPartNumber;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getClientInquiryElementId() {
		return clientInquiryElementId;
	}
	public void setClientInquiryElementId(Integer clientInquiryElementId) {
		this.clientInquiryElementId = clientInquiryElementId;
	}
	public Integer getItem() {
		return item;
	}
	public void setItem(Integer item) {
		this.item = item;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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
	public Double getQuoteAmount() {
		return quoteAmount;
	}
	public void setQuoteAmount(Double quoteAmount) {
		this.quoteAmount = quoteAmount;
	}
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Double getStorageAmount() {
		return storageAmount;
	}
	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}
	public Double getBaseStoragePrice() {
		return baseStoragePrice;
	}
	public void setBaseStoragePrice(Double baseStoragePrice) {
		this.baseStoragePrice = baseStoragePrice;
	}
	public String getInquiryPartNumber() {
		return inquiryPartNumber;
	}
	public void setInquiryPartNumber(String inquiryPartNumber) {
		this.inquiryPartNumber = inquiryPartNumber;
	}
	public String getInquiryDescription() {
		return inquiryDescription;
	}
	public void setInquiryDescription(String inquiryDescription) {
		this.inquiryDescription = inquiryDescription;
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
	public Double getFixedCost() {
		return fixedCost;
	}
	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}
	/**
	 * @return the importFee
	 */
	public Double getImportFee() {
		return importFee;
	}
	/**
	 * @param importFee the importFee to set
	 */
	public void setImportFee(Double importFee) {
		this.importFee = importFee;
	}
	/**
	 * @return the importFreight
	 */
	public Double getImportFreight() {
		return importFreight;
	}
	/**
	 * @param importFreight the importFreight to set
	 */
	public void setImportFreight(Double importFreight) {
		this.importFreight = importFreight;
	}
	/**
	 * @return the exportFee
	 */
	public Double getExportFee() {
		return exportFee;
	}
	/**
	 * @param exportFee the exportFee to set
	 */
	public void setExportFee(Double exportFee) {
		this.exportFee = exportFee;
	}
	/**
	 * @return the exportFreight
	 */
	public Double getExportFreight() {
		return exportFreight;
	}
	/**
	 * @param exportFreight the exportFreight to set
	 */
	public void setExportFreight(Double exportFreight) {
		this.exportFreight = exportFreight;
	}
	/**
	 * @return the hazFee
	 */
	public Double getHazFee() {
		return hazFee;
	}
	/**
	 * @param hazFee the hazFee to set
	 */
	public void setHazFee(Double hazFee) {
		this.hazFee = hazFee;
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
	 * @return the profitOtherFee
	 */
	public Double getProfitOtherFee() {
		return profitOtherFee;
	}
	/**
	 * @param profitOtherFee the profitOtherFee to set
	 */
	public void setProfitOtherFee(Double profitOtherFee) {
		this.profitOtherFee = profitOtherFee;
	}
	/**
	 * @return the supplierOrderElementPrice
	 */
	public Double getSupplierOrderElementPrice() {
		return supplierOrderElementPrice;
	}
	/**
	 * @param supplierOrderElementPrice the supplierOrderElementPrice to set
	 */
	public void setSupplierOrderElementPrice(Double supplierOrderElementPrice) {
		this.supplierOrderElementPrice = supplierOrderElementPrice;
	}
	/**
	 * @return the supplierOrderCurrencyId
	 */
	public Integer getSupplierOrderCurrencyId() {
		return supplierOrderCurrencyId;
	}
	/**
	 * @param supplierOrderCurrencyId the supplierOrderCurrencyId to set
	 */
	public void setSupplierOrderCurrencyId(Integer supplierOrderCurrencyId) {
		this.supplierOrderCurrencyId = supplierOrderCurrencyId;
	}
	/**
	 * @return the clientQuoteElementId
	 */
	public Integer getClientQuoteElementId() {
		return clientQuoteElementId;
	}
	/**
	 * @param clientQuoteElementId the clientQuoteElementId to set
	 */
	public void setClientQuoteElementId(Integer clientQuoteElementId) {
		this.clientQuoteElementId = clientQuoteElementId;
	}
	/**
	 * @return the supplierQuotePrice
	 */
	public Double getSupplierQuotePrice() {
		return supplierQuotePrice;
	}
	/**
	 * @param supplierQuotePrice the supplierQuotePrice to set
	 */
	public void setSupplierQuotePrice(Double supplierQuotePrice) {
		this.supplierQuotePrice = supplierQuotePrice;
	}
	/**
	 * @return the relativeRate
	 */
	public Double getRelativeRate() {
		return relativeRate;
	}
	/**
	 * @param relativeRate the relativeRate to set
	 */
	public void setRelativeRate(Double relativeRate) {
		this.relativeRate = relativeRate;
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
	 * @return the moq
	 */
	public Double getMoq() {
		return moq;
	}
	/**
	 * @param moq the moq to set
	 */
	public void setMoq(Double moq) {
		this.moq = moq;
	}
	
}
