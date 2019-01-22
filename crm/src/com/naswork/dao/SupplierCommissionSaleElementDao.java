package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.module.storage.controller.suppliercommissionsale.CountStockMarketVo;
import com.naswork.vo.PageModel;

public interface SupplierCommissionSaleElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierCommissionSaleElement record);

    int insertSelective(SupplierCommissionSaleElement record);

    SupplierCommissionSaleElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierCommissionSaleElement record);

    int updateByPrimaryKey(SupplierCommissionSaleElement record);
    
    public List<SupplierCommissionSaleElement> listPage(PageModel<SupplierCommissionSaleElement> page);
    
    public List<SupplierCommissionSaleElement> selectBySupplierCommissionSaleId(Integer supplierCommissionSaleId);
    
    public List<SupplierCommissionSaleElement> getDistinctWithSaleId(Integer supplierCommissionSaleId);
    
    public SupplierCommissionSaleElement getCountMessage(String partNumber);
    
    public List<Double> getCountMessageAverage(String partNumber);
    
    public SupplierCommissionSaleElement getSourcePrice(String id,String partNumber);
    
    public List<SupplierCommissionSaleElement> getCrawlSupplierList(PageModel<SupplierCommissionSaleElement> page);
    
    public Integer checkRecord(Integer supplierCommissionSaleElementId,Integer supplierId);
    
    public List<Map<String, Object>> stockMarketCountMessage(PageModel<String> page);
    
    public List<String> getSuppliers(Integer id);
    
    public List<CountStockMarketVo> getSvAmount(PageModel<String> page);
    
    public List<CountStockMarketVo> getOhAmount(PageModel<String> page);
    
    public List<CountStockMarketVo> getNeAmount(PageModel<String> page);
    
    public List<CountStockMarketVo> getArAmount(PageModel<String> page);
    
    public List<String> getDateAmount(PageModel<String> page);
    
    public List<String> getSupplierList(Integer id);
    
    public Integer checkCrawlRecord(Integer id);
    
    public List<CountStockMarketVo> getStockCrawlListPage(PageModel<CountStockMarketVo> page);
    
    public List<CountStockMarketVo> getStockCrawlElementPage(PageModel<CountStockMarketVo> page);
    
    public Integer checkCrawlRecordById(Integer id);
    
    public void insertStockMarket(String today,String id);
    
    public Integer getStockLastInsert();
    
    public Double getDataForPie(PageModel<String> page);
    
    public List<String> getSupplierCodeByCrawlMessageId(Integer id);
    
    public List<SupplierCommissionSaleElement> getCommissionElementByCrawlMessageId(Integer id);
    
    public Double getCrawlAmountBySupplier(PageModel<String> page);
    
    public List<Map<String, String>> getCountMessageForExcel(PageModel<String> page);
    
    public List<Map<String, String>> getTotalBySupplier(Integer id);
    
    public List<Map<String, String>> getSubTotalBySupplier(PageModel<String> page);
    
}