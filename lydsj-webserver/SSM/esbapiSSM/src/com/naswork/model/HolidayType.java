package com.naswork.model;

/**
 * 
 * @ClassName:  HolidayType   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: Li Zhenning 
 * @date:   2018-5-17 下午2:44:32   
 *
 */
public class HolidayType {
	private int holidayTypeId;
	private String holidayTypeName;
	private char enable;
	
	
	public int getHolidayTypeId() {
		return holidayTypeId;
	}
	public void setHolidayTypeId(int holidayTypeId) {
		this.holidayTypeId = holidayTypeId;
	}
	public String getHolidayTypeName() {
		return holidayTypeName;
	}
	public void setHolidayTypeName(String holidayTypeName) {
		this.holidayTypeName = holidayTypeName;
	}
	public char getEnable() {
		return enable;
	}
	public void setEnable(char enable) {
		this.enable = enable;
	}
	
	
}
