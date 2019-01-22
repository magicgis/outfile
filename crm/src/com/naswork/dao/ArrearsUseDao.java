package com.naswork.dao;

import java.util.List;

import com.naswork.model.ArrearsUse;

public interface ArrearsUseDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ArrearsUse record);

    int insertSelective(ArrearsUse record);

    List<ArrearsUse> selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArrearsUse record);

    int updateByPrimaryKey(ArrearsUse record);
    
   Double selectTotalBySupplierCode(String supplierCode);
   
   ArrearsUse selectByElementId(Integer importPackagePaymentElementId);
   
   void deleteByElementId(ArrearsUse record);
}