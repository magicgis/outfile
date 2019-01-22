package com.naswork.model;

import java.util.Date;

public class ClientQuoteElement {
	private Double bankCharges;
	
    private Integer id;

    private Integer clientQuoteId;

    private Integer clientInquiryElementId;

    private Integer supplierQuoteElementId;

    private Double price;

    private String remark;

    private Date updateTimestamp;

    private Double amount;
    
    private Integer item;
    
    private Integer leadTime;
    
    private String location;
    
    private Double fixedCost;
    
    private Integer clientInquiryId;
    
    private Double profitMargin;
    
    private Double moq;
    
    private Double relativeRate;

    public String getLocation() {
		return location;
	}

	public Double getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
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

    public Integer getClientInquiryElementId() {
        return clientInquiryElementId;
    }

    public void setClientInquiryElementId(Integer clientInquiryElementId) {
        this.clientInquiryElementId = clientInquiryElementId;
    }

    public Integer getSupplierQuoteElementId() {
        return supplierQuoteElementId;
    }

    public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
        this.supplierQuoteElementId = supplierQuoteElementId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
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
	 * @return the relativeRate
	 */
	public Double getRelativeRate() {
		return relativeRate;
	}

	/**
	 * @param relativeRate the relativeRate to set
	 */
	public void setRelativeRate(Double relativeRate) {
		this.relativeRate = relativeRate;
	}
}