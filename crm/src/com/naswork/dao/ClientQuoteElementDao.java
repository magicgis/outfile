package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.CompetitorVo;
import com.naswork.vo.PageModel;

public interface ClientQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    void insert(ClientQuoteElement record);

    int insertSelective(ClientQuoteElement record);

    ClientQuoteElement selectByPrimaryKey(Integer id);
    
    List<ClientQuoteElementVo> findElementidPage(PageModel<ClientQuoteElementVo> page);
    
    List<ClientQuoteElementVo> findClientInquiry(Integer clientInquiryId);
    
    List<ClientQuoteElementVo> findbyelementid(Integer elementid);
    
    List<ClientQuoteElementVo> findbycieid(String clientquteelementid);
    
    List<ClientQuoteElementVo> findElementDate(String clientquoteid);
    
    List<CompetitorVo> findcompetitor(Integer ClientInquiryElementId);

    int updateByPrimaryKeySelective(ClientQuoteElement record);

    void updateByPrimaryKey(ClientQuoteElement record);
    
    List<ClientQuoteElementVo> findElement(PageModel<ClientQuoteElementVo> page);
    
    ClientQuoteElementVo findClientQuoteElement(Integer clientquoteid, Integer clientinquiryelementid);
    
    List<ClientQuoteElementVo> findQuoteDatePage(PageModel<ClientQuoteElementVo> page);
    
    public List<ClientQuoteElementVo> getElementForList(PageModel<ClientQuoteElementVo> page);
    
    /*
     * 根据询价明细ID查询报价明细ID
     */
    public ClientQuoteElement findCqe(Integer clientInquiryElementId,Integer clientQuoteId);
    
   ClientQuoteElementVo findsupplier(String quoteNumber,String partNumber,String item);
   
   List<ClientQuoteElementVo> findSupplierQuoteElementId(SupplierQuoteElement supplierQuoteElement);
    
    List<ClientQuoteElementVo> findclientquote(String quoteNumber);
    
	 List<ClientQuoteElementVo> findElementDateByids(ClientQuote record);
	 
	public List<ClientQuoteElementVo> selectByElementId(String partCode);
	
	ClientQuoteElementVo findClientInuqiry(Integer id);
	
	public List<ClientQuoteElement> getByClientInquiryElementId(Integer clientInquiryElementId,Integer clientQuoteId);
	
	public Double getTotalById(Integer id);
	
	public ClientQuoteElement selectByClientInquiryElementId(Integer clientInquiryElementId);
	
	public void insertCheck(ClientInquiryElement clientInquiryElement);

}