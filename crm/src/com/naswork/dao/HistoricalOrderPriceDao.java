package com.naswork.dao;

import java.util.List;

import com.naswork.model.HistoricalOrderPrice;
import com.naswork.vo.PageModel;

public interface HistoricalOrderPriceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(HistoricalOrderPrice record);

    int insertSelective(HistoricalOrderPrice record);

    HistoricalOrderPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HistoricalOrderPrice record);

    int updateByPrimaryKey(HistoricalOrderPrice record);
    
    public List<HistoricalOrderPrice> clientPage(PageModel<HistoricalOrderPrice> page);
    
    public List<HistoricalOrderPrice> supplierPage(PageModel<HistoricalOrderPrice> page);
    
    public List<HistoricalOrderPrice> selectByCLientInquiryId(Integer clientInuqiryId);
    
    public List<HistoricalOrderPrice> selectByBsn(String bsn);
    
    public HistoricalOrderPrice getPriceByBsn(String bsn);
    
    public List<HistoricalOrderPrice> getByClient(String clientId,String bsn,String year);
    
    public List<HistoricalOrderPrice> getByPart(String partNumber);
}