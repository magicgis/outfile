package com.naswork.model;

import java.util.Date;

public class TjLmlyrsMonthly extends TjLmlyrsMonthlyKey {
    private Integer subscribercount;

    private Integer level;

    private Date inserttime;

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
}