package com.naswork.dao;

import java.util.List;

import com.naswork.model.SatairQuote;

public interface SatairQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SatairQuote record);

    int insertSelective(SatairQuote record);

    SatairQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SatairQuote record);

    int updateByPrimaryKey(SatairQuote record);
    
    public List<SatairQuote> getNewCrawMessage();
    
    public void lockTheMessage(SatairQuote satairQuote);
    
    public void unLockTheMessage(SatairQuote satairQuote);
}