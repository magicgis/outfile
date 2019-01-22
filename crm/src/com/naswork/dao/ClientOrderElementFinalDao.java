package com.naswork.dao;

import com.naswork.model.ClientOrderElementFinal;

public interface ClientOrderElementFinalDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientOrderElementFinal record);

    int insertSelective(ClientOrderElementFinal record);

    ClientOrderElementFinal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientOrderElementFinal record);

    int updateByPrimaryKey(ClientOrderElementFinal record);
    
    Double sumPrice(Integer clientWeatherOrderId);
    
    void updateBankCharges(ClientOrderElementFinal record);
    
    void updateStatus(ClientOrderElementFinal record);
}