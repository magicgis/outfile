package com.naswork.dao;

import java.util.List;

import com.naswork.model.StorckMarket;

public interface StorckMarketDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StorckMarket record);

    int insertSelective(StorckMarket record);

    StorckMarket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StorckMarket record);

    int updateByPrimaryKey(StorckMarket record);
    
    public List<StorckMarket> getEmailList();
    
    public List<Integer> getRecord(Integer supplierId);
    
    public Integer getClientInquiryId(Integer supplierId);
}