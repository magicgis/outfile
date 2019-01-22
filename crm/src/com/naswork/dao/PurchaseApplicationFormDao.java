package com.naswork.dao;

import com.naswork.model.PurchaseApplicationForm;

public interface PurchaseApplicationFormDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PurchaseApplicationForm record);

    int insertSelective(PurchaseApplicationForm record);

    PurchaseApplicationForm selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PurchaseApplicationForm record);

    int updateByPrimaryKey(PurchaseApplicationForm record);
    
    public PurchaseApplicationForm findByClientOrderId(Integer clientOrderId);
}