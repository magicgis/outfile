package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.AuthorityRelationDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientInvoiceDao;
import com.naswork.dao.ClientInvoiceElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientOrderElementFinalDao;
import com.naswork.dao.ClientOrderElementNotmatchDao;
import com.naswork.dao.ClientOrderElementUploadDao;
import com.naswork.dao.ClientProfitmarginDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ClientWeatherOrderDao;
import com.naswork.dao.ClientWeatherOrderElementDao;
import com.naswork.dao.CrmStockDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.HierarchicalRelationshipDao;
import com.naswork.dao.HistoricalOrderPriceDao;
import com.naswork.dao.HistoricalQuotationDao;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.OnPassageStorageDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.SendBackFlowMessageDao;
import com.naswork.dao.StorageToOrderEmailDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SupplierWeatherOrderDao;
import com.naswork.dao.SupplierWeatherOrderElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementFinal;
import com.naswork.model.ClientOrderElementUpload;
import com.naswork.model.ClientProfitmargin;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.CrmStock;
import com.naswork.model.ExchangeRate;
import com.naswork.model.HistoricalOrderPrice;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.model.OrderBankCharges;
import com.naswork.model.SendBackFlowMessage;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrder;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.module.task.controller.orderapproval.OrderApprovalVo;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.ProcessKeys;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientInvoiceService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.ContractReviewService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.FlowService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.service.OrderBankChargesService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.UserService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Service("clientOrderElementService")
public class ClientOrderElementServiceImpl  extends FlowServiceImpl implements ClientOrderElementService {
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ClientOrderElementUploadDao clientOrderElementUploadDao;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private SupplierOrderDao supplierOrderDao;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private UserDao userDao;
	@Resource
	private ClientInvoiceDao clientInvoiceDao;
	@Resource
	private ClientInvoiceElementDao clientInvoiceElementDao;
	@Resource
	private OnPassageStorageDao onPassageStroageDao;
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Resource
	private HistoricalQuotationDao historicalQuotationDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ImportPackageDao importPackageDao;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private FlowService flowService;
	@Resource
	private SupplierWeatherOrderElementDao supplierWeatherOrderElementDao;
	@Resource
	private ClientOrderElementFinalDao clientOrderElementFinalDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientWeatherOrderElementDao clientWeatherOrderElementDao;
	@Resource
	private ClientWeatherOrderDao clientWeatherOrderDao;
	@Resource
	private ContractReviewService contractReviewService;
	@Resource
	private OrderBankChargesService orderBankChargesService;
	@Resource
	private ClientQuoteDao clientQuoteDao;
	@Resource
	private CrmStockDao crmStockDao;
	@Resource
	private ClientOrderElementNotmatchDao clientOrderElementNotmatchDao;
	@Resource
	private HierarchicalRelationshipDao hierarchicalRelationshipDao;
	@Resource
	private HistoricalOrderPriceDao historicalOrderPriceDao;
	@Resource
	private  ClientInvoiceService clientInvoiceService;
	@Resource
	private ClientProfitmarginDao  clientProfitmarginDao;
	@Resource
	private SupplierWeatherOrderDao supplierWeatherOrderDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ExchangeRateService exchangeRateService;
	@Resource
	private StorageToOrderEmailDao storageToOrderEmailDao;
	@Resource
	private ClientService clientService;
	@Resource
	private SendBackFlowMessageDao sendBackFlowMessageDao;
	@Resource
	private UserService userService;
	
	
	@Override
	public ClientOrderElement selectByPrimaryKey(Integer id) {
		return clientOrderElementDao.selectByPrimaryKey(id);
	}
	
	/*
     * 列表数据
     */
    public void listPage(PageModel<ClientOrderElementVo> page){
    	page.setEntities(clientOrderElementDao.listPage(page));
    }
    
    /*
     * 订单明细信息
     */
    public List<ClientOrderElementVo> elementList(Integer id) {
		return clientOrderElementDao.list(id);
	}
    
    
    /*
     * 新增页面显示数据
     */
    public ClientOrderElementVo findByClientQuoteElementId(Integer id){
    	return clientOrderElementDao.findByClientQuoteElementId(id);
    }
    
    public void insertSelective(ClientOrderElement clientOrderElement){
    	clientOrderElementDao.insertSelective(clientOrderElement);
    }
    
    /*
     * 新增明细
     */
    public void insertSelective(List<ClientOrderElement> list,Integer clientOrderId,String userId){
    	List<EmailVo> emailVos=new ArrayList<EmailVo>();
    	ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderId);

    	for (int i = 0; i < list.size(); i++) {
    		if (list.get(i).getAmount()!=null) {
    			Integer leadtime = new Integer(list.get(i).getLeadTime());
    			Calendar calendar = Calendar.getInstance();
    			calendar.setTime(clientOrder.getOrderDate());
    			calendar.add(Calendar.DATE, new Integer(leadtime));
    			list.get(i).setDeadline(calendar.getTime());
    			list.get(i).setClientOrderId(clientOrderId);
    			list.get(i).setUpdateTimestamp(new Date());
        	    ClientQuoteElementVo clientQuoteElementVo=clientQuoteElementDao.findClientInuqiry(list.get(i).getClientQuoteElementId());
        	    if(null!=clientQuoteElementVo){
        	    	list.get(i).setDescription(clientQuoteElementVo.getDescription());
        	    }
        	    //list.get(i).setElementStatusId(704);
        	    if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() > 0) {
        	    	list.get(i).setElementStatusId(709);
				}else {
					list.get(i).setElementStatusId(708);
				}
    			clientOrderElementDao.insertSelective(list.get(i));
    			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(list.get(i).getClientQuoteElementId());
        		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
        		//OnPassageStorage onPassageStorage = onPassageStroageDao.selectByCoeId(clientOrderElement.getId());
        		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
					if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703 || 
							clientInquiryElement.getElementStatusId().equals(711) || clientInquiryElement.getElementStatusId()==711){
						clientInquiryElement.setElementStatusId(710);
						clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
        			}
    			}
    			
//    			ClientOrderElementVo  clientOrderElementVo=clientOrderElementDao.findByclientOrderELementId(clientOrderElement.getId());
    			Double storageAmount =0.0;
    			Integer cieElementId = supplierOrderElementDao.getElementId(list.get(i).getClientQuoteElementId());
    			Integer sqeElementId = supplierOrderElementDao.getSqeElementId(list.get(i).getClientQuoteElementId());
    			List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
    			List<ImportPackageElementVo> elementVos=importpackageElementService.findStorageByElementId(cieElementId, sqeElementId);
    			 for (ImportPackageElementVo importPackageElementVo : elementVos) {
    				 List<StorageFlowVo> flowVos=importpackageElementService.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
    				 	if(flowVos.size()>0){
    				 		for (StorageFlowVo storageFlowVo : flowVos) {
								if(storageFlowVo.getStorageAmount()>0){
									 Double useamount=orderApprovalDao.useStorageAmout(storageFlowVo.getId(), storageFlowVo.getImportPackageElementId());
									 storageFlowVo.setStorageAmount(storageFlowVo.getStorageAmount()-useamount);
									 storageAmount+=storageFlowVo.getStorageAmount();
									supplierList.add(storageFlowVo);
								}
								if(storageAmount>list.get(i).getAmount()){
									break;
								}
							}
    				 	}
    			 }
    			 
    			//获取在途库存件号信息
