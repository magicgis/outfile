package com.naswork.dao;

import java.util.List;

import com.naswork.model.StockMarketSupplierMap;
import com.naswork.vo.PageModel;
import org.apache.ibatis.annotations.Param;

public interface StockMarketSupplierMapDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StockMarketSupplierMap record);

    int insertSelective(StockMarketSupplierMap record);

    StockMarketSupplierMap selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StockMarketSupplierMap record);

    int updateByPrimaryKey(StockMarketSupplierMap record);
    
    public List<StockMarketSupplierMap> listPage(PageModel<StockMarketSupplierMap> page);
    
    public StockMarketSupplierMap checkRecord(StockMarketSupplierMap stockMarketSupplierMap);

    List<StockMarketSupplierMap> getSupplierIdByAirTypeId(Integer id);
}