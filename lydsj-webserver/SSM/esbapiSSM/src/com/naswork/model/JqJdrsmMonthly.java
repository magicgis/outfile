package com.naswork.model;

import java.util.Date;

public class JqJdrsmMonthly {
    private Integer id;

    private Date statsdate;

    private Integer jdsr;

    private Integer gtrs;

    private Integer skrs;

    private String recordidentifier;

    private Date inserttime;
    private Integer gtbl;
    private Integer skbl;
    

    public Integer getGtbl() {
		return gtbl;
	}

	public void setGtbl(Integer gtbl) {
		this.gtbl = gtbl;
	}

	public Integer getSkbl() {
		return skbl;
	}

	public void setSkbl(Integer skbl) {
		this.skbl = skbl;
	}

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

    public Integer getJdsr() {
        return jdsr;
    }

    public void setJdsr(Integer jdsr) {
        this.jdsr = jdsr;
    }

    public Integer getGtrs() {
        return gtrs;
    }

    public void setGtrs(Integer gtrs) {
        this.gtrs = gtrs;
    }

    public Integer getSkrs() {
        return skrs;
    }

    public void setSkrs(Integer skrs) {
        this.skrs = skrs;
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