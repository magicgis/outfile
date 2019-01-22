package com.naswork.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientInvoiceDao;
import com.naswork.dao.ClientInvoiceElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ExportPackageDao;
import com.naswork.dao.ExportPackageElementDao;
import com.naswork.dao.ExportPackageInstructionsElementDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.IncomeDetailDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ExportPackage;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ExportPackageInstructionsElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.IncomeDetail;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.storage.controller.exportpackage.ExcelVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientInvoiceService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

import oracle.net.nt.SdpNTAdapter;

@Service("exportPackageElementService")
public class ExportPackageElementServiceImpl implements
		ExportPackageElementService {

	@Resource
	private ExportPackageElementDao exportPackageElementDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	@Resource
	private ExportPackageInstructionsElementDao exportPackageInstructionsElementDao;
	@Resource
	private ExportPackageDao exportPackageDao;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientInvoiceDao clientInvoiceDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ClientInvoiceElementDao clientInvoiceElementDao;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private IncomeDetailDao incomeDetailDao;
	@Resource
	private ClientQuoteDao clientQuoteDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private ClientDao clientDao;
	
	
	@Override
	public void insertSelective(ExportPackageElement record) {
		ExportPackage exportPackage=exportPackageDao.selectByPrimaryKey(record.getExportPackageId());
		if(null!=exportPackage.getExportPackageInstructionsId()){
			
			ExportPackageInstructionsElement exportPackageInstructionsElement = new ExportPackageInstructionsElement();
			exportPackageInstructionsElement.setImportPackageElementId(record.getImportPackageElementId());
			exportPackageInstructionsElement.setExportPackageInstructionsId(exportPackage.getExportPackageInstructionsId());
			
			ExportPackageInstructionsElement element=exportPackageInstructionsElementDao.selectByImportElementIdAndExID(exportPackageInstructionsElement);
			Double amount=	exportPackageElementDao.selectByIpeId(record.getImportPackageElementId());
			if(record.getAmount().equals(element.getAmount())){
				exportPackageInstructionsElement.setExportPackageStatus(1);
				exportPackageInstructionsElement.setAmount(amount+record.getAmount());
			}else{
				exportPackageInstructionsElement.setExportPackageStatus(0);
				exportPackageInstructionsElement.setAmount(element.getAmount()-record.getAmount());
			}
			exportPackageInstructionsElementDao.updateStatus(exportPackageInstructionsElement);
		}
		record.setStatus(0);
		record.setUpdateTimestamp(new Date());
		exportPackageElementDao.insertSelective(record);
		ClientInquiryElement clientInquiryElement2 = clientInquiryElementDao.selectByEpeId(record.getId());
		ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(clientInquiryElement2.getClientOrderElementId());
		IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientInquiryElement2.getClientOrderElementId());
		if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
			if (clientOrderElement.getElementStatusId().equals(708) || clientOrderElement.getElementStatusId()==708){
				if (incomeDetail != null) {
					if (incomeDetail.getTotal().equals(clientOrderElement.getPrice()*clientOrderElement.getAmount())) {
						clientOrderElement.setElementStatusId(710);
					}else {
						clientOrderElement.setElementStatusId(709);
					}
				}else {
					clientOrderElement.setElementStatusId(709);
				}
				clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement);
			}
		}
		ImportPackageElement importPackageElement = importPackageElementDao.selectByPrimaryKey(record.getImportPackageElementId());
	}
	
	public void insertElements(List<ClientInvoiceExcelVo> list,Integer exportPackageId){
		for (int i = 0; i < list.size(); i++) {
			ExportPackageElement exportPackageElement = new ExportPackageElement();
			ImportPackageElement importPackageElement = importPackageElementDao.selectByPrimaryKey(list.get(i).getId());
			exportPackageElement.setImportPackageElementId(importPackageElement.getId());
			exportPackageElement.setAmount(importPackageElement.getAmount());
			exportPackageElement.setExportPackageId(exportPackageId);
			exportPackageElement.setStatus(0);
			exportPackageElement.setUpdateTimestamp(new Date());
			exportPackageElementDao.insertSelective(exportPackageElement);
			ClientInquiryElement clientInquiryElement2 = clientInquiryElementDao.selectByEpeId(exportPackageElement.getId());
			ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(clientInquiryElement2.getClientOrderElementId());
			IncomeDetail incomeDetail = incomeDetailDao.getTotalByClientOrderElementId(clientInquiryElement2.getClientOrderElementId());
			if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
				if (clientOrderElement.getElementStatusId().equals(708) || clientOrderElement.getElementStatusId()==708){
					if (incomeDetail.getTotal().equals(clientOrderElement.getPrice()*clientOrderElement.getAmount())) {
						clientOrderElement.setElementStatusId(710);
					}else {
						clientOrderElement.setElementStatusId(709);
					}
					clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement);
				}
			}
			checkExportPackage(exportPackageElement);
		}
	}

	@Override
	public ExportPackageElement selectByPrimaryKey(Integer id) {
		return exportPackageElementDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ExportPackageElement record) {
		return exportPackageElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(PageModel<ExportPackageElementVo> page, String where,
			GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exportPackageElementDao.listPage(page));
	}
	
	public List<ExportPackageElementVo> addElementPage(PageModel<ExportPackageElementVo> page,String where,GridSort sort){
		StringBuffer condition =new StringBuffer();
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
		page.setEntities(exportPackageElementDao.addElementPage(page));
		return exportPackageElementDao.addElementPage(page);
		
	}
	
	public List<ExportPackageElementVo> getImportElement(PageModel<ExportPackageElementVo> page){
		return exportPackageElementDao.getImportElement(page);
	}
	
	public List<ExportPackageElementVo> creatExcel(Integer id) {
		return exportPackageElementDao.Elements(id);
	}
	
	public List<ImportPackageElement> getElementByLocation(String location){
		return exportPackageElementDao.getElementByLocation(location);
	}
	
	public List<ExportPackageElementVo> getCountWithLocation(Integer exportPackageId){
		return exportPackageElementDao.getCountWithLocation(exportPackageId);
	}
	
	public void lackOfPartNumber(PageModel<ImportPackageElement> page,Integer exportPackageId) {
		/*List<ExportPackageElementVo> locationList = exportPackageElementDao.getCountWithLocation(exportPackageId);
		List<ImportPackageElement> list = new ArrayList<ImportPackageElement>();
		if (locationList.size()!=0) {
			for (ExportPackageElementVo exportPackageElementVo : locationList) {
				List<ImportPackageElement> importList = exportPackageElementDao.getElementByLocation(exportPackageElementVo.getLocation());
				if (importList.size()!=0) {
					for (ImportPackageElement importPackageElement : importList) {
						if (importPackageElement.getLocation()!="地面") {
							list.add(importPackageElement);
						}
					}
				}
			}
		}
		page.setEntities(list);*/
		page.put("exportPackageId", exportPackageId);
		List<ImportPackageElement> list = exportPackageElementDao.getLackPage(page);
		page.setEntities(exportPackageElementDao.getLackPage(page));
		
	}
	
	public List<ImportPackageElement> getLackList(PageModel<ImportPackageElement> page) {
		return exportPackageElementDao.getLackPage(page);
	}

	@Override
	public void findClientOrderNumber(PageModel<ExportPackageElementVo> page) {
		page.setEntities(exportPackageElementDao.findClientOrderNumber(page));
	}

	@Override
	public List<ExportPackageElementVo> findByEpidAndCoid(String exportPackageId, String clientOrderId) {
		return exportPackageElementDao.findByEpidAndCoid(exportPackageId, clientOrderId);
	}

	@Override
	public ExportPackageElementVo findEpeAmount(Integer exportPackageId, Integer clientOrderId) {
		return exportPackageElementDao.findEpeAmount(exportPackageId, clientOrderId);
	}

	@Override
	public void exportPackageInstructionsPage(PageModel<ExportPackageElementVo> page,
			String where, GridSort sort) {
		if(null!=where&&!"".equals(where)){
		page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exportPackageElementDao.exportPackageInstructionsPage(page));
	}
	
	public ExportPackageElement getImportPackageElementId(PageModel<ExportPackageElement> page){
		return exportPackageElementDao.getImportPackageElementId(page);
	}
	
    public int getCLientOrderElementCount(Integer clientOrderId){
    	return exportPackageElementDao.getCLientOrderElementCount(clientOrderId);
    }
    
    public int getExportPackageElementCount(Integer clientOrderId){
    	return exportPackageElementDao.getExportPackageElementCount(clientOrderId);
    }
    
    public void checkExportPackage(ExportPackageElement exportPackageElement) {
		ClientOrderElement clientOrderElement = clientOrderElementService.findByExportPackageElementId(exportPackageElement.getId());
		ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
		 List<ClientOrderElementVo> clientOrderElementVos=clientOrderElementDao.getCount(clientOrderElement.getClientOrderId());
		int exportPackageCount = getExportPackageElementCount(clientOrderElement.getClientOrderId());
		int clientOrderCount = getCLientOrderElementCount(clientOrderElement.getClientOrderId());
		
		Double prepayrate=clientOrder.getPrepayRate();
		Double receivepayrate=clientOrder.getReceivePayRate();
		Double shipPayRate=clientOrder.getShipPayRate();
		ClientQuote clientQuote = clientQuoteDao.selectByPrimaryKey(clientOrder.getClientQuoteId());
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientQuote.getClientInquiryId());
		Client client = clientDao.selectByPrimaryKey(clientInquiry.getClientId());
		
		if(!clientOrder.getOrderStatusId().equals(63)){
			if(prepayrate<1&&receivepayrate<1&&shipPayRate>0 && !client.getCode().startsWith("9") && !client.getCode().startsWith("8")){
				List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderId(clientOrderElement.getClientOrderId(),2);
				if(null!=clientInvoices&&clientInvoices.size()>0){
					ExportPackage exportPackage=exportPackageDao.selectByPrimaryKey(exportPackageElement.getExportPackageId());
					ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					boolean in=false;
					String lastNumber="";
					for (ClientInvoice clientInvoice : clientInvoices) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
						String invoiceDate=dateFormat.format(clientInvoice.getInvoiceDate());
						String nowDate=dateFormat.format(new Date());
						if(nowDate.equals(invoiceDate)){
							 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
							 clientInvoiceElement.setTerms(clientInvoice.getTerms());
							 clientInvoiceElement.setClientOrderElementId(clientOrderElement.getId());
							 Double exAmount=exportPackageElement.getAmount();
							 Double coAmount=clientOrderElement.getAmount();
							 if(coAmount.equals(exAmount)){
								 clientInvoiceElement.setAmount(coAmount);
								 clientInvoiceElementDao.insert(clientInvoiceElement);
							 }else{
								 Double exportAmount=0.0;
								 List<ClientOrderElementVo> elementVos=clientOrderElementDao.findByClientOrderElementId(clientOrderElement.getId());
								 for (ClientOrderElementVo clientOrderElementVo : elementVos) {
									 exportAmount+=clientOrderElementVo.getExportAmount();
								 }
								 ClientInvoiceElement element=clientInvoiceElementDao.selectByCoeIdAndCiId(clientInvoiceElement);
								 clientInvoiceElement.setAmount(exportAmount);
								 if(null==element){
									 clientInvoiceElementDao.insertSelective(clientInvoiceElement);
								 }else{
									 clientInvoiceElement.setId(element.getId());
									 clientInvoiceElementDao.updateByPrimaryKeySelective(clientInvoiceElement);
								 }
							 }
							 
							 in=true;
							 break;
						}
						lastNumber =lastNumber+clientInvoice.getInvoiceNumber();
					}
					if(!in){
							ClientInvoice invoice=new ClientInvoice();
							String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
									"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
							for (int i = 0; i < ziMu.length; i++) {
								int a=lastNumber.indexOf(ziMu[i]);
								if(lastNumber.indexOf(ziMu[i])>-1){
									continue;
								}else{
									
									invoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[i]);
									invoice.setClientOrderId(clientOrderElement.getClientOrderId());
									invoice.setInvoiceDate(new Date());
									Double terms=clientOrder.getShipPayRate()*100;
									if(clientOrderElementVos.size()==1){
										Double prepayRate=clientOrder.getPrepayRate()*100;
										 terms=100-prepayRate;
									}else if(terms==0){
										terms=100.0-(prepayrate*100)-(receivepayrate*100);
									}
									invoice.setTerms(terms.intValue());
									invoice.setInvoiceType(2);
									invoice.setInvoiceStatusId(0);
									clientInvoiceDao.insert(invoice);
									ClientInvoiceElement invoiceElement=new ClientInvoiceElement();
									invoiceElement.setTerms(terms.intValue());
									invoiceElement.setClientInvoiceId(invoice.getId());
									invoiceElement.setClientOrderElementId(clientOrderElement.getId());
									Double exAmount=exportPackageElement.getAmount();
									invoiceElement.setAmount(exAmount);
									clientInvoiceElementDao.insert(invoiceElement);
									break;
							}
						}
					}
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
							clientInvoice.setClientOrderId(clientOrderElement.getClientOrderId());
							clientInvoice.setInvoiceDate(new Date());
							Double terms=clientOrder.getShipPayRate()*100;
							
		
							if(clientOrderElementVos.size()==1){
								Double prepayRate=clientOrder.getPrepayRate()*100;
								 terms=100-prepayRate;
							}else if(terms==0){
								terms=100.0-(prepayrate*100)-(receivepayrate*100);
							}
							clientInvoice.setTerms(terms.intValue());
							clientInvoice.setInvoiceType(2);
							clientInvoice.setInvoiceStatusId(0);
							clientInvoiceDao.insert(clientInvoice);
							ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
							clientInvoiceElement.setTerms(terms.intValue());
							clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
							clientInvoiceElement.setClientOrderElementId(clientOrderElement.getId());
							Double exAmount=exportPackageElement.getAmount();
							clientInvoiceElement.setAmount(exAmount);
							clientInvoiceElementDao.insert(clientInvoiceElement);
							break;
						}
					}
				}
			
	    }
			
		if (exportPackageCount==clientOrderCount || new Integer(exportPackageCount).equals(new Integer(clientOrderCount))) {
			ClientInvoice clientInvoice = new ClientInvoice();
			String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
					"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
			int invoiceCount = clientInvoiceDao.getInvoiceCount(clientOrderElement.getClientOrderId());
			StringBuffer invoiceNumber = new StringBuffer();
			invoiceNumber.append(clientOrder.getOrderNumber().substring(4)).append(ziMu[invoiceCount]);
			clientInvoice.setInvoiceNumber(invoiceNumber.toString());
			clientInvoice.setClientOrderId(clientOrder.getId());
			clientInvoice.setInvoiceDate(new Date());
			/*Double finishedTerm = clientInvoiceDao.getFinishedTerm(clientOrder.getId());
			Double terms=0.0;
			if(null==finishedTerm){
				finishedTerm=0.0;
			}
			terms = 100 - finishedTerm;*/
			
			Double terms = clientOrder.getReceivePayRate()*100;
			if (terms>0) { 
				clientInvoice.setTerms(terms.intValue());
				clientInvoice.setInvoiceType(3);
				clientInvoice.setInvoiceStatusId(0);
				clientInvoiceDao.insertSelective(clientInvoice);
				List<ClientOrderElementVo> list = clientOrderElementService.findByOrderId(clientOrder.getId());
				for (int i = 0; i < list.size(); i++) {
					ClientInvoiceElement clientInvoiceElement = new ClientInvoiceElement();
					clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
					clientInvoiceElement.setClientOrderElementId(list.get(i).getClientOrderElementId());
					clientInvoiceElement.setAmount(list.get(i).getClientOrderAmount());
					clientInvoiceElement.setTerms(clientInvoice.getTerms());
					clientInvoiceElementDao.insertSelective(clientInvoiceElement);
				}
			}
			
		}
	
		}
	}
    
    public List<ExportPackageElementVo> selectByInstructionsId(Integer instructionsId){
    	return selectByInstructionsId(instructionsId);
    }
    
    public int deleteByPrimaryKey(Integer id){
    	return exportPackageElementDao.deleteByPrimaryKey(id);
    }
    
    public List<ImportPackageElement> unEqual(List<ExportPackageElementVo> list,List<ImportPackageElement> importList) {
    	//List<ImportPackageElement> unExist = new ArrayList<ImportPackageElement>();
		for (ExportPackageElementVo exportPackageElementVo : list) {
			for (int i = 0; i < importList.size(); i++) {
				if (exportPackageElementVo.getId().equals(importList.get(i).getId())) {
					importList.remove(i);
					break;
				}
			}
		}
		return importList;
	}

	@Override
	public Double BoxWeight(ImportPackageElement parame) {
		return exportPackageElementDao.BoxWeight(parame);
	}

	@Override
	public List<ExportPackageElementVo> findBoxWeight(Integer id) {
		return exportPackageElementDao.findBoxWeight(id);
	}
	
	public void checkExportPackageForCom(ExportPackageElement exportPackageElement,Integer shipId) {
		ClientOrderElement clientOrderElement = clientOrderElementService.findByExportPackageElementId(exportPackageElement.getId());
		ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(clientOrderElement.getClientOrderId());
		 List<ClientOrderElementVo> clientOrderElementVos=clientOrderElementDao.getCount(clientOrderElement.getClientOrderId());
		int exportPackageCount = getExportPackageElementCount(clientOrderElement.getClientOrderId());
		int clientOrderCount = getCLientOrderElementCount(clientOrderElement.getClientOrderId());
		
		Double prepayrate=clientOrder.getPrepayRate();
		Double receivepayrate=clientOrder.getReceivePayRate();
		Double shipPayRate=clientOrder.getShipPayRate();
		ClientQuote clientQuote = clientQuoteDao.selectByPrimaryKey(clientOrder.getClientQuoteId());
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientQuote.getClientInquiryId());
		Client client = clientDao.selectByPrimaryKey(clientInquiry.getClientId());
		
		if(!clientOrder.getOrderStatusId().equals(63)){
			if(prepayrate<1&&receivepayrate<1&&shipPayRate>0){
				List<ClientInvoice> clientInvoices=clientInvoiceDao.selectByclientOrderIdAndShipId(clientOrderElement.getClientOrderId(),2,shipId);
				
				if(null!=clientInvoices&&clientInvoices.size()>0){
					ExportPackage exportPackage=exportPackageDao.selectByPrimaryKey(exportPackageElement.getExportPackageId());
					ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					boolean in=false;
					String lastNumber="";
					for (ClientInvoice clientInvoice : clientInvoices) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
						String invoiceDate=dateFormat.format(clientInvoice.getInvoiceDate());
						String nowDate=dateFormat.format(new Date());
						if(nowDate.equals(invoiceDate)){
							 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
							 clientInvoiceElement.setTerms(clientInvoice.getTerms());
							 clientInvoiceElement.setClientOrderElementId(clientOrderElement.getId());
							 Double exAmount=exportPackageElement.getAmount();
							 Double coAmount=clientOrderElement.getAmount();
							 if(coAmount.equals(exAmount)){
								 clientInvoiceElement.setAmount(coAmount);
								 clientInvoiceElementDao.insert(clientInvoiceElement);
							 }else{
								 Double exportAmount=0.0;
								 List<ClientOrderElementVo> elementVos=clientOrderElementDao.findByClientOrderElementId(clientOrderElement.getId());
								 for (ClientOrderElementVo clientOrderElementVo : elementVos) {
									 exportAmount+=clientOrderElementVo.getExportAmount();
								 }
								 ClientInvoiceElement element=clientInvoiceElementDao.selectByCoeIdAndCiId(clientInvoiceElement);
								 clientInvoiceElement.setAmount(exportAmount);
								 if(null==element){
									 clientInvoiceElementDao.insertSelective(clientInvoiceElement);
								 }else{
									 clientInvoiceElement.setId(element.getId());
									 clientInvoiceElementDao.updateByPrimaryKeySelective(clientInvoiceElement);
								 }
							 }
							 
							 in=true;
							 break;
						}
						lastNumber =lastNumber+clientInvoice.getInvoiceNumber();
					}
					if(!in){
							ClientInvoice invoice=new ClientInvoice();
							String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
									"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
							for (int i = 0; i < ziMu.length; i++) {
								int a=lastNumber.indexOf(ziMu[i]);
								if(lastNumber.indexOf(ziMu[i])>-1){
									continue;
								}else{
									
									invoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[i]);
									invoice.setClientOrderId(clientOrderElement.getClientOrderId());
									invoice.setInvoiceDate(new Date());
									Double terms=clientOrder.getShipPayRate()*100;
									if(clientOrderElementVos.size()==1){
										Double prepayRate=clientOrder.getPrepayRate()*100;
										 terms=100-prepayRate;
									}else if(terms==0){
										terms=100.0-(prepayrate*100)-(receivepayrate*100);
									}
									invoice.setTerms(terms.intValue());
									invoice.setInvoiceType(2);
									invoice.setInvoiceStatusId(0);
									clientInvoiceDao.insert(invoice);
									ClientInvoiceElement invoiceElement=new ClientInvoiceElement();
									invoiceElement.setTerms(terms.intValue());
									invoiceElement.setClientInvoiceId(invoice.getId());
									invoiceElement.setClientOrderElementId(clientOrderElement.getId());
									Double exAmount=exportPackageElement.getAmount();
									invoiceElement.setAmount(exAmount);
									clientInvoiceElementDao.insert(invoiceElement);
									break;
							}
						}
					}
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
							clientInvoice.setClientOrderId(clientOrderElement.getClientOrderId());
							clientInvoice.setInvoiceDate(new Date());
							Double terms=clientOrder.getShipPayRate()*100;
							
		
							if(clientOrderElementVos.size()==1){
								Double prepayRate=clientOrder.getPrepayRate()*100;
								 terms=100-prepayRate;
							}else if(terms==0){
								terms=100.0-(prepayrate*100)-(receivepayrate*100);
							}
							clientInvoice.setTerms(terms.intValue());
							clientInvoice.setInvoiceType(2);
							clientInvoice.setInvoiceStatusId(0);
							clientInvoice.setClientShipId(shipId);
							clientInvoiceDao.insert(clientInvoice);
							ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
							clientInvoiceElement.setTerms(terms.intValue());
							clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
							clientInvoiceElement.setClientOrderElementId(clientOrderElement.getId());
							Double exAmount=exportPackageElement.getAmount();
							clientInvoiceElement.setAmount(exAmount);
							clientInvoiceElementDao.insert(clientInvoiceElement);
							break;
						}
					}
				}
			
	    }
			
		if (exportPackageCount==clientOrderCount || new Integer(exportPackageCount).equals(new Integer(clientOrderCount))) {
			ClientInvoice clientInvoice = new ClientInvoice();
			String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
					"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
			int invoiceCount = clientInvoiceDao.getInvoiceCount(clientOrderElement.getClientOrderId());
			StringBuffer invoiceNumber = new StringBuffer();
			invoiceNumber.append(clientOrder.getOrderNumber().substring(4)).append(ziMu[invoiceCount]);
			clientInvoice.setInvoiceNumber(invoiceNumber.toString());
			clientInvoice.setClientOrderId(clientOrder.getId());
			clientInvoice.setInvoiceDate(new Date());
			/*Double finishedTerm = clientInvoiceDao.getFinishedTerm(clientOrder.getId());
			Double terms=0.0;
			if(null==finishedTerm){
				finishedTerm=0.0;
			}
			terms = 100 - finishedTerm;*/
			
			Double terms = clientOrder.getReceivePayRate()*100;
			if (terms>0) { 
				clientInvoice.setTerms(terms.intValue());
				clientInvoice.setInvoiceType(3);
				clientInvoice.setInvoiceStatusId(0);
				clientInvoiceDao.insertSelective(clientInvoice);
				List<ClientOrderElementVo> list = clientOrderElementService.findByOrderId(clientOrder.getId());
				for (int i = 0; i < list.size(); i++) {
					ClientInvoiceElement clientInvoiceElement = new ClientInvoiceElement();
					clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
					clientInvoiceElement.setClientOrderElementId(list.get(i).getClientOrderElementId());
					clientInvoiceElement.setAmount(list.get(i).getClientOrderAmount());
					clientInvoiceElement.setTerms(clientInvoice.getTerms());
					clientInvoiceElementDao.insertSelective(clientInvoiceElement);
				}
			}
			
		}
	
		}
	}
	
	public List<ExportPackageElement> getByExportElementByOrderId(Integer clientOrderElementId,Integer importPackageId){
		return exportPackageElementDao.getByExportElementByOrderId(clientOrderElementId, importPackageId);
	}
}
