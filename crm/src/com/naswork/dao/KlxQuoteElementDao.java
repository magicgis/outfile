package com.naswork.dao;

import java.util.List;

import com.naswork.model.KlxQuoteElement;

public interface KlxQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KlxQuoteElement record);

    int insertSelective(KlxQuoteElement record);

    KlxQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KlxQuoteElement record);

    int updateByPrimaryKey(KlxQuoteElement record);
    
    public List<KlxQuoteElement> selectByClientInquiryId(Integer clientInquiryId,Integer id);
}