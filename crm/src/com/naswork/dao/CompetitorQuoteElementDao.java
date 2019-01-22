package com.naswork.dao;

import com.naswork.model.CompetitorQuoteElement;

public interface CompetitorQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CompetitorQuoteElement record);

    int insertSelective(CompetitorQuoteElement record);

    CompetitorQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompetitorQuoteElement record);

    int updateByPrimaryKey(CompetitorQuoteElement record);
}