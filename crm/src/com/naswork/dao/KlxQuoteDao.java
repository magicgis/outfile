package com.naswork.dao;

import java.util.List;

import com.naswork.model.KlxQuote;

public interface KlxQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KlxQuote record);

    int insertSelective(KlxQuote record);

    KlxQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KlxQuote record);

    int updateByPrimaryKey(KlxQuote record);
    
    public List<KlxQuote> getFinishList();
    
    public void lockMessage(KlxQuote klxQuote);
    
    public void unLockMessage(KlxQuote klxQuote);
}