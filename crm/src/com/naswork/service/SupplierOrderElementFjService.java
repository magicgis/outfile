package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierOrderElementFj;
import com.naswork.vo.PageModel;

public interface SupplierOrderElementFjService {

	public void insertSelective(SupplierOrderElementFj record);

    SupplierOrderElementFj selectByPrimaryKey(Integer id);

    public void updateByPrimaryKeySelective(SupplierOrderElementFj record);
    
    public List<SupplierOrderElementFj> getByOrderId(PageModel<String> page);
    
    public List<SupplierOrderElementFj> selectForeignKey(String ids);
	
}
