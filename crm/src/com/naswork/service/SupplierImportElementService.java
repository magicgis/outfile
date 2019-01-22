package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierImportElement;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;

public interface SupplierImportElementService {

	void insert(SupplierImportElement supplierImportElement);
	
	 void deleteByPrimaryKey(Integer key);
	 
	 List<SupplierInquiryStatistic> supplierImportStat(SupplierInquiryStatistic supplierInquiryStatistic);
}
