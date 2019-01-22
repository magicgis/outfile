package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierFileRelation;

public interface SupplierFileRelationService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(SupplierFileRelation record);

	    int insertSelective(SupplierFileRelation record);

	    SupplierFileRelation selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(SupplierFileRelation record);

	    int updateByPrimaryKey(SupplierFileRelation record);
	    
	    List<SupplierFileRelation> listData();
}
