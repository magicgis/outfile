package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.CountryDao;
import com.naswork.model.Country;
import com.naswork.service.CountryService;

@Service("countryService")
public class CountryServiceImpl implements CountryService {

	@Resource
	private CountryDao countryDao;
	
	@Override
	public void insertSelective(Country record) {
		countryDao.insertSelective(record);
	}

	@Override
	public Country selectByPrimaryKey(Integer id) {
		return countryDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(Country record) {
		countryDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Country> getList() {
		return countryDao.getList();
	}

}
