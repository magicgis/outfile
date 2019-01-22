package com.naswork.model;

import java.util.Date;

public class TjCmrsDailyKey {
    private Date recorddate;

    private Integer id;

    private String recordidentifier;

    public Date getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(Date recorddate) {
        this.recorddate = recorddate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecordidentifier() {
        return recordidentifier;
    }

    public void setRecordidentifier(String recordidentifier) {
        this.recordidentifier = recordidentifier;
    }
}