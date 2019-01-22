package com.naswork.service;

import com.naswork.model.AirSupplierRelationKey;
import com.naswork.vo.PageModel;

public interface AirSupplierRelationService {
	void selectByAirIdPage(PageModel<AirSupplierRelationKey> page);
	
	  void deleteByPrimaryKey(AirSupplierRelationKey key);

	    void insert(AirSupplierRelationKey record);

	    void insertSelective(AirSupplierRelationKey record);
}
