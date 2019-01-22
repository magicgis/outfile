package com.naswork.dao;

import java.util.List;

import com.naswork.model.DasiMessage;

public interface DasiMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DasiMessage record);

    int insertSelective(DasiMessage record);

    DasiMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DasiMessage record);

    int updateByPrimaryKey(DasiMessage record);
    
    public List<DasiMessage> getCrawlMessage(Integer dasiId);
}