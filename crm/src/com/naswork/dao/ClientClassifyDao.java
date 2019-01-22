package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientClassify;

public interface ClientClassifyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientClassify record);

    int insertSelective(ClientClassify record);

    List<ClientClassify> selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientClassify record);

    int updateByPrimaryKey(ClientClassify record);
}