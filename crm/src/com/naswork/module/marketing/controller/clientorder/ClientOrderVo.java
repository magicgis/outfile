package com.naswork.module.marketing.controller.clientorder;

import java.util.Date;
import java.util.List;

import com.naswork.model.ClientOrderElement;

public class ClientOrderVo {
	private Integer complete;
	
	private String userName;
	
	private Integer spzt;
	
	private Double profitMargin;

	private Integer id;
	
	private Double prepayRate;
	
	private Double shipPayRate;
	
	private Integer shipPayPeriod;
	
	private Double receivePayRate;
	
	private Integer receivePayPeriod;
	
	private Integer clientQuoteId;
	
	private Integer clientInquiryId;
	
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
	
	private String exchangeRate;
	
	private Integer bizTypeId;
	
	private String bizTypeCode;
	
	private String bizTypeValue;
	
	private Integer airTypeId;
	
	private String airTypeCode;
	
	private String airTypeValue;
	
	private Integer orderStatusId;
	
	private String orderStatusCode;
	
	private String orderStatusValue;
	
	private Integer seq;
	
	private String quoteNumber;
	
	private Date orderDate;
	
	private String sourceOrderNumber;
	
	private String orderNumber;
	
	private Integer terms;
	
	private String terms_1;
	
	private String remark;
	
	private Date updateTimestamp;
	
	private String contactName;
	
	private Integer inquiryItem;
	
	private Integer InquiryCsn;
	
	private String inquiryPartNumber;
	
	private Integer inquiryElementId;
	
	private String inquiryDescription;
	
	private String inquiryUnit;
	
	private Double OrderAmount;
	
    private Double freight;
    
    private Double lowestFreight;
    
    private String lc;

    private String importersRegistration;
    
	private List<ClientOrderElement> list;
	
	private Integer purchaseApply;
	
	private String exportAmountPercent;
	
	private String exportTotalPercent;
	
	private String certification;
	
	private Double total;
	
	private Integer urgentLevelId;
	
	private String urgentLevelValue;
	
	private Double incomeTotal;
	
	private Double exportTotal;
	
	private String exportDates;

	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	public String getCertification() {
		return certification;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCertification(String certification) {
		this.certification = certification;
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

	public Integer getSpzt() {
		return spzt;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
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

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getBizTypeCode() {
		return bizTypeCode;
	}

	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	public String getBizTypeValue() {
		return bizTypeValue;
	}

	public void setBizTypeValue(String bizTypeValue) {
		this.bizTypeValue = bizTypeValue;
	}

	public String getAirTypeCode() {
		return airTypeCode;
	}

	public void setAirTypeCode(String airTypeCode) {
		this.airTypeCode = airTypeCode;
	}

	public String getAirTypeValue() {
		return airTypeValue;
	}

	public void setAirTypeValue(String airTypeValue) {
		this.airTypeValue = airTypeValue;
	}

	public String getOrderStatusCode() {
		return orderStatusCode;
	}

	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}

	public String getOrderStatusValue() {
		return orderStatusValue;
	}

	public void setOrderStatusValue(String orderStatusValue) {
		this.orderStatusValue = orderStatusValue;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
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


	public String getTerms_1() {
		return terms_1;
	}

	public void setTerms_1(String terms_1) {
		this.terms_1 = terms_1;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientQuoteId() {
		return clientQuoteId;
	}

	public void setClientQuoteId(Integer clientQuoteId) {
		this.clientQuoteId = clientQuoteId;
	}

	public Integer getClientInquiryId() {
		return clientInquiryId;
	}

	public void setClientInquiryId(Integer clientInquiryId) {
		this.clientInquiryId = clientInquiryId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(Integer bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public Integer getAirTypeId() {
		return airTypeId;
	}

	public void setAirTypeId(Integer airTypeId) {
		this.airTypeId = airTypeId;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getTerms() {
		return terms;
	}

	public void setTerms(Integer terms) {
		this.terms = terms;
	}

	public List<ClientOrderElement> getList() {
		return list;
	}

	public void setList(List<ClientOrderElement> list) {
		this.list = list;
	}

	public Double getPrepayRate() {
		return prepayRate;
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Integer getInquiryItem() {
		return inquiryItem;
	}

	public void setInquiryItem(Integer inquiryItem) {
		this.inquiryItem = inquiryItem;
	}

	public Integer getInquiryCsn() {
		return InquiryCsn;
	}

	public void setInquiryCsn(Integer inquiryCsn) {
		InquiryCsn = inquiryCsn;
	}

	public String getInquiryPartNumber() {
		return inquiryPartNumber;
	}

	public void setInquiryPartNumber(String inquiryPartNumber) {
		this.inquiryPartNumber = inquiryPartNumber;
	}

	public Integer getInquiryElementId() {
		return inquiryElementId;
	}

	public void setInquiryElementId(Integer inquiryElementId) {
		this.inquiryElementId = inquiryElementId;
	}

	public String getInquiryDescription() {
		return inquiryDescription;
	}

	public void setInquiryDescription(String inquiryDescription) {
		this.inquiryDescription = inquiryDescription;
	}

	public String getInquiryUnit() {
		return inquiryUnit;
	}

	public void setInquiryUnit(String inquiryUnit) {
		this.inquiryUnit = inquiryUnit;
	}

	public Double getOrderAmount() {
		return OrderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		OrderAmount = orderAmount;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getLowestFreight() {
		return lowestFreight;
	}

	public void setLowestFreight(Double lowestFreight) {
		this.lowestFreight = lowestFreight;
	}

	public String getLc() {
		return lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	public String getImportersRegistration() {
		return importersRegistration;
	}

	public void setImportersRegistration(String importersRegistration) {
		this.importersRegistration = importersRegistration;
	}

	public Integer getPurchaseApply() {
		return purchaseApply;
	}

	public void setPurchaseApply(Integer purchaseApply) {
		this.purchaseApply = purchaseApply;
	}

	public String getExportTotalPercent() {
		return exportTotalPercent;
	}

	public void setExportTotalPercent(String exportTotalPercent) {
		this.exportTotalPercent = exportTotalPercent;
	}

	public String getExportAmountPercent() {
		return exportAmountPercent;
	}

	public void setExportAmountPercent(String exportAmountPercent) {
		this.exportAmountPercent = exportAmountPercent;
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
	 * @return the exportTotal
	 */
	public Double getExportTotal() {
		return exportTotal;
	}

	/**
	 * @param exportTotal the exportTotal to set
	 */
	public void setExportTotal(Double exportTotal) {
		this.exportTotal = exportTotal;
	}

	/**
	 * @return the exportDates
	 */
	public String getExportDates() {
		return exportDates;
	}

	/**
	 * @param exportDates the exportDates to set
	 */
	public void setExportDates(String exportDates) {
		this.exportDates = exportDates;
	}
	
	
}
