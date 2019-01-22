package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

public class TjSrfxMonthly implements Serializable{
    /**
	*@Field
	* TODO
	*/
	private static final long serialVersionUID = -1090484901602347517L;

	private Integer id;

    private Integer recordYear;

    private Integer recordMonth;

    private Float income;

    private Integer level;

    private Date insertTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecordYear() {
		return recordYear;
	}

	public void setRecordYear(Integer recordYear) {
		this.recordYear = recordYear;
	}

	public Integer getRecordMonth() {
		return recordMonth;
	}

	public void setRecordMonth(Integer recordMonth) {
		this.recordMonth = recordMonth;
	}

	public Float getIncome() {
		return income;
	}

	public void setIncome(Float income) {
		this.income = income;
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