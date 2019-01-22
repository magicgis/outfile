package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;

public interface SupplierWeatherOrderElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierWeatherOrderElement record);

    int insertSelective(SupplierWeatherOrderElement record);

    SupplierWeatherOrderElement selectByPrimaryKey(Integer id);
    
    List<SupplierWeatherOrderElement> selectByClientOrderElementId(Integer clientOrderElementId);
    
    SupplierWeatherOrderElement selectByCoeIdAndSqeId(Integer clientOrderElementId,Integer SupplierQuoteElementId);

    int updateByPrimaryKeySelective(SupplierWeatherOrderElement record);

    int updateByPrimaryKey(SupplierWeatherOrderElement record);
    
    ClientOrderElementVo findById(Integer id);
    
    public Double getAmontBySupplier(Integer clientWeatherOrderId,Integer supplierId);
    
    public Double getAmountByClientOrder(Integer clientWeatherOrderElementId);
}