package com.naswork.module.system.controller.exchangerate;

import java.util.Date;

public class ExchangeRateVo {

	private Integer id;
	
	private String value;
	
	private Double rate;
	
	private Double transferRange;
	
	private Date updateTimestamp;
	
	private Double relativeRate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
