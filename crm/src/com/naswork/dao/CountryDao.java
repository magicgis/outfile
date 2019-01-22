package com.naswork.dao;

import java.util.List;

import com.naswork.model.Country;

public interface CountryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Country record);

    int insertSelective(Country record);

    Country selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Country record);

    int updateByPrimaryKey(Country record);
    
    public List<Country> getList();
}