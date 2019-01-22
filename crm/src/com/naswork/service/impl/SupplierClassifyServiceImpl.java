package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierClassifyDao;
import com.naswork.model.SupplierClassify;
import com.naswork.service.SupplierClassifyService;
@Service("supplierClassifyServiceImpl")
public class SupplierClassifyServiceImpl implements SupplierClassifyService {

	@Resource
	private SupplierClassifyDao supplierClassifyDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return supplierClassifyDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SupplierClassify record) {
		return supplierClassifyDao.insert(record);
	}

	@Override
	public int insertSelective(SupplierClassify record) {
		return supplierClassifyDao.insertSelective(record);
	}

	@Override
	public List<SupplierClassify> selectByPrimaryKey(Integer id) {
		return supplierClassifyDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierClassify record) {
		return supplierClassifyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SupplierClassify record) {
		return supplierClassifyDao.updateByPrimaryKey(record);
	}

}
