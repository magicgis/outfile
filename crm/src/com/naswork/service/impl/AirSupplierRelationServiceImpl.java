package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.AirSupplierRelationDao;
import com.naswork.model.AirSupplierRelationKey;
import com.naswork.service.AirSupplierRelationService;
import com.naswork.vo.PageModel;
@Service("airSupplierRelationServiceImpl")
public class AirSupplierRelationServiceImpl implements AirSupplierRelationService {

	@Resource
	private AirSupplierRelationDao airSupplierRelationDao;
	
	@Override
	public void deleteByPrimaryKey(AirSupplierRelationKey key) {
		airSupplierRelationDao.deleteByPrimaryKey(key);
	}

	@Override
	public void insert(AirSupplierRelationKey record) {
		airSupplierRelationDao.insert(record);
	}

	@Override
	public void insertSelective(AirSupplierRelationKey record) {
		airSupplierRelationDao.insertSelective(record);
	}

	@Override
	public void selectByAirIdPage(PageModel<AirSupplierRelationKey> page) {
		page.setEntities(airSupplierRelationDao.selectByAirIdPage(page));
	}

}
