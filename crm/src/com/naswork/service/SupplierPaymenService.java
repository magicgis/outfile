package com.naswork.service;

import com.naswork.model.SupplierPayment;

public interface SupplierPaymenService {
	 void deleteByPrimaryKey(Integer id);

	 void insert(SupplierPayment record);

	 void insertSelective(SupplierPayment record);

	    SupplierPayment selectByPrimaryKey(Integer id);

	    void updateByPrimaryKeySelective(SupplierPayment record);

	    void updateByPrimaryKey(SupplierPayment record);
}
