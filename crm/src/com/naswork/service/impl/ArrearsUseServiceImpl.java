package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ArrearsUseDao;
import com.naswork.model.ArrearsUse;
import com.naswork.service.ArrearsUseService;

@Service("arrearsUseService")
public class ArrearsUseServiceImpl implements ArrearsUseService {

	@Resource
	private ArrearsUseDao arrearsUseDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return arrearsUseDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ArrearsUse record) {
		return arrearsUseDao.insert(record);
	}

	@Override
	public int insertSelective(ArrearsUse record) {
		return arrearsUseDao.insertSelective(record);
	}

	@Override
	public List<ArrearsUse> selectByPrimaryKey(Integer id) {
		return arrearsUseDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ArrearsUse record) {
		return arrearsUseDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ArrearsUse record) {
		return arrearsUseDao.updateByPrimaryKey(record);
	}

	@Override
	public Double selectTotalBySupplierCode(String supplierCode) {
		return arrearsUseDao.selectTotalBySupplierCode(supplierCode);
	}

	@Override
	public ArrearsUse selectByElementId(Integer importPackagePaymentElementId) {
		return arrearsUseDao.selectByElementId(importPackagePaymentElementId);
	}

	@Override
	public void deleteByElementId(ArrearsUse record) {
		arrearsUseDao.deleteByElementId(record);		
	}

}
