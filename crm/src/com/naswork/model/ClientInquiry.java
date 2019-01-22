package com.naswork.model;

import java.util.Date;
import java.util.List;

public class ClientInquiry {
	private Double exchangeRate;
	
	private Double counterFee;
	
	private Integer supplierQuoteElementId;
	
	private Integer supplierQuoteId;
	
	private String supplierInquiryQuoteNumber;
	
	private Double quotePrice;
	
	private String quoteRemark;
	
	private Integer elementId;
	
    private Integer id;

    private Integer clientContactId;

    private Integer clientId;

    private Integer bizTypeId;

    private Integer airTypeId;

    private Date inquiryDate;

    private Date deadline;

    private String sourceNumber;

    private String quoteNumber;

    private Integer quoteNumberSeq;

    private String terms;

    private String remark;

    private Integer inquiryStatusId;

    private Date updateTimestamp;
    
    private String supplierCode;
    
    private Double price;
    
    private String partNumber;
    
    private Double amount;
    
    private String unit;
    
    private String description;
    
    private String airType;
    
    private Integer item;
    
    private Integer csn;
    
    private Integer emailStatus;
    
    private Integer createUser;
    
    private Date realDeadline;
    
    private Integer crawlEmail;
    
    private Integer qqMailSend;
    
    private List<ClientInquiryElement> list;
    
    private String mixId;
    
    private Double maxPrice;
    
    private Double minPrice;

    public Integer getCsn() {
		return csn;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(Double counterFee) {
		this.counterFee = counterFee;
	}

	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}

	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}

	public Integer getSupplierQuoteId() {
		return supplierQuoteId;
	}

	public void setSupplierQuoteId(Integer supplierQuoteId) {
		this.supplierQuoteId = supplierQuoteId;
	}

	public String getSupplierInquiryQuoteNumber() {
		return supplierInquiryQuoteNumber;
	}

	public void setSupplierInquiryQuoteNumber(String supplierInquiryQuoteNumber) {
		this.supplierInquiryQuoteNumber = supplierInquiryQuoteNumber;
	}

	public Double getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(Double quotePrice) {
		this.quotePrice = quotePrice;
	}

	public String getQuoteRemark() {
		return quoteRemark;
	}

	public void setQuoteRemark(String quoteRemark) {
		this.quoteRemark = quoteRemark;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
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

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getBizTypeId() {
        return bizTypeId;
    }

    public void setBizTypeId(Integer bizTypeId) {
        this.bizTypeId = bizTypeId;
    }

    public Integer getAirTypeId() {
        return airTypeId;
    }

    public void setAirTypeId(Integer airTypeId) {
        this.airTypeId = airTypeId;
    }

    public Date getInquiryDate() {
        return inquiryDate;
    }

    public Integer getElementId() {
		return elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	public void setInquiryDate(Date inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Integer getQuoteNumberSeq() {
        return quoteNumberSeq;
    }

    public void setQuoteNumberSeq(Integer quoteNumberSeq) {
        this.quoteNumberSeq = quoteNumberSeq;
    }

    public String getAirType() {
		return airType;
	}

	public void setAirType(String airType) {
		this.airType = airType;
	}

	public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getInquiryStatusId() {
        return inquiryStatusId;
    }

    public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setInquiryStatusId(Integer inquiryStatusId) {
        this.inquiryStatusId = inquiryStatusId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public Integer getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
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
	 * @return the realDeadline
	 */
	public Date getRealDeadline() {
		return realDeadline;
	}

	/**
	 * @param realDeadline the realDeadline to set
	 */
	public void setRealDeadline(Date realDeadline) {
		this.realDeadline = realDeadline;
	}

	/**
	 * @return the crawlEmail
	 */
	public Integer getCrawlEmail() {
		return crawlEmail;
	}

	/**
	 * @param crawlEmail the crawlEmail to set
	 */
	public void setCrawlEmail(Integer crawlEmail) {
		this.crawlEmail = crawlEmail;
	}

	/**
	 * @return the qqMailSend
	 */
	public Integer getQqMailSend() {
		return qqMailSend;
	}

	/**
	 * @param qqMailSend the qqMailSend to set
	 */
	public void setQqMailSend(Integer qqMailSend) {
		this.qqMailSend = qqMailSend;
	}

	/**
	 * @return the list
	 */
	public List<ClientInquiryElement> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<ClientInquiryElement> list) {
		this.list = list;
	}

	/**
	 * @return the mixId
	 */
	public String getMixId() {
		return mixId;
	}

	/**
	 * @param mixId the mixId to set
	 */
	public void setMixId(String mixId) {
		this.mixId = mixId;
	}

	/**
	 * @return the maxPrice
	 */
	public Double getMaxPrice() {
		return maxPrice;
	}

	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	/**
	 * @return the minPrice
	 */
	public Double getMinPrice() {
		return minPrice;
	}

	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}
}