package com.naswork.model;

import java.util.Date;

public class JqIncomeMonthly {
    private Integer id;

    private Date statsdate;

    private Double income;

    private String recordidentifier;

    private Date inserttime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStatsdate() {
        return statsdate;
    }

    public void setStatsdate(Date statsdate) {
        this.statsdate = statsdate;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public String getRecordidentifier() {
        return recordidentifier;
    }

    public void setRecordidentifier(String recordidentifier) {
        this.recordidentifier = recordidentifier;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }
}