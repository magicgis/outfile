package com.naswork.dao;

import com.naswork.model.StockMarketCrawl;

public interface StockMarketCrawlDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StockMarketCrawl record);

    int insertSelective(StockMarketCrawl record);

    StockMarketCrawl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockMarketCrawl record);

    int updateByPrimaryKey(StockMarketCrawl record);
}