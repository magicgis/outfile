package com.naswork.model;

import java.util.Date;

public class SupplierOrder {
	private Integer createUserId;
	
	private Integer orderType;
	
	private Double prepayRate;
		
	private Double shipPayRate;
	   	
	private Double receivePayRate;
	   	
	private Integer receivePayPeriod;
	
    private Integer id;

    private Integer clientOrderId;

    private Integer supplierId;

    private Integer currencyId;

    private Double exchangeRate;

    private Date orderDate;

    private String orderNumber;

    private Integer terms;

    private String remark;

    private Date updateTimestamp;

    private Integer orderStatusId;
    
    private Date deadline;

    private Integer allPrepayNotImportStatus;
    
    private String partNumber;
    
    private Integer shipLeadTime;
    
    private Double bankCost;
   	
   	private Double feeForExchangeBill;
   	
   	private Double otherFee;
   	
   	private Integer urgentLevelId;
    
    private String urgentLevelValue;
    
    public Integer getId() {
        return id;
    }

    public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

	public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Integer getAllPrepayNotImportStatus() {
		return allPrepayNotImportStatus;
	}

	public void setAllPrepayNotImportStatus(Integer allPrepayNotImportStatus) {
		this.allPrepayNotImportStatus = allPrepayNotImportStatus;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public Integer getShipLeadTime() {
		return shipLeadTime;
	}

	public void setShipLeadTime(Integer shipLeadTime) {
		this.shipLeadTime = shipLeadTime;
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
	 * @return the otherFee
	 */
	public Double getOtherFee() {
		return otherFee;
	}

	/**
	 * @param otherFee the otherFee to set
	 */
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
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