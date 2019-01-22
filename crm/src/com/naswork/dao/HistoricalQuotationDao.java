package com.naswork.dao;

import com.naswork.model.HistoricalQuotation;

public interface HistoricalQuotationDao {
    void insert(HistoricalQuotation record);

    void insertSelective(HistoricalQuotation record);
    
    HistoricalQuotation findByBsn(String bsn);
    
    void updateByPrimaryKeySelective(HistoricalQuotation record);
}