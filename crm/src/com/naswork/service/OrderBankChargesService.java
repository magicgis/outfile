package com.naswork.service;

import java.util.List;

import com.naswork.model.OrderBankCharges;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface OrderBankChargesService {
    int insert(OrderBankCharges record);

    int insertSelective(OrderBankCharges record);
    
    public void orderBankChargesPage(PageModel<OrderBankCharges> page, String where,
			GridSort sort);
    
    void updateByPrimaryKey(OrderBankCharges record);
    
    List<OrderBankCharges> orderBankChargesByClientId(String clientId);
}
