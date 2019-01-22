package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

public class TjCblrdbfxMonthly implements Serializable{
    /**
	*@Field
	* TODO
	*/
	private static final long serialVersionUID = 873735631232652641L;

	private Integer id;

    private Float cost;

    private Float profit;

    private Integer recordMonth;

    private Integer recordYear;

    private Integer level;

    private Date insertTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Float getProfit() {
		return profit;
	}

	public void setProfit(Float profit) {
		this.profit = profit;
	}

	public Integer getRecordMonth() {
		return recordMonth;
	}

	public void setRecordMonth(Integer recordMonth) {
		this.recordMonth = recordMonth;
	}

	public Integer getRecordYear() {
		return recordYear;
	}

	public void setRecordYear(Integer recordYear) {
		this.recordYear = recordYear;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

   
}