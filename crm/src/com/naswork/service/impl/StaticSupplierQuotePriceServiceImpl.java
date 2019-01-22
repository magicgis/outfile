package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.ClientContactDao;
import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.StaticSupplierQuotePriceDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.model.Client;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.StaticClientQuotePrice;
import com.naswork.model.StaticSupplierQuotePrice;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.StaticSupplierQuotePriceService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

@Service("staticSupplierQuotePriceService")
public class StaticSupplierQuotePriceServiceImpl implements
		StaticSupplierQuotePriceService {

	@Resource
	private StaticSupplierQuotePriceDao staticSupplierQuotePriceDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientDao clientDao;
	@Resource
	private ClientContactDao clientContactDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	
	
	@Override
	public int insertSelective(StaticSupplierQuotePrice record) {
		return staticSupplierQuotePriceDao.insertSelective(record);
	}

	@Override
	public StaticSupplierQuotePrice selectByPrimaryKey(Integer id) {
		return staticSupplierQuotePriceDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StaticSupplierQuotePrice record) {
		return staticSupplierQuotePriceDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<StaticSupplierQuotePrice> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(staticSupplierQuotePriceDao.listPage(page));
    }
	
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
			List<StaticSupplierQuotePrice> elementList = new ArrayList<StaticSupplierQuotePrice>();
			List<Integer> supplierIds = new ArrayList<Integer>();
			int ab = sheet.getPhysicalNumberOfRows();
			HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			Map< Integer, Integer> supplierAndCurrency = new HashMap<Integer, Integer>();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            		StaticSupplierQuotePrice staticSupplierQuotePrice = new StaticSupplierQuotePrice();
	            	 	Integer supplierId = null;
	            	 	if (row.getCell(0) != null) {
	            	 		String code = null;
	            	 		Cell oneCell = row.getCell(0);
		            	 	if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	 		code = oneCell.toString().trim();
							}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								code = dataFormatter.formatCellValue(oneCell);
							}
		            	 	Supplier supplier = supplierDao.findByCode(code);
		            	 	if (supplier != null) {
		            	 		staticSupplierQuotePrice.setSupplierId(supplier.getId());
			            	 	//staticSupplierQuotePrice.setCurrencyId(supplier.getCurrencyId());
			            	 	supplierId = supplier.getId();
							}
		            	 	
						}
	            	 	String partNumber = null;
	            	 	if (row.getCell(1) != null) {
	            	 		Cell oneCell = row.getCell(1);
		            	 	if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	 		partNumber = oneCell.toString().trim();
							}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								partNumber = dataFormatter.formatCellValue(oneCell);
							}
		            	 	staticSupplierQuotePrice.setPartNumber(partNumber);
						}
	            	 	Double price = null;
	            	 	if (row.getCell(2) != null) {
	            	 		Cell oneCell = row.getCell(2);
	            	 		if (dataFormatter.formatCellValue(oneCell) != null && !"".equals(dataFormatter.formatCellValue(oneCell))) {
	            	 			price = new Double(dataFormatter.formatCellValue(oneCell));
	            	 			staticSupplierQuotePrice.setPrice(price);;
							}
		            	 	
						}
	            	 	Integer currencyId = null;
	            	 	if (row.getCell(3) != null) {
	            	 		String value = row.getCell(3).toString().trim();
	            	 		List<SystemCode> list = systemCodeDao.selectByValue(value);
	            	 		if (list.size() >0) {
								staticSupplierQuotePrice.setCurrencyId(list.get(0).getId());
								currencyId = list.get(0).getId();
							}
	            	 		
						}
	            	 	if (row.getCell(4) != null) {
	            	 		Cell oneCell = row.getCell(4);
	            	 		if (dataFormatter.formatCellValue(oneCell) != null && !"".equals(dataFormatter.formatCellValue(oneCell))) {
								Integer leadTime = new Integer(dataFormatter.formatCellValue(oneCell));
								staticSupplierQuotePrice.setLeadTime(leadTime);
							}
						}
	            	 	Integer year = null;
	            	 	if (row.getCell(5) != null) {
	            	 		Cell oneCell = row.getCell(5);
	            	 		if (dataFormatter.formatCellValue(oneCell) != null && !"".equals(dataFormatter.formatCellValue(oneCell))) {
								year = new Integer(dataFormatter.formatCellValue(oneCell));
								staticSupplierQuotePrice.setYear(year);
							}
	            	 		
						}
	            	 	if (row.getCell(6) != null) {
							String code = row.getCell(6).toString().trim();
							List<SystemCode> list = systemCodeDao.selectByAllCode(code);
							if (list.size() >0 ) {
								staticSupplierQuotePrice.setConditionId(list.get(0).getId());
							}
						}
	            	 	if (row.getCell(7) != null) {
	            	 		String code = row.getCell(7).toString().trim();
							List<SystemCode> list = systemCodeDao.selectByAllCode(code);
							if (list.size() >0 ) {
								staticSupplierQuotePrice.setCertificationId(list.get(0).getId());
							}
						}
	            	 	if (currencyId != null && supplierId != null) {
							if (!supplierAndCurrency.containsKey(supplierId)) {
								supplierAndCurrency.put(supplierId, currencyId);
							}
						}
			            
			            //存起读取的数据等待保存进数据库
			            if (!elementList.contains(staticSupplierQuotePrice)) {
			            	elementList.add(staticSupplierQuotePrice);
						}
			            if (!supplierIds.contains(staticSupplierQuotePrice.getSupplierId())) {
			            	supplierIds.add(staticSupplierQuotePrice.getSupplierId());
						}
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (elementList.size()>0){
				//存档
				/*excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());*/
				ClientInquiry clientInquiry = new ClientInquiry();
				Client client = clientDao.findByCode("199");
				List<ClientContact> contacts = clientContactDao.selectByClientId(client.getId());
				List<SystemCode> air = systemCodeDao.findType("AIR_TYPE");
				List<SystemCode> biz = systemCodeDao.findType("BIZ_TYPE");
				clientInquiry.setAirTypeId(air.get(0).getId());
				clientInquiry.setBizTypeId(biz.get(0).getId());
				clientInquiry.setClientContactId(contacts.get(0).getId());
				clientInquiry.setClientId(client.getId());
				clientInquiry.setInquiryDate(new Date());
				clientInquiry.setSourceNumber("static price");
				clientInquiry.setUpdateTimestamp(new Date());
				clientInquiryService.add(clientInquiry);
				List<ClientInquiryElement> clientElements = new ArrayList<ClientInquiryElement>();
				for (int i = 0; i < elementList.size(); i++) {
						staticSupplierQuotePriceDao.insertSelective(elementList.get(i));
						ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
						clientInquiryElement.setStatusSupplierQuotePriceId(elementList.get(i).getId());
						clientInquiryElement.setItem(i+1);
						clientInquiryElement.setCsn(i+1);
						clientInquiryElement.setAmount(new Double(1));
						clientInquiryElement.setClientInquiryId(clientInquiry.getId());
						clientInquiryElement.setPartNumber(elementList.get(i).getPartNumber());
						clientInquiryElement.setSupplierId(elementList.get(i).getSupplierId());
						clientInquiryElement.setUnit("EA");
						clientInquiryElement.setPrice(elementList.get(i).getPrice());
						clientInquiryElement.setCurrencyId(elementList.get(i).getCurrencyId());
						clientInquiryElement.setConditionId(elementList.get(i).getConditionId());
						clientInquiryElement.setCertificationId(elementList.get(i).getCertificationId());
						clientInquiryElement.setUpdateTimestamp(new Date());
						clientElements.add(clientInquiryElement);
				}
				clientInquiryElementService.insertSelective(clientElements, clientInquiry.getId());
				for (int i = 0; i < supplierIds.size(); i++) {
					Supplier supplier = supplierDao.selectByPrimaryKey(supplierIds.get(i));
					SupplierInquiry supplierInquiry = new SupplierInquiry();
					supplierInquiry.setClientInquiryId(clientInquiry.getId());
					supplierInquiry.setSupplierId(supplierIds.get(i));
					supplierInquiry.setInquiryDate(new Date());
					supplierInquiry.setQuoteNumber(supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(), supplierInquiry.getSupplierId()));
					supplierInquiryService.insertSelective(supplierInquiry);
					SupplierQuote supplierQuote = new SupplierQuote();
					supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
					supplierQuote.setQuoteDate(new Date());
					supplierQuote.setCurrencyId(supplierAndCurrency.get(supplierIds.get(i)));
					supplierQuoteService.insertSelective(supplierQuote);
					for (ClientInquiryElement clientInquiryElement: clientElements) {
						if (clientInquiryElement.getSupplierId().equals(supplierIds.get(i))) {
							SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
							supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
							supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
							supplierInquiryElementService.insertSelective(supplierInquiryElement);
							SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
							supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
							supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
							supplierQuoteElement.setPartNumber(clientInquiryElement.getPartNumber());
							supplierQuoteElement.setDescription("static price");
							supplierQuoteElement.setAmount(new Double(1));
							supplierQuoteElement.setUnit("EA");
							supplierQuoteElement.setPrice(clientInquiryElement.getPrice());
							supplierQuoteElement.setConditionId(clientInquiryElement.getConditionId());
							supplierQuoteElement.setCertificationId(clientInquiryElement.getCertificationId());
							supplierQuoteElement.setElementId(clientInquiryElement.getElementId());
							supplierQuoteElement.setUpdateTimestamp(new Date());
							supplierQuoteElement.setSupplierQuoteStatusId(70);
							supplierQuoteElementService.insert(supplierQuoteElement);
							StaticSupplierQuotePrice staticSupplierQuotePrice = new StaticSupplierQuotePrice();
							staticSupplierQuotePrice.setId(clientInquiryElement.getStatusSupplierQuotePriceId());
							staticSupplierQuotePrice.setSupplierQuoteElementId(supplierQuoteElement.getId());
							staticSupplierQuotePriceDao.updateByPrimaryKeySelective(staticSupplierQuotePrice);
						}
						
					}
				}
				
				success=true;
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;
			}
		} catch (Exception e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			e.printStackTrace();
			success = false;
			message="save unsuccessful!";
		}
		
		return null;
	}
	
	public ResultVo addWithPage(List<StaticSupplierQuotePrice> list) {
		try {
			ClientInquiry clientInquiry = new ClientInquiry();
			Client client = clientDao.findByCode("199");
			List<ClientContact> contacts = clientContactDao.selectByClientId(client.getId());
			List<SystemCode> air = systemCodeDao.findType("AIR_TYPE");
			List<SystemCode> biz = systemCodeDao.findType("BIZ_TYPE");
			clientInquiry.setAirTypeId(air.get(0).getId());
			clientInquiry.setBizTypeId(biz.get(0).getId());
			clientInquiry.setClientContactId(contacts.get(0).getId());
			clientInquiry.setClientId(client.getId());
			clientInquiry.setInquiryDate(new Date());
			clientInquiry.setSourceNumber("static price");
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryService.add(clientInquiry);
			List<ClientInquiryElement> clientElements = new ArrayList<ClientInquiryElement>();
			List<Integer> supplierIds = new ArrayList<Integer>();
			Map< Integer, Integer> supplierAndCurrency = new HashMap<Integer, Integer>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCode() != null && !"".equals(list.get(i).getCode())) {
					Supplier supplier = supplierDao.findByCode(list.get(i).getCode().trim());
					list.get(i).setSupplierId(supplier.getId());
					list.get(i).setUpdateTimestamp(new Date());
					staticSupplierQuotePriceDao.insertSelective(list.get(i));
					if (!supplierIds.contains(supplier.getId())) {
						supplierIds.add(supplier.getId());
					}
					if (!supplierAndCurrency.containsKey(supplier.getId())) {
						supplierAndCurrency.put(supplier.getId(), list.get(i).getCurrencyId());
					}
					ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
					clientInquiryElement.setStatusSupplierQuotePriceId(list.get(i).getId());
					clientInquiryElement.setItem(i+1);
					clientInquiryElement.setCsn(i+1);
					clientInquiryElement.setAmount(new Double(1));
					clientInquiryElement.setClientInquiryId(clientInquiry.getId());
					clientInquiryElement.setPartNumber(list.get(i).getPartNumber());
					clientInquiryElement.setSupplierId(list.get(i).getSupplierId());
					clientInquiryElement.setUnit("EA");
					clientInquiryElement.setPrice(list.get(i).getPrice());
					clientInquiryElement.setCurrencyId(list.get(i).getCurrencyId());
					clientInquiryElement.setConditionId(list.get(i).getConditionId());
					clientInquiryElement.setCertificationId(list.get(i).getCertificationId());
					clientInquiryElement.setUpdateTimestamp(new Date());
					clientElements.add(clientInquiryElement);
				}	
			}
			clientInquiryElementService.insertSelective(clientElements, clientInquiry.getId());
			for (int i = 0; i < supplierIds.size(); i++) {
				Supplier supplier = supplierDao.selectByPrimaryKey(supplierIds.get(i));
				SupplierInquiry supplierInquiry = new SupplierInquiry();
				supplierInquiry.setClientInquiryId(clientInquiry.getId());
				supplierInquiry.setSupplierId(supplierIds.get(i));
				supplierInquiry.setInquiryDate(new Date());
				supplierInquiry.setQuoteNumber(supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(), supplierInquiry.getSupplierId()));
				supplierInquiryService.insertSelective(supplierInquiry);
				SupplierQuote supplierQuote = new SupplierQuote();
				supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
				supplierQuote.setQuoteDate(new Date());
				supplierQuote.setCurrencyId(supplierAndCurrency.get(supplierIds.get(i)));
				supplierQuoteService.insertSelective(supplierQuote);
				for (ClientInquiryElement clientInquiryElement: clientElements) {
					if (clientInquiryElement.getSupplierId().equals(supplierIds.get(i))) {
						SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
						supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
						supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
						supplierInquiryElementService.insertSelective(supplierInquiryElement);
						SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
						supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
						supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
						supplierQuoteElement.setPartNumber(clientInquiryElement.getPartNumber());
						supplierQuoteElement.setDescription("static price");
						supplierQuoteElement.setAmount(new Double(1));
						supplierQuoteElement.setUnit("EA");
						supplierQuoteElement.setPrice(clientInquiryElement.getPrice());
						supplierQuoteElement.setConditionId(clientInquiryElement.getConditionId());
						supplierQuoteElement.setCertificationId(clientInquiryElement.getCertificationId());
						supplierQuoteElement.setElementId(clientInquiryElement.getElementId());
						supplierQuoteElement.setUpdateTimestamp(new Date());
						supplierQuoteElement.setSupplierQuoteStatusId(70);
						supplierQuoteElementService.insert(supplierQuoteElement);
						StaticSupplierQuotePrice staticSupplierQuotePrice = new StaticSupplierQuotePrice();
						staticSupplierQuotePrice.setId(clientInquiryElement.getStatusSupplierQuotePriceId());
						staticSupplierQuotePrice.setSupplierQuoteElementId(supplierQuoteElement.getId());
						staticSupplierQuotePriceDao.updateByPrimaryKeySelective(staticSupplierQuotePrice);
					}
					
				}
			}
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增失败！");
		}
	}
	
	public void edit(StaticSupplierQuotePrice staticSupplierQuotePrice) {
		staticSupplierQuotePriceDao.updateByPrimaryKeySelective(staticSupplierQuotePrice);
		StaticSupplierQuotePrice sta = staticSupplierQuotePriceDao.selectByPrimaryKey(staticSupplierQuotePrice.getId());
		SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
		supplierQuoteElement.setId(sta.getSupplierQuoteElementId());
		supplierQuoteElement.setPrice(sta.getPrice());
		supplierQuoteElementService.updateByPrimaryKeySelective(supplierQuoteElement);
	}
	
	public int deleteByPrimaryKey(Integer id){
		return staticSupplierQuotePriceDao.deleteByPrimaryKey(id);
	}

}
