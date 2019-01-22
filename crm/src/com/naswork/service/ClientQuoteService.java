package com.naswork.service;

import java.math.BigDecimal;
import java.util.List;

import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.marketing.controller.clientquote.CompetitorVo;
import com.naswork.module.marketing.controller.clientquote.ProfitStatementVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

public interface ClientQuoteService {
	  void deleteByPrimaryKey(Integer id);

	    void insert(ClientQuote record);

	    void insertSelective(ClientQuote record);
	    
	    public void insertInHistoryQuote(ClientQuote clientQuote);

	    ClientQuote selectByPrimaryKey(Integer id);

	    void updateByPrimaryKeySelective(ClientQuote record);

	    void updateByPrimaryKey(ClientQuote record);

		void findClientQuotePage(PageModel<ClientQuoteVo> page, String searchString, GridSort sort);
		
		List<ClientQuoteVo> findbyclientquoteid(String clientquoteid);
		
		List<ClientQuoteElementVo> findElementDate(String clientquoteid);
		
		List<ClientQuoteElementVo> findElementDateByids(ClientQuote record);
		
		List<ClientQuoteElementVo> findbyelementid(Integer elementid);
		
		List<ClientQuoteElementVo> findbycieid(String clientquteelementid);
		
		List<ClientQuoteElementVo> findElementid(PageModel<ClientQuoteElementVo> page, GridSort sort);
		
		 List<ClientQuoteElementVo> findClientInquiry(Integer clientInquiryId);
		
		List<CompetitorVo> findcompetitor(Integer ClientInquiryElementId);
		
		List<ProfitStatementVo> findOrderProfitStatement(String id);
		
		List<ProfitStatementVo> findProfitStatement(String id);
		
		List<ProfitStatementVo> findQuoteProfitStatement(String id);
		
		void findQuoteProfitStatementpage(PageModel<ProfitStatementVo> page);
		
		void findOrderProfitStatementpage(PageModel<ProfitStatementVo> page);
		
		void profitStatisticsPage(PageModel<ProfitStatementVo> page, String searchString, GridSort sort);
		
		void findOrderWeatherProfitStatementpage(PageModel<ProfitStatementVo> page);
		
		List<ClientOrderVo> findByCqId(String id);
		
		List<ClientQuoteElementVo> findQuoteDataByCieId(Integer id);
		
		List<ClientQuoteElementVo> findCodeByCoId(String clientOrderId);
		
		List<ProfitStatementVo> findSupplierOrderAmount(Integer clientOrderElementId);

		List<ClientQuoteElementVo> findOrderDataByCieId(Integer id);
		
		List<ClientQuoteVo> searchclient(AuthorityRelation authorityRelation );
		  
		List<ClientQuoteVo> searchairType();
		    
		List<ClientQuoteVo> searchbizType();
		    
		List<ClientQuoteVo> searchcurrency();
		 
		void findClientQuoteElementPage(PageModel<ClientQuoteElementVo> page, String searchString, GridSort sort);
		
		Integer findseq(int id);
		
		public BigDecimal caculateProfitMargin(BigDecimal revenueRate);
		
		public BigDecimal caculateRevenueRate(BigDecimal profitMargin);
		
		public BigDecimal caculatePrice(BigDecimal price, BigDecimal baseER,
				BigDecimal exchangeRate) ;

		void findQuoteDatePage(PageModel<ClientQuoteElementVo> page, String searchString, GridSort sort);
		
		public void getElementForListPage(PageModel<ClientQuoteElementVo> page, String searchString, GridSort sort);
		
		 List<ProfitStatementVo> findAlterNumber(Integer elementId);
		 
		 public Integer getCurrencyId(Integer id);
		 
		 ClientQuote lookclientquote(ClientInquiry ci,Integer currencyId,Double exchangeRate);
		 
		 public Double getQuoteCount(Integer clientQuoteId);
		 
		 public Double getQuotePrice(Integer clientQuoteId);
		 
		void selectByclientInquiryId(PageModel<ClientQuote> page);
		
		public List<ClientQuote> getByClientInquiryId(Integer clientInquiryId);
		
		/*public List<ClientQuote> getByClientInquiryId(Integer clientInquiryId);*/
}
