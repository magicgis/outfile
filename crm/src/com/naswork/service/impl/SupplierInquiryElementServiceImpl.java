package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.TPartDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Element;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.TPartService;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.UserVo;

@Service("supplierInquiryElementService")
public class SupplierInquiryElementServiceImpl implements
		SupplierInquiryElementService {

	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private TPartService tPartService;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	
	

	public int insertSelective(SupplierInquiryElement supplierInquiryElement) {
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
		if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
			if (clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700){
				clientInquiryElement.setElementStatusId(701);
				clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
			}
		}
		clientInquiryElement.setInquiryStatus(1);
		clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
		return supplierInquiryElementDao.insertSelective(supplierInquiryElement);
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
	            if (row!=null) {
	            		HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			            Integer id = new Integer(dataFormatter.formatCellValue(row.getCell(0)));
			            String partNum = row.getCell(4).toString().trim();
			            String desc = row.getCell(5).toString();
			            String msn = row.getCell(10).toString();
			            ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(id);
			            String bsn = tPartDao.getBsnByPartAndMsn(clientInquiryService.getCodeFromPartNumber(partNum), msn.trim());
			            if (bsn == null || "".equals(bsn)) {
							TPart tPart = new TPart();
							tPart.setPartName(desc);
							tPart.setPartNum(partNum);
							tPart.setMsn(msn);
							tPartService.insertSelective(tPart);
							clientInquiryElement.setBsn(tPart.getBsn());
			            	elementList.add(clientInquiryElement);
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
							clientInquiryElement.setBsn(bsn);
			            	elementList.add(clientInquiryElement);
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
    
    public boolean addInquiry(String [] ids,Integer clientInquiryElementId){
    	for (int i = 0; i < ids.length; i++) {
			Integer supplierId = supplierInquiryDao.getSupplierId(ids[i]);
			boolean success = insertInquiry(supplierId,clientInquiryElementId);
			if (!success) {
				return success;
			}
		}
		return true;
    }
    
    
    public boolean insertInquiry(Integer supplierId,Integer clientInquiryElementId){
    	try {
    		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElementId);
    		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		Date now = new Date();
    		List<SupplierInquiry> list = supplierInquiryDao.getInquiry(supplierId.toString(), clientInquiryElement.getClientInquiryId().toString(), format.format(now));
    		if (list.size() > 0) {
    			int count = supplierInquiryDao.getElementCount(clientInquiryElement.getPartNumber(), list.get(0).getId().toString());
    			if (count==0) {
    				SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
    				supplierInquiryElement.setClientInquiryElementId(clientInquiryElementId);
    				supplierInquiryElement.setSupplierInquiryId(list.get(0).getId());
    				supplierInquiryElement.setUpdateTimestamp(new Date());
    				supplierInquiryElementDao.insertSelective(supplierInquiryElement);
    				clientInquiryElement.setInquiryStatus(1);
    				if (clientInquiryElement.getElementStatusId() != null) {
    					if (clientInquiryElement.getElementStatusId().equals(new Integer(700))) {
    						clientInquiryElement.setElementStatusId(701);
    					}
    				}else {
    					clientInquiryElement.setElementStatusId(701);
    				}
        			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
        			if (clientInquiry.getInquiryStatusId().equals(30) || 30==clientInquiry.getInquiryStatusId()) {
        				clientInquiry.setInquiryStatusId(31);
            			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
					}
    			}
    		}else {
    			String quoteNumber = supplierInquiryService.getQuoteNumberSeq(new Date(),supplierId);
    			SupplierInquiry supplierInquiry = new SupplierInquiry();
    			supplierInquiry.setClientInquiryId(clientInquiryElement.getClientInquiryId());
    			supplierInquiry.setDeadline(clientInquiry.getDeadline());
    			supplierInquiry.setInquiryDate(new Date());
    			supplierInquiry.setSupplierId(supplierId);
    			supplierInquiry.setQuoteNumber(quoteNumber);
    			supplierInquiry.setUpdateTimestamp(new Date());
    			supplierInquiryDao.insertSelective(supplierInquiry);
    			clientInquiry.setInquiryStatusId(31);
    			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
    			SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
    			supplierInquiryElement.setClientInquiryElementId(clientInquiryElementId);
    			supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
    			supplierInquiryElement.setUpdateTimestamp(new Date());
    			supplierInquiryElementDao.insertSelective(supplierInquiryElement);
    			clientInquiryElement.setInquiryStatus(1);
    			if (clientInquiryElement.getElementStatusId() != null) {
					if (clientInquiryElement.getElementStatusId().equals(new Integer(700))) {
						clientInquiryElement.setElementStatusId(701);
					}
				}else {
					clientInquiryElement.setElementStatusId(701);
				}
    			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	
    }

}
