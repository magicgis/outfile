package com.naswork.dao;

import java.util.List;

import com.naswork.model.AviallQuoteElement;

public interface AviallQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AviallQuoteElement record);

    int insertSelective(AviallQuoteElement record);

    AviallQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AviallQuoteElement record);

    int updateByPrimaryKey(AviallQuoteElement record);
    
    public AviallQuoteElement getByClientInquiryElementId(Integer clientInquiryElementId);
    
    public List<AviallQuoteElement> selectByClientInquiryId(Integer clientInquiryId,Integer id);
}