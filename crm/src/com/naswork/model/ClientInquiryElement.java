package com.naswork.model;

import java.util.Date;

public class ClientInquiryElement {
    private Integer id;

    private Integer clientInquiryId;

    private Integer elementId;

    private String partNumber;
    
    private String alterPartNumber;

    private Integer item;

    private String description;

    private String remark;

    private String unit;

    private Double amount;
    
    private Integer isDelete;

    private Date updateTimestamp;
    
    private Integer csn;
    
    private Integer isMain;
    
	private Integer isBlacklist;
    
    private Integer mainId;
    
    private String mainPartNumber;
    
    private Integer line;
    
    private String error;
    
    private Integer userId;
    
    private Integer typeCode;
    
    private String bsn;
    
    private Integer emailSend;
    
    private Integer supplierId;
    
    private Double price;
    
    private Integer currencyId;
    
    private Integer conditionId;
    
    private Integer certificationId;
    
    private Integer statusSupplierQuotePriceId;
    
    private Integer elementStatusId;
    
    private Double total;
    
    private Integer clientOrderElementId;
    
    private String quoteNumber;
    
    private String userName;
    
    private Date inquiryDate;
    
    private Integer inquiryStatus;
    
    private String conditionCode;
    
    private String conditionValue;
    
    private Integer clientId;
    
    private Double bankCost;
    
    private Double fixedCost;
    
    private Double profitMargin;
    
    private String shortPartNumber;
    
    private String source;
    
    private Date deadline;
    
    private String aimPrice;
    
    private String elementStatusValue;
    
    private Integer bizTypeId;
    
    private Integer clientTemplateType;
    
    private String tPartRemark;
    
    private String sourceNumber;
    
    private String ata;
    
    private String weight;
    
    private String dimentions;
    
    private String condition;
    
    private String supplierAndPrice;
    
    private Double moq;
    
    private Integer leadTime;
    
    private String quoteCond;
    
    private String quoteCert;
    
    private Integer clientQuoteElementId;
    
    private Integer clientQuoteId;
    
    private Integer supplierQuoteElementId;
    
    private Integer row;
    
    private String clientCode;
    
    private String supplierCode;
    
    private Double supplierQuotePrice;
    
    private Integer supplierCurrencyId;
    
    private Double supplierExchangeRate;
    
    private Double exchangeRate;
    
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
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
        this.partNumber = partNumber.replace(" ", "");
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnit() {
        return unit;
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

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getAlterPartNumber() {
		return alterPartNumber;
	}

	public void setAlterPartNumber(String alterPartNumber) {
		this.alterPartNumber = alterPartNumber;
	}

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}

	public Integer getMainId() {
		return mainId;
	}

	public void setMainId(Integer mainId) {
		this.mainId = mainId;
	}

	public String getMainPartNumber() {
		return mainPartNumber;
	}

