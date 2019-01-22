package com.naswork.service;

import java.util.List;

import com.naswork.model.CompetitorSupplier;
import com.naswork.vo.PageModel;

public interface CompetitorSupplierService {

    void insertSelective(CompetitorSupplier record);

    CompetitorSupplier selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(CompetitorSupplier record);
    
    public List<CompetitorSupplier> listPage(PageModel<CompetitorSupplier> page);
    
    public int getCountByClientAndSupplier(Integer clientId,Integer supplierId);	
}
