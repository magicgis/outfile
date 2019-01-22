package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.StorckMarketDao;
import com.naswork.dao.SupplierContactDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.StorckMarket;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.GyExcelService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("supplierQuoteService")
public class SupplierQuoteServiceImpl implements SupplierQuoteService {
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource 
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private StorckMarketDao storckMarketDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private SupplierContactDao supplierContactDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	

	@Override
	public void findSupplierQuotePage(PageModel<SupplierQuoteVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<SupplierQuoteVo> supplierquotelist=supplierQuoteDao.findSupplierQuotePage(page);
		page.setEntities(supplierquotelist);
		
	}
	
	@Override
	public void findSupplierQuoteElementPage(PageModel<SupplierQuoteVo> page, String searchString, GridSort sort) {
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<SupplierQuoteVo> supplierquoteelementlist=supplierQuoteDao.findSupplierQuoteElementPage(page);
		page.setEntities(supplierquoteelementlist);
	}
	
	@Override
	public void findAirType(PageModel<SystemCode> page, String searchString, GridSort sort) {
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<SystemCode> listDateVos=supplierQuoteDao.findAirType(page);
		page.setEntities(listDateVos);
	}
	
	@Override
	public void findClientInquirypage(PageModel<ClientInquiry> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ClientInquiry> listDateVos=supplierQuoteDao.findClientInquirypage(page);
		page.setEntities(listDateVos);
	}
	
	@Override
	public void findWeatherQuote(PageModel<ClientInquiry> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ClientInquiry> listDateVos=supplierQuoteDao.findWeatherQuote(page);
		page.setEntities(listDateVos);
	}

