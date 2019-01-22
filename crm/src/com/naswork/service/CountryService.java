package com.naswork.service;

import java.util.List;

import com.naswork.model.Country;

public interface CountryService {

	public void insertSelective(Country record);

	public Country selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(Country record);
    
    public List<Country> getList();
	
}
