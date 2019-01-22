package com.naswork.service;

import java.util.List;

import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientInquiry;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierQuoteService {
	
	void findSupplierQuotePage(PageModel<SupplierQuoteVo> page, String searchString, GridSort sort);
	
	void findAirType(PageModel<SystemCode> page, String searchString, GridSort sort);
	
	void findClientInquirypage(PageModel<ClientInquiry> page, String searchString, GridSort sort);
	
	void findWeatherQuote(PageModel<ClientInquiry> page, String searchString, GridSort sort);
	
	List<ClientInquiry> findClientInquiryPage(PageModel<ClientInquiry> page, String searchString, GridSort sort);
	
	void findSupplierQuoteElementPage(PageModel<SupplierQuoteVo> page, String searchString, GridSort sort);
	
	List<SupplierQuoteVo> findSupplierQuoteElement(Integer id);
	
	public int insertSelective(SupplierQuote record);

    public SupplierQuote selectByPrimaryKey(Integer id);

	void updateByPrimaryKey(SupplierQuote record);
	
	List<ListDateVo> findsid(AuthorityRelation authorityRelation);
	
	List<ListDateVo> findcid();
	
	List<ListDateVo> findbizid();
	
	List<ListDateVo> findairid();
	
	List<ListDateVo> findcond();
	
	List<ClientInquiry> findClientInquiry(ListDateVo vo);
	
	List<ClientInquiry> findSupplierQuote(Integer id,Integer item);
	
	List<ListDateVo> findcert();
	
	List<ListDateVo> findsqstauts();
	
	public List<Integer> getCurrencyId(Integer id);
	
	public Integer findBySupplierInquiryId(Integer supplierInquiryId);
	
	SupplierQuote lookSupplierInquiry(Integer supplierInquiryId,Integer currencyId,Double exchangeRate);

	List<SupplierInquiryStatistic> supplierQuoteStat(SupplierInquiryStatistic supplierInquiryStatistic);
	
	public void updateByPrimaryKeySelective(SupplierQuote record);
}
