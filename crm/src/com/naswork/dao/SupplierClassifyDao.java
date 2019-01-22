package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierClassify;

public interface SupplierClassifyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierClassify record);

    int insertSelective(SupplierClassify record);

    List<SupplierClassify> selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierClassify record);

    int updateByPrimaryKey(SupplierClassify record);
}