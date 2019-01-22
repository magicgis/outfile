package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierQuoteElementUploadDao;
import com.naswork.model.SupplierQuoteElementUpload;
import com.naswork.service.SupplierQuoteElementUploadService;
import com.naswork.vo.PageModel;
@Service("supplierQuoteElementUploadServiceImpl")
public class SupplierQuoteElementUploadServiceImpl implements SupplierQuoteElementUploadService {

	@Resource
	private SupplierQuoteElementUploadDao supplierQuoteElementUploadDao;

	@Override
	public void deleteByPrimaryKey(Integer id) {
		supplierQuoteElementUploadDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(SupplierQuoteElementUpload record) {
		supplierQuoteElementUploadDao.insert(record);
	}

	@Override
	public void insertSelective(SupplierQuoteElementUpload record) {
		supplierQuoteElementUploadDao.insertSelective(record);
	}

	@Override
	public SupplierQuoteElementUpload selectByPrimaryKey(Integer id) {
		return supplierQuoteElementUploadDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierQuoteElementUpload record) {
		supplierQuoteElementUploadDao.updateByPrimaryKey(record);
	}

	@Override
	public void updateByPrimaryKey(SupplierQuoteElementUpload record) {
		supplierQuoteElementUploadDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void selectByUserId(PageModel<SupplierQuoteElementUpload> page) {
		page.setEntities(supplierQuoteElementUploadDao.selectByUserId(page));
	}

}
