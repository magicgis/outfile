package com.naswork.dao;

import java.util.List;

import com.naswork.model.KapcoQuote;
import com.naswork.model.TxtavQuote;

public interface TxtavQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TxtavQuote record);

    int insertSelective(TxtavQuote record);

    TxtavQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TxtavQuote record);

    int updateByPrimaryKey(TxtavQuote record);
    
    public List<TxtavQuote> getFinishList();
    
    public void lockMessage(TxtavQuote txtavQuote);
    
    public void unLockMessage(TxtavQuote txtavQuote);
}