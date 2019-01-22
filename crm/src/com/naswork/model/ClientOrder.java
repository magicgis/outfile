package com.naswork.model;

import java.util.Date;

public class ClientOrder {
	private Integer complete;
	
	private Integer clientWeatherOrderId;
	
	private Integer spzt;
	
	private Integer createUserId;
	
    private Integer id;
    
    private Double prepayRate;
	
	private Double shipPayRate;
	
	private Integer shipPayPeriod;
	
	private Double receivePayRate;
	
	private Integer receivePayPeriod;

    private Integer clientQuoteId;

    private Integer currencyId;

    private Double exchangeRate;

    private String sourceNumber;

    private String orderNumber;

    private Integer seq;

    private Date orderDate;

    private Integer terms;

    private String remark;

    private Integer orderStatusId;

    private Date updateTimestamp;
    
    private String clientCode;
    
    private String airCode;
    
    private String lc;
    
    private String importersRegistration;
    
    private Integer purchaseApply;
    
    private Double total;
    
    private Double totalAmount;
    
    private Double clientOrderPrice;
    
    private Double orderAmount;
    
    private String certification;
    
    private String replenishment;
    
    private Double freight;
    
    private Integer urgentLevelId;
    
    private String urgentLevelValue;

    public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	public Integer getClientWeatherOrderId() {
		return clientWeatherOrderId;
	}

	public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
		this.clientWeatherOrderId = clientWeatherOrderId;
	}

	public String getReplenishment() {
		return replenishment;
	}

	public void setReplenishment(String replenishment) {
		this.replenishment = replenishment;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getSpzt() {
		return spzt;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
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

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Double getPrepayRate() {
		return prepayRate;
	}

	public void setPrepayRate(Double prepayRate) {
		this.prepayRate = prepayRate;
	}

	public Double getShipPayRate() {
		return shipPayRate;
	}

	public void setShipPayRate(Double shipPayRate) {
		this.shipPayRate = shipPayRate;
	}

	public Integer getShipPayPeriod() {
		return shipPayPeriod;
	}

	public void setShipPayPeriod(Integer shipPayPeriod) {
		this.shipPayPeriod = shipPayPeriod;
	}

	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}

	public Integer getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Integer receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
	}

	public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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

	public void setAirCode(String airCode) {
		this.airCode = airCode;
	}

	public String getLc() {
		return lc;
	}

	public void setLc(String lc) {
		this.lc = lc;
	}

	public String getImportersRegistration() {
		return importersRegistration;
	}

	public void setImportersRegistration(String importersRegistration) {
		this.importersRegistration = importersRegistration;
	}

	public Integer getPurchaseApply() {
		return purchaseApply;
	}

	public void setPurchaseApply(Integer purchaseApply) {
		this.purchaseApply = purchaseApply;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getClientOrderPrice() {
		return clientOrderPrice;
	}

	public void setClientOrderPrice(Double clientOrderPrice) {
		this.clientOrderPrice = clientOrderPrice;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	/**
	 * @return the freight
	 */
	public Double getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(Double freight) {
		this.freight = freight;
	}

	/**
	 * @return the urgentLevelId
	 */
	public Integer getUrgentLevelId() {
		return urgentLevelId;
	}

	/**
	 * @param urgentLevelId the urgentLevelId to set
	 */
	public void setUrgentLevelId(Integer urgentLevelId) {
		this.urgentLevelId = urgentLevelId;
	}

	/**
	 * @return the urgentLevelValue
	 */
	public String getUrgentLevelValue() {
		return urgentLevelValue;
	}

	/**
	 * @param urgentLevelValue the urgentLevelValue to set
	 */
	public void setUrgentLevelValue(String urgentLevelValue) {
		this.urgentLevelValue = urgentLevelValue;
	}
}