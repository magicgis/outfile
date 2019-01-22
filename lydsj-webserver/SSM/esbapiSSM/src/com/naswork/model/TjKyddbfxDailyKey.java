package com.naswork.model;

import java.util.Date;

public class TjKyddbfxDailyKey {
    private Integer id;

    private Date recorddate;

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

    public Integer getSourcescope() {
        return sourcescope;
    }

    public void setSourcescope(Integer sourcescope) {
        this.sourcescope = sourcescope;
    }
}