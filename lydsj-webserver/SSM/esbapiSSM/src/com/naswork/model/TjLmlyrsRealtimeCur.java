package com.naswork.model;

import java.util.Date;

public class TjLmlyrsRealtimeCur extends TjLmlyrsRealtimeCurKey {
    private Integer subscribercount;

    private Integer level;

    private String recordidentifier;

    private Date inserttime;

    private Float trend;

    public Integer getSubscribercount() {
        return subscribercount;
    }

    public void setSubscribercount(Integer subscribercount) {
        this.subscribercount = subscribercount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public Float getTrend() {
        return trend;
    }

    public void setTrend(Float trend) {
        this.trend = trend;
    }
}