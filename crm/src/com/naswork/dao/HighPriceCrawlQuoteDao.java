package com.naswork.dao;

import java.util.List;

import com.naswork.model.HighPriceCrawlQuote;

public interface HighPriceCrawlQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(HighPriceCrawlQuote record);

    int insertSelective(HighPriceCrawlQuote record);

    HighPriceCrawlQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HighPriceCrawlQuote record);

    int updateByPrimaryKey(HighPriceCrawlQuote record);
    
    public List<HighPriceCrawlQuote> getUnFinishList();
    
    public List<HighPriceCrawlQuote> getBySupplierQuoteId(Integer supplieQuoteId);
}