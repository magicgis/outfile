package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierContact;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierContactService {
	
	void findSupplierContactPage(PageModel<SupplierContact> page, String searchString, GridSort sort);
		
	int deleteByPrimaryKey(Integer id);

	int insert(SupplierContact record);

	int insertSelective(SupplierContact record);

	SupplierContact selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SupplierContact record);

	int updateByPrimaryKey(SupplierContact record);
	    
	public List<SupplierContact> findBySupplierId(Integer supplierId);

	public void updateEmailPersonBySupplierId(Integer supplierId);
}
