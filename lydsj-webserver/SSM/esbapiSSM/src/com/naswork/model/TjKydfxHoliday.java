package com.naswork.model;

import java.util.Date;

/**
 * 
 * @ClassName:  TjKydfxHoliday   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: Li Zhenning 
 * @date:   2018-5-17 下午2:46:05   
 *
 */
public class TjKydfxHoliday {
	private int id;
	private int holidayYear;
	private int holidayType;
	private String sourceName;
	private int subscriberCount;
	private int sourceScope;
	private int level;
	private Date insertTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHolidayYear() {
		return holidayYear;
	}
	public void setHolidayYear(int holidayYear) {
		this.holidayYear = holidayYear;
	}
	public int getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(int holidayType) {
		this.holidayType = holidayType;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public int getSubscriberCount() {
		return subscriberCount;
	}
	public void setSubscriberCount(int subscriberCount) {
		this.subscriberCount = subscriberCount;
	}
	public int getSourceScope() {
		return sourceScope;
	}
	public void setSourceScope(int sourceScope) {
		this.sourceScope = sourceScope;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	
}
