package com.naswork.dao;

import java.util.List;

import com.naswork.model.KapcoQuoteElement;
import com.naswork.model.KlxQuoteElement;

public interface KapcoQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KapcoQuoteElement record);

    int insertSelective(KapcoQuoteElement record);

    KapcoQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KapcoQuoteElement record);

    int updateByPrimaryKey(KapcoQuoteElement record);
    
    public List<KapcoQuoteElement> selectByClientInquiryId(Integer clientInquiryId,Integer id);
}