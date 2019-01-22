package com.naswork.model;

import java.util.Date;

public class SupplierQuoteElement {
	
	private Double counterFee;
	
	private Integer currencyId;
	
	private Integer supplierId;
	
	private String supplierInquiryQuoteNumber;
	
	private Double exchangeRate;
	
	private String tagDate;
	
	private String warranty;
	
	private String serialNumber;
	
	private String tagSrc;
	
	private String trace;
	
	private Date validity;
	
	private Double freight;
	
    private String  conditionValue;
	
	 private String conditionCode;
	 
	 private String certificationCode;
	 
	 private String certificationValue;
	 
	 private String supplierQuoteStatusValue;
    
	private Integer item;
	
	private Integer csn;
	
    private Integer id;

    private Integer supplierQuoteId;

    private Integer supplierInquiryElementId;

    private Integer elementId;

    private String partNumber;
    
    private String alterPartNumber;

    private String description;

    private Double amount;

    private String unit;

    private Double price;
    
    private String location;
    
    private Integer moq;

    private String leadTime;

    private Integer conditionId;

    private Integer certificationId;

    private String remark;

    private Integer supplierQuoteStatusId;

    private Date updateTimestamp;
    
    private String code;
    
    private Integer clientInquiryElementId;
    
    private String supplierInquiryNumber;
    
    private String clientInquiryNumber;
    
    private Date quoteDate;
    
    private String quoteNumber;
    
    private Integer clientInquiryId;
    
    private String supplierName;
    
    private String supplierCode;
    
    private String currencyValue;
    
    private Integer createUser;
    
    private Double availableQty;
    
    private String shortPartNumber;
    
    private String coreCharge;
    
    private String sourceNumber;
    
    private Double feeForExchangeBill;
    
    private Double bankCost;
    
    private Double hazmatFee;
    
    private String quoteFeeForExchangeBill;
    
    private String quoteBankCost;
    
    private String quoteHazmatFee;
    
    private Integer isCompetitor;
    
    private Double otherFee;
    
    private String quoteOtherFee;
    
    private String isAgentValue;
    
    private Integer updateUserId;
    
    public String getQuoteNumber() {
		return quoteNumber;
	}


	public Double getCounterFee() {
		return counterFee;
	}


	public void setCounterFee(Double counterFee) {
		this.counterFee = counterFee;
	}


	public Integer getCurrencyId() {
		return currencyId;
	}


	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}


	public Integer getSupplierId() {
		return supplierId;
	}


	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}


	public String getSupplierInquiryQuoteNumber() {
		return supplierInquiryQuoteNumber;
	}


	public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
		this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
	}


	public String getTagDate() {
		return tagDate;
	}


	public Double getExchangeRate() {
		return exchangeRate;
	}


	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Integer getClientInquiryElementId() {
		return clientInquiryElementId;
	}

	public void setClientInquiryElementId(Integer clientInquiryElementId) {
		this.clientInquiryElementId = clientInquiryElementId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierQuoteId() {
        return supplierQuoteId;
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

	public String getCertificationValue() {
		return certificationValue;
	}

	public void setCertificationValue(String certificationValue) {
		this.certificationValue = certificationValue;
	}

	public String getSupplierQuoteStatusValue() {
		return supplierQuoteStatusValue;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public void setSupplierQuoteStatusValue(String supplierQuoteStatusValue) {
		this.supplierQuoteStatusValue = supplierQuoteStatusValue;
	}

	public void setSupplierQuoteId(Integer supplierQuoteId) {
        this.supplierQuoteId = supplierQuoteId;
    }


	public Double getFreight() {
		return freight;
	}


	public void setFreight(Double freight) {
		this.freight = freight;
	}


	public Integer getSupplierInquiryElementId() {
        return supplierInquiryElementId;
    }

    public void setSupplierInquiryElementId(Integer supplierInquiryElementId) {
        this.supplierInquiryElementId = supplierInquiryElementId;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public Integer getItem() {
		return item;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
        StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < partNumber.length(); i++) {
			char ch = partNumber.charAt(i);
			/*if (isValidCharacter(ch)) {
				buffer.append(Character.toUpperCase(ch));
			}*/
			String regex = "[a-z0-9A-Z\u4e00-\u9fa5]";//其他需要，直接修改正则表达式就好
			if (String.valueOf(ch).matches(regex)) {
				buffer.append(String.valueOf(ch));
			}
			//return str.matches(regex);
		}
		setShortPartNumber(buffer.toString());;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMoq() {
		return moq;
	}

	public void setMoq(Integer moq) {
		this.moq = moq;
	}

	public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAlterPartNumber() {
		return alterPartNumber;
	}

	public void setAlterPartNumber(String alterPartNumber) {
		this.alterPartNumber = alterPartNumber;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSupplierQuoteStatusId() {
        return supplierQuoteStatusId;
    }

    public void setSupplierQuoteStatusId(Integer supplierQuoteStatusId) {
        this.supplierQuoteStatusId = supplierQuoteStatusId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSupplierInquiryNumber() {
		return supplierInquiryNumber;
	}

	public void setSupplierInquiryNumber(String supplierInquiryNumber) {
		this.supplierInquiryNumber = supplierInquiryNumber;
	}

	public String getClientInquiryNumber() {
		return clientInquiryNumber;
	}

	public void setClientInquiryNumber(String clientInquiryNumber) {
		this.clientInquiryNumber = clientInquiryNumber;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}


	/**
	 * @return the clientInquiryId
	 */
	public Integer getClientInquiryId() {
		return clientInquiryId;
	}


	/**
	 * @param clientInquiryId the clientInquiryId to set
	 */
	public void setClientInquiryId(Integer clientInquiryId) {
		this.clientInquiryId = clientInquiryId;
	}


	/**
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}


	/**
	 * @param supplierName the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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
	 * @return the currencyValue
	 */
	public String getCurrencyValue() {
		return currencyValue;
	}


	/**
	 * @param currencyValue the currencyValue to set
	 */
	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}


	/**
	 * @return the createUser
	 */
	public Integer getCreateUser() {
		return createUser;
	}


	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
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
	 * @return the isCompetitor
	 */
	public Integer getIsCompetitor() {
		return isCompetitor;
	}


	/**
	 * @param isCompetitor the isCompetitor to set
	 */
	public void setIsCompetitor(Integer isCompetitor) {
		this.isCompetitor = isCompetitor;
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
	 * @return the isAgentValue
	 */
	public String getIsAgentValue() {
		return isAgentValue;
	}


	/**
	 * @param isAgentValue the isAgentValue to set
	 */
	public void setIsAgentValue(String isAgentValue) {
		this.isAgentValue = isAgentValue;
	}


	/**
	 * @return the updateUserId
	 */
	public Integer getUpdateUserId() {
		return updateUserId;
	}


	/**
	 * @param updateUserId the updateUserId to set
	 */
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}
}