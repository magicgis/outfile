package com.naswork.model;

import java.util.Date;

public class TjKydfxDailyKey {
    private Integer id;

    private Date recorddate;

    private String sourcename;

    private Integer sourcescope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(Date recorddate) {
        this.recorddate = recorddate;
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