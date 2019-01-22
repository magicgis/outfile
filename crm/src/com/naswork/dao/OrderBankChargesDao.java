package com.naswork.dao;

import java.util.List;

import com.naswork.model.OrderBankCharges;
import com.naswork.model.QuoteBankCharges;
import com.naswork.vo.PageModel;

public interface OrderBankChargesDao {
    int insert(OrderBankCharges record);

    int insertSelective(OrderBankCharges record);
    
    public List<OrderBankCharges> orderBankChargesPage(PageModel<OrderBankCharges> page);
    
    void updateByPrimaryKey(OrderBankCharges record);

    List<OrderBankCharges> orderBankChargesByClientId(String clientId);
}