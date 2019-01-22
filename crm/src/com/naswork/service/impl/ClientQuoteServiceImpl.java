package com.naswork.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientQuote;
import com.naswork.model.ExchangeRate;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.marketing.controller.clientquote.CompetitorVo;
import com.naswork.module.marketing.controller.clientquote.ProfitStatementVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("clientQuoteService")
public class ClientQuoteServiceImpl implements ClientQuoteService {

	@Resource
	private ClientQuoteDao clientQuoteDao;
	@Resource
	private ClientInquiryService clientInquiryService;

	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	
	@Resource
	private ExchangeRateDao exchangeRateDao;
	
	@Resource
	private ClientInquiryDao clientInquiryDao;
	
	@Resource
	private ClientDao clientDao;
	
	@Override
	public void deleteByPrimaryKey(Integer id) {
		clientQuoteDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(ClientQuote clientQuote) {
		clientQuoteDao.insert(clientQuote);
	}

	@Override
	public void insertSelective(ClientQuote clientQuote) {
		clientQuote.setPrepayRate(clientQuote.getPrepayRate()/100);
		clientQuote.setReceivePayRate(clientQuote.getReceivePayRate()/100);
		clientQuote.setShipPayRate(clientQuote.getShipPayRate()/100);
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(clientQuote.getCurrencyId());
		clientQuote.setExchangeRate(exchangeRate.getRate());
		Integer nextSeq = clientQuoteDao.findMaxSeq()+1;
		clientQuote.setSeq(nextSeq);
		clientQuote.setUpdateTimestamp(new Date());
		BigDecimal a = caculateProfitMargin(new BigDecimal(clientQuote.getProfitMargin()));
		clientQuote.setProfitMargin(a.doubleValue());
//		ClientInquiry clientInquiry = new ClientInquiry();
//		clientInquiry.setId(clientQuote.getClientInquiryId());
//		clientInquiry.setInquiryStatusId(33);
		int count = clientInquiryDao.getQuoteCount(clientQuote);
		if (count>0) {
			StringBuffer quoteNumber = new StringBuffer();
			quoteNumber.append(clientQuote.getQuoteNumber()).append("-").append(count+1);
			clientQuote.setQuoteNumber(quoteNumber.toString());
		}
		clientQuoteDao.insertSelective(clientQuote);
		
//		clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
		
	}
	
	public void insertInHistoryQuote(ClientQuote clientQuote) {
		Integer nextSeq = clientQuoteDao.findMaxSeq()+1;
		clientQuote.setSeq(nextSeq);
		clientQuote.setUpdateTimestamp(new Date());
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientQuote.getClientInquiryId());
		Client client = clientDao.selectByPrimaryKey(clientInquiry.getClientId());
		if (client.getProfitMargin() != null) {
			BigDecimal a = caculateProfitMargin(new BigDecimal(client.getProfitMargin()*100));
			clientQuote.setProfitMargin(a.doubleValue());
		}
		clientInquiry.setInquiryStatusId(33);
		int count = clientInquiryDao.getQuoteCount(clientQuote);
		if (count>0) {
			StringBuffer quoteNumber = new StringBuffer();
			quoteNumber.append(clientInquiry.getQuoteNumber()).append("-").append(count+1);
			clientQuote.setQuoteNumber(quoteNumber.toString());
		}else {
			clientQuote.setQuoteNumber(clientInquiry.getQuoteNumber());
		}
		clientQuote.setQuoteDate(new Date());
		//clientQuote.setQuoteNumber(clientInquiry.getQuoteNumber());
		clientQuoteDao.insertSelective(clientQuote);
		clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
	}

	@Override
	public ClientQuote selectByPrimaryKey(Integer id) {
		return clientQuoteDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ClientQuote clientQuote) {
		BigDecimal profitMargin=new BigDecimal(clientQuote.getProfitMargin());
		clientQuote.setPrepayRate(clientQuote.getPrepayRate()/100);
		clientQuote.setShipPayRate(clientQuote.getShipPayRate()/100);
		clientQuote.setReceivePayRate(clientQuote.getReceivePayRate()/100);
		clientQuote.setProfitMargin(caculateProfitMargin(profitMargin).doubleValue());
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(clientQuote.getCurrencyId());
		clientQuote.setExchangeRate(exchangeRate.getRate());
		clientQuoteDao.updateByPrimaryKeySelective(clientQuote);
	}

	@Override
	public void updateByPrimaryKey(ClientQuote clientQuote) {
		clientQuoteDao.updateByPrimaryKey(clientQuote);
	}

