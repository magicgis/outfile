package com.naswork.dao;

import java.util.List;

import com.naswork.model.AirSupplierRelationKey;
import com.naswork.vo.PageModel;

public interface AirSupplierRelationDao {
	List<AirSupplierRelationKey> selectByAirIdPage(PageModel<AirSupplierRelationKey> page);
	
    void deleteByPrimaryKey(AirSupplierRelationKey key);

    void insert(AirSupplierRelationKey record);

    void insertSelective(AirSupplierRelationKey record);
}