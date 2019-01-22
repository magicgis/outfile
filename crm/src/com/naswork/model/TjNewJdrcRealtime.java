package com.naswork.model;

import java.util.Date;

public class TjNewJdrcRealtime extends TjNewJdrcRealtimeKey {
    private Integer subscribercount;

    private Integer level;

    private Date inserttime;

    private String latest;

    private Float trend;

    private String spotname;

    public String getSpotname() {
        return spotname;
    }

    public void setSpotname(String spotname) {
        this.spotname = spotname;
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

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public Float getTrend() {
        return trend;
    }

    public void setTrend(Float trend) {
        this.trend = trend;
    }
}