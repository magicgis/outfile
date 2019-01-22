package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierClassify;

public interface SupplierClassifyService {
	  int deleteByPrimaryKey(Integer id);

	    int insert(SupplierClassify record);

	    int insertSelective(SupplierClassify record);

	    List<SupplierClassify> selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(SupplierClassify record);

	    int updateByPrimaryKey(SupplierClassify record);
}