	@Override
	public void findClientQuotePage(PageModel<ClientQuoteVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ClientQuoteVo> clientquotelist=clientQuoteDao.findPage(page);
		page.setEntities(clientquotelist);
	}


	@Override
	public void findClientQuoteElementPage(PageModel<ClientQuoteElementVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		List<ClientQuoteElementVo> clientquoteelementlist=clientQuoteElementDao.findElement(page);
		
		page.setEntities(clientquoteelementlist);
	}
	
	@Override
	public void findQuoteDatePage(PageModel<ClientQuoteElementVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ClientQuoteElementVo> clientquoteelementlist=clientQuoteElementDao.findQuoteDatePage(page);
		page.setEntities(clientquoteelementlist);
	}
	
	@Override
	public void getElementForListPage(PageModel<ClientQuoteElementVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ClientQuoteElementVo> clientquoteelementlist=clientQuoteElementDao.getElementForList(page);
		page.setEntities(clientquoteelementlist);
		
	}
	
	@Override
	public List<CompetitorVo> findcompetitor(Integer ClientInquiryElementId) {
		List<CompetitorVo> competitorlist= clientQuoteElementDao.findcompetitor(ClientInquiryElementId);
		return competitorlist;
	}

	
	@Override
	public List<ClientQuoteVo> searchclient(AuthorityRelation authorityRelation ) {
		return clientQuoteDao.searchclient(authorityRelation);
	}

	@Override
	public List<ClientQuoteVo> searchairType() {
		return clientQuoteDao.searchairType();
	}

	@Override
	public List<ClientQuoteVo> searchbizType() {
		return clientQuoteDao.searchbizType();
	}

	@Override
	public List<ClientQuoteVo> searchcurrency() {
		return clientQuoteDao.searchcurrency();
	}

	@Override
	public Integer findseq(int clientquoteid) {
		return clientQuoteDao.findseq(clientquoteid);
	}

