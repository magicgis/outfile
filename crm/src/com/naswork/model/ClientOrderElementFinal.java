package com.naswork.model;

import java.util.Date;

public class ClientOrderElementFinal {
	private Integer clientWeatherOrderId;
	
	private String description;
	
	private Double bankCharges;
	
    private Integer id;

    private Integer clientOrderElementId;

    private Double amount;

    private Double price;

    private String leadTime;

    private Double fixedCost;

    private Integer certificationId;

    private Integer orderStatusId;

    private Date updateTimestamp;

    private Date deadline;
    
    private Integer orderNumberIndex;
    
	public Integer getClientWeatherOrderId() {
		return clientWeatherOrderId;
	}

	public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
		this.clientWeatherOrderId = clientWeatherOrderId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public Double getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(Double bankCharges) {
		this.bankCharges = bankCharges;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
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

    public Double getFixedCost() {
        return fixedCost;
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

	/**
	 * @return the orderNumberIndex
	 */
	public Integer getOrderNumberIndex() {
		return orderNumberIndex;
	}

	/**
	 * @param orderNumberIndex the orderNumberIndex to set
	 */
	public void setOrderNumberIndex(Integer orderNumberIndex) {
		this.orderNumberIndex = orderNumberIndex;
	}

}