package com.naswork.dao;

import java.util.List;

import com.naswork.model.Jqqd;

public interface JqqdDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Jqqd record);

    int insertSelective(Jqqd record);

    Jqqd selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jqqd record);

    int updateByPrimaryKey(Jqqd record);

	List<Jqqd> selectAll();
}