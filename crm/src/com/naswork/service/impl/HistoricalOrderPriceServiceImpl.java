package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import oracle.net.aso.c;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.HistoricalOrderPriceDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.TManufactoryDao;
import com.naswork.dao.TPartDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.HistoricalOrderPrice;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.Supplier;
import com.naswork.model.TManufactory;
import com.naswork.model.TPart;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.crmstock.controller.CaacVo;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientService;
import com.naswork.service.GyFjService;
import com.naswork.service.HistoricalOrderPriceService;
import com.naswork.service.SupplierService;
import com.naswork.service.TPartService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.StringUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("historicalOrderPriceService")
public class HistoricalOrderPriceServiceImpl implements
		HistoricalOrderPriceService {

	@Resource
	private HistoricalOrderPriceDao historicalOrderPriceDao;
	@Resource
	private SupplierService supplierService;
	@Resource
	private ClientService clientService;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private TPartService tPartService;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private GyFjService gyFjService;
	@Resource
	private TManufactoryDao tManufactoryDao;
	
	
	@Override
	public int insertSelective(HistoricalOrderPrice record) {
		return historicalOrderPriceDao.insertSelective(record);
	}

	@Override
	public HistoricalOrderPrice selectByPrimaryKey(Integer id) {
		return historicalOrderPriceDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(HistoricalOrderPrice record) {
		return historicalOrderPriceDao.updateByPrimaryKeySelective(record);
	}
	
	public void clientPage(PageModel<HistoricalOrderPrice> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(historicalOrderPriceDao.clientPage(page));
    }
	
	public void supplierPage(PageModel<HistoricalOrderPrice> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(historicalOrderPriceDao.supplierPage(page));
    }
	
	
	public MessageVo clientUploadExcel(MultipartFile multipartFile){
		boolean success = true;
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
			List<HistoricalOrderPrice> elementList = new ArrayList<HistoricalOrderPrice>();
			List<HistoricalOrderPrice> error = new ArrayList<HistoricalOrderPrice>();
			List<Integer> errorLine = new ArrayList<Integer>();
			List<Integer> commitLine = new ArrayList<Integer>();
			List<Integer> repeatLine = new ArrayList<Integer>();
			List<HistoricalOrderPrice> addList = new ArrayList<HistoricalOrderPrice>();
			int ab = sheet.getPhysicalNumberOfRows();
			HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			int line = 2;
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            		HistoricalOrderPrice historicalOrderPrice = new HistoricalOrderPrice();
	            		if (row.getCell(0) != null) {
	            			Cell oneCell = row.getCell(0);
	            			String code = dataFormatter.formatCellValue(oneCell);
	            			if (code.trim().equals("1")) {
	            				Client client = clientService.findByCode("904");
		            			historicalOrderPrice.setClientId(client.getId());
							}else if (code.trim().equals("3")) {
								Client client = clientService.findByCode("903");
		            			historicalOrderPrice.setClientId(client.getId());
							}else if (code.trim().equals("5")) {
								Client client = clientService.findByCode("905");
		            			historicalOrderPrice.setClientId(client.getId());
							}else {
								Client client = clientService.findByCode(code);
		            			historicalOrderPrice.setClientId(client.getId());
							}
	            			
						}
	            		String partNum = "";
	            		if (row.getCell(1) != null) {
	            			Cell oneCell = row.getCell(1);
	    		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	    		            	partNum = oneCell.toString();
	    					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
	    						partNum = dataFormatter.formatCellValue(oneCell);
	    					}
	    		            historicalOrderPrice.setPartNum(partNum);
						}
	            		List<String> cageCode = new ArrayList<String>();
	            		if (row.getCell(7) != null) {
	            			Cell oneCell = row.getCell(7);
	    		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	    		            	String[] cages = oneCell.toString().trim().split("/");
	    		            	for (int j = 0; j < cages.length; j++) {
	    		            		if (cages[j].length() < 5) {
	    		            			cages[j] = StringUtils.leftPad(cages[j], 5, '0');
									}
									cageCode.add(cages[j]);
								}
	    					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
	    						String cageString = dataFormatter.formatCellValue(oneCell).trim();
	    						if (cageString.length() < 5) {
	    							cageString = StringUtils.leftPad(cageString, 5, '0');
								}
	    						cageCode.add(cageString);
	    					}
	    		            historicalOrderPrice.setCages(cageCode);
						}
	            		String desc = "";
	            		if (row.getCell(2) != null) {
	            			desc = row.getCell(2).toString();
	            			historicalOrderPrice.setPartName(desc);
						}
	            		/*if (!"".equals(desc) && "".equals(cageCode)) {
	            			List<TPart> tParts = tPartDao.selectByPartNumberCode(clientInquiryService.getCodeFromPartNumber(partNum));
	            			for (int j = 0; j < tParts.size(); j++) {
		            			String[] des = desc.split(",");
		            			String[] nameArray = tParts.get(j).getPartName().split(",");
								for (int k = 0; k < nameArray.length; k++) {
									for (int k2 = 0; k2 < des.length; k2++) {
										if (nameArray[k].equals(des[k2])) {
											historicalOrderPrice.setBsn(tParts.get(j).getBsn());
											break;
										}
									}
								}
							}
						}else*/
	            		historicalOrderPrice.setBsns(new ArrayList<String>());
	            		List<TPart> parts = new ArrayList<TPart>();
	            		if (cageCode.size() > 0) {
	            			for (int j = 0; j < cageCode.size(); j++) {
	            				parts.clear();
	            				TPart tPart = new TPart();
								tPart.setCageCode(cageCode.get(j));
								tPart.setPartNum(partNum);
								parts = tPartDao.getTPart(tPart);
								if (parts.size() > 0) {
									for (int k = 0; k < parts.size(); k++) {
										historicalOrderPrice.getBsns().add(parts.get(k).getBsn());
									}
								}else {
									List<TManufactory> list = tManufactoryDao.getMsnByCageCode(cageCode.get(j));
									if (list.size() > 0) {
										if (list.size() == 1) {
											tPart.setMsn(list.get(0).getMsn());
											tPart.setPartName(desc);
											tPartService.insertSelective(tPart);
										}else if (list.size() > 1) {
											for (int k = 0; k < list.size(); k++) {
												if (list.get(k).getMsn().startsWith("0")) {
													tPart.setMsn(list.get(k).getMsn());
													tPart.setPartName(desc);
													tPartService.insertSelective(tPart);
												}
											}
										}
										historicalOrderPrice.getBsns().add(tPart.getBsn());
									}
									
								}
								
							}
							
						}
	            		Double amount = null;
	            		if (row.getCell(3) != null) {
	            			Cell oneCell = row.getCell(3);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				amount = new Double(dataFormatter.formatCellValue(oneCell));
		            			historicalOrderPrice.setAmount(amount);
							}
	            			
						}
	            		if (row.getCell(4) != null) {
	            			Cell oneCell = row.getCell(4);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				Double price = new Double(dataFormatter.formatCellValue(oneCell));
		            			historicalOrderPrice.setPrice(price);
							}
	            			
						}
	            		if (row.getCell(6) != null) {
	            			Cell oneCell = row.getCell(6);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				try {
	            					Integer year = new Integer(dataFormatter.formatCellValue(oneCell));
			            			historicalOrderPrice.setYear(year);
								} catch (Exception e) {
									e.printStackTrace();
								}
	            				
							}
	            			
						}
	            		List<TManufactory> tManufactorys = tManufactoryDao.selectByCageCode(historicalOrderPrice.getCageCode());
	            		if (historicalOrderPrice.getBsns().size() > 0) {
	            			commitLine.add(i);
	            			for (int j = 0; j < historicalOrderPrice.getBsns().size(); j++) {
								HistoricalOrderPrice historicalOrderPrice2 = new HistoricalOrderPrice();
								historicalOrderPrice2.setClientId(historicalOrderPrice.getClientId());
								historicalOrderPrice2.setAmount(historicalOrderPrice.getAmount());
								historicalOrderPrice2.setPrice(historicalOrderPrice.getPrice());
								historicalOrderPrice2.setYear(historicalOrderPrice.getYear());
								historicalOrderPrice2.setBsn(historicalOrderPrice.getBsns().get(j));
								elementList.add(historicalOrderPrice2);
							}
							//elementList.add(historicalOrderPrice);
						}else if (historicalOrderPrice.getCageCode() != null && !"".equals(historicalOrderPrice.getCageCode())) {
							repeatLine.add(i);
							addList.add(historicalOrderPrice);
						}else {
							historicalOrderPrice.setLine(line);
							error.add(historicalOrderPrice);
							errorLine.add(i);
						}
			            
				}
	            line++;
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),
					this.fetchYwTableName(),this.fetchYwTablePkName(),errorLine,commitLine,repeatLine);
			/*if (error.size() > 0) {
				StringBuffer errorsString = new StringBuffer();
				errorsString.append("line ");
				for (int i = 0; i < error.size(); i++) {
					errorsString.append(error.get(i).getLine()).append(",");
				}
				errorsString.deleteCharAt(errorsString.length()-1);
				errorsString.append(",BSN ARE NOT EXIST!");
				return new MessageVo(false, errorsString.toString());
			}else {
				if (addList.size() > 0) {
					for (HistoricalOrderPrice historicalOrderPrice2 : addList) {
						TPart tPart = new TPart();
						tPart.setPartNum(historicalOrderPrice2.getPartNum());
						tPart.setPartName(historicalOrderPrice2.getPartName());
						tPart.setCageCode(historicalOrderPrice2.getCageCode());
						tPartService.insertSelective(tPart);
					}
				}
				if (elementList.size()>0){
					//存档
					excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
							this.fetchYwTableName(),this.fetchYwTablePkName());
					for (HistoricalOrderPrice historicalOrderPrice : elementList) {
						historicalOrderPriceDao.insertSelective(historicalOrderPrice);
					}
					success=true;
					message="save successful!";
					messageVo.setFlag(success);
					messageVo.setMessage(message);
					return messageVo;
				}
			}*/
			if (elementList.size()>0){
				for (HistoricalOrderPrice historicalOrderPrice : elementList) {
					historicalOrderPriceDao.insertSelective(historicalOrderPrice);
				}
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
		
		return new MessageVo(success, message);
	}
	
	
	public MessageVo supplierUploadExcel(MultipartFile multipartFile){
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
			List<HistoricalOrderPrice> elementList = new ArrayList<HistoricalOrderPrice>();
			List<HistoricalOrderPrice> error = new ArrayList<HistoricalOrderPrice>();
			List<HistoricalOrderPrice> addList = new ArrayList<HistoricalOrderPrice>();
			int ab = sheet.getPhysicalNumberOfRows();
			HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			int line = 2;
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(3) != null && !"".equals(row.getCell(3).toString())) {
	            		HistoricalOrderPrice historicalOrderPrice = new HistoricalOrderPrice();
	            		/*if (row.getCell(0) != null) {
	            			Cell oneCell = row.getCell(0);
	            			String code = dataFormatter.formatCellValue(oneCell);
	            			Supplier supplier = supplierDao.findByCode(code);
	            			historicalOrderPrice.setSupplierId(supplier.getId());
						}*/
	            		String partNum = "";
	            		if (row.getCell(3) != null) {
	            			Cell oneCell = row.getCell(3);
	    		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	    		            	partNum = oneCell.toString();
	    					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
	    						partNum = dataFormatter.formatCellValue(oneCell);
	    					}
	    		            historicalOrderPrice.setPartNum(partNum);
						}
	            		String cageCode = "";
	            		if (row.getCell(11) != null) {
	            			Cell oneCell = row.getCell(11);
	    		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	    		            	cageCode = oneCell.toString().trim();
	    					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
	    						cageCode = dataFormatter.formatCellValue(oneCell);
	    					}
	    		            historicalOrderPrice.setCageCode(cageCode);
						}
	            		String desc = "";
	            		if (row.getCell(4) != null) {
	            			desc = row.getCell(4).toString();
	            			historicalOrderPrice.setPartName(desc);
						}
	            		if (!"".equals(desc) && "".equals(cageCode)) {
	            			List<TPart> tParts = tPartDao.selectByPartNumberCode(clientInquiryService.getCodeFromPartNumber(partNum));
	            			for (int j = 0; j < tParts.size(); j++) {
		            			String[] des = desc.split(",");
		            			String[] nameArray = tParts.get(j).getPartName().split(",");
								for (int k = 0; k < nameArray.length; k++) {
									for (int k2 = 0; k2 < des.length; k2++) {
										if (nameArray[k].equals(des[k2])) {
											historicalOrderPrice.setBsn(tParts.get(j).getBsn());
											break;
										}
									}
								}
							}
						}else if (!"".equals(cageCode)) {
							TPart tPart = new TPart();
							tPart.setCageCode(cageCode);
							tPart.setPartNum(partNum);
							List<TPart> tParts = tPartDao.getTPart(tPart);
							if (tParts.size() > 0) {
								historicalOrderPrice.setBsn(tParts.get(0).getBsn());
							}
						}
	            		Double amount = null;
	            		if (row.getCell(5) != null) {
	            			Cell oneCell = row.getCell(5);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				amount = new Double(dataFormatter.formatCellValue(oneCell));
		            			historicalOrderPrice.setAmount(amount);
							}
	            			
						}
	            		if (row.getCell(8) != null) {
	            			Cell oneCell = row.getCell(8);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				Integer year = new Integer(dataFormatter.formatCellValue(oneCell));
		            			historicalOrderPrice.setYear(year);
							}
	            			
						}
	            		if (row.getCell(10) != null) {
	            			Cell oneCell = row.getCell(10);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				Double price = new Double(dataFormatter.formatCellValue(oneCell));
		            			historicalOrderPrice.setPrice(price);
		            			Row row2 = sheet.getRow(sheet.getFirstRowNum());
		            			String coString = row2.getCell(10).toString().trim();
		            			Supplier supplier = supplierDao.findByCode(coString);
		            			historicalOrderPrice.setSupplierId(supplier.getId());
							}
	            			
						}
	            		if (row.getCell(14) != null) {
	            			Cell oneCell = row.getCell(14);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				HistoricalOrderPrice historicalOrderPrice2 = historicalOrderPrice;
	            				Double price = new Double(dataFormatter.formatCellValue(oneCell));
	            				historicalOrderPrice2.setPrice(price);
		            			Row row2 = sheet.getRow(sheet.getFirstRowNum());
		            			String coString = row2.getCell(14).toString().trim();
		            			Supplier supplier = supplierDao.findByCode(coString);
		            			historicalOrderPrice2.setSupplierId(supplier.getId());
		            			if (!"".equals(historicalOrderPrice.getBsn()) && historicalOrderPrice.getBsn() != null) {
									elementList.add(historicalOrderPrice2);
								}else if (historicalOrderPrice.getCageCode() != null && !"".equals(historicalOrderPrice.getCageCode()) && historicalOrderPrice.getPartNum() != null) {
									addList.add(historicalOrderPrice2);
								}else {
									historicalOrderPrice.setLine(line);
									error.add(historicalOrderPrice2);
								}
							}
	            			
						}
	            		if (row.getCell(15) != null) {
	            			Cell oneCell = row.getCell(15);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				HistoricalOrderPrice historicalOrderPrice2 = historicalOrderPrice;
	            				Double price = new Double(dataFormatter.formatCellValue(oneCell));
	            				historicalOrderPrice2.setPrice(price);
		            			Row row2 = sheet.getRow(sheet.getFirstRowNum());
		            			String coString = row2.getCell(15).toString().trim();
		            			Supplier supplier = supplierDao.findByCode(coString);
		            			historicalOrderPrice2.setSupplierId(supplier.getId());
		            			if (!"".equals(historicalOrderPrice.getBsn()) && historicalOrderPrice.getBsn() != null) {
									elementList.add(historicalOrderPrice2);
								}else if (historicalOrderPrice.getCageCode() != null && !"".equals(historicalOrderPrice.getCageCode()) && historicalOrderPrice.getPartNum() != null) {
									addList.add(historicalOrderPrice2);
								}else {
									historicalOrderPrice.setLine(line);
									error.add(historicalOrderPrice2);
								}
							}
	            			
						}
	            		if (row.getCell(16) != null) {
	            			Cell oneCell = row.getCell(16);
	            			if (!"".equals(dataFormatter.formatCellValue(oneCell))) {
	            				HistoricalOrderPrice historicalOrderPrice2 = historicalOrderPrice;
	            				Double price = new Double(dataFormatter.formatCellValue(oneCell));
	            				historicalOrderPrice2.setPrice(price);
		            			Row row2 = sheet.getRow(sheet.getFirstRowNum());
		            			String coString = row2.getCell(16).toString().trim();
		            			Supplier supplier = supplierDao.findByCode(coString);
		            			historicalOrderPrice2.setSupplierId(supplier.getId());
		            			if (!"".equals(historicalOrderPrice.getBsn()) && historicalOrderPrice.getBsn() != null) {
									elementList.add(historicalOrderPrice2);
								}else if (historicalOrderPrice.getCageCode() != null && !"".equals(historicalOrderPrice.getCageCode()) && historicalOrderPrice.getPartNum() != null) {
									addList.add(historicalOrderPrice2);
								}else {
									historicalOrderPrice.setLine(line);
									error.add(historicalOrderPrice2);
								}
							}
	            			
						}
	            		if (!"".equals(historicalOrderPrice.getBsn()) && historicalOrderPrice.getBsn() != null) {
							elementList.add(historicalOrderPrice);
						}else if (historicalOrderPrice.getCageCode() != null && !"".equals(historicalOrderPrice.getCageCode()) && historicalOrderPrice.getPartNum() != null) {
							addList.add(historicalOrderPrice);
						}else {
							historicalOrderPrice.setLine(line);
							error.add(historicalOrderPrice);
						}
			            
				}
	            line++;
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (error.size() > 0) {
				StringBuffer errorsString = new StringBuffer();
				errorsString.append("line ");
				for (int i = 0; i < error.size(); i++) {
					errorsString.append(error.get(i).getLine()).append(",");
				}
				errorsString.deleteCharAt(errorsString.length()-1);
				errorsString.append(",BSN ARE NOT EXIST!");
				return new MessageVo(false, errorsString.toString());
			}else {
				if (addList.size() > 0) {
					for (HistoricalOrderPrice historicalOrderPrice2 : addList) {
						TPart tPart = new TPart();
						tPart.setPartNum(historicalOrderPrice2.getPartNum());
						tPart.setPartName(historicalOrderPrice2.getPartName());
						tPart.setCageCode(historicalOrderPrice2.getCageCode());
						tPartService.insertSelective(tPart);
					}
				}
				if (elementList.size()>0){
					//存档
					/*excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
							this.fetchYwTableName(),this.fetchYwTablePkName());*/
					for (HistoricalOrderPrice historicalOrderPrice : elementList) {
						historicalOrderPriceDao.insertSelective(historicalOrderPrice);
					}
					success=true;
					message="save successful!";
					messageVo.setFlag(success);
					messageVo.setMessage(message);
					return messageVo;
				}
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
	
	public MessageVo uploadExcel(MultipartFile multipartFile){
		boolean success = true;
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
			List<ImportStorageLocationList> elementList = new ArrayList<ImportStorageLocationList>();
			//test
			List<CaacVo> caac = new ArrayList<CaacVo>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            		
	            	 	String partNumber = row.getCell(0).toString();
	            	 	List<CaacVo> list = tPartDao.getMessageInCaac(partNumber);
	            	 	if (list.size() == 2) {
	            	 		if ((list.get(0).getCageCode() != null && !"".equals(list.get(0).getCageCode()) && !"".equals(list.get(1).getOem()) && list.get(1).getOem() != null) || 
	        						(list.get(1).getCageCode() != null && !"".equals(list.get(1).getCageCode()) && !"".equals(list.get(0).getOem()) && list.get(0).getOem() != null)) {
	            	 			CaacVo caacVo = new CaacVo();
	            	 			for (int j = 0; j < list.size(); j++) {
									if (list.get(j).getCageCode() != null && !"".equals(list.get(j).getCageCode())) {
										caacVo.setCageCode(list.get(j).getCageCode());
									}else if (list.get(j).getOem() != null && !"".equals(list.get(j).getOem())) {
										caacVo.setOem(list.get(j).getOem());
									}
									if (list.get(j).getAtaChapterSection() != null && !"".equals(list.get(j).getAtaChapterSection())) {
										caacVo.setAtaChapterSection(list.get(j).getAtaChapterSection());
									}
								}
	            	 			Cell cell = row.getCell(2);
	            	 			String value = caacVo.getCageCode();
	            				cell.setCellValue(StringUtil.delHTMLTag((String) value));
	            	 			Cell cell2 = row.getCell(3);
	            	 			String value2 = caacVo.getOem();
	            	 			cell2.setCellValue(StringUtil.delHTMLTag((String) value2));
	            	 			Cell cell3 = row.getCell(4);
	            	 			String value3 = caacVo.getAtaChapterSection().toString();
	            	 			cell3.setCellValue(StringUtil.delHTMLTag((String) value3));
	            	 			
	            	 		}
						}
			            
				}
	        }
			excelBackup2(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),"1",
					this.fetchYwTableName(),this.fetchYwTablePkName());
			//判断是否有错误行
			/*MessageVo messageVo = new MessageVo();
			if (elementList.size()>0){
				//存档
				
				for (ImportStorageLocationList importStorageLocationList : elementList) {
					importStorageLocationListService.insertSelective(importStorageLocationList);
				}
				success=true;
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;
			}*/
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
		
		return new MessageVo(success, message);
	}
	
	public void excelBackup2(POIExcelWorkBook wb,String FilePath,String FileName,String ywId,String ywTableName,String ywTablePkName) {
    	//String outputFilePath = this.fetchOutputFilePath();
		String excelType = "xls";
		String outputFileName = FileConstant.UPLOAD_REALPATH+File.separator+FilePath + File.separator + FileName + "." + excelType;
		String path = FilePath + File.separator + FileName + "." + excelType;
		String fileShortName = outputFileName.substring(outputFileName.lastIndexOf(File.separator)+1);
		File outputPath = new File(FilePath);
		if (!outputPath.exists()) {
			outputPath.mkdirs();
		}
		File outputFile = new File(outputFileName);
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(outputFile);
			wb.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	PageModel<GyExcel> page = new PageModel<GyExcel>();
		page.put("ywTableName", ywTableName);
		page.put("ywId", ywId);
		GyFj gyFj = new GyFj();
		gyFj.setFjName(fileShortName);
		gyFj.setFjPath(path);
		gyFj.setFjType(excelType);
		gyFj.setFjLength(outputFile.length());
		gyFj.setLrsj(new Date());
		UserVo user = ContextHolder.getCurrentUser(); 
		if(user!=null){
			gyFj.setUserId(user.getUserId());
		}else{
			gyFj.setUserId("0");
		}
		gyFj.setYwId(ywId);
		gyFj.setYwTableName(ywTableName);
		gyFj.setYwTablePkName(ywTablePkName);
		gyFjService.add(gyFj);
	}
	
	public void excelBackup(POIExcelWorkBook wb,String FilePath,String FileName,String ywTableName,String ywTablePkName
			,List<Integer> errorList,List<Integer> commitList,List<Integer> repearList) {
    	//String outputFilePath = this.fetchOutputFilePath();
		String excelType = "xls";
		String outputFileName = FileConstant.UPLOAD_REALPATH+File.separator+FilePath + File.separator + FileName + "." + excelType;
		String path = FilePath + File.separator + FileName + "." + excelType;
		String fileShortName = outputFileName.substring(outputFileName.lastIndexOf(File.separator)+1);
		File outputPath = new File(FilePath);
		if (!outputPath.exists()) {
			outputPath.mkdirs();
		}
		File outputFile = new File(outputFileName);
		FileOutputStream fos;
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row lastRow = sheet.getRow(lastRowNum);
		short firstCellNum = lastRow.getFirstCellNum();
		short lastCellNum = lastRow.getLastCellNum();
		HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
		for (int rowIndex = firstRowNum+1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (errorList.contains(rowIndex)) {
				Cell cell = row.createCell(16);
				String value = "不通过";
				cell.setCellValue(StringUtil.delHTMLTag((String) value));
				row = sheet.getRow(rowIndex-1);
				String partNum = "";
        		if (row.getCell(1) != null) {
        			Cell oneCell = row.getCell(1);
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	partNum = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						partNum = dataFormatter.formatCellValue(oneCell);
					}
				}
        		List<TPart> list = tPartService.selectByPartNumberCode(clientInquiryService.getCodeFromPartNumber(partNum));
        		if (list.size() > 0) {
					StringBuffer stringBuffer = new StringBuffer();
					for (int i = 0; i < list.size(); i++) {
						stringBuffer.append(list.get(i).getCageCode()).append(",");
					}
					stringBuffer.deleteCharAt(stringBuffer.length()-1);
					Cell cell2 = row.createCell(17);
					String value2 = stringBuffer.toString();
					cell2.setCellValue(StringUtil.delHTMLTag((String) value2));
				}
//				this.write(cell, value);
			}else if (commitList.contains(rowIndex)) {
				Cell cell = row.createCell(16);
				String value = "通过";
				cell.setCellValue(StringUtil.delHTMLTag((String) value));
//				this.write(cell, value);
			}else if (repearList.contains(rowIndex)) {
				Cell cell = row.createCell(16);
				String value = "重复";
				cell.setCellValue(StringUtil.delHTMLTag((String) value));
//				this.write(cell, value);
			}
		}	
		try {
			fos = new FileOutputStream(outputFile);
			wb.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	PageModel<GyExcel> page = new PageModel<GyExcel>();
		page.put("ywTableName", ywTableName);
		page.put("ywId", ContextHolder.getCurrentUser().getUserId());
		GyFj gyFj = new GyFj();
		gyFj.setFjName(fileShortName);
		gyFj.setFjPath(path);
		gyFj.setFjType(excelType);
		gyFj.setFjLength(outputFile.length());
		gyFj.setLrsj(new Date());
		UserVo user = ContextHolder.getCurrentUser(); 
		if(user!=null){
			gyFj.setUserId(user.getUserId());
		}else{
			gyFj.setUserId("0");
		}
		gyFj.setYwId(ContextHolder.getCurrentUser().getUserId());
		gyFj.setYwTableName(ywTableName);
		gyFj.setYwTablePkName(ywTablePkName);
		gyFjService.add(gyFj);
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
		
		return "HistoryPrice"+"_"+this.fetchUserName()+"_"+format.format(now);
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
		return "historical_order_price";
	}
    
    public String fetchYwTablePkName() {
		return "id";
	}
    
    public String fetchMappingKey() {
		return "HistoryPriceExcel";
	}
    
    public List<HistoricalOrderPrice> selectByCLientInquiryId(Integer clientInuqiryId){
    	return historicalOrderPriceDao.selectByCLientInquiryId(clientInuqiryId);
    }
    
    public List<HistoricalOrderPrice> selectByBsn(String bsn){
    	return historicalOrderPriceDao.selectByBsn(bsn);
    }

    public HistoricalOrderPrice getPriceByBsn(String bsn){
    	return historicalOrderPriceDao.getPriceByBsn(bsn);
    }
    
    public List<HistoricalOrderPrice> getByClient(String clientId,String bsn,String year){
    	return historicalOrderPriceDao.getByClient(clientId, bsn,year);
    }
    
    public List<HistoricalOrderPrice> getByPart(String partNumber){
    	return historicalOrderPriceDao.getByPart(partNumber);
    }
    
}
