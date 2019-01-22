package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.CompetitorSupplierDao;
import com.naswork.model.CompetitorSupplier;
import com.naswork.service.CompetitorSupplierService;
import com.naswork.vo.PageModel;

@Service("competitorSupplierService")
public class CompetitorSupplierServiceImpl implements CompetitorSupplierService {

	@Resource
	private CompetitorSupplierDao competitorSupplierDao;
	
	@Override
	public void insertSelective(CompetitorSupplier record) {
		competitorSupplierDao.insertSelective(record);
	}

	@Override
	public CompetitorSupplier selectByPrimaryKey(Integer id) {
		return competitorSupplierDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(CompetitorSupplier record) {
		competitorSupplierDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<CompetitorSupplier> listPage(PageModel<CompetitorSupplier> page) {
		return competitorSupplierDao.listPage(page);
	}

	@Override
	public int getCountByClientAndSupplier(Integer clientId, Integer supplierId) {
		return competitorSupplierDao.getCountByClientAndSupplier(clientId, supplierId);
	}

}
