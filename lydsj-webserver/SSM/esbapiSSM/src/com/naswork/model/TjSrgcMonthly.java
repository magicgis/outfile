package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

public class TjSrgcMonthly implements Serializable{
    /**
	*@Field
	* TODO
	*/
	private static final long serialVersionUID = 6332982060507025313L;

	private Integer id;

    private Integer recordYear;

    private Integer recordMonth;

    private Float income;

    private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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