package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierContactDao;
import com.naswork.model.SupplierContact;
import com.naswork.service.SupplierContactService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("supplierContactServiceImpl")
public class SupplierContactServiceImpl implements SupplierContactService {
	@Resource
	private SupplierContactDao supplierContactDao;
	@Override
	public void findSupplierContactPage(PageModel<SupplierContact> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		List<SupplierContact> suppliers=supplierContactDao.findSupplierContactPage(page);
		
		page.setEntities(suppliers);
	}
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return supplierContactDao.deleteByPrimaryKey(id);
	}
	@Override
	public int insert(SupplierContact record) {
		return supplierContactDao.insert(record);
	}
	@Override
	public int insertSelective(SupplierContact record) {
		return supplierContactDao.insertSelective(record);
	}
	@Override
	public SupplierContact selectByPrimaryKey(Integer id) {
		return supplierContactDao.selectByPrimaryKey(id);
	}
	@Override
	public int updateByPrimaryKeySelective(SupplierContact record) {
		return supplierContactDao.updateByPrimaryKeySelective(record);
	}
	@Override
	public int updateByPrimaryKey(SupplierContact record) {
		return supplierContactDao.updateByPrimaryKey(record);
	}
	
	public List<SupplierContact> findBySupplierId(Integer supplierId){
		return supplierContactDao.findBySupplierId(supplierId);
	}
	
	public void updateEmailPersonBySupplierId(Integer supplierId){
		supplierContactDao.updateEmailPersonBySupplierId(supplierId);
	}
}
