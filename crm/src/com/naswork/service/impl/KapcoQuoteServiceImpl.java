package com.naswork.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.naswork.dao.KapcoQuoteDao;
import com.naswork.dao.KapcoQuoteElementDao;
import com.naswork.dao.LocationFeeForExchangeBillMappingDao;
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
import com.naswork.model.KapcoQuote;
import com.naswork.model.KapcoQuoteElement;
import com.naswork.model.KlxQuote;
import com.naswork.model.KlxQuoteElement;
import com.naswork.model.LocationFeeForExchangeBillMapping;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.KapcoQuoteService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.TPartService;

@Service("kapcoQuoteService")
public class KapcoQuoteServiceImpl implements KapcoQuoteService {
	
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
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
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private KapcoQuoteDao kapcoQuoteDao;
	@Resource
	private KapcoQuoteElementDao kapcoQuoteElementDao;
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
	
    /**
    @Author: Modify by white
    @DateTime: 2018/9/18 15:28
    @Description: 修改银行手续费为35
    */
	public void createQuote(){
		List<KapcoQuote> crawList = kapcoQuoteDao.getFinishList();
		//锁定记录
		for (int i = 0; i < crawList.size(); i++) {
			kapcoQuoteDao.lockMessage(crawList.get(i));
		}
		for (int i = 0; i < crawList.size(); i++) {
			try {
				List<KapcoQuoteElement> klxList = kapcoQuoteElementDao.selectByClientInquiryId(crawList.get(i).getClientInquiryId(),crawList.get(i).getId());
				if (klxList.size() > 0) {
					Supplier supplier = supplierDao.findByCode("1005");
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
					Integer supplierQuoteId = supplierQuote.getId();
					
					ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(crawList.get(i).getClientInquiryId());
					if (clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId().equals(31)) {
						clientInquiry.setInquiryStatusId(35);
						clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
					}
					SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
					supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
					List<ClientInquiryElement> inquiryList = clientInquiryElementDao.findByclientInquiryId(crawList.get(i).getClientInquiryId());
					//for (ClientInquiryElement element : inquiryList) {
					for (KapcoQuoteElement kapcoQuoteElement : klxList) {
						//AviallQuoteElement aviallQuoteElement = aviallQuoteElementDao.getByClientInquiryElementId(element.getId());
						ClientInquiryElement element = clientInquiryElementDao.selectByPrimaryKey(kapcoQuoteElement.getClientInquiryElementId());
						if (kapcoQuoteElement != null) {
							if (!kapcoQuoteElement.getPartNumber().trim().equals(element.getPartNumber().trim())) {
								ClientInquiryElement inquiryElement = clientInquiryElementDao.selectByPartNumber(kapcoQuoteElement.getPartNumber().trim(),crawList.get(i).getClientInquiryId().toString());
								if (inquiryElement == null) {
									ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
									clientInquiryElement.setPartNumber(kapcoQuoteElement.getPartNumber().trim());
									clientInquiryElement.setAmount(element.getAmount());
									clientInquiryElement.setClientInquiryId(element.getClientInquiryId());
									clientInquiryElement.setDescription(element.getDescription());
						            List<Element> elements = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(kapcoQuoteElement.getPartNumber().trim()));
						            Element element2 = new Element();
						            if (elements.size()==0) {
						            	byte[] p = clientInquiryService.getCodeFromPartNumber(kapcoQuoteElement.getPartNumber().trim()).getBytes();
						            	Byte[] pBytes = new Byte[p.length];
						            	for(int j=0;j<p.length;j++){
						            		pBytes[j] = Byte.valueOf(p[j]);
						            	}
						            	element2.setPartNumberCode(pBytes);
						            	element2.setUpdateTimestamp(new Date());
										elementDao.insert(element2);
										clientInquiryElement.setElementId(element2.getId());
									}else {
										Element element3 = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(kapcoQuoteElement.getPartNumber().trim())).get(0);
										clientInquiryElement.setElementId(element3.getId());
									}
						            if (kapcoQuoteElement.getUnit() != null && !"".equals(kapcoQuoteElement.getUnit())) {
						            	clientInquiryElement.setUnit(kapcoQuoteElement.getUnit().trim());
									}else {
										clientInquiryElement.setUnit(element.getUnit());
									}
						            clientInquiryElement.setItem(clientInquiryElementDao.getMaxItem(crawList.get(i).getClientInquiryId())+1);
						            clientInquiryElement.setCsn(element.getCsn());
						            clientInquiryElement.setMainId(element.getId());
						            clientInquiryElement.setIsMain(1);
						            clientInquiryElement.setElementStatusId(700);
						            clientInquiryElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(kapcoQuoteElement.getPartNumber().trim()));
						            clientInquiryElement.setSource("爬虫");
						            clientInquiryElementDao.insertSelective(clientInquiryElement);
						            supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
								}else {
									supplierInquiryElement.setClientInquiryElementId(inquiryElement.getId());
								}
								
					           
							}else {
								supplierInquiryElement.setClientInquiryElementId(element.getId());
							}
							/*List<SupplierInquiryElement> elements = supplierInquiryElementDao.getListByInquiryElementId(supplierInquiryElement.getClientInquiryElementId());
							if (elements.size() == 0) {
								
							}else {
								supplierInquiryElement = elements.get(0);
							}*/
							supplierInquiryElementDao.insertSelective(supplierInquiryElement);
					        SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
					        supplierQuoteElement.setSupplierQuoteId(supplierQuoteId);
					        supplierQuoteElement.setPartNumber(kapcoQuoteElement.getPartNumber().trim());
					        supplierQuoteElement.setAmount(element.getAmount());
							supplierQuoteElement.setCertificationId(55);
							List<SystemCode> FN = systemCodeDao.selectByAllCode("FN");
							supplierQuoteElement.setConditionId(FN.get(0).getId());
							supplierQuoteElement.setDescription(kapcoQuoteElement.getDescription());
							if (kapcoQuoteElement.getUnit() != null && !"".equals(kapcoQuoteElement.getUnit())) {
								supplierQuoteElement.setUnit(kapcoQuoteElement.getUnit().trim());
							}else {
								supplierQuoteElement.setUnit("EA");
							}
							String price = kapcoQuoteElement.getUnitPrice().replace(",", "");
							if (new Double(price.replace("$", "")) > 0) {
								supplierQuoteElement.setPrice(new Double(price.replace("$", "")));
							}else {
								continue;
							}
							supplierQuoteElement.setSupplierQuoteStatusId(70);
							supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
							supplierQuoteElement.setElementId(element.getElementId());
							supplierQuoteElement.setFeeForExchangeBill(60.0);
							supplierQuoteElement.setBankCost(25.0);
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
							if (kapcoQuoteElement.getAmount() > 1) {
								supplierQuoteElement.setMoq(kapcoQuoteElement.getAmount().intValue());
							}
							int beacon = kapcoQuoteElement.getDescription().toLowerCase().indexOf("beacon");
							int battery = kapcoQuoteElement.getDescription().toLowerCase().indexOf("battery");
							int oxygen = kapcoQuoteElement.getDescription().toLowerCase().indexOf("oxygen");
							int generator = kapcoQuoteElement.getDescription().toLowerCase().indexOf("generator");
							int extinguisher = kapcoQuoteElement.getDescription().toLowerCase().indexOf("extinguisher");
							int raft = kapcoQuoteElement.getDescription().toLowerCase().indexOf("raft");
							int slider = kapcoQuoteElement.getDescription().toLowerCase().indexOf("slider");
							int elt = kapcoQuoteElement.getDescription().toLowerCase().indexOf("elt");
							int recorder = kapcoQuoteElement.getDescription().toLowerCase().indexOf("recorder");
							if (kapcoQuoteElement.getDanger() != null && (kapcoQuoteElement.getDanger() == 1 || "1".equals(kapcoQuoteElement.getDanger().toString()))) {
								supplierQuoteElement.setHazmatFee(new Double(350));
								supplierQuoteElement.setFeeForExchangeBill(180.0);
							}else if (battery >= 0 || beacon >= 0 || oxygen >= 0 || generator >= 0 || extinguisher >= 0 || raft >= 0 || elt >= 0 || slider >= 0 || recorder >= 0) {
//								if (supplierQuoteElement.getRemark() != null) {
//									supplierQuoteElement.setRemark(supplierQuoteElement.getRemark()+",DG fee ($200 not include)");
//								}else {
//									supplierQuoteElement.setRemark("DG fee ($200 not include)");
//								}
								supplierQuoteElement.setHazmatFee(new Double(350));
								supplierQuoteElement.setFeeForExchangeBill(180.0);
							}
							if (kapcoQuoteElement.getLeadTime().trim() != "0" && !"0".equals(kapcoQuoteElement.getLeadTime().trim())) {
								/*if (!kapcoQuoteElement.getLeadTime().equals(5)) {
									Double crawlLeadTime = new Double(clientInquiryService.getNumber(kapcoQuoteElement.getLeadTime()));
									crawlLeadTime = crawlLeadTime + crawlLeadTime/7*2;
									supplierQuoteElement.setLeadTime(new Double(Math.ceil(crawlLeadTime)).toString());
								}else {
									supplierQuoteElement.setLeadTime(kapcoQuoteElement.getLeadTime());
								}*/
								supplierQuoteElement.setLeadTime(kapcoQuoteElement.getLeadTime());
							}else {
								supplierQuoteElement.setLeadTime("3");
							}
							int index = 0;
							supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
							if (kapcoQuoteElement.getStockMessage() != null && !"".equals(kapcoQuoteElement.getStockMessage())) {
								int find = kapcoQuoteElement.getStockMessage().indexOf("),");
								//是否只有一个库存
								if (find > 0) {
									String[] location = kapcoQuoteElement.getStockMessage().split("\\),");
									boolean canInsert = true;
									if (location.length > 0) {
										for (int j = 0; j < location.length; j++) {
											if (location[j].indexOf("(") >= 0) {
												String[] array = location[j].split("\\(");
												String[] loca = array[0].split(",");
												supplierQuoteElement.setLocation(loca[0]);
												List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
												if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
													if (feeList.size() > 0 && feeList.get(0) != null) {
														boolean in = false;
														for (int k = 0; k < feeList.size(); k++) {
															if (feeList.get(k).getFeeExchangeBill() != null) {
																supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(k).getFeeExchangeBill()));
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
												supplierQuoteElement.setAvailableQty(new Double(array[1].trim()));
												if (supplierQuoteElement.getAvailableQty() < supplierQuoteElement.getAmount() && !"".equals(supplierQuoteElement.getAvailableQty()) && supplierQuoteElement.getAvailableQty() > 0) {
													supplierQuoteElement.setAmount(supplierQuoteElement.getAvailableQty());
												}else if (supplierQuoteElement.getAvailableQty() == null || "".equals(supplierQuoteElement.getAvailableQty()) || supplierQuoteElement.getAvailableQty().equals(new Double(0))) {
													supplierQuoteElement.setAmount(0.0);
													if (kapcoQuoteElement.getLeadTime() != null && kapcoQuoteElement.getLeadTime().trim() != "0" && !"0".equals(kapcoQuoteElement.getLeadTime().trim())) {
														Double crawlLeadTime = new Double(clientInquiryService.getNumber(kapcoQuoteElement.getLeadTime()));
														crawlLeadTime = crawlLeadTime + crawlLeadTime/7*2;
														supplierQuoteElement.setLeadTime(new Double(Math.ceil(crawlLeadTime)).toString());
													}else {
														canInsert = false;
													}
													
												}
												index = j+1;
												if (new Double(array[1].trim()) >= 0) {
													break;
												}
											}
										}
									}
									if (canInsert) {
										supplierQuoteElement.setBankCost(new Double(35));
										supplierQuoteElementDao.insertSelective(supplierQuoteElement);
										double quotePrice = 0.00;
										ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
										if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
											quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
										}else {
											quotePrice = supplierQuoteElement.getPrice();
										}
										List<HighPriceCrawlQuote> list = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
										if (quotePrice > new Double(5000) && list.size() == 0) {
											HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
											highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
											highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
										}
										clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
									}
									addExtraQuote(location,index,supplierQuoteElement,supplierQuote,supplier,exchangeRate,element.getAmount(),kapcoQuoteElement);
								}else {
									boolean canInsert = true;
									String[] array = kapcoQuoteElement.getStockMessage().split("\\(");
									String[] loca = array[0].split(",");
									supplierQuoteElement.setLocation(loca[0]);
									List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
									if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
										if (feeList.size() > 0 && feeList.get(0) != null) {
											boolean in = false;
											for (int k = 0; k < feeList.size(); k++) {
												if (feeList.get(k).getFeeExchangeBill() != null) {
													supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(k).getFeeExchangeBill()));
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
									supplierQuoteElement.setAvailableQty(new Double(array[1].substring(0, array[1].length()-1)));
									if (supplierQuoteElement.getAvailableQty() < supplierQuoteElement.getAmount() && !"".equals(supplierQuoteElement.getAvailableQty()) && supplierQuoteElement.getAvailableQty() > 0) {
										supplierQuoteElement.setAmount(supplierQuoteElement.getAvailableQty());
									}else if (supplierQuoteElement.getAvailableQty() == null || "".equals(supplierQuoteElement.getAvailableQty()) || supplierQuoteElement.getAvailableQty().equals(new Double(0))) {
										supplierQuoteElement.setAmount(0.0);
										if (kapcoQuoteElement.getLeadTime() != null && kapcoQuoteElement.getLeadTime().trim() != "0" && !"0".equals(kapcoQuoteElement.getLeadTime().trim())) {
											Double crawlLeadTime = new Double(clientInquiryService.getNumber(kapcoQuoteElement.getLeadTime()));
											crawlLeadTime = crawlLeadTime + crawlLeadTime/7*2;
											supplierQuoteElement.setLeadTime(new Double(Math.ceil(crawlLeadTime)).toString());
										}else {
											canInsert = false;
										}
									}
//									if (new Double(array[1].substring(0, array[1].length()-1)) > 0) {
//										supplierQuoteElementDao.insertSelective(supplierQuoteElement);
//									}
									if (canInsert) {
										supplierQuoteElement.setBankCost(new Double(35));
										supplierQuoteElementDao.insertSelective(supplierQuoteElement);
										double quotePrice = 0.00;
										ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
										if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
											quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
										}else {
											quotePrice = supplierQuoteElement.getPrice();
										}
										List<HighPriceCrawlQuote> list = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
										if (quotePrice > new Double(5000) && list.size() == 0) {
											HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
											highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
											highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
										}
										clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
									}
								}
								
							}else {
								if (kapcoQuoteElement.getLeadTime() != null && kapcoQuoteElement.getLeadTime().trim() != "0" && !"0".equals(kapcoQuoteElement.getLeadTime().trim())) {
									Double crawlLeadTime = new Double(clientInquiryService.getNumber(kapcoQuoteElement.getLeadTime()));
									crawlLeadTime = crawlLeadTime + crawlLeadTime/7*2;
									supplierQuoteElement.setLeadTime(new Double(Math.ceil(crawlLeadTime)).toString());
									supplierQuoteElement.setBankCost(new Double(35));
									supplierQuoteElementDao.insertSelective(supplierQuoteElement);
									double quotePrice = 0.00;
									ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
									if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
										quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
									}else {
										quotePrice = supplierQuoteElement.getPrice();
									}
									List<HighPriceCrawlQuote> list = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									if (quotePrice > new Double(5000) && list.size() == 0) {
										HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
										highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
										highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
									}
									clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
								}
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
			} catch (Exception e) {
				crawList.get(i).setSupplierQuoteId(null);
				kapcoQuoteDao.unLockMessage(crawList.get(i));
				e.printStackTrace();
			}
		}
	}
	
