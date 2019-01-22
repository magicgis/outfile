package com.naswork.dao;

import java.util.List;

import com.naswork.model.KapcoQuoteElement;
import com.naswork.model.TxtavQuoteElement;

public interface TxtavQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TxtavQuoteElement record);

    int insertSelective(TxtavQuoteElement record);

    TxtavQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TxtavQuoteElement record);

    int updateByPrimaryKey(TxtavQuoteElement record);
    
    public List<TxtavQuoteElement> selectByClientInquiryId(Integer clientInquiryId,Integer id);
}