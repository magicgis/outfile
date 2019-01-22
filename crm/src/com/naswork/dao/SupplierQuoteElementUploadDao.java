package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.model.SupplierQuoteElementUpload;
import com.naswork.vo.PageModel;

public interface SupplierQuoteElementUploadDao {
    void deleteByPrimaryKey(Integer id);

    void insert(SupplierQuoteElementUpload record);

    void insertSelective(SupplierQuoteElementUpload record);

    SupplierQuoteElementUpload selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(SupplierQuoteElementUpload record);

    void updateByPrimaryKey(SupplierQuoteElementUpload record);
    
    List<SupplierQuoteElementUpload> selectByUserId(PageModel<SupplierQuoteElementUpload> page);
}