	public void addExtraQuote(String[] loca,int index,SupplierQuoteElement supplierQuoteElement,SupplierQuote supplierQuote,Supplier supplier,ExchangeRate exchangeRate,Double amount,KapcoQuoteElement kapcoQuoteElement){
		for (int i = index; i < loca.length; i++) {
			boolean canInsert = true;
			supplierQuoteElement.setAmount(amount);
			if (loca[i].indexOf("(") >= 0) {
				String[] array = loca[i].split("\\(");
				String[] location = array[0].split(",");
				supplierQuoteElement.setLocation(location[0]);
				List<LocationFeeForExchangeBillMapping> feeList = locationFeeForExchangeBillMappingDao.getByLocation(supplierQuoteElement.getLocation());
				if (!(supplierQuoteElement.getHazmatFee() != null &&  supplierQuoteElement.getHazmatFee()> 0)) {
					if (feeList.size() > 0 && feeList.get(0) != null) {
						boolean in = false;
						for (int k = 0; k < feeList.size(); k++) {
							if (feeList.get(k).getFeeExchangeBill() != null) {
								supplierQuoteElement.setFeeForExchangeBill(new Double(feeList.get(k).getFeeExchangeBill()));
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
				if (i != loca.length-1) {
					supplierQuoteElement.setAvailableQty(new Double(array[1].trim()));
					if (supplierQuoteElement.getAvailableQty() < supplierQuoteElement.getAmount() && !"".equals(supplierQuoteElement.getAvailableQty()) && supplierQuoteElement.getAvailableQty() > 0) {
						supplierQuoteElement.setAmount(supplierQuoteElement.getAvailableQty());
					}else if (supplierQuoteElement.getAvailableQty() == null || "".equals(supplierQuoteElement.getAvailableQty()) || supplierQuoteElement.getAvailableQty().equals(new Double(0))) {
						supplierQuoteElement.setAmount(0.0);
						if (kapcoQuoteElement.getLeadTime() != null && kapcoQuoteElement.getLeadTime().trim() != "0" && !"0".equals(kapcoQuoteElement.getLeadTime().trim())) {
							Double crawlLeadTime = new Double(clientInquiryService.getNumber(kapcoQuoteElement.getLeadTime()));
							crawlLeadTime = crawlLeadTime + crawlLeadTime/7*2;
							supplierQuoteElement.setLeadTime(new Double(Math.ceil(crawlLeadTime)).toString());
						}else {
							canInsert = false;
						}
					}
				}else{
					supplierQuoteElement.setAvailableQty(new Double(array[1].substring(0, array[1].length()-1)));
					if (supplierQuoteElement.getAvailableQty() < supplierQuoteElement.getAmount() && !"".equals(supplierQuoteElement.getAvailableQty()) && supplierQuoteElement.getAvailableQty() > 0) {
						supplierQuoteElement.setAmount(supplierQuoteElement.getAvailableQty());
					}else if (supplierQuoteElement.getAvailableQty() == null || "".equals(supplierQuoteElement.getAvailableQty()) || supplierQuoteElement.getAvailableQty().equals(new Double(0))) {
						supplierQuoteElement.setAmount(0.0);
						if (kapcoQuoteElement.getLeadTime() != null && kapcoQuoteElement.getLeadTime().trim() != "0" && !"0".equals(kapcoQuoteElement.getLeadTime().trim())) {
							Double crawlLeadTime = new Double(clientInquiryService.getNumber(kapcoQuoteElement.getLeadTime()));
							crawlLeadTime = crawlLeadTime + crawlLeadTime/7*2;
							supplierQuoteElement.setLeadTime(new Double(Math.ceil(crawlLeadTime)).toString());
						}else {
							canInsert = false;
						}
					}
				}
				if (supplierQuoteElement.getAvailableQty() > 0) {
					List<SupplierQuote> quoteList = supplierQuoteDao.selectBySupplierInquiryId(supplierQuote.getSupplierInquiryId());
					int insertFlag = 0;
					for (SupplierQuote quote : quoteList) {
						supplierQuoteElement.setSupplierQuoteId(quote.getId());
						SupplierQuoteElement element = supplierQuoteElementDao.findSupplierQuoteElement(supplierQuoteElement);
						if (element == null) {
							supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
							if (canInsert) {
								supplierQuoteElement.setBankCost(new Double(35));
								supplierQuoteElementDao.insertSelective(supplierQuoteElement);
								double quotePrice = 0.00;
								ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
									quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
								}else {
									quotePrice = supplierQuoteElement.getPrice();
								}
								List<HighPriceCrawlQuote> list = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
								if (quotePrice > new Double(5000) && list.size() == 0) {
									HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
									highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
								}
								clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
							}
							insertFlag = 1;
							break;
						}
					}
					if (insertFlag == 0) {
						for (SupplierQuote quote : quoteList) {
							supplierQuoteElement.setSupplierQuoteId(quote.getId());
							int pos = -2;
						    int n = 0;
						    int subIndex = 0;
							String quoteNumber = "";
							while (pos != -1) {
						        if (pos == -2) {
						            pos = -1;
						        }
						        pos = supplierQuote.getQuoteNumber().indexOf("-", pos + 1);
						        if (pos != -1) {
						            n++;
						            subIndex = pos;
						        }
						    }
							//int index2 = supplierQuote.getQuoteNumber().indexOf("0", 1);
							if (n == 2 ) {
								quoteNumber = supplierQuote.getQuoteNumber().substring(0, subIndex).toString();
								quoteNumber = quoteNumber+"-"+(quoteList.size()+1);
							}else {
								quoteNumber = supplierQuote.getQuoteNumber()+"-"+(quoteList.size()+1);
							}
							 
							SupplierQuote supplierQuote2 = supplierQuote;
							supplierQuote2.setQuoteNumber(quoteNumber);
							supplierQuote2.setId(null);
							supplierQuoteDao.insertSelective(supplierQuote2);
							supplierQuoteElement.setSupplierQuoteId(supplierQuote2.getId());
							supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
							if (canInsert) {
								supplierQuoteElement.setBankCost(new Double(35));
								supplierQuoteElementDao.insertSelective(supplierQuoteElement);
								double quotePrice = 0.00;
								ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (!supplier.getCurrencyId().equals(usRate.getCurrencyId())) {
									quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate())).doubleValue();
								}else {
									quotePrice = supplierQuoteElement.getPrice();
								}
								List<HighPriceCrawlQuote> list = highPriceCrawlQuoteDao.getBySupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
								if (quotePrice > new Double(5000) && list.size() == 0) {
									HighPriceCrawlQuote highPriceCrawlQuote = new HighPriceCrawlQuote();
									highPriceCrawlQuote.setSupplierQuoteId(supplierQuoteElement.getSupplierQuoteId());
									highPriceCrawlQuoteDao.insertSelective(highPriceCrawlQuote);
								}
								clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
							}
							break;
						}
					}
				}
			}
		}
	}
	
}
