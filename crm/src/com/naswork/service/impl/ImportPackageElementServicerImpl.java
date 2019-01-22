package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.fabric.xmlrpc.base.Array;
import com.naswork.dao.AuthorityRelationDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.ExportPackageDao;
import com.naswork.dao.ExportPackageElementDao;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementPrepareDao;
import com.naswork.dao.ImportStorageLocationListDao;
import com.naswork.dao.IncomeDao;
import com.naswork.dao.IncomeDetailDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.OnPassageStorageDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.StorageCorrelationDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierImportElementDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ExportPackage;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.IncomeDetail;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.model.StorageCorrelation;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierImportElement;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.importpackage.SupplierImportElementVo;
import com.naswork.module.purchase.controller.importpackage.SupplierOrderElementVo;
import com.naswork.module.purchase.controller.importpackage.SupplierVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.ProcessKeys;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.FlowService;
import com.naswork.service.ImportPackagePaymentElementPrepareService;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.TPartService;
import com.naswork.utils.Compress;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
@Service("importPackElemnetService")
public class ImportPackageElementServicerImpl extends FlowServiceImpl implements ImportpackageElementService  {
@Resource
private ImportPackageElementDao importPackageElementDao;
@Resource
private ClientInquiryDao clientInquiryDao;
@Resource
private ClientInquiryElementDao clientInquiryElementDao;
@Resource
private SupplierInquiryDao supplierInquiryDao;
@Resource
private SupplierInquiryElementDao supplierInquiryElementDao;
@Resource
private SupplierQuoteDao supplierQuoteDao;
@Resource
private SupplierQuoteElementDao supplierQuoteElementDao;
@Resource
private ClientQuoteDao clientQuoteDao;
@Resource
private ClientQuoteElementDao clientQuoteElementDao;
@Resource
private ClientOrderDao clientOrderDao;
@Resource
private ClientOrderElementDao clientOrderElementDao;
@Resource
private SupplierOrderDao supplierOrderDao;
@Resource
private SupplierOrderElementDao supplierOrderElementDao;
@Resource
private SupplierImportElementDao supplierImportElementDao;
@Resource
private ExportPackageDao exportPackageDao;
@Resource
private ExchangeRateDao exchangeRateDao;
@Resource
private ImportPackageDao importPackageDao;
@Resource
private ExportPackageElementDao exportPackageElementDao;
@Resource
private UserDao userDao;
@Resource
private SupplierDao supplierDao;
@Resource
private ImportPackagePaymentDao importPackagePaymentDao;
@Resource
private ImportPackagePaymentElementService importPackagePaymentElementService;
@Resource
private OnPassageStorageDao onPassageStorageDao;
@Resource
private AuthorityRelationDao authorityRelationDao;
@Resource
private SupplierOrderService supplierOrderService;
@Resource
private Jbpm4JbyjDao jbyjDao;
@Resource
private Jbpm4TaskDao jbpm4TaskDao;
@Resource
private FlowService flowService;
@Resource
private ImportPackagePaymentElementPrepareService importPackagePaymentElementPrepareService;
@Resource
private IncomeDetailDao incomeDetailDao;
@Resource
private ExportPackageElementService exportPackageElementService;
@Resource
private ImportStorageLocationListDao importStorageLocationListDao;
@Resource
private OrderApprovalDao orderApprovalDao;
@Resource
private ClientInquiryService clientInquiryService;
@Resource
private TPartDao tPartDao;
@Resource
private TPartService tPartService;
@Resource
private StorageCorrelationDao storageCorrelationDao;


	@Override
	public void findElementListDatePage(PageModel<ImportPackageElementVo> page, String searchString, GridSort sort) {
		if(null!=searchString&&!searchString.equals("")){
			page.put("where", searchString);
		}
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		List<ImportPackageElementVo> importPackageElementList=importPackageElementDao.findElementListDatePage(page);
		page.setEntities(importPackageElementList);
		
	}

	@Override
	public void findsupplierorderDatePage(PageModel<SupplierOrderElementVo> page, String searchString, GridSort sort) {
		if(null!=searchString&&!"".equals(searchString)){
		page.put("where", searchString);
		}
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<SupplierOrderElementVo> supplierOrderElementList=importPackageElementDao.findsupplierorderDatePage(page);
		page.setEntities(supplierOrderElementList);
	}

	@Override
	public List<SupplierOrderElementVo> findsupplierorderDate(Integer supplierOrderElementId) {
		return importPackageElementDao.findsupplierorderDate(supplierOrderElementId);
	}

	@Override
	public void insert(ImportPackageElement record) {
		record.setRestLifeEmail(0);
		importPackageElementDao.insert(record);
		SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(record.getSupplierOrderElementId());
		if(null!=supplierOrderElement1){
			ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
			ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
			IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientOrderElement1.getId());
			ClientOrder clientOrder1 = clientOrderDao.selectByPrimaryKey(clientOrderElement1.getClientOrderId());
			/*if (!"".equals(clientOrderElement1.getElementStatusId()) && clientOrderElement1.getElementStatusId()!=null) {
				if (clientOrderElement1.getElementStatusId().equals(707) || clientOrderElement1.getElementStatusId()==707 ||
						clientOrderElement1.getElementStatusId().equals(705) || clientOrderElement1.getElementStatusId()==705){
					if (clientOrder1.getPrepayRate() > 0 && incomeDetail == null) {
						clientOrderElement1.setElementStatusId(709);
					}else {
						clientOrderElement1.setElementStatusId(708);
					}
					clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement1);
				}
			}*/
			ImportPackage importPackage = importPackageDao.selectByPrimaryKey(record.getImportPackageId());
			if (importPackage.getImportStatus() != null && importPackage.getImportStatus().equals(2)) {
				supplierOrderElement1.setElementStatusId(707);
				supplierOrderElementDao.updateByPrimaryKeySelective(supplierOrderElement1);
			}
		}
	}
	
    public void sendAlterstorageaEmail(List<EmailVo> emailVos,String  userId){
    	if(emailVos.size()>0){
        	//发送邮件
    		//for (int i = 0; i < supplierList.size(); i++) {
    		ExchangeMail exchangeMail = new ExchangeMail();
    		UserVo userVo2 = userDao.findUserByUserId(userId);
    		exchangeMail.setUsername(userVo2.getEmail());
    		exchangeMail.setPassword(userVo2.getEmailPassword());
    		List<UserVo> roleName=userDao.searchEmailByRoleName();
//    		List<UserVo> roleName=userDao.findEmailByorderNumber(emailVos.get(0).getSupplierOrderNumber());
    		
    		for (int j = 0; j < roleName.size(); j++) {
    				List<String> to = new ArrayList<String>();
    				to.add(roleName.get(j).getEmail());
    				List<String> cc = new ArrayList<String>();
    				List<String> bcc = new ArrayList<String>();
    				bcc.add(userVo2.getEmail());
    				StringBuffer bodyText = new StringBuffer();
    				String name = roleName.get(j).getUserName();
    				bodyText.append("<div>Dear ");
    				bodyText.append(name);
    				bodyText.append("</div>");
    				bodyText.append("<div>&nbsp;</div>");
    				String realPath = "";
    				if (true) {
    					//表头
    					bodyText.append("<div>&nbsp;&nbsp;现有客户订单"+emailVos.get(0).getClientOrderNumber()+"中如下项目修改订单数量，请按照最新订单数量修改原库存转订单数量 </div>");
    					bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
    							+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+ "Part No."
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"库存转订单入库单号"
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"原订单数量"
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"最新订单数量"
    							+ "</td></tr>"
    							);
    					for (int k = 0; k < emailVos.size(); k++) {
    						bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getPartNumber()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getNowImportpackNumber()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getOldAmount()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getNowAmount()
    										+"</td></tr>"
    										);
    					}
    					bodyText.append("</table></div>");
    					
    				}/*else {
    					bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
    					realPath = "D:/CRM/Files/mis/email/sampleoutput/"+clientInquiry.getQuoteNumber()+".xls";
    				}*/
    				
    				exchangeMail.init();
    				try {
    					exchangeMail.doSend("SYM客户订单数量更新", to, cc, bcc, bodyText.toString(), realPath);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    		}
        	}
    }
    
    public void sendReturnGoodsEmail(List<EmailVo> emailVos,String  userId){
    	if(emailVos.size()>0){
        	//发送邮件
    		//for (int i = 0; i < supplierList.size(); i++) {
    		ExchangeMail exchangeMail = new ExchangeMail();
    		UserVo userVo2 = userDao.findUserByUserId(userId);
    		exchangeMail.setUsername(userVo2.getEmail());
    		exchangeMail.setPassword(userVo2.getEmailPassword());
//    		List<UserVo> roleName=userDao.searchEmailByRoleName();
    		List<UserVo> roleName=userDao.findEmailByorderNumber(emailVos.get(0).getSupplierOrderNumber());
    		
    		for (int j = 0; j < roleName.size(); j++) {
    				List<String> to = new ArrayList<String>();
    				to.add(roleName.get(j).getEmail());
    				List<String> cc = new ArrayList<String>();
    				List<String> bcc = new ArrayList<String>();
    				bcc.add(userVo2.getEmail());
    				StringBuffer bodyText = new StringBuffer();
    				String name = roleName.get(j).getUserName();
    				bodyText.append("<div>Dear ");
    				bodyText.append(name);
    				bodyText.append("</div>");
    				bodyText.append("<div>&nbsp;</div>");
    				String realPath = "";
    				if (true) {
    					//表头
    					bodyText.append("<div>&nbsp;&nbsp;现有客户订单"+emailVos.get(0).getClientOrderNumber()+",供应商订单"+emailVos.get(0).getSupplierOrderNumber()+"中如下项目退货，请重新订购 </div>");
    					bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
    							+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+ "Part No."
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"Description"
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"客户订单号"
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"供应商订单号"
    							+ "</td></tr>"
    							);
    					for (int k = 0; k < emailVos.size(); k++) {
    						bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getPartNumber()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getDescription()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getClientOrderNumber()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getSupplierOrderNumber()
    										+"</td></tr>"
    										);
    					}
    					bodyText.append("</table></div>");
    					
    				}/*else {
    					bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
    					realPath = "D:/CRM/Files/mis/email/sampleoutput/"+clientInquiry.getQuoteNumber()+".xls";
    				}*/
    				
    				exchangeMail.init();
    				try {
    					exchangeMail.doSend("SYM退货", to, cc, bcc, bodyText.toString(), realPath);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    		}
        	}
    }

	@Override
	public ClientInquiry lookBySourceNumber()  {
		String storageSourceNumber = inquiryForGetStorageSourceNumber(Calendar
				.getInstance().get(Calendar.YEAR));
		ClientInquiry clientInquiry=importPackageElementDao.findBySourceNumber(storageSourceNumber);
		if(null==clientInquiry){
			clientInquiry=new ClientInquiry();
			clientInquiry.setClientId( new Integer(16));
			clientInquiry.setClientContactId(new Integer(8));
			clientInquiry.setBizTypeId( new Integer(120));
			clientInquiry.setAirTypeId( new Integer(123));
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
			clientInquiry.setInquiryDate( calendar.getTime());
			calendar.set(calendar.get(Calendar.YEAR), 11, 31, 0, 0, 0);
			clientInquiry.setDeadline( calendar.getTime());
			clientInquiry.setSourceNumber(storageSourceNumber);
			clientInquiry.setQuoteNumber(storageSourceNumber);
			int maxQuoteNumberSeq = importPackageElementDao.findMAXquoteNumberSeq(clientInquiry.getInquiryDate());
			Integer nextSeq = new Integer(maxQuoteNumberSeq + 1);
			clientInquiry.setQuoteNumberSeq(new Integer(nextSeq));
			clientInquiry.setTerms("");
			clientInquiry.setInquiryStatusId(35);
			clientInquiry.setRemark("STORAGE");
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryDao.insert(clientInquiry);
		}
		return  clientInquiry;
	}
