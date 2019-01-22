package com.naswork.service;

import com.naswork.model.StockMarketSupplierMap;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface StockMarketSupplierMapService {

	public void insertSelective(StockMarketSupplierMap record);

    public StockMarketSupplierMap selectByPrimaryKey(Integer id);

    public void updateByPrimaryKeySelective(StockMarketSupplierMap record);
    
    public void listPage(PageModel<StockMarketSupplierMap> page,GridSort sort);
    
    public void deleteByPrimaryKey(Integer id);
    
    public StockMarketSupplierMap checkRecord(StockMarketSupplierMap stockMarketSupplierMap);
	
}
