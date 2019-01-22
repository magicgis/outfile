package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientClassifyDao;
import com.naswork.model.ClientClassify;
import com.naswork.service.ClientClassifyService;
@Service("clientClassifyService")
public class ClientClassifyServiceImpl implements ClientClassifyService {

	@Resource
	private ClientClassifyDao clientClassifyDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return clientClassifyDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientClassify record) {
		return clientClassifyDao.insert(record);
	}

	@Override
	public int insertSelective(ClientClassify record) {
		return clientClassifyDao.insertSelective(record);
	}

	@Override
	public List<ClientClassify> selectByPrimaryKey(Integer id) {
		return clientClassifyDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientClassify record) {
		return clientClassifyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ClientClassify record) {
		return clientClassifyDao.updateByPrimaryKey(record);
	}

}
