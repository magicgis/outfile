package com.naswork.service;

import com.naswork.model.HistoricalQuotation;

public interface HistoricalQuotationService {
	 void insert(HistoricalQuotation record);

	    void insertSelective(HistoricalQuotation record);
	    
	    HistoricalQuotation findByBsn(String bsn);
	    
	    void updateByPrimaryKeySelective(HistoricalQuotation record);
}