	public void setMainPartNumber(String mainPartNumber) {
		this.mainPartNumber = mainPartNumber;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	public Integer getEmailSend() {
		return emailSend;
	}

	public void setEmailSend(Integer emailSend) {
		this.emailSend = emailSend;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
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

	public Integer getStatusSupplierQuotePriceId() {
		return statusSupplierQuotePriceId;
	}

	public void setStatusSupplierQuotePriceId(Integer statusSupplierQuotePriceId) {
		this.statusSupplierQuotePriceId = statusSupplierQuotePriceId;
	}

	public Integer getElementStatusId() {
		return elementStatusId;
	}

	public void setElementStatusId(Integer elementStatusId) {
		this.elementStatusId = elementStatusId;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
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

	/**
	 * @return the inquiryDate
	 */
	public Date getInquiryDate() {
		return inquiryDate;
	}

	/**
	 * @param inquiryDate the inquiryDate to set
	 */
	public void setInquiryDate(Date inquiryDate) {
		this.inquiryDate = inquiryDate;
	}

	/**
	 * @return the inquiryStatus
	 */
	public Integer getInquiryStatus() {
		return inquiryStatus;
	}

	/**
	 * @param inquiryStatus the inquiryStatus to set
	 */
	public void setInquiryStatus(Integer inquiryStatus) {
		this.inquiryStatus = inquiryStatus;
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
	 * @return the clientId
	 */
	public Integer getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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
	 * @return the fixedCost
	 */
	public Double getFixedCost() {
		return fixedCost;
	}

	/**
	 * @param fixedCost the fixedCost to set
	 */
	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	/**
	 * @return the profitMargin
	 */
	public Double getProfitMargin() {
		return profitMargin;
	}

	/**
	 * @param profitMargin the profitMargin to set
	 */
	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	/**
	 * @return the shortPartNumber
	 */
	public String getShortPartNumber() {
		return shortPartNumber;
	}

	/**
	 * @param shortPartNumber the shortPartNumber to set
	 */
	public void setShortPartNumber(String shortPartNumber) {
		this.shortPartNumber = shortPartNumber;
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
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
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

	/**
	 * @return the elementStatusValue
	 */
	public String getElementStatusValue() {
		return elementStatusValue;
	}

	/**
	 * @param elementStatusValue the elementStatusValue to set
	 */
	public void setElementStatusValue(String elementStatusValue) {
		this.elementStatusValue = elementStatusValue;
	}

	/**
	 * @return the bizTypeId
	 */
	public Integer getBizTypeId() {
		return bizTypeId;
	}

	/**
	 * @param bizTypeId the bizTypeId to set
	 */
	public void setBizTypeId(Integer bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	/**
	 * @return the clientTemplateType
	 */
	public Integer getClientTemplateType() {
		return clientTemplateType;
	}

	/**
	 * @param clientTemplateType the clientTemplateType to set
	 */
	public void setClientTemplateType(Integer clientTemplateType) {
		this.clientTemplateType = clientTemplateType;
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
	 * @return the ata
	 */
	public String getAta() {
		return ata;
	}

	/**
	 * @param ata the ata to set
	 */
	public void setAta(String ata) {
		this.ata = ata;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * @return the dimentions
	 */
	public String getDimentions() {
		return dimentions;
	}

	/**
	 * @param dimentions the dimentions to set
	 */
	public void setDimentions(String dimentions) {
		this.dimentions = dimentions;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return the supplierAndPrice
	 */
	public String getSupplierAndPrice() {
		return supplierAndPrice;
	}

	/**
	 * @param supplierAndPrice the supplierAndPrice to set
	 */
	public void setSupplierAndPrice(String supplierAndPrice) {
		this.supplierAndPrice = supplierAndPrice;
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

	/**
	 * @return the leadTime
	 */
	public Integer getLeadTime() {
		return leadTime;
	}

	/**
	 * @param leadTime the leadTime to set
	 */
	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}

	/**
	 * @return the quoteCond
	 */
	public String getQuoteCond() {
		return quoteCond;
	}

	/**
	 * @param quoteCond the quoteCond to set
	 */
	public void setQuoteCond(String quoteCond) {
		this.quoteCond = quoteCond;
	}

	/**
	 * @return the quoteCert
	 */
	public String getQuoteCert() {
		return quoteCert;
	}

	/**
	 * @param quoteCert the quoteCert to set
	 */
	public void setQuoteCert(String quoteCert) {
		this.quoteCert = quoteCert;
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
	 * @return the clientQuoteId
	 */
	public Integer getClientQuoteId() {
		return clientQuoteId;
	}

	/**
	 * @param clientQuoteId the clientQuoteId to set
	 */
	public void setClientQuoteId(Integer clientQuoteId) {
		this.clientQuoteId = clientQuoteId;
	}

	/**
	 * @return the supplierQuoteElementId
	 */
	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}

	/**
	 * @param supplierQuoteElementId the supplierQuoteElementId to set
	 */
	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}

	/**
	 * @return the row
	 */
	public Integer getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}

	/**
	 * @return the clientCode
	 */
	public String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	/**
	 * @return the supplierCode
	 */
	public String getSupplierCode() {
		return supplierCode;
	}

	/**
	 * @param supplierCode the supplierCode to set
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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
	 * @return the supplierCurrencyId
	 */
	public Integer getSupplierCurrencyId() {
		return supplierCurrencyId;
	}

	/**
	 * @param supplierCurrencyId the supplierCurrencyId to set
	 */
	public void setSupplierCurrencyId(Integer supplierCurrencyId) {
		this.supplierCurrencyId = supplierCurrencyId;
	}

	/**
	 * @return the supplierExchangeRate
	 */
	public Double getSupplierExchangeRate() {
		return supplierExchangeRate;
	}

	/**
	 * @param supplierExchangeRate the supplierExchangeRate to set
	 */
	public void setSupplierExchangeRate(Double supplierExchangeRate) {
		this.supplierExchangeRate = supplierExchangeRate;
	}

	/**
	 * @return the exchangeRate
	 */
	public Double getExchangeRate() {
		return exchangeRate;
	}

	/**
	 * @param exchangeRate the exchangeRate to set
	 */
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}


	
}