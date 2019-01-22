package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.CompetitorDao;
import com.naswork.dao.CompetitorSupplierDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Competitor;
import com.naswork.model.CompetitorQuote;
import com.naswork.model.CompetitorQuoteElement;
import com.naswork.model.CompetitorSupplier;
import com.naswork.module.marketing.controller.clientinquiry.CompetitorVo;
import com.naswork.service.CompetitorService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("competitorService")
public class CompetitorServiceImpl implements CompetitorService {

	@Resource
	private CompetitorDao competitorDao;
	@Resource
	private CompetitorSupplierDao competitorSupplierDao;
	
	public int insertSelective(Competitor record){
		return competitorDao.insertSelective(record);
	}

	public Competitor selectByPrimaryKey(Integer id){
    	return competitorDao.selectByPrimaryKey(id);
    }

	public int updateByPrimaryKeySelective(Competitor record){
    	return competitorDao.updateByPrimaryKeySelective(record);
    }
	
	public List<CompetitorVo> getCode(Integer clientInquiryId) {
		return competitorDao.getCode(clientInquiryId);
	}
	
	
	public List<Integer> getElementId(Integer clientInquiryId){
		return competitorDao.getElementId(clientInquiryId);
	}
	
	public List<CompetitorQuoteElement> getPrice(ClientInquiryElement clientInquiryElement){
		return competitorDao.getPrice(clientInquiryElement);
	}
	
	
	public CompetitorVo findByCode(Integer code){
		return competitorDao.findByCode(code.toString());
	}

	public List<CompetitorQuote> getfreight(Integer clientInquiryId){
		return competitorDao.getfreight(clientInquiryId);
	}
	
	public void competitorPage(PageModel<Competitor> page,String where,GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(competitorDao.competitorPage(page));
	}
	
	public List<Competitor> selectByCode(String code){
		return competitorDao.selectByCode(code);
	}
	
	public Integer getMaxId(){
		return competitorDao.getMaxId();
	}
	
	public void addMapSupplierAndCompetitor(Competitor competitor)throws Exception{
		String[] clients = competitor.getClients().split(",");
		for (int i = 0; i < clients.length; i++) {
			CompetitorSupplier competitorSupplier = new CompetitorSupplier();
			competitorSupplier.setClientId(new Integer(clients[i]));
			competitorSupplier.setSupplierId(competitor.getSupplierId());
			competitorSupplier.setCompetitorId((competitor.getId()));
			competitorSupplierDao.insertSelective(competitorSupplier);
		}
	}
	
	public void delMapSupplierAndCompetitor(Integer id)throws Exception{
		competitorSupplierDao.deleteByPrimaryKey(id);
	}
	
	public void getMapSupplierAndCompetitorList(PageModel<CompetitorSupplier> page){
		page.setEntities(competitorSupplierDao.listPage(page));
	}
	
	public int getCountByClientAndSupplier(Integer clientId,Integer supplierId){
		return competitorSupplierDao.getCountByClientAndSupplier(clientId, supplierId);
	}
}
