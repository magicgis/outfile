package com.naswork.model;

public class QuoteBankCharges {
	private String code;
	
    private Integer clientId;

    private Double bankCharges;

    public String getCode() {
		return code;
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

    public Double getBankCharges() {
        return bankCharges;
    }

    public void setBankCharges(Double bankCharges) {
        this.bankCharges = bankCharges;
    }
}