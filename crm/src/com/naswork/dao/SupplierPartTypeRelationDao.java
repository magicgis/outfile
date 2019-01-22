package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierPartTypeRelationKey;
import com.naswork.vo.PageModel;

public interface SupplierPartTypeRelationDao {
    int deleteByPrimaryKey(SupplierPartTypeRelationKey key);

    int insert(SupplierPartTypeRelationKey record);

    int insertSelective(SupplierPartTypeRelationKey record);
    
    public List<SupplierPartTypeRelationKey> selectByPartPage(PageModel<SupplierPartTypeRelationKey> page);
    
    public SupplierPartTypeRelationKey selectBySupplierIdAndAirId(SupplierPartTypeRelationKey supplierPartTypeRelationKey);
}