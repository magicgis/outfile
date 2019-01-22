package com.naswork.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

import com.naswork.dao.AirSupplierRelationDao;
import com.naswork.dao.AuthorityRelationDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.EmailMessageDao;
import com.naswork.dao.PartAndEmailDao;
import com.naswork.dao.SeqKeyDao;
import com.naswork.dao.SupplierAirRelationDao;
import com.naswork.dao.SupplierCommissionSaleDao;
import com.naswork.dao.SupplierContactDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierPnRelationDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.EmailMessage;
import com.naswork.model.PartAndEmail;
import com.naswork.model.SeqKey;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.EmailTemplateVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageVo;
import com.naswork.module.purchase.controller.supplierinquiry.SaveVo;
import com.naswork.module.purchase.controller.supplierinquiry.SupplierVo;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.GyExcelService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.UserService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Service("supplierInquiryService")
public class SupplierInquiryServiceImpl implements SupplierInquiryService {

	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource 
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private SeqKeyDao seqKeyDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierPnRelationDao supplierPnRelationDao;
	@Resource
	private UserDao userDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private SupplierContactDao supplierContactDao;
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private SupplierCommissionSaleDao supplierCommissionSaleDao;
	@Resource
	private SupplierAirRelationDao supplierAirRelationDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private EmailMessageDao emailMessageDao;
	@Resource
	private PartAndEmailDao partAndEmailDao;
	
	
	public static final Integer INIT_QUOTE_NUMBER_SEQ = new Integer(1000);
	
	public void findSupplier(PageModel<SupplierVo> page,String where) {
		if (!"".equals(where)) {
			page.put("where", where);
		}
		page.setEntities(supplierInquiryDao.listPage(page));
	}

	
	/*
     * 获取供应商条数
     */
    public Integer findCount(){
    	return supplierInquiryDao.findCount();
    }
    
    /*
     * 获取明细条数
     */
    public Integer findCount2(Integer id){
    	return supplierInquiryDao.findCount2(id);
    }
    
    /*
     * 插入
     */
    public int insertSelective(SupplierInquiry supplierInquiry){
    	return supplierInquiryDao.insertSelective(supplierInquiry);
    }
    
    /*
     * 根据客户询价的ID查询
     */
    public SupplierInquiry findClientInquiryElement(Integer id){
    	return supplierInquiryDao.findClientInquiryElement(id);
    }
    
