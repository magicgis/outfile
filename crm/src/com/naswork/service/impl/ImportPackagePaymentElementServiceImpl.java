package com.naswork.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.SupplierOrderElement;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("importPackagePaymentElementService")
public class ImportPackagePaymentElementServiceImpl implements
		ImportPackagePaymentElementService {

	@Resource
	private ImportPackagePaymentElementDao importPackagePaymentElementDao;
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource 
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private UserDao userDao;
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	
	@Override
	public int insertSelective(ImportPackagePaymentElement record) {
		return importPackagePaymentElementDao.insertSelective(record);
	}

	@Override
	public ImportPackagePaymentElement selectByPrimaryKey(Integer id) {
		return importPackagePaymentElementDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ImportPackagePaymentElement record) {
		return importPackagePaymentElementDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<ImportPackagePaymentElement> page){
		page.setEntities(importPackagePaymentElementDao.listPage(page));
	}
	
	public void addPaymentElemnt(List<ImportPackagePaymentElement> list,Integer importPackagePaymentId) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDataItem() != null) {
				if (list.get(i).getDataItem().equals("check")) {
					ImportPackagePayment importPackagePayment = importPackagePaymentDao.selectByPrimaryKey(importPackagePaymentId);
					SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(list.get(i).getSupplierOrderElementId());
					/*Double total = supplierOrderElement.getPrice()*supplierOrderElement.getAmount()*importPackagePayment.getPaymentPercentage()/100;
					BigDecimal b = new BigDecimal(total);  
					total = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					list.get(i).setPaymentSum(total);*/
					list.get(i).setImportPackagePaymentId(importPackagePaymentId);
					importPackagePaymentElementDao.insertSelective(list.get(i));
				}
			}
		}
	}
	
	public void edit(ImportPackagePaymentElement importPackagePaymentElement) {
		//ImportPackagePayment importPackagePayment = importPackagePaymentDao.selectByPrimaryKey(importPackagePaymentElement.getImportPackagePaymentId());
		//SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(importPackagePaymentElement.getSupplierOrderElementId());
		//Double amount = importPackagePaymentElement.getPaymentSum()/supplierOrderElement.getPrice()/importPackagePayment.getPaymentPercentage()*100;
		//BigDecimal b = new BigDecimal(amount);
		//amount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
		//importPackagePaymentElement.setAmount(amount);
		importPackagePaymentElementDao.updateByPrimaryKeySelective(importPackagePaymentElement);
		
	}

	@Override
	public void updateBySupplierOrderElementId(ImportPackagePaymentElement record) {
		importPackagePaymentElementDao.updateBySupplierOrderElementId(record);
	}
	
	public List<ImportPackagePaymentElement> elementList(Integer importPackagePaymentId){
		return importPackagePaymentElementDao.elementList(importPackagePaymentId);
	}

	public void addPaymentElemntTotal(List<ImportPackagePaymentElement> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDataItem().equals("check")) {
				//ImportPackagePayment importPackagePayment = importPackagePaymentDao.selectByPrimaryKey(list.get(i).getImportPackagePaymentId());
				ImportPackagePaymentElement importPackagePaymentElement = importPackagePaymentElementDao.selectByPrimaryKey(list.get(i).getId());
				//SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(list.get(i).getSupplierOrderElementId());
				//Double total = supplierOrderElement.getPrice()*list.get(i).getAmount()*importPackagePaymentElement.getPaymentPercentage()/100;
				if (importPackagePaymentElement.getPaymentSum() == null || "".equals(importPackagePaymentElement.getPaymentSum())) {
					importPackagePaymentElement.setPaymentSum(new Double(0));
				}
				BigDecimal a = new BigDecimal(importPackagePaymentElement.getPaymentSum());
				BigDecimal b = new BigDecimal(list.get(i).getPaymentSum());
				list.get(i).setPaymentSum(a.add(b).doubleValue());
				//total = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//list.get(i).setPaymentSum(list.get(i).getPaymentSum());
				importPackagePaymentElementDao.updateByPrimaryKeySelective(list.get(i));
				SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(importPackagePaymentElement.getSupplierOrderElementId());
				ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement.getClientOrderElementId());
				ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
				ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
				Double shouldPay = importPackagePaymentElement.getAmount() * importPackagePaymentElement.getPaymentPercentage() * supplierOrderElement.getPrice() / 100;
				/*if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
					if (clientOrderElement.getElementStatusId().equals(706) || clientOrderElement.getElementStatusId()==706 ||
							clientOrderElement.getElementStatusId().equals(704) || clientOrderElement.getElementStatusId()==704 || 
									clientOrderElement.getElementStatusId().equals(712) || clientOrderElement.getElementStatusId()==712){
						if (shouldPay.equals(list.get(i).getPaymentSum())) {
							clientOrderElement.setElementStatusId(705);
							clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
						}
					}
				}*/
				ImportPackagePayment importPackagePayment2 = importPackagePaymentDao.listById(list.get(i).getImportPackagePaymentId());
				if (importPackagePayment2.getShouldPay().equals(importPackagePayment2.getPaymentTotal())) {
					importPackagePayment2.setPaymentStatusId(231);
					importPackagePaymentDao.updateByPrimaryKeySelective(importPackagePayment2);
					paymentEmail(importPackagePaymentElement);
				}
			}
		}
	}
		
		public boolean paymentEmail(ImportPackagePaymentElement importPackagePaymentElement) {
	    	UserVo vo=ContextHolder.getCurrentUser();
	    	List<String> emails = new ArrayList<String>();
			StringBuffer buffer = new StringBuffer();
			ImportPackagePayment importPackagePayment=importPackagePaymentDao.selectByPrimaryKey(importPackagePaymentElement.getImportPackagePaymentId());
			buffer.append("付款单号:").append(importPackagePayment.getPaymentNumber()).append("付款完成");
			UserVo current = userDao.findUserByUserId(vo.getUserId());
			List<String> user = new ArrayList<String>();
			user.add(current.getEmail());
			
			ExchangeMail exchangeMail = new ExchangeMail();
			exchangeMail.setUsername(current.getEmail());
			exchangeMail.setPassword(current.getEmailPassword());
			exchangeMail.init();
			
			Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByBusinessKeyAndOutcome("IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+importPackagePaymentElement.getId(), "发起");
			if(null==jbpm4Jbyj){
				
				List<UserVo> userVo = userDao.selectBySupplierId(importPackagePayment.getSupplierId());
				for (UserVo userVo2 : userVo) {
				
					if (!emails.contains(userVo2.getEmail())&&!"".equals(userVo2.getEmail())) {
						emails.add(userVo2.getEmail());
					}
				}	
			}else{
				UserVo userVo = userDao.findUserByUserId(jbpm4Jbyj.getUserId());
				if (!emails.contains(userVo.getEmail())&&!"".equals(userVo.getEmail())) {
				emails.add(userVo.getEmail());
				}
			}
			
			List<String> bcc = new ArrayList<String>();
			
			StringBuffer title = new StringBuffer();
			
			try {
				exchangeMail.doSend("SYM付款完成通知", emails, user, bcc, buffer.toString(), "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}

	@Override
	public void tasklistPage(PageModel<ImportPackagePaymentElement> page) {
		page.setEntities(importPackagePaymentElementDao.tasklistPage(page));
	}
	
	public List<ImportPackagePaymentElement> selectBySupplierOrderElementId(Integer supplierOrderElementId){
		return importPackagePaymentElementDao.selectBySupplierOrderElementId(supplierOrderElementId);
	}

	@Override
	public ImportPackagePaymentElement elementData(Integer importPackagePaymentElementId) {
		return importPackagePaymentElementDao.elementData(importPackagePaymentElementId);
	}
	
}
