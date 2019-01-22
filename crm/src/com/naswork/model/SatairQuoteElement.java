package com.naswork.model;

import java.util.Date;

public class SatairQuoteElement {
    private Integer id;

    private String partNumber;

    private String cageCode;

    private String description;

    private String unit;

    private String unitPrice;

    private String currency;

    private String certification;

    private String leadTime;

    private String plant;

    private String enterPartnumber;

    private Double amount;

    private Integer satairQuoteId;
    
    private String quoteNumber;
    
    private String ifDanger;
    
    private String supplierCode;
    
    private String remark;
    
    private String moq;
    
    private Date updateDatetime;
    
    private Double storageAmount;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
    }

    public String getCageCode() {
        return cageCode;
    }

    public void setCageCode(String cageCode) {
        this.cageCode = cageCode;
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

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getEnterPartnumber() {
        return enterPartnumber;
    }

    public void setEnterPartnumber(String enterPartnumber) {
        this.enterPartnumber = enterPartnumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getSatairQuoteId() {
        return satairQuoteId;
    }

    public void setSatairQuoteId(Integer satairQuoteId) {
        this.satairQuoteId = satairQuoteId;
    }

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public String getIfDanger() {
		return ifDanger;
	}

	public void setIfDanger(String ifDanger) {
		this.ifDanger = ifDanger;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the moq
	 */
	public String getMoq() {
		return moq;
	}

	/**
	 * @param moq the moq to set
	 */
	public void setMoq(String moq) {
		this.moq = moq;
	}

	/**
	 * @return the updateDatetime
	 */
	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	/**
	 * @param updateDatetime the updateDatetime to set
	 */
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	/**
	 * @return the storageAmount
	 */
	public Double getStorageAmount() {
		return storageAmount;
	}

	/**
	 * @param storageAmount the storageAmount to set
	 */
	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}
}