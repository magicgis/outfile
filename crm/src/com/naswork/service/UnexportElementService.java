package com.naswork.service;

import java.util.List;

import com.naswork.model.UnexportElement;

public interface UnexportElementService {

    public int insertSelective(UnexportElement record);

    public UnexportElement selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(UnexportElement record);
    
    public int deleteByPrimaryKey(Integer id);
    
    public void deleteByUserId(Integer userId);
	
}
