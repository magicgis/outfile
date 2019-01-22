package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierCommissionForStockmarket;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierCommissionForStockmarketService {

	public void insertSelective(SupplierCommissionForStockmarket record);

	public SupplierCommissionForStockmarket selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(SupplierCommissionForStockmarket record);
	
	public void listPage(PageModel<SupplierCommissionForStockmarket> page,GridSort sort);
	
	public List<SupplierCommissionForStockmarket> getCrawlStockList();
	
}
