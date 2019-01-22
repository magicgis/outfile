package com.naswork.model;

import java.util.Date;

public class SupplierCommissionSale {
    private Integer id;

    private Integer supplierId;

    private Date commissionDate;

    private String remark;

    private Date updateTimestamp;
    
    private String supplierCode;
    
    private Integer currencyId;
    
    private String currencyCode;
    
    private Date validity;
    
    private Integer saleStatus;
    
    private Integer clientInquiryId;
    
    private Integer crawlClientInquiryId;
    
    private String quoteNumber;
    
    private Integer crawlStatus;
    
    private String crawlQuoteNumber;
    
    private Integer airTypeId;
    
    private Integer clientId;
    
    private String airTypeValue;
    
    private Integer commissionAirTypeId;
    
    private Integer crawlStorageStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Date getCommissionDate() {
        return commissionDate;
    }

    public void setCommissionDate(Date commissionDate) {
        this.commissionDate = commissionDate;
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

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	/**
	 * @return the currencyId
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * @return the validity
	 */
	public Date getValidity() {
		return validity;
	}

	/**
	 * @param validity the validity to set
	 */
	public void setValidity(Date validity) {
		this.validity = validity;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the saleStatus
	 */
	public Integer getSaleStatus() {
		return saleStatus;
	}

	/**
	 * @param saleStatus the saleStatus to set
	 */
	public void setSaleStatus(Integer saleStatus) {
		this.saleStatus = saleStatus;
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
	 * @return the crawlClientInquiryId
	 */
	public Integer getCrawlClientInquiryId() {
		return crawlClientInquiryId;
	}

	/**
	 * @param crawlClientInquiryId the crawlClientInquiryId to set
	 */
	public void setCrawlClientInquiryId(Integer crawlClientInquiryId) {
		this.crawlClientInquiryId = crawlClientInquiryId;
	}

	/**
	 * @return the quoteNumber
	 */
	public String getQuoteNumber() {
		return quoteNumber;
	}

	/**
	 * @param quoteNumber the quoteNumber to set
	 */
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	/**
	 * @return the crawlStatus
	 */
	public Integer getCrawlStatus() {
		return crawlStatus;
	}

	/**
	 * @param crawlStatus the crawlStatus to set
	 */
	public void setCrawlStatus(Integer crawlStatus) {
		this.crawlStatus = crawlStatus;
	}

	/**
	 * @return the crawlQuoteNumber
	 */
	public String getCrawlQuoteNumber() {
		return crawlQuoteNumber;
	}

	/**
	 * @param crawlQuoteNumber the crawlQuoteNumber to set
	 */
	public void setCrawlQuoteNumber(String crawlQuoteNumber) {
		this.crawlQuoteNumber = crawlQuoteNumber;
	}

	/**
	 * @return the airTypeId
	 */
	public Integer getAirTypeId() {
		return airTypeId;
	}

	/**
	 * @param airTypeId the airTypeId to set
	 */
	public void setAirTypeId(Integer airTypeId) {
		this.airTypeId = airTypeId;
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
	 * @return the airTypeValue
	 */
	public String getAirTypeValue() {
		return airTypeValue;
	}

	/**
	 * @param airTypeValue the airTypeValue to set
	 */
	public void setAirTypeValue(String airTypeValue) {
		this.airTypeValue = airTypeValue;
	}

	/**
	 * @return the commissionAirTypeId
	 */
	public Integer getCommissionAirTypeId() {
		return commissionAirTypeId;
	}

	/**
	 * @param commissionAirTypeId the commissionAirTypeId to set
	 */
	public void setCommissionAirTypeId(Integer commissionAirTypeId) {
		this.commissionAirTypeId = commissionAirTypeId;
	}

	/**
	 * @return the crawlStorageStatus
	 */
	public Integer getCrawlStorageStatus() {
		return crawlStorageStatus;
	}

	/**
	 * @param crawlStorageStatus the crawlStorageStatus to set
	 */
	public void setCrawlStorageStatus(Integer crawlStorageStatus) {
		this.crawlStorageStatus = crawlStorageStatus;
	}
}