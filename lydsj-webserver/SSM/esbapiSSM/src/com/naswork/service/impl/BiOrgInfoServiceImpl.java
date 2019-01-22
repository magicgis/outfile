package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.BiOrgInfoDao;
import com.naswork.model.BiOrgInfo;
import com.naswork.service.BiOrgInfoService;

@Service("biOrgInfoService")
public class BiOrgInfoServiceImpl implements BiOrgInfoService {
	@Resource
	private BiOrgInfoDao biOrgInfoDao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return biOrgInfoDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(BiOrgInfo record) {
		return biOrgInfoDao.insert(record);
	}

	@Override
	public int insertSelective(BiOrgInfo record) {
		return biOrgInfoDao.insertSelective(record);
	}

	@Override
	public BiOrgInfo selectByPrimaryKey(Integer id) {
		return biOrgInfoDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BiOrgInfo record) {
		return biOrgInfoDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(BiOrgInfo record) {
		return biOrgInfoDao.updateByPrimaryKey(record);
	}

	@Override
	public List<Integer> selectByParentId(Integer id) {
		return biOrgInfoDao.selectByParentId(id);
	}

	@Override
	public List<BiOrgInfo> getBiOrgInfoByParentId(Integer id) {
		return biOrgInfoDao.getBiOrgInfoByParentId(id);
	}

	@Override
	public List<BiOrgInfo> getBiOrgInfoByLevel(Integer level) {
		return biOrgInfoDao.getBiOrgInfoByLevel(level);
	}

}
