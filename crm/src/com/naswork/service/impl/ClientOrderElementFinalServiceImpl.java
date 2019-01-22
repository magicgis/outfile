package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientOrderElementFinalDao;
import com.naswork.model.ClientOrderElementFinal;
import com.naswork.service.ClientOrderElementFinalService;
@Service("clientOrderElementFinalService")
public class ClientOrderElementFinalServiceImpl implements ClientOrderElementFinalService {

	@Resource
	private ClientOrderElementFinalDao clientOrderElementFinalDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return clientOrderElementFinalDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientOrderElementFinal record) {
		return clientOrderElementFinalDao.insert(record);
	}

	@Override
	public int insertSelective(ClientOrderElementFinal record) {
		return clientOrderElementFinalDao.insertSelective(record);
	}

	@Override
	public ClientOrderElementFinal selectByPrimaryKey(Integer id) {
		return clientOrderElementFinalDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientOrderElementFinal record) {
		return clientOrderElementFinalDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ClientOrderElementFinal record) {
		return clientOrderElementFinalDao.updateByPrimaryKey(record);
	}

	@Override
	public void updateStatus(ClientOrderElementFinal record) {
		clientOrderElementFinalDao.updateStatus(record);
	}

}
