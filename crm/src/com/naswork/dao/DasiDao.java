package com.naswork.dao;

import java.util.List;

import com.naswork.model.Dasi;

public interface DasiDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Dasi record);

    int insertSelective(Dasi record);

    Dasi selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dasi record);

    int updateByPrimaryKey(Dasi record);
    
    public List<Dasi> getCompleteList();
}