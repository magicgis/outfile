package com.naswork.module.purchase.controller.supplierquote;

import java.util.Date;

public class SupplierQuoteVo {
		private Double counterFee;
	
		private String tagDate;
		
		private String warranty;
		
		private String serialNumber;
		
		private String tagSrc;
		
		private String trace;
	
		private Date validity;
	
		private Integer isMain;
	
		private String mainPartNumber;
	
		private Integer moq;
	
		private String location;
	
		private String quoteStatusValue;
	
		private String conditionValue;
		
		private String certificationValue;
		
		private String supplierQuoteStatusCode;
		
		private Integer supplierQuoteStatusId;
		
		private Integer certificationId;
		
		private Integer conditionId;
	
		private String quotePartNumber;
		
		private String quoteAlterPartNumber;
		
		private String quoteDescription;
		
		private Double quoteAmount;
		
		private String quoteUnit;
		
		private Double price;
		
		private String leadTime;
		
		private String conditionCode;
		
		private String certificationCode;
		
		private String supplierQuoteStatusValue;
		
		private String quoteRemark;
	
		private Integer item;
		
		private Integer csn;

	 	private Integer supplierId;

	    private Integer clientInquiryId;
	    
	    private Integer cieClientInquiryId;

	    private String supplierInquiryQuoteNumber;

	    private Date supplierInquiryDate;

	    private Date supplierDeadline;

	    private String remark;

	    private Date updateTimestamp;
	    
	    private Integer id;

	    private Integer supplierInquiryId;

	    private Integer currencyId;

	    private Double exchangeRate;

	    private Date quoteDate;
	    
	    private String supplierCode;

	    private String supplierName;
	    
	    private String supplierFax;
	    
	    private String supplierContactName;
	    
	    private String clientInquiryQuoteNumber;
	    
	    private Integer inquiryStatusId;
	    
	    private Integer clientId;
	    
	    private Integer airTypeId;
	    
	    private String clientCode;
	    
	    private String airTypeCode;
	    
	    private String currencyCode;
	    
	    private String currencyValue;
	    
	    private Double rate;
	    
	    private String supplierQuoteQuoteNumber;
	    
	    private Double latestPrice;
	    
	    private Integer haveAttachment;
	    
	    private String sourceNumber;
	    
	    private Double availableQty;
	    
	    private String coreCharge;
	    
	    private String feeForExchangeBill;
	    
	    private Double bankCost;
	    
	    private Double hazmatFee;
	    
	    private String quoteFeeForExchangeBill;
	    
	    private String quoteBankCost;
	    
	    private String quoteHazmatFee;
	    
	    private Double otherFee;
	    
	    private String quoteOtherFee;
	    
	    private String userName;

		public Date getValidity() {
			return validity;
		}

		public Double getCounterFee() {
			return counterFee;
		}

		public void setCounterFee(Double counterFee) {
			this.counterFee = counterFee;
		}

		public void setValidity(Date validity) {
			this.validity = validity;
		}

		public Integer getSupplierId() {
			return supplierId;
		}


		public String getTagDate() {
			return tagDate;
		}

		public void setTagDate(String tagDate) {
			this.tagDate = tagDate;
		}

		public String getWarranty() {
			return warranty;
		}

