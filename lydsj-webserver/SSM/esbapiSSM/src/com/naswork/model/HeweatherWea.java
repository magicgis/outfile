package com.naswork.model;

import java.util.Date;

/**
 * 
 * @ClassName:  HeweatherWea   
 * @Description:天气
 * @author: Li Zhenning 
 * @date:   2018-5-19 下午3:31:00   
 *
 */
public class HeweatherWea {
	private int id;
	private int area_id;
	private int fl;
	private int tmp;
	private int cond_code;
	private String cond_txt;
	private int wind_deg;
	private String wind_dir;
	private int wind_sc;
	private int wind_spd;
	private int hum;
	private int pcpn;
	private int pres;
	private int vis;
	private int cloud;
	private Date create_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFl() {
		return fl;
	}
	public void setFl(int fl) {
		this.fl = fl;
	}
	public int getTmp() {
		return tmp;
	}
	public void setTmp(int tmp) {
		this.tmp = tmp;
	}
	public int getCond_code() {
		return cond_code;
	}
	public void setCond_code(int cond_code) {
		this.cond_code = cond_code;
	}
	public String getCond_txt() {
		return cond_txt;
	}
	public void setCond_txt(String cond_txt) {
		this.cond_txt = cond_txt;
	}
	public int getWind_deg() {
		return wind_deg;
	}
	public void setWind_deg(int wind_deg) {
		this.wind_deg = wind_deg;
	}
	public String getWind_dir() {
		return wind_dir;
	}
	public void setWind_dir(String wind_dir) {
		this.wind_dir = wind_dir;
	}
	public int getWind_sc() {
		return wind_sc;
	}
	public void setWind_sc(int wind_sc) {
		this.wind_sc = wind_sc;
	}
	public int getWind_spd() {
		return wind_spd;
	}
	public void setWind_spd(int wind_spd) {
		this.wind_spd = wind_spd;
	}
	public int getHum() {
		return hum;
	}
	public void setHum(int hum) {
		this.hum = hum;
	}
	public int getPcpn() {
		return pcpn;
	}
	public void setPcpn(int pcpn) {
		this.pcpn = pcpn;
	}
	public int getPres() {
		return pres;
	}
	public void setPres(int pres) {
		this.pres = pres;
	}
	public int getVis() {
		return vis;
	}
	public void setVis(int vis) {
		this.vis = vis;
	}
	public int getCloud() {
		return cloud;
	}
	public void setCloud(int cloud) {
		this.cloud = cloud;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	
	
}