//	
//	public ClientInquiry createStorage() throws SQLException {
//	
////		return clientInquiry;
//	}
	
	private String inquiryForGetStorageSourceNumber(int year) {
		return "KC-" + year;
	}

	private String orderForGetStorageSourceNumber(int year) {
		return "ORD-KC-" + year;
	}
	
	private String getSupplierOrderNumber(String orderNumber,
			String supplierCode) {
		return orderNumber + "-" + supplierCode;
	}
	
	@Override
	public int findMAXid() {
		return importPackageElementDao.findMAXid();
	}

	@Override
	public ImportPackageElementVo findimportpackageelement(String importpackageelementid) {
		return importPackageElementDao.findimportpackageelement(importpackageelementid);
	}

	@Override
	public SupplierImportElementVo findSupplierimportElement(Integer importpackageelementid) {
		return importPackageElementDao.findSupplierimportElement(importpackageelementid);
	}

	@Override
	public void updateByPrimaryKey(ImportPackageElement record) {
		 importPackageElementDao.updateByPrimaryKey(record);;
	}
	
	@Override
	public void updateByPrimaryKeySelective(ImportPackageElement record) {
		 importPackageElementDao.updateByPrimaryKeySelective(record);;
	}

	@Override
	public List<ImportPackageElementVo> findByIpid(Integer ipid) {
		return importPackageElementDao.findByIpid(ipid);
	}
		
	public void StoragePage(PageModel<StorageDetailVo> page,String where,GridSort sort){
		if(where!=null && !"".equals(where)){
			page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(importPackageElementDao.StoragePage(page));
	}
	
	public void getNotInInstructionsPage(PageModel<StorageDetailVo> page,GridSort sort){
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(importPackageElementDao.getNotInInstructionsPage(page));
	}
	
	@Override
	public void findstorageFlowPage(PageModel<StorageFlowVo> page, String searchString, GridSort sort) {
		String buffer = null;
		if(null!=page.getString("partNumber")&&!"".equals(page.getString("partNumber"))){
			if("eq".equals(page.getString("check"))){
				String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
				 buffer =" part_number_code = "+"'"+partNumberCode+"'";
			}else{
			String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
			 buffer =" part_number_code LIKE "+"'%"+partNumberCode+"%'";
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
		List<StorageFlowVo> flowVos=importPackageElementDao.findstorageFlowPage(page);
		page.setEntities(flowVos);
		
		
	}

	@Override
	public StorageFlowVo findtotalList(PageModel<StorageFlowVo> page, String searchString) {
		String buffer = null;
		if(null!=page.getString("partNumber")&&!"".equals(page.getString("partNumber"))){
			if("eq".equals(page.getString("check"))){
				String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
				 buffer =" part_number_code = "+"'"+partNumberCode+"'";
			}else{
			String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
			 buffer =" part_number_code LIKE "+"'%"+partNumberCode+"%'";
			}
			 if(null!=searchString&&!"".equals(searchString)){
				 searchString=buffer+" and "+searchString;
			 }else{
				 searchString=buffer;
			 }
			
		}
		page.put("where", searchString);
		return importPackageElementDao.findtotalList(page);
	}
	
	@Override
	public List<ImportPackageElementVo> findByLocationIm( String locatin) {
		return importPackageElementDao.findByLocationIm( locatin);
	}

	@Override
	public List<ImportPackageElementVo> findByClientIdAndOrdernum(String clientId, String orderNumber) {
		return importPackageElementDao.findByClientIdAndOrdernum(clientId, orderNumber);
	}

	@Override
	public List<ImportPackageElementVo> findByLocationAndStatus( String locatin) {
		return importPackageElementDao.findByLocationAndStatus( locatin);
	}

	@Override
	public int findexportpackage(Integer importpackageelementid) {
		return importPackageElementDao.findexportpackage(importpackageelementid);
	}

	@Override
	public List<ImportPackageElementVo> findByLocationEx(String locatin) {
		return importPackageElementDao.findByLocationEx(locatin);
	}

	@Override
	public List<ImportPackageElementVo> findByClientId(String clientId) {
		return importPackageElementDao.findByClientId(clientId);
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
	
	public static boolean isValidPartNumber(String partNumber) {
		boolean flag = false;
		;
		if (StringUtils.isEmpty(partNumber)) {
			flag = false;
		} else {
			for (int i = 0; i < partNumber.length(); i++) {
				if (!isValidCharacter(partNumber.charAt(i))) {
					flag = false;
					break;
				} else {
					flag = true;
				}
			}
		}
		return flag;
	}

	public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
	}
	
	/**
	 * 生成入库单号
	 * **/
	public String generateImportNumber(ImportPackage importPackage) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(outputDate((Date) importPackage.getImportDate(),
				"yyyyMMdd"));
		List<SupplierVo> supplier=importPackageDao.findsupplier(importPackage.getSupplierId());
		for (SupplierVo supplierVo : supplier) {
		buffer.append(supplierVo.getCode());
		buffer.append(StringUtils.leftPad(importPackage.getSeq()
				.toString(), 3, '0'));}
		return buffer.toString();
	}
	
	public static String outputDate(Date value, String pattern) {
		return value == null ? "" : FastDateFormat.getInstance(pattern).format(
				value);
	}
	
	/**
	 * 生成出库单号
	 * **/
	public String generateExportNumber(ExportPackage exportPackage) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(outputDate((Date) exportPackage.getExportDate(),
				"yyyyMMdd"));
		List<ClientContact> client=importPackageDao.findclient(exportPackage.getClientId());
		for (ClientContact contact : client) {
		buffer.append(contact.getCode());
		buffer.append(StringUtils.leftPad(exportPackage.getSeq()
				.toString(), 3, '0'));}
		return buffer.toString();
	}

	@Override
	public void Storage(ImportPackageElement importPackageElement,List<ImportPackageListVo> imortpackagedate,SupplierImportElement supplierImportElement)throws Exception {
		importPackageElement.setRestLifeEmail(0);
		importPackageElementDao.insert(importPackageElement);
		if(null!=importPackageElement.getSupplierOrderElementId()){
		SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(importPackageElement.getSupplierOrderElementId());
			ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
			ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
			IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientOrderElement1.getId());
			ClientOrder clientOrder1 = clientOrderDao.selectByPrimaryKey(clientOrderElement1.getClientOrderId());
			if (!"".equals(clientOrderElement1.getElementStatusId()) && clientOrderElement1.getElementStatusId()!=null) {
				if (clientOrderElement1.getElementStatusId().equals(707) || clientOrderElement1.getElementStatusId()==707 ||
						clientOrderElement1.getElementStatusId().equals(705) || clientOrderElement1.getElementStatusId()==705){
					if (clientOrder1.getPrepayRate() > 0 && incomeDetail == null) {
						clientOrderElement1.setElementStatusId(709);
					}else {
						clientOrderElement1.setElementStatusId(708);
					}
					clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement1);
				}
			}
		}
		
		supplierImportElement.setImportPackageElementId(importPackageElement.getId());
		
		Double amount =importPackageElement.getAmount();
		Double price =importPackageElement.getPrice();
		
		ClientInquiryElement clientInquiryElement=new ClientInquiryElement();
		clientInquiryElement.setRemark("STORAGE");
		String storageSourceNumber = inquiryForGetStorageSourceNumber(Calendar
				.getInstance().get(Calendar.YEAR));
		ClientInquiry clientInquiry=importPackageElementDao.findBySourceNumber(storageSourceNumber);
		if(null==clientInquiry){
			clientInquiry=new ClientInquiry();
			clientInquiry.setClientId( new Integer(16));
			clientInquiry.setClientContactId(new Integer(8));
			clientInquiry.setBizTypeId( new Integer(120));
			clientInquiry.setAirTypeId( new Integer(123));
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
			clientInquiry.setInquiryDate( calendar.getTime());
			calendar.set(calendar.get(Calendar.YEAR), 11, 31, 0, 0, 0);
			clientInquiry.setDeadline( calendar.getTime());
			clientInquiry.setSourceNumber(storageSourceNumber);
			clientInquiry.setQuoteNumber(storageSourceNumber);
			int maxQuoteNumberSeq = importPackageElementDao.findMAXquoteNumberSeq(clientInquiry.getInquiryDate());
			Integer nextSeq = new Integer(maxQuoteNumberSeq + 1);
			clientInquiry.setQuoteNumberSeq(new Integer(nextSeq));
			clientInquiry.setTerms("");
			clientInquiry.setInquiryStatusId(35);
			clientInquiry.setRemark("STORAGE");
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryDao.insert(clientInquiry);
		}
		//SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(importPackageElement.getSupplierOrderElementId());
		//ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
		//ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
		//ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
		Integer clientInquiryId =clientInquiry.getId();
		clientInquiryElement.setClientInquiryId(clientInquiryId);
		clientInquiryElement.setElementId(importPackageElement.getElementId());
		clientInquiryElement.setPartNumber(importPackageElement.getPartNumber());
		clientInquiryElement.setDescription(importPackageElement.getDescription());
		clientInquiryElement.setUnit(importPackageElement.getUnit());
		clientInquiryElement.setAmount(importPackageElement.getAmount());
		clientInquiryElement.setUpdateTimestamp(new Date());
		clientInquiryElement.setIsMain(2);
		if(null!=importPackageElement.getSupplierOrderElementId()){
			SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(importPackageElement.getSupplierOrderElementId());
			ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
			ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
			clientInquiryElement.setBsn(clientInquiryElement1.getBsn());
		}
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
		clientInquiryElement.setElementStatusId(710);
		clientInquiryElementDao.insert(clientInquiryElement);
		
		Integer clientInquiryElementId=clientInquiryElement.getId();
		Integer supplierId =imortpackagedate.get(0).getSupplierId();
		String supplierCode =imortpackagedate.get(0).getSupplierCode();
		
		SupplierInquiry supplierinquiry=supplierInquiryDao.findSupplierByImportPackage(clientInquiryId, supplierId);
		if(null==supplierinquiry){
			supplierinquiry=new SupplierInquiry();
			Integer id=clientInquiry.getId();
			supplierinquiry.setClientInquiryId(id);
			supplierinquiry.setSupplierId(supplierId);
			supplierinquiry.setQuoteNumber(clientInquiry.getQuoteNumber()+"-"+supplierCode);
			supplierinquiry.setInquiryDate(clientInquiry.getInquiryDate());
			supplierinquiry.setDeadline(clientInquiry.getDeadline());
			supplierinquiry.setRemark(clientInquiry.getRemark());
			supplierInquiryDao.insertSelective(supplierinquiry);
		}
		
		Integer supplierInquiryId = supplierinquiry.getId();
		SupplierInquiryElement supplierInquiryElement=new SupplierInquiryElement();
		supplierInquiryElement.setSupplierInquiryId(supplierInquiryId);
		supplierInquiryElement.setClientInquiryElementId(clientInquiryElementId);
		supplierInquiryElementDao.insertSelective(supplierInquiryElement);
		
		Integer supplierInquiryElementId =supplierInquiryElement.getId();
		Integer currencyId = imortpackagedate.get(0).getCurrencyId();
		Double exchangeRate =imortpackagedate.get(0).getExchangeRate();
		
		SupplierQuote supplierQuote=supplierQuoteDao.findSupplierInquiry(supplierInquiryId);
		if(null==supplierQuote){
			supplierQuote=new SupplierQuote();
			supplierQuote.setCurrencyId(currencyId);
			supplierQuote.setExchangeRate(exchangeRate);
			supplierQuote.setSupplierInquiryId(supplierInquiryId);
			supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
			supplierQuote.setQuoteNumber(supplierinquiry.getQuoteNumber());
			supplierQuoteDao.insertSelective(supplierQuote);
		}
		Integer supplierQuoteId =supplierQuote.getId();
		
		SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
		supplierQuoteElement.setSupplierQuoteId(supplierQuoteId);
		supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
		supplierQuoteElement.setElementId(importPackageElement.getElementId());
		supplierQuoteElement.setPartNumber(importPackageElement.getPartNumber());
		supplierQuoteElement.setDescription(importPackageElement.getDescription());
		supplierQuoteElement.setAmount(importPackageElement.getAmount());
		supplierQuoteElement.setUnit(importPackageElement.getUnit());
		supplierQuoteElement.setPrice(price);
		supplierQuoteElement.setLeadTime("");
		supplierQuoteElement.setRemark(importPackageElement.getRemark());
		supplierQuoteElement.setConditionId(importPackageElement.getConditionId());
		supplierQuoteElement.setCertificationId(importPackageElement.getCertificationId());
		supplierQuoteElement.setSupplierQuoteStatusId(70);
		supplierQuoteElementDao.insert(supplierQuoteElement);
		
