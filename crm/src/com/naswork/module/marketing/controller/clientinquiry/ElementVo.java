package com.naswork.module.marketing.controller.clientinquiry;

import java.util.Date;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Data;
import com.naswork.model.ClientInquiryElement;

public class ElementVo {

	private Integer id;
	
	private Integer isMain;
	
	private Integer isBlacklist;
	
	private String mainPartNumber;
	
	private Double profitMargin;
	
	private String quoteNumber;
	
	private Integer clientInquiryId;
	
	private Integer item;
	
	private String partNumber;
	
	private String alterPartNumber;
	
	private String partNumberCode;
	
	private String description;
	
	private String unit;
	
	private Integer amount;
	
	private String remark;
	
	private Integer elementId;
	
	private Integer csn;
	
	private Date updateTimestamp;
	
	private String supplierCode;
	
	private String typeCode;
	
	private Double fixedCost;
	
	private String bsn;
	
	private Double historyPrice;
	
	private Integer historyYear;
	
	private String elementStatusValue;
	
	private String cageCode;
	
	private String conditionValue;
	
	private Integer conditionId;
	
	private String conditionCode;
	
	private Double bankCost;
	
	private String source;
	
	private String aimPrice;
	
	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	private List<ClientInquiryElement> list;
	
	
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartNumberCode() {
		return partNumberCode;
	}

	public void setPartNumberCode(String partNumberCode) {
		this.partNumberCode = partNumberCode;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
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

	public Integer getClientInquiryId() {
		return clientInquiryId;
	}

	public void setClientInquiryId(Integer clientInquiryId) {
		this.clientInquiryId = clientInquiryId;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getElementId() {
		return elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	public List<ClientInquiryElement> getList() {
		return list;
	}

	public void setList(List<ClientInquiryElement> list) {
		this.list = list;
	}

	public String getAlterPartNumber() {
		return alterPartNumber;
	}

	public void setAlterPartNumber(String alterPartNumber) {
		this.alterPartNumber = alterPartNumber;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}

	public String getMainPartNumber() {
		return mainPartNumber;
	}

	public void setMainPartNumber(String mainPartNumber) {
		this.mainPartNumber = mainPartNumber;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	public Double getHistoryPrice() {
		return historyPrice;
	}

	public void setHistoryPrice(Double historyPrice) {
		this.historyPrice = historyPrice;
	}

	public Integer getHistoryYear() {
		return historyYear;
	}

	public void setHistoryYear(Integer historyYear) {
		this.historyYear = historyYear;
	}

	public String getElementStatusValue() {
		return elementStatusValue;
	}

	public void setElementStatusValue(String elementStatusValue) {
		this.elementStatusValue = elementStatusValue;
	}

	public String getCageCode() {
		return cageCode;
	}

	public void setCageCode(String cageCode) {
		this.cageCode = cageCode;
	}

	/**
	 * @return the conditionValue
	 */
	public String getConditionValue() {
		return conditionValue;
	}

	/**
	 * @param conditionValue the conditionValue to set
	 */
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	/**
	 * @return the conditionId
	 */
	public Integer getConditionId() {
		return conditionId;
	}

	/**
	 * @param conditionId the conditionId to set
	 */
	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	/**
	 * @return the conditionCode
	 */
	public String getConditionCode() {
		return conditionCode;
	}

	/**
	 * @param conditionCode the conditionCode to set
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the aimPrice
	 */
	public String getAimPrice() {
		return aimPrice;
	}

	/**
	 * @param aimPrice the aimPrice to set
	 */
	public void setAimPrice(String aimPrice) {
		this.aimPrice = aimPrice;
	}

		
}
