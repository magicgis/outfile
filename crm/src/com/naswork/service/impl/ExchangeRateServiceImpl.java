package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ExchangeRateDao;
import com.naswork.model.ExchangeRate;
import com.naswork.module.system.controller.exchangerate.ExchangeRateVo;
import com.naswork.service.ExchangeRateService;
import com.naswork.vo.PageModel;

@Service("exchangeRateService")
public class ExchangeRateServiceImpl implements ExchangeRateService {

	@Resource
	private ExchangeRateDao exchangeRateDao;
	
	@Override
	public int insertSelective(ExchangeRate record) {
		return exchangeRateDao.insertSelective(record);
	}

	@Override
	public ExchangeRate selectByPrimaryKey(Integer currencyId) {
		return exchangeRateDao.selectByPrimaryKey(currencyId);
	}

	@Override
	public int updateByPrimaryKeySelective(ExchangeRate record) {
		return exchangeRateDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<ExchangeRateVo> page){
		page.setEntities(exchangeRateDao.listPage(page));
	}
	
	public ExchangeRateVo findById(Integer id){
		return exchangeRateDao.findById(id);
	}

}
