package com.naswork.dao;


import com.naswork.model.HeweatherAir;
import com.naswork.model.HeweatherWea;


public interface HeweatherDao {

	public void insertWea(HeweatherWea heweatherWea);
	
	public void insertAir(HeweatherAir heweatherAir);
}
