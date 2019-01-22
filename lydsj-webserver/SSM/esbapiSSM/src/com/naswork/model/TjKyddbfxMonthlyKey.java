package com.naswork.model;

import java.util.List;

public class TjKyddbfxMonthlyKey {
    private Integer id;

    private Integer recordyear;

    private Integer recordmonth;

    private Integer sourcescope;
    
    List<Integer> monthlist;

    public List<Integer> getMonthlist() {
		return monthlist;
	}

	public void setMonthlist(List<Integer> monthlist) {
		this.monthlist = monthlist;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordyear() {
        return recordyear;
    }

    public void setRecordyear(Integer recordyear) {
        this.recordyear = recordyear;
    }

    public Integer getRecordmonth() {
        return recordmonth;
    }

    public void setRecordmonth(Integer recordmonth) {
        this.recordmonth = recordmonth;
    }

    public Integer getSourcescope() {
        return sourcescope;
    }

    public void setSourcescope(Integer sourcescope) {
        this.sourcescope = sourcescope;
    }
}