package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierAptitude;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierAptitudeService {

	public void insertSelective(SupplierAptitude record);

	public SupplierAptitude selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(SupplierAptitude record);
    
	public void deleteByPrimaryKey(Integer id);
	
	public void listPage(PageModel<SupplierAptitude> page,GridSort sort,String where);
	
}
