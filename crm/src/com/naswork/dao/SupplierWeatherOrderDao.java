package com.naswork.dao;

import java.util.List;

import org.jbpm.pvm.internal.query.Page;

import com.naswork.model.SupplierWeatherOrder;
import com.naswork.vo.PageModel;

public interface SupplierWeatherOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierWeatherOrder record);

    int insertSelective(SupplierWeatherOrder record);

    SupplierWeatherOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierWeatherOrder record);

    int updateByPrimaryKey(SupplierWeatherOrder record);
    
    public List<SupplierWeatherOrder> selectByOrderIdAndSupplier(Integer orderId,Integer supplierId);
    
    public List<SupplierWeatherOrder> getFeeInfo(PageModel<SupplierWeatherOrder> page);
    
    public SupplierWeatherOrder getByOrderIdAndSupplier(Integer clientWeatherOrderId,Integer supplierId);
}