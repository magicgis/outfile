package com.naswork.dao;

import java.util.List;

import com.naswork.model.ExchangeRate;
import com.naswork.module.system.controller.exchangerate.ExchangeRateVo;
import com.naswork.vo.PageModel;

public interface ExchangeRateDao {
    int deleteByPrimaryKey(Integer currencyId);

    int insert(ExchangeRate record);

    int insertSelective(ExchangeRate record);

    ExchangeRate selectByPrimaryKey(Integer currencyId);

    int updateByPrimaryKeySelective(ExchangeRate record);

    int updateByPrimaryKey(ExchangeRate record);
    
    public List<ExchangeRateVo> listPage(PageModel<ExchangeRateVo> page);
    
    public ExchangeRateVo findById(Integer id);
}