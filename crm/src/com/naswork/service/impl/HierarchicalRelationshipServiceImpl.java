package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.HierarchicalRelationshipDao;
import com.naswork.model.HierarchicalRelationship;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.service.HierarchicalRelationshipService;
import com.naswork.vo.PageModel;
@Service("hierarchicalRelationshipService")
public class HierarchicalRelationshipServiceImpl implements HierarchicalRelationshipService {

	@Resource HierarchicalRelationshipDao hierarchicalRelationshipDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return hierarchicalRelationshipDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(HierarchicalRelationship record) {
		return hierarchicalRelationshipDao.insert(record);
	}

	@Override
	public int insertSelective(HierarchicalRelationship record) {
		return hierarchicalRelationshipDao.insertSelective(record);
	}

	@Override
	public HierarchicalRelationship selectByPrimaryKey(Integer id) {
		return hierarchicalRelationshipDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(HierarchicalRelationship record) {
		return hierarchicalRelationshipDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(HierarchicalRelationship record) {
		return hierarchicalRelationshipDao.updateByPrimaryKey(record);
	}

	public void relationListPage(PageModel<HierarchicalRelationship> page) {
		page.setEntities(hierarchicalRelationshipDao.relationListPage(page));
	}
}
