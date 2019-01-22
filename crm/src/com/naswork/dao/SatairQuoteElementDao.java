package com.naswork.dao;

import java.util.List;

import com.naswork.model.SatairQuoteElement;
import com.naswork.vo.PageModel;

public interface SatairQuoteElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SatairQuoteElement record);

    int insertSelective(SatairQuoteElement record);

    SatairQuoteElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SatairQuoteElement record);

    int updateByPrimaryKey(SatairQuoteElement record);
    
    public List<SatairQuoteElement> selecyBySatairQuoteId(String satairQuoteId,String partNumber,String enter);
    
    public List<SatairQuoteElement> selecyByNewSatairQuoteId(String satairQuoteId,String partNumber,String enter);
    
    public Integer getCerIdByCode(String code);
    
    public List<SatairQuoteElement> getEmailList();
    
    public void updateEmailStatus(SatairQuoteElement satairQuoteElement);
    
    public List<SatairQuoteElement> listPage(PageModel<SatairQuoteElement> page);
    
    public List<SatairQuoteElement> getByClientInquiryId(Integer clientInquiryId,Integer id);
    
    public List<SatairQuoteElement> list(PageModel<SatairQuoteElement> page);
}