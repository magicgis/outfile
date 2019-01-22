package com.naswork.model;

import java.util.Date;

public class SupplierCommissionForStockmarket {
    private Integer id;

    private Date createDate;

    private String number;

    private Integer supplierId;

    private Integer airTypeId;

    private String remark;

    private Integer clientInquiryId;

    private Integer saleStatus;

    private Integer crawlStatus;

    private Integer crawlStorageStatus;

    private Date updateTimestamp;
    
    private String airValue;

    private String airCode;
    
    private String quoteNumber;
    
    private String supplierCode;
    
    private Integer clientId;
    
    private Integer inquiryAirTypeId;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getAirTypeId() {
        return airTypeId;
    }

    public void setAirTypeId(Integer airTypeId) {
        this.airTypeId = airTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getClientInquiryId() {
        return clientInquiryId;
    }

    public void setClientInquiryId(Integer clientInquiryId) {
        this.clientInquiryId = clientInquiryId;
    }

    public Integer getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Integer saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Integer getCrawlStatus() {
        return crawlStatus;
    }

    public void setCrawlStatus(Integer crawlStatus) {
        this.crawlStatus = crawlStatus;
    }

    public Integer getCrawlStorageStatus() {
        return crawlStorageStatus;
    }

    public void setCrawlStorageStatus(Integer crawlStorageStatus) {
        this.crawlStorageStatus = crawlStorageStatus;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	/**
	 * @return the airValue
	 */
	public String getAirValue() {
		return airValue;
	}

	/**
	 * @param airValue the airValue to set
	 */
	public void setAirValue(String airValue) {
		this.airValue = airValue;
	}

	/**
	 * @return the airCode
	 */
	public String getAirCode() {
		return airCode;
	}

	/**
	 * @param airCode the airCode to set
	 */
	public void setAirCode(String airCode) {
		this.airCode = airCode;
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
	 * @return the inquiryAirTypeId
	 */
	public Integer getInquiryAirTypeId() {
		return inquiryAirTypeId;
	}

	/**
	 * @param inquiryAirTypeId the inquiryAirTypeId to set
	 */
	public void setInquiryAirTypeId(Integer inquiryAirTypeId) {
		this.inquiryAirTypeId = inquiryAirTypeId;
	}
}