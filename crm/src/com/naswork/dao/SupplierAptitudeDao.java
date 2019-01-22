package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierAptitude;
import com.naswork.vo.PageModel;

public interface SupplierAptitudeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierAptitude record);

    int insertSelective(SupplierAptitude record);

    SupplierAptitude selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierAptitude record);

    int updateByPrimaryKey(SupplierAptitude record);
    
    public List<SupplierAptitude> listPage(PageModel<SupplierAptitude> page);
    
    public List<SupplierAptitude> selectBySupplierQuoteElementId(Integer supplierQuoteElementId);
}