package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierPartTypeRelationDao;
import com.naswork.model.SupplierPartTypeRelationKey;
import com.naswork.service.SupplierPartTypeRelationService;
import com.naswork.vo.PageModel;

@Service("supplierPartTypeRelationService")
public class SupplierPartTypeRelationServiceImpl implements
		SupplierPartTypeRelationService {

	@Resource
	private SupplierPartTypeRelationDao supplierPartTypeRelationDao;
	
	@Override
	public int deleteByPrimaryKey(SupplierPartTypeRelationKey key) {
		return supplierPartTypeRelationDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(SupplierPartTypeRelationKey record) {
		return supplierPartTypeRelationDao.insert(record);
	}

	@Override
	public int insertSelective(SupplierPartTypeRelationKey record) {
		return supplierPartTypeRelationDao.insertSelective(record);
	}

	@Override
	public void selectByPartPage(
			PageModel<SupplierPartTypeRelationKey> page) {
		page.setEntities(supplierPartTypeRelationDao.selectByPartPage(page));
	}
	
	public SupplierPartTypeRelationKey selectBySupplierIdAndAirId(SupplierPartTypeRelationKey supplierPartTypeRelationKey){
		return supplierPartTypeRelationDao.selectBySupplierIdAndAirId(supplierPartTypeRelationKey);
	}

}
