package com.naswork.service;

import java.util.List;

import com.naswork.model.Supplier;
import com.naswork.model.SupplierContact;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

public interface SupplierService {

	void listPage(PageModel<Supplier> page, String searchString, GridSort sort);
	
	void insertSelective(Supplier record,List<UserVo> userVos);

	Supplier findByCode(String code);
	
	Supplier findByShortName(String shortName);
	
	Supplier findByName(String name);
	
	Supplier selectByPrimaryKey(Integer id);

	 void updateByPrimaryKeySelective(Supplier record);
	 
	 public Integer getCurrencyId(String code);
	 
	 public Integer findByUrl(String url);
	 
	 public List<Supplier> Suppliers(PageModel<Supplier> page);
	 
	 List<Supplier>  selectAll();
	 
	 public void getOutTimeSupplier();
	 
	 public Supplier selectByCode(String code);
	 
	 public List<Integer> checkByCode(String code);

	 List<Supplier> getSupplierByCodeAndUserId(String userId,String supplierCode);
}
