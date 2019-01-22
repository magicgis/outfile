package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierFileRelationDao;
import com.naswork.model.SupplierFileRelation;
import com.naswork.service.SupplierFileRelationService;
@Service("supplierFileRelationService")
public class SupplierFileRelationServiceImpl implements SupplierFileRelationService {
	@Resource
	private SupplierFileRelationDao supplierFileRelationDao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return supplierFileRelationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SupplierFileRelation record) {
		return supplierFileRelationDao.insert(record);
	}

	@Override
	public int insertSelective(SupplierFileRelation record) {
		return supplierFileRelationDao.insertSelective(record);
	}

	@Override
	public SupplierFileRelation selectByPrimaryKey(Integer id) {
		return supplierFileRelationDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierFileRelation record) {
		return supplierFileRelationDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SupplierFileRelation record) {
		return supplierFileRelationDao.updateByPrimaryKey(record);
	}

	@Override
	public List<SupplierFileRelation> listData() {
		return supplierFileRelationDao.listData();
	}
	

}