//    			 Double onPassageAmount = 0.0;
//     			List<OnPassageStorage> onPassageStroages = new ArrayList<OnPassageStorage>();
//     			List<SupplierListVo> onPassageList = supplierOrderElementDao.getOnPassagePartNumber(cieElementId, sqeElementId);
//     			for (SupplierListVo supplierListVo : onPassageList) {
//     				PageModel<OnPassageStorage> onPassagePage = new PageModel<OnPassageStorage>();
//     				onPassagePage.put("where", "sqe.id = "+supplierListVo.getId());
// 					List<OnPassageStorage> onPassageStroage = onPassageStroageDao.selectBySupplierQuoteElementId(onPassagePage);
// 					for (int i = 0; i < onPassageStroage.size(); i++) {
// 						onPassageAmount += onPassageStroage.get(i).getAmount();
// 						if (onPassageStroage.get(i).getAmount()>=clientOrderElement.getAmount()) {
// 							onPassageStroage.get(i).setBiggerAmount(1);
// 						}
// 						onPassageStroages.add(onPassageStroage.get(i));
// 					}
// 				} 
    			
    				if(null!=storageAmount&&storageAmount>0&&storageAmount>=list.get(i).getAmount()){
    					List<EmailVo> emailVo=new ArrayList<EmailVo>();
    					 emailVo=Storage(supplierList,list.get(i),clientOrder);
    					 emailVos.addAll(emailVo);
    	    		}	//在途库存
    				/*else if (null!=onPassageAmount && onPassageAmount>0 && onPassageAmount>=clientOrderElement.getAmount() && onPassageStroages.size()>0) {
    			useOnPassageStorage(onPassageStroages,clientOrderElement);
			}*//**/
			}
    	
    	}
    	
    	for (int j = 0; j < list.size(); j++) {
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(list.get(j).getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
			if (clientInquiryElement.getBsn() != null && !"".equals(clientInquiryElement.getBsn())) {
				Calendar now = Calendar.getInstance();
				String year = String.valueOf(now.get(Calendar.YEAR)).substring(2);
				List<HistoricalOrderPrice> his = historicalOrderPriceDao.getByClient(clientInquiry.getClientId().toString(), clientInquiryElement.getBsn(),year);
				if (his.size() > 0) {
					for (int i = 0; i < his.size(); i++) {
						his.get(i).setPrice(list.get(j).getPrice());
						his.get(i).setAmount(list.get(j).getAmount());
						historicalOrderPriceDao.updateByPrimaryKeySelective(his.get(i));
					}
				}else {
					HistoricalOrderPrice historicalOrderPrice = new HistoricalOrderPrice();
					historicalOrderPrice.setBsn(clientInquiryElement.getBsn());
					historicalOrderPrice.setClientId(clientInquiry.getClientId());
					historicalOrderPrice.setAmount(list.get(j).getAmount());
					historicalOrderPrice.setPrice(list.get(j).getPrice());
					historicalOrderPrice.setYear(new Integer(year));
					historicalOrderPriceDao.insertSelective(historicalOrderPrice);
				}
			}
		}
    	sendEmail(emailVos,userId);
    	
    }
    
    public List<EmailVo> Storage(List<StorageFlowVo> supplierList,ClientOrderElement clientOrderElement,ClientOrder clientOrder){
    	List<EmailVo> emailVos=new ArrayList<EmailVo>();
    	Double count=clientOrderElement.getAmount();
		boolean full=false;
    	  for (StorageFlowVo supplierListVo : supplierList) {
  			  //0091 1000002 1000 1000593
//  			  Integer supplierId=supplierListVo.getSupplierId();
  			//0091 1000不做转换供应商
//  			  if(supplierId.equals(1000002)||supplierId==1000002||supplierId.equals(1000593)||supplierId==1000593){
//  				
//      			}else{
//      				List<AuthorityRelation> authorityRelations=authorityRelationDao.selectBySupplierId(supplierId);
//      					List<RoleVo> roleVos=userDao.searchRoleByUserId(authorityRelations.get(0).getUserId().toString());
//      					
//      				if(roleVos.get(0).getRoleName().equals("国内采购")){
//							try {
//								supplierListVo.setSupplierId(1000002);
//								supplierListVo.setSupplierCode("0091");
//								Supplier supplier=supplierDao.findByCode("0091");
//    	      					supplierListVo.setCurrencyId(supplier.getCurrencyId());
//    	      					supplierListVo.setExchangeRate(supplier.getRate());
//    	      					supplierListVo.setAmount(0.0);
//								importpackageElementService.Storage( supplierListVo);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//      					
//      					
//      				}else if(roleVos.get(0).getRoleName().equals("国外采购")){
//      					try {
//							supplierListVo.setSupplierId(1000593);
//							supplierListVo.setSupplierCode("1000");
//							Supplier supplier=supplierDao.findByCode("1000");
//	      					supplierListVo.setCurrencyId(supplier.getCurrencyId());
//	      					supplierListVo.setExchangeRate(supplier.getRate());
//	      					supplierListVo.setAmount(0.0);
//							importpackageElementService.Storage( supplierListVo);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//  					
//      				}
//      			}
  			SupplierOrder supplierOrder=new SupplierOrder();
  			supplierOrder.setSupplierId(supplierListVo.getSupplierId());
  			supplierOrder.setClientOrderId(clientOrderElement.getClientOrderId());
  			supplierOrder.setOrderType(1);//标记为库存订单
  			SupplierOrder supplierOrder2 = supplierOrderDao.findByClientOrderId(supplierOrder);
  			
  				
  				 SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
  				 
  				 supplierOrderElement.setClientOrderElementId(clientOrderElement.getId());
  				 supplierOrderElement.setSupplierQuoteElementId(supplierListVo.getId());
  				 	if (supplierListVo.getUseAmount() != null) {
  				 		supplierOrderElement.setAmount(supplierListVo.getUseAmount());
					}else {
						if(clientOrderElement.getAmount()<=supplierListVo.getStorageAmount()){
	  						supplierOrderElement.setAmount(count);
	  						full=true;
	  					}else{
	  						if(supplierListVo.getStorageAmount()>=count){
	  							supplierOrderElement.setAmount(count);
	  							full=true;
	  						}else{
	  						supplierOrderElement.setAmount(supplierListVo.getStorageAmount());
	  						count=count-supplierListVo.getStorageAmount();
	  						}
	  					}
					}
  					supplierOrderElement.setPrice(supplierListVo.getPrice());
  					supplierOrderElement.setLeadTime("0");
  					supplierOrderElement.setDeadline(new Date());
  					boolean replenishment=true;
  					if(null!=clientOrder.getReplenishment() ){
  						if("off".equals(clientOrder.getReplenishment())){
  							replenishment=true;
  						}else if("on".equals(clientOrder.getReplenishment())){
  							replenishment=false;
  						}
  					}
  				if (supplierOrder2!=null&&null!=supplierOrder2.getOrderType()&&replenishment) {
  					supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
  					int amount = supplierOrderElementDao.getOrderCount(supplierOrder2.getId());
					if (amount == 0) {
						supplierOrderElement.setItem(1);
					}else {
						supplierOrderElement.setItem(amount+1);
					}
  					supplierOrderElementDao.insertSelective(supplierOrderElement);
  				}else {
  					supplierOrder.setOrderType(1);//标记为库存订单
  					supplierOrder.setOrderDate(new Date());
  					supplierOrder.setCurrencyId(supplierListVo.getCurrencyId());
  					supplierOrder.setExchangeRate(supplierListVo.getExchangeRate());
  					supplierOrderService.insertSelective(supplierOrder);
  					supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
  					int amount = supplierOrderElementDao.getOrderCount(supplierOrder.getId());
					if (amount == 0) {
						supplierOrderElement.setItem(1);
					}else {
						supplierOrderElement.setItem(amount+1);
					}
  					supplierOrderElementDao.insertSelective(supplierOrderElement);
  				}
  				
  				supplierListVo.setAmount(supplierOrderElement.getAmount());
				supplierListVo.setSupplierOrderElementId(supplierOrderElement.getId());
			
				
				String importpackNumber=importpackageElementService.splitOrder(supplierListVo);
				List<ClientOrderElementVo> clientOrderElementVos=clientOrderDao.findSupplierId(clientOrderElement.getId());
				
				EmailVo emailVo=new EmailVo();
				if(null!=clientOrderElementVos.get(0).getSupplierId()){
					emailVo.setSupplierId(clientOrderElementVos.get(0).getSupplierId());					
				}
				Integer clientId=clientOrderElementDao.findClientIdByCoeId(supplierOrderElement.getClientOrderElementId());
				emailVo.setClientId(clientId);
				emailVo.setNowImportpackNumber(importpackNumber);
				emailVo.setOldImportpackNumber(supplierListVo.getImportNumber());
				emailVo.setPartNumber(supplierListVo.getPartNumber());
				emailVo.setDescription(supplierListVo.getDescription());
				emailVo.setOrderNumber(clientOrder.getOrderNumber());
				emailVos.add(emailVo);
      			if(full){
      				break;
      			}
				
  			  }
		return emailVos;
    }
    
    public void sendEmail(List<EmailVo> emailVos,String  userId){
    	if(emailVos.size()>0){
        	//发送邮件
    		//for (int i = 0; i < supplierList.size(); i++) {
    		ExchangeMail exchangeMail = new ExchangeMail();
    		UserVo userVo2 = userDao.findUserByUserId(userId);
    		exchangeMail.setUsername(userVo2.getEmail());
    		exchangeMail.setPassword(userVo2.getEmailPassword());
    		List<UserVo> roleName=userDao.searchEmailByRoleName();
    	
    				List<String> to = new ArrayList<String>();
    				List<String> cc = new ArrayList<String>();
    				List<String> bcc = new ArrayList<String>();
    				cc.add(userVo2.getEmail());
    				if(null!=emailVos.get(0).getSupplierId()){
    					for (int i = 0; i < emailVos.size(); i++) {
	    					List<AuthorityRelation> authorityRelations=authorityRelationDao.selectBySupplierId(emailVos.get(i).getSupplierId());
	    					for (AuthorityRelation authorityRelation : authorityRelations) {
	    						UserVo userVo3 = userDao.findUserByUserId(authorityRelation.getUserId().toString());
	    						if(null!=userVo3.getEmail()&&!"".equals(userVo3.getEmail())&&!cc.contains(userVo3.getEmail())){
	    							cc.add(userVo3.getEmail());
	    						}
							}
    					}
    				}
    				if(null!=emailVos.get(0).getClientId()){
    					List<AuthorityRelation> authorityRelations=authorityRelationDao.selectByClientId(emailVos.get(0).getClientId());
    					for (AuthorityRelation authorityRelation : authorityRelations) {
    						UserVo userVo3 = userDao.findUserByUserId(authorityRelation.getUserId().toString());
//    						List<HierarchicalRelationship> relationships=hierarchicalRelationshipDao.selectByUserId(authorityRelation.getUserId().toString());
//    						if(relationships.size()>0){
//    							for (HierarchicalRelationship hierarchicalRelationship : relationships) {
//    								if(!cc.contains("sales09@betterairgroup.com")&&!exchangeMail.equals("liana@betterairgroup.com")){
//    	    							cc.add("sales09@betterairgroup.com");
//    	    						}
//    							}
//    						}
    						if(null!=userVo3.getEmail()&&!"".equals(userVo3.getEmail())&&!cc.contains(userVo3.getEmail())){
    							cc.add(userVo3.getEmail());
    						}
						}
    				}
    				
    				StringBuffer bodyText = new StringBuffer();
    				for (int j = 0; j < roleName.size(); j++) {
        				to.add(roleName.get(j).getEmail());
        				String name = roleName.get(j).getUserName();
        				bodyText.append("<div>Dear ");
        				bodyText.append(name);
        				bodyText.append("</div>");
        				bodyText.append("<div>&nbsp;</div>");
    				}
    			
    				String realPath = "";
    				if (true) {
    					//表头
    					bodyText.append("<div>&nbsp;&nbsp;现有客户订单"+emailVos.get(0).getOrderNumber()+"中如下项目，自有库存转订单 </div>");
    					bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
    							+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+ "Part No."
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"Description"
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"转订单入库单号"
    							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    							+"库存入库单号"
    							+ "</td></tr>"
    							);
    					for (int k = 0; k < emailVos.size(); k++) {
    						bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getPartNumber()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getDescription()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getNowImportpackNumber()
    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
    										+ emailVos.get(k).getOldImportpackNumber()
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
    					exchangeMail.doSend("SYM库存转订单", to, cc, bcc, bodyText.toString(), realPath);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    		
        	}
    }
    
    public int updateByPrimaryKeySelective(ClientOrderElement record){
    	return clientOrderElementDao.updateByPrimaryKeySelective(record);
    }
    
    /*
     * excel上传
     */
	public MessageVo UploadExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId){
		boolean success = true;
		String message = "保存成功！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		UserVo userVo=ContextHolder.getCurrentUser();
		boolean descNotMatch=false;
		try {
			List<ListDateVo> cert=supplierQuoteDao.findcert();
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			 //定义行
		    Row row;
			//错误行数集合
		    List<ClientOrderElement> errorList = new ArrayList<ClientOrderElement>();
			StringBuffer lines=new StringBuffer();
			
		    //记录行数
			Integer flag = 2;
			
			//询价item
			List<ClientOrderElementVo> list = clientOrderElementDao.findItem(clientOrderId);
			List<ClientOrderElement> entityList = new ArrayList<ClientOrderElement>();
			List<ClientOrderElementVo> unMatch = new ArrayList<ClientOrderElementVo>();
			ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderId);
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
				int a = sheet.getPhysicalNumberOfRows();
				int ifCorrect = 0;
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            	Integer number =new Double(row.getCell(0).toString()).intValue();
		            Integer item = new Double(row.getCell(1).toString()).intValue();
		            //String partNumber = row.getCell(2).toString().trim();
		            Cell oneCell = row.getCell(2);
		            String partNumber = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
						partNumber = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					    partNumber = dataFormatter.formatCellValue(oneCell);
					}
		            String description = row.getCell(3).toString();
		            String unit = row.getCell(4).toString();
		            Double amount = new Double(row.getCell(5).toString());
		            Double price = new Double(row.getCell(6).toString());
		            String leadTime = row.getCell(8).toString();
		            String remark = "";
		            if(null!=row.getCell(10)){
		            	remark=row.getCell(10).toString();
		            }
		            String certificationCode = "";
		            if(null!=row.getCell(11)){
		            	  certificationCode=row.getCell(11).toString();
		            }
		           
		            Integer certificationId=null;
		            for (ListDateVo listDateVo : cert) {
						if(listDateVo.getCode().equals(certificationCode)){
							certificationId= listDateVo.getId();
						}
						
					}
		            Double fixedCost=0.0;
		            if(null!=row.getCell(12)&&!"".equals(row.getCell(12)+"")){
		            	fixedCost=new Double(row.getCell(12).toString());
		            }
		            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		            String  dateString = sdf.format(HSSFDateUtil.getJavaDate(row.getCell(9).getNumericCellValue())).toString();
		            Date deadline = sdf.parse(dateString);*/
		            Calendar calendar = Calendar.getInstance();
	    			calendar.setTime(clientOrder.getOrderDate());
	    			calendar.add(Calendar.DATE, new Double(leadTime).intValue());
	    			Date deadline = calendar.getTime();
		            for (ClientOrderElementVo clientOrderElementVo : list) {
		            	/*if(clientOrderElementVo.getItem().equals(1034)){
		            		System.out.println();		
		            	}*/
		            	/*boolean match = false;
		            	if (!"".equals(clientOrderElementVo.getBsn()) && clientOrderElementVo.getBsn() != null) {
							TPart tPart = tPartDao.selectByPrimaryKey(clientOrderElementVo.getBsn());
							String[] dec = tPart.getDescription().split(",");
							for (int j = 0; j < dec.length; j++) {
								if (dec[j].equals(description)) {
									match = true;
									break;
								}
							}
							if (match) {
								if (number.equals(clientOrderElementVo.getItem())&&partNumber.equals(clientOrderElementVo.getInquiryPartNumber().trim())) {
									 ClientOrderElement clientOrderElement = new ClientOrderElement(clientOrderId, clientOrderElementVo.getId(), amount, price, leadTime, deadline, new Date(),partNumber,certificationId,fixedCost);
							         entityList.add(clientOrderElement);
							         ifCorrect = 1;
							         break;
								}
							}else {
								unMatch.add(e)
							}
						}else {
							if (match) {*/
								if (number.equals(clientOrderElementVo.getItem())&&partNumber.equals(clientOrderElementVo.getInquiryPartNumber().trim())) {
									 ClientOrderElement clientOrderElement = new ClientOrderElement(clientOrderId, clientOrderElementVo.getId(), amount, price, leadTime, deadline, new Date(),partNumber,certificationId,fixedCost,remark);
									 clientOrderElement.setItem(item);
									 clientOrderElement.setDescription(description);
									 entityList.add(clientOrderElement);
							         ClientQuoteElementVo clientQuoteElementVo=clientQuoteElementDao.findClientInuqiry(clientOrderElementVo.getId());
							         if(null!=clientQuoteElementVo&&null!=clientQuoteElementVo.getBsn()){
							        	 CrmStock crmStock=crmStockDao.findByBsn(clientQuoteElementVo.getBsn());
							        	 if (crmStock != null) {
							        		 String desc=crmStock.getPartName().toUpperCase();
								        	 description=description.toUpperCase();
								        	 if(desc.indexOf(description)<=-1){
								        		 clientOrderElement.setBsn(clientQuoteElementVo.getBsn());
								        		 descNotMatch=true;
								        	 }
										}
							         }
							         ifCorrect = 1;
							         break;
								}
						/*	}
						}*/
		            	
						
					}
		           if (ifCorrect==0) {
		            	ClientOrderElement clientOrderElement = new ClientOrderElement();
						clientOrderElement.setUserId(userId);
						clientOrderElement.setItem(number);
						clientOrderElement.setPartNumber(partNumber);
						clientOrderElement.setLine(flag);
						clientOrderElement.setError("S/N或者件号有误！");
						errorList.add(clientOrderElement);
					}
		            flag++;
				}
	            
			}
			
			if (errorList.size()>0) {
				lines.append("Line ");
				for (ClientOrderElement clientOrderElement : errorList) {
					clientOrderElementUploadDao.insertSelective(clientOrderElement);
				}
				success=false;
				messageVo.setFlag(success);
				messageVo.setMessage("新增失败！");
				return messageVo;
			}
			
			if(descNotMatch){
				for (ClientOrderElement clientOrderElement : entityList) {
					clientOrderElement.setUserId(Integer.parseInt(userVo.getUserId()));	
					clientOrderElementNotmatchDao.insert(clientOrderElement);
				}
				success=false;
				messageVo.setFlag(success);
				messageVo.setMessage("dotmatch");
				return messageVo;
			}
			
			if (errorList.size()==0) {
				/*//批量删除明细
				clientOrderElementDao.removeByClientOrderId(clientOrderId);*/
				clientInquiryService.excelBackup(wb, this.fetchOutputFilePath(), this.fetchOutputFileName(), clientOrderId.toString(),
						this.fetchYwTableName(), this.fetchYwTablePkName());
				for (int i = 0; i < entityList.size(); i++) {
					List<ClientOrderElement> checkList = clientOrderElementDao.checkOrderElement(entityList.get(i));
					if (checkList.size() == 0) {
						if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() > 0) {
							entityList.get(i).setElementStatusId(709);
						}else {
							entityList.get(i).setElementStatusId(708);
						}
						//entityList.get(i).setElementStatusId(704);
						clientOrderElementDao.insert(entityList.get(i));
						ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(entityList.get(i).getClientQuoteElementId());
		        		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
		        		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
							if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703|| 
									clientInquiryElement.getElementStatusId().equals(711) || clientInquiryElement.getElementStatusId()==711){
								clientInquiryElement.setElementStatusId(710);
				    			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
							}
		        		}
						//生成预付款发票
						if(clientOrder.getPrepayRate()>0){
							List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrderId,1);
							if(null!=clientInvoices&&clientInvoices.size()>0){
								 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
								 clientInvoiceElement.setAmount(entityList.get(i).getAmount());
								 Double terms=clientOrder.getPrepayRate()*100;
								 clientInvoiceElement.setTerms(terms.intValue());
								 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
								 clientInvoiceElement.setClientOrderElementId(entityList.get(i).getId());
								 clientInvoiceElementDao.insert(clientInvoiceElement);
							}else{
								String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
										"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
								for (int j = 0; j < ziMu.length; j++) {
									ClientInvoice incoiceNumber=clientInvoiceDao.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
									if(null!=incoiceNumber){
										continue;
									}else{
										ClientInvoice clientInvoice=new ClientInvoice();
										clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
										clientInvoice.setClientOrderId(clientOrderId);
										clientInvoice.setInvoiceDate(new Date());
										Double terms=clientOrder.getPrepayRate()*100;
										clientInvoice.setTerms(terms.intValue());
										clientInvoice.setInvoiceType(1);
										clientInvoice.setInvoiceStatusId(0);
										clientInvoiceDao.insert(clientInvoice);
										ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
										clientInvoiceElement.setAmount(entityList.get(i).getAmount());
										clientInvoiceElement.setTerms(terms.intValue());
										clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
										clientInvoiceElement.setClientOrderElementId(entityList.get(i).getId());
										clientInvoiceElementDao.insert(clientInvoiceElement);
										break;
									}
								}
							}
						}	
						
						//生成形式发票
						List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrderId,0);
						if(null!=clientInvoices&&clientInvoices.size()>0){
							 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
							 clientInvoiceElement.setAmount(entityList.get(i).getAmount());
							 clientInvoiceElement.setTerms(100);
							 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
							 clientInvoiceElement.setClientOrderElementId(entityList.get(i).getId());
							 ClientInvoiceElement invoiceElement=clientInvoiceElementDao.selectByCoeIdAndCiId(clientInvoiceElement);
							 if(null==invoiceElement){
								 clientInvoiceElementDao.insert(clientInvoiceElement);
							 }
						}else{
							ClientInvoice clientInvoice=new ClientInvoice();
							clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+"PR");
							clientInvoice.setClientOrderId(clientOrderId);
							clientInvoice.setInvoiceDate(new Date());
							clientInvoice.setTerms(100);
							clientInvoice.setInvoiceType(0);
							clientInvoice.setInvoiceStatusId(0);
							clientInvoiceDao.insert(clientInvoice);
							ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
							clientInvoiceElement.setAmount(entityList.get(i).getAmount());
							clientInvoiceElement.setTerms(100);
							clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
							clientInvoiceElement.setClientOrderElementId(entityList.get(i).getId());
							clientInvoiceElementDao.insert(clientInvoiceElement);
						}
						
						//自动转库存
//						Double storageAmount =0.0;
//		    			Integer cieElementId = supplierOrderElementDao.getElementId(entityList.get(i).getClientQuoteElementId());
//		    			Integer sqeElementId = supplierOrderElementDao.getSqeElementId(entityList.get(i).getClientQuoteElementId());
//		    			List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
//		    			List<ImportPackageElementVo> elementVos=importpackageElementService.findStorageByElementId(cieElementId, sqeElementId);
//		    			 for (ImportPackageElementVo importPackageElementVo : elementVos) {
//		    				 List<StorageFlowVo> flowVos=importpackageElementService.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
//		    				 	if(flowVos.size()>0){
//		    				 		for (StorageFlowVo storageFlowVo : flowVos) {
//										if(storageFlowVo.getStorageAmount()>0){
//											 Double useamount=orderApprovalDao.useStorageAmout(storageFlowVo.getId(), storageFlowVo.getImportPackageElementId());
//											 storageFlowVo.setStorageAmount(storageFlowVo.getStorageAmount()-useamount);
//											storageAmount+=storageFlowVo.getStorageAmount();
//											supplierList.add(storageFlowVo);
//										}
//										if(storageAmount>entityList.get(i).getAmount()){
//											break;
//										}
//									}
//		    				 	}
//		    			 }
		    			
