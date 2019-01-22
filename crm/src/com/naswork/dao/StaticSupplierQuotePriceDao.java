package com.naswork.dao;

import java.util.List;

import com.naswork.model.StaticSupplierQuotePrice;
import com.naswork.vo.PageModel;

public interface StaticSupplierQuotePriceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StaticSupplierQuotePrice record);

    int insertSelective(StaticSupplierQuotePrice record);

    StaticSupplierQuotePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaticSupplierQuotePrice record);

    int updateByPrimaryKey(StaticSupplierQuotePrice record);
    
    public List<StaticSupplierQuotePrice> listPage(PageModel<StaticSupplierQuotePrice> page);
    
    public List<StaticSupplierQuotePrice> findByPart(String partNumber);
    
}