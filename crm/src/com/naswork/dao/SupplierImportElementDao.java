package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierImportElement;
import com.naswork.model.SupplierImportElementKey;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;

public interface SupplierImportElementDao {
    int deleteByPrimaryKey(Integer key);

    int insert(SupplierImportElement record);

    int insertSelective(SupplierImportElement record);

    SupplierImportElement selectByPrimaryKey(SupplierImportElementKey key);

    int updateByPrimaryKeySelective(SupplierImportElement record);

    int updateByPrimaryKey(SupplierImportElement record);
    
    List<SupplierInquiryStatistic> supplierImportStat(SupplierInquiryStatistic supplierInquiryStatistic);
    
    public List<SupplierImportElement> selectBySupplierOrderId(Integer supplierOrderId);
    
    public Integer getClientByImportElementId(Integer id);
}