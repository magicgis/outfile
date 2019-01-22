package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierPartTypeRelationKey;
import com.naswork.vo.PageModel;

public interface SupplierPartTypeRelationService {

	public int deleteByPrimaryKey(SupplierPartTypeRelationKey key);

	public int insert(SupplierPartTypeRelationKey record);

	public int insertSelective(SupplierPartTypeRelationKey record);
    
    public void selectByPartPage(PageModel<SupplierPartTypeRelationKey> page);
    
    public SupplierPartTypeRelationKey selectBySupplierIdAndAirId(SupplierPartTypeRelationKey supplierPartTypeRelationKey);
	
}
