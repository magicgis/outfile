package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.vo.PageModel;

public interface SupplierCommissionSaleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierCommissionSale record);

    int insertSelective(SupplierCommissionSale record);

    SupplierCommissionSale selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierCommissionSale record);

    int updateByPrimaryKey(SupplierCommissionSale record);
    
    public List<SupplierCommissionSale> listPage(PageModel<SupplierCommissionSale> page);
    
    public List<Integer> getSupplierIdByInquiryId(Integer clientInquiryId);
    
    public List<Integer> getSupplierIdByClientInquiryId(Integer clientInquiryId);
    
    public SupplierCommissionSale selectByCrawlId(Integer id);
    
    public List<SupplierCommissionSale> getCrawlStockList();
}