package com.naswork.dao;

import java.util.List;

import org.jbpm.pvm.internal.query.Page;

import com.naswork.model.SupplierCommissionForStockmarket;
import com.naswork.vo.PageModel;

public interface SupplierCommissionForStockmarketDao {
	int deleteByPrimaryKey(Integer id);

	int insert(SupplierCommissionForStockmarket record);

	int insertSelective(SupplierCommissionForStockmarket record);

	SupplierCommissionForStockmarket selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SupplierCommissionForStockmarket record);

	int updateByPrimaryKey(SupplierCommissionForStockmarket record);
	
	public List<SupplierCommissionForStockmarket> listPage(PageModel<SupplierCommissionForStockmarket> page);
	
	public List<SupplierCommissionForStockmarket> getCrawlStockList();
}