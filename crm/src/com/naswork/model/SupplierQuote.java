package com.naswork.model;

import java.util.Date;

public class SupplierQuote {
	private String supplierInquiryQuoteNumber;
	
	private Integer quoteStatusId;
	
    private Integer id;

    private Integer supplierInquiryId;

    private Integer currencyId;

    private Double exchangeRate;

    private Date quoteDate;

    private Date updateTimestamp;
    
    private String value;
    
    private String  quoteStatusValue;
    
    private String quoteNumber;
    
    private Date validity;
    
    private Integer createUser;
    
    private String sourceNumber;
    
    private Double feeForExchangeBill;
    
    private Double bankCost;
    
    private Integer lastUpdateUser;

    public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public String getQuoteStatusValue() {
		return quoteStatusValue;
	}

	public void setQuoteStatusValue(String quoteStatusValue) {
		this.quoteStatusValue = quoteStatusValue;
	}

	public Integer getId() {
        return id;
    }

    public String getSupplierInquiryQuoteNumber() {
		return supplierInquiryQuoteNumber;
	}

	public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
		this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
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


	public String getValue() {
		return value;
	}

	public Integer getQuoteStatusId() {
		return quoteStatusId;
	}

	public void setQuoteStatusId(Integer quoteStatusId) {
		this.quoteStatusId = quoteStatusId;
	}

	public void setValue(String value) {
		this.value = value;
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

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
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
	 * @return the lastUpdateUser
	 */
	public Integer getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * @param lastUpdateUser the lastUpdateUser to set
	 */
	public void setLastUpdateUser(Integer lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
}