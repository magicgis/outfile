package com.naswork.dao;

import java.util.List;

import com.naswork.model.TManufactory;

public interface TManufactoryDao {
    int deleteByPrimaryKey(String msn);

    int insert(TManufactory record);

    int insertSelective(TManufactory record);

    TManufactory selectByPrimaryKey(String msn);
    
    List<TManufactory> selectByCageCode(String cageCode);

    int updateByPrimaryKeySelective(TManufactory record);

    int updateByPrimaryKey(TManufactory record);
    
    public List<TManufactory> getMsnByCageCode(String cageCode);
    
    public List<TManufactory> selectByMsn(String msn);
    
}