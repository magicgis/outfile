package com.naswork.service;

import java.util.List;

import javax.annotation.Resource;

import com.naswork.dao.CompetitorDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Competitor;
import com.naswork.model.CompetitorQuote;
import com.naswork.model.CompetitorQuoteElement;
import com.naswork.model.CompetitorSupplier;
import com.naswork.module.marketing.controller.clientinquiry.CompetitorVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface CompetitorService {
	
	int insertSelective(Competitor record);

    Competitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Competitor record);

	public List<CompetitorVo> getCode(Integer clientInquiryId);
	
	public List<Integer> getElementId(Integer clientInquiryId);
	
	public List<CompetitorQuoteElement> getPrice(ClientInquiryElement clientInquiryElement);
	
	public CompetitorVo findByCode(Integer code);
	
	public List<CompetitorQuote> getfreight(Integer clientInquiryId);
	
	/*
	 * 竞争对手
	 */
	public void competitorPage(PageModel<Competitor> page,String where,GridSort sort);
	
	/*
	 * 按代码查询
	 */
	public List<Competitor> selectByCode(String code);
	
	public Integer getMaxId();
	
	public void addMapSupplierAndCompetitor(Competitor competitor)throws Exception;
	
	public void getMapSupplierAndCompetitorList(PageModel<CompetitorSupplier> page);
	
	public void delMapSupplierAndCompetitor(Integer id)throws Exception;
	
	public int getCountByClientAndSupplier(Integer clientId,Integer supplierId);
	
}
