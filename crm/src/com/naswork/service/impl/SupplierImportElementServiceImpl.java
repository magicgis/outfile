package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierImportElementDao;
import com.naswork.model.SupplierImportElement;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.service.SupplierImportElementService;
@Service("supplierImportElement")
public class SupplierImportElementServiceImpl implements SupplierImportElementService {

	@Resource
	private SupplierImportElementDao supplierImportElementDao;
	@Override
	public void insert(SupplierImportElement supplierImportElement) {
		supplierImportElementDao.insert(supplierImportElement);
	}
	@Override
	public void deleteByPrimaryKey(Integer key) {
		supplierImportElementDao.deleteByPrimaryKey(key);
	}
	@Override
	public List<SupplierInquiryStatistic> supplierImportStat(SupplierInquiryStatistic supplierInquiryStatistic) {
		return supplierImportElementDao.supplierImportStat( supplierInquiryStatistic);
	}

}
