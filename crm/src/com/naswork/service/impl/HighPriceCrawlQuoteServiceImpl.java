package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.HighPriceCrawlQuoteDao;
import com.naswork.model.HighPriceCrawlQuote;
import com.naswork.service.HighPriceCrawlQuoteService;

@Service("highPriceCrawlQuoteService")
public class HighPriceCrawlQuoteServiceImpl implements
		HighPriceCrawlQuoteService {
	
	@Resource
	private HighPriceCrawlQuoteDao highPriceCrawlQuoteDao;
	
	@Override
	public void insertSelective(HighPriceCrawlQuote record) {
		highPriceCrawlQuoteDao.insertSelective(record);
	}

	@Override
	public HighPriceCrawlQuote selectByPrimaryKey(Integer id) {
		return highPriceCrawlQuoteDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(HighPriceCrawlQuote record) {
		highPriceCrawlQuoteDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<HighPriceCrawlQuote> getUnFinishList() {
		return highPriceCrawlQuoteDao.getUnFinishList();
	}
	
	public List<HighPriceCrawlQuote> getBySupplierQuoteId(Integer supplieQuoteId){
		return highPriceCrawlQuoteDao.getBySupplierQuoteId(supplieQuoteId);
	}

}
