package com.naswork.model;

import java.util.Date;

public class ClientOrderElement {
	private Integer complete;
	
	/**
	 * @return the splitReceive
	 */
	public Integer getSplitReceive() {
		return splitReceive;
	}

	/**
	 * @param splitReceive the splitReceive to set
	 */
	public void setSplitReceive(Integer splitReceive) {
		this.splitReceive = splitReceive;
	}

	private Double bankCharges;
	
	private String orderNumber;
	
	private String sourceNumber;
	
	private Integer spzt;
	
	private Integer orderStatusId;
	
    private Integer id;
    
    private Integer item;
    
    private Integer userId;
    
    private Integer clientOrderId;

    private Integer clientQuoteElementId;

    private Double amount;

    private Double price;

	private String leadTime;

    private Date deadline;

    private Date updateTimestamp;
    
    private String partNumber;
    
    private String clientCode;
    
    private String airCode;
    
    private String error;
    
    private Integer line;
    
    private Integer csn;
    
    private String description;
    
    private String unit;
    
    private String code;
    
    private String quoteNumber;
    
    private Date orderDate;
    
    private String awb;
    
    private Double weight;
    
    private String size;
    
    private String logisticsWayValue;
    
    private Date exportDate;
    
    private Date receiveDate;
    
    private Double total;
    
    private String quotePartNumber;
    
    private Integer certificationId;
    
    private Integer storageStatus;
    
    private Double fixedCost;
    
    private Double exchangeRate;
    
    private String remark;
    
    private String bsn;
    
    private Integer elementStatusId;
    
    private Integer splitReceive;
    
    private Double originalAmount;
    
    private Double clientOrderAmount;
    
	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	public ClientOrderElement(Integer orderStatusId, Integer clientOrderId,
			Integer clientQuoteElementId, Double price, String leadTime,
			Date deadline, String description,String remark,Integer elementStatusId) {
		super();
		this.orderStatusId = orderStatusId;
		this.clientOrderId = clientOrderId;
		this.clientQuoteElementId = clientQuoteElementId;
		this.price = price;
		this.leadTime = leadTime;
		this.deadline = deadline;
		this.description = description;
		this.remark = remark;
		this.elementStatusId = elementStatusId;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	public String getRemark() {
		return remark;
	}

	public Double getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public Integer getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}

	public String getQuotePartNumber() {
		return quotePartNumber;
	}

	public void setQuotePartNumber(String quotePartNumber) {
		this.quotePartNumber = quotePartNumber;
	}

	public Integer getLine() {
		return line;
	}

	public Integer getSpzt() {
		return spzt;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
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

	public ClientOrderElement() {
		super();
	}

	public ClientOrderElement(Integer clientOrderId,
			Integer clientQuoteElementId, Double amount, Double price,
			String leadTime, Date deadline, Date updateTimestamp,String partNumber,Integer certificationId,Double fixedCost,String remark) {
		super();
		this.clientOrderId = clientOrderId;
		this.clientQuoteElementId = clientQuoteElementId;
		this.amount = amount;
		this.price = price;
		this.leadTime = leadTime;
		this.deadline = deadline;
		this.updateTimestamp = updateTimestamp;
		this.partNumber = partNumber;
		this.certificationId = certificationId;
		this.fixedCost=fixedCost;
		this.remark=remark;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Integer clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Integer getClientQuoteElementId() {
        return clientQuoteElementId;
    }

    public void setClientQuoteElementId(Integer clientQuoteElementId) {
        this.clientQuoteElementId = clientQuoteElementId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getAirCode() {
		return airCode;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public void setAirCode(String airCode) {
		this.airCode = airCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCsn() {
		return csn;
	}

	public void setCsn(Integer csn) {
		this.csn = csn;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getAwb() {
		return awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLogisticsWayValue() {
		return logisticsWayValue;
	}

	public void setLogisticsWayValue(String logisticsWayValue) {
		this.logisticsWayValue = logisticsWayValue;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getStorageStatus() {
		return storageStatus;
	}

	public void setStorageStatus(Integer storageStatus) {
		this.storageStatus = storageStatus;
	}

	public Integer getElementStatusId() {
		return elementStatusId;
	}

	public void setElementStatusId(Integer elementStatusId) {
		this.elementStatusId = elementStatusId;
	}

	/**
	 * @return the originalAmount
	 */
	public Double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the clientOrderAmount
	 */
	public Double getClientOrderAmount() {
		return clientOrderAmount;
	}

	/**
	 * @param clientOrderAmount the clientOrderAmount to set
	 */
	public void setClientOrderAmount(Double clientOrderAmount) {
		this.clientOrderAmount = clientOrderAmount;
	}
}