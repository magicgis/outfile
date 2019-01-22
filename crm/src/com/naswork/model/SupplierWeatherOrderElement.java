package com.naswork.model;

import java.util.Date;
import java.util.List;

public class SupplierWeatherOrderElement {
	private Integer supplierStatus;
	
	private Integer clientWeatherOrderId;
	
	private Integer supplierCode;
	
	private String remark;
	
    private Integer id;

    private Integer clientOrderElementId;

    private Integer supplierQuoteElementId;

    private Double amount;

    private Double price;

    private String leadTime;

    private Date deadline;

    private Integer shipWayId;

    private String destination;

    private Date updateTimestamp;
    
    private String taskId;
    
    private Double bankCost;
    
    private Double feeForExchangeBill;
    
    private Double otherFee;
    
    private Integer supplierWeatherOrderId;
    
    private List<SupplierWeatherOrderElement> list;

    public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getSupplierStatus() {
		return supplierStatus;
	}

	public void setSupplierStatus(Integer supplierStatus) {
		this.supplierStatus = supplierStatus;
	}

	public Integer getClientWeatherOrderId() {
		return clientWeatherOrderId;
	}

	public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
		this.clientWeatherOrderId = clientWeatherOrderId;
	}


	public Integer getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(Integer supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }

    public Integer getSupplierQuoteElementId() {
        return supplierQuoteElementId;
    }

    public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
        this.supplierQuoteElementId = supplierQuoteElementId;
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

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getShipWayId() {
        return shipWayId;
    }

    public void setShipWayId(Integer shipWayId) {
        this.shipWayId = shipWayId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
	 * @return the supplierWeatherOrderId
	 */
	public Integer getSupplierWeatherOrderId() {
		return supplierWeatherOrderId;
	}

	/**
	 * @param supplierWeatherOrderId the supplierWeatherOrderId to set
	 */
	public void setSupplierWeatherOrderId(Integer supplierWeatherOrderId) {
		this.supplierWeatherOrderId = supplierWeatherOrderId;
	}

	/**
	 * @return the list
	 */
	public List<SupplierWeatherOrderElement> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<SupplierWeatherOrderElement> list) {
		this.list = list;
	}
}