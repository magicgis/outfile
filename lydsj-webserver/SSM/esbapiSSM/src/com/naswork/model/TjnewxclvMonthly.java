package com.naswork.model;

import java.util.Date;

public class TjnewxclvMonthly extends TjnewxclvMonthlyKey {
    private Integer subscribercount;

    private Integer level;

    private Date inserttime;

    private Float trend;

    private String spotName;

    private Integer valueofcount;

    public Integer getValueofcount() {
        return valueofcount;
    }

    public void setValueofcount(Integer valueofcount) {
        this.valueofcount = valueofcount;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

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