    /*
     * 拼接单号
     */
    public String getQuoteNumberSeq(Date date,Integer supplierId){
    	StringBuffer buffer = new StringBuffer();
    	try {	
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		String yearKey = Integer.toString(year % 100);
		buffer.append(StringUtils.leftPad(yearKey, 2, '0'));
		SeqKey seqKey = new SeqKey();
		seqKey.setKeyName(String.valueOf(yearKey));
		seqKey.setSchemaName("crm");
		seqKey.setTableName("supplier_inquiry");
		SeqKey seq = seqKeyDao.findMaxSeq(seqKey);
		Integer seqNumber = null;
		if(null!=seq) {
			seqNumber = seq.getSeq()+1;
			if (seqNumber != seq.getSeq()) {
				seqKey.setSeq(seqNumber);
				seqKeyDao.updateByPrimaryKeySelective(seqKey);
			}
		}
		if (null==seq) {
			seqKey.setSeq(1001);
			seqKeyDao.insertSelective(seqKey);
			seqNumber = 1001;
		}
		
		buffer.append(StringUtils.leftPad(Integer.toString(seqNumber), 6, '0'));
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	Supplier supplier = supplierDao.selectByPrimaryKey(supplierId);
    	buffer.append("-").append(supplier.getCode());
		return buffer.toString();
	}
    
    /*
     * 获取供应商询价ID
     */
    public Integer findId(SupplierInquiry supplierInquiry){
    	return supplierInquiryDao.findId(supplierInquiry);
    }
    
    /*
     * 管理页面数据
     */
    public void listManagePage(PageModel<ManageVo> page,String where,
			GridSort sort){
    	if (!where.equals("") && where != null) {
    		page.put("where", where);
		}
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(supplierInquiryDao.listManagePage(page));
    }
    
    /*
     * 获取供应商
     */
    public List<SupplierVo> suppliers(PageModel<SupplierVo> page){
    	return supplierInquiryDao.listPage(page);
    }
    
    /*
     * 供应商管理明细
     */
    public void ManageElementPage(PageModel<ManageElementVo> page){
    	page.setEntities(supplierInquiryDao.ManageElementPage(page));
    }
    
    public List<ManageElementVo> ManageElement(PageModel<ManageElementVo> page){
    	return supplierInquiryDao.ManageElementPage(page);
    }
    
    public List<ManageElementVo> Elements(Integer supplierInquiryId) {
		return supplierInquiryDao.ManageElement(supplierInquiryId);
	}
    
    /*
     * 主键查询
     */
    public ManageVo selectByPrimaryKey(PageModel<ManageVo> page,Integer id){
    	String where = "si.id = "+id;
    	page.put("where", where);
    	return supplierInquiryDao.listManagePage(page).get(0);
    }
    
    /*
     * 根据供应商询价Id查询
     */
    public Integer findBySupplierInquiryId(Integer supplierInquiryId){
    	return supplierQuoteDao.findBySupplierInquiryId(supplierInquiryId);
    }
    
    public SupplierInquiry selectByPrimaryKey(Integer id){
    	return supplierInquiryDao.selectByPrimaryKey(id);
    }
    
    /*
     * 获取供应商信息
     */
    public Supplier getSupplier(Integer supplierId){
    	return supplierDao.selectByPrimaryKey(supplierId);
    }
    
    /*
     * 获取供应商代码
     */
    public List<String> getQuoteNumbers(Integer clientInquiryId){
    	return supplierInquiryDao.getQuoteNumbers(clientInquiryId);
    }


	@Override
	public SupplierInquiry lookSupplierByImportPackage(ClientInquiry ci, Integer supplierId,String supplierCode) {
		SupplierInquiry supplierinquiry=supplierInquiryDao.findSupplierByImportPackage(ci.getId(), supplierId);
		
		if(null==supplierinquiry){
			supplierinquiry=new SupplierInquiry();
			Integer id=ci.getId();
			supplierinquiry.setClientInquiryId(id);
			supplierinquiry.setSupplierId(supplierId);
			supplierinquiry.setQuoteNumber(ci.getQuoteNumber()+"-"+supplierCode);
			supplierinquiry.setInquiryDate(ci.getInquiryDate());
			supplierinquiry.setDeadline(ci.getDeadline());
			supplierinquiry.setRemark(ci.getRemark());
			supplierInquiryDao.insertSelective(supplierinquiry);
		}
		
		return supplierinquiry;
	}
	
	public List<Supplier> getSupplier(PageModel<Supplier> page) {
		return supplierDao.Suppliers(page);
	}
	
	public String getEleCount(Integer id){
		SaveVo saveVo = supplierInquiryDao.getEleCount(id);
		StringBuffer rate = new StringBuffer();
		if (saveVo.getQuoteElementCount().equals(0)) {
			rate.append("0%");
			
		}else {
			Double Proportion = saveVo.getQuoteElementCount().doubleValue()/saveVo.getInquiryElementCount().doubleValue()*100;
			BigDecimal   b   =   new   BigDecimal(Proportion);  
			double   f1   =   b.setScale(2,   RoundingMode.HALF_UP).doubleValue();  
			rate.append(f1).append("%");
		}
		rate.append("(").append(saveVo.getQuoteElementCount()).append("/").append(saveVo.getInquiryElementCount()).append(")");
		return rate.toString();
	}
    
	public int updateByPrimaryKeySelective(SupplierInquiry record){
		return supplierInquiryDao.updateByPrimaryKeySelective(record);
	}
	
	public int getInquiryList(PageModel<FactoryVo> page,Integer clientInquiryId) {
		page.put("id", clientInquiryId);
		List<ClientInquiryElement> eleList = clientInquiryElementDao.findByclientInquiryIdNoBsn(page);
		SupplierInquiry supplierInquiry = findClientInquiryElement(clientInquiryId);
		List<FactoryVo> facList = new ArrayList<FactoryVo>();
		List<FactoryVo> List = new ArrayList<FactoryVo>();
		for (ClientInquiryElement clientInquiryElement : eleList) {
			/*FactoryVo factoryVo = new FactoryVo();
			factoryVo.setPartNum(clientInquiryElement.getPartNumber());
			factoryVo.setPartName(clientInquiryElement.getDescription());
			List.add(factoryVo);*/
			String partNumberCode = clientInquiryElementService.getCodeFromPartNumber(clientInquiryElement.getPartNumber());
			page.put("partNumberCode", partNumberCode);
			facList = supplierPnRelationDao.getTpartByMsnFlag(page);
			List<FactoryVo> match = new ArrayList<FactoryVo>();
			List<FactoryVo> unMatch = new ArrayList<FactoryVo>();

			for (int j = 0; j < facList.size(); j++) {
				facList.get(j).setId(clientInquiryElement.getId());
				facList.get(j).setConsultPartName(clientInquiryElement.getDescription());
				facList.get(j).setConsultPartNum(clientInquiryElement.getPartNumber());
				String[] name = facList.get(j).getPartName().trim().split(",");
				for (int i = 0; i < name.length; i++) {
					if (clientInquiryElement.getDescription() != null) {
						if (name[i].toLowerCase().equals(clientInquiryElement.getDescription().trim().toLowerCase())) {
							facList.get(j).setIfMatch(1);
							match.add(facList.get(j));
							break;
						}
					}else {
						facList.get(j).setIfMatch(0);
						unMatch.add(facList.get(j));
						break;
					}
					
				}
				if (facList.get(j).getIfMatch() == null){
					facList.get(j).setIfMatch(0);
					unMatch.add(facList.get(j));
				}
				//List.add(factoryVo2);
			}
			if (facList.size()==0) {
				FactoryVo faVo = new FactoryVo();
				faVo.setId(clientInquiryElement.getId());
				faVo.setConsultPartName(clientInquiryElement.getDescription());
				faVo.setConsultPartNum(clientInquiryElement.getPartNumber());
				faVo.setPartNum("没找到匹配的件号！");
				List.add(faVo);
			}
			for (FactoryVo factoryVo2 : match) {
				List.add(factoryVo2);
			}
			for (FactoryVo factoryVo2 : unMatch) {
				List.add(factoryVo2);
			}
			//List.add(new FactoryVo());
		}
		page.setEntities(List);
		return List.size();
	}


	@Override
	public List<SupplierInquiryStatistic> supplierInquiryStat(SupplierInquiryStatistic supplierInquiryStatistic) {
		return supplierInquiryDao.supplierInquiryStat(supplierInquiryStatistic);
	}
	
	public void inquiryPage(PageModel<ClientInquiryVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierInquiryDao.inquiryPage(page));
	}
	
