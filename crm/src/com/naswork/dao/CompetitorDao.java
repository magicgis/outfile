package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Competitor;
import com.naswork.model.CompetitorQuote;
import com.naswork.model.CompetitorQuoteElement;
import com.naswork.module.marketing.controller.clientinquiry.CompetitorVo;
import com.naswork.vo.PageModel;

public interface CompetitorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Competitor record);

    int insertSelective(Competitor record);

    Competitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Competitor record);

    int updateByPrimaryKey(Competitor record);
    
    public List<CompetitorVo> getCode(Integer clientInquiryId);
    
    public List<Integer> getElementId(Integer clientInquiryId);
    
    public List<CompetitorQuoteElement> getPrice(ClientInquiryElement clientInquiryElement);
    
    public CompetitorVo findByCode(String code);
    
    public List<CompetitorQuote> getfreight(Integer clientInquiryId);
    
    public List<Competitor> competitorPage(PageModel<Competitor> page);
    
    public List<Competitor> selectByCode(String code);
    
    public Integer getMaxId();
}