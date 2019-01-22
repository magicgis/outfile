package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierOrderElementFjDao;
import com.naswork.model.SupplierOrderElementFj;
import com.naswork.service.SupplierOrderElementFjService;
import com.naswork.vo.PageModel;

@Service("supplierOrderElementFjService")
public class SupplierOrderElementFjServiceImpl implements
		SupplierOrderElementFjService {

	@Resource
	private SupplierOrderElementFjDao supplierOrderElementFjDao;
	
	@Override
	public void insertSelective(SupplierOrderElementFj record) {
		supplierOrderElementFjDao.insertSelective(record);
	}

	@Override
	public SupplierOrderElementFj selectByPrimaryKey(Integer id) {
		return supplierOrderElementFjDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierOrderElementFj record) {
		supplierOrderElementFjDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SupplierOrderElementFj> getByOrderId(PageModel<String> page) {
		return supplierOrderElementFjDao.getByOrderId(page);
	}

	@Override
	public List<SupplierOrderElementFj> selectForeignKey(String ids) {
		return supplierOrderElementFjDao.selectForeignKey(ids);
	}

}
