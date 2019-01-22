package com.naswork.model;

import java.util.Date;

public class TjnewjdrcRealtime extends TjnewjdrcRealtimeKey {
    private Integer subscribercount;

    private Integer level;

    private Date inserttime;

    private String latest;

    private Float trend;
    
    private String second;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
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