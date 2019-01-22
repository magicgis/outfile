package com.naswork.dao;

import java.util.List;

import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.marketing.controller.clientquote.ProfitStatementVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

public interface ClientQuoteDao {
    void deleteByPrimaryKey(Integer id);

    void insert(ClientQuote record);

    void insertSelective(ClientQuote record);

    ClientQuote selectByPrimaryKey(Integer id);
    
    List<ClientQuoteVo> findbyclientquoteid(String clientquoteid);
    
    List<ProfitStatementVo> findQuoteProfitStatement(String id);
    
    List<ProfitStatementVo> findOrderProfitStatement(String id);
    
	List<ProfitStatementVo> findProfitStatement(String id);
    
    List<ProfitStatementVo> findQuoteProfitStatementpage(PageModel<ProfitStatementVo> page);
    
    List<ProfitStatementVo> findOrderProfitStatementpage(PageModel<ProfitStatementVo> page);
    
    List<ProfitStatementVo> profitStatisticsPage(PageModel<ProfitStatementVo> page);
    
    List<ProfitStatementVo> findOrderWeatherProfitStatementpage(PageModel<ProfitStatementVo> page);
    
    List<ClientOrderVo> findByCqId(String id);
    
    List<ClientQuoteElementVo> findQuoteDataByCieId(Integer id);
    
	List<ClientQuoteElementVo> findCodeByCoId(String clientOrderId);
    
    List<ProfitStatementVo> findSupplierOrderAmount(Integer clientOrderElementId);
    
    List<ClientQuoteElementVo> findOrderDataByCieId(Integer id);
    
    List<ClientQuoteElementVo> findbyelementid(PageModel<ClientQuoteElementVo> page);

    void updateByPrimaryKeySelective(ClientQuote record);

    void updateByPrimaryKey(ClientQuote record);
    
    List<ClientQuoteVo> findPage(PageModel<ClientQuoteVo> page);
    
    List<ClientQuoteElementVo> findElementPage(PageModel<ClientQuoteElementVo> page);
    
    List<ClientQuoteVo> searchclient(AuthorityRelation authorityRelation);
    
    List<ClientQuoteVo> searchairType();
    
    List<ClientQuoteVo> searchbizType();
    
    List<ClientQuoteVo> searchcurrency();
    
    Integer findseq(int clientquoteid);
    
    /*
     * 获取最大seq
     */
    public Integer findMaxSeq();
    
    List<ProfitStatementVo> findAlterNumber(Integer elementId);
    
    public Integer getCurrencyId(Integer clientInquiryId);
    
	 ClientQuote findclientquote(Integer cientinquiryid);
	 
	 public Double getQuoteCount(Integer clientQuoteId);
	 
	 public Double getQuotePrice(Integer clientQuoteId);
	 
	 List<ClientQuote> selectByclientInquiryId(PageModel<ClientQuote> page);
	 
	 public List<ClientQuote> getByClientInquiryId(Integer clientInquiryId);
	 
	 public ClientQuote getMessageForAddQuote(Integer clientInquiryId);
	 
}