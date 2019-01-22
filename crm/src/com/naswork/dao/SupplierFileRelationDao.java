package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierFileRelation;

public interface SupplierFileRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierFileRelation record);

    int insertSelective(SupplierFileRelation record);

    SupplierFileRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierFileRelation record);

    int updateByPrimaryKey(SupplierFileRelation record);
    
    List<SupplierFileRelation> listData();
}