	@Override
	public List<ClientInquiry> findClientInquiryPage(PageModel<ClientInquiry> page, String searchString, GridSort sort) {
		
		String buffer = null;
		if(null!=page.getString("partNumber")&&!"".equals(page.getString("partNumber"))){
			if("eq".equals(page.getString("check"))){
				String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
				buffer =" e.part_number_code = "+"'"+partNumberCode+"'";
			}else{
				String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
				buffer =" e.part_number_code LIKE "+"'%"+partNumberCode+"%'";
			}
				 searchString=buffer;
		}
		
		
		page.put("partNumber", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ClientInquiry> listDateVos=supplierQuoteDao.findClientInquiryPage(page);
		page.setEntities(listDateVos);
		
		return listDateVos;
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
	
	public List<ListDateVo> findsid(AuthorityRelation authorityRelation) {
		return supplierQuoteDao.findsid(authorityRelation);
	}

	public List<ListDateVo> findcid() {
		return supplierQuoteDao.findcid();
	}

	@Override
	public List<ListDateVo> findbizid() {
		return supplierQuoteDao.findbizid();
	}

	@Override
	public List<ListDateVo> findairid() {
		return supplierQuoteDao.findairid();
	}

	@Override
	public List<ListDateVo> findcond() {
		return supplierQuoteDao.findcond();
	}

	@Override
	public List<ListDateVo> findcert() {
		return supplierQuoteDao.findcert();
	}

	@Override
	public List<ListDateVo> findsqstauts() {
		return supplierQuoteDao.findsqstauts();
	}

	@Override
	public void updateByPrimaryKey(SupplierQuote vo) {
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(vo.getCurrencyId());
		vo.setExchangeRate(exchangeRate.getRate());
		SupplierQuote supplierQuote = supplierQuoteDao.selectByPrimaryKey(vo.getId());
		supplierQuoteDao.updateByPrimaryKey(vo);
		if ((supplierQuote.getValidity() == null && vo.getValidity() != null) || 
				(supplierQuote.getValidity() != null && vo.getValidity() != null && !supplierQuote.getValidity().equals(vo.getValidity()))) {
			List<SupplierQuoteElement> list = supplierQuoteElementDao.getBySupplierQuoteId(vo.getId());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setValidity(vo.getValidity());
				supplierQuoteElementDao.updateByPrimaryKeySelective(list.get(i));
			}
		}
	}

	
	public int insertSelective(SupplierQuote vo){
		int count = supplierQuoteDao.findBySupplierInquiryId(vo.getSupplierInquiryId());
		String quoteNumber = supplierInquiryDao.selectByPrimaryKey(vo.getSupplierInquiryId()).getQuoteNumber();
		if (count==0) {
			vo.setQuoteNumber(quoteNumber);
		}else {
			vo.setQuoteNumber(quoteNumber+"-"+(count+1));
		}
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(vo.getCurrencyId());
		vo.setExchangeRate(exchangeRate.getRate());
		Integer id = supplierInquiryDao.findClientInquiryId(vo.getSupplierInquiryId());
		ClientInquiry clientInquiry = clientInquiryService.findById(id);
		if (clientInquiry.getInquiryStatusId().equals(31) || clientInquiry.getInquiryStatusId() == 31) {
			clientInquiry.setId(id);
			clientInquiry.setInquiryStatusId(35);
			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
		}
		return supplierQuoteDao.insertSelective(vo);
	}

    public SupplierQuote selectByPrimaryKey(Integer id){
    	return supplierQuoteDao.selectByPrimaryKey(id);
    }

	@Override
	public List<SupplierQuoteVo> findSupplierQuoteElement(Integer id) {
		return supplierQuoteDao.findSupplierQuoteElement(id);
	}
	
	
	public List<Integer> getCurrencyId(Integer id){
		return supplierQuoteDao.getCurrencyId(id);
	}

	public Integer findBySupplierInquiryId(Integer supplierInquiryId){
		return supplierQuoteDao.findBySupplierInquiryId(supplierInquiryId);
	}
	
	@Override
	public SupplierQuote lookSupplierInquiry(Integer supplierInquiryId,Integer currencyId,Double exchangeRate) {
		SupplierQuote vo=supplierQuoteDao.findSupplierInquiry(supplierInquiryId);
		if(null==vo){
			vo=new SupplierQuote();
			vo.setCurrencyId(currencyId);
			vo.setExchangeRate(exchangeRate);
			vo.setSupplierInquiryId(supplierInquiryId);
			vo.setQuoteDate(Calendar.getInstance().getTime());
			supplierQuoteDao.insertSelective(vo);
		}
		return vo;
	}

	@Override
	public List<ClientInquiry> findClientInquiry(ListDateVo vo) {
		return supplierQuoteDao.findClientInquiry(vo);
	}

	@Override
	public List<ClientInquiry> findSupplierQuote(Integer id, Integer item) {
		return supplierQuoteDao.findSupplierQuote(id, item);
	}

	@Override
	public List<SupplierInquiryStatistic> supplierQuoteStat(SupplierInquiryStatistic supplierInquiryStatistic) {
		return supplierQuoteDao.supplierQuoteStat( supplierInquiryStatistic);
	}

	public void stockMarketEmail(){
		List<StorckMarket> list = storckMarketDao.getEmailList();
		List<Integer> supplierList = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setEmailStatus(1);
			storckMarketDao.updateByPrimaryKeySelective(list.get(i));
		}
		for (Integer id : supplierList) {
			List<Integer> elementIds = storckMarketDao.getRecord(id);
			Integer clientInquiryId = storckMarketDao.getClientInquiryId(id);
			sendEmail(elementIds,clientInquiryId,id);
		}
	}

	public void sendEmail(List<Integer> elementIds,Integer clientInquiryId,Integer supplierId){
		SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(clientInquiryId);
		String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierId);
		supplierInquiry.setQuoteNumber(quoteNumber);
		supplierInquiry.setUpdateTimestamp(new Date());
		supplierInquiry.setSupplierId(supplierId);
		supplierInquiryService.insertSelective(supplierInquiry);
		List<ClientInquiryElement> elementList = new ArrayList<ClientInquiryElement>();
		
		for (int j = 0; j < elementIds.size(); j++) {
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(elementIds.get(j));
			int count = supplierQuoteElementDao.getByPartInTwoWeeks(elementList.get(j).getPartNumber(), supplierId.toString());
			if (!(count > 0)) {
				elementList.add(clientInquiryElement);
			}
		}
		
