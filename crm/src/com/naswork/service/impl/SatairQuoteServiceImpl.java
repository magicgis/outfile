package com.naswork.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.HighPriceCrawlQuoteDao;
import com.naswork.dao.LocationFeeForExchangeBillMappingDao;
import com.naswork.dao.SatairQuoteDao;
import com.naswork.dao.SatairQuoteElementDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.UserDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.Element;
import com.naswork.model.ExchangeRate;
import com.naswork.model.HighPriceCrawlQuote;
import com.naswork.model.LocationFeeForExchangeBillMapping;
import com.naswork.model.SatairQuote;
import com.naswork.model.SatairQuoteElement;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.SatairQuoteService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.TPartService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("satairQuoteService")
public class SatairQuoteServiceImpl implements SatairQuoteService {

	@Resource
	private SatairQuoteDao satairQuoteDao;
	@Resource
	private SatairQuoteElementDao satairQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private TPartService tPartService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private UserDao userDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ElementDao elementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ExchangeRateService exchangeRateService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private HighPriceCrawlQuoteDao highPriceCrawlQuoteDao;
	@Resource
	private LocationFeeForExchangeBillMappingDao locationFeeForExchangeBillMappingDao;
	
	
	@Override
	public int insertSelective(SatairQuote record) {
		return satairQuoteDao.insertSelective(record);
	}