	public BigDecimal caculateProfitMargin(BigDecimal revenueRate) {
		return new BigDecimal(100.00).divide(
				new BigDecimal(100).subtract(revenueRate), 2,
				BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal caculateRevenueRate(BigDecimal profitMargin) {
		return profitMargin.subtract(BigDecimal.ONE)
				.divide(profitMargin, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100));
	}

	
	public BigDecimal caculatePrice(BigDecimal price, BigDecimal baseER,
			BigDecimal exchangeRate) {
		price = price.multiply(baseER).divide(exchangeRate, 4,
				BigDecimal.ROUND_HALF_UP);
		return price;
	}
	
	@Override
	public  List<ClientQuoteElementVo> findElementid(PageModel<ClientQuoteElementVo> page, GridSort sort) {
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		return clientQuoteElementDao.findElementidPage(page);
		
	}

	@Override
	public List<ClientQuoteVo> findbyclientquoteid(String clientquoteid) {
		return clientQuoteDao.findbyclientquoteid(clientquoteid);
	}

	@Override
	public List<ClientQuoteElementVo> findbyelementid(Integer elementid) {
		return clientQuoteElementDao.findbyelementid(elementid);
	}

	@Override
	public List<ClientQuoteElementVo> findbycieid(String clientquteelementid) {
		return clientQuoteElementDao.findbycieid(clientquteelementid);
	}

	@Override
	public List<ClientQuoteElementVo> findElementDate(String clientquoteid) {
		return clientQuoteElementDao.findElementDate(clientquoteid);
	}

	@Override
	public void findQuoteProfitStatementpage(PageModel<ProfitStatementVo> page) {
		List<ProfitStatementVo> cqeList=clientQuoteDao.findQuoteProfitStatementpage(page);
		page.setEntities(cqeList);
	}
	
	@Override
	public void findOrderProfitStatementpage(PageModel<ProfitStatementVo> page) {
		List<ProfitStatementVo> cqeList=clientQuoteDao.findOrderProfitStatementpage(page);
		page.setEntities(cqeList);
	}

	@Override
	public List<ClientOrderVo> findByCqId(String id) {
		return clientQuoteDao.findByCqId(id);
	}

	@Override
	public List<ClientQuoteElementVo> findQuoteDataByCieId(Integer id) {
		return clientQuoteDao.findQuoteDataByCieId(id);
	}
	
	@Override
	public List<ClientQuoteElementVo> findOrderDataByCieId(Integer id) {
		return clientQuoteDao.findOrderDataByCieId(id);
	}

	@Override
	public List<ProfitStatementVo> findQuoteProfitStatement(String id) {
		return clientQuoteDao.findQuoteProfitStatement(id);
	}
	
	@Override
	public List<ProfitStatementVo> findOrderProfitStatement(String id) {
		return clientQuoteDao.findOrderProfitStatement(id);
	}

	@Override
	public List<ClientQuoteElementVo> findClientInquiry(Integer clientInquiryId) {
		return clientQuoteElementDao.findClientInquiry(clientInquiryId);
	}

	@Override
	public List<ProfitStatementVo> findAlterNumber(Integer elementId) {
		return clientQuoteDao.findAlterNumber(elementId);
	}

	public Integer getCurrencyId(Integer id) {
		return clientQuoteDao.getCurrencyId(id);
	}

	@Override
	public ClientQuote lookclientquote(ClientInquiry ci,Integer currencyId,Double exchangeRate) {
		ClientQuote clientQuote=clientQuoteDao.findclientquote(ci.getId());
		if(null==clientQuote){
			clientQuote=new ClientQuote();
			clientQuote.setClientInquiryId(ci.getId());
			clientQuote.setQuoteDate(Calendar.getInstance().getTime());
			clientQuote.setCurrencyId(currencyId);
			clientQuote.setExchangeRate(exchangeRate);
			clientQuote.setQuoteNumber(ci.getQuoteNumber());
			clientQuote.setSeq(1);
			clientQuote.setProfitMargin(BigDecimal.ONE.doubleValue());
			clientQuoteDao.insertSelective(clientQuote);
		}
		return clientQuote;
	}

	public Double getQuoteCount(Integer clientQuoteId){
		return clientQuoteDao.getQuoteCount(clientQuoteId);
	}

	@Override
	public List<ProfitStatementVo> findSupplierOrderAmount(Integer clientOrderElementId) {
		return clientQuoteDao.findSupplierOrderAmount(clientOrderElementId);
	}

	@Override
	public List<ClientQuoteElementVo> findCodeByCoId(String clientOrderId) {
		return clientQuoteDao.findCodeByCoId(clientOrderId);
	}

	@Override
	public void findOrderWeatherProfitStatementpage(PageModel<ProfitStatementVo> page) {
		List<ProfitStatementVo> cqeList=clientQuoteDao.findOrderWeatherProfitStatementpage(page);
		page.setEntities(cqeList);		
	}

	@Override
	public List<ProfitStatementVo> findProfitStatement(String id) {
		return clientQuoteDao.findProfitStatement(id);
	}

	@Override
	public void profitStatisticsPage(PageModel<ProfitStatementVo> page, String searchString, GridSort sort) {
		String buffer = null;
		if(null!=page.getString("partNumber")&&!"".equals(page.getString("partNumber"))){
			if("eq".equals(page.getString("check"))){
				String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
				 buffer ="( e1.part_number_code = "+"'"+partNumberCode+"'"+"or e2.part_number_code = "+"'"+partNumberCode+"'"+")";
			}else{
			String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
			 buffer ="( e1.part_number_code LIKE "+"'%"+partNumberCode+"%'"+"or e2.part_number_code LIKE "+"'%"+partNumberCode+"%'"+")";
			}
			 if(null!=searchString&&!"".equals(searchString)){
				 searchString=buffer+" and "+searchString;
			 }else{
				 searchString=buffer;
			 }
		}
		
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ProfitStatementVo> cqeList=clientQuoteDao.profitStatisticsPage(page);
		page.setEntities(cqeList);			
	}
	
	public static String getCodeFromPartNumber(String partNumber) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < partNumber.length(); i++) {
			char ch = partNumber.charAt(i);
			/*if (isValidCharacter(ch)) {
				buffer.append(Character.toUpperCase(ch));
			}*/
			String regex = "[a-z0-9A-Z\u4e00-\u9fa5]";//其他需要，直接修改正则表达式就好
			if (String.valueOf(ch).matches(regex)) {
				buffer.append(String.valueOf(ch));
			}
		}
		return buffer.toString();
	}
	
	public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
	}

	@Override
	public Double getQuotePrice(Integer clientQuoteId) {
		return clientQuoteDao.getQuotePrice(clientQuoteId);
	}

	@Override
	public void selectByclientInquiryId(PageModel<ClientQuote> page) {
		page.setEntities(clientQuoteDao.selectByclientInquiryId(page)) ;
	}

	@Override
	public List<ClientQuoteElementVo> findElementDateByids(ClientQuote record) {
		return clientQuoteElementDao.findElementDateByids(record);
	}
	
	public List<ClientQuote> getByClientInquiryId(Integer clientInquiryId){
		return clientQuoteDao.getByClientInquiryId(clientInquiryId);
	}
	
	/*public List<ClientQuote> getByClientInquiryId(Integer clientInquiryId){
		return clientQuoteDao.getByClientInquiryId(clientInquiryId);
	}*/
}
