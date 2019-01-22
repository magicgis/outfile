package com.naswork.dao;

import java.util.List;

import com.naswork.model.LocationFeeForExchangeBillMapping;

public interface LocationFeeForExchangeBillMappingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationFeeForExchangeBillMapping record);

    int insertSelective(LocationFeeForExchangeBillMapping record);

    LocationFeeForExchangeBillMapping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LocationFeeForExchangeBillMapping record);

    int updateByPrimaryKey(LocationFeeForExchangeBillMapping record);
    
    public List<LocationFeeForExchangeBillMapping> getByLocation(String location);
    
}