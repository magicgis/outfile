package com.naswork.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.storage.controller.suppliercommissionsale.CountStockMarketVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

public interface SupplierCommissionSaleElementService {
	
    int insertSelective(SupplierCommissionSaleElement record);

    SupplierCommissionSaleElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierCommissionSaleElement record);
    
    public void listPage(PageModel<SupplierCommissionSaleElement> page);
    
    public MessageVo uploadExcelSecond(MultipartFile multipartFile, Integer id);
    
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id);
    
    public void addWithTable(List<SupplierCommissionSaleElement> list,Integer id);
    
    public void addQuote(Integer supplierCommissionSaleId);
    
    public ResultVo AddInquiryAndCrawlElement(Integer id,Integer airType);
    
    public List<SupplierCommissionSaleElement> getDistinctWithSaleId(Integer id);
    
    public void getCrawlSupplierList(PageModel<SupplierCommissionSaleElement> page,GridSort sort);
    
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
    
    public void crawlStockMarket(Integer id);
    
    public void getStockCrawlListPage(PageModel<CountStockMarketVo> page,GridSort sort);
    
    public void getStockCrawlElementPage(PageModel<CountStockMarketVo> page,GridSort sort,String where);
    
    public Integer checkCrawlRecordById(Integer id);
    
    public void insertStockMarket(String today,String id);
    
    public Integer getStockLastInsert();
    
    public Double getDataForPie(PageModel<String> page);

}
