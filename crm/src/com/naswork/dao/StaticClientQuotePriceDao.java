package com.naswork.dao;

import java.util.List;

import com.naswork.model.StaticClientQuotePrice;
import com.naswork.vo.PageModel;

public interface StaticClientQuotePriceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StaticClientQuotePrice record);

    int insertSelective(StaticClientQuotePrice record);

    StaticClientQuotePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaticClientQuotePrice record);

    int updateByPrimaryKey(StaticClientQuotePrice record);
    
    public List<StaticClientQuotePrice> listPage(PageModel<StaticClientQuotePrice> page);
    
    public List<StaticClientQuotePrice> findByCLientAndPart(String clientId,String partNumber);
    
    public List<Integer> getClients();
    
    public int findByClientId(Integer clientId);
    
}