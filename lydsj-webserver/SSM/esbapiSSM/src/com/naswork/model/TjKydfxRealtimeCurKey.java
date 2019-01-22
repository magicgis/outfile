package com.naswork.model;

import java.util.Date;

public class TjKydfxRealtimeCurKey {
    private Integer id;

    private Date recorddatetime;

    private String sourcename;

    private Integer sourcescope;

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

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public Integer getSourcescope() {
        return sourcescope;
    }

    public void setSourcescope(Integer sourcescope) {
        this.sourcescope = sourcescope;
    }
}