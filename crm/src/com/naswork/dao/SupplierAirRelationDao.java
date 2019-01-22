package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierAirRelationKey;
import com.naswork.vo.PageModel;

public interface SupplierAirRelationDao {
	List<SupplierAirRelationKey> selectBySupplierIdPage(PageModel<SupplierAirRelationKey> page);
	
	SupplierAirRelationKey selectBySupplierIdAndAirId(SupplierAirRelationKey supplierAirRelationKey);
	
    void deleteByPrimaryKey(SupplierAirRelationKey key);

    List<SupplierAirRelationKey> selectByAirIdPage(PageModel<SupplierAirRelationKey> page);
    
    void insert(SupplierAirRelationKey record);

    void insertSelective(SupplierAirRelationKey record);
    
    public List<SupplierAirRelationKey> selectByClientInuqiryId(Integer clientInquiryId);
    
    public List<SupplierAirRelationKey> selectByClientInuqiryIdForFree(Integer clientInquiryId);
    
    public List<SupplierAirRelationKey> getSupplierByIds(String ids);
}