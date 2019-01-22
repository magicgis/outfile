package com.naswork.dao;

import java.util.List;

import com.naswork.model.OrderBankCharges;
import com.naswork.model.QuoteBankCharges;
import com.naswork.vo.PageModel;

public interface QuoteBankChargesdDao {
    int insert(QuoteBankCharges record);

    int insertSelective(QuoteBankCharges record);
    
    public List<QuoteBankCharges> quoteBankChargesPage(PageModel<QuoteBankCharges> page);
    
    QuoteBankCharges findByClientId(Integer clientId);
    
    void updateByPrimaryKey(QuoteBankCharges record);
}