package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierAptitudeDao;
import com.naswork.model.SupplierAptitude;
import com.naswork.service.SupplierAptitudeService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("supplierAptitudeService")
public class SupplierAptitudeServiceImpl implements SupplierAptitudeService {

	@Resource
	private SupplierAptitudeDao supplierAptitudeDao;
	
	@Override
	public void insertSelective(SupplierAptitude record) {
		supplierAptitudeDao.insertSelective(record);
	}

	@Override
	public SupplierAptitude selectByPrimaryKey(Integer id) {
		return supplierAptitudeDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierAptitude record) {
		supplierAptitudeDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void deleteByPrimaryKey(Integer id) {
		supplierAptitudeDao.deleteByPrimaryKey(id);
	}

	@Override
	public void listPage(PageModel<SupplierAptitude> page, GridSort sort,
			String where) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(supplierAptitudeDao.listPage(page));
	}

}
