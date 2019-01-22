package com.naswork.dao;

import java.util.List;

import com.naswork.model.KapcoQuote;
import com.naswork.model.KlxQuote;

public interface KapcoQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KapcoQuote record);

    int insertSelective(KapcoQuote record);

    KapcoQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KapcoQuote record);

    int updateByPrimaryKey(KapcoQuote record);
    
    public List<KapcoQuote> getFinishList();
    
    public void lockMessage(KapcoQuote kapcoQuote);
    
    public void unLockMessage(KapcoQuote kapcoQuote);
}