package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.HistoricalQuotationDao;
import com.naswork.model.HistoricalQuotation;
import com.naswork.service.HistoricalQuotationService;

@Service("historicalQuotationService")
public class HistoricalQuotationServiceImpl implements HistoricalQuotationService {

	@Resource
	private HistoricalQuotationDao historicalQuotationDao;
	@Override
	public void insert(HistoricalQuotation record) {
		historicalQuotationDao.insert(record);
	}

	@Override
	public void insertSelective(HistoricalQuotation record) {
		historicalQuotationDao.insertSelective(record);
	}

	@Override
	public HistoricalQuotation findByBsn(String bsn) {
		return historicalQuotationDao.findByBsn(bsn);
	}

	@Override
	public void updateByPrimaryKeySelective(HistoricalQuotation record) {
		historicalQuotationDao.updateByPrimaryKeySelective(record);		
	}

}
