package com.naswork.dao;

import java.util.List;

import org.jbpm.pvm.internal.query.Page;

import com.naswork.model.ClientInquiryElement;
import com.naswork.model.SatairQuoteElement;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.purchase.controller.supplierquote.SupplierInquiryEmelentVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.PageModel;

public interface SupplierQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierQuoteElement record);

    int insertSelective(SupplierQuoteElement record);
    
    int insertelement(SupplierQuoteElement record);

    SupplierQuoteElement selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(SupplierQuoteElement record);
    
    void updateBySupplierQuoteId(SupplierQuoteElement record);

    void updateByPrimaryKey(SupplierQuoteElement record);
    
    public Integer findMaxItem(Integer id);
    
    int insertSelective(ClientInquiryElement record);
    
    List<SupplierQuoteVo> findsupplierquote(Integer id);
    
    List<SupplierInquiryEmelentVo> findsupplierinquiryelement(Integer supplierinquiryid,Integer supplierQuoteId);
    
    public List<SupplierQuoteElement> listByPartNumberPage(PageModel<SupplierQuoteElement> page);
    
    public List<SupplierQuoteElement> findByClientInquiryElementId(Integer clientInquiryElementId);
    
    public SupplierQuoteElement selectBySupplierOrderElementId(Integer supplierOrderElementId);
    
    SupplierQuoteElement selectQuoteByElementId(Integer supplierInquiryId,Integer elementId);
    
    public Integer getTaxReimbursementId(Integer supplierQuoteElementId);
    
    public Double getLatestPrice(String partNumber);
    
    SupplierQuoteElement findSupplierQuoteElement(SupplierQuoteElement record);
    
    public List<SupplierQuoteElement> repairListByPartNumberPage(PageModel<SupplierQuoteElement> page);
    
    public List<SupplierQuoteElement> getByShortPartForHis(PageModel<SupplierQuoteElement> page);
    
    public List<SupplierQuoteElement> getByShortPart(PageModel<SupplierQuoteElement> page);
    
    public List<SupplierQuoteElement> getByOrderELementId(PageModel<String> page);
    
    public int getByPartInTwoWeeks(String partNumber,String supplierId);
    
    public List<SupplierQuoteElement> getCompetitorPrice(String clientId,String shortPart);
    
    public Double getCountByQuoteId(Integer quoteId);
    
    public List<SupplierQuoteElement> getBySupplierQuoteId(Integer supplierQuoteId);
    
    public List<SupplierQuoteElement> getStorage(PageModel<SupplierQuoteElement> page);
    
    public Double getTotalByQuoteId(Integer supplierQuoteId);
    
    public List<SupplierQuoteElement> getHighPriceCrawlPage(PageModel<SupplierQuoteElement> page);
    
}