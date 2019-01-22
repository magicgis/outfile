package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierDebtDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.model.SupplierDebt;
import com.naswork.service.SupplierDebtService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("supplierDebtService")
public class SupplierDebtServiceImpl implements SupplierDebtService {

	@Resource 
	private SupplierDebtDao supplierDebtDao;
	
	@Override
	public int insertSelective(SupplierDebt record) {
		return supplierDebtDao.insertSelective(record);
	}

	@Override
	public SupplierDebt selectByPrimaryKey(Integer id) {
		return supplierDebtDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierDebt record) {
		return supplierDebtDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(PageModel<SupplierDebt> page,String where,GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(supplierDebtDao.listPage(page));
	}

	@Override
	public SupplierDebt totalArrears(String supplierCode) {
		return supplierDebtDao.totalArrears(supplierCode);
	}

	@Override
	public List<SupplierDebt> dataBySupplierCode(String supplierCode) {
		return supplierDebtDao.dataBySupplierCode(supplierCode);
	}

}
