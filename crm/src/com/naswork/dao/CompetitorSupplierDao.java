package com.naswork.dao;

import java.util.List;

import com.naswork.model.CompetitorSupplier;
import com.naswork.vo.PageModel;

public interface CompetitorSupplierDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CompetitorSupplier record);

    int insertSelective(CompetitorSupplier record);

    CompetitorSupplier selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompetitorSupplier record);

    int updateByPrimaryKey(CompetitorSupplier record);
    
    public List<CompetitorSupplier> listPage(PageModel<CompetitorSupplier> page);
    
    public int getCountByClientAndSupplier(Integer clientId,Integer supplierId);
}