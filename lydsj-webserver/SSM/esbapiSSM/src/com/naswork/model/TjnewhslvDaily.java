package com.naswork.model;

import java.util.Date;

public class TjnewhslvDaily extends TjnewhslvDailyKey {
    private Integer subscribercount;

    private Integer level;

    private Date inserttime;

    private Float trend;

    private Integer sumofday;

    private Integer day;

    public Integer getSumofday() {
        return sumofday;
    }

    public void setSumofday(Integer sumofday) {
        this.sumofday = sumofday;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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