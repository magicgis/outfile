package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.UnexportElementDao;
import com.naswork.model.UnexportElement;
import com.naswork.service.UnexportElementService;

@Service("unexportElementService")
public class UnexportElementServiceImpl implements UnexportElementService {

	@Resource
	private UnexportElementDao unexportElementDao;
	
	@Override
	public int insertSelective(UnexportElement record) {
		return unexportElementDao.insertSelective(record);
	}

	@Override
	public UnexportElement selectByPrimaryKey(Integer id) {
		return unexportElementDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UnexportElement record) {
		return unexportElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return unexportElementDao.deleteByPrimaryKey(id);
	}
	
	public void deleteByUserId(Integer userId){
		unexportElementDao.deleteByUserId(userId);
	}

}
