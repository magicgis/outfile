package com.naswork.service;

import com.naswork.model.ExchangeRate;
import com.naswork.module.system.controller.exchangerate.ExchangeRateVo;
import com.naswork.vo.PageModel;

public interface ExchangeRateService {

    int insertSelective(ExchangeRate record);

    ExchangeRate selectByPrimaryKey(Integer currencyId);

    int updateByPrimaryKeySelective(ExchangeRate record);
    
    /**
     * 列表
     */
    public void listPage(PageModel<ExchangeRateVo> page);
    
    public ExchangeRateVo findById(Integer id);
	
}
