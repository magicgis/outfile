package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.SeqKeyDao;
import com.naswork.dao.SupplierAptitudeDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierOrderElementFjDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.Element;
import com.naswork.model.ExchangeRate;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.SeqKey;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAptitude;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierOrderElementFj;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.StorageOrderVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("supplierOrderElementService")
public class SupplierOrderElementServiceImpl implements
		SupplierOrderElementService {
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private SupplierOrderDao supplierOrderDao;
	@Resource
	private ElementDao elementDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private SystemCodeDao  systemCodeDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private SeqKeyDao seqKeyDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private ImportPackageElementDao importpackageElementDao;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SupplierOrderElementFjDao supplierOrderElementFjDao;
	@Resource
	private SupplierAptitudeDao supplierAptitudeDao;
	
	@Override
	public void insertSelective(SupplierOrderElement record) {
		int count = supplierOrderElementDao.getOrderCount(record.getSupplierOrderId());
		if (count == 0) {
			record.setItem(1);
		}else {
			record.setItem(count+1);
		}
		/*SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(record.getSupplierOrderId());
		if (supplierOrder.getOrderType().equals(1)) {
			record.setElementStatusId(710);
		}else {
			record.setElementStatusId(705);
		}*/
		supplierOrderElementDao.insertSelective(record);
		ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(record.getClientOrderElementId());
		ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
//		if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
//			if (clientOrderElement.getElementStatusId().equals(704) || clientOrderElement.getElementStatusId()==704|| 
//					clientOrderElement.getElementStatusId().equals(712) || clientOrderElement.getElementStatusId()==712){
//				if (supplierOrder.getPrepayRate() > 0) {
//					clientOrderElement.setElementStatusId(706);
//				}else {
//					clientOrderElement.setElementStatusId(705);
//				}
//				clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
//			}
//		}
	}

	@Override
	public void updateByPrimaryKeySelective(SupplierOrderElement record) {
		SupplierOrderElement supplierOrderElement = supplierOrderElementDao.selectByPrimaryKey(record.getId());
		ClientOrderElement clientOrderElement = clientOrderElementDao.selectByPrimaryKey(supplierOrderElement.getClientOrderElementId());
		ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
		if (record.getAwb()!=null && !"".equals(record.getAwb())) {
			if (!"1".equals(record.getImportStatus())) {
				record.setImportStatus(0);
			}
		}
		supplierOrderElementDao.updateByPrimaryKeySelective(record);
		/*if (record.getAwb()!=null && !"".equals(record.getAwb())) {
			if (!"".equals(clientOrderElement.getElementStatusId()) && clientOrderElement.getElementStatusId()!=null) {
				if (clientOrderElement.getElementStatusId().equals(705) || clientOrderElement.getElementStatusId()==705){
					clientOrderElement.setElementStatusId(707);
					clientOrderElementDao.updateByPrimaryKeySelective(clientOrderElement);
				}
			}
		}*/
	}
	
	/*
     * 根据elementId查询
     */
    public AddSupplierOrderElementVo findByElementId(Integer id){
    	return supplierOrderElementDao.findByElementId(id);
    }
    
    public Double getStorageAmount(String partNumber){
    	return supplierOrderElementDao.getStorageAmount(partNumber);
    }
    
    public void getImportPreparePage(PageModel<SupplierOrderElement> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(supplierOrderElementDao.getImportPreparePage(page));
    	
    }
    
    public void listByPartNumberPage(PageModel<SupplierQuoteElement> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierQuoteElementDao.listByPartNumberPage(page));
    }
    
    public void repairListByPartNumberPage(PageModel<SupplierQuoteElement> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierQuoteElementDao.repairListByPartNumberPage(page));
    }
    
    /**
     * 新增客户询价明细
     */
    public ClientInquiryElement addClientInquiryElement(SupplierQuoteElement supplierQuoteElement,StorageOrderVo storageOrderVo) {
    	List<ClientInquiryElement> list = clientInquiryElementDao.findByclientInquiryId(storageOrderVo.getClientInquiryId());
		int item;
		if (list.size()==0) {
			item = 1;
		}else {
			item = list.size()+1;
		}
    	ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
    	clientInquiryElement.setClientInquiryId(storageOrderVo.getClientInquiryId());
    	List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
        Element element2 = new Element();
        if (element.size()==0) {
        	byte[] p = clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()).getBytes();
        	Byte[] pBytes = new Byte[p.length];
        	for(int j=0;j<p.length;j++){
        		pBytes[j] = Byte.valueOf(p[j]);
        	}
        	element2.setPartNumberCode(pBytes);
        	element2.setUpdateTimestamp(new Date());
			elementDao.insert(element2);
			clientInquiryElement.setElementId(element2.getId());
		}else {
			Element element3 = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber())).get(0);
			clientInquiryElement.setElementId(element3.getId());
		}
		clientInquiryElement.setPartNumber(supplierQuoteElement.getPartNumber());
		clientInquiryElement.setItem(item);
		clientInquiryElement.setCsn(item);
		clientInquiryElement.setUnit(supplierQuoteElement.getUnit());
		clientInquiryElement.setAmount(supplierQuoteElement.getAmount());
		clientInquiryElement.setUpdateTimestamp(new Date());
		clientInquiryElement.setIsBlacklist(0);
		clientInquiryElement.setDescription(supplierQuoteElement.getDescription());
		clientInquiryElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
		clientInquiryElement.setSource("采购");
    	clientInquiryElementDao.insertSelective(clientInquiryElement);
    	return clientInquiryElement;
	}
    
    /**
     * 新增供应商询价明细
     */
    public SupplierInquiryElement addSupplierInquiryElement(ClientInquiryElement clientInquiryElement,StorageOrderVo storageOrderVo) {
		SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
		supplierInquiryElement.setSupplierInquiryId(storageOrderVo.getSupplierInquiryId());
		supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
		supplierInquiryElement.setUpdateTimestamp(new Date());
		supplierInquiryElementDao.insertSelective(supplierInquiryElement);
		return supplierInquiryElement;
	}
    
    /**
     * 新增供应商报价明细
     */
    public SupplierQuoteElement addSupplierQuoteElement(SupplierQuoteElement supplierQuoteElement,SupplierInquiryElement supplierInquiryElement,StorageOrderVo storageOrderVo) {
    	List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
        Element element2 = new Element();
        if (element.size()==0) {
        	byte[] p = clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()).getBytes();
        	Byte[] pBytes = new Byte[p.length];
        	for(int j=0;j<p.length;j++){
        		pBytes[j] = Byte.valueOf(p[j]);
        	}
        	element2.setPartNumberCode(pBytes);
        	element2.setUpdateTimestamp(new Date());
			elementDao.insert(element2);
			supplierQuoteElement.setElementId(element2.getId());
		}else {
			Element element3 = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber())).get(0);
			supplierQuoteElement.setElementId(element3.getId());
		}
    	supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
    	supplierQuoteElement.setSupplierQuoteId(storageOrderVo.getSupplierQuoteId());
    	supplierQuoteElement.setSupplierQuoteStatusId(70);
    	supplierQuoteElement.setUpdateTimestamp(new Date());
    	supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
    	supplierQuoteElementDao.insertSelective(supplierQuoteElement);
    	clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
    	ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
    	clientInquiryElement.setElementStatusId(702);
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
		return supplierQuoteElement;
	}
    
    /**
     * 新增客户报价明细
     */
    public ClientQuoteElement addClientQuoteElement(SupplierQuoteElement supplierQuoteElement,StorageOrderVo storageOrderVo,ClientInquiryElement clientInquiryElement) {
		ClientQuoteElement clientQuoteElement = new ClientQuoteElement();
		clientQuoteElement.setClientQuoteId(storageOrderVo.getClientQuoteId());
		clientQuoteElement.setClientInquiryElementId(clientInquiryElement.getId());
		clientQuoteElement.setSupplierQuoteElementId(supplierQuoteElement.getId());
		clientQuoteElement.setPrice(supplierQuoteElement.getPrice());
		clientQuoteElement.setUpdateTimestamp(new Date());
		clientQuoteElement.setLeadTime(new Integer(supplierQuoteElement.getLeadTime()));
		clientQuoteElement.setAmount(supplierQuoteElement.getAmount());
		clientQuoteElementDao.insertSelective(clientQuoteElement);
		return clientQuoteElement;
	}
    
    /**
     * 新增客户订单明细
     */
    public ClientOrderElement addClientOrderElement(ClientQuoteElement clientQuoteElement,StorageOrderVo storageOrderVo) {
		ClientOrderElement clientOrderElement = new ClientOrderElement();
		clientOrderElement.setClientOrderId(storageOrderVo.getClientOrderId());
		clientOrderElement.setClientQuoteElementId(clientQuoteElement.getId());
		clientOrderElement.setLeadTime(clientQuoteElement.getLeadTime().toString());
		clientOrderElement.setAmount(clientQuoteElement.getAmount());
		clientOrderElement.setPrice(clientQuoteElement.getPrice());
		clientOrderElement.setUpdateTimestamp(new Date());
		clientOrderElementDao.insertSelective(clientOrderElement);
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
			if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703 || 
					clientInquiryElement.getElementStatusId().equals(711) || clientInquiryElement.getElementStatusId()==711){
				clientInquiryElement.setElementStatusId(710);
				clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
			}
		}
		return clientOrderElement;
	}
    
    public void StorageOrderElement(SupplierQuoteElement supplierQuoteElement,Integer clientOrderId) {
    	StorageOrderVo storageOrderVo = supplierOrderDao.getIds(clientOrderId);
		ClientInquiryElement clientInquiryElement = addClientInquiryElement(supplierQuoteElement, storageOrderVo);
		SupplierInquiryElement supplierInquiryElement = addSupplierInquiryElement(clientInquiryElement, storageOrderVo);
		SupplierQuoteElement supplierQuoteElement2 = addSupplierQuoteElement(supplierQuoteElement, supplierInquiryElement, storageOrderVo);
		ClientQuoteElement clientQuoteElement = addClientQuoteElement(supplierQuoteElement2, storageOrderVo, clientInquiryElement);
		ClientOrderElement clientOrderElement = addClientOrderElement(clientQuoteElement, storageOrderVo);
	}

	@Override
	public SupplierOrderElement selectBySqeIdAndSoId(Integer supplierOrderId, Integer supplierQuoteElementId) {
		return supplierOrderElementDao.selectBySqeIdAndSoId(supplierOrderId, supplierQuoteElementId);
	}
	
	public SupplierOrderElement selectByPrimaryKey(Integer id){
		return supplierOrderElementDao.selectByPrimaryKey(id);
	}
	
	public SupplierOrderElement getPaymentMessage(Integer supplierOrderElementId){
		return supplierOrderElementDao.getPaymentMessage(supplierOrderElementId);
	}
	
	public Integer getSupplierId(Integer supplierOrderElementId){
		return supplierOrderElementDao.getSupplierId(supplierOrderElementId);
	}
	
	public List<SupplierOrderElement> selectBySupplierOrderId(Integer supplierOrderId){
		return supplierOrderElementDao.selectBySupplierOrderId(supplierOrderId);
	}

	@Override
	public MessageVo uploadExcel(MultipartFile multipartFile, String clientOrderId) {
		MessageVo messageVo=new MessageVo();
		boolean success = false;
		String message = "";
		InputStream fileStream = null;
		List<ClientQuoteElement> list=new ArrayList<ClientQuoteElement>();
		UserVo userVo=ContextHolder.getCurrentUser();
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		byte[] bytes;
		try {
			bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
		    //定义行
		    Row row;
		    int line=1;
			int start=0;
			int coloumNum=sheet.getRow(1).getPhysicalNumberOfCells();
			for (int j = 0; j < coloumNum; j++) {
				row = sheet.getRow(1);
				
	            String lowprice = row.getCell(j).toString();
	            if(lowprice.equals("确定供应商")){
	            	start=j;
	            	break;
	            }
			}
		    
			for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i+1);
				if (row != null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
					 Cell p1 = row.getCell(start+1);
					 String p2=p1+"";
					 if(null==p1||p2.equals("")){
						 line++;
						 continue;
					 }else{
					
					String orderNumber=row.getCell(0).toString();
					
					Double a=new Double(row.getCell(1).toString());
					 Integer item = a.intValue();
					 String partNumber = ""; 
			            if (row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_STRING) {
							partNumber = row.getCell(2).toString();
						}else if (row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    partNumber = dataFormatter.formatCellValue(row.getCell(2));
						}
//		            String supplierCode = row.getCell(start+6).toString();
//		            Double b = new Double(row.getCell(start+7).toString());
//		            Integer supplierquoteNumber = b.intValue();
		            String supplierquoteNumber=row.getCell(start).toString();
		            double price=row.getCell(start+1).getNumericCellValue();
		            double amount=row.getCell(start+2).getNumericCellValue();
		            String leadTime="";
		            String ship="";
		            String destination="";
		            Integer shipId = null;
		            Integer destId = null;
			            if(null!= row.getCell(start+4)){
			            	 leadTime=row.getCell(start+4).toString();
			            	}
			            if(null!= row.getCell(start+5)){
			            	List<SystemCode> destinationlist = systemCodeDao.findType("STORE_LOCATION");
			            	destination=row.getCell(start+5).toString();
					            for (SystemCode systemCode : destinationlist) {
									if(destination.equals(systemCode.getValue())){
										destId=systemCode.getId();
									}
								}
			            	}
			            if(null!= row.getCell(start+6)){
			            	List<SystemCode> shiplist = systemCodeDao.findType("LOGISTICS_WAY");
			            	ship=row.getCell(start+6).toString();
			            		for (SystemCode systemCode : shiplist) {
								if(ship.equals(systemCode.getValue())){
									shipId=systemCode.getId();
								}
							}
			            	}
			            SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement(); 
			            SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
			            Integer clientOrderElementId=clientOrderElementDao.findByOrderNumberAndItem(orderNumber, item.toString());
			            if(supplierquoteNumber.indexOf("/")>-1){
			            	 String[] numberorid=supplierquoteNumber.split("/");
					            supplierOrderElement.setSupplierQuoteElementId(Integer.parseInt(numberorid[1]));
					            supplierQuoteElement.setQuoteNumber(numberorid[0]);
			            }else{
			            	Supplier supplier=supplierDao.findByCode(supplierquoteNumber);
			            	if(null!=supplier){
			            		ClientOrderVo clientOrderVo=clientOrderDao.findById(Integer.parseInt(clientOrderId));
			            		SupplierInquiry supplierinquiry=new SupplierInquiry();
			            			Integer id=clientOrderVo.getClientInquiryId();
			            			ClientInquiry clientInquiry=clientInquiryDao.selectByPrimaryKey(id);
			            			supplierinquiry.setClientInquiryId(id);
			            			supplierinquiry.setSupplierId(supplier.getId());
			            			String quoteNumber = supplierInquiryService.getQuoteNumberSeq(clientInquiry.getInquiryDate(),supplier.getId());
			            			supplierinquiry.setQuoteNumber(quoteNumber);
			            			supplierinquiry.setInquiryDate(clientInquiry.getInquiryDate());
			            			supplierinquiry.setDeadline(clientInquiry.getDeadline());
			            			supplierinquiry.setRemark(clientInquiry.getRemark());
			            			supplierInquiryDao.insertSelective(supplierinquiry);
			            		
			            		com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo clientOrderElementVo=clientOrderElementDao.findByClientQuoteElementId(clientOrderElementId);
			            		Integer supplierInquiryId = supplierinquiry.getId();
			            		SupplierInquiryElement supplierInquiryElement=new SupplierInquiryElement();
			            		supplierInquiryElement.setSupplierInquiryId(supplierInquiryId);
			            		supplierInquiryElement.setClientInquiryElementId(clientOrderElementVo.getClientInquiryElementId());
			            		supplierInquiryElementDao.insertSelective(supplierInquiryElement);
			            		
			            		Integer supplierInquiryElementId =supplierInquiryElement.getId();
//			            		ClientQuote clientQuote=new ClientQuote();
//			            		clientQuote.setIds(clientOrderElementId.toString());
//			            		List<ClientOrderElementVo>  orderdatalist=supplierOrderElementDao.ClientOrderData(clientQuote);
//			            		Integer currencyId = orderdatalist.get(0).getCurrencyId();
//			            		Double exchangeRate =orderdatalist.get(0).getExchangeRate();
			            		Supplier supplier2=supplierDao.selectByPrimaryKey(supplier.getId());
			            		ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
			            		SupplierQuote supplierQuote=new SupplierQuote();
			            			supplierQuote.setCurrencyId(supplier2.getCurrencyId());
			            			supplierQuote.setExchangeRate(exchangeRate.getRate());
			            			supplierQuote.setSupplierInquiryId(supplierInquiryId);
			            			supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
			            			GregorianCalendar gc=new GregorianCalendar(); 
			            			gc.setTime(Calendar.getInstance().getTime()); 
			            			gc.add(2,1); 
			            			gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
			            			Date validity=gc.getTime();
			            			supplierQuote.setValidity(validity);
			            			supplierQuote.setQuoteNumber(supplierinquiry.getQuoteNumber());
			            			supplierQuoteDao.insertSelective(supplierQuote);
			            		
			            		Integer supplierQuoteId =supplierQuote.getId();
			            		
			            		SupplierQuoteElement quoteElement=new SupplierQuoteElement();
			            		quoteElement.setSupplierQuoteId(supplierQuoteId);
			            		quoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
			            		quoteElement.setElementId(clientOrderElementVo.getQuoteElementId());
			            		quoteElement.setPartNumber(clientOrderElementVo.getQuotePartNumber());
			            		quoteElement.setDescription(clientOrderElementVo.getQuoteDescription());
			            		quoteElement.setAmount(clientOrderElementVo.getSupplierQuoteAmount());
			            		quoteElement.setUnit(clientOrderElementVo.getQuoteUnit());
			            		quoteElement.setPrice(price);
			            		quoteElement.setLeadTime("");
			            		quoteElement.setRemark(clientOrderElementVo.getQuoteRemark());
			            		quoteElement.setConditionId(clientOrderElementVo.getConditionId());
			            		quoteElement.setCertificationId(clientOrderElementVo.getCertificationId());
			            		quoteElement.setSupplierQuoteStatusId(70);
			            		supplierQuoteElementDao.insert(quoteElement);
			            		clientInquiryElementDao.updateByPartNumber(quoteElement.getPartNumber());
			            		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
			            		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
									if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701 || clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700){
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
			            		supplierOrderElement.setSupplierQuoteElementId(quoteElement.getId());
			            		supplierQuoteElement.setQuoteNumber(supplierinquiry.getQuoteNumber());
			            	}
			            }
			           
			           List<ClientQuoteElementVo> sqe= clientQuoteElementDao.findSupplierQuoteElementId(supplierQuoteElement);
			           
			            supplierOrderElement.setClientOrderElementId(clientOrderElementId);
			            SupplierOrder supplierOrder=new SupplierOrder();
			            supplierOrder.setClientOrderId(Integer.parseInt(clientOrderId));
			            supplierOrder.setSupplierId(sqe.get(0).getSupplierId());
			        	SupplierOrder supplierOrder2 = supplierOrderDao.findByClientOrderId(supplierOrder);
			        	supplierOrderElement.setAmount(amount);
						supplierOrderElement.setPrice(price);
						supplierOrderElement.setLeadTime(leadTime);
						if(null!=shipId){
						supplierOrderElement.setShipWayId(shipId);
						}
						if(null!=destId){
						supplierOrderElement.setDestination(destId.toString());
						}
						 Double storageAmount =0.0;
						 List<StorageFlowVo> flowVos=importpackageElementDao.findStorageBySupplierQuoteElementId(supplierOrderElement.getSupplierQuoteElementId());
						 	if(flowVos.size()>0){
						 		for (StorageFlowVo storageFlowVo : flowVos) {
									if(storageFlowVo.getStorageAmount()>0){
										storageAmount+=storageFlowVo.getStorageAmount();
										supplierList.add(storageFlowVo);
									}
								}
						 	}
						 		if(storageAmount>0){
						 			ClientOrderElement clientOrderElement=new ClientOrderElement();
									clientOrderElement.setId(supplierOrderElement.getClientOrderElementId());
									clientOrderElement.setAmount(supplierOrderElement.getAmount());
									clientOrderElement.setClientOrderId(supplierOrder.getClientOrderId());
									ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(supplierOrder.getClientOrderId());
									List<EmailVo> emailVo=new ArrayList<EmailVo>();
									 emailVo=clientOrderElementService.Storage(supplierList, clientOrderElement, clientOrder);
									 emailVos.addAll(emailVo);
									
						 		}else{
									if (supplierOrder2!=null&&null==supplierOrder2.getOrderType()) {
										supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
										int count = supplierOrderElementDao.getOrderCount(supplierOrderElement.getSupplierOrderId());
										if (count == 0) {
											supplierOrderElement.setItem(1);
										}else {
											supplierOrderElement.setItem(count+1);
										}
										supplierOrderElementDao.insertSelective(supplierOrderElement);
									}else {
//										ClientQuote clientQuote=new ClientQuote();
//					            		clientQuote.setIds(clientOrderElementId.toString());
//					            		List<ClientOrderElementVo>  orderdatalist=supplierOrderElementDao.ClientOrderData(clientQuote);
										supplierOrder.setOrderDate(new Date());
										Supplier supplier=supplierDao.selectByPrimaryKey(supplierOrder.getSupplierId());
										supplierOrder.setPrepayRate(supplier.getPrepayRate());
										supplierOrder.setShipPayRate(supplier.getShipPayRate());
										supplierOrder.setReceivePayPeriod(supplier.getReceivePayPeriod());
										supplierOrder.setReceivePayRate(supplier.getReceivePayRate());
										ClientOrder clientOrder = clientOrderDao.selectByPrimaryKey(supplierOrder.getClientOrderId());
										Double terms = clientOrder.getPrepayRate()*100;
										supplierOrder.setTerms(terms.intValue());
										try {
											supplierOrder.setOrderNumber(getOrderNumberSeq(supplierOrder.getOrderDate()));
										} catch (SQLException e) {
											e.printStackTrace();
										}	
					            		ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
										supplierOrder.setCurrencyId(supplier.getCurrencyId());
										supplierOrder.setExchangeRate(exchangeRate.getRate());
										supplierOrder.setOrderStatusId(1000090);
										supplierOrderDao.insertSelective(supplierOrder);
										supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
										int count = supplierOrderElementDao.getOrderCount(supplierOrderElement.getSupplierOrderId());
										if (count == 0) {
											supplierOrderElement.setItem(1);
										}else {
											supplierOrderElement.setItem(count+1);
										}
										supplierOrderElementDao.insertSelective(supplierOrderElement);
				
										}
						 		}
						messageVo.setMessage("Save success");
						messageVo.setFlag(true);
					 }
				}
				
			}
			 clientOrderElementService.sendEmail(emailVos, userVo.getUserId());
		} catch (IOException e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				messageVo.setMessage("Save failed");
				messageVo.setFlag(false);
			}
			e.printStackTrace();
		}
		return messageVo;
	}
	
	public String getOrderNumberSeq(Date date) throws SQLException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ORD-");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		String yearKey = Integer.toString(year % 100);
		buffer.append(StringUtils.leftPad(yearKey, 2, '0'));
		SeqKey seqKey = new SeqKey();
		seqKey.setKeyName(yearKey);
		seqKey.setSchemaName("crm");
		seqKey.setTableName("supplier_order");
		SeqKey seqKey2 = seqKeyDao.findMaxSeq(seqKey);
		Integer seqNumber=null;
		if (seqKey2!=null) {
			seqNumber = seqKey2.getSeq().intValue();
			seqNumber++;
		}
		if(seqKey2==null){
			seqKey.setUpdateTimestamp(new Date());
			seqKey.setSeq(1001);
			seqNumber=1001;
			seqKeyDao.insertSelective(seqKey);
			buffer.append(StringUtils.leftPad(Integer.toString(seqNumber), 6, '0'));
			return buffer.toString();
		}
		if (seqNumber != seqKey2.getSeq()) {
			seqKey.setSeq(seqNumber);
			seqKeyDao.updateByPrimaryKeySelective(seqKey);
			buffer.append(StringUtils.leftPad(Integer.toString(seqNumber), 6, '0'));
		}
		
		return buffer.toString();
	}

	@Override
	public List<SupplierOrderElement> findStorageByPn(String quotePartNumber) {
		return supplierOrderElementDao.findStorageByPn(quotePartNumber);
	}

	@Override
	public Integer getElementId(Integer clientQuoteElementId) {
		return supplierOrderElementDao.getElementId(clientQuoteElementId);
	}

	@Override
	public Integer getSqeElementId(Integer clientQuoteElementId) {
		return supplierOrderElementDao.getSqeElementId(clientQuoteElementId);
	}

	@Override
	public List<SupplierListVo> findByElementIdsAndCoeId(Integer cieElementId, Integer sqeElementId,
			Integer clientOrderElementId) {
		return supplierOrderElementDao.findByElementIdsAndCoeId(cieElementId, sqeElementId, clientOrderElementId);
	}

	@Override
	public com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo findByClientOrderElementId(
			Integer clientOrderElementId) {
		return supplierOrderElementDao.findByClientOrderElementId(clientOrderElementId);
	}

	@Override
	public List<SupplierListVo> getOnPassagePartNumber(Integer cieElementId, Integer sqeElementId) {
		return supplierOrderElementDao.getOnPassagePartNumber(cieElementId, sqeElementId);
	}

	@Override
	public String findBsnBySoeId(Integer id) {
		return supplierOrderElementDao.findBsnBySoeId(id);
	}
	
	public AddSupplierOrderElementVo getImportDateBySoeId(Integer supplierOrderElementId){
		return supplierOrderElementDao.getImportDateBySoeId(supplierOrderElementId);
	}

	@Override
	public Double getImportAmount(Integer id) {
		return supplierOrderElementDao.getImportAmount(id);
	}

	@Override
	public Integer getSupplierOrderELementId(Integer id) {
		return supplierOrderElementDao.getSupplierOrderELementId(id);
	}
	
	public Double getOtherOrderAmount(Integer clientOrderElementId,Integer supplierOrderElementId){
		return supplierOrderElementDao.getOtherOrderAmount(clientOrderElementId, supplierOrderElementId);
	}
	
	public SupplierOrderElement getOrdersAmount(Integer clientOrderElementId){
		return supplierOrderElementDao.getOrdersAmount(clientOrderElementId);
	}
	
	public Double getAmountByCLientElementId(Integer clientOrderElementId){
		return supplierOrderElementDao.getAmountByCLientElementId(clientOrderElementId);
	}
	
	public List<String> getImportNumberById(Integer id){
		return supplierOrderElementDao.getImportNumberById(id);
	}
	
	public Integer getShelfLifeBySoeId(String soeId){
		return supplierOrderElementDao.getShelfLifeBySoeId(soeId);
	}
	
	public List<SupplierOrderElementFj> getByOrderId(PageModel<String> page){
		return supplierOrderElementFjDao.getByOrderId(page);
	}
	
	public List<SupplierOrderElementFj> selectForeignKey(String ids){
		return supplierOrderElementFjDao.selectForeignKey(ids);
	}
	
	public List<Integer> getShipByOrderNumber(String orderNumber){
		return supplierOrderElementDao.getShipByOrderNumber(orderNumber);
	}
	
	public ResultVo checkAptitude(Integer id){
		List<SupplierAptitude> list = supplierAptitudeDao.selectBySupplierQuoteElementId(id);
		List<SupplierAptitude> warnList = new ArrayList<SupplierAptitude>();
		if (list.size() > 0 && list.get(0) != null) {
			for (int i = 0; i < list.size(); i++) {
				Date today = new Date();
				if (list.get(i).getExpireDate().getTime() < today.getTime()) {
					warnList.add(list.get(i));
				}
			}
			if (warnList.size() > 0) {
				StringBuffer warn = new StringBuffer();
				for (int i = 0; i <warnList.size(); i++) {
					warn.append(list.get(i).getName()).append(",");
				}
				warn.delete(warn.length()-1, warn.length());
				return new ResultVo(true, "该供应商的资质："+warn.toString()+"已过期！");
			}else {
				return new ResultVo(false, "");
			}
		}else {
			return new ResultVo(false, "");
		}
	}
	
	public Double getTotalByOrder(Integer supplierOrderId){
		return supplierOrderElementDao.getTotalByOrder(supplierOrderId);
	}

}
