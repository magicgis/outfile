package com.naswork.model;

import java.util.Date;

public class ClientWeatherOrderElement {
	private String bsn;
	
	private String description;
	
	private Double bankCharges;
	
	private String remark;
	
	private Integer item;
	    
	private Integer userId;
	    
	private Integer line;
	    
	private String error;
	
    private Integer id;

    private Integer clientWeatherOrderId;

    private Integer clientQuoteElementId;

	private Double amount;

    private Double price;

    private String leadTime;

    private Date deadline;

    private Date updateTimestamp;

    private Integer certificationId;

    private Integer orderStatusId;

    private Double fixedCost;
    
    private String partNumber;
    
    private Double exchangeRate;
    
    private Integer currencyId;
    
	private Double clientProfitMargin;
	
	private String supplierCode;
	
	private Double supplierExchangeRate;
	
	private String unit;
	
	private Integer supplierCurrencyId;
	
	public ClientWeatherOrderElement(Integer clientWeatherOrderId,
			Integer clientQuoteElementId, Double amount, Double price,
			String leadTime, Date deadline, Date updateTimestamp,String partNumber,Integer certificationId,Double fixedCost,String remark) {
		super();
		this.clientWeatherOrderId = clientWeatherOrderId;
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
	
	public ClientWeatherOrderElement(ClientWeatherOrderElementBackUp clientWeatherOrderElementBackUp) {
		super();
		this.description = clientWeatherOrderElementBackUp.getDescription();
		this.remark = clientWeatherOrderElementBackUp.getRemark();
		this.clientWeatherOrderId = clientWeatherOrderElementBackUp.getClientWeatherOrderId();
		this.clientQuoteElementId = clientWeatherOrderElementBackUp.getClientQuoteElementId();
		this.amount = clientWeatherOrderElementBackUp.getAmount();
		this.price = clientWeatherOrderElementBackUp.getPrice();
		this.leadTime = clientWeatherOrderElementBackUp.getLeadTime();
		this.deadline = clientWeatherOrderElementBackUp.getDeadline();
		this.certificationId = clientWeatherOrderElementBackUp.getCertificationId();
		this.fixedCost = clientWeatherOrderElementBackUp.getFixedCost();
		this.unit = clientWeatherOrderElementBackUp.getUnit();
		this.partNumber = clientWeatherOrderElementBackUp.getPartNumber();
	}

	public Double getClientProfitMargin() {
		return clientProfitMargin;
	}

	public void setClientProfitMargin(Double clientProfitMargin) {
		this.clientProfitMargin = clientProfitMargin;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
    
    public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public ClientWeatherOrderElement() {
		super();
	}
	
    public Integer getId() {
        return id;
    }

    public Double getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientWeatherOrderId() {
        return clientWeatherOrderId;
    }

    public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
        this.clientWeatherOrderId = clientWeatherOrderId;
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

    public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

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

    public Integer getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Integer certificationId) {
        this.certificationId = certificationId;
    }

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
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
}