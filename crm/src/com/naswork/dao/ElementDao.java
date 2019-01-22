package com.naswork.dao;

import java.util.List;

import com.naswork.model.Element;

public interface ElementDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Element record);

    Element selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Element record);

    int updateByPrimaryKeyWithBLOBs(Element record);

    int updateByPrimaryKey(Element record);
    
    /*
     * 根据件号查询ID
     */
    public List<Element> findIdByPn(String partNumber);
    
    /*
     * 新增
     */
    public void insert(Element element);
}