//	    				if(null!=storageAmount&&storageAmount>0&&storageAmount>=entityList.get(i).getAmount()){
//	    					List<EmailVo> emailVo=new ArrayList<EmailVo>();
//	    					 emailVo=Storage(supplierList,entityList.get(i),clientOrder);
//	    					 emailVos.addAll(emailVo);
//	    	    		}
					}
				}
				
				for (int j = 0; j < entityList.size(); j++) {
					ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(entityList.get(j).getClientQuoteElementId());
					ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
					ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
					if (clientInquiryElement.getBsn() != null && !"".equals(clientInquiryElement.getBsn())) {
						Calendar now = Calendar.getInstance();
						String year = String.valueOf(now.get(Calendar.YEAR)).substring(2);
						List<HistoricalOrderPrice> his = historicalOrderPriceDao.getByClient(clientInquiry.getClientId().toString(), clientInquiryElement.getBsn(),year);
						if (his.size() > 0) {
							for (int i = 0; i < his.size(); i++) {
								his.get(i).setPrice(entityList.get(j).getPrice());
								his.get(i).setAmount(entityList.get(j).getAmount());
								historicalOrderPriceDao.updateByPrimaryKeySelective(his.get(i));
							}
						}else {
							HistoricalOrderPrice historicalOrderPrice = new HistoricalOrderPrice();
							historicalOrderPrice.setBsn(clientInquiryElement.getBsn());
							historicalOrderPrice.setClientId(clientInquiry.getClientId());
							historicalOrderPrice.setAmount(entityList.get(j).getAmount());
							historicalOrderPrice.setPrice(entityList.get(j).getPrice());
							historicalOrderPrice.setYear(new Integer(year));
							historicalOrderPriceDao.insertSelective(historicalOrderPrice);
						}
					}
				}
				
				sendEmail(emailVos,userId.toString());
				
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;
			}
			
			
		}catch (Exception e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
			}
			e.printStackTrace();
		}
		return null;
	}
	
	public MessageVo addClientOrder(List<ClientOrderElement>  clientOrderElements){
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		UserVo userVo=ContextHolder.getCurrentUser();
		MessageVo messageVo = new MessageVo();
		boolean success = true;
		String message = "保存成功！";
		ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderElements.get(0).getClientOrderId());
		for (int i = 0; i < clientOrderElements.size(); i++) {
			 ClientQuoteElementVo clientQuoteElementVo=clientQuoteElementDao.findClientInuqiry(clientOrderElements.get(i).getClientQuoteElementId());
	         if(null!=clientQuoteElementVo&&null!=clientQuoteElementVo.getBsn()){
	        	 CrmStock crmStock=crmStockDao.findByBsn(clientQuoteElementVo.getBsn());
	        	 if (crmStock != null) {
	        	 String desc=crmStock.getPartName().toUpperCase();
	        	 String description=clientOrderElements.get(i).getDescription().toUpperCase();
		        	 if(desc.indexOf(description)<=-1){
		        		 desc+=","+description;
		        		 crmStock.setPartName(desc);
		        		 crmStockDao.updateByBsn(crmStock);
		        	 }
	        	 }
	         }
	        if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() < 0) {
	        	clientOrderElements.get(i).setElementStatusId(709);
			}else {
				clientOrderElements.get(i).setElementStatusId(708);
			}
	        //clientOrderElements.get(i).setElementStatusId(704);
			clientOrderElementDao.insertSelective(clientOrderElements.get(i));
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElements.get(i).getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
				if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703|| 
						clientInquiryElement.getElementStatusId().equals(711) || clientInquiryElement.getElementStatusId()==711){
					clientInquiryElement.setElementStatusId(710);
	    			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
				}
			}
			//生成预付款发票
			if(clientOrder.getPrepayRate()>0){
				List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrder.getId(),1);
	//			ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderId);
	//			List<ClientOrderElementVo> clientOrderVo=clientOrderElementDao.findByOrderId(clientOrderId);
				if(null!=clientInvoices&&clientInvoices.size()>0){
	//				for (ClientOrderElementVo elementDataVo : clientOrderVo) {
	//					if (elementDataVo.getClientOrderAmount()!=null) {
						 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
						 clientInvoiceElement.setAmount(clientOrderElements.get(i).getAmount());
						 Double terms=clientOrder.getPrepayRate()*100;
						 clientInvoiceElement.setTerms(terms.intValue());
						 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
						 clientInvoiceElement.setClientOrderElementId(clientOrderElements.get(i).getId());
						 clientInvoiceElementDao.insert(clientInvoiceElement);
	//					}
	//				}
				}else{
					String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
							"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
					for (int j = 0; j < ziMu.length; j++) {
						ClientInvoice incoiceNumber=clientInvoiceDao.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
						if(null!=incoiceNumber){
							continue;
						}else{
							ClientInvoice clientInvoice=new ClientInvoice();
							clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
							clientInvoice.setClientOrderId(clientOrder.getId());
							clientInvoice.setInvoiceDate(new Date());
							Double terms=clientOrder.getPrepayRate()*100;
							clientInvoice.setTerms(terms.intValue());
							clientInvoice.setInvoiceType(1);
							clientInvoice.setInvoiceStatusId(0);
							clientInvoiceDao.insert(clientInvoice);
			//				for (ClientOrderElementVo elementDataVo : clientOrderVo) {
			//					if (elementDataVo.getClientOrderAmount()!=null) {
								 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
								 clientInvoiceElement.setAmount(clientOrderElements.get(i).getAmount());
								 clientInvoiceElement.setTerms(terms.intValue());
								 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
								 clientInvoiceElement.setClientOrderElementId(clientOrderElements.get(i).getId());
								 clientInvoiceElementDao.insert(clientInvoiceElement);
								 break;
			//					}
			//				}
						}
					}
				}
		}	
			
			Double storageAmount =0.0;
			Integer cieElementId = supplierOrderElementDao.getElementId(clientOrderElements.get(i).getClientQuoteElementId());
			Integer sqeElementId = supplierOrderElementDao.getSqeElementId(clientOrderElements.get(i).getClientQuoteElementId());
			List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
			List<ImportPackageElementVo> elementVos=importpackageElementService.findStorageByElementId(cieElementId, sqeElementId);
			 for (ImportPackageElementVo importPackageElementVo : elementVos) {
				 List<StorageFlowVo> flowVos=importpackageElementService.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
				 	if(flowVos.size()>0){
				 		for (StorageFlowVo storageFlowVo : flowVos) {
							if(storageFlowVo.getStorageAmount()>0){
								storageAmount+=storageFlowVo.getStorageAmount();
								supplierList.add(storageFlowVo);
							}
							if(storageAmount>clientOrderElements.get(i).getAmount()){
								break;
							}
						}
				 	}
			 }
			
				if(null!=storageAmount&&storageAmount>0&&storageAmount>=clientOrderElements.get(i).getAmount()){
					List<EmailVo> emailVo=new ArrayList<EmailVo>();
					 emailVo=Storage(supplierList,clientOrderElements.get(i),clientOrder);
					 emailVos.addAll(emailVo);
	    		}
		}
		
		for (int j = 0; j < clientOrderElements.size(); j++) {
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElements.get(j).getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
			if (clientInquiryElement.getBsn() != null && !"".equals(clientInquiryElement.getBsn())) {
				Calendar now = Calendar.getInstance();
				String year = String.valueOf(now.get(Calendar.YEAR)).substring(2);
				List<HistoricalOrderPrice> his = historicalOrderPriceDao.getByClient(clientInquiry.getClientId().toString(), clientInquiryElement.getBsn(),year);
				if (his.size() > 0) {
					for (int i = 0; i < his.size(); i++) {
						his.get(i).setPrice(clientOrderElements.get(j).getPrice());
						his.get(i).setAmount(clientOrderElements.get(j).getAmount());
						historicalOrderPriceDao.updateByPrimaryKeySelective(his.get(i));
					}
				}else {
					HistoricalOrderPrice historicalOrderPrice = new HistoricalOrderPrice();
					historicalOrderPrice.setBsn(clientInquiryElement.getBsn());
					historicalOrderPrice.setClientId(clientInquiry.getClientId());
					historicalOrderPrice.setAmount(clientOrderElements.get(j).getAmount());
					historicalOrderPrice.setPrice(clientOrderElements.get(j).getPrice());
					historicalOrderPrice.setYear(new Integer(year));
					historicalOrderPriceDao.insertSelective(historicalOrderPrice);
				}
			}
		}
		
		sendEmail(emailVos,userVo.getUserId().toString());
		message="save successful!";
		messageVo.setFlag(success);
		messageVo.setMessage(message);
		return messageVo;
	}
	
	/*
	 * 未到货列表
	 */
	public void unFinish(PageModel<ClientOrderElementVo> page,String where,GridSort sort) {
		StringBuffer condition = new StringBuffer();
		if (where!=null && !"".equals(where)) {
			condition.append(" and ").append(where);
			page.put("where", condition.toString());
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(clientOrderElementDao.unFinishPage(page));
	}
	
	public void getUnfinishOrderPage(PageModel<ClientOrderElementVo> page,String where,GridSort sort){
		StringBuffer condition = new StringBuffer();
		if (where!=null && !"".equals(where)) {
			condition.append(" and ").append(where);
			page.put("where", condition.toString());
		}
		if(sort!=null){
			/*sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));*/
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(clientOrderElementDao.getUnfinishOrderPage(page));
	}
	
	/*
     * 路径
     */
    public String fetchOutputFilePath() {
		return FileConstant.EXCEL_BACKUP+File.separator+"sampleoutput";
		
	}
    
    /*
     * 文件名
     */
    public String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		
		return "ClinetOrderELement"+"_"+this.fetchUserName()+"_"+format.format(now);
	}
   
    /*
     * 用户名
     */
    public String fetchUserName(){
		UserVo user = ContextHolder.getCurrentUser();
		if(user!=null){
			return user.getUserName();
		}else{
			return "";
		}
    }

    public String fetchYwTableName() {
		return "client_order_element";
	}
    
    public String fetchYwTablePkName() {
		return "id";
	}
    
    public String fetchMappingKey() {
		return "ClientOrderElementExcel";
	}

	@Override
	public void insert(ClientOrderElement clientOrderElement) {
		clientOrderElementDao.insert(clientOrderElement);
	}
	
	public void findByOrderIdPage(PageModel<ClientOrderElementVo> page,GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(clientOrderElementDao.findByOrderIdPage(page));
	}
	
	public List<ClientOrderElementVo> findByOrderId(Integer id) {
		return clientOrderElementDao.findByOrderId(id);
	}
	
	public void errorList(PageModel<ClientOrderElementUpload> page){
		page.setEntities(clientOrderElementUploadDao.listPage(page));
	}
	
	public void deleteMessage(Integer userId){
		clientOrderElementUploadDao.deleteMessage(userId);
	}

	@Override
	public ClientOrderElementVo findByclientOrderELementId(Integer id) {
		return clientOrderElementDao.findByclientOrderELementId(id);
	}
	
	/*
     *货款到期提醒
     */
    public void getDeadLineOrderPage(PageModel<ClientOrderElementVo> page,String where,GridSort sort){
    	if (where!=null && !"".equals(where)) {
			page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(clientOrderElementDao.getDeadLineOrderPage(page));
    }
    
    
    /*
     * 根据出库明细ID查询
     */
    public ClientOrderElement findByExportPackageElementId(Integer exportPackageElementId){
    	return clientOrderElementDao.findByExportPackageElementId(exportPackageElementId);
    }

	@Override
	public void orderApproval(ClientOrderElement clientOrderElement, String ids,OrderApproval other) {
	   UserVo user= ContextHolder.getCurrentUser();
	   List<OrderApproval> orderApprovalList=new ArrayList<OrderApproval>();
	   List<OrderApproval> noPassStock=orderApprovalDao.selectByCoeIdAndState(clientOrderElement.getId(), 0, 0);
	   List<OrderApproval> passStock=orderApprovalDao.selectByCoeIdAndState(clientOrderElement.getId(), 1, 0);
	   Double storageAmount=orderApprovalDao.findStorageAmount(clientOrderElement.getId(),0);
	   List<OrderApproval> onpassnoPassStock=orderApprovalDao.selectByCoeIdAndState(clientOrderElement.getId(), 0, 1);
	   List<OrderApproval> onpasspassStock=orderApprovalDao.selectByCoeIdAndState(clientOrderElement.getId(), 1, 1);
	   Double onpassstorageAmount=orderApprovalDao.findStorageAmount(clientOrderElement.getId(),1);
	   Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
	   boolean in=false;
//	 Double storageAmount=0.0;
	if(null!=other){
		 variables.put("to", other.getHandle());
		 other.setClientOrderElementId(clientOrderElement.getId());
		 other.setClientOrderId(clientOrderElement.getClientOrderId());
			orderApprovalDao.insert(other);
			orderApprovalList.add(other);
	}else{
	   if(storageAmount>=clientOrderElement.getAmount()){//有库存，利润通过
		  Double amount=0.0;
		   for (OrderApproval orderApproval : passStock) {
			   amount=amount+orderApproval.getAmount();
			   orderApprovalList.add(orderApproval);
			   if(amount>=clientOrderElement.getAmount()){
				   break;
			   }
		   }
		   in=true;
		   variables.put("to", "有库存");
	   }else if(onpassstorageAmount>=clientOrderElement.getAmount()){
		   Double amount=0.0;
		   for (OrderApproval orderApproval : onpasspassStock) {
			   amount=amount+orderApproval.getAmount();
			   orderApprovalList.add(orderApproval);
			   if(amount>=clientOrderElement.getAmount()){
				   break;
			   }
		   }
		   in=true;
		   variables.put("to", "有库存");
	   }else if(noPassStock.size()>0||passStock.size()>0||onpassnoPassStock.size()>0||onpasspassStock.size()>0){//所有库存
		   variables.put("to", "有库存");
		   orderApprovalList.addAll(passStock);
		   orderApprovalList.addAll(noPassStock);
		   orderApprovalList.addAll(onpassnoPassStock);
		   orderApprovalList.addAll(onpasspassStock);
	   }
	   else{
		   variables.put("to", "无库存");
		   OrderApproval orderApproval=new OrderApproval();
		   orderApproval.setClientOrderElementId(clientOrderElement.getId());
		   orderApproval.setClientOrderId(clientOrderElement.getClientOrderId());
			orderApprovalDao.insert(orderApproval);
			orderApprovalList.add(orderApproval);
	   }
	}
	Double approvalAmount=0.0;
	   for (OrderApproval orderApproval : orderApprovalList) {
	   
		String processName = "合同审批";
		String buninessKey = "ORDER_APPROVAL.ID."+String.valueOf(orderApproval.getId());
		
		try{
			if(null!=other&&null!=other.getUserId()){
			variables.put(WorkFlowConstants.START_USER, other.getUserId());//-- 发起人
			}else{
				variables.put(WorkFlowConstants.START_USER, ContextHolder.getCurrentUser().getUserId());//-- 发起人
			}
			variables.put(WorkFlowConstants.TASK_INFO, processName);//-- 流程信息
			  
			   
			ProcessInstance processInstance = startProcessInstance(ProcessKeys.ContractreviewProcess.toValue(), buninessKey, variables);//-- 创建过程实例， 
			Task task = findTaskByProcessInstanceId(processInstance.getId());//-- 获取任务
			if(null!=other&&null!=other.getDesc()){
				task.setDescription(other.getDesc());
			}else{
			task.setDescription( WFUtils.getDescriptionStr(processName, WFUtils.DESCRIPTION_START) );
			}
			WFUtils.addTaskParticipatingUser(ids, task.getId(), taskService);
			
			Jbpm4Jbyj jbyj = new Jbpm4Jbyj();
			jbyj.setProcessinstanceId(processInstance.getId());
			jbyj.setTaskId("");
			jbyj.setBusinessKey(buninessKey);
			jbyj.setUserId(user.getUserId());
//			jbyj.setCreateTime(new Date());
			jbyj.setOutcome("发起");
			jbyj.setJbyj("合同评审");
			jbyj.setUserName(user.getUserName());
			jbyj.setRoleId(String.valueOf(user.getRoleId()));
			jbyj.setTaskName("合同评审");// 任务名称
			jbyj.setTaskSzmpy("htps");
			jbyj.setTaskInfoUrl("");
			jbyjDao.insert(jbyj);
			
			ClientOrderElement record=new ClientOrderElement();
			record.setId(clientOrderElement.getId());
			record.setSpzt(232);
			clientOrderElementDao.updateByPrimaryKeySelective(record);
			
			  OrderApproval approval=new OrderApproval();
			  approval.setId(orderApproval.getId());
			  approval.setSpzt(232);
			  approval.setTaskId(task.getId());
			  if(null!=other){
			  other.setTaskId(task.getId());
			  }
			orderApprovalDao.updateByPrimaryKeySelective(approval);
			
			Jbpm4Task jbpm4Task=new Jbpm4Task();
			jbpm4Task.setYwTableId(clientOrderElement.getClientOrderId());
			jbpm4Task.setYwTableElementId(clientOrderElement.getId());
			jbpm4Task.setExecutionId(processInstance.getId());
			jbpm4Task.setRelationId(orderApproval.getId());
			jbpm4TaskDao.updateByExecutionId(jbpm4Task);
			jbpm4TaskDao.updateJbpm4HistTask(jbpm4Task);
			jbpm4TaskDao.updateJbpm4HistActinst(jbpm4Task);
			
			if(in){
				approvalAmount+=orderApproval.getAmount();
				if(approvalAmount>=clientOrderElement.getAmount()){
					approvalAmount=clientOrderElement.getAmount()-(approvalAmount-orderApproval.getAmount());
					orderApproval.setAmount(approvalAmount);
					orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
				}
				flowService.completeTask(task.getId(), "通过", ContextHolder.getCurrentUser().getUserId(), "","库存利润通过", "clientOrderElementService#pass", "","",task.getId());
//				record.setId(clientOrderElement.getId());
//				record.setSpzt(235);
//				clientOrderElementDao.updateByPrimaryKeySelective(record);
				Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByTaskId(task.getId());
				jbpm4Jbyj.setUserId("0");
				jbpm4Jbyj.setUserName("");
				jbpm4Jbyj.setTaskId(task.getId());
				jbpm4Jbyj.setOutcome("库存转订单");
				jbyjDao.update(jbpm4Jbyj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	   }
	}
    
    /**
     * 出在途库存
     */
    public void useOnPassageStorage(List<OnPassageStorage> onPassageStroages,ClientOrderElement clientOrderElement) {
    	boolean userOne = false;
    	Double used = 0.0;
    	for (OnPassageStorage onPassageStroage : onPassageStroages) {
			if (new Integer(1).equals(onPassageStroage.getBiggerAmount())) {
				OnPassageStorage onPassageStroage2 = new OnPassageStorage();
				onPassageStroage2.setSupplierOrderElementId(onPassageStroage.getSupplierOrderElementId());
				onPassageStroage2.setClientOrderElementId(clientOrderElement.getId());
				onPassageStroage2.setAmount(clientOrderElement.getAmount());
				onPassageStroage2.setImportStatus(0);
				onPassageStroageDao.insertSelective(onPassageStroage2);
				userOne = true;
				break;
			}
		}
    	if (!userOne) {
    		for (int i = 0; i < onPassageStroages.size(); i++) {
    			used += onPassageStroages.get(i).getAmount();
    			if (used<clientOrderElement.getAmount()) {
					OnPassageStorage onPassageStroage2 = new OnPassageStorage();
					onPassageStroage2.setSupplierOrderElementId(onPassageStroages.get(i).getSupplierOrderElementId());
					onPassageStroage2.setClientOrderElementId(clientOrderElement.getId());
					onPassageStroage2.setAmount(onPassageStroages.get(i).getAmount());
					onPassageStroage2.setImportStatus(0);
					onPassageStroageDao.insertSelective(onPassageStroage2);
				}else {
					BigDecimal b1 = new BigDecimal(Double.toString(onPassageStroages.get(i).getAmount()));
					BigDecimal b2 = new BigDecimal(Double.toString(used));
					BigDecimal b3 = new BigDecimal(Double.toString(clientOrderElement.getAmount()));
					//Double a = b2.divide(b3).doubleValue();
					used = b1.subtract(b2.subtract(b3)).doubleValue();
					if (used>0.0) {
						OnPassageStorage onPassageStroage2 = new OnPassageStorage();
						onPassageStroage2.setSupplierOrderElementId(onPassageStroages.get(i).getSupplierOrderElementId());
						onPassageStroage2.setClientOrderElementId(clientOrderElement.getId());
						onPassageStroage2.setAmount(used);
						onPassageStroage2.setImportStatus(0);
						onPassageStroageDao.insertSelective(onPassageStroage2);
					}
					break;
				}
    		}
		}
    	clientOrderElement.setStorageStatus(1);
    	clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
	}

	@Override
	public List<ClientOrderElement> findSpztByClientOrderId(ClientOrderElement record) {
		return clientOrderElementDao.findSpztByClientOrderId(record);
	}
    
    public Double getTotalAmount(Integer clientOrderId){
    	return clientOrderElementDao.getTotalAmount(clientOrderId);
    }
    
    /*
     * 覆盖excel上传
     */
	public MessageVo coverExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId){
		boolean success = true;
		String message = "保存成功！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		try {
			List<ListDateVo> cert=supplierQuoteDao.findcert();
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			 //定义行
		    Row row;
			//错误行数集合
		    List<ClientOrderElement> errorList = new ArrayList<ClientOrderElement>();
			StringBuffer lines=new StringBuffer();
			
		    //记录行数
			Integer flag = 2;
			
			//询价item
			List<ClientOrderElementVo> list = clientOrderElementDao.findItem(clientOrderId);
			List<ClientOrderElement> entityList = new ArrayList<ClientOrderElement>();
			ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderId);
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
				int a = sheet.getPhysicalNumberOfRows();
				int ifCorrect = 0;
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            	Integer number =new Double(row.getCell(0).toString()).intValue();
		            Integer item = new Double(row.getCell(1).toString()).intValue();
		            //String partNumber = row.getCell(2).toString().trim();
		            Cell oneCell = row.getCell(2);
		            String partNumber = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
						partNumber = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					    partNumber = dataFormatter.formatCellValue(oneCell);
					}
		            String description = row.getCell(3).toString();
		            String unit = row.getCell(4).toString();
		            Double amount = new Double(row.getCell(5).toString());
		            Double price = new Double(row.getCell(6).toString());
		            String leadTime = row.getCell(8).toString();
		            String remark = "";
		            if(null!=row.getCell(10)){
		            	remark=row.getCell(10).toString();
		            }
		            String certificationCode = "";
		            if(null!=row.getCell(11)){
		            	  certificationCode=row.getCell(11).toString();
		            }
		           
		            Integer certificationId=null;
		            for (ListDateVo listDateVo : cert) {
						if(listDateVo.getCode().equals(certificationCode)){
							certificationId= listDateVo.getId();
						}
						
					}
		            Double fixedCost=0.0;
		            if(null!=row.getCell(12)&&!"".equals(row.getCell(12)+"")){
		            	fixedCost=new Double(row.getCell(12).toString());
		            }
		            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		            String  dateString = sdf.format(HSSFDateUtil.getJavaDate(row.getCell(9).getNumericCellValue())).toString();
		            Date deadline = sdf.parse(dateString);*/
		            Calendar calendar = Calendar.getInstance();
	    			calendar.setTime(clientOrder.getOrderDate());
	    			calendar.add(Calendar.DATE, new Double(leadTime).intValue());
	    			Date deadline = calendar.getTime();
		            for (ClientOrderElementVo clientOrderElementVo : list) {
						if (number.equals(clientOrderElementVo.getItem())&&partNumber.equals(clientOrderElementVo.getInquiryPartNumber())) {
							 ClientOrderElement clientOrderElement = new ClientOrderElement(clientOrderId, clientOrderElementVo.getId(), amount, price, leadTime, deadline, new Date(),partNumber,certificationId,fixedCost,remark);
							 clientOrderElement.setDescription(description);
							 entityList.add(clientOrderElement);
					         ifCorrect = 1;
					         break;
						}else {
							
						}
					}
		           if (ifCorrect==0) {
		            	ClientOrderElement clientOrderElement = new ClientOrderElement();
						clientOrderElement.setUserId(userId);
						clientOrderElement.setItem(number);
						clientOrderElement.setPartNumber(partNumber);
						clientOrderElement.setLine(flag);
						clientOrderElement.setError("S/N或者件号有误！");
						errorList.add(clientOrderElement);
					}
		            flag++;
				}
	            
			}
			if (errorList.size()>0) {
				lines.append("Line ");
				for (ClientOrderElement clientOrderElement : errorList) {
					clientOrderElementUploadDao.insertSelective(clientOrderElement);
				}
				success=false;
				messageVo.setFlag(success);
				messageVo.setMessage("新增失败！");
				return messageVo;
			}
			
			if (errorList.size()==0) {
				//批量删除明细
				clientOrderElementDao.removeByClientOrderId(clientOrderId);
				clientInquiryService.excelBackup(wb, this.fetchOutputFilePath(), this.fetchOutputFileName(), clientOrderId.toString(),
						this.fetchYwTableName(), this.fetchYwTablePkName());
				for (ClientOrderElement clientOrderElement : entityList) {
					clientOrderElementDao.insert(clientOrderElement);
					

	    			
					
					/*if(clientOrder.getPrepayRate()>0){
						List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrderId,1);
//						ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderId);
//						List<ClientOrderElementVo> clientOrderVo=clientOrderElementDao.findByOrderId(clientOrderId);
						if(null!=clientInvoices&&clientInvoices.size()>0){
//							for (ClientOrderElementVo elementDataVo : clientOrderVo) {
//								if (elementDataVo.getClientOrderAmount()!=null) {
								 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
								 clientInvoiceElement.setAmount(clientOrderElement.getAmount());
								 Double terms=clientOrder.getPrepayRate()*100;
								 clientInvoiceElement.setTerms(terms.intValue());
								 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
								 clientInvoiceElement.setClientOrderElementId(clientOrderElement.getId());
								 clientInvoiceElementDao.insert(clientInvoiceElement);
//								}
//							}
						}else{
							String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
									"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
							for (int i = 0; i < ziMu.length; i++) {
								ClientInvoice incoiceNumber=clientInvoiceDao.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[i]);
								if(null!=incoiceNumber){
									continue;
								}else{
									ClientInvoice clientInvoice=new ClientInvoice();
									clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[i]);
									clientInvoice.setClientOrderId(clientOrderId);
									clientInvoice.setInvoiceDate(new Date());
									Double terms=clientOrder.getPrepayRate()*100;
									clientInvoice.setTerms(terms.intValue());
									clientInvoice.setInvoiceType(1);
									clientInvoice.setInvoiceStatusId(0);
									clientInvoiceDao.insert(clientInvoice);
									for (ClientOrderElementVo elementDataVo : clientOrderVo) {
										if (elementDataVo.getClientOrderAmount()!=null) {
										 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
										 clientInvoiceElement.setAmount(clientOrderElement.getAmount());
										 clientInvoiceElement.setTerms(terms.intValue());
										 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
										 clientInvoiceElement.setClientOrderElementId(clientOrderElement.getId());
										 clientInvoiceElementDao.insert(clientInvoiceElement);
										 break;
										}
									}
								}
							}
						}
				}*/	
					
					/*Double storageAmount =0.0;
	    			List<SupplierOrderElement> vos=supplierOrderElementDao.findStorageByPn(clientOrderElement.getPartNumber());
	    			Integer cieElementId = supplierOrderElementDao.getElementId(clientOrderElement.getClientQuoteElementId());
	    			Integer sqeElementId = supplierOrderElementDao.getSqeElementId(clientOrderElement.getClientQuoteElementId());
	    			List<SupplierListVo> supplierList =new ArrayList<SupplierListVo>();
	    			
	    			for (SupplierOrderElement supplierOrderElement : vos) {
	    				storageAmount+=supplierOrderElement.getStorageAmount();
	    				List<SupplierListVo> lists =supplierOrderElementDao.findByElementIdsAndCoeId(cieElementId, sqeElementId,supplierOrderElement.getClientOrderElementId());
	    				supplierList.addAll(lists);
	    			}
	    			Double count=clientOrderElement.getAmount();
	    			boolean full=false;
//					Double storageAmount =supplierOrderElementDao.getStorageAmount(clientOrderElement.getPartNumber());
	    			if(null!=storageAmount&&storageAmount>0&&storageAmount>=clientOrderElement.getAmount()){
//					Integer cieElementId = supplierOrderElementDao.getElementId(clientOrderElement.getClientQuoteElementId());
//	    			Integer sqeElementId = supplierOrderElementDao.getSqeElementId(clientOrderElement.getClientQuoteElementId());
//	    			List<SupplierListVo> supplierList =supplierOrderElementDao.findByElementIds(cieElementId, sqeElementId);
//	    			com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo clientOrderElementVo =supplierOrderElementDao.findById(clientOrderElement.getId());
	      		  for (SupplierListVo supplierListVo : supplierList) {
	      			
	      			SupplierOrder supplierOrder=new SupplierOrder();
	      			supplierOrder.setSupplierId(supplierListVo.getSupplierId());
	      			supplierOrder.setClientOrderId(clientOrderElement.getClientOrderId());
	      			SupplierOrder supplierOrder2 = supplierOrderDao.findByClientOrderId(supplierOrder);
	      			
	      			com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo clientOrderElementVo1 = supplierOrderElementDao.findByClientOrderElementId(supplierListVo.getClientOrderElementId());
	      			if(null!=clientOrderElementVo1&&clientOrderElementVo1.getStorageAmount()>0){
	      				
	      				 SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
	      				 
	      				 supplierOrderElement.setClientOrderElementId(clientOrderElement.getId());
	      				 supplierOrderElement.setSupplierQuoteElementId(supplierListVo.getId());
//	      					if(clientOrderElement.getAmount()<=supplierListVo.getQuoteAmount()){
//	      					supplierOrderElement.setAmount(clientOrderElement.getAmount());
////	      					clientOrderElementVo.setStorageAmount(supplierListVo.getQuoteAmount()-clientOrderElementVo.getClientOrderAmount());
//	      					}else{
//	      						supplierOrderElement.setAmount(supplierListVo.getQuoteAmount());
////	      						clientOrderElement.setStorageAmount(0.0);
//	      					}
	      					if(clientOrderElement.getAmount()<=supplierListVo.getQuoteAmount()){
	          					supplierOrderElement.setAmount(clientOrderElement.getAmount());
	          					full=true;
//	          					clientOrderElementVo.setStorageAmount(supplierListVo.getQuoteAmount()-clientOrderElementVo.getClientOrderAmount());
	          					}else{
	          						if(supplierListVo.getQuoteAmount()>=count){
	          							supplierOrderElement.setAmount(count);
	          							full=true;
	          						}else{
	          						supplierOrderElement.setAmount(supplierListVo.getQuoteAmount());
	          						count=count-supplierListVo.getQuoteAmount();
	          						}
	          					}
	      					supplierOrderElement.setPrice(supplierListVo.getPrice());
	      					supplierOrderElement.setLeadTime("0");
	      					supplierOrderElement.setDeadline(new Date());
	      				if (supplierOrder2!=null ) {
//	      					SupplierOrderElement element=supplierOrderElementDao.selectBySqeIdAndSoId(supplierOrder2.getId(), supplierListVo.getId());
//	      					if(null==element){
	      					supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
	      					supplierOrderElementDao.insertSelective(supplierOrderElement);
//	      					}
	      					
	      				}else {
	      					supplierOrder.setOrderDate(new Date());
	      					supplierOrder.setCurrencyId(supplierListVo.getCurrencyId());
	      					supplierOrder.setExchangeRate(supplierListVo.getExchangeRate());
	      					supplierOrderService.insertSelective(supplierOrder);
	      					supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
	      					supplierOrderElementDao.insertSelective(supplierOrderElement);
	      				}
	      				
//	      				if(clientOrderElementVo.getStorageAmount()>supplierOrderElement.getAmount()){
	      					clientOrderElementVo1.setAmount(supplierOrderElement.getAmount());
	      					clientOrderElementVo1.setSupplierOrderElementId(supplierOrderElement.getId());
	      					String importpackNumber=importpackageElementService.splitOrder(clientOrderElementVo1);
//	      				}
	      					List<ClientOrderElementVo> clientOrderElementVos=clientOrderDao.findSupplierId(clientOrderElement.getId());
	      					EmailVo emailVo=new EmailVo();
	      					if(null!=clientOrderElementVos.get(0).getSupplierId()){
								emailVo.setSupplierId(clientOrderElementVos.get(0).getSupplierId());					
							      }
	      					emailVo.setNowImportpackNumber(importpackNumber);
	      					emailVo.setOldImportpackNumber(clientOrderElementVo1.getImportNumber());
	      					emailVo.setPartNumber(clientOrderElementVo1.getPartNumber());
	      					emailVo.setDescription(clientOrderElementVo1.getDescription());
	      					emailVo.setOrderNumber(clientOrder.getOrderNumber());
	      					emailVos.add(emailVo);
	      			}
	      			if(full){
	      				break;
	      			}
	      			}
				}
				}
				
				sendEmail(emailVos,userId.toString());
				
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;*/
			}
			}
			
		}catch (Exception e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
			}
			e.printStackTrace();
		}
		return new MessageVo(success, message);
	}

	@Override
	public String findBsnByCoeId(Integer id) {
		return clientOrderElementDao.findBsnByCoeId(id);
	}

	@Override
	public List<ClientOrderElement> selectByForeignKey(Integer clientOrderId) {
		return clientOrderElementDao.selectByForeignKey(clientOrderId);
	}



	@Override
	public boolean Stock(List<ImportPackageElementVo> elementVos, ClientOrderElement clientOrderElement,List<StorageFlowVo> supplierList) {
		boolean use=false;
		 Double amount=clientOrderElement.getAmount();
		 Double fixedCost=clientOrderElement.getFixedCost();
		 Double orderPrice=clientOrderElement.getPrice()*clientOrderElement.getExchangeRate();
		 if(fixedCost<1){
			 fixedCost=fixedCost*clientOrderElement.getPrice();
		 }
		 
		 List<StorageFlowVo>  benchStandardList=new ArrayList<StorageFlowVo>();
		 Double benchStandardAmount=0.0;
		 List<StorageFlowVo>  benchSubstandardList=new ArrayList<StorageFlowVo>();
		 List<StorageFlowVo> fullList =new ArrayList<StorageFlowVo>();
		for (ImportPackageElementVo importPackageElementVo : elementVos) {
			 List<StorageFlowVo> flowVos=importpackageElementService.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
			 if(flowVos.size()>0){
				 for (StorageFlowVo storageFlowVo : flowVos) {
					 Double storagePrice=storageFlowVo.getPrice()*storageFlowVo.getExchangeRate();
					 Double storageAmount=storageFlowVo.getStorageAmount();
					 //有库存，数量和利润是否满足
					 Double profitMargin=((orderPrice-fixedCost)-storagePrice)/orderPrice;
					 storageFlowVo.setProfitMargin(profitMargin);
					 if(storageAmount>=amount){
						 if(profitMargin>=0.18){
							 fullList.clear();
							 fullList.add(storageFlowVo);
							 supplierList.addAll(fullList);
							 use=true;
							 return use;//数量利润满足直接跳出
						 }else{
							 fullList.add(storageFlowVo);
						 }
					 }else{
						 if(profitMargin>=0.18){
							 benchStandardAmount+=storageAmount;
							 benchStandardList.add(storageFlowVo);
							 if(benchStandardAmount>=amount){
								 supplierList.addAll(benchStandardList);
								 use=true;
								 return use;//数量利润满足直接跳出
							 }
						 }else{
							 benchSubstandardList.add(storageFlowVo);
						 }
					 }
				}
			 }
		}
		//前面没有跳出的情况
			    supplierList.addAll(fullList);
			 for (StorageFlowVo storageFlowVo2 : benchSubstandardList) {
				benchStandardList.add(storageFlowVo2);
			 } 
			     supplierList.addAll(benchStandardList);
		return use;
	}
	
	@Override
	public boolean onPassageStock(List<SupplierListVo> elementVos, ClientOrderElement clientOrderElement,
			List<OnPassageStorage> supplierList) {
		boolean use=false;
		 Double amount=clientOrderElement.getAmount();
		 Double fixedCost=clientOrderElement.getFixedCost();
		 Double orderPrice=clientOrderElement.getPrice();
		 if(fixedCost<1){
			 fixedCost=fixedCost*clientOrderElement.getPrice();
		 }
		 
		 ClientOrder clientOrder=clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
		 
		 List<OnPassageStorage>  benchStandardList=new ArrayList<OnPassageStorage>();
		 Double benchStandardAmount=0.0;
		 List<OnPassageStorage>  benchSubstandardList=new ArrayList<OnPassageStorage>();
		 Double benchSubstandardAmount=0.0;
		 List<OnPassageStorage> fullList =new ArrayList<OnPassageStorage>();
		 for (SupplierListVo supplierListVo : elementVos) {
			 PageModel<OnPassageStorage> onPassagePage = new PageModel<OnPassageStorage>();
				onPassagePage.put("where", "sqe.id = "+supplierListVo.getId());
				List<OnPassageStorage> onPassageStroage = onPassageStroageDao.selectBySupplierQuoteElementId(onPassagePage);
				for (OnPassageStorage onPassageStorage : onPassageStroage) {
					 Double storagePrice=onPassageStorage.getPrice()*onPassageStorage.getExchangeRate()/clientOrder.getExchangeRate();
					 Double storageAmount=onPassageStorage.getAmount();
					 //有库存，数量和利润是否满足
					 Double profitMargin=((orderPrice-fixedCost)-storagePrice)/orderPrice;
					 onPassageStorage.setProfitMargin(profitMargin);
					 if(storageAmount>=amount){
						 if(profitMargin>=0.18){
							 fullList.clear();
							 fullList.add(onPassageStorage);
							 supplierList.addAll(fullList);
							 use=true;
							 return use;//数量利润满足直接跳出
						 }else{
							 fullList.add(onPassageStorage);
						 }
					 }else{
						 if(profitMargin>=0.18){
							 benchStandardAmount+=storageAmount;
							 benchStandardList.add(onPassageStorage);
							 if(benchStandardAmount>=amount){
								 supplierList.addAll(benchStandardList);
								 use=true;
								 return use;//数量利润满足直接跳出
							 }
						 }else{
							 benchSubstandardAmount+=storageAmount;
							 benchSubstandardList.add(onPassageStorage);
						 }
					 }
				}
		}
		return false;
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object pass(String businessKey,
			String taskId, String outcome, String assignee, String comment){
	
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(orderApproval.getClientOrderElementId());
		ClientProfitmargin clientProfitmargin=clientProfitmarginDao.selectByClientId(clientOrderElementVo.getClientId());
		Double price =0.0;
		 ClientWeatherOrder clientWeatherOrder=clientWeatherOrderDao.selectByPrimaryKey(orderApproval.getClientOrderId());
		if(orderApproval.getType().equals(1)||orderApproval.getType()==1){//在途
			AddSupplierOrderElementVo elementVo=supplierOrderElementDao.findByElementId(orderApproval.getSupplierOrderElementId());
			Integer sCurrencyId=elementVo.getCurrencyId();
			Integer cCurrencyId=clientWeatherOrder.getCurrencyId();
			price =orderApproval.getPrice();
			if(!sCurrencyId.equals(cCurrencyId)){
				price =orderApproval.getPrice()*elementVo.getExchangeRate()/clientWeatherOrder.getExchangeRate();
			}
		
		}else{//自有库存
		 ImportPackageElementVo elementVo=importpackageElementService.findimportpackageelement(orderApproval.getImportPackageElementId().toString());
		 	Integer sCurrencyId=elementVo.getCurrencyId();
			Integer cCurrencyId=clientWeatherOrder.getCurrencyId();
			price =orderApproval.getPrice();
			if(!sCurrencyId.equals(cCurrencyId)){
				price =orderApproval.getPrice()*elementVo.getExchangeRate()/clientWeatherOrder.getExchangeRate();
			}
		}
		 Double orderPrice=clientWeatherOrderElement.getPrice();
		 Double fixedCost=clientWeatherOrderElement.getFixedCost();
		 if(fixedCost<1){
			 fixedCost=fixedCost*clientWeatherOrderElement.getPrice();
		 }
		 ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
		 SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
		 Double feeForExchangeBill = supplierQuoteElement.getFeeForExchangeBill();
		 Double bankCharges = supplierQuoteElement.getBankCost();
		 if (bankCharges == null) {
			 bankCharges = 0.0;
		 }
		 Double profitMargin=((orderPrice-fixedCost-bankCharges-feeForExchangeBill)-price)/orderPrice;
		 BigDecimal pm=new BigDecimal(profitMargin);
		 profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		Jbpm4Jbyj zjljbyj=jbpm4JbyjService.findByTask("总经理审核库存", "ORDER_APPROVAL.ID."+orderApproval.getId());
		if(profitMargin>=clientProfitmargin.getProfitMargin()||null!=zjljbyj){//利润通过或已经经过一次审核可以使用库存
			variables.put("desc", "销售生成客户订单");
			variables.put("to", "通过");
			variables.put("assignee", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
		Double orderAmount=clientWeatherOrderElement.getAmount();
		Double amount=orderApprovalDao.findUseAmount(orderApproval.getClientOrderElementId());//这个件号使用库存的总数量
		Double lockAmount = orderApprovalDao.getUsedStorageAmount(orderApproval.getSupplierQuoteElementId(), orderApproval.getImportPackageElementId(), orderApproval.getId());
		if (lockAmount == null) {
			lockAmount = 0.00;
		}
		Double	passAmount=amount+orderApproval.getAmount();//-lockAmount;
		ImportPackageElement importPackageElement = importpackageElementService.selectByPrimaryKey(orderApproval.getImportPackageElementId());
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByEpeId(clientQuoteElement.getClientInquiryElementId());
		String unit = (clientWeatherOrderElement.getUnit() != null && !"".equals(clientWeatherOrderElement))?clientWeatherOrderElement.getUnit():clientInquiryElement.getUnit();
		if(passAmount>=orderAmount && importPackageElement.getUnit().equals(unit)){
			orderApproval.setAmount(orderAmount-amount);
			orderApproval.setState(1);//通过
			orderApproval.setOccupy(1);//使用
			orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
			List<OrderApprovalVo> notUseList=orderApprovalDao.notUse(orderApproval.getClientOrderElementId());
			for (OrderApprovalVo notUse : notUseList) {//库存数量大于等于订单数量，其他不使用的删除，不用进一步审核
				if(notUse.getId().equals(orderApproval.getId())){
					continue;
				}
				orderApprovalDao.deleteByPrimaryKey(notUse.getId());
				if(null!=notUse.getTaskId()){
					flowService.deleteByParticipation(new BigDecimal(notUse.getTaskId()));
					flowService.deleteByPrimaryKey(new BigDecimal(notUse.getTaskId()));
				}
			}
		}else{
			orderApproval.setState(1);
			orderApproval.setOccupy(1);
			orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		}
			Jbpm4Task record=new Jbpm4Task();
			record.setTaskdefname("采购审核");
			record.setYwTableElementId(orderApproval.getClientOrderElementId());
			 List<Jbpm4Task> havetask=jbpm4TaskDao.selectByTaskName(record);
			 if(havetask.size()==0){
				 record.setTaskdefname("采购审核库存");
				 havetask=jbpm4TaskDao.selectByTaskName(record);
			 }
			if(orderAmount>passAmount&&havetask.size()<=1){//如果库存数量小于订单数量，库存审核流程结束了，并且没有采购做预订单的流程，系统自动生成一条流程
				Jbpm4Task jbpm4Task =new Jbpm4Task();
				jbpm4Task.setYwTableElementId(orderApproval.getClientOrderElementId());
				jbpm4Task.setTaskdefname("采购生成供应商预订单");
				List<Jbpm4Task>  jbpm4Tasks=flowService.selectWeatherOrder(jbpm4Task);
				if(jbpm4Tasks.size()>0){
					return variables;
				}else{
					OrderApproval approval=new OrderApproval();
					approval.setHandle("无库存");
					approval.setAmount(orderAmount);
					Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByBusinessKeyAndOutcome("ORDER_APPROVAL.ID."+orderApproval.getId(), "发起");
					approval.setDesc(jbpm4Jbyj.getUserName()+"发起【合同审批】等待您的处理!");
					approval.setUserId(jbpm4Jbyj.getUserId());
					Jbpm4Jbyj jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", "ORDER_APPROVAL.ID."+orderApproval.getId());
			 		if(null==jbyj){
			 			jbyj=jbpm4JbyjService.findByTask("采购审核","ORDER_APPROVAL.ID."+orderApproval.getId());
			 		}
			 		if(null==jbyj){
			 			jbyj=jbpm4JbyjService.findByTask("采购审核库存","ORDER_APPROVAL.ID."+orderApproval.getId());
			 		}
			 		if(null==jbyj){
			 			assignee=ContextHolder.getCurrentUser().getUserId();
			 		}else{
			 		assignee=jbyj.getUserId();
			 		}
			 		contractReviewService.orderApproval(clientWeatherOrderElement, assignee,approval);
					Jbpm4Task task=	jbpm4TaskDao.selectByPrimaryKey(new BigDecimal(taskId));
					Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+approval.getId());
					task2.setDescr(task.getDescr());
					flowService.updateByPrimaryKeySelective(task2);
					List<Jbpm4Jbyj> jbpm4Jbyjs=flowService.selectByYwTableElementId(orderApproval.getClientOrderElementId().toString(),"ContractreviewProcess.ORDER_APPROVAL.ID."+approval.getId());
					for (int i = 0; i < jbpm4Jbyjs.size(); i++) {
							Jbpm4Jbyj gyJbyj=new Jbpm4Jbyj();
							gyJbyj.setProcessinstanceId("ContractreviewProcess.ORDER_APPROVAL.ID."+approval.getId());
							gyJbyj.setUserId(jbpm4Jbyjs.get(i).getUserId());
							gyJbyj.setUserName(jbpm4Jbyjs.get(i).getUserName());
							gyJbyj.setTaskName(jbpm4Jbyjs.get(i).getTaskName());
							flowService.updateBytaskName(gyJbyj);
					}
				}
			}
		}else{
			variables.put("to", "库存利润低于标准");
			variables.put("assignee", "2");
		}
		
		return variables;
	}
	
	 public Object syncFinish(String businessKey,String dbids){
		 String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		Integer elementId = supplierOrderElementDao.getElementId(clientOrderElement.getClientQuoteElementId());
		 List<OrderApprovalVo> approvalVos= orderApprovalDao.syncFinishData(elementId, orderApproval.getClientOrderId());
		 if(approvalVos.size()>0){
			 for (OrderApprovalVo orderApprovalVo : approvalVos) {
				if(dbids.indexOf(orderApprovalVo.getTaskId())>-1){
					continue;
				}else{
					try {
						completeTask(orderApprovalVo.getTaskId(), "使用", (String) flowService.getVariable(orderApprovalVo.getTaskId(), WorkFlowConstants.START_USER), "","库存利润通过", "clientOrderElementService#pass", "","",orderApprovalVo.getTaskId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		 }
		return null;
	 }
	
	 public Object syncNoUse(String businessKey,String dbids){
		 String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		Integer elementId = supplierOrderElementDao.getElementId(clientOrderElement.getClientQuoteElementId());
		 List<OrderApprovalVo> approvalVos= orderApprovalDao.syncFinishData(elementId, orderApproval.getClientOrderId());
		 if(approvalVos.size()>0){
			 for (OrderApprovalVo orderApprovalVo : approvalVos) {
				if(dbids.indexOf(orderApprovalVo.getTaskId())>-1){
					continue;
				}else{
					try {
						completeTask(orderApprovalVo.getTaskId(), "不使用", ContextHolder.getCurrentUser().getUserId().toString(), "","", "clientOrderElementService#nopass", "","",orderApprovalVo.getTaskId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		 }
		return null;
	 }
	 
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object nopass(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		
//		Jbpm4Task record2=new Jbpm4Task();
//		record2.setTaskdefname("采购审核");
//		record2.setYwTableElementId(orderApproval.getClientOrderElementId());
//		List<Jbpm4Task> jbpm4Tasks2=jbpm4TaskDao.selectWeatherOrder(record2);
//		
//		Jbpm4Task record3=new Jbpm4Task();
//		record3.setTaskdefname("总经理审核");
//		record3.setYwTableElementId(orderApproval.getClientOrderElementId());
//		List<Jbpm4Task> jbpm4Tasks3=jbpm4TaskDao.selectWeatherOrder(record3);
//		
//		if(jbpm4Tasks2.size()==1&&jbpm4Tasks3.size()==0){
//			orderApproval.setTaskId(taskId);
//		}else{
//			orderApproval.setTaskId(null);
//		}
		Jbpm4Task jbpm4Task =new Jbpm4Task();
		jbpm4Task.setYwTableElementId(orderApproval.getClientOrderElementId());
		jbpm4Task.setTaskdefname("采购生成供应商预订单");
		List<Jbpm4Task>  jbpm4Tasks=flowService.selectWeatherOrder(jbpm4Task);
		orderApproval.setTaskId(taskId);
		for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
			OrderApproval approval=orderApprovalDao.selectByPrimaryKey(jbpm4Task2.getRelationId());
			if(null!=approval.getTaskId()){
				orderApproval.setTaskId(null);
				break;
			}
		}
		orderApproval.setOccupy(2);
		orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		return null;
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object weatherpass(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		
		SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
		if(supplierWeatherOrderElement != null){
			if(supplierWeatherOrderElement.getSupplierStatus().equals(0)||supplierWeatherOrderElement.getSupplierStatus()==0){
				variables.put("desc", null);
				variables.put("to", "价格上涨或利润不通过");
				return variables;
			}
			
			ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(orderApproval.getClientOrderElementId());
			ClientProfitmargin clientProfitmargin=clientProfitmarginDao.selectByClientId(clientOrderElementVo.getClientId());
			SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
			ClientWeatherOrder clientWeatherOrder=clientWeatherOrderDao.selectByPrimaryKey(orderApproval.getClientOrderId());
			ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
			Integer sqCurrencyId=element.getCurrencyId();
			Integer cwoCurrencyId=clientWeatherOrder.getCurrencyId();
			Double weatherPrice=supplierWeatherOrderElement.getPrice();
			if(!sqCurrencyId.equals(cwoCurrencyId)){
				ClientQuote clientQuote = clientQuoteDao.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				//判断是否7开头客户
				ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
				ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
				if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
					if (!sqCurrencyId.toString().equals("11")) {
						ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sqCurrencyId);
						Double relative = new BigDecimal(currentRate.getRelativeRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
						relative = relative*usRate.getRelativeRate();
						weatherPrice = clientQuoteService.caculatePrice(new BigDecimal(supplierWeatherOrderElement.getPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
					}else {
						weatherPrice = clientQuoteService.caculatePrice(new BigDecimal(supplierWeatherOrderElement.getPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
					}
				}else {
					//正常的价格计算方法
					weatherPrice = clientQuoteService.caculatePrice(new BigDecimal(supplierWeatherOrderElement.getPrice()), new BigDecimal(element.getExchangeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
					//weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
				}
				
			}
//			Double quotePrice=clientOrderElementVo.getSupplierQuotePrice()*clientOrderElementVo.getSupplierQuoteExchangeRate()/clientWeatherOrder.getExchangeRate();
			Double orderPrice=clientWeatherOrderElement.getPrice();
			Double fixedCost=clientWeatherOrderElement.getFixedCost();
			if (fixedCost != null && fixedCost < 1) {
				fixedCost = orderPrice*fixedCost;
			}else if (fixedCost == null) {
				fixedCost = 0.0;
			}
			Double bankChargesOrder=clientWeatherOrderElement.getBankCharges();
			if (bankChargesOrder != null && bankChargesOrder < 1) {
				bankChargesOrder = orderPrice*bankChargesOrder;
			}else if (bankChargesOrder == null) {
				bankChargesOrder = 0.0;
			}
			 ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
			 SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
			 SupplierQuote supplierQuote = supplierQuoteDao.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteId());
			 SupplierInquiry supplierInquiry = supplierInquiryDao.selectByPrimaryKey(supplierQuote.getSupplierInquiryId());
			 SupplierWeatherOrder supplierWeatherOrder = supplierWeatherOrderDao.getByOrderIdAndSupplier(clientWeatherOrder.getId(), supplierInquiry.getSupplierId());
			 Double feeForExchangeBill = 0.0;
			 Double bankCharges = 0.0;
			 Double hazmatFee = 0.0;
			 Double quoteOtherFee = 0.0;
			ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
			if (supplierQuoteElement.getBankCost() != null) {
				BigDecimal bankCost = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
					if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
						bankCharges = bankCost.doubleValue()/clientQuoteElement.getMoq();
					}else {
						bankCharges = bankCost.doubleValue()/clientWeatherOrderElement.getAmount();
					}
				}else {
					bankCharges = bankCost.doubleValue()/clientWeatherOrderElement.getAmount();
				}
				
			}
			if (supplierQuoteElement.getFeeForExchangeBill() != null) {
				BigDecimal orderFeeForExchangeBill = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
					if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
						feeForExchangeBill = orderFeeForExchangeBill.doubleValue()/clientQuoteElement.getMoq();
					}else {
						feeForExchangeBill = orderFeeForExchangeBill.doubleValue()/clientWeatherOrderElement.getAmount();
					}
				}else {
					feeForExchangeBill = orderFeeForExchangeBill.doubleValue()/clientWeatherOrderElement.getAmount();
				}
				
			}
			if (supplierQuoteElement.getHazmatFee() != null) {
				BigDecimal HazmatFee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				hazmatFee = HazmatFee.doubleValue();
			}
			if (supplierQuoteElement.getOtherFee() != null) {
				BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
					if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
						quoteOtherFee = otherFee.doubleValue()/clientQuoteElement.getMoq();
					}else {
						quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
					}
				}else {
					quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
				}
				
			}
			Double profitMargin=(orderPrice-bankCharges-feeForExchangeBill-hazmatFee-bankChargesOrder-fixedCost-weatherPrice-quoteOtherFee)/
					(orderPrice-bankCharges-feeForExchangeBill-hazmatFee-bankChargesOrder-fixedCost-quoteOtherFee);
			BigDecimal pm=new BigDecimal(profitMargin);
			profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(supplierWeatherOrderElement.getPrice()>clientOrderElementVo.getSupplierQuotePrice()){//订单价格是否高于报价价格
			variables.put("desc", null);
			variables.put("to", "价格上涨或利润不通过");
			}else{
				if(profitMargin>=clientProfitmargin.getProfitMargin()){
					variables.put("desc", "销售生成客户订单");
			variables.put("to", "利润通过");
			Jbpm4Task record=new Jbpm4Task();
			record.setTaskdefname("销售生成客户订单");
			record.setYwTableElementId(orderApproval.getClientOrderElementId());
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.selectByTaskName(record);
			for (Jbpm4Task jbpm4Task : jbpm4Tasks) {
				jbpm4Task.setDescr("销售生成客户订单");
					jbpm4TaskDao.updateByPrimaryKeySelective(jbpm4Task);
				}
			}else{
				variables.put("desc", null);
			variables.put("to", "价格上涨或利润不通过");
				}
			}
			List<OrderApprovalVo> notUseList=orderApprovalDao.notUse(orderApproval.getClientOrderElementId());
			for (OrderApprovalVo notUse : notUseList) {
				if(null==notUse.getSupplierWeatherOrderElementId()){
					orderApprovalDao.deleteByPrimaryKey(notUse.getId());
					if(null!=notUse.getTaskId()){
						flowService.deleteByParticipation(new BigDecimal(notUse.getTaskId()));
						flowService.deleteByPrimaryKey(new BigDecimal(notUse.getTaskId()));
					}
				}
			}
			return variables;
		}else {
			return variables;
		}
	}
	
	
	@Override
	public Integer findClientIdByCoeId(Integer clientOrderElementId) {
		return clientOrderElementDao.findClientIdByCoeId(clientOrderElementId);
	}

	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object finalpass(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		boolean in=false;
		Double profitMargin=0.0;
		UserVo userVo=ContextHolder.getCurrentUser();
		ClientWeatherOrder clientWeatherOrder=clientWeatherOrderDao.selectByPrimaryKey(orderApproval.getClientOrderId());
		ClientOrderElementFinal clientOrderElementFinal=clientOrderElementFinalDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(orderApproval.getClientOrderElementId());
		ClientProfitmargin clientProfitmargin=clientProfitmarginDao.selectByClientId(clientOrderElementVo.getClientId());
		List<Jbpm4Task> jbpm4Tasks=	jbpm4TaskDao.findByYwTableElementId(orderApproval.getClientOrderElementId());
		if(null==orderApproval.getSupplierWeatherOrderElementId()||clientOrderElementFinal.getOrderStatusId()!=60){
			if(!clientOrderElementFinal.getOrderStatusId().equals(64)||clientOrderElementFinal.getOrderStatusId()!=64){
				profitMargin=clientProfitmargin.getProfitMargin();
				orderApproval.setSpzt(235);
			}else{
				orderApproval.setSpzt(233);
			}
			in=true;
			variables.put("to", "订单利润通过");
			orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		}else{
			SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
			if(supplierWeatherOrderElement.getSupplierStatus().equals(0)||supplierWeatherOrderElement.getSupplierStatus()==0){
				variables.put("to", "订单利润通过");
				orderApproval.setSpzt(233);
				orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
				return variables;
			}
			
			SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
			Integer sqCurrencyId=element.getCurrencyId();
			Integer cwoCurrencyId=clientWeatherOrder.getCurrencyId();
			Double weatherPrice=supplierWeatherOrderElement.getPrice();
			if(!sqCurrencyId.equals(cwoCurrencyId)){
			 weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
			}
			 weatherPrice=new BigDecimal(weatherPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			Double orderPrice=clientOrderElementFinal.getPrice();
			Double fixedCost=clientOrderElementFinal.getFixedCost();
			if(fixedCost != null && fixedCost < 1){
				fixedCost=clientOrderElementFinal.getPrice()*fixedCost;
			}else if (fixedCost == null) {
				fixedCost = 0.0;
			}
			Double bankChargesOrder=clientOrderElementFinal.getBankCharges();
			if(bankChargesOrder != null && bankChargesOrder < 1){
				bankChargesOrder=clientOrderElementFinal.getPrice()*bankChargesOrder;
			}else if (bankChargesOrder == null) {
				bankChargesOrder = 0.0;
			}
			ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
			Double quoteFeeForExchangeBill = 0.0;
			Double quoteBankCost = 0.0;
			Double quoteHazmatFee = 0.0;
			Double quoteOtherFee = 0.0;
			if (element.getBankCost() != null && element.getBankCost() > 0) {
				BigDecimal bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(element.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
					if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
						quoteBankCost = bankCostQuote.doubleValue()/clientQuoteElement.getMoq();
					}else {
						quoteBankCost = bankCostQuote.doubleValue()/clientWeatherOrderElement.getAmount();
					}
				}else {
					quoteBankCost = bankCostQuote.doubleValue()/clientWeatherOrderElement.getAmount();
				}
				
			}
			if (element.getFeeForExchangeBill() != null && element.getFeeForExchangeBill() > 0) {
				BigDecimal feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(element.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
					if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
						quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientQuoteElement.getMoq();
					}else {
						quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientWeatherOrderElement.getAmount();
					}
				}else {
					quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientWeatherOrderElement.getAmount();
				}
				
			}
			if (element.getHazmatFee() != null && element.getHazmatFee() > 0) {
				BigDecimal hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(element.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				quoteHazmatFee = hzmatFeequote.doubleValue();
			}
			if (element.getOtherFee() != null && element.getOtherFee() > 0) {
				BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(element.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
				if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
					if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
						quoteOtherFee = otherFee.doubleValue()/clientQuoteElement.getMoq();
					}else {
						quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
					}
				}else {
					quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
				}
				
			}
			 Double bankCharges=0.0;
			 List<ClientQuoteVo> quoteVos= clientQuoteDao.findbyclientquoteid(clientWeatherOrder.getClientQuoteId().toString());
			 Double sumPrice= clientOrderElementFinalDao.sumPrice(clientWeatherOrder.getId());
			 List<OrderBankCharges> list=orderBankChargesService.orderBankChargesByClientId(quoteVos.get(0).getClient_id().toString());
			  for (OrderBankCharges orderBankCharges : list) {
				  if(null==orderBankCharges.getOrderPriceAbove()&&sumPrice<orderBankCharges.getOrderPriceFollowing()){
					  bankCharges=orderBankCharges.getBankCharges();  break;
				  }else
				  if(null==orderBankCharges.getOrderPriceFollowing()&&sumPrice>orderBankCharges.getOrderPriceAbove()){
					  bankCharges=orderBankCharges.getBankCharges();  break;
				  }else
				  if(sumPrice>orderBankCharges.getOrderPriceAbove()&&sumPrice<orderBankCharges.getOrderPriceFollowing()){
					  bankCharges=orderBankCharges.getBankCharges();  break;
				  }
			}
			  ClientOrderElementFinal record=new ClientOrderElementFinal();
			  record.setBankCharges(bankCharges);
			  record.setClientWeatherOrderId(orderApproval.getClientOrderId());
			  clientOrderElementFinalDao.updateBankCharges(record);
			  bankCharges=bankCharges*orderPrice;
			  profitMargin=(orderPrice-quoteBankCost-quoteFeeForExchangeBill-quoteHazmatFee-bankChargesOrder-fixedCost-quoteOtherFee-weatherPrice)/
					  (orderPrice-quoteBankCost-quoteFeeForExchangeBill-quoteHazmatFee-bankChargesOrder-fixedCost-quoteOtherFee);
			  BigDecimal pm=new BigDecimal(profitMargin);
			  profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			  Jbpm4Jbyj zjljbyj=jbpm4JbyjService.findByTask("总经理审核利润", "ORDER_APPROVAL.ID."+orderApproval.getId());
			if(profitMargin>=clientProfitmargin.getProfitMargin()){
				in=true;
				variables.put("to", "订单利润通过");
				orderApproval.setSpzt(235);
				orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
			}else if(null!=zjljbyj){
				in=true;
				profitMargin=clientProfitmargin.getProfitMargin();
				variables.put("to", "订单利润通过");
				orderApproval.setSpzt(235);
				orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
			}else{
				Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", businessKey);
				variables.put("assignee", jbpm4Jbyj.getUserId());
				variables.put("to", "订单利润不通过");
			}
		}
		
		if(jbpm4Tasks.size()<2&&profitMargin>=clientProfitmargin.getProfitMargin()){
			ClientQuote clientQuote = clientQuoteDao.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
			ClientInquiry Inquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
			Client client = clientService.selectByPrimaryKey(Inquiry.getClientId());
			ClientOrder clientOrder = new ClientOrder();
			if (client.getCode().startsWith("3") && clientOrderElementFinal.getOrderNumberIndex() != null) {
				String index = clientOrderElementFinal.getOrderNumberIndex().toString().replace("-", "");
				if ("0".equals(index) || "1".equals(index)) {
					clientOrder=clientOrderDao.selectByOrderNumber("ORD-" + clientQuote.getQuoteNumber());
				}else {
					clientOrder=clientOrderDao.selectByOrderNumber("ORD-" + clientQuote.getQuoteNumber() + "-" + index);
				}
			}else {
				clientOrder=clientOrderDao.findClientWeatherOrderId(orderApproval.getClientOrderId());
			}
			List<OrderApproval> approvals=orderApprovalDao.selectByClientOrderElementId(orderApproval.getClientOrderElementId());
			
			if(null==clientOrder){
				clientOrder=new ClientOrder();
				clientOrder.setClientQuoteId(clientWeatherOrder.getClientQuoteId());
				clientOrder.setCurrencyId(clientWeatherOrder.getCurrencyId());
				clientOrder.setExchangeRate(clientWeatherOrder.getExchangeRate());
				clientOrder.setSourceNumber(clientWeatherOrder.getSourceNumber());
				clientOrder.setOrderDate(clientWeatherOrder.getOrderDate());
				clientOrder.setPrepayRate(clientWeatherOrder.getPrepayRate());
				clientOrder.setShipPayRate(clientWeatherOrder.getShipPayRate());
				clientOrder.setShipPayPeriod(clientWeatherOrder.getShipPayPeriod());
				clientOrder.setReceivePayRate(clientWeatherOrder.getReceivePayRate());
				clientOrder.setReceivePayPeriod(clientWeatherOrder.getReceivePayPeriod());
				clientOrder.setRemark(clientWeatherOrder.getRemark());
				clientOrder.setOrderStatusId(clientWeatherOrder.getOrderStatusId());
				clientOrder.setUpdateTimestamp(new Date());
				clientOrder.setClientWeatherOrderId(orderApproval.getClientOrderId());
				clientOrder.setCreateUserId(Integer.parseInt(userVo.getUserId()));
				clientOrder.setImportersRegistration(clientWeatherOrder.getImportersRegistration());
				clientOrder.setLc(clientWeatherOrder.getLc());
				clientOrder.setCertification(clientWeatherOrder.getCertification());
				clientOrder.setComplete(1);
				clientOrder.setUrgentLevelId(clientWeatherOrder.getUrgentLevelId());
				Integer seq=clientQuoteDao.findseq(clientWeatherOrder.getClientQuoteId());
				int maxSeq;
				if(seq==null){
					maxSeq=0;
				}else{
					maxSeq=seq;
				}
				if (client.getCode().startsWith("3") && clientOrderElementFinal.getOrderNumberIndex() != null) {
					String index = clientOrderElementFinal.getOrderNumberIndex().toString().replace("-", "");
					String orderNumber=clientQuote.getQuoteNumber();
					if (!"0".equals(index) && !"1".equals(index)) {
						orderNumber = orderNumber + "-" +index;
					}
					orderNumber = "ORD-" + orderNumber;//订单号组装
					clientOrder.setOrderNumber(orderNumber);
					clientOrder.setSeq(new Integer(index));
				}else {
					clientOrder.setSeq(++maxSeq);
					String orderNumber=clientQuote.getQuoteNumber();
					if(maxSeq>1){
						orderNumber = orderNumber + "-" +maxSeq;
					}
					orderNumber = "ORD-" + orderNumber;//订单号组装
					clientOrder.setOrderNumber(orderNumber);
				}
				clientOrderDao.insert(clientOrder);
			}
			ClientOrderElement record=new ClientOrderElement();
			record.setClientOrderId(clientOrder.getId());
			record.setDescription(clientOrderElementFinal.getDescription());
			record.setAmount(clientOrderElementFinal.getAmount());
			record.setLeadTime(clientOrderElementFinal.getLeadTime());
			record.setDeadline(clientOrderElementFinal.getDeadline());
			record.setPrice(clientOrderElementFinal.getPrice());
			record.setFixedCost(clientOrderElementFinal.getFixedCost());
			record.setCertificationId(clientOrderElementFinal.getCertificationId());
			record.setOrderStatusId(clientOrderElementFinal.getOrderStatusId());
			record.setClientQuoteElementId(clientWeatherOrderElement.getClientQuoteElementId());
			record.setRemark(clientWeatherOrderElement.getRemark());
			record.setBankCharges(clientOrderElementFinal.getBankCharges());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			if (clientWeatherOrderElement.getPartNumber() != null && !"".equals(clientWeatherOrderElement.getPartNumber())) {
				record.setPartNumber(clientWeatherOrderElement.getPartNumber());
			}else if (clientInquiryElement.getPartNumber() != null && !"".equals(clientInquiryElement.getPartNumber())) {
				record.setPartNumber(clientInquiryElement.getPartNumber());
			}
			if (clientWeatherOrderElement.getUnit() != null && !"".equals(clientWeatherOrderElement.getUnit())) {
				record.setUnit(clientWeatherOrderElement.getUnit());
			}else if (clientInquiryElement.getUnit() != null && !"".equals(clientInquiryElement.getUnit())) {
				record.setUnit(clientInquiryElement.getUnit());
			}
			if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() < 0) {
				record.setElementStatusId(709);
			}else {
				record.setElementStatusId(708);
			}
			//record.setElementStatusId(704);
			clientOrderElementDao.insertSelective(record);
			
			//历史订单价格
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
			if (clientInquiryElement.getBsn() != null && !"".equals(clientInquiryElement.getBsn())) {
				Calendar now = Calendar.getInstance();
				String year = String.valueOf(now.get(Calendar.YEAR)).substring(2);
				List<HistoricalOrderPrice> his = historicalOrderPriceDao.getByClient(clientInquiry.getClientId().toString(), clientInquiryElement.getBsn(),year);
				if (his.size() > 0) {
					for (int i = 0; i < his.size(); i++) {
						his.get(i).setPrice(record.getPrice());
						his.get(i).setAmount(record.getAmount());
						historicalOrderPriceDao.updateByPrimaryKeySelective(his.get(i));
					}
				}else {
					HistoricalOrderPrice historicalOrderPrice = new HistoricalOrderPrice();
					historicalOrderPrice.setBsn(clientInquiryElement.getBsn());
					historicalOrderPrice.setClientId(clientInquiry.getClientId());
					historicalOrderPrice.setAmount(record.getAmount());
					historicalOrderPrice.setPrice(record.getPrice());
					historicalOrderPrice.setYear(new Integer(year));
					historicalOrderPriceDao.insertSelective(historicalOrderPrice);
				}
			}
			
			//生成形式发票
			List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrder.getId(),0);
			if(null!=clientInvoices&&clientInvoices.size()>0){
					 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					 clientInvoiceElement.setAmount(record.getAmount());
					 clientInvoiceElement.setTerms(100);
					 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
					 clientInvoiceElement.setClientOrderElementId(record.getId());
					 ClientInvoiceElement element=clientInvoiceElementDao.selectByCoeIdAndCiId(clientInvoiceElement);
					 if(null==element){
						 clientInvoiceElementDao.insert(clientInvoiceElement);
					 }
			}else{
//				String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
//						"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
//				for (int j = 0; j < ziMu.length; j++) {
//					ClientInvoice incoiceNumber=clientInvoiceDao.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
//					if(null!=incoiceNumber){
//						continue;
//					}else{
				ClientInvoice clientInvoice=new ClientInvoice();
				clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+"PR");
				clientInvoice.setClientOrderId(clientOrder.getId());
				clientInvoice.setInvoiceDate(new Date());
				clientInvoice.setTerms(100);
				clientInvoice.setInvoiceType(0);
				clientInvoice.setInvoiceStatusId(0);
				clientInvoiceDao.insert(clientInvoice);
					 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					 clientInvoiceElement.setAmount(record.getAmount());
					 clientInvoiceElement.setTerms(100);
					 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
					 clientInvoiceElement.setClientOrderElementId(record.getId());
					 clientInvoiceElementDao.insert(clientInvoiceElement);
//					 break;
//					}
//				}
			}
			if (clientOrder.getPrepayRate() > 0) {
				List<ClientInvoice> prepayInvoices=clientInvoiceDao.selectByclientOrderId(clientOrder.getId(),1);
				if (prepayInvoices.size() > 0) {
					ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					 clientInvoiceElement.setAmount(record.getAmount());
					 clientInvoiceElement.setTerms((int)(100*clientOrder.getPrepayRate()));
					 clientInvoiceElement.setClientInvoiceId(prepayInvoices.get(0).getId());
					 clientInvoiceElement.setClientOrderElementId(record.getId());
					 ClientInvoiceElement element=clientInvoiceElementDao.selectByCoeIdAndCiId(clientInvoiceElement);
					 if(null==element){
						 clientInvoiceElementDao.insert(clientInvoiceElement);
					 }
				}else {
					String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
							"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
					for (int j = 0; j < ziMu.length; j++) {
						ClientInvoice incoiceNumber=clientInvoiceDao.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
						if(null!=incoiceNumber){
							continue;
						}else{
							ClientInvoice clientInvoice=new ClientInvoice();
							clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
							clientInvoice.setClientOrderId(clientOrder.getId());
							clientInvoice.setInvoiceDate(new Date());
							Double terms=clientOrder.getPrepayRate()*100;
							clientInvoice.setTerms(terms.intValue());
							clientInvoice.setInvoiceType(1);
							clientInvoice.setInvoiceStatusId(0);
							clientInvoiceDao.insert(clientInvoice);
							ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
							clientInvoiceElement.setAmount(record.getAmount());
							clientInvoiceElement.setTerms(terms.intValue());
							clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
							clientInvoiceElement.setClientOrderElementId(record.getId());
							clientInvoiceElementDao.insert(clientInvoiceElement);
							break;
						}
					}
				}
			}
			
			
    		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
				if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703 || 
						clientInquiryElement.getElementStatusId().equals(711) || clientInquiryElement.getElementStatusId()==711){
					clientInquiryElement.setElementStatusId(710);
					clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
    			}
			}
			
			for (OrderApproval orderApproval2 : approvals) {
				if(null!=orderApproval2.getSupplierWeatherOrderElementId()){
					SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval2.getSupplierWeatherOrderElementId());
					SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
					SupplierOrder supplierOrder=new SupplierOrder();
					supplierOrder.setClientOrderId(record.getClientOrderId());
					supplierOrder.setSupplierId(element.getSupplierId());
					SupplierOrder supplierOrder2 = supplierOrderService.findByClientOrderId(supplierOrder);
					SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
					supplierOrderElement.setAmount(supplierWeatherOrderElement.getAmount());
					supplierOrderElement.setPrice(supplierWeatherOrderElement.getPrice());
					supplierOrderElement.setLeadTime(supplierWeatherOrderElement.getLeadTime());
					supplierOrderElement.setDeadline(supplierWeatherOrderElement.getDeadline());
					supplierOrderElement.setShipWayId(supplierWeatherOrderElement.getShipWayId());
					supplierOrderElement.setDestination(supplierWeatherOrderElement.getDestination());
					supplierOrderElement.setClientOrderElementId(record.getId());
					supplierOrderElement.setSupplierQuoteElementId(supplierWeatherOrderElement.getSupplierQuoteElementId());
					supplierOrderElement.setBankCost(supplierWeatherOrderElement.getBankCost());
					if (supplierOrder2!=null &&null==supplierOrder2.getOrderType()) {
						supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
						int count = supplierOrderElementDao.getOrderCount(supplierOrder2.getId());
						if (count == 0) {
							supplierOrderElement.setItem(1);
						}else {
							supplierOrderElement.setItem(count+1);
						}
						supplierOrderElementDao.insertSelective(supplierOrderElement);
					}else {
						Jbpm4Jbyj jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", "ORDER_APPROVAL.ID."+orderApproval.getId());
				 		if(null==jbyj){
				 			jbyj=jbpm4JbyjService.findByTask("采购审核","ORDER_APPROVAL.ID."+orderApproval.getId());
				 		}
				 		if(null==jbyj){
				 			jbyj=jbpm4JbyjService.findByTask("采购审核库存","ORDER_APPROVAL.ID."+orderApproval.getId());
				 		}
				 		if(null!=jbyj){
				 			supplierOrder.setCreateUserId(Integer.parseInt(jbyj.getUserId()));
				 		}
						supplierOrder.setOrderDate(new Date());
						Supplier supplier=supplierDao.selectByPrimaryKey(supplierOrder.getSupplierId());
						ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
						supplierOrder.setExchangeRate(exchangeRate.getRate());
						supplierOrder.setCurrencyId(supplier.getCurrencyId());
						supplierOrder.setPrepayRate(supplier.getPrepayRate());
						supplierOrder.setShipPayRate(supplier.getShipPayRate());
						supplierOrder.setReceivePayPeriod(supplier.getReceivePayPeriod());
						supplierOrder.setReceivePayRate(supplier.getReceivePayRate());
						supplierOrder.setUrgentLevelId(clientWeatherOrder.getUrgentLevelId());
						supplierOrderService.insertSelective(supplierOrder);
						supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
						int count = supplierOrderElementDao.getOrderCount(supplierOrderElement.getSupplierOrderId());
						if (count == 0) {
							supplierOrderElement.setItem(1);
						}else {
							supplierOrderElement.setItem(count+1);
						}
						supplierOrderElementDao.insertSelective(supplierOrderElement);
					}
					
					SupplierOrder supplierOrder1 = supplierOrderDao.selectByPrimaryKey(supplierOrderElement.getSupplierOrderId());
					if (!"".equals(record.getElementStatusId()) && record.getElementStatusId()!=null) {
						/*if (record.getElementStatusId().equals(704) || record.getElementStatusId()==704|| 
								record.getElementStatusId().equals(712) || record.getElementStatusId()==712){*/
							/*if (supplierOrder1.getPrepayRate() > 0) {
								record.setElementStatusId(706);
							}else {
								record.setElementStatusId(705);
							}*/
							if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() < 0) {
								record.setElementStatusId(709);
							}else {
								record.setElementStatusId(708);
							}
							supplierOrderElement.setElementStatusId(705);
							supplierOrderElementDao.updateByPrimaryKeySelective(supplierOrderElement);
							clientOrderElementDao.updateByPrimaryKeySelective(record);
						/*}*/
					}
				}
			}
			if(in){
				 List<OrderApproval> storageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 0);
				 List<OrderApproval> onpassstorageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 1);
					if(jbpm4Tasks.size()<2){
						if(storageList.size()>0){
							LinkedList<OrderApproval> sortList = new LinkedList<OrderApproval>();
							for (OrderApproval orderApproval2 : storageList) {
								ImportPackageElement importPackageElement = importpackageElementService.selectByPrimaryKey(orderApproval2.getImportPackageElementId());
								if (clientInquiryService.getCodeFromPartNumber(importPackageElement.getPartNumber()).equals(clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()))) {
									sortList.addFirst(orderApproval2);
								}else {
									sortList.add(orderApproval2);
								}
							}
							UserVo user = ContextHolder.getCurrentUser();
						 	List<EmailVo> emailVos=new ArrayList<EmailVo>();
							List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
							for (OrderApproval orderApproval2 : sortList) {
								if(orderApproval2.getOccupy()==1){
									StorageFlowVo flowVos=importpackageElementService.findStorageBySoeIdAndIpeId(orderApproval2.getSupplierQuoteElementId(),orderApproval2.getImportPackageElementId());
									if (flowVos != null) {
										flowVos.setUseAmount(orderApproval2.getAmount());
										supplierList.add(flowVos);
									}
								}
							}
							List<EmailVo> emailVo=new ArrayList<EmailVo>();
							ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(record.getId());
							ClientOrder clientOrder1=clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
							emailVo=Storage(supplierList,clientOrderElement,clientOrder1);
						 
							/*if (!"".equals(record.getElementStatusId()) && record.getElementStatusId()!=null) {*/
								record.setElementStatusId(708);
								clientOrderElementDao.updateByPrimaryKeySelective(record);
							/*}*/
							emailVos.addAll(emailVo);
							for (int i = 0; i < emailVos.size(); i++) {
								emailVos.get(i).setUserId(new Integer(user.getUserId()));
								storageToOrderEmailDao.insertSelectiveByEmailVo(emailVos.get(i));
							}
							//sendEmail(emailVos,user.getUserId());
						} 
						if(onpassstorageList.size()>0){
							List<OnPassageStorage> onPassageStroages = new ArrayList<OnPassageStorage>();
							ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(record.getId());
							for (OrderApproval orderApproval2 : onpassstorageList) {
								if(orderApproval2.getOccupy()==1){
									OnPassageStorage onPassageStorage=new OnPassageStorage();
									onPassageStorage.setSupplierOrderElementId(orderApproval2.getSupplierOrderElementId());
									onPassageStorage.setAmount(orderApproval2.getAmount());
									if(orderApproval2.getAmount()>=clientOrderElement.getAmount()){
										onPassageStorage.setBiggerAmount(1);
									}
									onPassageStroages.add(onPassageStorage);
								}
							}
							useOnPassageStorage(onPassageStroages,clientOrderElement);
						}
					}
			}
		}
		
		
		return variables;
	}

	
	@Override
	public Object pricenopass(String businessKey, String taskId, String outcome, String assignee, String comment) {
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
		ClientOrderElementFinal clientOrderElementFinal=clientOrderElementFinalDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
		ClientOrder clientOrder=clientOrderDao.selectByPrimaryKey(orderApproval.getClientOrderId());
		Double sqePrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientOrder.getExchangeRate();
		Double weatherPrice=new BigDecimal(sqePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		Double orderPrice=clientOrderElementFinal.getPrice();
		 Double profitMargin=((orderPrice)-weatherPrice)/orderPrice;
		if(profitMargin>0.18){
			List<Jbpm4Task> jbpm4Tasks=	jbpm4TaskDao.findByYwTableElementId(orderApproval.getClientOrderElementId());
			 List<OrderApproval> storageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 0);
			 List<OrderApproval> onpassstorageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 1);
				if(jbpm4Tasks.size()<2){
					if(storageList.size()>0){
					UserVo user = ContextHolder.getCurrentUser();
				 	List<EmailVo> emailVos=new ArrayList<EmailVo>();
					List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
					 for (OrderApproval orderApproval2 : storageList) {
						StorageFlowVo flowVos=importpackageElementService.findStorageBySoeIdAndIpeId(orderApproval2.getSupplierQuoteElementId(),orderApproval2.getImportPackageElementId());
						supplierList.add(flowVos);
					 }
					 List<EmailVo> emailVo=new ArrayList<EmailVo>();
					 ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
					 ClientOrder clientOrder1=clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
					 emailVo=Storage(supplierList,clientOrderElement,clientOrder1);
					 emailVos.addAll(emailVo);
					 sendEmail(emailVos,user.getUserId());
					} 
					if(onpassstorageList.size()>0){
						List<OnPassageStorage> onPassageStroages = new ArrayList<OnPassageStorage>();
						ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
						for (OrderApproval orderApproval2 : onpassstorageList) {
							OnPassageStorage onPassageStorage=new OnPassageStorage();
							onPassageStorage.setSupplierOrderElementId(orderApproval2.getSupplierOrderElementId());
							onPassageStorage.setAmount(orderApproval2.getAmount());
							if(orderApproval2.getAmount()>=clientOrderElement.getAmount()){
								onPassageStorage.setBiggerAmount(1);
							}
							onPassageStroages.add(onPassageStorage);
						}
						useOnPassageStorage(onPassageStroages,clientOrderElement);
					}
				}
			variables.put("to", "降价后利润通过");
			orderApproval.setSpzt(235);
			orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		}else{
			variables.put("to", "利润率低于标准");
		}
		return variables;
	}
    
	@Override
	public Object pricepass(String businessKey, String taskId, String outcome, String assignee, String comment) {
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		List<Jbpm4Task> jbpm4Tasks=	jbpm4TaskDao.findByYwTableElementId(orderApproval.getClientOrderElementId());
		 List<OrderApproval> storageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 0);
		 List<OrderApproval> onpassstorageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 1);
				if(jbpm4Tasks.size()<2){
					if(storageList.size()>0){
					UserVo user = ContextHolder.getCurrentUser();
				 	List<EmailVo> emailVos=new ArrayList<EmailVo>();
					List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
					 for (OrderApproval orderApproval2 : storageList) {
						StorageFlowVo flowVos=importpackageElementService.findStorageBySoeIdAndIpeId(orderApproval2.getSupplierQuoteElementId(),orderApproval2.getImportPackageElementId());
						supplierList.add(flowVos);
					 }
					 List<EmailVo> emailVo=new ArrayList<EmailVo>();
					 ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
					 ClientOrder clientOrder1=clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
					 emailVo=Storage(supplierList,clientOrderElement,clientOrder1);
					 emailVos.addAll(emailVo);
					 sendEmail(emailVos,user.getUserId());
					} 
					if(onpassstorageList.size()>0){
						List<OnPassageStorage> onPassageStroages = new ArrayList<OnPassageStorage>();
						ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
						for (OrderApproval orderApproval2 : onpassstorageList) {
							OnPassageStorage onPassageStorage=new OnPassageStorage();
							onPassageStorage.setSupplierOrderElementId(orderApproval2.getSupplierOrderElementId());
							onPassageStorage.setAmount(orderApproval2.getAmount());
							if(orderApproval2.getAmount()>=clientOrderElement.getAmount()){
								onPassageStorage.setBiggerAmount(1);
							}
							onPassageStroages.add(onPassageStorage);
						}
						useOnPassageStorage(onPassageStroages,clientOrderElement);
					}
				}
				orderApproval.setSpzt(235);
				orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		return null;
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object zjlnopass(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		orderApproval.setSpzt(233);
		orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		return null;
	}

	@Override
	public List<Integer> findUser(Integer clientOrderElementId) {
		return clientOrderElementDao.findUser(clientOrderElementId);
	}

	@Override
	public Object weatherorderpass(String businessKey, String taskId, String outcome, String assignee, String comment) {
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(orderApproval.getClientOrderElementId());
		ClientProfitmargin clientProfitmargin=clientProfitmarginDao.selectByClientId(clientOrderElementVo.getClientId());
		SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
	
		if(supplierWeatherOrderElement.getSupplierStatus().equals(0)||supplierWeatherOrderElement.getSupplierStatus()==0){
			variables.put("to", "通过");
			return variables;
		}
		
		SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
		ClientWeatherOrder clientWeatherOrder=clientWeatherOrderDao.selectByPrimaryKey(orderApproval.getClientOrderId());
		ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		Integer sCurrencyId=element.getCurrencyId();
		Integer cCurrencyId=clientWeatherOrder.getCurrencyId();
		Double weatherPrice=supplierWeatherOrderElement.getPrice();
		if(!sCurrencyId.equals(cCurrencyId)){
			ClientQuote clientQuote = clientQuoteDao.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
			Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
			//判断是否7开头客户
			ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
			if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
				if (!sCurrencyId.toString().equals("11")) {
					ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sCurrencyId);
					Double relative = new BigDecimal(currentRate.getRelativeRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
					relative = relative*usRate.getRelativeRate();
					weatherPrice = clientQuoteService.caculatePrice(new BigDecimal(supplierWeatherOrderElement.getPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
				}else {
					weatherPrice = clientQuoteService.caculatePrice(new BigDecimal(supplierWeatherOrderElement.getPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
				}
			}else {
				//正常的价格计算方法
				weatherPrice = clientQuoteService.caculatePrice(new BigDecimal(supplierWeatherOrderElement.getPrice()), new BigDecimal(element.getExchangeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
				//weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
			}
			weatherPrice=new BigDecimal(weatherPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//			weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
//			weatherPrice=new BigDecimal(weatherPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
		Double orderPrice=clientWeatherOrderElement.getPrice();
		Double fixedCost=clientWeatherOrderElement.getFixedCost();
		Jbpm4Jbyj zjljbyj=jbpm4JbyjService.findByTask("总经理审核利润", "ORDER_APPROVAL.ID."+orderApproval.getId());
		if(fixedCost != null && fixedCost < 1){
			fixedCost=orderPrice*fixedCost;
		}else if (fixedCost == null) {
			fixedCost = 0.0;
		}
		Double bankChargesOrder=clientWeatherOrderElement.getBankCharges();
		if(bankChargesOrder != null && bankChargesOrder < 1){
			bankChargesOrder=orderPrice*bankChargesOrder;
		}else if (bankChargesOrder == null) {
			bankChargesOrder = 0.0;
		}
		ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
		Double quoteFeeForExchangeBill = 0.0;
		Double quoteBankCost = 0.0;
		Double quoteHazmatFee = 0.0;
		Double quoteOtherFee = 0.0;
		if (element.getBankCost() != null && element.getBankCost() > 0) {
			BigDecimal bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(element.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
				if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
					quoteBankCost = bankCostQuote.doubleValue()/clientQuoteElement.getMoq();
				}else {
					quoteBankCost = bankCostQuote.doubleValue()/clientWeatherOrderElement.getAmount();
				}
			}else {
				quoteBankCost = bankCostQuote.doubleValue()/clientWeatherOrderElement.getAmount();
			}
		}
		if (element.getFeeForExchangeBill() != null && element.getFeeForExchangeBill() > 0) {
			BigDecimal feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(element.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
				if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
					quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientQuoteElement.getMoq();
				}else {
					quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientWeatherOrderElement.getAmount();
				}
			}else {
				quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientWeatherOrderElement.getAmount();
			}
		}
		if (element.getHazmatFee() != null && element.getHazmatFee() > 0) {
			BigDecimal hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(element.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			quoteHazmatFee = hzmatFeequote.doubleValue();
		}
		if (element.getOtherFee() != null && element.getOtherFee() > 0) {
			BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(element.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
				if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
					quoteOtherFee = otherFee.doubleValue()/clientQuoteElement.getMoq();
				}else {
					quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
				}
			}else {
				quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
			}
		}
		 Double profitMargin=(orderPrice-fixedCost-bankChargesOrder-quoteFeeForExchangeBill-quoteBankCost-quoteHazmatFee-quoteOtherFee-weatherPrice)/
				 (orderPrice-fixedCost-bankChargesOrder-quoteFeeForExchangeBill-quoteBankCost-quoteHazmatFee-quoteOtherFee);
		  BigDecimal pm=new BigDecimal(profitMargin);
		  profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(profitMargin>=clientProfitmargin.getProfitMargin()||null!=zjljbyj){
				variables.put("to", "通过");
				Jbpm4Task record=new Jbpm4Task();
				record.setTaskdefname("销售生成客户订单");
				record.setYwTableElementId(orderApproval.getClientOrderElementId());
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.selectByTaskName(record);
				for (Jbpm4Task jbpm4Task : jbpm4Tasks) {
					jbpm4Task.setDescr("销售生成客户订单");
					jbpm4TaskDao.updateByPrimaryKeySelective(jbpm4Task);
				}
				variables.put("assignee", ContextHolder.getCurrentUser().getUserId());
			}else{
				variables.put("desc", WFUtils.getDescriptionStr("提交审核"));
				variables.put("to", "利润不通过");
				variables.put("assignee", "2");
			}
		
		return variables;
	}
	


	@Override
	public Double sumPrice(Integer clientWeatherOrderId) {
		return clientOrderElementDao.sumPrice(clientWeatherOrderId);
	}

	@Override
	public void updateBybankCharges(ClientOrderElement clientOrderElement) {
		clientOrderElementDao.updateBybankCharges(clientOrderElement);		
	}

	@Override
	public Object theprice(String businessKey, String taskId, String outcome, String assignee, String comment) {
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		UserVo userVo=ContextHolder.getCurrentUser();
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		SupplierWeatherOrderElement weatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
		SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(weatherOrderElement.getSupplierQuoteElementId());
		ClientWeatherOrder clientWeatherOrder=clientWeatherOrderDao.selectByPrimaryKey(orderApproval.getClientOrderId());
		ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(orderApproval.getClientOrderElementId());
		ClientProfitmargin clientProfitmargin=clientProfitmarginDao.selectByClientId(clientOrderElementVo.getClientId());
		Integer sCurrencyId=element.getCurrencyId();
		Integer cCurrencyId=clientWeatherOrder.getCurrencyId();
		Double weatherPrice=weatherOrderElement.getPrice();
		if(!sCurrencyId.equals(cCurrencyId)){
		 weatherPrice=weatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
		 weatherPrice=new BigDecimal(weatherPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		ClientOrderElementFinal clientOrderElementFinal=clientOrderElementFinalDao.selectByPrimaryKey(orderApproval.getClientOrderElementId());
		Double orderPrice=clientOrderElementFinal.getPrice();
		Double fixedCost=clientOrderElementFinal.getFixedCost();
		Jbpm4Jbyj zjljbyj=jbpm4JbyjService.findByTask("总经理审核订单利润", "ORDER_APPROVAL.ID."+orderApproval.getId());
		if(fixedCost<1){
			fixedCost=fixedCost*clientOrderElementFinal.getPrice();
		}
		Double bankCharges=clientOrderElementFinal.getBankCharges()*orderPrice;
		ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
		Double quoteFeeForExchangeBill = 0.0;
		Double quoteBankCost = 0.0;
		Double quoteHazmatFee = 0.0;
		Double quoteOtherFee = 0.0;
		if (element.getBankCost() != null && element.getBankCost() > 0) {
			BigDecimal bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(element.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
				if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
					quoteBankCost = bankCostQuote.doubleValue()/clientQuoteElement.getMoq();
				}else {
					quoteBankCost = bankCostQuote.doubleValue()/clientWeatherOrderElement.getAmount();
				}
			}else {
				quoteBankCost = bankCostQuote.doubleValue()/clientWeatherOrderElement.getAmount();
			}
		}
		if (element.getFeeForExchangeBill() != null && element.getFeeForExchangeBill() > 0) {
			BigDecimal feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(element.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
				if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
					quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientQuoteElement.getMoq();
				}else {
					quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientWeatherOrderElement.getAmount();
				}
			}else {
				quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/clientWeatherOrderElement.getAmount();
			}
		}
		if (element.getHazmatFee() != null && element.getHazmatFee() > 0) {
			BigDecimal hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(element.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			quoteHazmatFee = hzmatFeequote.doubleValue();
		}
		if (element.getOtherFee() != null && element.getOtherFee() > 0) {
			BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(element.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientWeatherOrder.getExchangeRate()));
			if (clientQuoteElement.getMoq() != null && !"".equals(clientQuoteElement.getMoq())) {
				if (clientQuoteElement.getMoq() > clientWeatherOrderElement.getAmount()) {
					quoteOtherFee = otherFee.doubleValue()/clientQuoteElement.getMoq();
				}else {
					quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
				}
			}else {
				quoteOtherFee = otherFee.doubleValue()/clientWeatherOrderElement.getAmount();
			}
		}
		 //Double profitMargin=((orderPrice-fixedCost-bankCharges)-weatherPrice)/orderPrice;
		Double profitMargin = (orderPrice - fixedCost - bankCharges - quoteOtherFee - quoteHazmatFee - quoteFeeForExchangeBill - quoteBankCost - weatherPrice)/
				(orderPrice - fixedCost - bankCharges - quoteOtherFee - quoteHazmatFee - quoteFeeForExchangeBill - quoteBankCost);
		  BigDecimal pm=new BigDecimal(profitMargin);
		  profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			List<Jbpm4Task> jbpm4Tasks=	jbpm4TaskDao.findByYwTableElementId(orderApproval.getClientOrderElementId());
			if(profitMargin>=clientProfitmargin.getProfitMargin()||null!=zjljbyj){
				variables.put("to", "通过");
				orderApproval.setSpzt(235);
				orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
				
				if(jbpm4Tasks.size()<2){
				List<OrderApproval> approvals=orderApprovalDao.selectByClientOrderElementId(orderApproval.getClientOrderElementId());
				ClientOrder clientOrder=clientOrderDao.findClientWeatherOrderId(orderApproval.getClientOrderId());
				
				if(null==clientOrder){
					clientOrder=new ClientOrder();
					clientOrder.setClientQuoteId(clientWeatherOrder.getClientQuoteId());
					clientOrder.setCurrencyId(clientWeatherOrder.getCurrencyId());
					clientOrder.setExchangeRate(clientWeatherOrder.getExchangeRate());
					clientOrder.setSourceNumber(clientWeatherOrder.getSourceNumber());
					clientOrder.setOrderDate(clientWeatherOrder.getOrderDate());
					clientOrder.setPrepayRate(clientWeatherOrder.getPrepayRate());
					clientOrder.setShipPayRate(clientWeatherOrder.getShipPayRate());
					clientOrder.setShipPayPeriod(clientWeatherOrder.getShipPayPeriod());
					clientOrder.setReceivePayRate(clientWeatherOrder.getReceivePayRate());
					clientOrder.setReceivePayPeriod(clientWeatherOrder.getReceivePayPeriod());
					clientOrder.setRemark(clientWeatherOrder.getRemark());
					clientOrder.setOrderStatusId(clientWeatherOrder.getOrderStatusId());
					clientOrder.setUpdateTimestamp(new Date());
					clientOrder.setClientWeatherOrderId(orderApproval.getClientOrderId());
					clientOrder.setCreateUserId(Integer.parseInt(userVo.getUserId()));
					clientOrder.setImportersRegistration(clientWeatherOrder.getImportersRegistration());
					clientOrder.setLc(clientWeatherOrder.getLc());
					clientOrder.setCertification(clientWeatherOrder.getCertification());
					clientOrder.setComplete(1);
					clientOrder.setUrgentLevelId(clientWeatherOrder.getUrgentLevelId());
					Integer seq=clientQuoteDao.findseq(clientWeatherOrder.getClientQuoteId());
					ClientQuote clientQuote=clientQuoteDao.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
					int maxSeq;
					if(seq==null){
						maxSeq=0;
					}else{
						maxSeq=seq;
					}
					clientOrder.setSeq(++maxSeq);
					String orderNumber=clientQuote.getQuoteNumber();
					if(maxSeq>1){
						orderNumber = orderNumber + "-" +maxSeq;
					}
					orderNumber = "ORD-" + orderNumber;//订单号组装
					clientOrder.setOrderNumber(orderNumber);
					clientOrderDao.insert(clientOrder);
				}
				ClientOrderElement record=new ClientOrderElement();
				record.setClientOrderId(clientOrder.getId());
				record.setDescription(clientOrderElementFinal.getDescription());
				record.setAmount(clientOrderElementFinal.getAmount());
				record.setLeadTime(clientOrderElementFinal.getLeadTime());
				record.setDeadline(clientOrderElementFinal.getDeadline());
				record.setPrice(clientOrderElementFinal.getPrice());
				record.setFixedCost(clientOrderElementFinal.getFixedCost());
				record.setCertificationId(clientOrderElementFinal.getCertificationId());
				record.setOrderStatusId(clientOrderElementFinal.getOrderStatusId());
				record.setClientQuoteElementId(clientWeatherOrderElement.getClientQuoteElementId());
				if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() < 0) {
					record.setElementStatusId(709);
				}else {
					record.setElementStatusId(708);
				}
				//record.setElementStatusId(704);
				record.setRemark(clientWeatherOrderElement.getRemark());
				record.setBankCharges(clientOrderElementFinal.getBankCharges());
				clientOrderElementDao.insertSelective(record);
				
				//生成形式发票
				List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrder.getId(),0);
				if(null!=clientInvoices&&clientInvoices.size()>0){
						 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
						 clientInvoiceElement.setAmount(record.getAmount());
						 clientInvoiceElement.setTerms(100);
						 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
						 clientInvoiceElement.setClientOrderElementId(record.getId());
						 ClientInvoiceElement invoiceElement=clientInvoiceElementDao.selectByCoeIdAndCiId(clientInvoiceElement);
						 if(null==invoiceElement){
							 clientInvoiceElementDao.insert(clientInvoiceElement);
						 }
				}else{
				/*	String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
							"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
					for (int j = 0; j < ziMu.length; j++) {
						ClientInvoice incoiceNumber=clientInvoiceDao.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[j]);
						if(null!=incoiceNumber){
							continue;
						}else{*/
					ClientInvoice clientInvoice=new ClientInvoice();
					clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+"PR");
					clientInvoice.setClientOrderId(clientOrder.getId());
					clientInvoice.setInvoiceDate(new Date());
					clientInvoice.setTerms(100);
					clientInvoice.setInvoiceType(0);
					clientInvoice.setInvoiceStatusId(0);
					clientInvoiceDao.insert(clientInvoice);
						 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
						 clientInvoiceElement.setAmount(record.getAmount());
						 clientInvoiceElement.setTerms(100);
						 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
						 clientInvoiceElement.setClientOrderElementId(record.getId());
						 clientInvoiceElementDao.insert(clientInvoiceElement);
//						 break;
						}
//					}
//				}
				
        		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
        		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
					if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703 || 
							clientInquiryElement.getElementStatusId().equals(711) || clientInquiryElement.getElementStatusId()==711){
						clientInquiryElement.setElementStatusId(710);
						clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
        			}
    			}
				
				for (OrderApproval orderApproval2 : approvals) {
					if(null!=orderApproval2.getSupplierWeatherOrderElementId()){
						SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval2.getSupplierWeatherOrderElementId());
						SupplierQuoteElement supplierQuoteElement=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
						SupplierOrder supplierOrder=new SupplierOrder();
						supplierOrder.setClientOrderId(record.getClientOrderId());
						supplierOrder.setSupplierId(supplierQuoteElement.getSupplierId());
						SupplierOrder supplierOrder2 = supplierOrderService.findByClientOrderId(supplierOrder);
						SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
						supplierOrderElement.setAmount(supplierWeatherOrderElement.getAmount());
						supplierOrderElement.setPrice(supplierWeatherOrderElement.getPrice());
						supplierOrderElement.setLeadTime(supplierWeatherOrderElement.getLeadTime());
						supplierOrderElement.setDeadline(supplierWeatherOrderElement.getDeadline());
						supplierOrderElement.setShipWayId(supplierWeatherOrderElement.getShipWayId());
						supplierOrderElement.setDestination(supplierWeatherOrderElement.getDestination());
						supplierOrderElement.setClientOrderElementId(record.getId());
						supplierOrderElement.setSupplierQuoteElementId(supplierWeatherOrderElement.getSupplierQuoteElementId());
						if (supplierOrder2!=null &&null==supplierOrder2.getOrderType()) {
							supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
							int amount = supplierOrderElementDao.getOrderCount(supplierOrder2.getId());
							if (amount == 0) {
								supplierOrderElement.setItem(1);
							}else {
								supplierOrderElement.setItem(amount+1);
							}
							supplierOrderElementDao.insertSelective(supplierOrderElement);
						}else {
							Jbpm4Jbyj jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", "ORDER_APPROVAL.ID."+orderApproval.getId());
					 		if(null==jbyj){
					 			jbyj=jbpm4JbyjService.findByTask("采购审核","ORDER_APPROVAL.ID."+orderApproval.getId());
					 		}
					 		if(null==jbyj){
					 			jbyj=jbpm4JbyjService.findByTask("采购审核库存","ORDER_APPROVAL.ID."+orderApproval.getId());
					 		}
					 		if(null!=jbyj){
					 			supplierOrder.setCreateUserId(Integer.parseInt(jbyj.getUserId()));
					 		}
							supplierOrder.setOrderDate(new Date());
							Supplier supplier=supplierDao.selectByPrimaryKey(supplierOrder.getSupplierId());
							ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
							supplierOrder.setExchangeRate(exchangeRate.getRate());
							supplierOrder.setCurrencyId(supplier.getCurrencyId());
							supplierOrder.setPrepayRate(supplier.getPrepayRate());
							supplierOrder.setShipPayRate(supplier.getShipPayRate());
							supplierOrder.setReceivePayPeriod(supplier.getReceivePayPeriod());
							supplierOrder.setReceivePayRate(supplier.getReceivePayRate());
							supplierOrder.setUrgentLevelId(clientWeatherOrder.getUrgentLevelId());
							supplierOrderService.insertSelective(supplierOrder);
							supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
							int amount = supplierOrderElementDao.getOrderCount(supplierOrder.getId());
							if (amount == 0) {
								supplierOrderElement.setItem(1);
							}else {
								supplierOrderElement.setItem(amount+1);
							}
							supplierOrderElementDao.insertSelective(supplierOrderElement);
							
							}
						SupplierOrder supplierOrder1 = supplierOrderDao.selectByPrimaryKey(supplierOrderElement.getSupplierOrderId());
						if (!"".equals(record.getElementStatusId()) && record.getElementStatusId()!=null) {
							/*if (record.getElementStatusId().equals(704) || record.getElementStatusId()==704|| 
									record.getElementStatusId().equals(712) || record.getElementStatusId()==712){
								if (supplierOrder1.getPrepayRate() > 0) {
									record.setElementStatusId(706);
								}else {
									record.setElementStatusId(705);
								}*/
								if (clientOrder.getPrepayRate() != null && clientOrder.getPrepayRate() < 0) {
									record.setElementStatusId(709);
								}else {
									record.setElementStatusId(708);
								}
								supplierOrderElement.setElementStatusId(707);
								clientOrderElementDao.updateByPrimaryKeySelective(record);
							/*}*/
						}
					}
				}
				
			
				 List<OrderApproval> storageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 0);
				 List<OrderApproval> onpassstorageList=orderApprovalDao.selectByCoeIdAndState(orderApproval.getClientOrderElementId(), 1, 1);
					
						if(storageList.size()>0){
						UserVo user = ContextHolder.getCurrentUser();
					 	List<EmailVo> emailVos=new ArrayList<EmailVo>();
						List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
						 for (OrderApproval orderApproval2 : storageList) {
							 if(orderApproval2.getOccupy()==1){
								StorageFlowVo flowVos=importpackageElementService.findStorageBySoeIdAndIpeId(orderApproval2.getSupplierQuoteElementId(),orderApproval2.getImportPackageElementId());
								supplierList.add(flowVos);
							 }
						 }
						 List<EmailVo> emailVo=new ArrayList<EmailVo>();
						 ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(record.getId());
						 ClientOrder clientOrder1=clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
						 emailVo=Storage(supplierList,clientOrderElement,clientOrder1);
						 
							if (!"".equals(record.getElementStatusId()) && record.getElementStatusId()!=null) {
								record.setElementStatusId(708);
								clientOrderElementDao.updateByPrimaryKeySelective(record);
							}
							
						 emailVos.addAll(emailVo);
						 sendEmail(emailVos,user.getUserId());
						} 
						if(onpassstorageList.size()>0){
							List<OnPassageStorage> onPassageStroages = new ArrayList<OnPassageStorage>();
							ClientOrderElement clientOrderElement=clientOrderElementDao.selectByPrimaryKey(record.getId());
							for (OrderApproval orderApproval2 : onpassstorageList) {
								if(orderApproval2.getOccupy()==1){
									OnPassageStorage onPassageStorage=new OnPassageStorage();
									onPassageStorage.setSupplierOrderElementId(orderApproval2.getSupplierOrderElementId());
									onPassageStorage.setAmount(orderApproval2.getAmount());
									if(orderApproval2.getAmount()>=clientOrderElement.getAmount()){
										onPassageStorage.setBiggerAmount(1);
									}
									onPassageStroages.add(onPassageStorage);
								}
							}
							useOnPassageStorage(onPassageStroages,clientOrderElement);
						}
					}
			}else{
				variables.put("to", "利润率低于标准");
				variables.put("assignee", "2");
			}
			return variables;
	}

	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object cancellationorder(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		orderApproval.setSpzt(233);
		orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
		return null;
	}
	
	/**
	 * 根据流程id退回选择处理人
	 * **/
	public Object returnUser(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
//		String id=flowService.getIdFromBusinessKey(businessKey);
		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", businessKey);
		variables.put("assignee", jbpm4Jbyj.getUserId());
		String[] keys = businessKey.split("\\.");
		UserVo userVo = ContextHolder.getCurrentUser();
		RoleVo roleVo = userService.getPower(new Integer(jbpm4Jbyj.getUserId()));
 		List<Integer> userList = userService.getLeadersByRole(new Integer(roleVo.getRoleId()));
 		Jbpm4Task jbpm4Task = jbpm4TaskDao.selectByPrimaryKey(new BigDecimal(taskId));
 		if (!userVo.getUserId().equals(userList.get(0).toString())) {
 			SendBackFlowMessage sendBackFlowMessage = new SendBackFlowMessage();
 			sendBackFlowMessage.setOrderApprovalId(new Integer(keys[keys.length-1]));
 			sendBackFlowMessage.setUserId(userList.get(0));
 			sendBackFlowMessage.setDescription(jbpm4Task.getName());
 			sendBackFlowMessageDao.insertSelective(sendBackFlowMessage);
		}
		return variables;
	}
	
	public Integer getTotalCount(Integer clientOrderId){
		return clientOrderElementDao.getTotalCount(clientOrderId);
	}
    
    public Integer getUnfinishCount(Integer clientOrderId){
    	return clientOrderElementDao.getUnfinishCount(clientOrderId);
    }
    
    public List<String> getSupplierNames(Integer clientOrderElementId){
    	return clientOrderElementDao.getSupplierNames(clientOrderElementId);
    }
    
    public Double getTotalById(Integer id){
    	return clientOrderElementDao.getTotalById(id);
    }
    
    public List<ClientOrderElement> checkOrderElement(ClientOrderElement clientOrderElement){
    	return clientOrderElementDao.checkOrderElement(clientOrderElement);
    }

}
