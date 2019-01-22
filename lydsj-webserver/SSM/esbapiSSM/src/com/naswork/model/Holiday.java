package com.naswork.model;

import java.util.Date;

/**
 * 
 * @ClassName:  Holiday   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: Li Zhenning 
 * @date:   2018-5-17 下午2:44:40   
 *
 */
public class Holiday {
	private int hid;
	private int holidayType;
	private int holidayYear;
	private Date holidayStartDate;
	private Date holidayEndDate;
	
	
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(int holidayType) {
		this.holidayType = holidayType;
	}
	public int getHolidayYear() {
		return holidayYear;
	}
	public void setHolidayYear(int holidayYear) {
		this.holidayYear = holidayYear;
	}
	public Date getHolidayStartDate() {
		return holidayStartDate;
	}
	public void setHolidayStartDate(Date holidayStartDate) {
		this.holidayStartDate = holidayStartDate;
	}
	public Date getHolidayEndDate() {
		return holidayEndDate;
	}
	public void setHolidayEndDate(Date holidayEndDate) {
		this.holidayEndDate = holidayEndDate;
	}
	
	
}
