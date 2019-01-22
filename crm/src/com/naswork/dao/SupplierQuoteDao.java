package com.naswork.dao;

import java.util.List;

import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientInquiry;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.PageModel;

public interface SupplierQuoteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierQuote record);

    int insertSelective(SupplierQuote record);
    
    List<ClientInquiry> findClientInquiry(ListDateVo vo);
    
    List<ClientInquiry> findSupplierQuote(Integer id,Integer item);
    
    List<ClientInquiry> findSupplierQuoteForCom(Integer id,Integer item);
    
    int findsupplier(Integer clientinquiryid);
    
    SupplierQuote selectByPrimaryKey(Integer id);
    
	List<SupplierQuoteVo> findSupplierQuoteElement(Integer id);

    int updateByPrimaryKeySelective(SupplierQuote record);

    void updateByPrimaryKey(SupplierQuote record);
    
    List<SupplierQuoteVo> findSupplierQuotePage(PageModel<SupplierQuoteVo> page);
    
    List<ClientInquiry> findClientInquiryPage(PageModel<ClientInquiry> page);
    
    List<SupplierQuoteVo> findSupplierQuoteElementPage(PageModel<SupplierQuoteVo> page);
    
    List<SystemCode> findAirType(PageModel<SystemCode> page);
    
    List<ClientInquiry> findClientInquirypage(PageModel<ClientInquiry> page);
    
    List<ClientInquiry> findWeatherQuote(PageModel<ClientInquiry> page);

    List<ListDateVo> findsid(AuthorityRelation authorityRelation);
    
    List<ListDateVo> findcid();
    
    List<ListDateVo> findbizid();
	
	List<ListDateVo> findairid();
	
	List<ListDateVo> findcond();
	
	List<ListDateVo> findcert();
	
	List<ListDateVo> findsqstauts();
	
	public Integer findBySupplierInquiryId(Integer supplierInquiryId);
	
	public List<Integer> getCurrencyId(Integer id);
	
	SupplierQuote findSupplierInquiry(Integer supplierInquiryId);
	
	List<SupplierInquiryStatistic> supplierQuoteStat(SupplierInquiryStatistic supplierInquiryStatistic);
	
	List<SupplierQuote> findSupplierquote(SupplierQuote record);
	
	public List<SupplierQuote> getBySupplierInquiryId(SupplierQuote supplierQuote);
	
	public List<SupplierQuote> selectBySupplierInquiryId(Integer supplierInquiryId);
	
	public List<Integer> getByClientInquiryId(Integer clientInquiryId);
	
	public List<SupplierQuote> getByInquiryIdAndCurrencyId(Integer supplierInquiryId,Integer currencyId);
}