	@Override
	public SatairQuote selectByPrimaryKey(Integer id) {
		return satairQuoteDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SatairQuote record) {
		return satairQuoteDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SatairQuote> getNewCrawMessage() {
		return satairQuoteDao.getNewCrawMessage();
	}
	/**
	@Author: Modify by white
	@DateTime: 2018/9/18 15:29
	@Description: 修改银行手续费为35
	*/
	public void createQuote(){
		List<SatairQuote> crawList = satairQuoteDao.getNewCrawMessage();
		//锁定记录
		for (int i = 0; i < crawList.size(); i++) {
			satairQuoteDao.lockTheMessage(crawList.get(i));
		}
		for (int i = 0; i < crawList.size(); i++) {
			try {
				List<SatairQuoteElement> list = satairQuoteElementDao.getByClientInquiryId(crawList.get(i).getClientInquiryId(),crawList.get(i).getId());
				if (list.size() > 0) {
					Supplier supplier = supplierDao.findByCode("1077");
					ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
					SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(crawList.get(i).getClientInquiryId());
					String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplier.getId());
					supplierInquiry.setQuoteNumber(quoteNumber);
					supplierInquiry.setUpdateTimestamp(new Date());
					supplierInquiry.setSupplierId(supplier.getId());
					supplierInquiryService.insertSelective(supplierInquiry);
					
					SupplierQuote supplierQuote = new SupplierQuote();
					supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
					supplierQuote.setCurrencyId(supplier.getCurrencyId());
					supplierQuote.setExchangeRate(exchangeRate.getRate());
					supplierQuote.setQuoteDate(new Date());
					supplierQuote.setUpdateTimestamp(new Date());
					supplierQuote.setQuoteNumber(supplierInquiry.getQuoteNumber());
					supplierQuote.setBankCost(new Double(35));
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					if (year == 2017) {
						 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
						 Date date = sdf.parse("2017-12-31");
						 supplierQuote.setValidity(date);
					}else {
						GregorianCalendar gc = new GregorianCalendar();
						Date today = new Date();
						gc.setTime(today);
						gc.add(5,30);
						supplierQuote.setValidity(gc.getTime());
					}
					supplierQuoteDao.insertSelective(supplierQuote);
					
					ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(crawList.get(i).getClientInquiryId());
					if (clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId().equals(31)) {
						clientInquiry.setInquiryStatusId(35);
						clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
					}
					
					List<ClientInquiryElement> inquiryList = clientInquiryElementDao.findByclientInquiryId(crawList.get(i).getClientInquiryId());
					for (int k = 0; k < inquiryList.size(); k++) {
						if (inquiryList.get(k).getPartNumber() != "" && !"".equals(inquiryList.get(k).getPartNumber())) {
							List<SatairQuoteElement> satairList = satairQuoteElementDao.selecyByNewSatairQuoteId(crawList.get(i).getId().toString(),inquiryList.get(k).getPartNumber(),inquiryList.get(k).getPartNumber()+"%");
							for (SatairQuoteElement satairQuoteElement : satairList) {
								TPart tPart = new TPart();
								//tPart.setShortPartNum(clientInquiryElementService.getShortPartNumber(satairQuoteElement.getPartNumber()));
								tPart.setShortPartNum(clientInquiryService.getCodeFromPartNumber(satairQuoteElement.getPartNumber()));
								tPart.setCageCode(satairQuoteElement.getCageCode());
								List<TPart> tParts = tPartDao.getTPartByShort(tPart);
								if (tParts.size() > 0) {
									for (int j = 0; j < tParts.size(); j++){
										String[] des = tParts.get(j).getPartName().split(",");
										boolean exist = false;
										for (int b = 0; b < des.length; b++) {
											if (des[b].equals(satairQuoteElement.getDescription())) {
												exist = true;
												break;
											}
										}
										if (!exist) {
											tParts.get(j).setPartName(tParts.get(j).getPartName()+","+satairQuoteElement.getDescription());
											tPartDao.updateByPrimaryKeySelective(tParts.get(j));
										}
									}
								} else {
									TPart tPart2 = new TPart();
									tPart2.setMsn("0-"+satairQuoteElement.getCageCode());
									tPart2.setPartNum(satairQuoteElement.getPartNumber());
									tPart2.setPartName(satairQuoteElement.getDescription());
									tPartService.insertSelective(tPart2);
								}
								
							}
							SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
							supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
							if (satairList.size() > 0) {
								if (!satairList.get(0).getPartNumber().trim().equals(inquiryList.get(k).getPartNumber().trim())) {
									ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
									clientInquiryElement.setPartNumber(satairList.get(0).getPartNumber().trim());
									clientInquiryElement.setAmount(inquiryList.get(k).getAmount());
									clientInquiryElement.setClientInquiryId(inquiryList.get(k).getClientInquiryId());
									clientInquiryElement.setDescription(satairList.get(0).getDescription());
						            List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim()));
						            Element element2 = new Element();
						            if (element.size()==0) {
						            	byte[] p = clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim()).getBytes();
						            	Byte[] pBytes = new Byte[p.length];
						            	for(int j=0;j<p.length;j++){
						            		pBytes[j] = Byte.valueOf(p[j]);
						            	}
						            	element2.setPartNumberCode(pBytes);
						            	element2.setUpdateTimestamp(new Date());
										elementDao.insert(element2);
										clientInquiryElement.setElementId(element2.getId());
									}else {
										Element element3 = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim())).get(0);
										clientInquiryElement.setElementId(element3.getId());
									}
						            if (satairList.get(0).getUnit() != null && !"".equals(satairList.get(0).getUnit())) {
						            	clientInquiryElement.setUnit(satairList.get(0).getUnit().trim());
									}else {
										clientInquiryElement.setUnit(inquiryList.get(k).getUnit());
									}
						            clientInquiryElement.setItem(clientInquiryElementDao.getMaxItem(crawList.get(i).getClientInquiryId())+1);
						            clientInquiryElement.setCsn(inquiryList.get(k).getCsn());
						            clientInquiryElement.setMainId(inquiryList.get(k).getId());
						            clientInquiryElement.setIsMain(1);
						            clientInquiryElement.setElementStatusId(700);
						            clientInquiryElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim()));
						            clientInquiryElement.setSource("爬虫");
						            clientInquiryElementDao.insertSelective(clientInquiryElement);
						            supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
								}else {
									supplierInquiryElement.setClientInquiryElementId(inquiryList.get(k).getId());
								}
							}else {
								supplierInquiryElement.setClientInquiryElementId(inquiryList.get(k).getId());
							}
							supplierInquiryElementDao.insertSelective(supplierInquiryElement);
							if (satairList.size() == 1) {
								SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
								supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
								if (!satairList.get(0).getPartNumber().trim().equals(inquiryList.get(k).getPartNumber().trim())) {
									supplierQuoteElement.setPartNumber(satairList.get(0).getPartNumber());
								}else {
									supplierQuoteElement.setPartNumber(satairList.get(0).getPartNumber());
								}
								supplierQuoteElement.setAmount(inquiryList.get(k).getAmount());
								if (satairList.get(0).getAmount() >1) {
									supplierQuoteElement.setMoq(satairList.get(0).getAmount().intValue());
								}
								Integer cerId = satairQuoteElementDao.getCerIdByCode(satairList.get(0).getCertification().trim());
								if (cerId == null) {
									cerId = satairQuoteElementDao.getCerIdByCode("Other");
									
								}
								if (satairList.get(0).getCageCode() != null) {
									supplierQuoteElement.setRemark(satairList.get(0).getCageCode());
								}
								supplierQuoteElement.setCertificationId(cerId);
								List<SystemCode> FN = systemCodeDao.selectByAllCode("FN");
								List<SystemCode> NE = systemCodeDao.selectByAllCode("NE");
								if (satairList.get(0).getLeadTime().trim().equals("In stock") || satairList.get(0).getLeadTime().trim().equals("0")) {
									supplierQuoteElement.setConditionId(NE.get(0).getId());
									supplierQuoteElement.setLeadTime("3");
									//supplierQuoteElement.setLeadTime("1");
								}else {
									supplierQuoteElement.setConditionId(FN.get(0).getId());
									if (satairList.get(0).getLeadTime() != null && !"".equals(satairList.get(0).getLeadTime())) {
										Double leadTime = new Double(satairList.get(0).getLeadTime().toString());
										BigDecimal lead = new BigDecimal(leadTime);
										leadTime = leadTime + lead.divide(new BigDecimal(7),0, RoundingMode.HALF_UP).doubleValue() * new Double(2);
										supplierQuoteElement.setLeadTime(leadTime.toString());
									}
									
								}
								supplierQuoteElement.setDescription(satairList.get(0).getDescription());
								if (satairList.get(0).getUnit() != null && !"".equals(satairList.get(0).getUnit())) {
									supplierQuoteElement.setUnit(satairList.get(0).getUnit());
								}else {
									supplierQuoteElement.setUnit("EA");
								}
								if (satairList.get(0).getUnitPrice() != null && !"".equals(satairList.get(0).getUnitPrice())) {
									satairList.get(0).setUnitPrice(satairList.get(0).getUnitPrice().replaceAll(",",""));
								}
								if (satairList.get(0).getStorageAmount() != null) {
									supplierQuoteElement.setAvailableQty(new Double(satairList.get(0).getStorageAmount()));
								}
								if (new Double(satairList.get(0).getUnitPrice()) > 0) {
									supplierQuoteElement.setPrice(new Double(satairList.get(0).getUnitPrice()));
								}else {
									continue;
								}
								supplierQuoteElement.setSupplierQuoteStatusId(70);
								supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
								supplierQuoteElement.setElementId(inquiryList.get(k).getElementId());
								supplierQuoteElement.setFeeForExchangeBill(60.0);
								//获取危险品列表
								ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
								Integer count = clientInquiryElementDao.getDangerList(clientInquiryElement);
								String[] loca = satairList.get(0).getPlant().split(" ");
								supplierQuoteElement.setLocation(loca[loca.length-1]);
								//不同地区接收不同的提后换单费
								List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
								if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
									if (feeList.size() > 0 && feeList.get(0) != null) {
										boolean in = false;
										for (int l = 0; l < feeList.size(); l++) {
											if (feeList.get(l).getFeeExchangeBill() != null) {
												supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(l).getFeeExchangeBill()));
												in = true;
												break;
											}
										}
										if (!in) {
											supplierQuoteElement.setFeeForExchangeBill(new Double(160));
											LocationFeeForExchangeBillMapping locationFeeForExchangeBillMapping = new LocationFeeForExchangeBillMapping();
											locationFeeForExchangeBillMapping.setLocation(supplierQuoteElement.getLocation().trim());
											locationFeeForExchangeBillMappingDao.insertSelective(locationFeeForExchangeBillMapping);
										}
									}else {
										supplierQuoteElement.setFeeForExchangeBill(new Double(160));
									}
								}
								
								if (satairList.get(0).getIfDanger().equals("1") || count > 0) {
									supplierQuoteElement.setHazmatFee(new Double(350));
									supplierQuoteElement.setFeeForExchangeBill(180.0);
								}
								supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
								if (year == 2017) {
									 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
									 Date date = sdf.parse("2017-12-31");
									 supplierQuoteElement.setValidity(date);
								}else {
									GregorianCalendar gc = new GregorianCalendar();
									Date today = new Date();
									gc.setTime(today);
									gc.add(5,30);
									supplierQuoteElement.setValidity(gc.getTime());
								}
								supplierQuoteElement.setBankCost(new Double(35));
								supplierQuoteElementDao.insertSelective(supplierQuoteElement);
								double price = 0.00;
								ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
									price = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
								}else {
									price = supplierQuoteElement.getPrice();
								}
								List<HighPriceCrawlQuote> lists = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
								if (price > new Double(5000) && lists.size() == 0) {
									HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
									highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
								}
								clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
								ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
								if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
									if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
										if (clientQuoteElement != null) {
											clientInquiryElement.setElementStatusId(713);
										}
									}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701|| clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700){
										clientInquiryElement.setElementStatusId(702);
									}
									clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
									if (clientInquiryElement.getMainId() !=null && !"".equals(clientInquiryElement.getMainId())) {
										ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElement.getMainId());
										mainElement.setElementStatusId(702);
										clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
									}else {
										List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(clientInquiryElement.getId());
										for (int j = 0; j < alterList.size(); j++) {
											alterList.get(j).setElementStatusId(702);
											clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
										}
									}
								}
							}else if (satairList.size() > 1) {
								//String[] loca = satairList.get(0).getPlant().split(" ");
								String locationString = "";
								StringBuffer remark = new StringBuffer();
								double amount = 0;
								for (int j = 0; j < satairList.size(); j++) {
									SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
									supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
									if (satairList.get(j).getEnterPartnumber() != null && !"".equals(satairList.get(j).getEnterPartnumber())) {
										supplierQuoteElement.setPartNumber(satairList.get(j).getEnterPartnumber());
										supplierQuoteElement.setAlterPartNumber(satairList.get(j).getPartNumber());
									}else {
										supplierQuoteElement.setPartNumber(satairList.get(j).getPartNumber());
									}
									if (satairList.get(j).getCageCode() != null) {
										supplierQuoteElement.setRemark(satairList.get(j).getCageCode());
									}
									supplierQuoteElement.setAmount(inquiryList.get(k).getAmount());
									if (satairList.get(j).getAmount() >1) {
										supplierQuoteElement.setMoq(satairList.get(j).getAmount().intValue());
									}
									Integer cerId = satairQuoteElementDao.getCerIdByCode(satairList.get(j).getCertification().trim());
									if (cerId != null) {
										supplierQuoteElement.setCertificationId(cerId);
									}else {
										supplierQuoteElement.setCertificationId(54);
									}
									List<SystemCode> FN = systemCodeDao.selectByAllCode("FN");
									List<SystemCode> NE = systemCodeDao.selectByAllCode("NE");
									if (satairList.get(j).getLeadTime().trim().equals("In stock") || satairList.get(j).getLeadTime().trim().equals("0")) {
										supplierQuoteElement.setConditionId(NE.get(0).getId());
										supplierQuoteElement.setLeadTime("3");
									}else {
										supplierQuoteElement.setConditionId(FN.get(0).getId());
										if (satairList.get(j).getLeadTime() != null && !"".equals(satairList.get(j).getLeadTime())) {
											Double leadTime = new Double(satairList.get(j).getLeadTime().toString());
											BigDecimal lead = new BigDecimal(leadTime);
											leadTime = leadTime + lead.divide(new BigDecimal(7),0, RoundingMode.HALF_UP).doubleValue() * new Double(2);
											supplierQuoteElement.setLeadTime(leadTime.toString());
										}
										//supplierQuoteElement.setLeadTime(satairList.get(j).getLeadTime().toString());
									}
									supplierQuoteElement.setDescription(satairList.get(j).getDescription());
									supplierQuoteElement.setUnit(satairList.get(j).getUnit());
									if (satairList.get(j).getUnitPrice() != null && !"".equals(satairList.get(j).getUnitPrice())) {
										satairList.get(j).setUnitPrice(satairList.get(j).getUnitPrice().replaceAll(",",""));
									}
									supplierQuoteElement.setPrice(new Double(satairList.get(j).getUnitPrice()));
									supplierQuoteElement.setSupplierQuoteStatusId(70);
									if (supplierQuoteElement.getRemark() != null) {
										supplierQuoteElement.setRemark(supplierQuoteElement.getRemark() + remark.toString());
									}else {
										supplierQuoteElement.setRemark(remark.toString());
									}
  									String[] loca = satairList.get(j).getPlant().split(" ");
									locationString = loca[loca.length-1];
									if (satairList.get(j).getStorageAmount() != null && !"".equals(satairList.get(j).getStorageAmount())) {
										supplierQuoteElement.setAvailableQty(new Double(satairList.get(j).getStorageAmount()));
									}
									supplierQuoteElement.setLocation(locationString);
									List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
									if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
										if (feeList.size() > 0 && feeList.get(0) != null) {
											boolean in = false;
											for (int l = 0; l < feeList.size(); l++) {
												if (feeList.get(l).getFeeExchangeBill() != null) {
													supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(l).getFeeExchangeBill()));
													in = true;
													break;
												}
											}
											if (!in) {
												supplierQuoteElement.setFeeForExchangeBill(new Double(160));
												LocationFeeForExchangeBillMapping locationFeeForExchangeBillMapping = new LocationFeeForExchangeBillMapping();
												locationFeeForExchangeBillMapping.setLocation(supplierQuoteElement.getLocation().trim());
												locationFeeForExchangeBillMappingDao.insertSelective(locationFeeForExchangeBillMapping);
											}
										}else {
											supplierQuoteElement.setFeeForExchangeBill(new Double(160));
										}
									}
									supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
									supplierQuoteElement.setElementId(inquiryList.get(k).getElementId());
									if (satairList.get(j).getIfDanger().equals("1")) {
										//supplierQuoteElement.setRemark(supplierQuoteElement.getRemark()+",DG $350，not include)");
										supplierQuoteElement.setHazmatFee(new Double(350));
										supplierQuoteElement.setFeeForExchangeBill(180.0);
									}
									supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
									if (year == 2017) {
										 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
										 Date date = sdf.parse("2017-12-31");
										 supplierQuoteElement.setValidity(date);
									}else {
										GregorianCalendar gc = new GregorianCalendar();
										Date today = new Date();
										gc.setTime(today);
										gc.add(5,30);
										supplierQuoteElement.setValidity(gc.getTime());
									}
									supplierQuoteElement.setBankCost(new Double(35));
									supplierQuoteElementDao.insertSelective(supplierQuoteElement);
									double price = 0.00;
									ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
									if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
										price = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
									}else {
										price = supplierQuoteElement.getPrice();
									}
									List<HighPriceCrawlQuote> lists = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									if (price > new Double(5000) && lists.size() == 0) {
										HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
										highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
										highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
									}
									clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
									//break;
								}
								ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
								ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
								if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
									if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
										if (clientQuoteElement != null) {
											clientInquiryElement.setElementStatusId(713);
										}
									}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701 || clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700){
										clientInquiryElement.setElementStatusId(702);
									}
									clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
									if (clientInquiryElement.getMainId() !=null && !"".equals(clientInquiryElement.getMainId())) {
				            			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElement.getMainId());
				            			if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
				            				mainElement.setElementStatusId(702);
					            			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
										}
				            		}else {
				            			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(clientInquiryElement.getId());
				            			for (int j = 0; j < alterList.size(); j++) {
				            				if (alterList.get(j).getElementStatusId() != null) {
				            					if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
					            					alterList.get(j).setElementStatusId(702);
						            				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
												}
											}
				            			}
				            		}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				crawList.get(i).setSupplierQuoteId(null);
				satairQuoteDao.unLockTheMessage(crawList.get(i));
				e.printStackTrace();
			}
		}
		
	}
	
	public void unSelectEmail(){
		List<SatairQuoteElement> textList = satairQuoteElementDao.getEmailList();
		StringBuffer bodyText = new StringBuffer();
		List<UserVo> userList = userDao.getEmailBySupplierCode("1077");
		if (textList.size()>0) {
			bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
					+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+ "询价单号"
					+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
					+"Part No."
					+"</td></tr>"
					);
			for (int i = 0; i < textList.size(); i++) {
				bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+ textList.get(i).getQuoteNumber()
						+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
						+ textList.get(i).getPartNumber()
						+"</td></tr>"
						);
			}
			bodyText.append("</table></div>");
			List<String> emails = new ArrayList<String>();
			for (int i = 0; i < userList.size(); i++) {
				emails.add(userList.get(i).getEmail());
			}
			emails.add("limmy@betterairgroup.com");
			ExchangeMail exchangeMail = new ExchangeMail();
			exchangeMail.setUsername(userList.get(0).getEmail());
			exchangeMail.setPassword(userList.get(0).getEmailPassword());
			exchangeMail.init();
			try {
				exchangeMail.doSend("satair不能选择件号", emails, new ArrayList<String>(), new ArrayList<String>(), bodyText.toString(), "");
				for (int i = 0; i < textList.size(); i++) {
					satairQuoteElementDao.updateEmailStatus(textList.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void createQuoteNew(){
		List<SatairQuote> crawList = satairQuoteDao.getNewCrawMessage();
		//锁定记录
		for (int i = 0; i < crawList.size(); i++) {
			satairQuoteDao.lockTheMessage(crawList.get(i));
		}
		for (int i = 0; i < crawList.size(); i++) {
			try {
				List<SatairQuoteElement> list = satairQuoteElementDao.getByClientInquiryId(crawList.get(i).getClientInquiryId(),crawList.get(i).getId());
				if (list.size() > 0) {
					Supplier supplier = supplierDao.findByCode("1077");
					ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
					SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(crawList.get(i).getClientInquiryId());
					String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplier.getId());
					supplierInquiry.setQuoteNumber(quoteNumber);
					supplierInquiry.setUpdateTimestamp(new Date());
					supplierInquiry.setSupplierId(supplier.getId());
					supplierInquiryService.insertSelective(supplierInquiry);
					
					SupplierQuote supplierQuote = new SupplierQuote();
					supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
					supplierQuote.setCurrencyId(supplier.getCurrencyId());
					supplierQuote.setExchangeRate(exchangeRate.getRate());
					supplierQuote.setQuoteDate(new Date());
					supplierQuote.setUpdateTimestamp(new Date());
					supplierQuote.setQuoteNumber(supplierInquiry.getQuoteNumber());
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					if (year == 2017) {
						 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
						 Date date = sdf.parse("2017-12-31");
						 supplierQuote.setValidity(date);
					}else {
						GregorianCalendar gc = new GregorianCalendar();
						Date today = new Date();
						gc.setTime(today);
						gc.add(5,30);
						supplierQuote.setValidity(gc.getTime());
					}
					supplierQuoteDao.insertSelective(supplierQuote);
					
					ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(crawList.get(i).getClientInquiryId());
					if (clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId().equals(31)) {
						clientInquiry.setInquiryStatusId(35);
						clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
					}
					
					List<ClientInquiryElement> inquiryList = clientInquiryElementDao.findByclientInquiryId(crawList.get(i).getClientInquiryId());
					for (int k = 0; k < inquiryList.size(); k++) {
						if (inquiryList.get(k).getPartNumber() != "" && !"".equals(inquiryList.get(k).getPartNumber())) {
							List<SatairQuoteElement> satairList = satairQuoteElementDao.selecyByNewSatairQuoteId(crawList.get(i).getId().toString(),inquiryList.get(k).getPartNumber(),inquiryList.get(k).getPartNumber()+"%");
							for (SatairQuoteElement satairQuoteElement : satairList) {
								TPart tPart = new TPart();
								//tPart.setShortPartNum(clientInquiryElementService.getShortPartNumber(satairQuoteElement.getPartNumber()));
								tPart.setShortPartNum(clientInquiryService.getCodeFromPartNumber(satairQuoteElement.getPartNumber()));
								tPart.setCageCode(satairQuoteElement.getCageCode());
								List<TPart> tParts = tPartDao.getTPartByShort(tPart);
								if (tParts.size() > 0) {
									for (int j = 0; j < tParts.size(); j++){
										String[] des = tParts.get(j).getPartName().split(",");
										boolean exist = false;
										for (int b = 0; b < des.length; b++) {
											if (des[b].equals(satairQuoteElement.getDescription())) {
												exist = true;
												break;
											}
										}
										if (!exist) {
											tParts.get(j).setPartName(tParts.get(j).getPartName()+","+satairQuoteElement.getDescription());
											tPartDao.updateByPrimaryKeySelective(tParts.get(j));
										}
									}
								} else {
									TPart tPart2 = new TPart();
									tPart2.setMsn("0-"+satairQuoteElement.getCageCode());
									tPart2.setPartNum(satairQuoteElement.getPartNumber());
									tPart2.setPartName(satairQuoteElement.getDescription());
									tPartService.insertSelective(tPart2);
								}
								
							}
							SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
							supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
							if (satairList.size() > 0) {
								if (!satairList.get(0).getPartNumber().trim().equals(inquiryList.get(k).getPartNumber().trim())) {
									ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
									clientInquiryElement.setPartNumber(satairList.get(0).getPartNumber().trim());
									clientInquiryElement.setAmount(inquiryList.get(k).getAmount());
									clientInquiryElement.setClientInquiryId(inquiryList.get(k).getClientInquiryId());
									clientInquiryElement.setDescription(satairList.get(0).getDescription());
						            List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim()));
						            Element element2 = new Element();
						            if (element.size()==0) {
						            	byte[] p = clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim()).getBytes();
						            	Byte[] pBytes = new Byte[p.length];
						            	for(int j=0;j<p.length;j++){
						            		pBytes[j] = Byte.valueOf(p[j]);
						            	}
						            	element2.setPartNumberCode(pBytes);
						            	element2.setUpdateTimestamp(new Date());
										elementDao.insert(element2);
										clientInquiryElement.setElementId(element2.getId());
									}else {
										Element element3 = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim())).get(0);
										clientInquiryElement.setElementId(element3.getId());
									}
						            if (satairList.get(0).getUnit() != null && !"".equals(satairList.get(0).getUnit())) {
						            	clientInquiryElement.setUnit(satairList.get(0).getUnit().trim());
									}else {
										clientInquiryElement.setUnit(inquiryList.get(k).getUnit());
									}
						            clientInquiryElement.setItem(clientInquiryElementDao.getMaxItem(crawList.get(i).getClientInquiryId())+1);
						            clientInquiryElement.setCsn(inquiryList.get(k).getCsn());
						            clientInquiryElement.setMainId(inquiryList.get(k).getId());
						            clientInquiryElement.setIsMain(1);
						            clientInquiryElement.setElementStatusId(700);
						            clientInquiryElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(satairList.get(0).getPartNumber().trim()));
						            clientInquiryElement.setSource("爬虫");
						            clientInquiryElementDao.insertSelective(clientInquiryElement);
						            supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
								}else {
									supplierInquiryElement.setClientInquiryElementId(inquiryList.get(k).getId());
								}
							}else {
								supplierInquiryElement.setClientInquiryElementId(inquiryList.get(k).getId());
							}
							supplierInquiryElementDao.insertSelective(supplierInquiryElement);
							if (satairList.size() == 1) {
								SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
								supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
								if (!satairList.get(0).getPartNumber().trim().equals(inquiryList.get(k).getPartNumber().trim())) {
									supplierQuoteElement.setPartNumber(satairList.get(0).getPartNumber());
								}else {
									supplierQuoteElement.setPartNumber(satairList.get(0).getPartNumber());
								}
								supplierQuoteElement.setAmount(inquiryList.get(k).getAmount());
								if (satairList.get(0).getAmount() >1) {
									supplierQuoteElement.setMoq(satairList.get(0).getAmount().intValue());
								}
								Integer cerId = satairQuoteElementDao.getCerIdByCode(satairList.get(0).getCertification().trim());
								if (cerId == null) {
									cerId = satairQuoteElementDao.getCerIdByCode("Other");
									
								}
								if (satairList.get(0).getCageCode() != null) {
									supplierQuoteElement.setRemark(satairList.get(0).getCageCode());
								}
								supplierQuoteElement.setCertificationId(cerId);
								List<SystemCode> FN = systemCodeDao.selectByAllCode("FN");
								List<SystemCode> NE = systemCodeDao.selectByAllCode("NE");
								if (satairList.get(0).getLeadTime().trim().equals("In stock")) {
									supplierQuoteElement.setConditionId(NE.get(0).getId());
									supplierQuoteElement.setLeadTime("3");
									//supplierQuoteElement.setLeadTime("1");
								}else {
									supplierQuoteElement.setConditionId(FN.get(0).getId());
									supplierQuoteElement.setLeadTime(satairList.get(0).getLeadTime().toString());
								}
								supplierQuoteElement.setDescription(satairList.get(0).getDescription());
								if (satairList.get(0).getUnit() != null && !"".equals(satairList.get(0).getUnit())) {
									supplierQuoteElement.setUnit(satairList.get(0).getUnit());
								}else {
									supplierQuoteElement.setUnit("EA");
								}
								if (satairList.get(0).getUnitPrice() != null && !"".equals(satairList.get(0).getUnitPrice())) {
									satairList.get(0).setUnitPrice(satairList.get(0).getUnitPrice().replaceAll(",",""));
								}
								if (satairList.get(0).getStorageAmount() != null) {
									supplierQuoteElement.setAvailableQty(new Double(satairList.get(0).getStorageAmount()));
								}
								if (new Double(satairList.get(0).getUnitPrice()) > 0) {
									supplierQuoteElement.setPrice(new Double(satairList.get(0).getUnitPrice()));
								}else {
									continue;
								}
								supplierQuoteElement.setSupplierQuoteStatusId(70);
								supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
								supplierQuoteElement.setElementId(inquiryList.get(k).getElementId());
								supplierQuoteElement.setFeeForExchangeBill(60.0);
								//获取危险品列表
								ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
								Integer count = clientInquiryElementDao.getDangerList(clientInquiryElement);
								String[] loca = satairList.get(0).getPlant().split(" ");
								supplierQuoteElement.setLocation(loca[loca.length-1]);
								//不同地区接收不同的提后换单费
								List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
								if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
									if (feeList.size() > 0 && feeList.get(0) != null) {
										boolean in = false;
										for (int l = 0; l < feeList.size(); l++) {
											if (feeList.get(l).getFeeExchangeBill() != null) {
												supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(l).getFeeExchangeBill()));
												in = true;
												break;
											}
										}
										if (!in) {
											supplierQuoteElement.setFeeForExchangeBill(new Double(160));
											LocationFeeForExchangeBillMapping locationFeeForExchangeBillMapping = new LocationFeeForExchangeBillMapping();
											locationFeeForExchangeBillMapping.setLocation(supplierQuoteElement.getLocation().trim());
											locationFeeForExchangeBillMappingDao.insertSelective(locationFeeForExchangeBillMapping);
										}
									}else {
										supplierQuoteElement.setFeeForExchangeBill(new Double(160));
									}
								}
								
								if (satairList.get(0).getIfDanger().equals("1") || count > 0) {
									supplierQuoteElement.setHazmatFee(new Double(350));
									supplierQuoteElement.setFeeForExchangeBill(180.0);
								}
								supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
								if (year == 2017) {
									 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
									 Date date = sdf.parse("2017-12-31");
									 supplierQuoteElement.setValidity(date);
								}else {
									GregorianCalendar gc = new GregorianCalendar();
									Date today = new Date();
									gc.setTime(today);
									gc.add(5,30);
									supplierQuoteElement.setValidity(gc.getTime());
								}
								supplierQuoteElement.setBankCost(new Double(35));
								supplierQuoteElementDao.insertSelective(supplierQuoteElement);
								double price = 0.00;
								ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
									price = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
								}else {
									price = supplierQuoteElement.getPrice();
								}
								List<HighPriceCrawlQuote> lists = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
								if (price > new Double(5000) && lists.size() == 0) {
									HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
									highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
								}
								clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
								ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
								if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
									if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
										if (clientQuoteElement != null) {
											clientInquiryElement.setElementStatusId(713);
										}
									}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701|| clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700){
										clientInquiryElement.setElementStatusId(702);
									}
									clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
									if (clientInquiryElement.getMainId() !=null && !"".equals(clientInquiryElement.getMainId())) {
										ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElement.getMainId());
										mainElement.setElementStatusId(702);
										clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
									}else {
										List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(clientInquiryElement.getId());
										for (int j = 0; j < alterList.size(); j++) {
											alterList.get(j).setElementStatusId(702);
											clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
										}
									}
								}
							}else if (satairList.size() > 1) {
								//String[] loca = satairList.get(0).getPlant().split(" ");
								String locationString = "";
								StringBuffer remark = new StringBuffer();
								double amount = 0;
								for (int j = 0; j < satairList.size(); j++) {
									SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
									supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
									if (satairList.get(j).getEnterPartnumber() != null && !"".equals(satairList.get(j).getEnterPartnumber())) {
										supplierQuoteElement.setPartNumber(satairList.get(j).getEnterPartnumber());
										supplierQuoteElement.setAlterPartNumber(satairList.get(j).getPartNumber());
									}else {
										supplierQuoteElement.setPartNumber(satairList.get(j).getPartNumber());
									}
									if (satairList.get(j).getCageCode() != null) {
										supplierQuoteElement.setRemark(satairList.get(j).getCageCode());
									}
									supplierQuoteElement.setAmount(inquiryList.get(k).getAmount());
									if (satairList.get(j).getAmount() >1) {
										supplierQuoteElement.setMoq(new Double(amount).intValue());
									}
									Integer cerId = satairQuoteElementDao.getCerIdByCode(satairList.get(j).getCertification().trim());
									if (cerId != null) {
										supplierQuoteElement.setCertificationId(cerId);
									}else {
										supplierQuoteElement.setCertificationId(54);
									}
									List<SystemCode> FN = systemCodeDao.selectByAllCode("FN");
									List<SystemCode> NE = systemCodeDao.selectByAllCode("NE");
									if (satairList.get(j).getLeadTime().trim().equals("In stock")) {
										supplierQuoteElement.setConditionId(NE.get(0).getId());
										supplierQuoteElement.setLeadTime("0");
									}else {
										supplierQuoteElement.setConditionId(FN.get(0).getId());
										supplierQuoteElement.setLeadTime(satairList.get(j).getLeadTime().toString());
									}
									supplierQuoteElement.setDescription(satairList.get(j).getDescription());
									supplierQuoteElement.setUnit(satairList.get(j).getUnit());
									if (satairList.get(j).getUnitPrice() != null && !"".equals(satairList.get(j).getUnitPrice())) {
										satairList.get(j).setUnitPrice(satairList.get(j).getUnitPrice().replaceAll(",",""));
									}
									supplierQuoteElement.setPrice(new Double(satairList.get(j).getUnitPrice()));
									supplierQuoteElement.setSupplierQuoteStatusId(70);
									if (supplierQuoteElement.getRemark() != null) {
										supplierQuoteElement.setRemark(supplierQuoteElement.getRemark() + remark.toString());
									}else {
										supplierQuoteElement.setRemark(remark.toString());
									}
  									String[] loca = satairList.get(j).getPlant().split(" ");
									locationString = loca[loca.length-1];
									if (satairList.get(j).getStorageAmount() != null && !"".equals(satairList.get(j).getStorageAmount())) {
										supplierQuoteElement.setAvailableQty(new Double(satairList.get(j).getStorageAmount()));
									}
									supplierQuoteElement.setLocation(locationString);
									List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
									if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
										if (feeList.size() > 0 && feeList.get(0) != null) {
											boolean in = false;
											for (int l = 0; l < feeList.size(); l++) {
												if (feeList.get(l).getFeeExchangeBill() != null) {
													supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(l).getFeeExchangeBill()));
													in = true;
													break;
												}
											}
											if (!in) {
												supplierQuoteElement.setFeeForExchangeBill(new Double(160));
												LocationFeeForExchangeBillMapping locationFeeForExchangeBillMapping = new LocationFeeForExchangeBillMapping();
												locationFeeForExchangeBillMapping.setLocation(supplierQuoteElement.getLocation().trim());
												locationFeeForExchangeBillMappingDao.insertSelective(locationFeeForExchangeBillMapping);
											}
										}else {
											supplierQuoteElement.setFeeForExchangeBill(new Double(160));
										}
									}
									supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
									supplierQuoteElement.setElementId(inquiryList.get(k).getElementId());
									if (satairList.get(j).getIfDanger().equals("1")) {
										//supplierQuoteElement.setRemark(supplierQuoteElement.getRemark()+",DG $350，not include)");
										supplierQuoteElement.setHazmatFee(new Double(350));
										supplierQuoteElement.setFeeForExchangeBill(180.0);
									}
									supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
									if (year == 2017) {
										 SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
										 Date date = sdf.parse("2017-12-31");
										 supplierQuoteElement.setValidity(date);
									}else {
										GregorianCalendar gc = new GregorianCalendar();
										Date today = new Date();
										gc.setTime(today);
										gc.add(5,30);
										supplierQuoteElement.setValidity(gc.getTime());
									}
									supplierQuoteElement.setBankCost(new Double(35));
									supplierQuoteElementDao.insertSelective(supplierQuoteElement);
									double price = 0.00;
									ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
									if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
										price = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
									}else {
										price = supplierQuoteElement.getPrice();
									}
									List<HighPriceCrawlQuote> lists = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									if (price > new Double(5000) && lists.size() == 0) {
										HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
										highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
										highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
									}
									clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
									//break;
								}
								ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
								ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
								if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
									if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
										if (clientQuoteElement != null) {
											clientInquiryElement.setElementStatusId(713);
										}
									}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701 || clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700){
										clientInquiryElement.setElementStatusId(702);
									}
									clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
									if (clientInquiryElement.getMainId() !=null && !"".equals(clientInquiryElement.getMainId())) {
				            			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElement.getMainId());
				            			if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
				            				mainElement.setElementStatusId(702);
					            			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
										}
				            		}else {
				            			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(clientInquiryElement.getId());
				            			for (int j = 0; j < alterList.size(); j++) {
				            				if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
				            					alterList.get(j).setElementStatusId(702);
					            				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
											}
				            				
				            			}
				            		}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				crawList.get(i).setSupplierQuoteId(null);
				satairQuoteDao.unLockTheMessage(crawList.get(i));
				e.printStackTrace();
			}
		}
		
	}
	
	public void listPage(PageModel<SatairQuoteElement> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(satairQuoteElementDao.listPage(page));
	}
	
	public void list(PageModel<SatairQuoteElement> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", "order by a.update_datetime desc");
		}
		
		page.setEntities(satairQuoteElementDao.list(page));
	}

}
