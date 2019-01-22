package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierDebt;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierDebtService {

    int insertSelective(SupplierDebt record);

    SupplierDebt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierDebt record);
    
    public void listPage(PageModel<SupplierDebt> page,String where,GridSort sort);
    
    SupplierDebt totalArrears(String supplierCode);
    
    List<SupplierDebt>dataBySupplierCode(String supplierCode);
	
}
