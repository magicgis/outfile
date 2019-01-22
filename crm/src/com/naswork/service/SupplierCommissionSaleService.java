package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierCommissionSale;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierCommissionSaleService {
	
    void insertSelective(SupplierCommissionSale record);

    SupplierCommissionSale selectByPrimaryKey(Integer id);

    public void updateByPrimaryKeySelective(SupplierCommissionSale record);
    
    public void listPage(PageModel<SupplierCommissionSale> page,String where,GridSort sort);
    
    public List<SupplierCommissionSale> getCrawlStockList();

}
