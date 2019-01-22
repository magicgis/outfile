package com.naswork.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.naswork.dao.*;
import com.naswork.model.*;
import com.naswork.service.EmailRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.EmailTemplateVo;
import com.naswork.module.marketing.controller.partsinformation.PartsInformationVo;
import com.naswork.module.purchase.controller.supplierquote.CrawerVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.SupplierAnnualOfferService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.CrawerUtil;
import com.naswork.utils.EmailCrawerUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.utils.QQMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("clientInquiryElementService")
public class ClientInquiryElementServiceImpl extends Thread implements
		ClientInquiryElementService {

	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ElementDao elementDao;
	@Resource
	private ClientInquiryAlterElementDao clientInquiryAlterElementDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private StaticClientQuotePriceDao staticClientQuotePriceDao;
	@Resource
	private StaticSupplierQuotePriceDao staticSupplierQuotePriceDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private SupplierAnnualOfferService supplierAnnualOfferService;
	@Resource
	private PartAndEmailDao partAndEmailDao;
	@Resource
	private ClientDao clientDao;
	@Resource
	private SupplierContactDao supplierContactDao;
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Resource
	private UserDao userDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private EmailRecordDao emailRecordDao;

	/*
	 * 页面新增明细
	 */
	@Override
	public ResultVo insertSelective(List<ClientInquiryElement> list,Integer clientInquiryId) {
		boolean success = false;
		String message = "";
		Integer maxItem = clientInquiryElementDao.findMaxItem(clientInquiryId);
		Integer nextItem = null;
		if (maxItem!=null) {
			nextItem = clientInquiryElementDao.findMaxItem(clientInquiryId)+1;
		}else{
			nextItem = 1;
		}
		/*if (1==1) {
			throw new NullPointerException("编号为空");
		}*/
		
		//记录行数
		int line=1;
		List<Integer> errorLine = new ArrayList<Integer>();
		List<ClientInquiryElement> eleList = new ArrayList<ClientInquiryElement>();
		for (ClientInquiryElement clientInquiryElement : list) {
			if (clientInquiryElement.getItem()!=null) {
				if (clientInquiryElement.getItem().equals(nextItem)) {
				 	List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
				 	Element element2 = new Element();
				 	if (element.size()==0) {
				 		byte[] p = getCodeFromPartNumber(clientInquiryElement.getPartNumber()).getBytes();
		            	Byte[] pBytes = new Byte[p.length];
		            	for(int j=0;j<p.length;j++){
		            		pBytes[j] = Byte.valueOf(p[j]);
		            	}
		            	element2.setPartNumberCode(pBytes);
		            	element2.setUpdateTimestamp(new Date());
						elementDao.insert(element2);
						clientInquiryElement.setElementId(element2.getId());
					}else {
						clientInquiryElement.setElementId(element.get(0).getId());
					}
				 	clientInquiryElement.setPartNumber(clientInquiryElement.getPartNumber().trim().replaceAll("\r|\t|\n", "").replaceAll("\\xc2|\\xa0", "").replaceAll(" ", ""));
				 	clientInquiryElement.setClientInquiryId(clientInquiryId);
					clientInquiryElement.setUpdateTimestamp(new Date());
					clientInquiryElement.setShortPartNumber(getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
					eleList.add(clientInquiryElement);
				}else {
					errorLine.add(line);
				}
			}
			nextItem++;
			line++;
		}

		
		if (errorLine.size()>0) {
			StringBuffer error = new StringBuffer();
			error.append("第 ");
			for (Integer integer : errorLine) {
				error.append(integer).append(",");
			}
			error.deleteCharAt(error.length()-1);
			error.append(" 行有错误,序号已存在或序号不连续！");
			message = error.toString();
			return new ResultVo(success, message);
		}else if (errorLine.size()==0) {
			for (int i = 0; i < eleList.size(); i++) {
				List<ClientInquiryElement> listInquiryElements = clientInquiryElementDao.findByclientInquiryId(clientInquiryId);
				List<ClientInquiryElement> updateList = new ArrayList<ClientInquiryElement>();
				//String partNumberCode=getShortPartNumber(eleList.get(i).getPartNumber());
				String partNumberCode=clientInquiryService.getCodeFromPartNumber(eleList.get(i).getPartNumber());
				/*List<TPart> plist=tPartDao.selectByPartNumberCode(partNumberCode);
				for (TPart tPart : plist) {
					if(tPart.getIsBlacklist().equals(1)){
						eleList.get(i).setIsBlacklist(1);
						break;
					}
				}*/
				if (eleList.get(i).getAlterPartNumber()!=null && 
					!"".equals(eleList.get(i).getAlterPartNumber())) {
				eleList.get(i).setIsMain(2);
					eleList.get(i).setElementStatusId(700);
					clientInquiryElementDao.insertSelective(eleList.get(i));
					for (int j = 0; j < eleList.size(); j++) {
						if (eleList.get(i).getCsn().equals(eleList.get(j).getCsn()) && 
								!eleList.get(i).getPartNumber().equals(eleList.get(j).getPartNumber())) {
							eleList.get(j).setIsMain(1);
							eleList.get(j).setMainId(eleList.get(i).getId());
						}
					}
				}else {
					List<ClientInquiryElement> lists = clientInquiryElementDao.selectByCsn(eleList.get(i).getCsn(),clientInquiryId);
					if (lists.size()>0) {
						eleList.get(i).setIsMain(1);
						eleList.get(i).setMainId(lists.get(0).getId());
					}
					eleList.get(i).setElementStatusId(700);
					clientInquiryElementDao.insertSelective(eleList.get(i));
					
				}
			}
		}
		success = true;
		message="新增成功！";
		Calendar rightNow=Calendar.getInstance();
		int day=rightNow.get(rightNow.DAY_OF_WEEK);
		int hour = rightNow.get(rightNow.HOUR_OF_DAY);
//			System.out.print("today:"+day+",");
//			System.out.print("time"+hour);
		/*if (day == 7 || day == 0 || (day == 6 && hour >= 18)) {
			boolean email = clientInquiryService.sendEmail(clientInquiryId,0,true);
			//boolean commission = clientInquiryService.supplierCommissionSale(clientInquiryId);
			if (!email) {
				message="新增成功！邮件发送不成功！";
			}
		}else {
			boolean email = clientInquiryService.sendEmail(clientInquiryId,1,false);
			//boolean commission = clientInquiryService.supplierCommissionSale(clientInquiryId);
			if (!email) {
				message="新增成功！邮件发送不成功！";
			}
		}*/
		//boolean email = clientInquiryService.sendEmail(clientInquiryId,1,false);
		//boolean commission = clientInquiryService.supplierCommissionSale(clientInquiryId);
		/*if (!email) {
			message="新增成功！邮件发送不成功！";
		}*/
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientInquiryId);
		int count = supplierInquiryDao.getCountByCLientInuqiryId(clientInquiryId);
		if ((clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId() == 30)&& count>0) {
			clientInquiry.setInquiryStatusId(31);
			clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
		}
//			if (!email && !commission) {
//				message="新增成功！邮件发送不成功！";
//			}
		return new ResultVo(success, message);
	}
	
	/*
	 * 页面新增明细
	 */
	@Override
	public ResultVo insertForCopyTable(List<ClientInquiryElement> list,Integer clientInquiryId) {
		boolean success = false;
		String message = "";
		Integer maxItem = clientInquiryElementDao.findMaxItem(clientInquiryId);
		Integer nextItem = null;
		if (maxItem!=null) {
			nextItem = clientInquiryElementDao.findMaxItem(clientInquiryId)+1;
		}else{
			nextItem = 1;
		}
		//记录行数
		int line=1;
		List<Integer> errorLine = new ArrayList<Integer>();
		List<ClientInquiryElement> eleList = new ArrayList<ClientInquiryElement>();
		for (ClientInquiryElement clientInquiryElement : list) {
			if (clientInquiryElement.getItem()!=null) {
				if (clientInquiryElement.getItem().equals(nextItem)) {
					if (clientInquiryElement.getCondition() != null && !"".equals(clientInquiryElement.getCondition())) {
						List<SystemCode> conList = systemCodeDao.findType("COND");
						for (SystemCode systemCode : conList) {
							if (systemCode.getCode().toLowerCase().equals(clientInquiryElement.getCondition().toLowerCase())) {
								clientInquiryElement.setConditionId(systemCode.getId());
								break;
							}
						}
					}
				 	List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
				 	Element element2 = new Element();
				 	if (element.size()==0) {
				 		byte[] p = getCodeFromPartNumber(clientInquiryElement.getPartNumber()).getBytes();
		            	Byte[] pBytes = new Byte[p.length];
		            	for(int j=0;j<p.length;j++){
		            		pBytes[j] = Byte.valueOf(p[j]);
		            	}
		            	element2.setPartNumberCode(pBytes);
		            	element2.setUpdateTimestamp(new Date());
						elementDao.insert(element2);
						clientInquiryElement.setElementId(element2.getId());
					}else {
						clientInquiryElement.setElementId(element.get(0).getId());
					}
				 	clientInquiryElement.setPartNumber(clientInquiryElement.getPartNumber().trim().replaceAll("\r|\t|\n", "").replaceAll("\\xc2|\\xa0", "").replaceAll(" ", ""));
				 	clientInquiryElement.setClientInquiryId(clientInquiryId);
					clientInquiryElement.setUpdateTimestamp(new Date());
					clientInquiryElement.setShortPartNumber(getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
					eleList.add(clientInquiryElement);
				}else {
					errorLine.add(line);
				}
			}
			nextItem++;
			line++;
		}
		if (errorLine.size()>0) {
			StringBuffer error = new StringBuffer();
			error.append("第 ");
			for (Integer integer : errorLine) {
				error.append(integer).append(",");
			}
			error.deleteCharAt(error.length()-1);
			error.append(" 行有错误,序号已存在或序号不连续！");
			message = error.toString();
			return new ResultVo(success, message);
		}else if (errorLine.size()==0) {
			for (int i = 0; i < eleList.size(); i++) {
				List<ClientInquiryElement> listInquiryElements = clientInquiryElementDao.findByclientInquiryId(clientInquiryId);
				List<ClientInquiryElement> updateList = new ArrayList<ClientInquiryElement>();
				String partNumberCode=clientInquiryService.getCodeFromPartNumber(eleList.get(i).getPartNumber());
				if (eleList.get(i).getAlterPartNumber()!=null && 
					!"".equals(eleList.get(i).getAlterPartNumber())) {
						eleList.get(i).setIsMain(2);
						eleList.get(i).setElementStatusId(700);
						clientInquiryElementDao.insertSelective(eleList.get(i));
						for (int j = 0; j < eleList.size(); j++) {
							if (eleList.get(i).getCsn().equals(eleList.get(j).getCsn()) && 
									!eleList.get(i).getPartNumber().equals(eleList.get(j).getPartNumber())) {
								eleList.get(j).setIsMain(1);
								eleList.get(j).setMainId(eleList.get(i).getId());
							}
						}
				}else {
					List<ClientInquiryElement> lists = clientInquiryElementDao.selectByCsn(eleList.get(i).getCsn(),clientInquiryId);
					if (lists.size()>0) {
						eleList.get(i).setIsMain(1);
						eleList.get(i).setMainId(lists.get(0).getId());
					}
					eleList.get(i).setElementStatusId(700);
					clientInquiryElementDao.insertSelective(eleList.get(i));
					
				}
			}
		}
		success = true;
		message="新增成功！";
		Calendar rightNow=Calendar.getInstance();
		int day=rightNow.get(rightNow.DAY_OF_WEEK);
		int hour = rightNow.get(rightNow.HOUR_OF_DAY);
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientInquiryId);
		int count = supplierInquiryDao.getCountByCLientInuqiryId(clientInquiryId);
		if ((clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId() == 30)&& count>0) {
			clientInquiry.setInquiryStatusId(31);
			clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
		}
		return new ResultVo(success, message);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientInquiryElement record) {
		List<Element> element = elementDao.findIdByPn(record.getPartNumber());
	 	Element element2 = new Element();
	 	if (element.size()==0) {
	 		byte[] p = record.getPartNumber().getBytes();
        	Byte[] pBytes = new Byte[p.length];
        	for(int j=0;j<p.length;j++){
        		pBytes[j] = Byte.valueOf(p[j]);
        	}
        	element2.setPartNumberCode(pBytes);
        	element2.setUpdateTimestamp(new Date());
			elementDao.insert(element2);
			record.setElementId(element2.getId());
		}else {
			record.setElementId(element.get(0).getId());
		}
	 	record.setShortPartNumber(getCodeFromPartNumber(record.getPartNumber()));
		return clientInquiryElementDao.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public void update(ClientInquiryElement record) {
		
		 clientInquiryElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public ClientInquiryElement selectByPrimaryKey(Integer id) {
		return clientInquiryElementDao.selectByPrimaryKey(id);
	}
	
	public List<ClientInquiryElement> findByclientInquiryId(Integer id){
		return clientInquiryElementDao.findByclientInquiryId(id);
	}
	
	public List<ElementVo> getEle(Integer clientInuqiryId){
	    return clientInquiryElementDao.getEle(clientInuqiryId);
	}

	public List<ClientDownLoadVo> getPricesByElementVo(ElementVo elementVo){
		return clientInquiryElementDao.getPricesByElementVo(elementVo);
	}
	
	public ExchangeRate getValue(Integer currencyId) {
		return exchangeRateDao.selectByPrimaryKey(currencyId);
	}
	
	@Override
	public void insertByimportPackage(ClientInquiryElement clientInquiryElement) {
		Integer maxItem = clientInquiryElementDao.findMaxItem(clientInquiryElement.getClientInquiryId());
		int item=0;
		if(null==maxItem){
			item = 0;
		}else{
			item=maxItem;
		}
		clientInquiryElement.setItem(item+1);
		clientInquiryElement.setCsn(item+1);
		clientInquiryElement.setShortPartNumber(getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
		clientInquiryElementDao.insert(clientInquiryElement);
	}

	@Override
	public Integer findMaxItem(Integer id) {
		return clientInquiryElementDao.findMaxItem(id);
	}
	
	public String getCodeFromPartNumber(String partNumber) {
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
	
	public void marketpartPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryElementDao.marketpartPage(page));
	}
	
	public void purchasepartPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		//page.setEntities(clientInquiryElementDao.purchasepart(page));
		page.setEntities(clientInquiryElementDao.purchasepartPage(page));
	}
	
	public void purchasepartPageWithPart(PageModel<PartsInformationVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		//page.setEntities(clientInquiryElementDao.purchasepartWithPart(page));
		page.setEntities(clientInquiryElementDao.purchasepartWithPartPage(page));
	}
	
	public void supplierAbilityPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryElementDao.supplierAbilityPage(page));
	}
	
	public List<ElementVo> getSupplierCode(Integer clientInuqiryId){
		return clientInquiryElementDao.getSupplierCode(clientInuqiryId);
	}
	
	public List<ClientInquiryElement> findCieBySi(Integer supplierInquiryId){
		return clientInquiryElementDao.findCieBySi(supplierInquiryId);
	}
	
    public List<PartsInformationVo> marketPartInformation(PageModel<PartsInformationVo> page){
    	return clientInquiryElementDao.marketPartInformation(page);
    }
    
    public List<PartsInformationVo> purchasePartInformation(PageModel<PartsInformationVo> page){
    	return clientInquiryElementDao.purchasePartInformation(page);
    }
    
    public List<ClientDownLoadVo> getPricesByPartVo(PartsInformationVo partsInformationVo){
		return clientInquiryElementDao.getPricesByPartVo(partsInformationVo);
	}
    
    public List<String> selectByMainId(Integer mainId){
    	return clientInquiryElementDao.selectByMainId(mainId);
    }
    
    public void marketpartAllNullPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName().toLowerCase()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryElementDao.marketpartAllNullPage(page));
    }
    
    public void marketpartCoeNullPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName().toLowerCase()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<PartsInformationVo> list = clientInquiryElementDao.marketpartCoeNullPage(page);
		page.setEntities(list);
    }
    
    public boolean updateBsn(String[] bsns,String[] clientInquiryElementIds){
    	try {
    		for (int i = 0; i < clientInquiryElementIds.length; i++) {
    			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(new Integer(clientInquiryElementIds[i]));
    			clientInquiryElement.setBsn(bsns[i]);
    			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
    			TPart tPart = tPartDao.selectByPrimaryKey(bsns[i]);
    			String[] names = tPart.getPartName().split(",");
    			int flag = 0;
    			for (int k = 0; k < names.length; k++) {
    				if (names[k].equals(clientInquiryElement.getDescription())) {
    					flag = 1;
    					break;
    				}
    			}
    			if (flag==0) {
    				StringBuffer name = new StringBuffer();
    				name.append(tPart.getPartName()).append(",").append(clientInquiryElement.getDescription());
    				tPart.setPartName(name.toString());
    				tPartDao.updateByPrimaryKeySelective(tPart);
    			}
    			
    			//供应商年度报价有此件号直接新增报价
    			supplierAnnualOfferService.addSupplierQuote(clientInquiryElement);
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public void selectByBsnPartPage(PageModel<PartsInformationVo> page){
    	page.setEntities(clientInquiryElementDao.selectByBsnPartPage(page));
    }
    
    public String getShortPartNumber(String partNumber){
    	String shortPart = partNumber.replaceAll("[\\p{Punct}\\p{Space}]+", "");
    	return shortPart;
    }
    
    public void addStaticElement(ClientInquiry clientInquiry,ClientQuote clientQuote) {
		List<ClientInquiryElement> list = clientInquiryElementDao.findByclientInquiryId(clientInquiry.getId());
		for (ClientInquiryElement clientInquiryElement : list) {
			List<StaticClientQuotePrice> clientStaticPrices = staticClientQuotePriceDao.findByCLientAndPart(clientInquiry.getClientId().toString(),clientInquiryElement.getPartNumber());
			List<StaticSupplierQuotePrice> supplierStaticPrices = staticSupplierQuotePriceDao.findByPart(clientInquiryElement.getPartNumber());
			if (supplierStaticPrices.size() > 0 && clientStaticPrices.size() > 0) {
				ClientQuoteElement clientQuoteElement = new ClientQuoteElement();
				clientQuoteElement.setClientInquiryElementId(clientInquiryElement.getId());
				clientQuoteElement.setClientQuoteId(clientQuote.getId());
				if (clientQuote.getCurrencyId().equals(clientStaticPrices.get(0).getCurrencyId())) {
					clientQuoteElement.setPrice(clientStaticPrices.get(0).getPrice());
				}else {
					ExchangeRate quoteExchange = exchangeRateDao.selectByPrimaryKey(clientQuote.getCurrencyId());
					ExchangeRate staticExchange = exchangeRateDao.selectByPrimaryKey(clientStaticPrices.get(0).getCurrencyId());
					Double price = clientStaticPrices.get(0).getPrice() * staticExchange.getRate() / quoteExchange.getRate();
					DecimalFormat    df   = new DecimalFormat("######0.00");
					clientQuoteElement.setPrice( new Double(df.format(price)));
				}
				
				clientQuoteElement.setSupplierQuoteElementId(supplierStaticPrices.get(0).getSupplierQuoteElementId());
				clientQuoteElement.setAmount(clientInquiryElement.getAmount());
				clientQuoteElement.setLeadTime(supplierStaticPrices.get(0).getLeadTime());
				clientQuoteElement.setUpdateTimestamp(new Date());
				clientQuoteElementDao.insertSelective(clientQuoteElement);
			}
		}
	}
    /**
    @Author: Modify by white
    @DateTime: 2018/9/17 16:32
    @Description: 添加“7”开头的客户询价单也进行爬虫
    */
    public void searchSatair(List<ClientInquiryElement> list,Integer clientInquiryId,Integer supplier,Integer commitCrawl,HashMap<String, Boolean> crawlSupplier) {
    	try {
    		final List<ClientInquiryElement> elementList = list;
        	final Integer id = clientInquiryId;
        	final Integer ifSupplier = supplier;
        	final Integer purchaseCommit = commitCrawl;//采购提交爬虫
        	final HashMap<String, Boolean> supplierMap = crawlSupplier;//采购发起的爬虫，需要爬的供应商的列表
        	new Thread() {
        		 public void run() {
        			 try {
        				 Thread.sleep(2000);
					 } catch (InterruptedException e) {
						 e.printStackTrace();
					 }
        			 final List<CrawerVo> partList = new ArrayList<CrawerVo>();
        			 for (ClientInquiryElement clientInquiryElement : elementList) {
        				CrawerVo crawerVo = new CrawerVo();
    					crawerVo.setPn(clientInquiryElement.getPartNumber());
    					crawerVo.setDescrition(clientInquiryElement.getDescription());
    					crawerVo.setId(clientInquiryElement.getId());
    					crawerVo.setAmount(clientInquiryElement.getAmount().toString());
    					partList.add(crawerVo);
        			 }
        			 final ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(id);
        			 Client client = clientDao.selectByPrimaryKey(clientInquiry.getClientId());
        			 String start = " ";
        			 if (client.getCode().startsWith("8")) {
        				 start = "8";
    				 }else if (client.getCode().startsWith("9") || client.getCode().startsWith("-1")) {
    					 start = "9";
    				 }else if (client.getCode().equals("70") || client.getCode().equals("370")) {
    					 start = "70";
    				 }else if(client.getCode().startsWith("7")){
        			 	start = "8";
					 }
        			 if (purchaseCommit.equals(1)) {
        				 start = "9";
        			 }
        			 final String st = start;
        			 if (supplierMap == null || (supplierMap.containsKey("1006") && supplierMap.get("1006"))) {
        				 new Thread() {
            	    		 public void run() {
            	    			 CrawerUtil crawerStroge = new CrawerUtil("http://127.0.0.1:8013/crawAviall", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),st,ifSupplier);
            	    			 crawerStroge.crawMessage();
            	    		 }
            	         }.start();
        			 }
        			 if (supplierMap == null || (supplierMap.containsKey("1003") && supplierMap.get("1003"))) {
    	    	         new Thread() {
    	    	    		 public void run() {
    	    	    			 CrawerUtil crawerStroge = new CrawerUtil("http://127.0.0.1:8014/crawKlx", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),st,ifSupplier);
    	    	    			 crawerStroge.crawMessage();
    	    	    		 }
    	    	         }.start();
        			 }
        			 if (supplierMap == null || (supplierMap.containsKey("1444") && supplierMap.get("1444"))) {
    	    	         new Thread() {
    	    	    		 public void run() {
    	    	    			 CrawerUtil crawerStroge = new CrawerUtil("http://127.0.0.1:8015/crawStroge", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),"",ifSupplier);
    	    	    			 crawerStroge.crawMessage();
    	    	    		 }
    	    	         }.start();
        			 }
        			 if (supplierMap == null || (supplierMap.containsKey("1005") && supplierMap.get("1005"))) {
    	    	         new Thread() {
    	    	    		 public void run() {
    	    	    			 CrawerUtil crawerStroge = new CrawerUtil("http://127.0.0.1:8016/crawKapco", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),st,ifSupplier);
    	    	    			 crawerStroge.crawMessage();
    	    	    		 }
    	    	         }.start();
        			 }
        			 if (supplierMap == null || (supplierMap.containsKey("6012") && supplierMap.get("6012"))) {
    	    	         new Thread() {
    	    	    		 public void run() {
    	    	    			 CrawerUtil crawerStroge = new CrawerUtil("http://127.0.0.1:8017/crawTxtac", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),st,ifSupplier);
    	    	    			 crawerStroge.crawMessage();
    	    	    		 }
    	    	         }.start();
        			}
        			if(st.equals("9") || st.equals("8")){
         		 		new Thread() {
 	   	    	    		public void run() {
 		        		 		CrawerUtil crawerUtil = new CrawerUtil("http://127.0.0.1:8019/crawDasi", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),st,ifSupplier);
 		    	    		    crawerUtil.crawMessage();
 		   	    	    	}
 	   	    	        }.start();
         		 	}
        		 	if (supplierMap == null || (supplierMap.containsKey("1077") && supplierMap.get("1077"))) {
    	    	    	CrawerUtil crawerUtil = new CrawerUtil("http://127.0.0.1:8012/crawSatair", partList,id,clientInquiry.getBizTypeId(),clientInquiry.getQuoteNumber(),st,ifSupplier);
    	    		    crawerUtil.crawMessage();
        		 	}
                 }
             }.start();    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void getEmail(List<ClientInquiryElement> list,Integer clientInquiryId) {
    	final List<ClientInquiryElement> elementList = list;
    	final Integer id = clientInquiryId;
    	new Thread() {
    		 public void run() {
    			 List<CrawerVo> partList = new ArrayList<CrawerVo>();
    			 for (ClientInquiryElement clientInquiryElement : elementList) {
    				CrawerVo crawerVo = new CrawerVo();
					crawerVo.setPn(clientInquiryElement.getPartNumber());
					crawerVo.setDescrition(clientInquiryElement.getDescription());
					crawerVo.setId(clientInquiryElement.getId());
					partList.add(crawerVo);
    			 }
    			 EmailCrawerUtil crawerUtil = new EmailCrawerUtil("http://127.0.0.1:8012/email", partList,id);
    			 crawerUtil.crawMessage();
             }
         }.start();    
	}
    
    public void matchElement(List<ClientInquiryElement> list) {
    	final List<ClientInquiryElement> elementList = list;
    	new Thread() {
    		 public void run() {
    			 for (int i = 0; i < elementList.size(); i++) {
					List<String> bsns = clientInquiryElementDao.getBsnByShortPart(elementList.get(i).getShortPartNumber());
					for (int j = 0; j < bsns.size(); j++) {
						TPart tPart = tPartDao.selectByPrimaryKey(bsns.get(j));
						if (tPart != null) {
							String[] name = tPart.getPartName().trim().split(",");
							boolean ifMatch = false;
							for (int k = 0; k < name.length; k++) {
								if (elementList.get(i).getDescription() != null) {
									if (name[k].toLowerCase().equals(elementList.get(i).getDescription().trim().toLowerCase())) {
										ifMatch = true;
										break;
									}
								}
							}
							if (ifMatch) {
								elementList.get(i).setBsn(bsns.get(0));
								clientInquiryElementDao.updateByPrimaryKeySelective(elementList.get(i));
								break;
							}
						}
					}
					/*if (bsns.size() > 0) {
						elementList.get(i).setBsn(bsns.get(0));
						clientInquiryElementDao.updateByPrimaryKeySelective(elementList.get(i));
					}*/
				}
             }
         }.start();    
	}
    
    public ClientInquiryElement selectByEpeId(Integer exportPackageElementId){
    	return clientInquiryElementDao.selectByEpeId(exportPackageElementId);
    }
    
    public List<ClientInquiryElement> getLotsInquiryElements(PageModel<String> page){
    	return clientInquiryElementDao.getLotsInquiryElements(page);
    }
    
    public List<ClientInquiryElement> getList(){
    	return clientInquiryElementDao.getList();
    }
    
    public List<String> getCageCode(String shortCode){
    	return clientInquiryElementDao.getCageCode(shortCode);
    }
    
    public void updateCageCode(String cageCode,String sn){
    	clientInquiryElementDao.updateCageCode(cageCode, sn);
    }
    
    public void elementPage(PageModel<ClientInquiryElement> page,String where,GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryElementDao.elementPage(page));
    }
    
    public void elementPageForMarketHis(PageModel<ClientInquiryElement> page,String where,GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryElementDao.elementPageForMarketHis(page));
    }
    
    public void getDataOnce(PageModel<ClientInquiryElement> page,String where,GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName().toLowerCase()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryElementDao.getDataOnce(page));
    }
    
    public boolean updateStatus(PageModel<ClientInquiryElement> page){
    	try {
			List<ClientInquiryElement> list = clientInquiryElementDao.getElementList(page);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setInquiryStatus(1);
				clientInquiryElementDao.updateByPrimaryKeySelective(list.get(i));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public void sendByCrawEmail(){
    	sendByCrawEmailHasSupplier();
    	sendByCrawEmailNoSupplier();
    }
    
    public void sendByCrawEmailHasSupplier(){
    	try {
    		List<Integer> supplierIds = partAndEmailDao.getSupplierList();
    		PageModel<String> page = new PageModel<String>();
        	for (Integer supplierId : supplierIds) {
        		page.put("supplierId", supplierId);
    			List<PartAndEmail> record = partAndEmailDao.getBySupplierIdOrEmail(page);
    			List<PartAndEmail> elementList = partAndEmailDao.getElement(page);
    			for (int i = 0; i < record.size(); i++) {
    				record.get(i).setEmailStatus(1);
					partAndEmailDao.updateByPrimaryKeySelective(record.get(i));
				}
    			EmailTemplateVo[] templateVos = clientInquiryService.getEmailTemplates();
				int index = (int) (Math.random() * templateVos.length);
				EmailTemplateVo emailTemplateVo = templateVos[index];
    			ExchangeMail exchangeMail = new ExchangeMail();
    			exchangeMail.setUsername("purchaser@betterairgroup.com");
    			exchangeMail.setPassword("GZBAcom@)!&0817");
    			StringBuffer bodyText = new StringBuffer();
    			List<SupplierContact> emails = supplierContactDao.getEmailList(supplierId);
    			String[] calls = {"Dear sales","Dear sales team","Dear sales team","Dear sales","Dear sales team","Dear sales"};
    			String call = calls[(int)Math.random()*calls.length];
    			bodyText.append("<div>Dear ");
    			bodyText.append(emails.get(0).getName());
    			bodyText.append("</div>");
    			bodyText.append("");
    			bodyText.append("<div>&nbsp;</div>");
	    			if (emailTemplateVo.getHeader() != null && !"".equals(emailTemplateVo.getHeader())) {
						bodyText.append(emailTemplateVo.getHeader());
					}
    				bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
    						+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    						+"Part No."
    						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    						+"Description"
    						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    						+"A/U"
    						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    						+"Qty"
    						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    						+"remark"
    						+"</td></tr>"
    						);
    				boolean ohOrSv = false;
    				for (int k = 0; k < elementList.size(); k++) {
    					String remark = "";
    					if (elementList.get(k).getRemark() != null) {
    						remark = elementList.get(k).getRemark();
    					}
    					bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    									+ elementList.get(k).getPartNumber()
    									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    									+ elementList.get(k).getDescription()
    									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    									+ elementList.get(k).getUnit()
    									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    									+ elementList.get(k).getAmount()
    									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    									+ remark
    									+"</td></tr>"
    									);
    				}
    				bodyText.append("</table></div>");
    				bodyText.append("<div> </div>");
    				if (emailTemplateVo.getBottom() != null && !"".equals(emailTemplateVo.getBottom())) {
						bodyText.append(emailTemplateVo.getBottom());
					}
    				/*bodyText.append("<div>For OH or SV parts, please kindly provide the Airworthiness Release Certification and "
    						+ "shop finding report along with the quotation for our reference. "
    						+ "Pls also advise warranty period in your quotation.</div>");
    				bodyText.append("<div>Thank you and look forward to your reply.</div>");*/
//    			}else {
//    				bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
//    				realPath = "D:/CRM/Files/mis/email/sampleoutput/"+quoteNumber+".xls";
//    			}
    			
    			bodyText.append("<div>&nbsp;</div>");
    			bodyText.append("<div>Best Regards</div>");
    			bodyText.append("<div>").append("Purchasing team").append("</div>");
    			bodyText.append("<div>Betterair Trade Co., Ltd</div>");
    			bodyText.append("<div>Email : ").append(exchangeMail.getUsername()).append("</div>");
    			bodyText.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
    			bodyText.append("<div>Tel.: ").append("+852 30717759  Fax: +852 30763097").append("</div>");
    			List<String> cc = new ArrayList<String>();
				List<String> bcc = new ArrayList<String>();
				List<String> to = new ArrayList<String>();
				bcc.add("purchaser@betterairgroup.com");
				to.add(emails.get(0).getEmail());
				exchangeMail.init();
    			exchangeMail.doSend("RFQ partsbase or stockmarket", to, cc, bcc, bodyText.toString(), "");
    			for (int i = 0; i < record.size(); i++) {
    				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    				Timestamp timestamp = Timestamp.valueOf(df.format(new Date()));
    				record.get(i).setSendTime(timestamp);
					partAndEmailDao.updateByPrimaryKeySelective(record.get(i));
				}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    public void sendByCrawEmailNoSupplier(){
    	try {
    		List<String> emails = partAndEmailDao.getEmailList();
    		PageModel<String> page = new PageModel<String>();
        	for (String email : emails) {
        		page.put("email", email);
    			List<PartAndEmail> record = partAndEmailDao.getBySupplierIdOrEmail(page);
    			List<PartAndEmail> elementList = partAndEmailDao.getElement(page);
    			for (int i = 0; i < record.size(); i++) {
    				record.get(i).setEmailStatus(1);
					partAndEmailDao.updateByPrimaryKeySelective(record.get(i));
				}
    			EmailTemplateVo[] templateVos = clientInquiryService.getEmailTemplates();
				int index = (int) (Math.random() * templateVos.length);
				EmailTemplateVo emailTemplateVo = templateVos[index];
    			ExchangeMail exchangeMail = new ExchangeMail();
    			exchangeMail.setUsername("purchaser@betterairgroup.com");
    			exchangeMail.setPassword("GZBAcom@)!&0817");
    			StringBuffer bodyText = new StringBuffer();
    			String[] calls = {"Dear sales","Dear sales team","Dear sales team","Dear sales","Dear sales team","Dear sales"};
    			String call = calls[(int)Math.random()*calls.length];
    			bodyText.append("<div>");
    			bodyText.append(call);
    			bodyText.append("</div>");
    			bodyText.append("");
    			bodyText.append("<div>&nbsp;</div>");
    			if (emailTemplateVo.getHeader() != null && !"".equals(emailTemplateVo.getHeader())) {
					bodyText.append(emailTemplateVo.getHeader());
				}
				bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
						+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+"Part No."
						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+"Description"
						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+"A/U"
						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+"Qty"
						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+"remark"
						+"</td></tr>"
						);
				boolean ohOrSv = false;
				for (int k = 0; k < elementList.size(); k++) {
					String remark = "";
					if (elementList.get(k).getRemark() != null) {
						remark = elementList.get(k).getRemark();
					}
					bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ elementList.get(k).getPartNumber()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ elementList.get(k).getDescription()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ elementList.get(k).getUnit()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ elementList.get(k).getAmount()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ remark
									+"</td></tr>"
									);
				}
				bodyText.append("</table></div>");
				bodyText.append("<div> </div>");
				if (emailTemplateVo.getBottom() != null && !"".equals(emailTemplateVo.getBottom())) {
					bodyText.append(emailTemplateVo.getBottom());
				}
    			bodyText.append("<div>&nbsp;</div>");
    			bodyText.append("<div>Best Regards</div>");
    			bodyText.append("<div>").append("Purchasing team").append("</div>");
    			bodyText.append("<div>Betterair Trade Co., Ltd</div>");
    			bodyText.append("<div>Email : ").append(exchangeMail.getUsername()).append("</div>");
    			bodyText.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
    			bodyText.append("<div>Tel.: ").append("+852 30717759  Fax: +852 30763097").append("</div>");
    			List<String> cc = new ArrayList<String>();
				List<String> bcc = new ArrayList<String>();
				List<String> to = new ArrayList<String>();
				bcc.add("purchaser@betterairgroup.com");
				to.add(email);
				exchangeMail.init();
    			exchangeMail.doSend("RFQ partsbase or stockmarket", to, cc, bcc, bodyText.toString(), "");
    			for (int i = 0; i < record.size(); i++) {
    				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    				Timestamp timestamp = Timestamp.valueOf(df.format(new Date()));
    				record.get(i).setSendTime(timestamp);
					partAndEmailDao.updateByPrimaryKeySelective(record.get(i));
				}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    }
    
    public Double getTotalPrice(Integer id){
    	return clientInquiryElementDao.getTotalPrice(id);
    }
    
    public int getCountByTime(String time){
    	return partAndEmailDao.getCountByTime(time);
    }
    
    public List<ClientInquiryElement> getDateBySelect(String ids){
    	return clientInquiryElementDao.getDateBySelect(ids);
    }
    
    public boolean QQEmail(List<ClientInquiryElement> list,ClientInquiry clientInquiry,UserVo user){

		//创建发邮箱记录
		EmailRecord emailRecord = new EmailRecord();
		//存放接受者
		List<String> reciverList = new ArrayList<String>();

		//发送者邮箱
		String sender = "";

    	try {
    		QQMail qqMail = new QQMail();
    		if (user.getLoginName().toLowerCase().equals("amber")) {
    			qqMail.setUserName("1463884086@qq.com");
        		qqMail.setPassWord("nsctqmtpkenugebc");
        		sender = "1463884086@qq.com";
			}else if (user.getLoginName().toLowerCase().equals("nora")) {
				qqMail.setUserName("2519812884@qq.com");
	    		qqMail.setPassWord("hlslhftuyphedjdc");
				sender = "2519812884@qq.com";
			}else if (user.getLoginName().toLowerCase().equals("crystal")) {
				qqMail.setUserName("3537552702@qq.com");
	    		qqMail.setPassWord("hvnrmypnneqwdbbf");
				sender = "3537552702@qq.com";
			}else if (user.getLoginName().toLowerCase().equals("hope")) {
				qqMail.setUserName("480978761@qq.com");
	    		qqMail.setPassWord("gkjmfjyglfzebjec");
				sender = "480978761@qq.com";
			}else if (user.getLoginName().toLowerCase().equals("angela")) {
				qqMail.setUserName("3346278259@qq.com");
	    		qqMail.setPassWord("delkljvfariqdbdh");
				sender = "3346278259@qq.com";
			}else if (user.getLoginName().toLowerCase().equals("kent")) {
				qqMail.setUserName("2318357277@qq.com");
	    		qqMail.setPassWord("clhffllvgtwoebbg");
				sender = "2318357277@qq.com";
			}else if(user.getLoginName().toLowerCase().equals("t1")){
//				qqMail.setUserName("white@naswork.com");
//				qqMail.setPassWord("eN3sAAaLPT2MikWE");
				qqMail.setUserName("942364283@qq.com");
				qqMail.setPassWord("xzjtivftdzrrbceh");
				sender = "942364283@qq.com";
			}
    		qqMail.setTitle(clientInquiry.getQuoteNumber());
    		StringBuffer bodyText = new StringBuffer();
    		List<String> tos = new ArrayList<String>();
//    		tos.add("tanoy@naswork.com");
//    		tos.add("liana@betterairgroup.com");
    		List<Integer> userList = authorityRelationDao.getUserIdByClient(clientInquiry.getClientId());
    		for (Integer integer : userList) {
				UserVo userVo = userDao.findById(integer);
				if (userVo.getLoginName().toLowerCase().equals("amber")) {
					tos.add("1463884086@qq.com");//amber
//					reciverList.add("1463884086@qq.com");
				}else if (userVo.getLoginName().toLowerCase().equals("hope")) {
					tos.add("480978761@qq.com");//hope
//					reciverList.add("480978761@qq.com");
				}else if (userVo.getLoginName().toLowerCase().equals("nora")) {
					tos.add("2519812884@qq.com");//nora
//					reciverList.add("2519812884@qq.com");
				}else if (userVo.getLoginName().toLowerCase().equals("crystal")){
					tos.add("3537552702@qq.com");//crystal
//					reciverList.add("");
				}else if (userVo.getLoginName().toLowerCase().equals("kent")){
					tos.add("2318357277@qq.com");//kent
					reciverList.add("");
				}
				else if (userVo.getLoginName().toLowerCase().equals("t1")){
					tos.add("942364283@qq.com");
					reciverList.add("");
//					tos.add("white@naswork.com");
				}
			}
    		if (!user.getLoginName().toLowerCase().equals("kent")) {
    			tos.add("3346278259@qq.com");//angela 
			}else {
				tos.add("1391479993@qq.com");//bob
			}
    		//tos.add("3346278259@qq.com");//angela 
    		tos.add("3131660631@qq.com");//kris
    		tos.add("2026458660@qq.com");//limmy
    		tos.add("3508643956@qq.com");//renee
    		tos.add("3335454113@qq.com");//cassie
    		tos.add("2326155059@qq.com ");//Saidy
    		tos.add("2764805164@qq.com");//donna

			tos.add("white@naswork.com");//white
			tos.add("942364283@qq.com");//white
    		qqMail.setToList(tos);
    		bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
					+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+"Part No."
					+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+"Description"
					+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+"A/U"
					+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+"Qty"
					+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+"remark"
					+"</td></tr>"
					);
			boolean ohOrSv = false;
			for (int k = 0; k < list.size(); k++) {
				String remark = "";
				if (list.get(k).getRemark() != null) {
					remark = list.get(k).getRemark();
				}
				bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ list.get(k).getPartNumber()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ list.get(k).getDescription()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ list.get(k).getUnit()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ list.get(k).getAmount()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ remark
								+"</td></tr>"
								);
			}
			bodyText.append("</table></div>");
			qqMail.setBodyText(bodyText.toString());
			boolean isSend = qqMail.sendEmail();
			if(isSend){
				emailRecord.setClientInquiryId(clientInquiry.getId());
				emailRecord.setSender(sender);
				emailRecord.setReceiver(StringUtils.join(tos.toArray(),";"));
				emailRecord.setType("销售新建询价明细");
				emailRecordDao.insertSelective(emailRecord);
			}
			return isSend;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
