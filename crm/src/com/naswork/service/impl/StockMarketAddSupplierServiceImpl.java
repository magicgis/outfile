package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.StockMarketAddSupplierDao;
import com.naswork.model.StockMarketAddSupplier;
import com.naswork.service.StockMarketAddSupplierService;

@Service("stockMarketAddSupplierService")
public class StockMarketAddSupplierServiceImpl implements
		StockMarketAddSupplierService {

	@Resource
	private StockMarketAddSupplierDao stockMarketAddSupplierDao;
	
	@Override
	public void insertSelective(StockMarketAddSupplier record) {
		stockMarketAddSupplierDao.insertSelective(record);
	}

	@Override
	public StockMarketAddSupplier selectByPrimaryKey(Integer id) {
		return stockMarketAddSupplierDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(StockMarketAddSupplier record) {
		stockMarketAddSupplierDao.updateByPrimaryKeySelective(record);
	}
	
	public void deleteByPrimaryKey(Integer id){
		stockMarketAddSupplierDao.deleteByPrimaryKey(id);
	}

}
