package com.naswork.service;

import com.naswork.model.StockMarketAddSupplier;

public interface StockMarketAddSupplierService {

	public void insertSelective(StockMarketAddSupplier record);

	public StockMarketAddSupplier selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(StockMarketAddSupplier record);
	
	public void deleteByPrimaryKey(Integer id);
	
}
