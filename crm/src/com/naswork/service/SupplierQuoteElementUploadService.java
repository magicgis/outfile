package com.naswork.service;

import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.model.SupplierQuoteElementUpload;
import com.naswork.vo.PageModel;

public interface SupplierQuoteElementUploadService {
	 void deleteByPrimaryKey(Integer id);

	    void insert(SupplierQuoteElementUpload record);

	    void insertSelective(SupplierQuoteElementUpload record);

	    SupplierQuoteElementUpload selectByPrimaryKey(Integer id);

	    void updateByPrimaryKeySelective(SupplierQuoteElementUpload record);

	    void updateByPrimaryKey(SupplierQuoteElementUpload record);
	    
	    void selectByUserId(PageModel<SupplierQuoteElementUpload> page);
}