//		if(null!=importPackageElement.getSupplierOrderElementId()){
//			List<OrderApproval> approvals=orderApprovalDao.OnpassStorageAlterStorage(importPackageElement.getSupplierOrderElementId());
//			for (OrderApproval orderApproval : approvals) {
//				orderApproval.setImportPackageElementId(importPackageElement.getId());
//				orderApproval.setSupplierQuoteElementId(supplierQuoteElement.getId());
//				orderApproval.setSupplierOrderElementId(null);
//				orderApproval.setType(0);
//				orderApprovalDao.updateByPrimaryKey(orderApproval);
//			}
//		}
		
		Integer supplierQuoteElementId =supplierQuoteElement.getId();
		importPackageElement.setSupplierQuoteElementId(supplierQuoteElementId);
		ClientQuote clientQuote=clientQuoteDao.findclientquote(clientInquiry.getId());
		if(null==clientQuote){
			clientQuote=new ClientQuote();
			clientQuote.setClientInquiryId(clientInquiry.getId());
			clientQuote.setQuoteDate(Calendar.getInstance().getTime());
			clientQuote.setCurrencyId(currencyId);
			clientQuote.setExchangeRate(exchangeRate);
			clientQuote.setQuoteNumber(clientInquiry.getQuoteNumber());
			clientQuote.setSeq(1);
			clientQuote.setProfitMargin(BigDecimal.ONE.doubleValue());
			clientQuoteDao.insertSelective(clientQuote);
		}
		
		Integer clientQuoteId =clientQuote.getId();
		ClientQuoteElement clientQuoteElement=new ClientQuoteElement();
		clientQuoteElement.setClientQuoteId(clientQuoteId);
		clientQuoteElement.setClientInquiryElementId(clientInquiryElementId);
		clientQuoteElement.setSupplierQuoteElementId(supplierQuoteElementId);
		clientQuoteElement.setAmount(amount);
		clientQuoteElement.setPrice(price);
		clientQuoteElement.setRemark(importPackageElement.getRemark());
		clientQuoteElementDao.insertSelective(clientQuoteElement);
		
		Integer clientQuoteElementId =clientQuoteElement.getId();
		ClientOrder clientOrder=clientOrderDao.findOrderByImportPacksge(clientQuoteId);
		if(null==clientOrder){
			UserVo userVo =  ContextHolder.getCurrentUser();
			clientOrder=new ClientOrder();
			clientOrder.setClientQuoteId(clientQuoteId);
			clientOrder.setCurrencyId(currencyId);
			clientOrder.setExchangeRate(exchangeRate);
			Calendar calendar = Calendar.getInstance();
			clientOrder.setOrderDate(calendar.getTime());
			String orderStorageSourceNumber = orderForGetStorageSourceNumber(calendar
					.get(Calendar.YEAR));
			clientOrder.setSourceNumber(orderStorageSourceNumber);
			clientOrder.setSeq(1);
			clientOrder.setOrderNumber(orderStorageSourceNumber);
			clientOrder.setTerms(100);
			clientOrder.setOrderStatusId(63);
			clientOrder.setRemark("STORAGE");
			clientOrder.setCreateUserId(Integer.parseInt(userVo.getUserId()));
			clientOrder.setComplete(1);
			clientOrderDao.insertSelective(clientOrder);
		}
		
		Integer clientOrderId = clientOrder.getId();
		Calendar calendar = Calendar.getInstance();
		ClientOrderElement clientOrderElement=new ClientOrderElement();
		clientOrderElement.setClientOrderId(clientOrderId);
		clientOrderElement.setClientQuoteElementId(clientQuoteElementId);
		clientOrderElement.setAmount(amount);
		clientOrderElement.setPrice(price);
		clientOrderElement.setLeadTime("0");
		clientOrderElement.setDeadline(calendar.getTime());
		clientOrderElement.setUnit(clientInquiryElement.getUnit());
		clientOrderElementDao.insert(clientOrderElement);
		
		Integer clientOrderElementId = clientOrderElement.getId();
		List<SupplierOrder> supplierOrder=supplierOrderDao.findSupplierOrder( clientOrderId, supplierId, 63);
		
		SupplierOrder record=new SupplierOrder();
		if(null==supplierOrder||supplierOrder.isEmpty()){
			record.setClientOrderId(clientOrderId);
			record.setSupplierId(supplierId);
			record.setCurrencyId(currencyId);
			record.setExchangeRate(exchangeRate);
			record.setOrderNumber(getSupplierOrderNumber(clientOrder.getOrderNumber(),supplierCode));
			record.setTerms(100);
			record.setOrderStatusId(63);
			record.setOrderDate(new Date());
			record.setUpdateTimestamp(new Date());
			record.setRemark("STORAGE");
			supplierOrderDao.insertSelective(record);
		}else{
			record=supplierOrder.get(0);
		}
		
		Integer supplierOrderId =record.getId();

		SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
		supplierOrderElement.setSupplierOrderId(supplierOrderId);
		supplierOrderElement.setClientOrderElementId(clientOrderElementId);
		supplierOrderElement.setSupplierQuoteElementId(supplierQuoteElementId);
		supplierOrderElement.setAmount(amount);
		supplierOrderElement.setPrice(price);
		supplierOrderElement.setLeadTime("0");
		supplierOrderElement.setDeadline(calendar.getTime());
		supplierOrderElement.setOrderStatusId(63);
		int count = supplierOrderElementDao.getOrderCount(supplierOrderElement.getSupplierOrderId());
		if (count == 0) {
			supplierOrderElement.setItem(1);
		}else {
			supplierOrderElement.setItem(count+1);
		}
		supplierOrderElementDao.insertSelective(supplierOrderElement);
		
		Integer supplierOrderElementId =supplierOrderElement.getId();
		supplierImportElement.setSupplierOrderElementId(supplierOrderElementId);
		supplierImportElementDao.insert(supplierImportElement);
	}
	
	@Override
	public void Storage(StorageFlowVo storageFlowVo)throws Exception {
		
	
		String storageSourceNumber = inquiryForGetStorageSourceNumber(Calendar
				.getInstance().get(Calendar.YEAR));
		ClientInquiry clientInquiry=importPackageElementDao.findBySourceNumber(storageSourceNumber);
		if(null==clientInquiry){
			clientInquiry=new ClientInquiry();
			clientInquiry.setClientId( new Integer(16));
			clientInquiry.setClientContactId(new Integer(8));
			clientInquiry.setBizTypeId( new Integer(120));
			clientInquiry.setAirTypeId( new Integer(123));
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
			clientInquiry.setInquiryDate( calendar.getTime());
			calendar.set(calendar.get(Calendar.YEAR), 11, 31, 0, 0, 0);
			clientInquiry.setDeadline( calendar.getTime());
			clientInquiry.setSourceNumber(storageSourceNumber);
			clientInquiry.setQuoteNumber(storageSourceNumber);
			int maxQuoteNumberSeq = importPackageElementDao.findMAXquoteNumberSeq(clientInquiry.getInquiryDate());
			Integer nextSeq = new Integer(maxQuoteNumberSeq + 1);
			clientInquiry.setQuoteNumberSeq(new Integer(nextSeq));
			clientInquiry.setTerms("");
			clientInquiry.setInquiryStatusId(35);
			clientInquiry.setRemark("STORAGE");
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryDao.insert(clientInquiry);
		}
		Integer clientInquiryId =clientInquiry.getId();
		ClientInquiryElement clientInquiryElement=clientInquiryElementDao.selectStorageByElementId(storageFlowVo.getElementId().toString(), storageSourceNumber);
		if(null==clientInquiryElement){
		clientInquiryElement=new ClientInquiryElement();
		clientInquiryElement.setRemark("STORAGE");
		clientInquiryElement.setClientInquiryId(clientInquiryId);
		clientInquiryElement.setElementId(storageFlowVo.getElementId());
		clientInquiryElement.setPartNumber(storageFlowVo.getPartNumber());
		clientInquiryElement.setDescription(storageFlowVo.getDescription());
		clientInquiryElement.setUnit(storageFlowVo.getUnit());
		clientInquiryElement.setAmount(storageFlowVo.getAmount());
		clientInquiryElement.setUpdateTimestamp(new Date());
		clientInquiryElement.setIsMain(2);
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
		clientInquiryElement.setElementStatusId(710);
		clientInquiryElementDao.insert(clientInquiryElement);
		}
		Integer clientInquiryElementId=clientInquiryElement.getId();
		Integer supplierId =storageFlowVo.getSupplierId();
		String supplierCode =storageFlowVo.getSupplierCode();
		
		SupplierInquiry supplierinquiry=supplierInquiryDao.findSupplierByImportPackage(clientInquiryId, supplierId);
		if(null==supplierinquiry){
			supplierinquiry=new SupplierInquiry();
			Integer id=clientInquiry.getId();
			supplierinquiry.setClientInquiryId(id);
			supplierinquiry.setSupplierId(supplierId);
			supplierinquiry.setQuoteNumber(clientInquiry.getQuoteNumber()+"-"+supplierCode);
			supplierinquiry.setInquiryDate(clientInquiry.getInquiryDate());
			supplierinquiry.setDeadline(clientInquiry.getDeadline());
			supplierinquiry.setRemark(clientInquiry.getRemark());
			supplierInquiryDao.insertSelective(supplierinquiry);
		}
		
		Integer supplierInquiryId = supplierinquiry.getId();
		SupplierInquiryElement supplierInquiryElement=supplierInquiryElementDao.selectInquiryByElementId(storageFlowVo.getElementId(), supplierId, clientInquiryId);
		if(null==supplierInquiryElement){
		supplierInquiryElement=new SupplierInquiryElement();
		supplierInquiryElement.setSupplierInquiryId(supplierInquiryId);
		supplierInquiryElement.setClientInquiryElementId(clientInquiryElementId);
		supplierInquiryElementDao.insertSelective(supplierInquiryElement);
		}
		Integer supplierInquiryElementId =supplierInquiryElement.getId();
		Integer currencyId = storageFlowVo.getCurrencyId();
		Double exchangeRate =storageFlowVo.getExchangeRate();
		
		SupplierQuote supplierQuote=supplierQuoteDao.findSupplierInquiry(supplierInquiryId);
		if(null==supplierQuote){
			supplierQuote=new SupplierQuote();
			supplierQuote.setCurrencyId(currencyId);
			supplierQuote.setExchangeRate(exchangeRate);
			supplierQuote.setSupplierInquiryId(supplierInquiryId);
			supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
			supplierQuote.setQuoteNumber(supplierinquiry.getQuoteNumber());
			supplierQuoteDao.insertSelective(supplierQuote);
		}
		Integer supplierQuoteId =supplierQuote.getId();
		
		SupplierQuoteElement supplierQuoteElement=supplierQuoteElementDao.selectQuoteByElementId(supplierInquiryId, storageFlowVo.getElementId());
		if(null==supplierQuoteElement){
		 supplierQuoteElement=new SupplierQuoteElement();
		supplierQuoteElement.setSupplierQuoteId(supplierQuoteId);
		supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
		supplierQuoteElement.setElementId(storageFlowVo.getElementId());
		supplierQuoteElement.setPartNumber(storageFlowVo.getPartNumber());
		supplierQuoteElement.setDescription(storageFlowVo.getDescription());
		supplierQuoteElement.setAmount(storageFlowVo.getAmount());
		supplierQuoteElement.setUnit(storageFlowVo.getUnit());
		supplierQuoteElement.setPrice(storageFlowVo.getPrice());
		supplierQuoteElement.setLeadTime("");
		supplierQuoteElement.setRemark(storageFlowVo.getRemark());
		supplierQuoteElement.setConditionId(storageFlowVo.getConditionId());
		supplierQuoteElement.setCertificationId(storageFlowVo.getCertificationId());
		supplierQuoteElement.setSupplierQuoteStatusId(70);
		supplierQuoteElementDao.insert(supplierQuoteElement);
		}
		storageFlowVo.setId(supplierQuoteElement.getId());
		
	}
   

	@Override
	public void alterstoragea(ImportPackageElementVo importPackageElementVo, ImportPackageElement importPackageElement)throws Exception {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date exportDate = calendar.getTime();
////		ExportPackageVo exportPackageVo=new ExportPackageVo();
////		exportPackageVo.setExportDate(exportDate);
////		exportPackageVo.setClientId(16);
////		ExportPackage exportPackage=exportPackageDao.findByCidAndexportDate(exportPackageVo);
////		if(null==exportPackage){
//		ExportPackage	exportPackage=new ExportPackage();
//			exportPackage.setExportDate(exportDate);
//			exportPackage.setClientId(16);
//			exportPackage.setExchangeRate(importPackageElementVo.getExchangeRate());
//			exportPackage.setCurrencyId(importPackageElementVo.getCurrencyId());
//			exportPackage.setRemark("STORAGE");
//			List<ExportPackage> list=exportPackageDao.findStorageExport(exportPackage);
//			if(list.size()>0){
//				exportPackage.setId(list.get(0).getId());
//			}else{
//			exportPackage.getExportNumber();
//			
//			Integer seq=exportPackageDao.getMaxSeq(exportPackage);
//			int maxSeq;
//			if(null==seq){
//				maxSeq = 0;
//			}else{
//				maxSeq=seq;
//			}
//
//			 seq =maxSeq+1;
//			exportPackage.setSeq(seq);
//			String exportNumber =generateExportNumber(exportPackage);
//			exportPackage.setExportNumber(exportNumber);
//			exportPackage.setUpdateTimestamp(new Date());
//			exportPackageDao.insertSelective(exportPackage);
//			}
//			if(null!=exportPackage){
//				ExportPackageElement exportPackageElement=new ExportPackageElement();
//				exportPackageElement.setExportPackageId(exportPackage.getId());
//				exportPackageElement.setImportPackageElementId(importPackageElementVo.getId());
//				exportPackageElement.setAmount(importPackageElementVo.getAmount());
//				exportPackageElement.setRemark("STORAGE");
//				exportPackageElement.setUpdateTimestamp(new Date());
//				exportPackageElement.setStatus(0);
//				exportPackageElementDao.insertSelective(exportPackageElement);
//			}
		
			importPackageElement.setImportPackageId(importPackageElementVo.getImportPackageId());
			importPackageElement.setElementId(importPackageElementVo.getElementId());
			importPackageElement.setPartNumber(importPackageElementVo.getPartNumber());
			importPackageElement.setDescription(importPackageElementVo.getDescription());
			importPackageElement.setUnit(importPackageElementVo.getUnit());
			importPackageElement.setAmount(-importPackageElementVo.getAmount());
			importPackageElement.setPrice(importPackageElementVo.getPrice());
			importPackageElement.setCertificationId(importPackageElementVo.getCertificationId());
			importPackageElement.setConditionId(importPackageElementVo.getConditionId());
			importPackageElement.setRemark(importPackageElementVo.getRemark());
			List<ImportPackageListVo> imortpackagedate=importPackageDao.findImportPackageDate(importPackageElementVo.getImportPackageId().toString());
			for (ImportPackageListVo importPackageListVo : imortpackagedate) {
				 Date importDate  =importPackageListVo.getImportDate();
				 importPackageElement.setCertificationDate(importDate);
				 int originalNumber=getOriginalNumber(importDate);
				 importPackageElement.setOriginalNumber(originalNumber);
			}
	//		importPackageElement.setOriginalNumber(importPackageElementVo.getOriginalNumber());
			importPackageElement.setSerialNumber(importPackageElementVo.getSerialNumber());
			importPackageElement.setCertificationDate(importPackageElementVo.getCertificationDate());
			importPackageElement.setUpdateTimestamp(new Date());
			importPackageElement.setComplianceCertificate(300);
			importPackageElement.setCompleteComplianceCertificate(520);
			importPackageElement.setSupplierOrderElementId(importPackageElementVo.getIpeSupplierOrderElementId());
			importPackageElement.setImportPackageSign(importPackageElementVo.getImportPackageSign());
			importPackageElement.setRestLifeEmail(0);
			importPackageElementDao.insert(importPackageElement);
			SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(importPackageElementVo.getSupplierOrderElementId());
			ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
			ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
			IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientOrderElement1.getId());
			ClientOrder clientOrder1 = clientOrderDao.selectByPrimaryKey(clientOrderElement1.getClientOrderId());
			if (!"".equals(clientOrderElement1.getElementStatusId()) && clientOrderElement1.getElementStatusId()!=null) {
				if (clientOrderElement1.getElementStatusId().equals(707) || clientOrderElement1.getElementStatusId()==707 ||
						clientOrderElement1.getElementStatusId().equals(705) || clientOrderElement1.getElementStatusId()==705){
					if (clientOrder1.getPrepayRate() > 0 && incomeDetail == null) {
						clientOrderElement1.setElementStatusId(709);
					}else {
						clientOrderElement1.setElementStatusId(708);
					}
					clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement1);
				}
			}
			

			Integer importPackageElementId2 =importPackageElement.getId();
			
			SupplierImportElement supplierImportElement2=new SupplierImportElement();
			
			supplierImportElement2.setImportPackageElementId(importPackageElementId2);
			supplierImportElement2.setSupplierOrderElementId(importPackageElementVo.getSupplierOrderElementId());
			supplierImportElement2.setAmount(-importPackageElementVo.getAmount());
			supplierImportElementDao.insert(supplierImportElement2);
			
			ClientInquiryElement clientInquiryElement=new ClientInquiryElement();
			clientInquiryElement.setRemark("STORAGE");
			String storageSourceNumber = inquiryForGetStorageSourceNumber(Calendar
					.getInstance().get(Calendar.YEAR));
			ClientInquiry clientInquiry=importPackageElementDao.findBySourceNumber(storageSourceNumber);
			if(null==clientInquiry){
				clientInquiry=new ClientInquiry();
				clientInquiry.setClientId( new Integer(16));
				clientInquiry.setClientContactId(new Integer(8));
				clientInquiry.setBizTypeId( new Integer(120));
				clientInquiry.setAirTypeId( new Integer(123));
				Calendar calendar1 = Calendar.getInstance();
				calendar1.set(calendar1.get(Calendar.YEAR), 0, 1, 0, 0, 0);
				clientInquiry.setInquiryDate( calendar1.getTime());
				calendar1.set(calendar1.get(Calendar.YEAR), 11, 31, 0, 0, 0);
				clientInquiry.setDeadline( calendar1.getTime());
				clientInquiry.setSourceNumber(storageSourceNumber);
				clientInquiry.setQuoteNumber(storageSourceNumber);
				int maxQuoteNumberSeq = importPackageElementDao.findMAXquoteNumberSeq(clientInquiry.getInquiryDate());
				Integer nextSeq = new Integer(maxQuoteNumberSeq + 1);
				clientInquiry.setQuoteNumberSeq(new Integer(nextSeq));
				clientInquiry.setTerms("");
				clientInquiry.setInquiryStatusId(35);
				clientInquiry.setRemark("STORAGE");
				clientInquiry.setUpdateTimestamp(new Date());
				clientInquiryDao.insert(clientInquiry);
			}
			
			Integer clientInquiryId =clientInquiry.getId();
			clientInquiryElement.setClientInquiryId(clientInquiryId);
			clientInquiryElement.setElementId(importPackageElementVo.getElementId());
			clientInquiryElement.setPartNumber(importPackageElementVo.getPartNumber());
			clientInquiryElement.setDescription(importPackageElementVo.getDescription());
			clientInquiryElement.setUnit(importPackageElementVo.getUnit());
			clientInquiryElement.setAmount(importPackageElementVo.getAmount());
			clientInquiryElement.setUpdateTimestamp(new Date());
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
			clientInquiryElement.setBsn(clientInquiryElement1.getBsn());
			clientInquiryElementDao.insert(clientInquiryElement);
			
			Integer clientInquiryElementId=clientInquiryElement.getId();
			Integer supplierId =importPackageElementVo.getSupplierId();
			String supplierCode =importPackageElementVo.getSupplierCode();
			SupplierInquiry supplierinquiry=supplierInquiryDao.findSupplierByImportPackage(clientInquiry.getId(), supplierId);
			
			if(null==supplierinquiry){
				supplierinquiry=new SupplierInquiry();
				Integer id=clientInquiry.getId();
				supplierinquiry.setClientInquiryId(id);
				supplierinquiry.setSupplierId(supplierId);
				supplierinquiry.setQuoteNumber(clientInquiry.getQuoteNumber()+"-"+supplierCode);
				supplierinquiry.setInquiryDate(clientInquiry.getInquiryDate());
				supplierinquiry.setDeadline(clientInquiry.getDeadline());
				supplierinquiry.setRemark(clientInquiry.getRemark());
				supplierInquiryDao.insertSelective(supplierinquiry);
			}
			
			Integer supplierInquiryId = supplierinquiry.getId();
			SupplierInquiryElement supplierInquiryElement=new SupplierInquiryElement();
			supplierInquiryElement.setSupplierInquiryId(supplierInquiryId);
			supplierInquiryElement.setClientInquiryElementId(clientInquiryElementId);
			supplierInquiryElementDao.insertSelective(supplierInquiryElement);
			
			Integer supplierInquiryElementId =supplierInquiryElement.getId();
			SupplierQuote vo=supplierQuoteDao.findSupplierInquiry(supplierInquiryId);
			if(null==vo){
				vo=new SupplierQuote();
				vo.setCurrencyId(importPackageElementVo.getCurrencyId());
				vo.setExchangeRate(importPackageElementVo.getExchangeRate());
				vo.setSupplierInquiryId(supplierInquiryId);
				vo.setQuoteDate(Calendar.getInstance().getTime());
				vo.setQuoteNumber(supplierinquiry.getQuoteNumber());
				supplierQuoteDao.insertSelective(vo);
			}
			
			Integer supplierQuoteId =vo.getId();
			SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
			supplierQuoteElement.setSupplierQuoteId(supplierQuoteId);
			supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
			supplierQuoteElement.setElementId(importPackageElementVo.getElementId());
			supplierQuoteElement.setPartNumber(importPackageElementVo.getPartNumber());
			supplierQuoteElement.setDescription(importPackageElementVo.getDescription());
			supplierQuoteElement.setAmount(importPackageElementVo.getAmount());
			supplierQuoteElement.setUnit(importPackageElementVo.getUnit());
			supplierQuoteElement.setPrice(importPackageElementVo.getPrice());
			supplierQuoteElement.setLeadTime("");
			supplierQuoteElement.setRemark(importPackageElementVo.getRemark());
			supplierQuoteElement.setConditionId(importPackageElementVo.getConditionId());
			supplierQuoteElement.setCertificationId(importPackageElementVo.getCertificationId());
			supplierQuoteElement.setSupplierQuoteStatusId(70);
			supplierQuoteElementDao.insert(supplierQuoteElement);
			
			Integer supplierQuoteElementId =supplierQuoteElement.getId();
			ClientQuote clientQuote=clientQuoteDao.findclientquote(clientInquiry.getId());
			if(null==clientQuote){
				clientQuote=new ClientQuote();
				clientQuote.setClientInquiryId(clientInquiry.getId());
				clientQuote.setQuoteDate(Calendar.getInstance().getTime());
				clientQuote.setCurrencyId(importPackageElementVo.getCurrencyId());
				clientQuote.setExchangeRate(importPackageElementVo.getExchangeRate());
				clientQuote.setQuoteNumber(clientInquiry.getQuoteNumber());
				clientQuote.setSeq(1);
				clientQuote.setProfitMargin(BigDecimal.ONE.doubleValue());
				clientQuoteDao.insertSelective(clientQuote);
			}
			
			Integer clientQuoteId =clientQuote.getId();
			ClientQuoteElement clientQuoteElement=new ClientQuoteElement();
			clientQuoteElement.setClientQuoteId(clientQuoteId);
			clientQuoteElement.setClientInquiryElementId(clientInquiryElementId);
			clientQuoteElement.setSupplierQuoteElementId(supplierQuoteElementId);
			clientQuoteElement.setAmount(importPackageElementVo.getAmount());
			clientQuoteElement.setPrice(importPackageElementVo.getPrice());
			clientQuoteElement.setRemark(importPackageElement.getRemark());
			clientQuoteElementDao.insertSelective(clientQuoteElement);
			
			Integer clientQuoteElementId =clientQuoteElement.getId();
			ClientOrder clientOrder=clientOrderDao.findOrderByImportPacksge(clientQuoteId);
			if(null==clientOrder){
				clientOrder=new ClientOrder();
				clientOrder.setClientQuoteId(clientQuoteId);
				clientOrder.setCurrencyId(importPackageElementVo.getCurrencyId());
				clientOrder.setExchangeRate(importPackageElementVo.getExchangeRate());
				Calendar calendar1 = Calendar.getInstance();
				clientOrder.setOrderDate(calendar1.getTime());
				String orderStorageSourceNumber = orderForGetStorageSourceNumber(calendar
						.get(Calendar.YEAR));
				clientOrder.setSourceNumber(orderStorageSourceNumber);
				clientOrder.setSeq(1);
				clientOrder.setOrderNumber(orderStorageSourceNumber);
				clientOrder.setTerms(100);
				clientOrder.setOrderStatusId(63);
				clientOrder.setRemark("STORAGE");
				clientOrder.setComplete(1);
				clientOrderDao.insertSelective(clientOrder);
			}
			
			Integer clientOrderId = clientOrder.getId();
			ClientOrderElement clientOrderElement=new ClientOrderElement();
			clientOrderElement.setClientOrderId(clientOrderId);
			clientOrderElement.setClientQuoteElementId(clientQuoteElementId);
			clientOrderElement.setAmount(importPackageElementVo.getAmount());
			clientOrderElement.setPrice(importPackageElementVo.getPrice());
			clientOrderElement.setLeadTime("0");
			clientOrderElement.setDeadline(calendar.getTime());
			clientOrderElementDao.insert(clientOrderElement);
			
			Integer clientOrderElementId = clientOrderElement.getId();
			List<SupplierOrder> supplierOrder=supplierOrderDao.findSupplierOrder( clientOrderId, supplierId, 63);
			
			SupplierOrder record=new SupplierOrder();
			if(null==supplierOrder||supplierOrder.isEmpty()){
				record.setClientOrderId(clientOrderId);
				record.setSupplierId(supplierId);
				record.setCurrencyId(importPackageElementVo.getCurrencyId());
				record.setExchangeRate(importPackageElementVo.getExchangeRate());
				record.setOrderNumber(getSupplierOrderNumber(clientOrder.getOrderNumber(),supplierCode));
				record.setTerms(100);
				record.setOrderStatusId(63);
				record.setOrderDate(new Date());
				record.setUpdateTimestamp(new Date());
				record.setRemark("STORAGE");
				supplierOrderDao.insertSelective(record);
			}else{
				record=supplierOrder.get(0);
			}
			
			Integer supplierOrderId =record.getId();
			SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
			supplierOrderElement.setSupplierOrderId(supplierOrderId);
			supplierOrderElement.setClientOrderElementId(clientOrderElementId);
			supplierOrderElement.setSupplierQuoteElementId(supplierQuoteElementId);
			supplierOrderElement.setAmount(importPackageElementVo.getAmount());
			supplierOrderElement.setPrice(importPackageElementVo.getPrice());
			supplierOrderElement.setLeadTime("0");
			supplierOrderElement.setDeadline(calendar.getTime());
			supplierOrderElement.setOrderStatusId(63);
			int amount = supplierOrderElementDao.getOrderCount(supplierOrderId);
			if (amount == 0) {
				supplierOrderElement.setItem(1);
			}else {
				supplierOrderElement.setItem(amount+1);
			}
			supplierOrderElementDao.insertSelective(supplierOrderElement);
			
			Integer supplierOrderElementId =supplierOrderElement.getId();
			ImportPackage importPackage=new ImportPackage();
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			String ipdate=dateFormat.format(new Date());
			try {
				importPackage.setImportDate(dateFormat.parse(ipdate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			importPackage.setRemark("STORAGE");
			importPackage.setSupplierId(supplierId);
			List<ImportPackage> importPackages=importPackageDao.findStorageAlertOrder(importPackage);
			if(importPackages.size()>0){
				importPackage.setId(importPackages.get(0).getId());
			}else{
			importPackage.setImportDate(exportDate);
			
			importPackage.setCurrencyId(importPackageElementVo.getCurrencyId());
			importPackage.setExchangeRate(importPackageElementVo.getExchangeRate());
		
			Integer ipseq=importPackageDao.findmaxseq(importPackage);
			importPackage.setSeq(ipseq+1);
			String importNumber = generateImportNumber(importPackage);
			importPackage.setImportNumber(importNumber);
			//入库转库存的时候 默认完成 即状态为1
//			importPackage.setImportStatus(1);
			importPackageDao.insert(importPackage);
			}
			
			importPackageElement.setImportPackageId(importPackage.getId());
			importPackageElement.setElementId(importPackageElementVo.getElementId());
			importPackageElement.setPartNumber(importPackageElementVo.getPartNumber());
			importPackageElement.setDescription(importPackageElementVo.getDescription());
			importPackageElement.setUnit(importPackageElementVo.getUnit());
			importPackageElement.setAmount(importPackageElementVo.getAmount());
			importPackageElement.setPrice(importPackageElementVo.getPrice());
			importPackageElement.setCertificationId(importPackageElementVo.getCertificationId());
			importPackageElement.setConditionId(importPackageElementVo.getConditionId());
			importPackageElement.setRemark(importPackageElementVo.getRemark());
			imortpackagedate=importPackageDao.findImportPackageDate(importPackage.getId().toString());
			for (ImportPackageListVo importPackageListVo : imortpackagedate) {
				 Date importDate  =importPackageListVo.getImportDate();
				 importPackageElement.setCertificationDate(importDate);
				 int originalNumber=getOriginalNumber(importDate);
				 importPackageElement.setOriginalNumber(originalNumber);
			}
//			importPackageElement.setOriginalNumber(importPackageElementVo.getOriginalNumber());
			importPackageElement.setSerialNumber(importPackageElementVo.getSerialNumber());
			importPackageElement.setCertificationDate(importPackageElementVo.getCertificationDate());
			importPackageElement.setUpdateTimestamp(new Date());
			importPackageElement.setComplianceCertificate(300);
			importPackageElement.setCompleteComplianceCertificate(520);
			importPackageElement.setSupplierOrderElementId(importPackageElementVo.getIpeSupplierOrderElementId());
			importPackageElement.setImportPackageSign(1);
			importPackageElementDao.insert(importPackageElement);
			if (!"".equals(clientOrderElement1.getElementStatusId()) && clientOrderElement1.getElementStatusId()!=null) {
				if (clientOrderElement1.getElementStatusId().equals(707) || clientOrderElement1.getElementStatusId()==707 ||
						clientOrderElement1.getElementStatusId().equals(705) || clientOrderElement1.getElementStatusId()==705){
					if (clientOrder1.getPrepayRate() > 0 && incomeDetail != null) {
						clientOrderElement1.setElementStatusId(709);
					}else {
						clientOrderElement1.setElementStatusId(708);
					}
					clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement1);
				}
			}
			

			Integer importPackageElementId =importPackageElement.getId();
			
			SupplierImportElement supplierImportElement=new SupplierImportElement();
			
			supplierImportElement.setImportPackageElementId(importPackageElementId);
			supplierImportElement.setSupplierOrderElementId(supplierOrderElementId);
			supplierImportElement.setAmount(importPackageElementVo.getAmount());
			supplierImportElementDao.insert(supplierImportElement);
//		}
	}

	@Override
	public int findSupplierBySupplierOrderNumber(String orderNumber) {
		return importPackageElementDao.findSupplierBySupplierOrderNumber(orderNumber);
	}
	
	public StorageDetailVo getCountAndPrice(){
		return importPackageElementDao.getCountAndPrice();
	}

	@Override
	public List<ImportPackageElementVo> findImportpackageData(ImportPackageElementVo imortpackagedate) {
		return importPackageElementDao.findImportpackageData(imortpackagedate);
	}

	@Override
	public int findStock(ImportPackageElementVo imortpackagedate) {
		return importPackageElementDao.findStock(imortpackagedate);
	}

	@Override
	public StorageDetailVo BoxWeight(ImportPackageElement parame) {
		return importPackageElementDao.BoxWeight(parame);
	}

	@Override
	public List<StorageDetailVo> ecportpackageInstructionsData(StorageFlowVo vo) {
		return importPackageElementDao.ecportpackageInstructionsData(vo);
	}

	@Override
	public ImportPackageElement selectByPrimaryKey(Integer id) {
		return importPackageElementDao.selectByPrimaryKey(id);
	}

	@Override
	public SupplierOrderElementVo findByclientOrderElementId(Integer clientOrderElementId) {
		return importPackageElementDao.findByclientOrderElementId(clientOrderElementId);
	}
		
	/**
	@Author: Modify by white
	@DateTime: 2018/11/9 16:04
	@Description: 修改“库存转订单”的时候为完成状态
	*/
	@Override
	public String splitOrder(StorageFlowVo supplierListVo ) {
		ImportPackage importPackage=new ImportPackage();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String ipdate=dateFormat.format(new Date());
		try {
			importPackage.setImportDate(dateFormat.parse(ipdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		importPackage.setSupplierId(supplierListVo.getSupplierId());
		importPackage.setCurrencyId(supplierListVo.getCurrencyId());
		importPackage.setRemark("库存转订单");
		List<ImportPackage> importPackages=importPackageDao.findStorageAlertOrder(importPackage);
		if(importPackages.size()>0){
			importPackage.setId(importPackages.get(0).getId());
			importPackage.setImportNumber(importPackages.get(0).getImportNumber());
		}else{
		importPackage.setExchangeRate(supplierListVo.getExchangeRate());
		Integer ipseq=importPackageDao.findmaxseq(importPackage);
		importPackage.setSeq(ipseq+1);
		String importNumber = generateImportNumber(importPackage);
		importPackage.setImportNumber(importNumber);
		//“库存转订单的时候自动完成”
		importPackage.setImportStatus(1);
		importPackageDao.insert(importPackage);
		}
		ImportPackageElement importPackageElement=new ImportPackageElement();
		List<ImportPackageListVo> imortpackagedate=importPackageDao.findImportPackageDate(importPackage.getId().toString());
		for (ImportPackageListVo importPackageListVo : imortpackagedate) {
			 Date importDate  =importPackageListVo.getImportDate();
			 int originalNumber=getOriginalNumber(importDate);
			 importPackageElement.setOriginalNumber(originalNumber);
		}
		
		importPackageElement.setImportPackageId(importPackage.getId());
		importPackageElement.setElementId(supplierListVo.getElementId());
		importPackageElement.setPartNumber(supplierListVo.getPartNumber().trim());
		importPackageElement.setDescription(supplierListVo.getDescription());
		importPackageElement.setUnit(supplierListVo.getUnit());
		importPackageElement.setAmount(supplierListVo.getAmount());
		importPackageElement.setPrice(supplierListVo.getPrice());
		importPackageElement.setCertificationId(supplierListVo.getCertificationId());
		importPackageElement.setConditionId(supplierListVo.getConditionId());
		importPackageElement.setRemark(supplierListVo.getRemark());
		importPackageElement.setSerialNumber(supplierListVo.getSerialNumber());
		importPackageElement.setCertificationDate(supplierListVo.getCertificationDate());
		importPackageElement.setUpdateTimestamp(new Date());
		importPackageElement.setLocation(supplierListVo.getLocation());
		importPackageElement.setComplianceCertificate(300);
		importPackageElement.setCompleteComplianceCertificate(520);
		importPackageElement.setImportPackageSign(1);
		importPackageElement.setSupplierOrderElementId(supplierListVo.getIpeSupplierOrderElementId());
		importPackageElement.setRestLifeEmail(0);
		importPackageElementDao.insert(importPackageElement);
		SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(supplierListVo.getSupplierOrderElementId());
		ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
		ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
		ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
		IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientOrderElement1.getId());
		ClientOrder clientOrder1 = clientOrderDao.selectByPrimaryKey(clientOrderElement1.getClientOrderId());
		if (!"".equals(clientOrderElement1.getElementStatusId()) && clientOrderElement1.getElementStatusId()!=null) {
			if (clientOrderElement1.getElementStatusId().equals(707) || clientOrderElement1.getElementStatusId()==707 ||
					clientOrderElement1.getElementStatusId().equals(705) || clientOrderElement1.getElementStatusId()==705){
				if (clientOrder1.getPrepayRate() > 0 && incomeDetail == null) {
					clientOrderElement1.setElementStatusId(709);
				}else {
					clientOrderElement1.setElementStatusId(708);
				}
				clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement1);
			}
		}
		
		SupplierImportElement supplierImportElement=new SupplierImportElement();
		
		supplierImportElement.setImportPackageElementId(importPackageElement.getId());
		supplierImportElement.setSupplierOrderElementId(supplierListVo.getSupplierOrderElementId());
		supplierImportElement.setAmount(supplierListVo.getAmount());
		supplierImportElementDao.insert(supplierImportElement);
		
		ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare = new ImportPackagePaymentElementPrepare();
		importPackagePaymentElementPrepare.setAmount(importPackageElement.getAmount());
		Integer supplierId = supplierOrderElementDao.getSupplierId(supplierImportElement.getSupplierOrderElementId());
		importPackagePaymentElementPrepare.setSupplierId(supplierId);
		importPackagePaymentElementPrepare.setSupplierOrderElementId(supplierListVo.getSupplierOrderElementId());
		importPackagePaymentElementPrepare.setImportPackageId(importPackage.getId());
		importPackagePaymentElementPrepareService.add(importPackagePaymentElementPrepare);
		
		ImportPackageElement importPackageElement2=new ImportPackageElement();
		importPackageElement2.setId(supplierListVo.getImportPackageElementId());
		importPackageElement2.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
		importPackageElementDao.updateByPrimaryKeySelective(importPackageElement2);
		
		supplierImportElement.setSupplierOrderElementId(supplierListVo.getSoeId());
		supplierImportElement.setImportPackageElementId(supplierListVo.getImportPackageElementId());
		supplierImportElement.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
		supplierImportElementDao.updateByPrimaryKeySelective(supplierImportElement);
		
//		SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
//		supplierOrderElement.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
//		supplierOrderElement.setId(supplierListVo.getSoeId());
//		supplierOrderElementDao.updateByPrimaryKeySelective(supplierOrderElement);
		
//		ClientOrderElement clientOrderElement=new ClientOrderElement();
//		clientOrderElement.setId(supplierListVo.getId());
//		clientOrderElement.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
//		clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
//		
//		SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
//		supplierQuoteElement.setId(supplierListVo.getId());
//		supplierQuoteElement.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
//		supplierQuoteElementDao.updateByPrimaryKeySelective(supplierQuoteElement);
//		
//		ClientQuoteElement clientQuoteElement=new ClientQuoteElement();
//		clientQuoteElement.setId(supplierListVo.getClientQuoteElementId());
//		clientQuoteElement.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
//		clientQuoteElementDao.updateByPrimaryKeySelective(clientQuoteElement);
//		
//		ClientInquiryElement clientInquiryElement =new ClientInquiryElement();
//		clientInquiryElement.setId(supplierListVo.getClientInquiryElementId());
//		clientInquiryElement.setAmount(supplierListVo.getImportPackageAmount()-supplierListVo.getAmount());
//		clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
		
		return importPackage.getImportNumber();
		
	}
	
	 public int getOriginalNumber(Date importDate) {
			int prefix = getOriginalNumberPrefix(importDate);
			int onStart = prefix * 100;
			int onEnd = prefix * 100 + 100;
			int max = importPackageDao.findMaxOriginalNumber(onStart, onEnd);
			int sum =max+1;
			if(sum==onEnd){
				 onStart = prefix * 1000;
				 onEnd = prefix * 1000 + 1000;
				 max = importPackageDao.findMaxOriginalNumber(onStart, onEnd);
			}
			int originalNumber;
			if (max > 0) {
				originalNumber = max + 1;
			} else {
				originalNumber = onStart + 1;
			}
			return originalNumber;
		}
	 
	 public int getOriginalNumberPrefix(Date importDate) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(importDate);
			int year = calendar.get(Calendar.YEAR) % 100;
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int prefix = year * 10000 + month * 100 + day;
			return prefix;
		}

	@Override
	public List<ImportPackageElementVo> findStorageByCoeId(Integer id) {
		return importPackageElementDao.findStorageByCoeId(id);
	}
	
	public void checkLastPayment(ImportPackageElement importPackageElement,Integer supplierOrderElementId) {
		SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(supplierOrderElementId);
		SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(supplierOrderElement.getSupplierOrderId());
		Double orderAmount = supplierOrderElementDao.getAmountBySupplierOrderId(supplierOrderElement.getSupplierOrderId());
		Double importAmount = importPackageElementDao.getAmountBySupplierOrderId(supplierOrderElement.getSupplierOrderId());
		if (orderAmount.equals(importAmount)) {
			String[] ziMu={"PA","PB","PC","PD","PE","PF","PG","PH","PI","PJ","PK","PL","PM",
					"PN","PO","PP","PQ","PR","PS","PT","PU","PV","PW","PX","PY","PZ"};
			int index = importPackagePaymentDao.getCountByImportPackageId(supplierOrder.getId());
			ImportPackage importPackage = importPackageDao.selectByImportPackageElementId(importPackageElement.getId());
			Supplier supplier = supplierDao.selectByPrimaryKey(importPackage.getSupplierId());
			ImportPackagePayment importPackagePayment = new ImportPackagePayment();
			importPackagePayment.setSupplierOrderId(supplierOrder.getId());
			importPackagePayment.setPaymentType(2);
			importPackagePayment.setPaymentDate(new Date());
			importPackagePayment.setPaymentNumber(supplierOrder.getOrderNumber().substring(4)+ziMu[index]);
			importPackagePayment.setPaymentPercentage(supplier.getReceivePayRate());
			importPackagePayment.setSupplierId(supplier.getId());
			importPackagePayment.setPaymentStatusId(234);
			importPackagePaymentDao.insertSelective(importPackagePayment);
			List<SupplierOrderElement> list = supplierOrderElementDao.selectBySupplierOrderId(supplierOrder.getId());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getAmount()>0) {
					ImportPackagePaymentElement importPackagePaymentElement = new ImportPackagePaymentElement();
					importPackagePaymentElement.setImportPackagePaymentId(importPackagePayment.getId());
					importPackagePaymentElement.setSupplierOrderElementId(list.get(i).getId());
					importPackagePaymentElement.setAmount(list.get(i).getAmount());
					importPackagePaymentElementService.insertSelective(importPackagePaymentElement);
				}
			}
		}
	}

	@Override
	public   List<ImportPackageElement> findCertStatus(Integer importpackageId) {
		return importPackageElementDao.findCertStatus(importpackageId);
	}
	
	public List<StorageDetailVo> getStorageAmountByQuote(PageModel<StorageDetailVo> page){
		return importPackageElementDao.getStorageAmountByQuote(page);
	}
	
	public List<SupplierOrderElementVo> selectELementBySoe(Integer supplierOrderElementId){
		return importPackageElementDao.selectELementBySoe(supplierOrderElementId);
	}
	
	public Double selectImportBySoeIdAndCoeId(Integer supplierOrderElementId,Integer clientOrderElementId){
		return importPackageElementDao.selectImportBySoeIdAndCoeId(supplierOrderElementId, clientOrderElementId);
	}
	
	public SupplierImportElement onPassageImport(ImportPackageElement importPackageElement,SupplierImportElement supplierImportElement) {
		SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(supplierImportElement.getSupplierOrderElementId());
		OnPassageStorage onPassageStorage = new OnPassageStorage();
		onPassageStorage.setAmount(importPackageElement.getAmount());
		onPassageStorage.setSupplierOrderElementId(supplierImportElement.getSupplierOrderElementId());
		onPassageStorage.setClientOrderElementId(importPackageElement.getClientOrderElementId());
		List<OnPassageStorage> list = onPassageStorageDao.getBySoeIdAndCoeId(onPassageStorage);
		BigDecimal supplierOrderAmount =new BigDecimal(Double.toString(supplierOrderElement.getAmount()));
		BigDecimal newSupplierOrderAmount =new BigDecimal(Double.toString(importPackageElement.getAmount()));
		supplierOrderElement.setAmount(supplierOrderAmount.subtract(newSupplierOrderAmount).doubleValue());
		SupplierOrderElement newSupplierOrderElement = new SupplierOrderElement();
		newSupplierOrderElement.setClientOrderElementId(list.get(0).getClientOrderElementId());
		newSupplierOrderElement.setAmount(list.get(0).getAmount());
		newSupplierOrderElement.setSupplierOrderId(supplierOrderElement.getSupplierOrderId());
		newSupplierOrderElement.setSupplierQuoteElementId(supplierOrderElement.getSupplierQuoteElementId());
		newSupplierOrderElement.setPrice(supplierOrderElement.getPrice());
		newSupplierOrderElement.setOrderStatusId(supplierOrderElement.getOrderStatusId());
		if (supplierOrderElement.getDeadline() != null) {
			newSupplierOrderElement.setDeadline(supplierOrderElement.getDeadline());
		}
		if (supplierOrderElement.getLeadTime() != null) {
			newSupplierOrderElement.setLeadTime(supplierOrderElement.getLeadTime());
		}
		if (supplierOrderElement.getShipWayId() != null) {
			newSupplierOrderElement.setShipWayId(supplierOrderElement.getShipWayId());
		}
		if (supplierOrderElement.getImportStatus() != null) {
			newSupplierOrderElement.setImportStatus(supplierOrderElement.getImportStatus());
		}
		if (supplierOrderElement.getDestination() != null) {
			newSupplierOrderElement.setDestination(supplierOrderElement.getDestination());
		}
		if (supplierOrderElement.getAwb() != null) {
			newSupplierOrderElement.setAwb(supplierOrderElement.getAwb());
		}
		int amount = supplierOrderElementDao.getOrderCount(supplierOrderElement.getSupplierOrderId());
		if (amount == 0) {
			supplierOrderElement.setItem(1);
		}else {
			supplierOrderElement.setItem(amount+1);
		}
		supplierOrderElementDao.insertSelective(newSupplierOrderElement);
		supplierOrderElementDao.updateByPrimaryKeySelective(supplierOrderElement);
		importPackageElementDao.insertSelective(importPackageElement);
		SupplierOrderElement supplierOrderElement1 = supplierOrderElementDao.selectByPrimaryKey(importPackageElement.getSupplierOrderElementId());
		ClientOrderElement clientOrderElement1 = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement1.getClientOrderElementId());
		ClientQuoteElement clientQuoteElement1 = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement1.getClientQuoteElementId());
		ClientInquiryElement clientInquiryElement1 = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement1.getClientInquiryElementId());
		IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientOrderElement1.getId());
		ClientOrder clientOrder1 = clientOrderDao.selectByPrimaryKey(clientOrderElement1.getClientOrderId());
		if (!"".equals(clientOrderElement1.getElementStatusId()) && clientOrderElement1.getElementStatusId()!=null) {
			if (clientOrderElement1.getElementStatusId().equals(707) || clientOrderElement1.getElementStatusId()==707 ||
					clientOrderElement1.getElementStatusId().equals(705) || clientOrderElement1.getElementStatusId()==705){
				if (clientOrder1.getPrepayRate() > 0 && incomeDetail == null) {
					clientOrderElement1.setElementStatusId(709);
				}else {
					clientOrderElement1.setElementStatusId(708);
				}
				clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement1);
			}
		}
		supplierImportElement.setImportPackageElementId(importPackageElement.getId());
		supplierImportElement.setSupplierOrderElementId(newSupplierOrderElement.getId());
		supplierImportElementDao.insertSelective(supplierImportElement);
		list.get(0).setImportStatus(1);
		list.get(0).setSupplierOrderElementId(supplierImportElement.getSupplierOrderElementId());
		onPassageStorageDao.updateByPrimaryKeySelective(list.get(0));
		sendEmail(supplierOrderElement,newSupplierOrderElement);
		return supplierImportElement;
	}
	
	public void sendEmail(SupplierOrderElement supplierOrderElement,SupplierOrderElement newSupplierOrderElement) {
		SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(supplierOrderElement.getSupplierOrderId());
		SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectByPrimaryKey(supplierOrderElement.getSupplierQuoteElementId());
		List<Integer> list = authorityRelationDao.getUserId(supplierOrder.getSupplierId());
		List<String> emails = new ArrayList<String>();
		List<String> user = new ArrayList<String>();
		List<String> cc = new ArrayList<String>();
		UserVo storage = userDao.getByRole("物流").get(0);
		cc.add(storage.getEmail());
		for (int i = 0; i < list.size(); i++) {
			UserVo userVo = userDao.findById(list.get(i));
			emails.add(userVo.getEmail());
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("订单").append(supplierOrder.getOrderNumber()).append("件号").append(supplierQuoteElement.getPartNumber()).append("的明细")
		.append("拆分成两个，数量分别是：").append(supplierOrderElement.getAmount()).append("个和").append(newSupplierOrderElement.getAmount()).append("个");
		ExchangeMail exchangeMail = new ExchangeMail();
		exchangeMail.setUsername(storage.getEmail());
		exchangeMail.setPassword(storage.getEmailPassword());
		exchangeMail.init();
		String path = "";
		List<String> bcc = new ArrayList<String>();
		try {
			exchangeMail.doSend("拆分订单明细", emails, cc, user, buffer.toString(), path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<ImportPackageElement> selectByExportPackageId(PageModel<ExportPackageElementVo> page){
		return importPackageElementDao.selectByExportPackageId(page);
	}
	
	 public List<ImportPackageElement> selectByUserId(Integer userId){
		 return importPackageElementDao.selectByUserId(userId);
	 }

//	@Override
//	public String splitOrder(List<OrderApproval> storageList, List<OrderApproval> onStorageList) {
//		if(storageList.size()>0){
//			for (OrderApproval orderApproval : storageList) {
//				String ipeIds=orderApproval.getIpeIds();
//				String[] importPackageElementIds=ipeIds.split(",");
//				boolean full=false;
//				ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
//				Double orderAmount=clientOrderElement.getAmount();
//				for (int i = 0; i < importPackageElementIds.length; i++) {
//					ImportPackageElementVo elementVo=importPackageElementDao.selectStorageByImportPackageElementId(importPackageElementIds[i]);
//					Double storageAmount=elementVo.getAmount();
//					SupplierOrder supplierOrder=new SupplierOrder();
//	      			supplierOrder.setSupplierId(elementVo.getSupplierId());
//	      			supplierOrder.setClientOrderId(clientOrderElement.getClientOrderId());
//	      			SupplierOrder supplierOrder2 = supplierOrderDao.findByClientOrderId(supplierOrder);
//	      			
//	      			SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
//	      			 supplierOrderElement.setClientOrderElementId(clientOrderElement.getId());
//	      			supplierOrderElement.setSupplierQuoteElementId(elementVo.getSupplierQuoteElementId());
//	      			supplierOrderElement.setPrice(elementVo.getPrice());
//  					supplierOrderElement.setLeadTime("0");
//  					supplierOrderElement.setDeadline(new Date());
//	      			if(orderAmount<=storageAmount){
//      					supplierOrderElement.setAmount(clientOrderElement.getAmount());
//      					full=true;
////      					clientOrderElementVo.setStorageAmount(supplierListVo.getQuoteAmount()-clientOrderElementVo.getClientOrderAmount());
//      					}else{
//      						if(storageAmount>=orderAmount){
//      							supplierOrderElement.setAmount(orderAmount);
//      							full=true;
//      						}else{
//      						supplierOrderElement.setAmount(storageAmount);
//      						orderAmount=orderAmount-storageAmount;
//      						}
//      					}
//	      			
//	      			 
//	      			
//	      			if (supplierOrder2!=null ) {
////  					SupplierOrderElement element=supplierOrderElementDao.selectBySqeIdAndSoId(supplierOrder2.getId(), supplierListVo.getId());
////  					if(null==element){
//  					supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
//  					supplierOrderElementDao.insertSelective(supplierOrderElement);
////  					}
//  					
//  				}else {
//  					supplierOrder.setOrderDate(new Date());
//  					supplierOrder.setCurrencyId(elementVo.getCurrencyId());
//  					supplierOrder.setExchangeRate(elementVo.getExchangeRate());
//  					supplierOrderService.insertSelective(supplierOrder);
//  					supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
//  					supplierOrderElementDao.insertSelective(supplierOrderElement);
//  					
//  				}
//	      			ClientOrderElementVo clientOrderElementVo=new ClientOrderElementVo();
//	      			clientOrderElementVo.setAmount(supplierOrderElement.getAmount());
//	      			clientOrderElementVo.setSupplierId(elementVo.getSupplierId());
//	      			clientOrderElementVo.setCurrencyId(elementVo.getCurrencyId());
//	      			clientOrderElementVo.setExchangeRate(elementVo.getExchangeRate());
//	      			clientOrderElementVo.setElementId(elementVo.getElementId());
//	      			clientOrderElementVo.setPartNumber(elementVo.getPartNumber());
//	      			clientOrderElementVo.setDescription(elementVo.getDescription());
//	      			clientOrderElementVo.setPrice(elementVo.getPrice());
//	      			clientOrderElementVo.setUnit(elementVo.getUnit());
//	      			clientOrderElementVo.setCertificationId(elementVo.getCertificationId());
//	      			clientOrderElementVo.setConditionId(elementVo.getConditionId());
//	      			clientOrderElementVo.setLocation(elementVo.getLocation());
//	      			clientOrderElementVo.setRemark(elementVo.getRemark());
//	      			clientOrderElementVo.setSerialNumber(elementVo.getSerialNumber());
//	      			clientOrderElementVo.setCertificationDate(elementVo.getCertificationDate());
//	      			clientOrderElementVo.setSupplierOrderElementId(supplierOrderElement.getId());
//	      			clientOrderElementVo.setImportPackageElementId(elementVo.getImportPackageElementId());
//	      			clientOrderElementVo.setSoeId(elementVo.getSupplierOrderElementId());
//	      			clientOrderElementVo.setId(elementVo.getClientOrderElementId());
//	      			clientOrderElementVo.setClientQuoteElementId(elementVo.getClientQuoteElementId());
//	      			clientOrderElementVo.setSupplierQuoteElementId(elementVo.getSupplierQuoteElementId());
//	      			clientOrderElementVo.setClientInquiryElementId(elementVo.getClientInquiryElementId());
//	      			clientOrderElementVo.setStorageAmount(elementVo.getAmount());
//	      			splitOrder( clientOrderElementVo );
//	      			if(full){
//	      				break;
//	      			}
//				}
//			}
//		}
//		if(onStorageList.size()>0){
//			
//		}
//		
//		return null;
//	}

	@Override
	public Integer findImportPackageSign(Integer supplierOrderELementId) {
		return importPackageElementDao.findImportPackageSign(supplierOrderELementId);
	}

	@Override
	public List<ImportPackageElement> findAbnormalPart(Integer importPackageId) {
		return importPackageElementDao.findAbnormalPart(importPackageId);
	}

	@Override
	public void abnormalPartRequest(List<ImportPackageElement> elementVos, String ids) {
		for (ImportPackageElement importPackageElement : elementVos) {
			UserVo user= ContextHolder.getCurrentUser();
			
			String processName = "异常件审批";
			String buninessKey = "IMPORT_PACKAGE_ELEMENT.ID."+String.valueOf(importPackageElement.getId());
			
			try{
				
				Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
				variables.put(WorkFlowConstants.START_USER, ContextHolder.getCurrentUser().getUserId());//-- 发起人
				variables.put(WorkFlowConstants.TASK_INFO, processName);//-- 流程信息
				ProcessInstance processInstance = startProcessInstance(ProcessKeys.AbnormalpartProcess.toValue(), buninessKey, variables);//-- 创建过程实例， 
				Task task = findTaskByProcessInstanceId(processInstance.getId());//-- 获取任务
				
				task.setDescription( WFUtils.getDescriptionStr(processName, WFUtils.DESCRIPTION_START) );
				WFUtils.addTaskParticipatingUser(ids, task.getId(), taskService);
	//			WFUtils.addTaskParticipatingUser("1", task.getId(), taskService);
				
				Jbpm4Jbyj jbyj = new Jbpm4Jbyj();
				jbyj.setProcessinstanceId(processInstance.getId());
				jbyj.setTaskId("");
				jbyj.setBusinessKey(buninessKey);
				jbyj.setUserId(user.getUserId());
	//			jbyj.setCreateTime(new Date());
				jbyj.setOutcome("发起");
				jbyj.setJbyj("异常件处理");
				jbyj.setUserName(user.getUserName());
				jbyj.setRoleId(String.valueOf(user.getRoleId()));
				jbyj.setTaskName("异常件确认");// 任务名称
				jbyj.setTaskSzmpy("ycjqr");
				jbyj.setTaskInfoUrl("");
				jbyjDao.insert(jbyj);
				
				ImportPackageElement record=new ImportPackageElement();
				record.setId(importPackageElement.getId());
				record.setSpzt(232);
				importPackageElementDao.updateByPrimaryKeySelective(record);
				
				Jbpm4Task jbpm4Task=new Jbpm4Task();
				jbpm4Task.setYwTableId(importPackageElement.getImportPackageId());
				jbpm4Task.setYwTableElementId(importPackageElement.getId());
				jbpm4Task.setExecutionId(processInstance.getId());
				jbpm4TaskDao.updateByExecutionId(jbpm4Task);
				jbpm4TaskDao.updateJbpm4HistTask(jbpm4Task);
				jbpm4TaskDao.updateJbpm4HistActinst(jbpm4Task);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void tasklistPage(PageModel<ImportPackageElement> page) {
		page.setEntities(importPackageElementDao.tasklistPage(page));		
	}

	@Override
	public List<ImportPackageElementVo> findByLocation() {
		return importPackageElementDao.findByLocation();
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object over(String businessKey,
			String taskId, String outcome, String assignee, String comment){

		ImportPackageElement record=new ImportPackageElement();
		String id=flowService.getIdFromBusinessKey(businessKey);
		record.setId(Integer.parseInt(id));
		record.setSpzt(235);
		record.setApprovalStatus(0);//0转库存
		importPackageElementDao.updateByPrimaryKeySelective(record);
		return null;
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object returnOver(String businessKey,
			String taskId, String outcome, String assignee, String comment){

		ImportPackageElement record=new ImportPackageElement();
		String id=flowService.getIdFromBusinessKey(businessKey);
		record.setId(Integer.parseInt(id));
		record.setSpzt(235);
		record.setApprovalStatus(1);//1退货
		ImportPackageElement element=importPackageElementDao.selectByPrimaryKey(Integer.parseInt(id));
		importPackageElementDao.updateByPrimaryKeySelective(record);
		Jbpm4Task jbpm4Task=new Jbpm4Task();
		jbpm4Task.setYwTableId(element.getImportPackageId());
		List<Jbpm4Task>  jbpm4Tasks=jbpm4TaskDao.findByYwTableId(jbpm4Task);
		if(jbpm4Tasks.size()<2){
			finsh(id,element);
		}
		return null;
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object altOver(String businessKey,
			String taskId, String outcome, String assignee, String comment){

		ImportPackageElement record=new ImportPackageElement();
		String id=flowService.getIdFromBusinessKey(businessKey);
		record.setId(Integer.parseInt(id));
		record.setSpzt(235);
		record.setApprovalStatus(2);//1alt
		importPackageElementDao.updateByPrimaryKeySelective(record);
		ImportPackageElement element=importPackageElementDao.selectByPrimaryKey(Integer.parseInt(id));
		
		Jbpm4Task jbpm4Task=new Jbpm4Task();
		jbpm4Task.setYwTableId(element.getImportPackageId());
		List<Jbpm4Task>  jbpm4Tasks=jbpm4TaskDao.findByYwTableId(jbpm4Task);
		if(jbpm4Tasks.size()<2){
			finsh(id,element);
		}
		return null;
	}
	
	public void finsh(String id,ImportPackageElement element){
		
		List<ImportPackageElementVo> altelementVos=importPackageElementDao.findByApprovalStatus(element.getImportPackageId(), 2);
		if(altelementVos.size()>0){
			UserVo user = ContextHolder.getCurrentUser();
			ExchangeMail exchangeMail = new ExchangeMail();
			UserVo userVo2 = userDao.findUserByUserId(user.getUserId());
			exchangeMail.setUsername(userVo2.getEmail());
			exchangeMail.setPassword(userVo2.getEmailPassword());
			StorageFlowVo  vos=importPackageElementDao.findSupplierIdByIpeId(Integer.parseInt(id));
			
			StringBuffer bodyText = new StringBuffer();
			bodyText.append("<div>Dear ");
		
			bodyText.append("</div>");
			bodyText.append("<div>&nbsp;</div>");
			String realPath = "";
			List<String> to = new ArrayList<String>();
			List<String> cc = new ArrayList<String>();
			List<String> bcc = new ArrayList<String>();
			if(null!=vos.getSupplierId()&&!"".equals(vos.getSupplierId())){
				List<AuthorityRelation> authorityRelations=authorityRelationDao.selectBySupplierId(vos.getSupplierId());
				for (AuthorityRelation authorityRelation : authorityRelations) {
					UserVo userVo3 = userDao.findUserByUserId(authorityRelation.getUserId().toString());
					
					to.add(userVo3.getEmail());
					
					
					List<UserVo> finance = userDao.getByRole("财务");
					for (UserVo userVo4 : finance) {
						if (!to.contains(userVo4.getEmail()) && userVo4.getEmail() != null) {
							to.add(userVo4.getEmail());
						}
					}
					StringBuffer where = new StringBuffer();
					where.append("tr.role_name = '销售' and ip.ID = ").append(vos.getId());
					PageModel<UserVo> page = new PageModel<UserVo>();
					page.put("where", where);
					List<UserVo> market = userDao.getByImportIdAndRoleName(page);
					for (UserVo userVo5 : market) {
						if (!to.contains(userVo5.getEmail()) && userVo5.getEmail() != null) {
							to.add(userVo5.getEmail());
						}
					}
					PageData data = new PageData();
					data.put("loginName", "Michelle");
					List<UserVo> manage = userDao.searchUserByLoginName(data);
					for (UserVo userVo6 : manage) {
						if (!to.contains(userVo6.getEmail()) && userVo6.getEmail() != null) {
							to.add(userVo6.getEmail());
						}
					}
				
					cc.add(userVo2.getEmail());
					String name = userVo3.getUserName();
					bodyText.append(name);
				}
				
					if (true) {
						//表头
						bodyText.append("<div>&nbsp;&nbsp;入库单"+vos.getImportNumber()+"中异常件已确认转为alt，已入库 </div>");
						bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
								+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ "Part No."
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+"Description"
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+"数量"
								+ "</td></tr>"
								);
						for (ImportPackageElementVo importPackageElementVo : altelementVos) {
							bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ importPackageElementVo.getPartNumber()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ importPackageElementVo.getDescription()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ importPackageElementVo.getAmount()
									+ "</td></tr>"
									);
						}
							
						bodyText.append("</table></div>");
						
					}/*else {
						bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
						realPath = "D:/CRM/Files/mis/email/sampleoutput/"+clientInquiry.getQuoteNumber()+".xls";
					}*/
					
					exchangeMail.init();
					try {
						exchangeMail.doSend("SYM异常件转ALT", to, cc, bcc, bodyText.toString(), realPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
		}
		
		List<ImportPackageElementVo> returnelementVos=importPackageElementDao.findByApprovalStatus(element.getImportPackageId(), 1);
		if(returnelementVos.size()>0){
		
			UserVo user = ContextHolder.getCurrentUser();
			ExchangeMail exchangeMail = new ExchangeMail();
			UserVo userVo2 = userDao.findUserByUserId(user.getUserId());
			exchangeMail.setUsername(userVo2.getEmail());
			exchangeMail.setPassword(userVo2.getEmailPassword());
			StorageFlowVo  vos=importPackageElementDao.findSupplierIdByIpeId(Integer.parseInt(id));
		
			if(null!=vos.getSupplierId()&&!"".equals(vos.getSupplierId())){
				List<String> to = new ArrayList<String>();
				
				List<String> cc = new ArrayList<String>();
				List<String> bcc = new ArrayList<String>();
				StringBuffer bodyText = new StringBuffer();
				List<AuthorityRelation> authorityRelations=authorityRelationDao.selectBySupplierId(vos.getSupplierId());
			
				
				for (AuthorityRelation authorityRelation : authorityRelations) {
					UserVo userVo3 = userDao.findUserByUserId(authorityRelation.getUserId().toString());
					to.add(userVo3.getEmail());
					cc.add(userVo2.getEmail());
				}
					
					String realPath = "";
					if (true) {
						//表头
						bodyText.append("<div>&nbsp;&nbsp;入库单"+vos.getImportNumber()+"中异常件已退货，请确认 </div>");
						bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
								+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ "Part No."
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+"Description"
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+"数量"
								+ "</td></tr>"
								);
						for (ImportPackageElementVo importPackageElementVo : returnelementVos) {
							bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ importPackageElementVo.getPartNumber()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ importPackageElementVo.getDescription()
									+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
									+ importPackageElementVo.getAmount()
									+ "</td></tr>"
									);
						}
							
						bodyText.append("</table></div>");
						
					}/*else {
						bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
						realPath = "D:/CRM/Files/mis/email/sampleoutput/"+clientInquiry.getQuoteNumber()+".xls";
					}*/
					
					exchangeMail.init();
					try {
						exchangeMail.doSend("SYM异常件退货", to, cc, bcc, bodyText.toString(), realPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
		}
	}
	
	/**
	 * 发送异常件邮件
	 * **/
	public void abnormalEmail(List<ImportPackageElement> elementVos,String ids,ImportPackage importPackage){

		UserVo user = ContextHolder.getCurrentUser();
		ExchangeMail exchangeMail = new ExchangeMail();
		UserVo userVo2 = userDao.findUserByUserId(user.getUserId());
		exchangeMail.setUsername(userVo2.getEmail());
		exchangeMail.setPassword(userVo2.getEmailPassword());
		List<String> to = new ArrayList<String>();
		List<String> cc = new ArrayList<String>();
		List<String> bcc = new ArrayList<String>();
		String[] id=ids.split(",");
			for (int i = 0; i < id.length; i++) {
				UserVo userVo3 = userDao.findUserByUserId(id[i]);
				to.add(userVo3.getEmail());
				cc.add(userVo2.getEmail());
			}
				StringBuffer bodyText = new StringBuffer();
				
				if (true) {
					//表头
					bodyText.append("<div>&nbsp;&nbsp;入库单"+importPackage.getImportNumber()+"有以下异常件，请确认 </div>");
					bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
							+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+ "Part No."
							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"Description"
							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"数量"
							+ "</td></tr>"
							);
					for (ImportPackageElement importPackageElement : elementVos) {
						bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+ importPackageElement.getPartNumber()
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+ importPackageElement.getDescription()
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+ importPackageElement.getAmount()
										+ "</td></tr>"
										);
					}
					bodyText.append("</table></div>");
					
				}/*else {
					bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
					realPath = "D:/CRM/Files/mis/email/sampleoutput/"+clientInquiry.getQuoteNumber()+".xls";
				}*/
				
				//异常件附件压缩
				String fileName="D:"+File.separator+"CRM"+File.separator+"Files"+File.separator+"mis"+
						File.separator+"upload"+File.separator+importPackage.getImportNumber();
				 String zipFileName ="D:"+File.separator+"CRM"+File.separator+"Files"+File.separator+"mis"+
				File.separator+"upload"+File.separator+importPackage.getImportNumber()+".zip";
				File path=new File(fileName);
				
				    if (path.isDirectory()) {
				    	   File[] files = path.listFiles();
							String[] pathNames=new String[files.length];
				            for (int i = 0; i < files.length; ++i) {
				            	pathNames[i]=files[i].getPath();
				            	}
				        	Compress compress=new Compress(zipFileName);
							compress.compress(pathNames);
				    }
				
				exchangeMail.init();
				try {
					exchangeMail.doSend("SYM异常件邮件", to, cc, bcc, bodyText.toString(), zipFileName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
	}

	@Override
	public List<ImportPackageElementVo> findStorageByElementId(Integer cieElementId, Integer sqeElementId) {
		return importPackageElementDao.findStorageByElementId(cieElementId, sqeElementId);
	}

	@Override
	public List<StorageFlowVo> findStorageBySupplierQuoteElementId(Integer supplierQuoteElementId) {
		return importPackageElementDao.findStorageBySupplierQuoteElementId(supplierQuoteElementId);
	}

	@Override
	public StorageFlowVo findStorageBySoeIdAndIpeId(Integer supplierQuoteElementId, Integer importPackageElementId) {
		return importPackageElementDao.findStorageBySoeIdAndIpeId(supplierQuoteElementId, importPackageElementId);
	}

	@Override
	public Double findsupplierorderImportAmount(Integer supplierOrderELmentId) {
		return importPackageElementDao.findsupplierorderImportAmount(supplierOrderELmentId);
	}
	
	public List<ImportPackagePaymentElementPrepare> getPaymentList(Integer importPackageId){
		return importPackageElementDao.getPaymentList(importPackageId);
	}
	
	public void changeLocation(String input,String aim){
		try {
			String[] inputList = input.split(",");
			List<Integer> importList = new ArrayList<Integer>();
			for (int i = 0; i < inputList.length; i++) {
				if (inputList[i].startsWith("030")) {
					String importId = inputList[i].substring(3, 10);
					String clientOrderElementId = inputList[i].substring(10,17);
					PageModel<ExportPackageElement> page = new PageModel<ExportPackageElement>();
					if(inputList[i].length()>17){
						String seq=inputList[i].substring(17,19);
						Integer sequence = new Integer(seq);
						page.put("sequence", sequence);
					}
//					clientOrderElementId = clientOrderElementId.substring(0, clientOrderElementId.length()-1);
					page.put("importPackageId", importId);
					page.put("clientOrderElementId", clientOrderElementId);
					ExportPackageElement exportPackageElement = exportPackageElementService.getImportPackageElementId(page);
					importList.add(exportPackageElement.getImportPackageElementId());
				}else if (inputList[i].startsWith("040")) {
					String locationId = inputList[i].substring(18);
//					locationId  = locationId.substring(0, locationId.length()-1);
					ImportStorageLocationList importStorageLocationList = importStorageLocationListDao.selectByPrimaryKey(new Integer(locationId));
					List<Integer> importElementIds = importPackageElementDao.getIdByLocation(importStorageLocationList.getLocation());
					for (int j = 0; j < importElementIds.size(); j++) {
						importList.add(importElementIds.get(j));
					}
				}else {
					List<ImportPackageElement> list = importPackageElementDao.selectByLocation(inputList[i].trim());
					for (int j = 0; j < list.size(); j++) {
						importList.add(list.get(j).getId());
					}
				}
			}
			
			String locationId = aim.substring(18);
//			locationId = locationId.substring(0, locationId.length()-1);
			ImportStorageLocationList importStorageLocationList = importStorageLocationListDao.selectByPrimaryKey(new Integer(locationId));
			for (int i = 0; i < importList.size(); i++) {
				ImportPackageElement importPackageElement = new ImportPackageElement();
				importPackageElement.setId(importList.get(i));
				importPackageElement.setLocation(importStorageLocationList.getLocation());
				importPackageElementDao.updateByPrimaryKeySelective(importPackageElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void getUnchangeLocation(PageModel<StorageDetailVo> page,GridSort sort){
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(importPackageElementDao.getUnchangeLocationPage(page));
	}

	@Override
	public Integer findSupplierQuoteElementId(Integer importPackageElementId) {
		return importPackageElementDao.findSupplierQuoteElementId(importPackageElementId);
	}

	@Override
	public StorageFlowVo findImportPackageElementId(Integer supplierQuoteElementId) {
		return importPackageElementDao.findImportPackageElementId(supplierQuoteElementId);
	}

	@Override
	public List<ImportPackageElement> findSign(Integer supplierOrderELementId) {
		return importPackageElementDao.findSign(supplierOrderELementId);
	}
	
	public List<ImportPackageElement> exportLotsExcelElement(PageModel<String> page){
		return importPackageElementDao.exportLotsExcelElement(page);
	}
	
    /*
     * excel上传
     */
    public MessageVo uploadExcel(MultipartFile multipartFile){
    	boolean success = false;
		String message = "保存成功！";
		InputStream fileStream = null;
		try {
			
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			//定义行
		    Row row;
			//记录错误行数
			List<String> errorList = new ArrayList<String>();
			List<ClientInquiryElement> elementList = new ArrayList<ClientInquiryElement>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			int errorLine = 2;
			//错误行数集合
			StringBuffer lines=new StringBuffer();
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            		HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			            String partNum = row.getCell(0).toString().trim();
			            String desc = row.getCell(1).toString();
			            String msn = row.getCell(3).toString();
			            String bsn = tPartDao.getBsnByPartAndMsn(clientInquiryService.getCodeFromPartNumber(partNum), msn.trim());
			            if (bsn == null || "".equals(bsn)) {
							TPart tPart = new TPart();
							tPart.setPartName(desc);
							tPart.setPartNum(partNum);
							tPart.setMsn(msn);
							tPartService.insertSelective(tPart);
						}else {
							String[] descs = desc.split(",");
							TPart tPart = tPartDao.selectByPrimaryKey(bsn);
							String[] d = tPart.getPartName().split(",");
							StringBuffer stringBuffer = new StringBuffer();
							stringBuffer.append(tPart.getPartName());
							for (int j = 0; j < descs.length; j++) {
								boolean exist = false;
								for (int j2 = 0; j2 < d.length; j2++) {
									if (d[j2].trim().toUpperCase().equals(descs[j].trim().toUpperCase())) {
										exist = true;
									}
								}
								if (!exist) {
									stringBuffer.append(",").append(descs[j]);
								}
							}
							tPart.setPartName(stringBuffer.toString());
							tPartDao.updateByPrimaryKeySelective(tPart);
						}
			           
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (errorList.size()>0) {
				lines.append("MSN ");
				for (int i = 0; i < errorList.size(); i++) {
					lines.append(errorList.get(i)).append(",");
				}
				lines.deleteCharAt(lines.length());
				lines.append("DO NOT EXIST！");
				return new MessageVo(false, lines.toString());
			}
			
			if (errorList.size()==0){
				success = true;
				for (int i = 0; i < elementList.size(); i++) {
					clientInquiryElementDao.updateByPrimaryKeySelective(elementList.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			success=false;
			message="save unsuccessful!";
			return new MessageVo(success, message);
		}
		
		return new MessageVo(success, message);
    }
    
    public List<ImportPackageElement> selectByLocation(String location){
    	return importPackageElementDao.selectByLocation(location);
    }
    
    public List<ImportPackageElement> selectByLocationId(Integer locationId){
    	return importPackageElementDao.selectByLocationId(locationId);
    }
    
    public List<Integer> getByLocation(String location,String id){
    	return importPackageElementDao.getByLocation(location,id);
    }
    
    public List<ImportPackageElement> getBySoeId(Integer supplierOrderElementId){
    	return importPackageElementDao.getBySoeId(supplierOrderElementId);
    }
    
    public Double getLockStorageAmount(Integer supplierQuoteElementId,Integer importPackageElementId){
    	return importPackageElementDao.getLockStorageAmount(supplierQuoteElementId, importPackageElementId);
    }
    
    public boolean addStorageCorrelation(StorageCorrelation storageCorrelation){
    	try {
			List<StorageCorrelation> importList = storageCorrelationDao.selectByImportId(storageCorrelation.getImportPackageElementId());
			PageModel<String> page = new PageModel<String>();
			if (importList.size() > 0) {
				page.put("importId", "'%"+storageCorrelation.getCorrelationImportPackageElementId()+"%'");
				List<StorageCorrelation> checkList = storageCorrelationDao.selectByCorrelationId(page);
				if (checkList.size() == 0) {
					StorageCorrelation addVo = importList.get(0);
					addVo.setCorrelationImportPackageElementId(addVo.getCorrelationImportPackageElementId()+","+storageCorrelation.getCorrelationImportPackageElementId());
					storageCorrelationDao.updateByPrimaryKeySelective(addVo);
				}
				return true;
			}
			importList = storageCorrelationDao.selectByImportId(new Integer(storageCorrelation.getCorrelationImportPackageElementId()));
			if (importList.size() > 0) {
				page.put("importId", "'%"+storageCorrelation.getImportPackageElementId()+"%'");
				List<StorageCorrelation> checkList = storageCorrelationDao.selectByCorrelationId(page);
				if (checkList.size() == 0) {
					StorageCorrelation addVo = importList.get(0);
					addVo.setCorrelationImportPackageElementId(addVo.getCorrelationImportPackageElementId()+","+storageCorrelation.getImportPackageElementId());
					storageCorrelationDao.updateByPrimaryKeySelective(addVo);
				}
				return true;
			}
			page.put("importId", "'%"+storageCorrelation.getImportPackageElementId()+"%'");
			importList = storageCorrelationDao.selectByCorrelationId(page);
			if (importList.size() > 0) {
				page.put("importId", "'%"+storageCorrelation.getCorrelationImportPackageElementId()+"%'");
				List<StorageCorrelation> checkList = storageCorrelationDao.selectByCorrelationId(page);
				if (checkList.size() == 0) {
					StorageCorrelation addVo = importList.get(0);
					addVo.setCorrelationImportPackageElementId(addVo.getCorrelationImportPackageElementId()+","+storageCorrelation.getCorrelationImportPackageElementId());
					storageCorrelationDao.updateByPrimaryKeySelective(addVo);
				}
				return true;
			}
			page.clear();
			page.put("importId", "'%"+storageCorrelation.getCorrelationImportPackageElementId()+"%'");
			importList = storageCorrelationDao.selectByCorrelationId(page);
			if (importList.size() > 0) {
				page.put("importId", "'%"+storageCorrelation.getImportPackageElementId()+"%'");
				List<StorageCorrelation> checkList = storageCorrelationDao.selectByCorrelationId(page);
				if (checkList.size() == 0) {
					StorageCorrelation addVo = importList.get(0);
					addVo.setCorrelationImportPackageElementId(addVo.getCorrelationImportPackageElementId()+","+storageCorrelation.getImportPackageElementId());
					storageCorrelationDao.updateByPrimaryKeySelective(addVo);
				}
				return true;
			}
			storageCorrelationDao.insertSelective(storageCorrelation);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    @Override
	public void correlationPage(PageModel<StorageDetailVo> page) {
    	StorageCorrelation storageCorrelation = storageCorrelationDao.getList(page);
    	if (storageCorrelation != null) {
    		StringBuffer str = new StringBuffer();
    		str.append("a.id in (");
    		if (!storageCorrelation.getImportPackageElementId().equals(new Integer(page.get("importId").toString()))) {
    			str.append(storageCorrelation.getImportPackageElementId()).append(",");
			}
    		String[] ids = storageCorrelation.getCorrelationImportPackageElementId().split(",");
    		for (int i = 0; i < ids.length; i++) {
				if (!ids[i].equals(page.get("importId").toString())) {
					str.append(ids[i]).append(",");
				}
			}
    		str.delete(str.length()-1, str.length());
    		str.append(")");
    		page.put("where", str);
    		page.setEntities(importPackageElementDao.StoragePage(page));
		}
	}
    
    public boolean deleteCorrelation(String id){
    	try {
    		PageModel<StorageDetailVo> page = new PageModel<StorageDetailVo>();
        	String[] ids = id.split(",");
        	for (int i = 0; i < ids.length; i++) {
        		page.clear();
        		page.put("importId", ids[i]);
        		page.put("importIdLike", "'%"+ids[i]+"%'");
    			StorageCorrelation storageCorrelation = storageCorrelationDao.getList(page);
    			if (storageCorrelation != null) {
    				String[] importIds = storageCorrelation.getCorrelationImportPackageElementId().split(",");
    				if (importIds.length == 1) {
    					storageCorrelationDao.deleteByPrimaryKey(storageCorrelation.getId());
    				}else {
    					if (storageCorrelation.getImportPackageElementId().toString().equals(ids[i])) {
    						storageCorrelation.setImportPackageElementId(new Integer(importIds[0]));
    						int index = storageCorrelation.getCorrelationImportPackageElementId().indexOf(",");
    						storageCorrelation.setCorrelationImportPackageElementId(storageCorrelation.getCorrelationImportPackageElementId().substring(index+1));
    						storageCorrelationDao.updateByPrimaryKeySelective(storageCorrelation);
    					}else {
    						StringBuffer string = new StringBuffer();
    						for (int j = 0; j < importIds.length; j++) {
    							if (!importIds[j].equals(ids[i])) {
    								string.append(importIds[j]).append(",");
    							}
    						}
    						string.delete(string.length()-1, string.length());
    						storageCorrelation.setCorrelationImportPackageElementId(string.toString());
    						storageCorrelationDao.updateByPrimaryKeySelective(storageCorrelation);
    					}
    				}
    			}
    		}
        	return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public List<StorageDetailVo> getHasLifeStorage(){
    	return importPackageElementDao.getHasLifeStorage();
    }
    
    public void sendEmail(List<ImportPackageElement> elements){
    	try {
    		List<UserVo> users = new ArrayList<UserVo>();//需要权限用户
        	List<UserVo> mustSendUsers = new ArrayList<UserVo>();//默认发送用户
        	List<UserVo> purchases = userDao.getByRole("国内采购");//国内采购用户
        	List<UserVo> abroadPurchasesa = userDao.getByRole("国外采购");//国外采购用户
        	List<UserVo> markets = userDao.getByRole("销售");//销售用户
        	List<UserVo> abroadPurchasesb = userDao.getByRole("采购数据录入员");//采购数据录入员
        	List<UserVo> logistics = userDao.getByRole("国际物流");//国际物流用户
        	List<UserVo> storages = userDao.getByRole("物流");//仓库
        	
        	if (storages != null && storages.size() > 0) {
        		mustSendUsers.addAll(storages);
    		}
        	if (logistics != null && logistics.size() > 0) {
        		mustSendUsers.addAll(logistics);
    		}
    		if (purchases != null && purchases.size() > 0) {
    			users.addAll(purchases);		
    		}
    		if (abroadPurchasesa != null && abroadPurchasesa.size() > 0) {
    			users.addAll(abroadPurchasesa);
    		}
    		if (markets != null && markets.size() > 0) {
    			users.addAll(markets);
    		}
    		if (abroadPurchasesb != null && abroadPurchasesb.size() > 0) {
    			users.addAll(abroadPurchasesb);
    		}
    		
    		ExchangeMail exchangeMail = new ExchangeMail();
    		if (storages != null && storages.size() > 0) {
        		UserVo userVo = userDao.findById(new Integer(storages.get(0).getUserId()));
        		exchangeMail.setUsername(userVo.getEmail());
        		exchangeMail.setPassword(userVo.getEmailPassword());
    		}else if (logistics != null && logistics.size() > 0) {
    			UserVo userVo = userDao.findById(new Integer(logistics.get(0).getUserId()));
        		exchangeMail.setUsername(userVo.getEmail());
        		exchangeMail.setPassword(userVo.getEmailPassword());
    		}
    		
    		if (mustSendUsers.size() > 0) {
    			List<String> emails = new ArrayList<String>();
    			for (UserVo userVo : mustSendUsers) {
    				if (userVo.getEmail() != null && !emails.contains(userVo.getEmail())) {
    					emails.add(userVo.getEmail());
    				}
    			}
    			StringBuffer bodyText = new StringBuffer();
    			//表头
    			bodyText.append("<div>以下件号下个月可用寿命将低于75% </div>");
    			bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
    							+ "<tr><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+ "件号"
    							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"入库单号"
    							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"客户订单号"
    							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"供应商订单号"
    							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"位置"
    							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"下月剩余寿命"
    							+ "</th></tr>"
    							);
    			for (ImportPackageElement importPackageElement : elements) {
    				ImportPackage importPackage = importPackageDao.selectByPrimaryKey(importPackageElement.getImportPackageId());
    				bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    								+ importPackageElement.getPartNumber()
    								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    								+importPackage.getImportNumber()
    								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    								+importPackageElement.getClientOrderNumber()
    								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    								+importPackageElement.getSupplierOrderNumber()
    								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    								+importPackageElement.getLocation()
    								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    								+importPackageElement.getRestLifeNextMonth()+"%"
    								+ "</td></tr>"
    								);
    			}
    			bodyText.append("</table>");
    			exchangeMail.init();
    			try {
    				exchangeMail.doSend("库存寿命提醒", emails, new ArrayList<String>(), new ArrayList<String>(), bodyText.toString(), "");
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		
    		for (UserVo userVo : users) {
    			if (userVo.getEmail() != null && !"".equals(userVo.getEmail())) {
    				List<String> emails = new ArrayList<String>();
        			emails.add(userVo.getEmail());
        			List<RoleVo> list = userDao.searchRoleByUserId(userVo.getUserId());
        			StringBuffer bodyText = new StringBuffer();
        			boolean in = false;
        			//表头
        			bodyText.append("<div>以下件号下个月可用寿命将低于75% </div>");
        			bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
        							+ "<tr><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
        							+ "件号"
        							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
        							+"入库单号"
        							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
        							+"客户订单号"
        							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
        							+"供应商订单号"
        							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
        							+"位置"
        							+ "</th><th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
        							+"下月剩余寿命"
        							+ "</th></tr>"
        							);
        			for (ImportPackageElement importPackageElement : elements) {
        				ImportPackage importPackage = importPackageDao.selectByPrimaryKey(importPackageElement.getImportPackageId());
        				Integer clientId = supplierImportElementDao.getClientByImportElementId(importPackageElement.getId());
        				Integer checkPower = 0;
        				if (list.size() > 0) {
        					if (list.get(0).getRoleName().indexOf("采购") >= 0) {
        						checkPower = authorityRelationDao.checkPowerBySupplier(new Integer(userVo.getUserId()), importPackage.getSupplierId());
							}else if (list.get(0).getRoleName().indexOf("销售") >= 0) {
								checkPower = authorityRelationDao.checkPowerByClient(new Integer(userVo.getUserId()), clientId);
							}
        					//checkPower = authorityRelationDao.checkPowerByClientOrSupplier(new Integer(userVo.getUserId()), importPackage.getSupplierId(), clientId);
            				if (checkPower != null && checkPower > 0) {
            					in = true;
            					bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
            							+ importPackageElement.getPartNumber()
            							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
            							+importPackage.getImportNumber()
            							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
            							+importPackageElement.getClientOrderNumber()
            							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
            							+importPackageElement.getSupplierOrderNumber()
            							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
            							+importPackageElement.getLocation()
            							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
            							+importPackageElement.getRestLifeNextMonth()+"%"
            							+ "</td></tr>"
            							);
            					
            				}
						}
        			}
        			bodyText.append("</table>");
        			if (in) {
        				exchangeMail.init();
        				try {
        					exchangeMail.doSend("库存寿命提醒", emails, new ArrayList<String>(), new ArrayList<String>(), bodyText.toString(), "");
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        			}
				}
    		}
    		for (ImportPackageElement element : elements) {
    			ImportPackageElement importPackageElement = new ImportPackageElement();
    			importPackageElement.setId(element.getId());
    			importPackageElement.setRestLifeEmail(1);
    			importPackageElementDao.updateByPrimaryKeySelective(importPackageElement);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public List<ImportPackageElementVo> getLocationInUse(){
    	return importPackageElementDao.getLocationInUse();
    }
    
    public ResultVo updateLocation(String[] ids,String location){
    	try {
    		ImportPackageElement importPackageElement = new ImportPackageElement();
    		importPackageElement.setLocation(location);
			for (int i = 0; i < ids.length; i++) {
				importPackageElement.setId(new Integer(ids[i]));
				importPackageElementDao.updateByPrimaryKeySelective(importPackageElement);
			}
			return new ResultVo(true, "修改完成！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
    }
    
    public ImportPackageElement getImportPackageElementByLocationAndCoeId(PageModel<ImportPackageElement> page){
    	return importPackageElementDao.getImportPackageElementByLocationAndCoeId(page);
    }
    
    public void missionComplete(ImportPackage importPackage){
    	List<SupplierOrderElement> list = supplierOrderElementDao.selectByImportPackageId(importPackage.getId());
    	for (int i = 0; i < list.size(); i++) {
    		list.get(i).setElementStatusId(710);
    		supplierOrderElementDao.updateByPrimaryKeySelective(list.get(i));
		}
    }
    
    public List<ImportPackageElementVo> getRestLocation(){
    	return importPackageElementDao.getRestLocation();
    }

}
