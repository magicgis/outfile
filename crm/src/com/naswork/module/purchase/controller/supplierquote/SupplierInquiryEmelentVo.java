package com.naswork.module.purchase.controller.supplierquote;

import java.util.Date;
import java.util.List;

import com.naswork.model.SupplierQuoteElement;

public class SupplierInquiryEmelentVo {
	
		private Date validity;
		
		private Integer csn;
	
		private Integer id;
	
		private Integer supplierInquiryId;

	    private Integer clientInquiryElementId;

	    private Date updateTimestamp;
	    
	    private String supplierInquiryQuoteNumber;
	    
	    private Integer elementId;

	    private String partNumber;

	    private Integer item;

	    private String description;

	    private String remark;

	    private String unit;

	    private Double amount;
	    
	    private Integer supplierQuoteId;

	    private Integer supplierInquiryElementId;
	    
	    private String alterPartNumber;

	    private Double price;

	    private String leadTime;

	    private Integer conditionId;

	    private Integer certificationId;

	    private Integer supplierQuoteStatusId;

	    private List<SupplierQuoteElement> list;
	    
	    private String tPartRemark;
	    
	    private String feeForExchangeBill;
	    
	    private Double bankCost;
	    
		public Integer getId() {
			return id;
		}

		public Date getValidity() {
			return validity;
		}

		public void setValidity(Date validity) {
			this.validity = validity;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getSupplierInquiryId() {
			return supplierInquiryId;
		}

		public void setSupplierInquiryId(Integer supplierInquiryId) {
			this.supplierInquiryId = supplierInquiryId;
		}

		public Integer getClientInquiryElementId() {
			return clientInquiryElementId;
		}

		public void setClientInquiryElementId(Integer clientInquiryElementId) {
			this.clientInquiryElementId = clientInquiryElementId;
		}

		public Date getUpdateTimestamp() {
			return updateTimestamp;
		}

		public Integer getCsn() {
			return csn;
		}

		public void setCsn(Integer csn) {
			this.csn = csn;
		}

		public void setUpdateTimestamp(Date updateTimestamp) {
			this.updateTimestamp = updateTimestamp;
		}

		public String getSupplierInquiryQuoteNumber() {
			return supplierInquiryQuoteNumber;
		}

		public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
			this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
		}

		public Integer getElementId() {
			return elementId;
		}

		public void setElementId(Integer elementId) {
			this.elementId = elementId;
		}

		public String getPartNumber() {
			return partNumber;
		}

		public void setPartNumber(String partNumber) {
			this.partNumber = partNumber;
		}

		public Integer getItem() {
			return item;
		}

		public void setItem(Integer item) {
			this.item = item;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getSupplierQuoteId() {
			return supplierQuoteId;
		}

		public void setSupplierQuoteId(Integer supplierQuoteId) {
			this.supplierQuoteId = supplierQuoteId;
		}

		public Integer getSupplierInquiryElementId() {
			return supplierInquiryElementId;
		}

		public void setSupplierInquiryElementId(Integer supplierInquiryElementId) {
			this.supplierInquiryElementId = supplierInquiryElementId;
		}

		public String getAlterPartNumber() {
			return alterPartNumber;
		}

		public void setAlterPartNumber(String alterPartNumber) {
			this.alterPartNumber = alterPartNumber;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public String getLeadTime() {
			return leadTime;
		}

		public void setLeadTime(String leadTime) {
			this.leadTime = leadTime;
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

		public Integer getSupplierQuoteStatusId() {
			return supplierQuoteStatusId;
		}

		public void setSupplierQuoteStatusId(Integer supplierQuoteStatusId) {
			this.supplierQuoteStatusId = supplierQuoteStatusId;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getUnit() {
			return unit;
		}

		public List<SupplierQuoteElement> getList() {
			return list;
		}

		public void setList(List<SupplierQuoteElement> list) {
			this.list = list;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		/**
		 * @return the tPartRemark
		 */
		public String gettPartRemark() {
			return tPartRemark;
		}

		/**
		 * @param tPartRemark the tPartRemark to set
		 */
		public void settPartRemark(String tPartRemark) {
			this.tPartRemark = tPartRemark;
		}

		/**
		 * @return the feeForExchangeBill
		 */
		public String getFeeForExchangeBill() {
			return feeForExchangeBill;
		}

		/**
		 * @param feeForExchangeBill the feeForExchangeBill to set
		 */
		public void setFeeForExchangeBill(String feeForExchangeBill) {
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

}
