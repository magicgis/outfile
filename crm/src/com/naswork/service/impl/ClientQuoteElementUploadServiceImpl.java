package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientQuoteElementUploadDao;
import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.service.ClientQuoteElementUploadService;
import com.naswork.vo.PageModel;
@Service("clientQuoteElementUploadServiceImpl")
public class ClientQuoteElementUploadServiceImpl implements ClientQuoteElementUploadService {

	@Resource
	private ClientQuoteElementUploadDao clientQuoteElementUploadDao;
	@Override
	public void deleteByPrimaryKey(Integer id) {
		clientQuoteElementUploadDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(ClientQuoteElementUpload record) {
		clientQuoteElementUploadDao.insert(record);
	}

	@Override
	public void insertSelective(ClientQuoteElementUpload record) {
		clientQuoteElementUploadDao.insertSelective(record);
	}

	@Override
	public ClientQuoteElementUpload selectByPrimaryKey(Integer id) {
		return clientQuoteElementUploadDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ClientQuoteElementUpload record) {
		clientQuoteElementUploadDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void updateByPrimaryKey(ClientQuoteElementUpload record) {
		clientQuoteElementUploadDao.updateByPrimaryKey(record);
	}

	@Override
	public void selectByUserId(PageModel<ClientQuoteElementUpload> page) {
		page.setEntities(clientQuoteElementUploadDao.selectByUserId(page));
	}

}
