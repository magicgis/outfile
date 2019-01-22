package com.naswork.dao;



import java.util.List;

import com.naswork.model.SupplierInquiryElement;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;

public interface SupplierInquiryElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierInquiryElement record);

    int insertSelective(SupplierInquiryElement record);

    SupplierInquiryElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierInquiryElement record);

    int updateByPrimaryKey(SupplierInquiryElement record);
    
    SupplierInquiryElement  selectInquiryByElementId(Integer elementId,Integer supplierId,Integer clientInquiryId);
    
    SupplierInquiryElement findSupplierInquiryElement(SupplierInquiryElement record);
    
    SupplierInquiryElement findByElementIdAndMian(Integer elementId,Integer supplierInquiryId);
    
    public List<SupplierInquiryElement> getList(Integer clientInquiryElement,Integer supplierInquiryId);
    
    public List<SupplierInquiryElement> getListByInquiryElementId(Integer clientInquiryElementId);
}