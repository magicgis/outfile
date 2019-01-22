package com.naswork.dao;

import java.util.List;

import com.naswork.model.AviallQuote;

public interface AviallQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AviallQuote record);

    int insertSelective(AviallQuote record);

    AviallQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AviallQuote record);

    int updateByPrimaryKey(AviallQuote record);
    
    public List<AviallQuote> getFinishList();
    
    public void lockMessage(AviallQuote aviallQuote);
    
    public void unLockMessage(AviallQuote aviallQuote);
}