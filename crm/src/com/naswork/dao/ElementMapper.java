package com.naswork.dao;

import com.naswork.model.Element;

public interface ElementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Element record);

    int insertSelective(Element record);

    Element selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Element record);

    int updateByPrimaryKeyWithBLOBs(Element record);

    int updateByPrimaryKey(Element record);
}