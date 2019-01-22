package com.naswork.dao;

import java.util.List;

import com.naswork.model.UnexportElement;

public interface UnexportElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UnexportElement record);

    int insertSelective(UnexportElement record);

    UnexportElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnexportElement record);

    int updateByPrimaryKey(UnexportElement record);
    
    public void deleteByUserId(Integer userId);
    
}