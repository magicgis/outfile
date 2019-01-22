package com.naswork.dao;

import com.naswork.model.StockMarketAddSupplier;

public interface StockMarketAddSupplierDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StockMarketAddSupplier record);

    int insertSelective(StockMarketAddSupplier record);

    StockMarketAddSupplier selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockMarketAddSupplier record);

    int updateByPrimaryKey(StockMarketAddSupplier record);
}