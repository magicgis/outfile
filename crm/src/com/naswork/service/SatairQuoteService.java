package com.naswork.service;

import java.util.List;

import com.naswork.model.SatairQuote;
import com.naswork.model.SatairQuoteElement;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SatairQuoteService {

    public int insertSelective(SatairQuote record);

    public SatairQuote selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(SatairQuote record);
    
    public List<SatairQuote> getNewCrawMessage();
	
    public void createQuote();
    
    public void unSelectEmail();
    
    public void listPage(PageModel<SatairQuoteElement> page, String where,
			GridSort sort);
    
    public void list(PageModel<SatairQuoteElement> page, String where,
			GridSort sort);
}
