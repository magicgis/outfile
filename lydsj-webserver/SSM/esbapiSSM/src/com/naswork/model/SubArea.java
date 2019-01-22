package com.naswork.model;

import java.math.BigDecimal;
import java.util.List;

public class SubArea {
 private Integer id;
 
 private List<BigDecimal> disPos;
 
 private String indexCode;
 
 private String name;
 
 private Integer alarm;
 
 private Integer num;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public List<BigDecimal> getDisPos() {
	return disPos;
}

public void setDisPos(List<BigDecimal> disPos) {
	this.disPos = disPos;
}

public String getIndexCode() {
	return indexCode;
}

public void setIndexCode(String indexCode) {
	this.indexCode = indexCode;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Integer getAlarm() {
	return alarm;
}

public void setAlarm(Integer alarm) {
	this.alarm = alarm;
}

public Integer getNum() {
	return num;
}

public void setNum(Integer num) {
	this.num = num;
}
}