		for (int j = 0; j < elementList.size(); j++) {
			SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
			supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
			supplierInquiryElement.setClientInquiryElementId(elementList.get(j).getId());
			supplierInquiryElement.setUpdateTimestamp(new Date());
			//ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(elementList.get(j));
			supplierInquiryElementService.insertSelective(supplierInquiryElement);
			if (!"".equals(elementList.get(j).getElementStatusId()) && elementList.get(j).getElementStatusId()!=null) {
				if (elementList.get(j).getElementStatusId().equals(700) || elementList.get(j).getElementStatusId()==700) {
					elementList.get(j).setElementStatusId(701);
					clientInquiryElementDao.updateByPrimaryKeySelective(elementList.get(j));
				}
			}
		}
		
		if (elementList.size()>20) {
			StringBuffer businessKey = new StringBuffer();
			businessKey.append("supplier_air_relation.id.").append(supplierInquiry.getId()).append(".supplierAirRelationExcel");
			try {
				gyExcelService.generateExcel(businessKey.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
		
		ExchangeMail exchangeMail = new ExchangeMail();
		exchangeMail.setUsername("purchaser@betterairgroup.com");
		exchangeMail.setPassword("GZBAcom@)!&0817");
		List<SupplierContact> supplierContacts = supplierContactDao.getEmailPerson(supplierId);
		if (supplierContacts.size() == 0) {
			supplierContacts = supplierContactDao.getEmails(supplierId);
		}
		List<String> to = new ArrayList<String>();
		List<String> cc = new ArrayList<String>();
		List<String> bcc = new ArrayList<String>();
		to.add(supplierContacts.get(0).getEmail());
		bcc.add(exchangeMail.getUsername());
		StringBuffer bodyText = new StringBuffer();
		String name = supplierContacts.get(0).getName();
		bodyText.append("<div>Dear ");
		bodyText.append(name);
		bodyText.append("</div>");
		bodyText.append("<div>&nbsp;</div>");
		String realPath = "";
		if (elementList.size()<=20) {
			//表头
			bodyText.append("<div>Would you please kindly check if you could quote below part number to us? </div>");
			bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
					+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+ "SN"
					+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
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
				//ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(elementIds.get(k));
				bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ elementList.get(k).getItem()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ elementList.get(k).getPartNumber()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ elementList.get(k).getDescription()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ elementList.get(k).getUnit()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ elementList.get(k).getAmount()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ elementList.get(k).getRemark()
								+"</td></tr>"
								);
			}
			bodyText.append("</table></div>");
			bodyText.append("<div> </div>");
			bodyText.append("<div>For OH or SV parts, please kindly provide the Airworthiness Release Certification and "
						+ "shop finding report along with the quotation for our reference. "
						+ "Pls also advise warranty period in your quotation.</div>");
			bodyText.append("<div>Thank you and look forward to your reply.</div>");
		}else {
			bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
			realPath = "D:/CRM/Files/mis/email/sampleoutput/"+quoteNumber+".xls";
		}
		
		bodyText.append("<div>&nbsp;</div>");
		bodyText.append("<div>Best Regards</div>");
		bodyText.append("<div>").append("Purchasing team").append("</div>");
		//bodyText.append("<div>Purchasing Specialist</div>");
		bodyText.append("<div>Betterair Trade Co., Ltd</div>");
		bodyText.append("<div>Email : ").append(exchangeMail.getUsername()).append("</div>");
		bodyText.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
		bodyText.append("<div>Tel.: ").append("+852 30717759  Fax: +852 30763097").append("</div>");
		exchangeMail.init();
		try {
			exchangeMail.doSend("RFQ"+quoteNumber, to, cc, bcc, bodyText.toString(), realPath);
			supplierInquiry.setEmailStatus(1);
			supplierInquiryDao.updateByPrimaryKeySelective(supplierInquiry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateByPrimaryKeySelective(SupplierQuote record){
		supplierQuoteDao.updateByPrimaryKeySelective(record);
		if (record.getQuoteStatusId().equals(91)) {
			List<SupplierQuoteElement> list = supplierQuoteElementDao.getBySupplierQuoteId(record.getId());
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setSupplierQuoteStatusId(72);
				supplierQuoteElementDao.updateByPrimaryKeySelective(list.get(i));
			}
		}
	}


}
