package com.naswork.dao;

import com.naswork.model.ExchangeRate;

public interface ExchangeRateMapper {
    int deleteByPrimaryKey(Integer currencyId);

    int insert(ExchangeRate record);

    int insertSelective(ExchangeRate record);

    ExchangeRate selectByPrimaryKey(Integer currencyId);

    int updateByPrimaryKeySelective(ExchangeRate record);

    int updateByPrimaryKey(ExchangeRate record);
}