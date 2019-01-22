package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ExchangeExportPackageDao;
import com.naswork.model.ExchangeExportPackage;
import com.naswork.service.ExchangeExportPackageService;
import com.naswork.vo.PageModel;

@Service("exchangeExportPackageService")
public class ExchangeExportPackageServiceImpl implements
		ExchangeExportPackageService {
	@Resource
	private ExchangeExportPackageDao exchangeExportPackageDao;

	@Override
	public int insertSelective(ExchangeExportPackage record) {
		return exchangeExportPackageDao.insertSelective(record);
	}

	@Override
	public ExchangeExportPackage selectByPrimaryKey(Integer id) {
		return exchangeExportPackageDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ExchangeExportPackage record) {
		return exchangeExportPackageDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(
			PageModel<ExchangeExportPackage> page) {
		page.setEntities(exchangeExportPackageDao.listPage(page));
	}
	
	public int deleteByPrimaryKey(Integer id){
		return exchangeExportPackageDao.deleteByPrimaryKey(id);
	}
	
	public Double sumByImportId(Integer exchangeImportPackageId){
		return exchangeExportPackageDao.sumByImportId(exchangeImportPackageId);
	}

}
