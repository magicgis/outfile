package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierAirRelationDao;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.service.SupplierAirRelationService;
import com.naswork.vo.PageModel;
@Service("supplierAirRelationServiceImpl")
public class SupplierAirRelationServiceImpl implements SupplierAirRelationService {
	@Resource
	private SupplierAirRelationDao supplierAirRelationDao;
	
	@Override
	public void deleteByPrimaryKey(SupplierAirRelationKey key) {
		supplierAirRelationDao.deleteByPrimaryKey(key);
	}

	@Override
	public void insert(SupplierAirRelationKey record) {
		supplierAirRelationDao.insert(record);
	}

	@Override
	public void insertSelective(SupplierAirRelationKey record) {
		supplierAirRelationDao.insertSelective(record);
	}

	@Override
	public void selectBySupplierIdPage(PageModel<SupplierAirRelationKey> page) {
		page.setEntities(supplierAirRelationDao.selectBySupplierIdPage(page));
	}

	@Override
	public SupplierAirRelationKey selectBySupplierIdAndAirId(SupplierAirRelationKey supplierAirRelationKey) {
		return supplierAirRelationDao.selectBySupplierIdAndAirId(supplierAirRelationKey);
	}

	@Override
	public void selectByAirIdPage(PageModel<SupplierAirRelationKey> page) {
		page.setEntities(supplierAirRelationDao.selectByAirIdPage(page));
	}

}
