package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientOrderElementFinalDao;
import com.naswork.dao.ClientOrderElementNotmatchDao;
import com.naswork.dao.ClientOrderElementUploadDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ClientWeatherOrderDao;
import com.naswork.dao.ClientWeatherOrderElementBackUpDao;
import com.naswork.dao.ClientWeatherOrderElementDao;
import com.naswork.dao.CrmStockDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementFinal;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ClientWeatherOrderElementBackUp;
import com.naswork.model.CrmStock;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;
@Service("clientWeatherOrderElementService")
public class ClientWeatherOrderElementServiceImpl implements ClientWeatherOrderElementService {

	@Resource
	private ClientWeatherOrderElementDao clientWeatherOrderElementDao;
	@Resource
	private ClientWeatherOrderDao clientWeatherOrderDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private ClientOrderElementUploadDao clientOrderElementUploadDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private ClientOrderElementFinalDao clientOrderElementFinalDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientOrderElementFinalDao clientOrderElementFianlDao;
	@Resource
	private CrmStockDao crmStockDao;
	@Resource
	private ClientOrderElementNotmatchDao clientOrderElementNotmatchDao;
	@Resource
	private ClientWeatherOrderElementBackUpDao clientWeatherOrderElementBackUpDao;
	@Resource
	private ClientQuoteDao clientQuoteDao;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return clientWeatherOrderElementDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientWeatherOrderElement record) {
		return clientWeatherOrderElementDao.insert(record);
	}

	@Override
	public int insertSelective(ClientWeatherOrderElement record) {
		return clientWeatherOrderElementDao.insertSelective(record);
	}

	@Override
	public ClientWeatherOrderElement selectByPrimaryKey(Integer id) {
		return clientWeatherOrderElementDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientWeatherOrderElement record) {
		return clientWeatherOrderElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ClientWeatherOrderElement record) {
		return clientWeatherOrderElementDao.updateByPrimaryKey(record);
	}
	
	public void findByOrderIdPage(PageModel<ClientOrderElementVo> page,GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(clientWeatherOrderElementDao.findByOrderIdPage(page));
	}
	
	public List<ClientOrderElementVo> findByOrderId(Integer id) {
		return clientWeatherOrderElementDao.findByOrderId(id);
	}
	
    public List<ClientOrderElementVo> elementList(Integer id) {
		return clientWeatherOrderElementDao.elementList(id);
	}

	@Override
	public void insertSelective(List<ClientOrderElement> list, Integer clientOrderId, String userId) {
		ClientWeatherOrder clientWeatherOrder = clientWeatherOrderDao.selectByPrimaryKey(clientOrderId);
    	for (ClientOrderElement  clientOrderElement: list) {
    		if (clientOrderElement.getAmount()!=null) {
    			ClientWeatherOrderElement record =new ClientWeatherOrderElement();
    			record.setClientQuoteElementId(clientOrderElement.getClientQuoteElementId());
    			record.setAmount(clientOrderElement.getAmount());
    			record.setPrice(clientOrderElement.getPrice());
    			record.setLeadTime(clientOrderElement.getLeadTime());
    			Integer leadtime = new Integer(clientOrderElement.getLeadTime());
    			Calendar calendar = Calendar.getInstance();
    			calendar.setTime(clientWeatherOrder.getOrderDate());
    			calendar.add(Calendar.DATE, new Integer(leadtime));
    			record.setDeadline(calendar.getTime());
    			record.setClientWeatherOrderId(clientOrderId);
    			record.setUpdateTimestamp(new Date());
    			record.setRemark(clientOrderElement.getRemark());
    			record.setUnit(clientOrderElement.getUnit());
    			record.setDescription(clientOrderElement.getDescription());
    			if(null!=clientOrderElement.getFixedCost()){
    				record.setFixedCost(clientOrderElement.getFixedCost());
    			}
    			if(null!=clientOrderElement.getBankCharges()){
    				record.setBankCharges(clientOrderElement.getBankCharges());
    			}
    			if(null!=clientOrderElement.getCertificationId()){
    				record.setCertificationId(clientOrderElement.getCertificationId());
    			}
    			if(null!=clientOrderElement.getPartNumber()){
    				record.setPartNumber(clientOrderElement.getPartNumber());
    			}
        		clientWeatherOrderElementDao.insertSelective(record);
    		}
    	}
	}
	
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
		    List<ClientWeatherOrderElementBackUp> backUpList = new ArrayList<ClientWeatherOrderElementBackUp>();
			StringBuffer lines=new StringBuffer();
			
		    //记录行数
			Integer flag = 2;
			
			//询价item
			List<ClientOrderElementVo> list = clientWeatherOrderElementDao.findItem(clientOrderId);
			List<ClientWeatherOrderElement> entityList = new ArrayList<ClientWeatherOrderElement>();
			List<ClientOrderElementVo> unMatch = new ArrayList<ClientOrderElementVo>();
			ClientWeatherOrder clientOrder = clientWeatherOrderDao.selectByPrimaryKey(clientOrderId);
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
		            String remark ="";
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
		            Calendar calendar = Calendar.getInstance();
	    			calendar.setTime(clientOrder.getOrderDate());
	    			calendar.add(Calendar.DATE, new Double(leadTime).intValue());
	    			Date deadline = calendar.getTime();
	    			ClientWeatherOrderElementBackUp clientWeatherOrderElementBackUp = new ClientWeatherOrderElementBackUp(userId, number, partNumber, " ", clientOrderId, amount, price, leadTime, deadline, certificationId, fixedCost, remark, description, unit);
		            for (ClientOrderElementVo clientOrderElementVo : list) {
		            	ClientWeatherOrderElement clientWeatherOrderElement = new ClientWeatherOrderElement(clientOrderId, clientOrderElementVo.getId(), amount, price, leadTime, deadline, new Date(),partNumber,certificationId,fixedCost,remark);
						clientWeatherOrderElement.setPartNumber(partNumber);
						clientWeatherOrderElement.setUnit(unit);
						if (number.equals(clientOrderElementVo.getItem())&&partNumber.toLowerCase().equals(clientOrderElementVo.getInquiryPartNumber().trim().toLowerCase())) {
							clientWeatherOrderElementBackUp.setClientQuoteElementId(clientWeatherOrderElement.getClientQuoteElementId());
							clientWeatherOrderElement.setItem(item);
							 clientWeatherOrderElement.setDescription(description);
							entityList.add(clientWeatherOrderElement);
					         ClientQuoteElementVo clientQuoteElementVo=clientQuoteElementDao.findClientInuqiry(clientOrderElementVo.getId());
					         if(null!=clientQuoteElementVo&&null!=clientQuoteElementVo.getBsn()){
					        	 CrmStock crmStock=crmStockDao.findByBsn(clientQuoteElementVo.getBsn());
					        	 if (crmStock != null) {
						        	 String desc=crmStock.getPartName().toUpperCase();
						        	 description=description.toUpperCase();
						        	 if(desc.indexOf(description)<=-1){
						        		 clientWeatherOrderElement.setBsn(clientQuoteElementVo.getBsn());
						        		 descNotMatch=true;
						        	 }
					        	 }
					         }
					         ifCorrect = 1;
					         break;
						}
					}
		           if (ifCorrect==0) {
		        	   clientWeatherOrderElementBackUp.setLine(flag);
		        	   clientWeatherOrderElementBackUp.setErrorFlag(1);
		        	   clientWeatherOrderElementBackUp.setError("S/N或者件号有误！");
		        	   clientWeatherOrderElementBackUp.setPartNumber(partNumber);
		            	ClientOrderElement clientOrderElement = new ClientOrderElement();
						clientOrderElement.setUserId(userId);
						clientOrderElement.setItem(number);
						clientOrderElement.setPartNumber(partNumber);
						clientOrderElement.setLine(flag);
						clientOrderElement.setError("S/N或者件号有误！");
						errorList.add(clientOrderElement);
					}else {
						clientWeatherOrderElementBackUp.setLine(flag);
						clientWeatherOrderElementBackUp.setErrorFlag(0);
					}
		            backUpList.add(clientWeatherOrderElementBackUp);
		            flag++;
				}
	            
			}
			if (errorList.size()>0) {
				for (int i = 0; i < backUpList.size(); i++) {
					clientWeatherOrderElementBackUpDao.insertSelective(backUpList.get(i));
				}
				lines.append("Line ");
				/*for (ClientOrderElement clientOrderElement : errorList) {
					clientOrderElementUploadDao.insertSelective(clientOrderElement);
				}*/
				success=false;
				messageVo.setFlag(success);
				messageVo.setMessage("新增失败！");
				return messageVo;
			}
			if(descNotMatch){
				for (ClientWeatherOrderElement clientWeatherOrderElement : entityList) {
					clientWeatherOrderElement.setUserId(Integer.parseInt(userVo.getUserId()));	
				clientOrderElementNotmatchDao.add(clientWeatherOrderElement);
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
				for (ClientWeatherOrderElement clientWeatherOrderElement : entityList) {
					clientWeatherOrderElementDao.insert(clientWeatherOrderElement);
				}
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
		}
		return null;
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
	public List<ClientWeatherOrderElement> selectByForeignKey(Integer clientOrderId) {
		return clientWeatherOrderElementDao.selectByForeignKey(clientOrderId);
	}

	@Override
	public List<Integer> findUser(Integer clientOrderElementId) {
		return clientWeatherOrderElementDao.findUser(clientOrderElementId);
	}

	@Override
	public ClientOrderElementVo findByclientOrderELementId(Integer id) {
		return clientWeatherOrderElementDao.findByclientOrderELementId(id);
	}
	
	@Override
	public MessageVo wetherorderuploadExcel(MultipartFile multipartFile, Integer clientOrderId, Integer userId) {
		boolean success = true;
		String message = "保存成功！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		//List<EmailVo> emailVos=new ArrayList<EmailVo>();
		try {
			List<ListDateVo> cert=supplierQuoteDao.findcert();
			List<SystemCode>  orderCode=systemCodeDao.findType("ORDER_STATUS");
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
			//StringBuffer lines=new StringBuffer();
			
		    //记录行数
			//Integer flag = 2;
			
			//询价item
			//List<ClientOrderElementVo> list = clientOrderElementDao.findItem(clientOrderId);
			
			//List<ClientOrderElement> entityList = new ArrayList<ClientOrderElement>();
			List<ClientWeatherOrderElement> clientOrderElements=new ArrayList<ClientWeatherOrderElement>();
			ClientWeatherOrder clientWeatherOrder = clientWeatherOrderDao.selectByPrimaryKey(clientOrderId);
			
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
		          
		           
//	    			String orderStatus = row.getCell(13).toString();
//	    			Integer orderStatusId=null;
//	    			for (SystemCode systemCode : orderCode) {
//	    				if(systemCode.getValue().equals(orderStatus)){
//	    					orderStatusId=systemCode.getId();
//	    				}
//	    			}
	    			ClientWeatherOrderElement clientWeatherOrderElement=new ClientWeatherOrderElement();
	    			  Pattern pattern = Pattern.compile("[0-9]*"); 
			            Matcher isNum = pattern.matcher(leadTime);
			            if( !isNum.matches() ){
			            	
			            }else{
			            	 Calendar calendar = Calendar.getInstance();
				    			calendar.setTime(clientWeatherOrder.getOrderDate());
				    			calendar.add(Calendar.DATE, new Double(leadTime).intValue());
				    			Date deadline = calendar.getTime();
				    			clientWeatherOrderElement.setDeadline(deadline);
			            }
	    			Integer clientOrderElementId=clientWeatherOrderElementDao.findByOrderNumberAndItem(clientWeatherOrder.getOrderNumber(), number.toString());
	    			clientWeatherOrderElement.setId(clientOrderElementId);
	    			clientWeatherOrderElement.setAmount(amount);
	    			clientWeatherOrderElement.setFixedCost(fixedCost);
	    			clientWeatherOrderElement.setPrice(price);
	    			clientWeatherOrderElement.setLeadTime(leadTime);
	    			clientWeatherOrderElement.setDescription(description);
//	    			clientWeatherOrderElement.setOrderStatusId(orderStatusId);
	    			clientWeatherOrderElement.setCertificationId(certificationId);
	    			clientOrderElements.add(clientWeatherOrderElement);
				}
	            
			}
			if (errorList.size()==0) {
				/*//批量删除明细
				clientOrderElementDao.removeByClientOrderId(clientOrderId);*/
				clientInquiryService.excelBackup(wb, this.fetchOutputFilePath(), this.fetchOutputFileName(), clientOrderId.toString(),
						this.fetchYwTableName(), this.fetchYwTablePkName());
				for (ClientWeatherOrderElement clientWeatherOrderElement : clientOrderElements) {
						clientWeatherOrderElementDao.updateByPrimaryKeySelective(clientWeatherOrderElement);
				}
				
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
	
	@Override
	public MessageVo finalUploadExcel(MultipartFile multipartFile, Integer clientOrderId, Integer userId) {
		boolean success = true;
		String message = "保存成功！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		//List<EmailVo> emailVos=new ArrayList<EmailVo>();
		try {
			List<ListDateVo> cert=supplierQuoteDao.findcert();
			List<SystemCode>  orderCode=systemCodeDao.findType("ORDER_STATUS");
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
			List<ClientOrderElementFinal> clientOrderElementFinal=new ArrayList<ClientOrderElementFinal>();
			ClientWeatherOrder clientWeatherOrder = clientWeatherOrderDao.selectByPrimaryKey(clientOrderId);
			
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
		          
		           
	    			String orderStatus = row.getCell(13).toString();
	    			Integer orderStatusId=null;
	    			Integer clientOrderElementId=clientWeatherOrderElementDao.findByOrderNumberAndItem(clientWeatherOrder.getOrderNumber(), number.toString());
    			  if(null!=orderStatus&&orderStatus.equals("客户取消合同")){
//			            	ClientWeatherOrderElement clientOrderElement2 = clientWeatherOrderElementDao.selectByPrimaryKey(clientOrderElementId);
//			    			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement2.getClientQuoteElementId());
//			    			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
//			    				clientInquiryElement.setElementStatusId(711);
//			    			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
		    			orderStatusId=64;
		            }else if(null!=orderStatus){
		            	
		    			for (SystemCode systemCode : orderCode) {
		    				if(systemCode.getValue().equals(orderStatus)){
		    					orderStatusId=systemCode.getId();
		    				}
		    			}
		            }
	    			ClientOrderElementFinal elementFinal=new ClientOrderElementFinal();
	    			  Pattern pattern = Pattern.compile("[0-9]*"); 
			            Matcher isNum = pattern.matcher(leadTime);
			            if( !isNum.matches() ){
			            	
			            }else{
			            	 Calendar calendar = Calendar.getInstance();
				    			calendar.setTime(clientWeatherOrder.getOrderDate());
				    			calendar.add(Calendar.DATE, new Double(leadTime).intValue());
				    			Date deadline = calendar.getTime();
				    			elementFinal.setDeadline(deadline);
			            }
	    			if (row.getCell(14) != null) {
	    				HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					    String index = dataFormatter.formatCellValue(row.getCell(14));
	    				elementFinal.setOrderNumberIndex(new Integer(index));
					}
	    			elementFinal.setClientOrderElementId(clientOrderElementId);
	    			elementFinal.setDescription(description);
	    			elementFinal.setAmount(amount);
	    			elementFinal.setFixedCost(fixedCost);
	    			elementFinal.setPrice(price);
	    			elementFinal.setLeadTime(leadTime);
	    			elementFinal.setOrderStatusId(orderStatusId);
	    			elementFinal.setCertificationId(certificationId);
	    			elementFinal.setClientWeatherOrderId(clientOrderId);
	    			clientOrderElementFinal.add(elementFinal);
				}
	            
			}
			if (errorList.size()==0) {
				/*//批量删除明细
				clientOrderElementDao.removeByClientOrderId(clientOrderId);*/
				clientInquiryService.excelBackup(wb, this.fetchOutputFilePath(), this.fetchOutputFileName(), clientOrderId.toString(),
						this.fetchYwTableName(), this.fetchYwTablePkName());
				for (ClientOrderElementFinal elementFinal : clientOrderElementFinal) {
					ClientOrderElementFinal orderElementFinal=clientOrderElementFinalDao.selectByPrimaryKey(elementFinal.getClientOrderElementId());
					if(null==orderElementFinal){
						clientOrderElementFinalDao.insert(elementFinal);
					}
				}
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

	@Override
	public MessageVo uploadCancellationOrder(MultipartFile multipartFile, Integer clientOrderId, Integer userId) {
		boolean success = true;
		String message = "保存成功！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		//List<EmailVo> emailVos=new ArrayList<EmailVo>();
		try {
			List<ListDateVo> cert=supplierQuoteDao.findcert();
			List<SystemCode>  orderCode=systemCodeDao.findType("ORDER_STATUS");
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
			List<ClientOrderElementFinal> clientOrderElementFinals=new ArrayList<ClientOrderElementFinal>();
			ClientWeatherOrder clientWeatherOrder = clientWeatherOrderDao.selectByPrimaryKey(clientOrderId);
			
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
		            Integer clientOrderElementId=clientWeatherOrderElementDao.findByOrderNumberAndItem(clientWeatherOrder.getOrderNumber(), number.toString());
		        	String orderStatus = row.getCell(13).toString();
		        	Integer orderStatusId=null;
		            if(null!=orderStatus&&orderStatus.equals("客户取消合同")){
//		            	ClientWeatherOrderElement clientOrderElement2 = clientWeatherOrderElementDao.selectByPrimaryKey(clientOrderElementId);
//		    			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientOrderElement2.getClientQuoteElementId());
//		    			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
//		    				clientInquiryElement.setElementStatusId(711);
//		    			clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
		    			orderStatusId=64;
		            }else if(null!=orderStatus){
		            	
		    			for (SystemCode systemCode : orderCode) {
		    				if(systemCode.getValue().equals(orderStatus)){
		    					orderStatusId=systemCode.getId();
		    				}
		    			}
		            }
	    			
		            ClientOrderElementFinal record=new ClientOrderElementFinal();
	    			  Pattern pattern = Pattern.compile("[0-9]*"); 
			            Matcher isNum = pattern.matcher(leadTime);
			            if( !isNum.matches() ){
			            	
			            }else{
			            	 Calendar calendar = Calendar.getInstance();
				    			calendar.setTime(clientWeatherOrder.getOrderDate());
				    			calendar.add(Calendar.DATE, new Double(leadTime).intValue());
				    			Date deadline = calendar.getTime();
				    			record.setDeadline(deadline);
			            }
	    			
	    			record.setClientOrderElementId(clientOrderElementId);
//	    			record.setDescription(description);
	    			record.setAmount(amount);
	    			record.setFixedCost(fixedCost);
	    			record.setPrice(price);
	    			record.setLeadTime(leadTime);
	    			record.setOrderStatusId(orderStatusId);
	    			record.setCertificationId(certificationId);
	    			record.setClientWeatherOrderId(clientOrderId);
	    			clientOrderElementFinals.add(record);
				}
	            
			}
			if (errorList.size()==0) {
				/*//批量删除明细
				clientOrderElementDao.removeByClientOrderId(clientOrderId);*/
				clientInquiryService.excelBackup(wb, this.fetchOutputFilePath(), this.fetchOutputFileName(), clientOrderId.toString(),
						this.fetchYwTableName(), this.fetchYwTablePkName());
				for (ClientOrderElementFinal clientOrderElementFinal : clientOrderElementFinals) {
						clientOrderElementFianlDao.updateStatus(clientOrderElementFinal);
				}
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
	
	@Override
	public Double sumPrice(Integer clientWeatherOrderId) {
		return clientWeatherOrderElementDao.sumPrice(clientWeatherOrderId);
	}

	@Override
	public MessageVo addClientOrder(List<ClientOrderElement> clientOrderElements) {
		UserVo userVo=ContextHolder.getCurrentUser();
		MessageVo messageVo = new MessageVo();
		boolean success = true;
		String message = "保存成功！";
		for (ClientOrderElement clientOrderElement : clientOrderElements) {
			ClientWeatherOrderElement clientWeatherOrderElement=new ClientWeatherOrderElement();
			 ClientQuoteElementVo clientQuoteElementVo=clientQuoteElementDao.findClientInuqiry(clientOrderElement.getClientQuoteElementId());
	         if(null!=clientQuoteElementVo&&null!=clientQuoteElementVo.getBsn()){
	        	 CrmStock crmStock=crmStockDao.findByBsn(clientQuoteElementVo.getBsn());
	        	 if (crmStock != null) {
	        	 String desc=crmStock.getPartName().toUpperCase();
	        	 String description=clientOrderElement.getDescription().toUpperCase();
		        	 if(desc.indexOf(description)<=-1){
		        		 desc+=","+description;
		        		 crmStock.setPartName(desc);
		        		 crmStockDao.updateByBsn(crmStock);
		        	 }
	        	 }
	         }
	         clientWeatherOrderElement.setDescription(clientOrderElement.getDescription());
	         clientWeatherOrderElement.setClientWeatherOrderId(clientOrderElement.getClientOrderId());
	         clientWeatherOrderElement.setClientQuoteElementId(clientOrderElement.getClientQuoteElementId());
	         clientWeatherOrderElement.setAmount(clientOrderElement.getAmount());
	         clientWeatherOrderElement.setPrice(clientOrderElement.getPrice());
	         clientWeatherOrderElement.setLeadTime(clientOrderElement.getLeadTime());
	         clientWeatherOrderElement.setDeadline(clientOrderElement.getDeadline());
	         clientWeatherOrderElement.setCertificationId(clientOrderElement.getCertificationId());
	         clientWeatherOrderElement.setFixedCost(clientOrderElement.getFixedCost());
	         clientWeatherOrderElement.setRemark(clientOrderElement.getRemark());
			clientWeatherOrderElementDao.insert(clientWeatherOrderElement);
		}
		message="save successful!";
		messageVo.setFlag(success);
		messageVo.setMessage(message);
		return messageVo;
	}
	
	public List<ClientOrderElementVo> getElementByIds(PageModel<String> page){
		return clientWeatherOrderElementDao.getElementByIds(page);
	}
	
	public List<ClientWeatherOrderElement> getRealPrice(Integer clientOrderElementId){
		return clientWeatherOrderElementDao.getRealPrice(clientOrderElementId);
	}
	
	public void getErroeListPage(PageModel<ClientWeatherOrderElementBackUp> page){
		page.setEntities(clientWeatherOrderElementBackUpDao.getErrorListPage(page));
	}
	
	public boolean connectByItem(String[] ids){
		try {
			for (int i = 0; i < ids.length; i++) {
				ClientWeatherOrderElementBackUp clientWeatherOrderElementBackUp = clientWeatherOrderElementBackUpDao.selectByPrimaryKey(new Integer(ids[i]));
				ClientWeatherOrder clientWeatherOrder = clientWeatherOrderDao.selectByPrimaryKey(clientWeatherOrderElementBackUp.getClientWeatherOrderId());
				ClientQuote clientQuote = clientQuoteDao.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
				
				//生成另件号询价明细
				List<ClientInquiryElement> elements = clientInquiryElementDao.getByItem(clientQuote.getClientInquiryId(), clientWeatherOrderElementBackUp.getItem());
				if (elements.size() > 0) {
					ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(elements.get(0).getId());
					ClientInquiryElement clientInquiryElement = elements.get(0);
					int maxItem = clientInquiryElementDao.getMaxItem(clientQuote.getClientInquiryId());
					clientInquiryElement.setPartNumber(clientWeatherOrderElementBackUp.getPartNumber());
					clientInquiryElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(clientWeatherOrderElementBackUp.getPartNumber()));
					clientInquiryElement.setItem(maxItem+1);;
					clientInquiryElement.setIsMain(1);
					if (elements.get(0).getMainId() == null) {
						clientInquiryElement.setMainId(elements.get(0).getId());
					}
					clientInquiryElement.setDescription(clientWeatherOrderElementBackUp.getDescription());
					clientInquiryElement.setUnit(clientWeatherOrderElementBackUp.getUnit());
					clientInquiryElement.setAmount(clientWeatherOrderElementBackUp.getAmount());
					clientInquiryElement.setId(null);
					clientInquiryElement.setSource("销售");
					clientInquiryElementDao.insertSelective(clientInquiryElement);
					//报价明细
					clientQuoteElement.setClientInquiryElementId(clientInquiryElement.getId());
					clientQuoteElement.setId(null);
					clientQuoteElementDao.insertSelective(clientQuoteElement);
					//修改状态
					clientWeatherOrderElementBackUp.setClientQuoteElementId(clientQuoteElement.getId());
					clientWeatherOrderElementBackUp.setErrorFlag(0);
					clientWeatherOrderElementBackUpDao.updateByPrimaryKeySelective(clientWeatherOrderElementBackUp);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean unCommit(String[] ids){
		try {
			for (int i = 0; i < ids.length; i++) {
				ClientWeatherOrderElementBackUp clientWeatherOrderElementBackUp = clientWeatherOrderElementBackUpDao.selectByPrimaryKey(new Integer(ids[i]));
				//修改状态
				clientWeatherOrderElementBackUp.setErrorFlag(2);
				clientWeatherOrderElementBackUpDao.updateByPrimaryKeySelective(clientWeatherOrderElementBackUp);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<ClientWeatherOrderElementBackUp> checkErrorRecord(Integer id){
		return clientWeatherOrderElementBackUpDao.checkErrorRecord(id);
	}
	
	public void deleteMessage(Integer userId){
		clientWeatherOrderElementBackUpDao.deleteMessage(userId);
	}
	
	public List<ClientWeatherOrderElementBackUp> selectByOrderId(Integer id){
		return clientWeatherOrderElementBackUpDao.selectByOrderId(id);
	}
	
	public Double getTotalPrice(Integer clientWeatherOrderId){
		return clientWeatherOrderElementDao.getTotalPrice(clientWeatherOrderId);
	}
}
