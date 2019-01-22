package com.naswork.model;

import java.util.Date;

public class ExchangeRate {
    private Integer currencyId;

    private Double rate;
    
    private Double transferRange;

    private Date updateTimestamp;
    
    private Double relativeRate;

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public Double getTransferRange() {
		return transferRange;
	}

	public void setTransferRange(Double transferRange) {
		this.transferRange = transferRange;
	}

	/**
	 * @return the relativeRate
	 */
	public Double getRelativeRate() {
		return relativeRate;
	}

	/**
	 * @param relativeRate the relativeRate to set
	 */
	public void setRelativeRate(Double relativeRate) {
		this.relativeRate = relativeRate;
	}
}