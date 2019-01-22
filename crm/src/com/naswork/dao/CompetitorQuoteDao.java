package com.naswork.dao;

import com.naswork.model.CompetitorQuote;

public interface CompetitorQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CompetitorQuote record);

    int insertSelective(CompetitorQuote record);

    CompetitorQuote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompetitorQuote record);

    int updateByPrimaryKey(CompetitorQuote record);
}