		public void setWarranty(String warranty) {
			this.warranty = warranty;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getTagSrc() {
			return tagSrc;
		}

		public void setTagSrc(String tagSrc) {
			this.tagSrc = tagSrc;
		}

		public String getTrace() {
			return trace;
		}

		public void setTrace(String trace) {
			this.trace = trace;
		}

		public Integer getCsn() {
			return csn;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public void setCsn(Integer csn) {
			this.csn = csn;
		}

		public String getConditionValue() {
			return conditionValue;
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

		public void setConditionValue(String conditionValue) {
			this.conditionValue = conditionValue;
		}

		public String getCertificationValue() {
			return certificationValue;
		}

		public void setCertificationValue(String certificationValue) {
			this.certificationValue = certificationValue;
		}

		public String getQuoteStatusValue() {
			return quoteStatusValue;
		}

		public void setQuoteStatusValue(String quoteStatusValue) {
			this.quoteStatusValue = quoteStatusValue;
		}

		public String getSupplierQuoteStatusCode() {
			return supplierQuoteStatusCode;
		}

		public void setSupplierQuoteStatusCode(String supplierQuoteStatusCode) {
			this.supplierQuoteStatusCode = supplierQuoteStatusCode;
		}

		public Integer getSupplierQuoteStatusId() {
			return supplierQuoteStatusId;
		}

		public void setSupplierQuoteStatusId(Integer supplierQuoteStatusId) {
			this.supplierQuoteStatusId = supplierQuoteStatusId;
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

		public String getQuoteAlterPartNumber() {
			return quoteAlterPartNumber;
		}

		public void setQuoteAlterPartNumber(String quoteAlterPartNumber) {
			this.quoteAlterPartNumber = quoteAlterPartNumber;
		}

		public void setConditionId(Integer conditionId) {
			this.conditionId = conditionId;
		}

		public String getQuotePartNumber() {
			return quotePartNumber;
		}

		public void setQuotePartNumber(String quotePartNumber) {
			this.quotePartNumber = quotePartNumber;
		}

		public String getQuoteDescription() {
			return quoteDescription;
		}

		public void setQuoteDescription(String quoteDescription) {
			this.quoteDescription = quoteDescription;
		}

		public Double getQuoteAmount() {
			return quoteAmount;
		}

		public void setQuoteAmount(Double quoteAmount) {
			this.quoteAmount = quoteAmount;
		}

		public String getQuoteUnit() {
			return quoteUnit;
		}

		public void setQuoteUnit(String quoteUnit) {
			this.quoteUnit = quoteUnit;
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

		public String getSupplierQuoteStatusValue() {
			return supplierQuoteStatusValue;
		}

		public void setSupplierQuoteStatusValue(String supplierQuoteStatusValue) {
			this.supplierQuoteStatusValue = supplierQuoteStatusValue;
		}

		public String getQuoteRemark() {
			return quoteRemark;
		}

		public void setQuoteRemark(String quoteRemark) {
			this.quoteRemark = quoteRemark;
		}

		public Integer getItem() {
			return item;
		}

		public void setItem(Integer item) {
			this.item = item;
		}

		public void setSupplierId(Integer supplierId) {
			this.supplierId = supplierId;
		}

		public Integer getClientInquiryId() {
			return clientInquiryId;
		}

		public Integer getCieClientInquiryId() {
			return cieClientInquiryId;
		}

		public void setCieClientInquiryId(Integer cieClientInquiryId) {
			this.cieClientInquiryId = cieClientInquiryId;
		}

		public void setClientInquiryId(Integer clientInquiryId) {
			this.clientInquiryId = clientInquiryId;
		}




		public String getSupplierInquiryQuoteNumber() {
			return supplierInquiryQuoteNumber;
		}

		public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
			this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
		}

		public Date getSupplierInquiryDate() {
			return supplierInquiryDate;
		}

		public void setSupplierInquiryDate(Date supplierInquiryDate) {
			this.supplierInquiryDate = supplierInquiryDate;
		}

		public Date getSupplierDeadline() {
			return supplierDeadline;
		}

		public void setSupplierDeadline(Date supplierDeadline) {
			this.supplierDeadline = supplierDeadline;
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

		public Integer getSupplierInquiryId() {
			return supplierInquiryId;
		}

		public void setSupplierInquiryId(Integer supplierInquiryId) {
			this.supplierInquiryId = supplierInquiryId;
		}

		public Integer getCurrencyId() {
			return currencyId;
		}

		public void setCurrencyId(Integer currencyId) {
			this.currencyId = currencyId;
		}

		public Double getExchangeRate() {
			return exchangeRate;
		}

		public void setExchangeRate(Double exchangeRate) {
			this.exchangeRate = exchangeRate;
		}

		public Date getQuoteDate() {
			return quoteDate;
		}

		public void setQuoteDate(Date quoteDate) {
			this.quoteDate = quoteDate;
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

		public String getSupplierFax() {
			return supplierFax;
		}

		public void setSupplierFax(String supplierFax) {
			this.supplierFax = supplierFax;
		}

		public String getSupplierContactName() {
			return supplierContactName;
		}

		public void setSupplierContactName(String supplierContactName) {
			this.supplierContactName = supplierContactName;
		}

		public String getClientInquiryQuoteNumber() {
			return clientInquiryQuoteNumber;
		}

		public void setClientInquiryQuoteNumber(String clientInquiryQuoteNumber) {
			this.clientInquiryQuoteNumber = clientInquiryQuoteNumber;
		}

		public Integer getInquiryStatusId() {
			return inquiryStatusId;
		}

		public void setInquiryStatusId(Integer inquiryStatusId) {
			this.inquiryStatusId = inquiryStatusId;
		}

		public Integer getClientId() {
			return clientId;
		}

		public void setClientId(Integer clientId) {
			this.clientId = clientId;
		}

		public Integer getAirTypeId() {
			return airTypeId;
		}

		public void setAirTypeId(Integer airTypeId) {
			this.airTypeId = airTypeId;
		}

		public String getClientCode() {
			return clientCode;
		}

		public void setClientCode(String clientCode) {
			this.clientCode = clientCode;
		}

		public String getAirTypeCode() {
			return airTypeCode;
		}

		public void setAirTypeCode(String airTypeCode) {
			this.airTypeCode = airTypeCode;
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

		public Double getRate() {
			return rate;
		}

		public void setRate(Double rate) {
			this.rate = rate;
		}

		public String getSupplierQuoteQuoteNumber() {
			return supplierQuoteQuoteNumber;
		}

		public void setSupplierQuoteQuoteNumber(String supplierQuoteQuoteNumber) {
			this.supplierQuoteQuoteNumber = supplierQuoteQuoteNumber;
		}

		public Double getLatestPrice() {
			return latestPrice;
		}

		public void setLatestPrice(Double latestPrice) {
			this.latestPrice = latestPrice;
		}

		/**
		 * @return the haveAttachment
		 */
		public Integer getHaveAttachment() {
			return haveAttachment;
		}

		/**
		 * @param haveAttachment the haveAttachment to set
		 */
		public void setHaveAttachment(Integer haveAttachment) {
			this.haveAttachment = haveAttachment;
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
		 * @return the availableQty
		 */
		public Double getAvailableQty() {
			return availableQty;
		}

		/**
		 * @param availableQty the availableQty to set
		 */
		public void setAvailableQty(Double availableQty) {
			this.availableQty = availableQty;
		}

		/**
		 * @return the coreCharge
		 */
		public String getCoreCharge() {
			return coreCharge;
		}

		/**
		 * @param coreCharge the coreCharge to set
		 */
		public void setCoreCharge(String coreCharge) {
			this.coreCharge = coreCharge;
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
		public Integer getMoq() {
			return moq;
		}

		/**
		 * @param moq the moq to set
		 */
		public void setMoq(Integer moq) {
			this.moq = moq;
		}

		/**
		 * @return the quoteFeeForExchangeBill
		 */
		public String getQuoteFeeForExchangeBill() {
			return quoteFeeForExchangeBill;
		}

		/**
		 * @param quoteFeeForExchangeBill the quoteFeeForExchangeBill to set
		 */
		public void setQuoteFeeForExchangeBill(String quoteFeeForExchangeBill) {
			this.quoteFeeForExchangeBill = quoteFeeForExchangeBill;
		}

		/**
		 * @return the quoteBankCost
		 */
		public String getQuoteBankCost() {
			return quoteBankCost;
		}

		/**
		 * @param quoteBankCost the quoteBankCost to set
		 */
		public void setQuoteBankCost(String quoteBankCost) {
			this.quoteBankCost = quoteBankCost;
		}

		/**
		 * @return the quoteHazmatFee
		 */
		public String getQuoteHazmatFee() {
			return quoteHazmatFee;
		}

		/**
		 * @param quoteHazmatFee the quoteHazmatFee to set
		 */
		public void setQuoteHazmatFee(String quoteHazmatFee) {
			this.quoteHazmatFee = quoteHazmatFee;
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
		 * @return the quoteOtherFee
		 */
		public String getQuoteOtherFee() {
			return quoteOtherFee;
		}

		/**
		 * @param quoteOtherFee the quoteOtherFee to set
		 */
		public void setQuoteOtherFee(String quoteOtherFee) {
			this.quoteOtherFee = quoteOtherFee;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

}
