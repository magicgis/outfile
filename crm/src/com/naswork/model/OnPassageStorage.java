package com.naswork.model;

import java.util.Date;

public class OnPassageStorage {
	private Double profitMargin;
	
	private Double exchangeRate;
	
	private Double price;
	
    private Integer id;

    private Integer supplierOrderElementId;

    private Integer clientOrderElementId;

    private Double amount;

    private Integer importStatus;

    private Date updateTimestamp;
    
    private String orderNumber;
    
    private String supplierCode;
    
    private String partNumber;
    
    private String leadTime;
    
    private Date orderDate; 
    
    private Integer biggerAmount;
    
    private Integer currencyId;
    
    private String unit;

    public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierOrderElementId() {
        return supplierOrderElementId;
    }

    public void setSupplierOrderElementId(Integer supplierOrderElementId) {
        this.supplierOrderElementId = supplierOrderElementId;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(Integer importStatus) {
        this.importStatus = importStatus;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getBiggerAmount() {
		return biggerAmount;
	}

	public void setBiggerAmount(Integer biggerAmount) {
		this.biggerAmount = biggerAmount;
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

}