package com.naswork.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Array;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementPrepareDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierImportElementDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.UserDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierImportElement;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.GyExcelService;
import com.naswork.service.ImportPackagePaymentElementPrepareService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("importPackagePaymentElementPrepareService")
public class ImportPackagePaymentElementPrepareServiceImpl implements
		ImportPackagePaymentElementPrepareService {

	@Resource
	private ImportPackagePaymentElementPrepareDao importPackagePaymentElementPrepareDao;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private UserDao userDao;
	@Resource
	private SupplierOrderDao supplierOrderDao;
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private SupplierImportElementDao supplierImportElementDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	
	
	public void insertSelective(ImportPackagePaymentElementPrepare record){
		importPackagePaymentElementPrepareDao.insertSelective(record);
		if (record.getSupplierOrderElementId() != null && !"".equals(record.getSupplierOrderElementId())) {
			SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(record.getSupplierOrderElementId());
			ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement.getClientOrderElementId());
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			/*if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
				if (clientOrderElement.getElementStatusId().equals(706) || clientOrderElement.getElementStatusId()==706 ||
						clientOrderElement.getElementStatusId().equals(704) || clientOrderElement.getElementStatusId()==704|| 
								clientOrderElement.getElementStatusId().equals(712) || clientOrderElement.getElementStatusId()==712){
					clientOrderElement.setElementStatusId(705);*/
					/*ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
					if (clientOrder.getPrepayRate() != null && !"".equals(clientOrder.getPrepayRate()) && clientOrder.getPrepayRate() < 0) {
						clientOrderElement.setElementStatusId(709);
					}else {
						clientOrderElement.setElementStatusId(708);
					}
					clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);*/
				/*}
			}*/
		}
	}

	public ImportPackagePaymentElementPrepare selectByPrimaryKey(Integer id){
		return importPackagePaymentElementPrepareDao.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ImportPackagePaymentElementPrepare record){
		return importPackagePaymentElementPrepareDao.updateByPrimaryKeySelective(record);
	}
	
	public void add(ImportPackagePaymentElementPrepare record){
		ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare = importPackagePaymentElementPrepareDao.selectBySupplierIdAndSupplierElementId(record);
		if (importPackagePaymentElementPrepare!=null) {
			importPackagePaymentElementPrepare.setAmount(importPackagePaymentElementPrepare.getAmount()+record.getAmount());
			importPackagePaymentElementPrepareDao.updateByPrimaryKeySelective(importPackagePaymentElementPrepare);
		}else {
			importPackagePaymentElementPrepareDao.insertSelective(record);
		}
		
	}
	
	public List<ImportPackagePaymentElementPrepare> selectBySupplierId(Integer selectBySupplierId){
		return importPackagePaymentElementPrepareDao.selectBySupplierId(selectBySupplierId);
	}
	
    public List<ImportPackagePaymentElementPrepare> selectByImportPackageId(Integer importPackageId){
    	return importPackagePaymentElementPrepareDao.selectByImportPackageId(importPackageId);
    }
    
    public Double selectPrepayRateById(Integer id){
    	return importPackagePaymentElementPrepareDao.selectPrepayRateById(id);
    }
    
    public boolean checkImportAmount(Integer importPackageId,Integer userId) {
		List<ImportPackagePaymentElementPrepare> list = importPackagePaymentElementPrepareDao.selectByImportPackageId(importPackageId);
		List<ImportPackagePaymentElementPrepare> elementList = new ArrayList<ImportPackagePaymentElementPrepare>();
		List<Integer> emailList = new ArrayList<Integer>();
		List<String> emails = new ArrayList<String>();
		for (ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare : list) {
			Double prepayRate = importPackagePaymentElementPrepareDao.selectPrepayRateById(importPackagePaymentElementPrepare.getId());
			
			if (prepayRate.equals(new Double(100))) {
				SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(importPackagePaymentElementPrepare.getSupplierOrderElementId());
				SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(supplierOrderElement.getSupplierOrderId());
				if (supplierOrderElement.getAmount()>importPackagePaymentElementPrepare.getAmount()) {
					elementList.add(importPackagePaymentElementPrepare);
				}
			}
		}
		for (ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare2 : elementList) {
			SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(importPackagePaymentElementPrepare2.getSupplierOrderElementId());
			if (!emailList.contains(supplierOrderElement.getSupplierOrderId())) {
				emailList.add(supplierOrderElement.getSupplierOrderId());
			}
		}
		for (Integer integer : emailList) {
			SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(integer);
			List<UserVo> userVo = userDao.selectBySupplierId(supplierOrder.getSupplierId());
			for (UserVo userVo2 : userVo) {
				if (!emails.contains(userVo2.getEmail())) {
					emails.add(userVo2.getEmail());
				}
			}	
			List<UserVo> finance = userDao.getByRole("财务");
			for (UserVo userVo2 : finance) {
				if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail() != null) {
					emails.add(userVo2.getEmail());
				}
			}	
			
			
			if (emails.size()>0) {
				UserVo current = userDao.findUserByUserId(userId.toString());
				List<String> user = new ArrayList<String>();
				ExchangeMail exchangeMail = new ExchangeMail();
				exchangeMail.setUsername(current.getEmail());
				exchangeMail.setPassword(current.getEmailPassword());
				StringBuffer bodyText = new StringBuffer();
				bodyText.append("订单").append(supplierOrder.getOrderNumber()).append(":");
				for (ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare2 : elementList) {
					SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(importPackagePaymentElementPrepare2.getSupplierOrderElementId());
					if (supplierOrderElement.getSupplierOrderId().equals(integer)) {
						SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectBySupplierOrderElementId(importPackagePaymentElementPrepare2.getSupplierOrderElementId());
						bodyText.append("件号").append(supplierQuoteElement.getPartNumber())
						.append("入库数量：").append(importPackagePaymentElementPrepare2.getAmount()).append("，订单数量：").append(supplierOrderElement.getAmount()).append(",");
					}
					
				}
				bodyText.deleteCharAt(bodyText.length()-1);  
				exchangeMail.init();
				String path = "";
				List<String> bcc = new ArrayList<String>();
				try {
					exchangeMail.doSend("预付全款货物未到齐", emails, user, bcc, bodyText.toString(), path);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			
		}
		return true;
	}
    
    
    public void checkOverLeadTime() {
    	List<UserVo> users = userDao.getUsers();
    	for (UserVo userVo3 : users) {
    		List<SupplierOrder> orderList = supplierOrderDao.getIdByLeadTime(new Integer(userVo3.getUserId()));
    		List<SupplierOrder> list = new ArrayList<SupplierOrder>();
    		for (SupplierOrder supplierOrder : orderList) {
    			List<SupplierImportElement> importList = supplierImportElementDao.selectBySupplierOrderId(supplierOrder.getId());
    			boolean imp = false;
    			for (SupplierImportElement supplierImportElement : importList) {
    				if (supplierImportElement!=null) {
    					imp = true;
    				}
    			}
    			if (imp==false) {
    				if (!list.contains(supplierOrder)) {
    					list.add(supplierOrder);
    				}
    				
    			}
    		}
    		for (SupplierOrder supplierOrder : list) {
    			supplierOrder.setAllPrepayNotImportStatus(1);
				supplierOrderDao.updateByPrimaryKeySelective(supplierOrder);
    			List<String> emails = new ArrayList<String>();
    			StringBuffer buffer = new StringBuffer();
    			List<UserVo> userVo = userDao.selectBySupplierId(supplierOrder.getSupplierId());
    			for (UserVo userVo2 : userVo) {
    				if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail()!=null) {
    					emails.add(userVo2.getEmail());
    				}
    			}
    			
    			List<UserVo> finance = userDao.getByRole("财务");
    			for (UserVo userVo2 : finance) {
    				if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail()!=null) {
    					emails.add(userVo2.getEmail());
    				}
    			}
    			SimpleDateFormat dateFormate=new SimpleDateFormat("yyyy年MM月dd日");
    			Calendar calendar=Calendar.getInstance(); 
    			List<SupplierOrder> textList = supplierOrderDao.getEmailTextList(supplierOrder.getId());
    			buffer.append("<table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">");
    			buffer.append("<tr>");
    			buffer.append("<th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">件号</th>")
    				  .append("<th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">到期日期</th>");
    			buffer.append("</tr>");
    			for (SupplierOrder supplierOrder2 : textList) {
    				calendar.setTime(supplierOrder2.getDeadline()); 
    				calendar.add(Calendar.DAY_OF_MONTH, supplierOrder2.getShipLeadTime());
    				buffer.append("<tr>");
    				buffer.append("<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">");
    				buffer.append(supplierOrder2.getPartNumber());
    				buffer.append("</td>");
    				buffer.append("<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">");
    				buffer.append(dateFormate.format(calendar.getTime()));
    				buffer.append("</td>");
    				buffer.append("</tr>");
				}
    			buffer.append("</table>");
    			UserVo storage = userDao.getByRole("物流").get(0);
    			List<String> user = new ArrayList<String>();
    			user.add(storage.getEmail());
    			ExchangeMail exchangeMail = new ExchangeMail();
    			exchangeMail.setUsername(storage.getEmail());
    			exchangeMail.setPassword(storage.getEmailPassword());
    			exchangeMail.init();
    			String path = "";
    			List<String> bcc = new ArrayList<String>();
    			try {
    				if (userVo3.getEmailPassword() != null && !"".equals(userVo3.getEmailPassword()) && textList.size() > 0) {
    					exchangeMail.doSend("<div>订单"+supplierOrder.getOrderNumber()+"到期未到货,</div>", emails, user, bcc, buffer.toString(), path);
//        				supplierOrder.setAllPrepayNotImportStatus(1);
//        				supplierOrderDao.updateByPrimaryKeySelective(supplierOrder);
					}
    				
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				if (userVo3.getEmailPassword() != null || !"".equals(userVo3.getEmailPassword())) {
    					supplierOrder.setAllPrepayNotImportStatus(0);
        				supplierOrderDao.updateByPrimaryKeySelective(supplierOrder);
					}
    				
    			}
    		}
		}
	}
    
    public boolean importCondition(ImportPackage importPackage,Integer userId) {
    	
    	StringBuffer ywid = new StringBuffer();
    	ywid.append(importPackage.getId()).append(",").append(userId);
    	List<String> emails = new ArrayList<String>();
		List<ImportPackagePaymentElementPrepare> list = importPackagePaymentElementPrepareDao.getImportElementByImportId(importPackage.getId());
		Supplier supplier = supplierDao.selectByPrimaryKey(importPackage.getSupplierId());
		/*StringBuffer businessKey = new StringBuffer();
		businessKey.append("import_condition.id.").append(ywid).append(".ImportConditionExcel");
		try {
			gyExcelService.generateExcel(businessKey.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}*/
		StringBuffer buffer = new StringBuffer();
		buffer.append("请看系统  入库单号:").append(importPackage.getImportNumber());
		/*buffer.append("供应商").append(supplier.getCode()).append(",入库单").append(importPackage.getImportNumber()).append("的到货情况：");
		buffer.append("<table>")
		      .append("<tr>")
			  .append("<th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">件号</th>")
			  .append("<th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">到货数量</th>")
			  .append("<th class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">异常数量</th>")
			  .append("</tr>");
		for (ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare : list){
			Double exception = importPackageElementDao.getTotalAmountByOrderELementId(importPackagePaymentElementPrepare.getSupplierOrderElementId());
			if (exception == null) {
				exception = new Double(0);
			}
			buffer.append("<tr>")
			      .append("<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">")
				  .append(importPackagePaymentElementPrepare.getPartNumber()).append("</td>")
			      .append("<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">")
				  .append(importPackagePaymentElementPrepare.getAmount()).append("</td>")
			      .append("<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">")
				  .append(exception).append("</td>")
				  .append("</tr>");
		}
		buffer.append("</table>");*/
		UserVo current = userDao.findUserByUserId(userId.toString());
		List<String> user = new ArrayList<String>();
		user.add(current.getEmail());
		
		ExchangeMail exchangeMail = new ExchangeMail();
		exchangeMail.setUsername(current.getEmail());
		exchangeMail.setPassword(current.getEmailPassword());
		exchangeMail.init();
		
		List<UserVo> userVo = userDao.selectBySupplierId(supplier.getId());
		for (UserVo userVo2 : userVo) {
			if (!emails.contains(userVo2.getEmail())&&!"".equals(userVo2.getEmail())) {
				emails.add(userVo2.getEmail());
			}
		}	
		List<UserVo> finance = userDao.getByRole("财务");
		for (UserVo userVo2 : finance) {
			if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail() != null&&!"".equals(userVo2.getEmail())) {
				emails.add(userVo2.getEmail());
			}
		}
	
	
		StringBuffer where = new StringBuffer();
		where.append("tr.role_name = '销售' and ip.ID = ").append(importPackage.getId());
		PageModel<UserVo> page = new PageModel<UserVo>();
		page.put("where", where);
		List<UserVo> market = userDao.getByImportIdAndRoleName(page);
		for (UserVo userVo2 : market) {
			if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail() != null&&!"".equals(userVo2.getEmail())) {
				emails.add(userVo2.getEmail());
			}
		}
		PageData data = new PageData();
		data.put("loginName", "Michelle");
		List<UserVo> manage = userDao.searchUserByLoginName(data);
		for (UserVo userVo2 : manage) {
			if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail() != null&&!"".equals(userVo2.getEmail())) {
				emails.add(userVo2.getEmail());
			}
		}
		data.put("loginName", "John");
		manage = userDao.searchUserByLoginName(data);
		for (UserVo userVo2 : manage) {
			if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail() != null&&!"".equals(userVo2.getEmail())) {
				emails.add(userVo2.getEmail());
			}
		}
		
		
		String path = "";//"D:/CRM/Files/mis/email/sampleoutput/"+importPackage.getImportNumber()+"入库单到货情况.xls";
		List<String> bcc = new ArrayList<String>();
		
		StringBuffer title = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(importPackage.getImportDate());
		if(importPackage.getImportStatus()<0){//退货时入库完成标题改为退回清单
			title.append(cal.get(Calendar.YEAR)).append("年")
			 .append(cal.get(Calendar.MONTH)+1).append("月")
			 .append(cal.get(Calendar.DAY_OF_MONTH)).append("日")
			 .append(supplier.getCode()).append("退货清单");
		}else{
			title.append(cal.get(Calendar.YEAR)).append("年")
			 .append(cal.get(Calendar.MONTH)+1).append("月")
			 .append(cal.get(Calendar.DAY_OF_MONTH)).append("日")
			 .append(supplier.getCode()).append("号供应商到货清单");
		}
		
		try {
			exchangeMail.doSend(title.toString(), emails, user, bcc, buffer.toString(), path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
