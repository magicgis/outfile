package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.SupplierPaymentDao;
import com.naswork.model.ExchangeRate;
import com.naswork.model.SupplierPayment;
import com.naswork.service.SupplierPaymenService;
@Service("supplierPaymenServiceImpl")
public class SupplierPaymenServiceImpl implements SupplierPaymenService {

	@Resource
	private SupplierPaymentDao supplierPaymentDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Override
	public void deleteByPrimaryKey(Integer id) {
		supplierPaymentDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(SupplierPayment record) {
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
		record.setExchangeRate(exchangeRate.getRate());
		supplierPaymentDao.insert(record);
	}

	@Override
	public void insertSelective(SupplierPayment record) {
		supplierPaymentDao.insertSelective(record);
	}

	@Override
	public SupplierPayment selectByPrimaryKey(Integer id) {
		return supplierPaymentDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierPayment record) {
		supplierPaymentDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void updateByPrimaryKey(SupplierPayment record) {
		supplierPaymentDao.updateByPrimaryKey(record);
	}

}
