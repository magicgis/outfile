package com.naswork.model;

public class OrderBankCharges {
	private Integer id;
	
    private Integer clientId;

    private Double orderPriceAbove;

    private Double orderPriceFollowing;

    private Double bankCharges;
    
    private String code;

    public String getCode() {
		return code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Double getOrderPriceAbove() {
        return orderPriceAbove;
    }

    public void setOrderPriceAbove(Double orderPriceAbove) {
        this.orderPriceAbove = orderPriceAbove;
    }

    public Double getOrderPriceFollowing() {
        return orderPriceFollowing;
    }

    public void setOrderPriceFollowing(Double orderPriceFollowing) {
        this.orderPriceFollowing = orderPriceFollowing;
    }

    public Double getBankCharges() {
        return bankCharges;
    }

    public void setBankCharges(Double bankCharges) {
        this.bankCharges = bankCharges;
    }
}