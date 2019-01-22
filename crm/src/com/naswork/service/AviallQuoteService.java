package com.naswork.service;

import java.util.List;

import com.naswork.model.AviallQuote;

public interface AviallQuoteService {

	int insertSelective(AviallQuote record);

    AviallQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AviallQuote record);
    
    public void createQuote();
}
