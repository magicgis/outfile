package com.naswork.model;

import java.util.Date;

public class TjnewhslvMonthly extends TjnewhslvMonthlyKey {
    private Integer subscribercount;

    private Integer level;

    private Date inserttime;

    private Float trend;

    private Integer sumofmonth;

    private Integer sumofyear;

    private Integer sumofcountymonth;

    private String spotName;

    private Integer rankNum;

    public Integer getRankNum() {
        return rankNum;
    }

    public void setRankNum(Integer rankNum) {
        this.rankNum = rankNum;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public Integer getSumofcountymonth() {
        return sumofcountymonth;
    }

    public void setSumofcountymonth(Integer sumofcountymonth) {
        this.sumofcountymonth = sumofcountymonth;
    }

    public Integer getSumofyear() {
        return sumofyear;
    }

    public void setSumofyear(Integer sumofyear) {
        this.sumofyear = sumofyear;
    }

    public Integer getSumofmonth() {
        return sumofmonth;
    }

    public void setSumofmonth(Integer sumofmonth) {
        this.sumofmonth = sumofmonth;
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