package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientOrderElementNotmatchDao;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementNotmatch;
import com.naswork.model.ClientOrderElementUpload;
import com.naswork.service.ClientOrderElementNotmatchService;
import com.naswork.vo.PageModel;
@Service("clientOrderElementNotmatchService")
public class ClientOrderElementNotmatchServiceImpl implements ClientOrderElementNotmatchService {

	@Resource
	private ClientOrderElementNotmatchDao clientOrderElementNotmatchDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return clientOrderElementNotmatchDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientOrderElement record) {
		return clientOrderElementNotmatchDao.insert(record);
	}

	@Override
	public int insertSelective(ClientOrderElementNotmatch record) {
		return clientOrderElementNotmatchDao.insertSelective(record);
	}

	@Override
	public ClientOrderElementNotmatch selectByPrimaryKey(Integer id) {
		return clientOrderElementNotmatchDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientOrderElementNotmatch record) {
		return clientOrderElementNotmatchDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ClientOrderElementNotmatch record) {
		return clientOrderElementNotmatchDao.updateByPrimaryKey(record);
	}

	@Override
	public void deleteByUserId(Integer clientOrderId) {
		clientOrderElementNotmatchDao.deleteByUserId(clientOrderId);		
	}

	@Override
	public List<ClientOrderElement> selectByUserId(Integer userId) {
		return clientOrderElementNotmatchDao.selectByUserId(userId);
	}
	
	public void listpage(PageModel<ClientOrderElementNotmatch> page){
		page.setEntities(clientOrderElementNotmatchDao.listpage(page));
	}
	
}
