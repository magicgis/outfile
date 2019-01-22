package com.naswork.service;

import java.util.List;

import com.naswork.model.HighPriceCrawlQuote;

public interface HighPriceCrawlQuoteService {

	public void insertSelective(HighPriceCrawlQuote record);

	public HighPriceCrawlQuote selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(HighPriceCrawlQuote record);
	
	public List<HighPriceCrawlQuote> getUnFinishList();
	
	public List<HighPriceCrawlQuote> getBySupplierQuoteId(Integer supplieQuoteId);
	
}
