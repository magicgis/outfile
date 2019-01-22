package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.AuthorityRelationDao;
import com.naswork.model.AuthorityRelation;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.service.AuthorityRelationService;
@Service("authorityRelationService")
public class AuthorityRelationServiceImpl implements AuthorityRelationService {
 
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return authorityRelationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AuthorityRelation record) {
		return authorityRelationDao.insert(record);
	}

	@Override
	public int insertSelective(PowerVo record) {
		return authorityRelationDao.insertSelective(record);
	}

	@Override
	public AuthorityRelation selectByPrimaryKey(Integer id) {
		return authorityRelationDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AuthorityRelation record) {
		return authorityRelationDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AuthorityRelation record) {
		return authorityRelationDao.updateByPrimaryKey(record);
	}

	@Override
	public void updateBySupplierId(AuthorityRelation record) {
		authorityRelationDao.updateBySupplierId(record);		
	}

	@Override
	public void deletePower(Integer id) {
		authorityRelationDao.deletePower(id);		
	}

	@Override
	public List<Integer> getUserId(Integer supplierId) {
		return authorityRelationDao.getUserId(supplierId);
	}

	@Override
	public int checkPower(PowerVo powerVo) {
		return authorityRelationDao.checkPower(powerVo);
	}

	@Override
	public List<AuthorityRelation> selectBySupplierId(Integer supplierId) {
		return authorityRelationDao.selectBySupplierId(supplierId);
	}

}
