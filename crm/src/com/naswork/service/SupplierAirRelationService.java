package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierAirRelationKey;
import com.naswork.vo.PageModel;

public interface SupplierAirRelationService {
	SupplierAirRelationKey selectBySupplierIdAndAirId(SupplierAirRelationKey supplierAirRelationKey);
	
	void selectBySupplierIdPage(PageModel<SupplierAirRelationKey> page);
	
	void selectByAirIdPage(PageModel<SupplierAirRelationKey> page);
	
    void deleteByPrimaryKey(SupplierAirRelationKey key);

    void insert(SupplierAirRelationKey record);

    void insertSelective(SupplierAirRelationKey record);
}
