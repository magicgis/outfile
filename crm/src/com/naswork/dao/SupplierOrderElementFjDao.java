package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierOrderElementFj;
import com.naswork.vo.PageModel;

public interface SupplierOrderElementFjDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierOrderElementFj record);

    int insertSelective(SupplierOrderElementFj record);

    SupplierOrderElementFj selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierOrderElementFj record);

    int updateByPrimaryKey(SupplierOrderElementFj record);
    
    public List<SupplierOrderElementFj> getByOrderId(PageModel<String> page);
    
    public List<SupplierOrderElementFj> selectForeignKey(String ids);
}