	public Integer getQuoteCount(Integer supplierInquiryId){
		return supplierInquiryDao.getQuoteCount(supplierInquiryId);
	}
	
	public void findByClientQuoteNumberPage(PageModel<SupplierInquiry> page,String where,GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierInquiryDao.findByClientQuoteNumberPage(page));
	}
	
	public void sendEmail(SupplierInquiry supplierInquiry,String userId,String firstText,String secondText,List<SupplierContact> list,Integer ifAuto) {
		UserVo userVo = ContextHolder.getCurrentUser();
		List<SupplierContact> supplierContacts = new ArrayList<SupplierContact>();
		for (SupplierContact supplierContact : list) {
			if (supplierContact.getDataItem().equals("check") && supplierContact.getSupplierId().equals(supplierInquiry.getSupplierId())) {
				if (!supplierContacts.contains(supplierContact)) {
					supplierContacts.add(supplierContact);
				}
			}
		}
		List<ClientInquiryElement> elementList = new ArrayList<ClientInquiryElement>();
		if (ifAuto == 1) {
			elementList = clientInquiryElementDao.selectBySupplierInquiryIdWhenAuto(supplierInquiry.getId());
		}else {
			elementList = clientInquiryElementDao.selectBySupplierInquiryId(supplierInquiry.getId());
		}
		UserVo current = userDao.findUserByUserId(userId.toString());
		String businessKey = "supplier_inquiry.id."+supplierInquiry.getId()+"-"+ifAuto+".SupplierInquieyExcel";
		String realPath = "";
		String path = "";
		if (elementList.size()>0) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
				Date now = new Date();
				realPath = "D:/CRM/Files/mis/excel/sampleoutput/"+supplierInquiry.getQuoteNumber()+"_"+"SupplierInquiry"+"_"+current.getUserName()+"_"+format.format(now)+".xls";
				gyExcelService.generateExcel(businessKey.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		List<RoleVo> roleVos = userDao.searchRoleByUserId(userId);
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(supplierInquiry.getClientInquiryId());
		SystemCode biz = systemCodeDao.selectByPrimaryKey(clientInquiry.getBizTypeId());
		//List<SupplierContact> supplierContacts = supplierContactDao.getEmails(supplierInquiry.getSupplierId());
		for (int j = 0; j < supplierContacts.size(); j++) {
			StringBuffer text = new StringBuffer();
			if (supplierContacts.get(j).getEmail()!=null && !"".equals(supplierContacts.get(j).getEmail())) {
				List<String> to = new ArrayList<String>();
				to.add(supplierContacts.get(j).getEmail());
				if (roleVos.get(0).getRoleName().equals("国内采购") && elementList.size()>0) {
					text.append("<div>").append(supplierContacts.get(j).getSurName()).append(supplierContacts.get(j).getAppellation()).append(",您好！").append("</div>");
					text.append("<div>附件为").append(biz.getValue()).append(supplierInquiry.getQuoteNumber()).append(",</div>");
					if (!"".equals(firstText) && firstText != null) {
						text.append("<div>").append(firstText).append(",").append("</div>");
					}
					text.append("<div>").append("请尽快报价，谢谢！").append("</div>");
					if (!"".equals(secondText) && secondText != null) {
						text.append("<div>").append(secondText).append("</div>");
					}
					text.append("</div>");
					text.append("<div>&nbsp;</div>");
					text.append("<div>-----------------------------------------------<div>");
					text.append("<div><b>").append(current.getFullName()).append("</b></div>");
					text.append("<div></div>");
					text.append("<div><b>广州航润贸易有限公司</b></div>");
					text.append("<div>地址：广东省广州市越秀区竹丝岗二马路39号之一中航大厦4楼  邮编：510080</div>");
					text.append("<div>手机：").append(current.getMobile()).append(" 固定电话：").append(current.getPhone()).append(" 传真：").append(current.getFax()).append("</div>");
					text.append("<div>E-mail：").append(current.getEmail()).append("</div>");
					path = realPath;
				}else if (roleVos.get(0).getRoleName().equals("国外采购")){
					
					if (elementList.size()<=20 && elementList.size()>0) {
						String name = supplierContacts.get(j).getName();
						text.append("<div>Dear ");
						text.append(name);
						text.append("</div>");
						text.append("<div>&nbsp;</div>");
						//表头
						text.append("<div>Would you please kindly check that if you could quote below part number to us? </div>");
						text.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
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
								+"Remark"
								+ "</td></tr>"
								);
						for (int k = 0; k < elementList.size(); k++) {
							text.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
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
						text.append("</table></div>");
						SystemCode systemCode = systemCodeDao.findById(clientInquiry.getAirTypeId());
						if (systemCode.getCode().equals("81")) {
							text.append("<div> </div>");
							text.append("<div>For OH parts, please kindly provide below information and document along with the quotation.</div>");
							text.append("<div>1>	TSN/CSN of the parts.</div>");
							text.append("<div>2>	ARC and shop finding report.</div>");
							text.append("<div>3>	Last operator.</div>");
							text.append("<div>4>	Full traceability with NIS.</div>");
						}else if (systemCode.getCode().equals("44")) {
							text.append("<div> </div>");
							text.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
											+ "And please kindly inform if there is any hazmat fee or a special box needed to ship the parts? "
											+ "If yes, please kindly also quote to us. And for the special container, "
											+ "please kindly quote both outright price and rental price to us. "
											+ "We will decide to buy or rent the box according to the price. </div>");
						}else if (systemCode.getCode().equals("43")) {
							text.append("<div> </div>");
							text.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
											+ "Please kindly inform if there is any hazmat fee to ship the parts? "
											+ "If yes, please kindly also quote to us.</div>");
						}
						text.append("<div>Thank you and look forward to your reply.</div>");
						text.append("<div>&nbsp;</div>");
						text.append("<div>Best Regards</div>");
						text.append("<div>").append(current.getFullName()).append("</div>");
						text.append("<div>Purchasing Specialist</div>");
						text.append("<div>Betterair Trade Co., Ltd</div>");
						text.append("<div>Email : ").append(current.getEmail()).append("</div>");
						text.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
						text.append("<div>Tel.:  ").append(current.getPhone()).append("</div>");
					}else {
						text.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
						path = realPath;
					}
				}
				List<String> user = new ArrayList<String>();
				ExchangeMail exchangeMail = new ExchangeMail();
				exchangeMail.setUsername(current.getEmail());
				exchangeMail.setPassword(current.getEmailPassword());
				exchangeMail.init();
				List<String> bcc = new ArrayList<String>();
				bcc.add(current.getEmail());
				if (userVo.getLoginName().equals("Alteon")) {
					PageData data = new PageData();
					data.put("loginName", "Leelu");
					List<UserVo> manage = userDao.searchUserByLoginName(data);
					if (manage.size() > 0) {
						user.add(manage.get(0).getEmail());
					}
					
				}
				String title = "RFQ"+supplierInquiry.getQuoteNumber();
				try {
					if (roleVos.get(0).getRoleName().indexOf("采购") > 0 && elementList.size()>0) {
						if (text.length() > 0 || !"".equals(path)) {
							exchangeMail.doSend(title.toString(), to, user, bcc, text.toString(), path);
							supplierInquiry.setEmailStatus(1);
							supplierInquiryDao.updateByPrimaryKeySelective(supplierInquiry);
						}
					}
					
					/*if (to.size() > 0) {
						supplierInquiry.setEmailStatus(1);
						supplierInquiryDao.updateByPrimaryKeySelective(supplierInquiry);
					}*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	
	public void sendEmail(SupplierInquiry supplierInquiry,String userId,String firstText,String secondText) {
		List<SupplierContact> supplierContacts = supplierContactDao.findBySupplierId(supplierInquiry.getSupplierId());
		List<ClientInquiryElement> elementList = new ArrayList<ClientInquiryElement>();
		elementList = clientInquiryElementDao.selectBySupplierInquiryId(supplierInquiry.getId());
		UserVo current = userDao.findUserByUserId(userId.toString());
		String businessKey = "supplier_inquiry.id."+supplierInquiry.getId()+"-"+0+".SupplierInquieyExcel";
		String realPath = "";
		String path = "";
		if (elementList.size()>0) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
				Date now = new Date();
				realPath = "D:/CRM/Files/mis/excel/sampleoutput/"+supplierInquiry.getQuoteNumber()+"_"+"SupplierInquiry"+"_"+current.getUserName()+"_"+format.format(now)+".xls";
				gyExcelService.generateExcel(businessKey.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		List<RoleVo> roleVos = userDao.searchRoleByUserId(userId);
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(supplierInquiry.getClientInquiryId());
		SystemCode biz = systemCodeDao.selectByPrimaryKey(clientInquiry.getBizTypeId());
		//List<SupplierContact> supplierContacts = supplierContactDao.getEmails(supplierInquiry.getSupplierId());
		for (int j = 0; j < supplierContacts.size(); j++) {
			StringBuffer text = new StringBuffer();
			if (supplierContacts.get(j).getEmail()!=null && !"".equals(supplierContacts.get(j).getEmail())) {
				List<String> to = new ArrayList<String>();
				to.add(supplierContacts.get(j).getEmail());
				if (roleVos.get(0).getRoleName().equals("国内采购") && elementList.size()>0) {
					text.append("<div>").append(supplierContacts.get(j).getSurName()).append(supplierContacts.get(j).getAppellation()).append(",您好！").append("</div>");
					text.append("<div>附件为").append(biz.getValue()).append(supplierInquiry.getQuoteNumber()).append(",</div>");
					if (!"".equals(firstText) && firstText != null) {
						text.append("<div>").append(firstText).append(",").append("</div>");
					}
					text.append("<div>").append("请尽快报价，谢谢！").append("</div>");
					if (!"".equals(secondText) && secondText != null) {
						text.append("<div>").append(secondText).append("</div>");
					}
					text.append("</div>");
					text.append("<div>&nbsp;</div>");
					text.append("<div>-----------------------------------------------<div>");
					text.append("<div><b>").append(current.getFullName()).append("</b></div>");
					text.append("<div></div>");
					text.append("<div><b>广州航润贸易有限公司</b></div>");
					text.append("<div>地址：广东省广州市越秀区竹丝岗二马路39号之一中航大厦4楼  邮编：510080</div>");
					text.append("<div>手机：").append(current.getMobile()).append(" 固定电话：").append(current.getPhone()).append(" 传真：").append(current.getFax()).append("</div>");
					text.append("<div>E-mail：").append(current.getEmail()).append("</div>");
					path = realPath;
				}else if (roleVos.get(0).getRoleName().equals("国外采购")){
					
					if (elementList.size()<=20 && elementList.size()>0) {
						String name = supplierContacts.get(j).getName();
						text.append("<div>Dear ");
						text.append(name);
						text.append("</div>");
						text.append("<div>&nbsp;</div>");
						//表头
						text.append("<div>Would you please kindly check that if you could quote below part number to us? </div>");
						text.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
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
								+"Remark"
								+ "</td></tr>"
								);
						for (int k = 0; k < elementList.size(); k++) {
							text.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
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
						text.append("</table></div>");
						SystemCode systemCode = systemCodeDao.findById(clientInquiry.getAirTypeId());
						if (systemCode.getCode().equals("81")) {
							text.append("<div> </div>");
							text.append("<div>For OH parts, please kindly provide below information and document along with the quotation.</div>");
							text.append("<div>1>	TSN/CSN of the parts.</div>");
							text.append("<div>2>	ARC and shop finding report.</div>");
							text.append("<div>3>	Last operator.</div>");
							text.append("<div>4>	Full traceability with NIS.</div>");
						}else if (systemCode.getCode().equals("44")) {
							text.append("<div> </div>");
							text.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
											+ "And please kindly inform if there is any hazmat fee or a special box needed to ship the parts? "
											+ "If yes, please kindly also quote to us. And for the special container, "
											+ "please kindly quote both outright price and rental price to us. "
											+ "We will decide to buy or rent the box according to the price. </div>");
						}else if (systemCode.getCode().equals("43")) {
							text.append("<div> </div>");
							text.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
											+ "Please kindly inform if there is any hazmat fee to ship the parts? "
											+ "If yes, please kindly also quote to us.</div>");
						}
						text.append("<div>Thank you and look forward to your reply.</div>");
						text.append("<div>&nbsp;</div>");
						text.append("<div>Best Regards</div>");
						text.append("<div>").append(current.getFullName()).append("</div>");
						text.append("<div>Purchasing Specialist</div>");
						text.append("<div>Betterair Trade Co., Ltd</div>");
						text.append("<div>Email : ").append(current.getEmail()).append("</div>");
						text.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
						text.append("<div>Tel.:  ").append(current.getPhone()).append("</div>");
					}else {
						text.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
						path = realPath;
					}
				}
				List<String> user = new ArrayList<String>();
				ExchangeMail exchangeMail = new ExchangeMail();
				exchangeMail.setUsername(current.getEmail());
				exchangeMail.setPassword(current.getEmailPassword());
				exchangeMail.init();
				List<String> bcc = new ArrayList<String>();
				bcc.add(current.getEmail());
				String title = "RFQ"+supplierInquiry.getQuoteNumber();
				try {
					if (roleVos.get(0).getRoleName().indexOf("采购") > 0 && elementList.size()>0) {
						if (text.length() > 0 || !"".equals(path)) {
							exchangeMail.doSend(title.toString(), to, user, bcc, text.toString(), path);
							supplierInquiry.setEmailStatus(1);
							supplierInquiryDao.updateByPrimaryKeySelective(supplierInquiry);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	
	public List<SupplierContact> getContacts(String[] suppliers) {
		List<SupplierContact> list = new ArrayList<SupplierContact>();
		for (int i = 0; i < suppliers.length; i++) {
			Supplier supplier = supplierDao.selectByPrimaryKey(new Integer(suppliers[i]));
			List<SupplierContact> contacts = supplierContactDao.findBySupplierId(supplier.getId());
			for (int j = 0; j < contacts.size(); j++) {
				contacts.get(j).setSupplierCode(supplier.getCode());
				list.add(contacts.get(j));
			}
		}
		return list;
	}
	
	public List<ManageElementVo> ManageElementWhenAuto(Integer supplierInquiryId){
		return supplierInquiryDao.ManageElementWhenAuto(supplierInquiryId);
	}
	
	/**
     * 生成询价单并发送邮件
     * @param id
     * @param userVo
     * @param elementList
     */
    public boolean sendEmail(String elementIds,Integer id){
    	ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(id);
    	boolean success = true;
		try {
			List<Integer> storeSupplier = supplierCommissionSaleDao.getSupplierIdByInquiryId(id);
    		//根据类型代码获取供应商列表
    		List<SupplierAirRelationKey> supplierList = supplierAirRelationDao.getSupplierByIds(elementIds);
    		if (supplierList.size()>0) {
				SupplierInquiry supplierInquiry = findClientInquiryElement(id);
				for (int i = 0; i < supplierList.size(); i++) {
					List<ClientInquiryElement> elementList = clientInquiryElementDao.getListBySelect(elementIds, supplierList.get(i).getSupplierId().toString());
					for (int j = 0; j < storeSupplier.size(); j++) {
						if (null!=storeSupplier.get(j)&&storeSupplier.get(j).equals(supplierList.get(i).getSupplierId())) {
							storeSupplier.remove(j);
						}
					}
					for (int j = 0; j < elementList.size(); j++) {
						int count = supplierQuoteElementDao.getByPartInTwoWeeks(elementList.get(j).getPartNumber(), supplierList.get(i).getSupplierId().toString());
						if (count > 0) {
							elementList.remove(j);
							j=j-1;
						}
					}
					if (elementList.size() > 0) {
						String quoteNumber = getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierList.get(i).getSupplierId());
    					supplierInquiry.setQuoteNumber(quoteNumber);
    					
    					supplierInquiry.setUpdateTimestamp(new Date());
    					supplierInquiry.setSupplierId(supplierList.get(i).getSupplierId());
    					supplierInquiry.setAutoAdd(1);
    					insertSelective(supplierInquiry);
    					for (int j = 0; j < elementList.size(); j++) {
    						SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
    						supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
    						supplierInquiryElement.setClientInquiryElementId(elementList.get(j).getId());
    						supplierInquiryElement.setUpdateTimestamp(new Date());
    						ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(elementList.get(j).getId());
    						supplierInquiryElementService.insertSelective(supplierInquiryElement);
    						if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
    							if (clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700) {
    								clientInquiryElement.setElementStatusId(701);
    								clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
    							}
    						}
    						
    					}
    					if (elementList.size()>20) {
    						StringBuffer businessKey = new StringBuffer();
    						businessKey.append("supplier_air_relation.id.").append(supplierInquiry.getId()).append(".supplierAirRelationExcel");
    						try {
    							gyExcelService.generateExcel(businessKey.toString());
    						} catch (Exception e1) {
    							e1.printStackTrace();
    							success = false;
    						} 
    					}
    					
	    				//发送邮件
	    				ExchangeMail exchangeMail = new ExchangeMail();
	    				List<Integer> users = authorityRelationDao.getUserId(supplierList.get(i).getSupplierId());
						UserVo userVo2 = userDao.findUserByUserId(users.get(0).toString());
//						exchangeMail.setUsername("purchaser@betterairgroup.com");
//						exchangeMail.setPassword("GZBAcom@)!&0817");
						exchangeMail.setUsername("liana@betterairgroup.com");
						exchangeMail.setPassword("TestZAQ!2wsx");
	    				String userEmail = "";
	    				String fullName = userVo2.getFullName();
	    				String tel = userVo2.getPhone();
	    				List<SupplierContact> supplierContacts = supplierContactDao.getEmailPerson(supplierList.get(i).getSupplierId());
	    				if (supplierContacts.size() == 0) {
	    					supplierContacts = supplierContactDao.getEmails(supplierList.get(i).getSupplierId());
						}
	    				EmailTemplateVo[] templateVos = clientInquiryService.getEmailTemplates();
	    				int index = (int) (Math.random() * templateVos.length);
	    				EmailTemplateVo emailTemplateVo = templateVos[index];
	    					for (int j = 0; j < supplierContacts.size(); j++) {
	    						if (supplierContacts.get(j).getEmail()!=null && !"".equals(supplierContacts.get(j).getEmail())) {
	    							List<String> to = new ArrayList<String>();
	    							if (supplierContacts.get(j).getEmail() != null && !"".equals(supplierContacts.get(j).getEmail())) {
	    								to.add(supplierContacts.get(j).getEmail().trim());
	    							}
	    							List<String> cc = new ArrayList<String>();
	    							List<String> bcc = new ArrayList<String>();
	    							bcc.add(exchangeMail.getUsername());
	    							StringBuffer bodyText = new StringBuffer();
	    							String name = supplierContacts.get(j).getName();
	    							bodyText.append("<div>Dear ");
	    							bodyText.append(name);
	    							bodyText.append("</div>");
	    							bodyText.append("<div>&nbsp;</div>");
	    							String realPath = "";
	    							if (elementList.size()<=20) {
	    								//表头
	    								if (emailTemplateVo.getHeader() != null && !"".equals(emailTemplateVo.getHeader())) {
											bodyText.append(emailTemplateVo.getHeader());
										}
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
	    									String remark = "";
	    									if (elementList.get(k).getRemark() != null) {
	    										remark = elementList.get(k).getRemark();
											}
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
	    													+ remark
	    													+"</td></tr>"
	    													);
	    								}
	    								bodyText.append("</table></div>");
	    								if (emailTemplateVo.getBottom() != null && !"".equals(emailTemplateVo.getBottom())) {
											bodyText.append(emailTemplateVo.getBottom());
										}
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
	    								EmailMessage emailMessage = new EmailMessage(supplierInquiry.getId(), to.toString(), cc.toString(), bcc.toString(),new Date());
	    								emailMessageDao.insertSelective(emailMessage);
	    								supplierInquiry.setEmailStatus(1);
	    								supplierInquiryDao.updateByPrimaryKeySelective(supplierInquiry);
	    							} catch (Exception e) {
	    								success = false;
	    								e.printStackTrace();
	    							}
    						}
    						
    					}
					}
					
				}
			}
    		return success;
		} catch (Exception e) {
			e.printStackTrace();
		}/*finally{
			clientInquiry.setEmailStatus(1);
			clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
		}*/
    	return false;
    }
    
    public void getPrepareEmailList(PageModel<PartAndEmail> page,String where,GridSort sort){
    	if (where != null && !"".equals(where)) {
    		page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(partAndEmailDao.getPrepareEmailPage(page));
    }
    
    public void cancelRecord(PartAndEmail partAndEmail){
    	partAndEmailDao.cancelRecord(partAndEmail);
    }
}
