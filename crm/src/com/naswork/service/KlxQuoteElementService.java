package com.naswork.service;

import com.naswork.model.KlxQuoteElement;

public interface KlxQuoteElementService {
	
	int insertSelective(KlxQuoteElement record);

    KlxQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KlxQuoteElement record);
    
    public void createQuote();

}
