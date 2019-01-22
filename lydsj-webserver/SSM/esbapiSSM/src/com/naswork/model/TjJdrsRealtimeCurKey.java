package com.naswork.model;

import java.util.Date;

public class TjJdrsRealtimeCurKey {
    private Integer id;

    private Date recorddatetime;

    private String recordidentifier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRecorddatetime() {
        return recorddatetime;
    }

    public void setRecorddatetime(Date recorddatetime) {
        this.recorddatetime = recorddatetime;
    }

    public String getRecordidentifier() {
        return recordidentifier;
    }

    public void setRecordidentifier(String recordidentifier) {
        this.recordidentifier = recordidentifier;
    }
}