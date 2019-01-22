package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierDebt;
import com.naswork.vo.PageModel;

public interface SupplierDebtDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierDebt record);

    int insertSelective(SupplierDebt record);

    SupplierDebt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierDebt record);

    int updateByPrimaryKey(SupplierDebt record);
    
    public List<SupplierDebt> listPage(PageModel<SupplierDebt> page);
    
    SupplierDebt totalArrears(String supplierCode);
    
    List<SupplierDebt>dataBySupplierCode(String supplierCode);
}