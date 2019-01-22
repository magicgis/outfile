package com.naswork.dao;

import java.util.List;

import com.naswork.model.StorehouseAddress;
import com.naswork.vo.PageModel;

public interface StorehouseAddressDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StorehouseAddress record);

    int insertSelective(StorehouseAddress record);

    StorehouseAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StorehouseAddress record);

    int updateByPrimaryKey(StorehouseAddress record);
    
    public List<StorehouseAddress> listPage(PageModel<StorehouseAddress> page);
    
    public List<StorehouseAddress> selectByName(String name);
    
    public List<StorehouseAddress> selectAll();
}