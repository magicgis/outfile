package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.dialect.FirebirdDialect;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ClientWeatherOrderDao;
import com.naswork.dao.ClientWeatherOrderElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SupplierWeatherOrderDao;
import com.naswork.dao.SupplierWeatherOrderElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OrderApproval;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrder;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ContractReviewService;
import com.naswork.service.FlowService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierWeatherOrderElementService;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
@Service("supplierWeatherOrderElementService")
public class SupplierWeatherOrderElementServiceImpl implements SupplierWeatherOrderElementService {

	@Resource
	private SupplierWeatherOrderElementDao supplierWeatherOrderElementDao;
	@Resource
	private SystemCodeDao  systemCodeDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private FlowService flowService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private Jbpm4JbyjDao JbyjDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private ClientWeatherOrderElementDao clientWeatherOrderElementDao;
	@Resource
	private ClientWeatherOrderDao clientWeatherOrderDao;
	@Resource
	private ContractReviewService contractReviewService;
	@Resource
	private UserDao userDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private SupplierWeatherOrderDao supplierWeatherOrderDao;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return supplierWeatherOrderElementDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SupplierWeatherOrderElement record) {
		return supplierWeatherOrderElementDao.insert(record);
	}

	@Override
	public int insertSelective(SupplierWeatherOrderElement record) {
		return supplierWeatherOrderElementDao.insertSelective(record);
	}

	@Override
	public SupplierWeatherOrderElement selectByPrimaryKey(Integer id) {
		return supplierWeatherOrderElementDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierWeatherOrderElement record) {
		return supplierWeatherOrderElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SupplierWeatherOrderElement record) {
		return supplierWeatherOrderElementDao.updateByPrimaryKey(record);
	}

	@Override
	public MessageVo uploadExcel(MultipartFile multipartFile, String clientOrderId,String taskdefname) {
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
			int coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();
			for (int j = 0; j < coloumNum; j++) {
				row = sheet.getRow(0);
				
	            String lowprice = row.getCell(j).toString();
	            if(lowprice.equals("确定供应商")){
	            	start=j;
	            	break;
	            }
			}
		    int frist=sheet.getFirstRowNum();
			String coeID="";
			for (int i = frist; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i+1);
				if(null==row){
					 continue;
				}
				if (row.getCell(0) != null) {
					String orderNumber=row.getCell(0).toString();
					Double a=new Double(row.getCell(1).toString());
					Integer item = a.intValue();
					Integer clientOrderElementId=clientWeatherOrderElementDao.findByOrderNumberAndItem(orderNumber, item.toString());
					if(null!=row.getCell(start-1)){
						String status=row.getCell(start-1).toString();
						if(status.equals("取消")){
							SupplierWeatherOrderElement record=new SupplierWeatherOrderElement();
							record.setClientOrderElementId(clientOrderElementId);
							record.setAmount(0.0);
							record.setPrice(0.0);
							record.setSupplierStatus(0);
							String remark="";
					          if(null!=row.getCell(start+4)){
					            	remark= row.getCell(start+4).toString();
					           }
					        record.setRemark(remark);
					        
							if(coeID.indexOf(clientOrderElementId.toString())==-1){
								Jbpm4Task jbpm4Task =new Jbpm4Task();
								jbpm4Task.setYwTableElementId(record.getClientOrderElementId());
								if(taskdefname.equals("add")){
									jbpm4Task.setTaskdefname("采购生成供应商预订单");
								}else if(taskdefname.equals("update")){
									jbpm4Task.setTaskdefname("采购询问供应商能否降价");
								}
								
								List<Jbpm4Task>  jbpm4Tasks=jbpm4TaskDao.selectByTaskName(jbpm4Task);
								for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
									OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(jbpm4Task2.getRelationId());
									orderApproval.setTaskId(null);
									if(null!=orderApproval.getSupplierWeatherOrderElementId()){
										supplierWeatherOrderElementDao.deleteByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
									}
									orderApproval.setSupplierWeatherOrderElementId(null);
									orderApprovalDao.updateByPrimaryKey(orderApproval);
								}
								coeID+=","+clientOrderElementId.toString();
							}
					        
							supplierWeatherOrderElementDao.insert(record);
							
							Jbpm4Task jbpm4Task =new Jbpm4Task();
							jbpm4Task.setYwTableElementId(record.getClientOrderElementId());
							if(taskdefname.equals("add")){
								jbpm4Task.setTaskdefname("采购生成供应商预订单");
							}else if(taskdefname.equals("update")){
								jbpm4Task.setTaskdefname("采购询问供应商能否降价");
							}
						
							List<Jbpm4Task>  jbpm4Tasks=flowService.selectWeatherOrder(jbpm4Task);
							 		
							for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
								OrderApproval orderApproval=new OrderApproval();
								orderApproval.setId(jbpm4Task2.getRelationId());
								orderApproval.setTaskId(jbpm4Task2.getId());
								orderApproval.setSupplierWeatherOrderElementId(record.getId());
								orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
								break;
							}
							messageVo.setMessage("Save success");
							messageVo.setFlag(true);
							continue;
						}
					}
					
					Cell p1 = row.getCell(start);
					 String p2=p1+"";
					 if(null==p1||p2.equals("")){
						 line++;
						 continue;
					 }else{
					
					
//					String partNumber = ""; 
//			            if (row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_STRING) {
//							partNumber = row.getCell(2).toString();
//						}else if (row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
//							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
//						    partNumber = dataFormatter.formatCellValue(row.getCell(2));
//						}
			            String supplierquoteNumber="";
			            	
						 if (row.getCell(start).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
							supplierquoteNumber = dataFormatter.formatCellValue(row.getCell(start));
						}else{
							supplierquoteNumber=row.getCell(start).getStringCellValue();
						}
			            
		          
		            String getsupplierquoteNumber=row.getCell(start).toString();
		            Cell priceCell=row.getCell(start+1);
		            double price=0.0;
		            if(null==priceCell){
		            	 char  [] stringArr = getsupplierquoteNumber.toCharArray();//拿Exeecl公式
		 	            List<String> code=new ArrayList<String>();
		 	            int number = 0;
		 	            for (int j = 0; j < stringArr.length; j++) {
		 	            	String c=String.valueOf(stringArr[j]);
		 	            	if(isInteger(c)){
		 	            		break;
		 	            	}else{
		 	            		code.add(c);
		 	            	}
		 				}
		 	            
		 	            String[] array = new String[] { "A", "B", "C", "D", "E", "F", "G", "H","I", "J", "K", "L", "M", "N", "O", "P", "Q",
		 	            	    "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		 	            		  for(int f = 0; f < code.size(); f++){
		 	            			  for (int j = 0; j < array.length; j++) {
		 	            			  if(code.get(f).equals(array[j])){
		 	            				  if(code.size()>1&&f==0&&code.get(0).equals("A")){
		 	            					  number=26;
		 	            					  break;
		 	            				  }else if(code.size()>1&&f==0&&code.get(0).equals("B")){
		 	            					  number=52;
		 	            					  break;
		 	            				  }else{
		 	            					  number+=j;
		 	            					  break;
		 	            				  }
		 	            			  }
		 	            		  }
		 						
		 					}
		 	            		 price=row.getCell(number-1).getNumericCellValue();
		            }else{
		             price=row.getCell(start+1).getNumericCellValue();
		            }
		            
		            Cell amountCell=row.getCell(start+2);
		            double amount=0.0;
		            if(null==amountCell){
		            	   amount=row.getCell(start-2).getNumericCellValue();
		            }else{
		            	  amount=row.getCell(start+2).getNumericCellValue();
		            }
		            String remark="";
		            if(null!=row.getCell(start+4)){
		            	remark= row.getCell(start+4).toString();
		            }
		            String leadTime="";
		            String ship="";
		            String destination="";
		            String cert="";
		            String cond="";
		            Integer shipId = null;
		            Integer destId = null;
		            Integer condId=null;
		            Integer certId=null;
		            String location="";
		            Double bankCost=null;
		            Double feeForExchangeBill=null;
		            Double otherFee=null;
			            if(null!= row.getCell(start+5)){
				            	if (row.getCell(start+5).getCellType()==HSSFCell.CELL_TYPE_STRING) {
				            		leadTime = row.getCell(start+5).toString();
								}else if (row.getCell(start+5).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
									HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
									leadTime = dataFormatter.formatCellValue(row.getCell(start+5));
								}
			            	}
			            if(null!= row.getCell(start+6)){
			            	List<SystemCode> destinationlist = systemCodeDao.findType("STORE_LOCATION");
			            	destination=row.getCell(start+6).toString();
					            for (SystemCode systemCode : destinationlist) {
									if(destination.equals(systemCode.getValue())){
										destId=systemCode.getId();
									}
								}
			            	}
			            if(null!= row.getCell(start+7)){
			            	List<SystemCode> shiplist = systemCodeDao.findType("LOGISTICS_WAY");
			            	ship=row.getCell(start+7).toString();
			            		for (SystemCode systemCode : shiplist) {
								if(ship.equals(systemCode.getValue())){
									shipId=systemCode.getId();
								}
							}
			            	}
			            if(null!= row.getCell(start+8)){
			            	List<SystemCode> condlist = systemCodeDao.findType("COND");
			            	cond=row.getCell(start+8).toString();
			            		for (SystemCode systemCode : condlist) {
								if(cond.equals(systemCode.getCode())){
									condId=systemCode.getId();
								}
							}
			            	}
			            if(null!= row.getCell(start+9)){
			            	List<SystemCode> certlist = systemCodeDao.findType("CERT");
			            	cert=row.getCell(start+9).toString();
			            		for (SystemCode systemCode : certlist) {
								if(cert.equals(systemCode.getCode())){
									certId=systemCode.getId();
								}
							}
			            	}
			           
			            if(null!= row.getCell(start+10)){
			            	location=row.getCell(start+10).toString();
			            	}
			            if(null!= row.getCell(start+11)){
			            	if (!"".equals(row.getCell(start+11).toString())) {
			            		bankCost=new Double(row.getCell(start+11).toString());
							}
			            }
			            if(null!= row.getCell(start+12)){
			            	if (!"".equals(row.getCell(start+12).toString())) {
			            		feeForExchangeBill=new Double(row.getCell(start+12).toString());
							}
			            }
			            if(null!= row.getCell(start+13)){
			            	if (!"".equals(row.getCell(start+13).toString())) {
			            		otherFee=new Double(row.getCell(start+13).toString());
							}
			            }
			            SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement(); 
			            SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
			          
			            if(supplierquoteNumber.indexOf("/")>-1){
			            	 String[] numberorid=supplierquoteNumber.split("/");
					            supplierOrderElement.setSupplierQuoteElementId(Integer.parseInt(numberorid[1]));
					            supplierQuoteElement.setQuoteNumber(numberorid[0]);
			            }else{
			        		RoleVo role=userDao.getPower(Integer.parseInt(userVo.getUserId()));
			            	if(supplierquoteNumber.trim().length()<4){
			            		if(role.getRoleName().equals("国内采购")){
				            		for (int j = supplierquoteNumber.trim().length(); j < 4; j++) {
				            			supplierquoteNumber="0"+supplierquoteNumber;
									}
			            		}
			            	}
			            	
			            	Supplier supplier=supplierDao.findByCode(supplierquoteNumber);
			            	if(null!=supplier){
			            		ClientOrderVo clientOrderVo=clientWeatherOrderDao.findById(Integer.parseInt(clientOrderId));
			            		com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(clientOrderElementId);
			            		SupplierInquiryElement supplierInquiryElement=new SupplierInquiryElement();
			            		SupplierInquiry supplierinquiry=new SupplierInquiry();
			            		boolean in  =false;
			            		boolean in2  =false;
			            			Integer id=clientOrderVo.getClientInquiryId();
			            			ClientInquiry clientInquiry=clientInquiryDao.selectByPrimaryKey(id);
			            			supplierinquiry.setClientInquiryId(id);
			            			supplierinquiry.setSupplierId(supplier.getId());
			            			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			            		        String dateNowStr = sdf.format(new Date());  
			            		        try {
											Date today = sdf.parse(dateNowStr);
											supplierinquiry.setInquiryDate(today);
										} catch (ParseException e) {
											e.printStackTrace();
										} 
			            			List<SupplierInquiry> supplierInquiries=supplierInquiryDao.findSupplierInquiry(supplierinquiry);
			            			
			            			if(supplierInquiries.size()==0){
			            			String quoteNumber = supplierInquiryService.getQuoteNumberSeq(clientInquiry.getInquiryDate(),supplier.getId());
			            			supplierinquiry.setQuoteNumber(quoteNumber);
			            			supplierinquiry.setInquiryDate(new Date());
			            			supplierinquiry.setDeadline(clientInquiry.getDeadline());
			            			supplierinquiry.setRemark("");
			            			supplierInquiryDao.insertSelective(supplierinquiry);
			            			supplierInquiryElement.setSupplierInquiryId(supplierinquiry.getId());
			            			in=true;
			            			}else{
			            				for (SupplierInquiry supplierInquiry2 : supplierInquiries) {
				            				SupplierInquiryElement record=new SupplierInquiryElement();
				            				record.setClientInquiryElementId(clientOrderElementVo.getClientInquiryElementId());
				            				record.setSupplierInquiryId(supplierInquiry2.getId());
				            				SupplierInquiryElement element=supplierInquiryElementDao.findSupplierInquiryElement(record);
				            				if(null==element){
				            					supplierInquiryElement.setSupplierInquiryId(supplierInquiry2.getId());
				            					in=true;
				            					break;
				            				}
			            				}
			            			}
			            			
			            			if(!in){
			            				String quoteNumber = supplierInquiryService.getQuoteNumberSeq(clientInquiry.getInquiryDate(),supplier.getId());
				            			supplierinquiry.setQuoteNumber(quoteNumber);
				            			supplierinquiry.setInquiryDate(new Date());
				            			supplierinquiry.setDeadline(clientInquiry.getDeadline());
				            			supplierinquiry.setRemark("");
				            			supplierInquiryDao.insertSelective(supplierinquiry);
				            			supplierInquiryElement.setSupplierInquiryId(supplierinquiry.getId());
			            			}
			            		
			            		Integer supplierInquiryId = supplierinquiry.getId();
			            		supplierInquiryElement.setClientInquiryElementId(clientOrderElementVo.getClientInquiryElementId());
			            		supplierInquiryElementDao.insertSelective(supplierInquiryElement);
			            		
			            		Integer supplierInquiryElementId =supplierInquiryElement.getId();
			            		Supplier supplier2=supplierDao.selectByPrimaryKey(supplier.getId());
			            		ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
			            		SupplierQuote supplierQuote=new SupplierQuote();
			            		supplierQuote.setSupplierInquiryId(supplierInquiryElement.getSupplierInquiryId());
		            		        try {
										Date today = sdf.parse(dateNowStr);
										supplierQuote.setQuoteDate(today);
									} catch (ParseException e) {
										e.printStackTrace();
									} 
			            		
		            		        List<SupplierQuote> quotes=supplierQuoteDao.findSupplierquote(supplierQuote);
		            		    	SupplierQuoteElement quoteElement=new SupplierQuoteElement();
		            		        if(quotes.size()==0){
			            			supplierQuote.setCurrencyId(supplier2.getCurrencyId());
			            			supplierQuote.setExchangeRate(exchangeRate.getRate());
			            			supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
			            			GregorianCalendar gc=new GregorianCalendar(); 
			            			gc.setTime(Calendar.getInstance().getTime()); 
			            			gc.add(2,1); 
			            			gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
			            			Date validity=gc.getTime();
			            			supplierQuote.setValidity(validity);
			            			SupplierInquiry inquiry=supplierInquiryDao.selectByPrimaryKey(supplierInquiryElement.getSupplierInquiryId());
			            			supplierQuote.setQuoteNumber(inquiry.getQuoteNumber());
			            			supplierQuoteDao.insertSelective(supplierQuote);
			            			quoteElement.setSupplierQuoteId(supplierQuote.getId());
			            			in2=true;
		            		        }else{
		            		        	for (SupplierQuote supplierQuote2 : quotes) {
		            		        		SupplierQuoteElement record=new SupplierQuoteElement();
		            		        		record.setSupplierInquiryElementId(supplierInquiryElement.getId());
		            		        		record.setSupplierQuoteId(supplierQuote.getId());
		            		        		SupplierQuoteElement element=supplierQuoteElementDao.findSupplierQuoteElement(record);
		            		        		if(null==element){
		            		        			quoteElement.setSupplierQuoteId(supplierQuote2.getId());
				            					in2=true;
				            					break;
				            				}
										}
		            		        }
		            		        if(!in2){
		            		        	supplierQuote.setCurrencyId(supplier2.getCurrencyId());
				            			supplierQuote.setExchangeRate(exchangeRate.getRate());
				            			supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
				            			GregorianCalendar gc=new GregorianCalendar(); 
				            			gc.setTime(Calendar.getInstance().getTime()); 
				            			gc.add(2,1); 
				            			gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				            			Date validity=gc.getTime();
				            			supplierQuote.setValidity(validity);
				            			SupplierInquiry inquiry=supplierInquiryDao.selectByPrimaryKey(supplierInquiryElement.getSupplierInquiryId());
				            			supplierQuote.setQuoteNumber(inquiry.getQuoteNumber());
				            			supplierQuoteDao.insertSelective(supplierQuote);
				            			quoteElement.setSupplierQuoteId(supplierQuote.getId());
		            		        }
		            		        
			            		Integer supplierQuoteId =supplierQuote.getId();
			            		
			            	
			            		quoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
			            		quoteElement.setElementId(clientOrderElementVo.getQuoteElementId());
			            		quoteElement.setPartNumber(clientOrderElementVo.getQuotePartNumber());
			            		quoteElement.setDescription(clientOrderElementVo.getQuoteDescription());
			            		quoteElement.setAmount(clientOrderElementVo.getSupplierQuoteAmount());
			            		quoteElement.setUnit(clientOrderElementVo.getQuoteUnit());
			            		quoteElement.setPrice(price);
			            		quoteElement.setLeadTime("");
			            		quoteElement.setRemark(clientOrderElementVo.getQuoteRemark());
			            		if(null!=condId&&!"".equals(cond)){
			            			quoteElement.setConditionId(condId);
			            		}else{
			            			quoteElement.setConditionId(clientOrderElementVo.getConditionId());
			            		}
			            		if(null!=certId&&!"".equals(cert)){
			            			quoteElement.setCertificationId(certId);
			            		}else{
			            			quoteElement.setCertificationId(clientOrderElementVo.getCertificationId());
			            		}
			            		
			            		
			            		quoteElement.setLocation(location);
			            		quoteElement.setSupplierQuoteStatusId(70);
			            		supplierQuoteElementDao.insert(quoteElement);
			            		clientInquiryElementDao.updateByPartNumber(quoteElement.getPartNumber());
			            		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
								ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
								if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
									if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
										if (clientQuoteElement != null) {
											clientInquiryElement.setElementStatusId(713);
										}
									}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701){
										clientInquiryElement.setElementStatusId(702);
									}
									clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
									if (clientInquiryElement.getMainId() !=null && !"".equals(clientInquiryElement.getMainId())) {
										 ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElement.getMainId());
										 if (mainElement.getElementStatusId() != null) {
											 if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
								    			 mainElement.setElementStatusId(702);
								        		 clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
											 }
										 }
							    	 }else {
							    		 List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(clientInquiryElement.getId());
							    		 for (int j = 0; j < alterList.size(); j++) {
							    			 if(alterList.get(j).getElementStatusId() != null){
							    				 if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
								    				 alterList.get(j).setElementStatusId(702);
								        			 clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
												 }
							    			 }
							    		 }
							    	 }
								}
			            		supplierOrderElement.setSupplierQuoteElementId(quoteElement.getId());
			            		supplierQuoteElement.setQuoteNumber(supplierinquiry.getQuoteNumber());
			            	}
			            }
			            SupplierWeatherOrderElement record=new SupplierWeatherOrderElement();
			            record.setBankCost(bankCost);
			            record.setFeeForExchangeBill(feeForExchangeBill);
			            record.setOtherFee(otherFee);
			            record.setSupplierStatus(1);
			            record.setClientOrderElementId(clientOrderElementId);
			            record.setSupplierQuoteElementId(supplierOrderElement.getSupplierQuoteElementId());
			            record.setAmount(amount);
			            record.setPrice(price);
			            if(null==leadTime||leadTime.equals("")){
			            	leadTime="90";
			            }
			            record.setLeadTime(leadTime);
			            	if(null!=shipId){
			            record.setShipWayId(shipId);
							}
							if(null!=destId){
						record.setDestination(destId.toString());
							}
							 Pattern pattern = Pattern.compile("[0-9]*"); 
							   Matcher isNum = pattern.matcher(leadTime);
							if(null!=leadTime&&!"".equals(leadTime)&&isNum.matches()){
								GregorianCalendar gc=new GregorianCalendar(); 
								gc.setTime(Calendar.getInstance().getTime()); 
								gc.add(5,Integer.parseInt(leadTime));
		            			
		            			gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
		            			record.setDeadline(gc.getTime());
							}
							record.setRemark(remark);
							if(coeID.indexOf(clientOrderElementId.toString())==-1){
								Jbpm4Task jbpm4Task =new Jbpm4Task();
								jbpm4Task.setYwTableElementId(record.getClientOrderElementId());
								if(taskdefname.equals("add")){
									jbpm4Task.setTaskdefname("采购生成供应商预订单");
								}else if(taskdefname.equals("update")){
									jbpm4Task.setTaskdefname("采购询问供应商能否降价");
								}
								
								List<Jbpm4Task>  jbpm4Tasks=jbpm4TaskDao.selectByTaskName(jbpm4Task);
								for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
									OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(jbpm4Task2.getRelationId());
									orderApproval.setTaskId(null);
									if(null!=orderApproval.getSupplierWeatherOrderElementId()){
										supplierWeatherOrderElementDao.deleteByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
									}
									orderApproval.setSupplierWeatherOrderElementId(null);
									orderApprovalDao.updateByPrimaryKey(orderApproval);
								}
								coeID+=","+clientOrderElementId.toString();
							}
							
//						SupplierWeatherOrderElement element=supplierWeatherOrderElementDao.selectByCoeIdAndSqeId(clientOrderElementId, supplierOrderElement.getSupplierQuoteElementId());
//						if(null!=element){
//							record.setId(element.getId());
//							supplierWeatherOrderElementDao.updateByPrimaryKeySelective(record);
//						}else{
						supplierWeatherOrderElementDao.insert(record);
						Jbpm4Task jbpm4Task =new Jbpm4Task();
						jbpm4Task.setYwTableElementId(record.getClientOrderElementId());
						if(taskdefname.equals("add")){
							jbpm4Task.setTaskdefname("采购生成供应商预订单");
						}else if(taskdefname.equals("update")){
							jbpm4Task.setTaskdefname("采购询问供应商能否降价");
						}
					
						List<Jbpm4Task>  jbpm4Tasks=flowService.selectWeatherOrder(jbpm4Task);
						 		
						for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
							OrderApproval orderApproval=new OrderApproval();
							orderApproval.setId(jbpm4Task2.getRelationId());
							orderApproval.setTaskId(jbpm4Task2.getId());
							orderApproval.setSupplierWeatherOrderElementId(record.getId());
							orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
							break;
						}
						
						if(jbpm4Tasks.size()<1){
							ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementDao.selectByPrimaryKey(record.getClientOrderElementId());
							OrderApproval orderApproval=new OrderApproval();
							orderApproval.setClientOrderElementId(clientWeatherOrderElement.getId());
							Jbpm4Task task=	flowService.selectDbversion(jbpm4Task);
							if(task.getDescr().indexOf("不使用")>-1){
								orderApproval.setHandle("有库存");
							}else{
								orderApproval.setHandle("无库存");
							}
							Jbpm4Jbyj jbpm4Jbyj=JbyjDao.findGyJbyjByBusinessKeyAndOutcome("ORDER_APPROVAL.ID."+task.getRelationId(), "发起");
							orderApproval.setDesc(jbpm4Jbyj.getUserName()+"发起【合同审批】等待您的处理!");
							orderApproval.setUserId(jbpm4Jbyj.getUserId());
							contractReviewService.orderApproval(clientWeatherOrderElement,  ContextHolder.getCurrentUser().getUserId(),orderApproval);
							
							if(orderApproval.getHandle().equals("有库存")){
								try {
									flowService.completeTask(orderApproval.getTaskId(), "不使用", "", "","", "", "","/market/clientweatherorder/purchaseConfirmProfit?clientOrderElementId="+orderApproval.getId(),orderApproval.getTaskId());
//									Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
//									flowService.completeTask(task2.getId(), "不使用",  ContextHolder.getCurrentUser().getUserId(), "","", "", "","",task2.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
							
							Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
							task2.setDescr(task.getDescr());
							flowService.updateByPrimaryKeySelective(task2);
							orderApproval.setSupplierWeatherOrderElementId(record.getId());
							orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
							
							List<Jbpm4Jbyj> jbpm4Jbyjs=flowService.selectByYwTableElementId(record.getClientOrderElementId().toString(),"ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
							for (int j = 0; j < jbpm4Jbyjs.size(); j++) {
									Jbpm4Jbyj gyJbyj=new Jbpm4Jbyj();
									gyJbyj.setProcessinstanceId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
									gyJbyj.setUserId(jbpm4Jbyjs.get(j).getUserId());
									gyJbyj.setUserName(jbpm4Jbyjs.get(j).getUserName());
									gyJbyj.setTaskName(jbpm4Jbyjs.get(j).getTaskName());
									flowService.updateBytaskName(gyJbyj);
								}
							}
						}
						messageVo.setMessage("Save success");
						messageVo.setFlag(true);
				}
				
			}
//			}
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
	
	@Override
	public MessageVo addSupplierWeatherOrder( SupplierWeatherOrderElement supplierWeatherOrderElement,SupplierQuoteElement supplierQuoteElement) {
		MessageVo messageVo=new MessageVo();
		boolean success = true;
		String message = "";
		SupplierWeatherOrderElement weatherOrderElement=new SupplierWeatherOrderElement();
		if(supplierWeatherOrderElement.getSupplierStatus().equals(1)||supplierWeatherOrderElement.getSupplierStatus()==1){
		String supplierCode=supplierWeatherOrderElement.getSupplierCode().toString();
		UserVo userVo=ContextHolder.getCurrentUser();
		RoleVo role=userDao.getPower(Integer.parseInt(userVo.getUserId()));
		if(supplierCode.trim().length()<4){
			if(role.getRoleName().equals("国内采购")){
				for (int j = supplierCode.trim().length(); j < 4; j++) {
	    			supplierCode="0"+supplierCode;
				}
			}
    	}
		
		Supplier supplier=supplierDao.findByCode(supplierCode);
		if(null!=supplier){
			ClientOrderVo clientOrderVo=clientWeatherOrderDao.findById(supplierWeatherOrderElement.getClientWeatherOrderId());
			com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementDao.findByclientOrderELementId(supplierWeatherOrderElement.getClientOrderElementId());
			SupplierInquiryElement supplierInquiryElement=new SupplierInquiryElement();
			SupplierInquiry supplierinquiry=new SupplierInquiry();
			boolean in  =false;
			boolean in2  =false;
			Integer id=clientOrderVo.getClientInquiryId();
			ClientInquiry clientInquiry=clientInquiryDao.selectByPrimaryKey(id);
			supplierinquiry.setClientInquiryId(id);
			supplierinquiry.setSupplierId(supplier.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			String dateNowStr = sdf.format(new Date());  
			try {
				Date today = sdf.parse(dateNowStr);
				supplierinquiry.setInquiryDate(today);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			List<SupplierInquiry> supplierInquiries=supplierInquiryDao.findSupplierInquiry(supplierinquiry);

			if(supplierInquiries.size()==0){
				String quoteNumber = supplierInquiryService.getQuoteNumberSeq(clientInquiry.getInquiryDate(),supplier.getId());
				supplierinquiry.setQuoteNumber(quoteNumber);
				supplierinquiry.setInquiryDate(new Date());
				supplierinquiry.setDeadline(clientInquiry.getDeadline());
				supplierinquiry.setRemark("");
				supplierInquiryDao.insertSelective(supplierinquiry);
				supplierInquiryElement.setSupplierInquiryId(supplierinquiry.getId());
				in=true;
			}else{
				for (SupplierInquiry supplierInquiry2 : supplierInquiries) {
					SupplierInquiryElement record=new SupplierInquiryElement();
					record.setClientInquiryElementId(clientOrderElementVo.getClientInquiryElementId());
					record.setSupplierInquiryId(supplierInquiry2.getId());
					SupplierInquiryElement element=supplierInquiryElementDao.findSupplierInquiryElement(record);
					if(null==element){
						supplierInquiryElement.setSupplierInquiryId(supplierInquiry2.getId());
						in=true;
						break;
					}
				}
			}

			if(!in){
				String quoteNumber = supplierInquiryService.getQuoteNumberSeq(clientInquiry.getInquiryDate(),supplier.getId());
				supplierinquiry.setQuoteNumber(quoteNumber);
				supplierinquiry.setInquiryDate(new Date());
				supplierinquiry.setDeadline(clientInquiry.getDeadline());
				supplierinquiry.setRemark("");
				supplierInquiryDao.insertSelective(supplierinquiry);
				supplierInquiryElement.setSupplierInquiryId(supplierinquiry.getId());
			}

			Integer supplierInquiryId = supplierinquiry.getId();
			supplierInquiryElement.setClientInquiryElementId(clientOrderElementVo.getClientInquiryElementId());
			supplierInquiryElementDao.insertSelective(supplierInquiryElement);

			Integer supplierInquiryElementId =supplierInquiryElement.getId();
			Supplier supplier2=supplierDao.selectByPrimaryKey(supplier.getId());
			ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
			SupplierQuote supplierQuote=new SupplierQuote();
			supplierQuote.setSupplierInquiryId(supplierInquiryElement.getSupplierInquiryId());
			try {
				Date today = sdf.parse(dateNowStr);
				supplierQuote.setQuoteDate(today);
			} catch (ParseException e) {
				e.printStackTrace();
			} 

			List<SupplierQuote> quotes=supplierQuoteDao.findSupplierquote(supplierQuote);
			SupplierQuoteElement quoteElement=new SupplierQuoteElement();
			if(quotes.size()==0){
				supplierQuote.setCurrencyId(supplier2.getCurrencyId());
				supplierQuote.setExchangeRate(exchangeRate.getRate());
				supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
				GregorianCalendar gc=new GregorianCalendar(); 
				gc.setTime(Calendar.getInstance().getTime()); 
				gc.add(2,1); 
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				Date validity=gc.getTime();
				supplierQuote.setValidity(validity);
				SupplierInquiry inquiry=supplierInquiryDao.selectByPrimaryKey(supplierInquiryElement.getSupplierInquiryId());
				supplierQuote.setQuoteNumber(inquiry.getQuoteNumber());
				supplierQuoteDao.insertSelective(supplierQuote);
				quoteElement.setSupplierQuoteId(supplierQuote.getId());
				in2=true;
			}else{
				for (SupplierQuote supplierQuote2 : quotes) {
					SupplierQuoteElement record=new SupplierQuoteElement();
					record.setSupplierInquiryElementId(supplierInquiryElement.getId());
					record.setSupplierQuoteId(supplierQuote.getId());
					SupplierQuoteElement element=supplierQuoteElementDao.findSupplierQuoteElement(record);
					if(null==element){
						quoteElement.setSupplierQuoteId(supplierQuote2.getId());
						in2=true;
						break;
					}
				}
			}
			if(!in2){
				supplierQuote.setCurrencyId(supplier2.getCurrencyId());
				supplierQuote.setExchangeRate(exchangeRate.getRate());
				supplierQuote.setQuoteDate(Calendar.getInstance().getTime());
				GregorianCalendar gc=new GregorianCalendar(); 
				gc.setTime(Calendar.getInstance().getTime()); 
				gc.add(2,1); 
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				Date validity=gc.getTime();
				supplierQuote.setValidity(validity);
				SupplierInquiry inquiry=supplierInquiryDao.selectByPrimaryKey(supplierInquiryElement.getSupplierInquiryId());
				supplierQuote.setQuoteNumber(inquiry.getQuoteNumber());
				supplierQuoteDao.insertSelective(supplierQuote);
				quoteElement.setSupplierQuoteId(supplierQuote.getId());
			}
	        SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
			quoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
			quoteElement.setElementId(clientOrderElementVo.getQuoteElementId());
			quoteElement.setPartNumber(clientOrderElementVo.getQuotePartNumber());
			quoteElement.setDescription(clientOrderElementVo.getQuoteDescription());
			quoteElement.setAmount(clientOrderElementVo.getSupplierQuoteAmount());
			quoteElement.setUnit(clientOrderElementVo.getQuoteUnit());
			quoteElement.setPrice(supplierWeatherOrderElement.getPrice());
			quoteElement.setLeadTime("");
			quoteElement.setRemark(clientOrderElementVo.getQuoteRemark());
			if(null!=supplierQuoteElement){
				if(null!=supplierQuoteElement.getConditionId()&&!"".equals(supplierQuoteElement.getConditionId())){
					quoteElement.setConditionId(supplierQuoteElement.getConditionId());
				}else{
					quoteElement.setConditionId(clientOrderElementVo.getConditionId());
				}
				if(null!=supplierQuoteElement.getCertificationId()&&!"".equals(supplierQuoteElement.getCertificationId())){
					quoteElement.setCertificationId(supplierQuoteElement.getCertificationId());
				}else{
					quoteElement.setCertificationId(clientOrderElementVo.getCertificationId());
				}
				quoteElement.setLocation(supplierQuoteElement.getLocation());
			}else{
				quoteElement.setConditionId(clientOrderElementVo.getConditionId());
				quoteElement.setCertificationId(clientOrderElementVo.getCertificationId());
			}
			quoteElement.setSupplierQuoteStatusId(70);
		
			supplierQuoteElementDao.insert(quoteElement);
			clientInquiryElementDao.updateByPartNumber(quoteElement.getPartNumber());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
			if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
				if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
					if (clientQuoteElement != null) {
						clientInquiryElement.setElementStatusId(713);
					}
				}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701){
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


			
			weatherOrderElement.setClientOrderElementId(supplierWeatherOrderElement.getClientOrderElementId());
			weatherOrderElement.setSupplierQuoteElementId(supplierOrderElement.getSupplierQuoteElementId());
			weatherOrderElement.setAmount(supplierWeatherOrderElement.getAmount());
			weatherOrderElement.setPrice(supplierWeatherOrderElement.getPrice());
			weatherOrderElement.setDestination(supplierWeatherOrderElement.getDestination());
			weatherOrderElement.setShipWayId(supplierWeatherOrderElement.getShipWayId());
			Pattern pattern = Pattern.compile("[0-9]*"); 
		
			if(null!=supplierWeatherOrderElement.getLeadTime()&&!"".equals(supplierWeatherOrderElement.getLeadTime())){
				weatherOrderElement.setLeadTime(supplierWeatherOrderElement.getLeadTime());
				Matcher isNum = pattern.matcher(supplierWeatherOrderElement.getLeadTime());
				if(isNum.matches()){
					GregorianCalendar gc=new GregorianCalendar(); 
					gc.setTime(Calendar.getInstance().getTime()); 
					gc.add(5,Integer.parseInt(supplierWeatherOrderElement.getLeadTime()));

					gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
					weatherOrderElement.setDeadline(gc.getTime());
				}
			}
			weatherOrderElement.setRemark(supplierWeatherOrderElement.getRemark());;
			weatherOrderElement.setSupplierStatus(1);
			}
		}else if(supplierWeatherOrderElement.getSupplierStatus().equals(0)||supplierWeatherOrderElement.getSupplierStatus()==0){
			weatherOrderElement.setSupplierStatus(0);
			weatherOrderElement.setAmount(0.0);
			weatherOrderElement.setPrice(0.0);
			weatherOrderElement.setRemark(supplierWeatherOrderElement.getRemark());;
			weatherOrderElement.setClientOrderElementId(supplierWeatherOrderElement.getClientOrderElementId());
		}
		if (supplierWeatherOrderElement.getBankCost() != null && !"".equals(supplierWeatherOrderElement.getBankCost())) {
			weatherOrderElement.setBankCost(supplierWeatherOrderElement.getBankCost());
		}
		if (supplierWeatherOrderElement.getFeeForExchangeBill() != null && !"".equals(supplierWeatherOrderElement.getFeeForExchangeBill())) {
			weatherOrderElement.setFeeForExchangeBill(supplierWeatherOrderElement.getFeeForExchangeBill());
		}
		if (supplierWeatherOrderElement.getOtherFee() != null && !"".equals(supplierWeatherOrderElement.getOtherFee())) {
			weatherOrderElement.setOtherFee(supplierWeatherOrderElement.getOtherFee());
		}
			supplierWeatherOrderElementDao.insert(weatherOrderElement);

//			Jbpm4Task jbpm4Task =new Jbpm4Task();
//			jbpm4Task.setYwTableElementId(weatherOrderElement.getClientOrderElementId());
//			jbpm4Task.setTaskdefname("采购生成供应商预订单");
//			List<Jbpm4Task>  jbpm4Tasks=flowService.selectWeatherOrder(jbpm4Task);
//			 		
//			for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
				OrderApproval orderApproval=new OrderApproval();
				orderApproval.setId(supplierWeatherOrderElement.getId());
				orderApproval.setTaskId(supplierWeatherOrderElement.getTaskId());
				orderApproval.setSupplierWeatherOrderElementId(weatherOrderElement.getId());
				orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
//				break;
//			}
			
			messageVo.setMessage("Save success");
			messageVo.setFlag(true);

		return messageVo;
	}
	
	 public static boolean isInteger(String str) {    
		    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    //是否是数字
		    return pattern.matcher(str).matches();    
		  }  

	@Override
	public MessageVo updateuploadExcel(MultipartFile multipartFile, String clientOrderId) {
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
				
	            String numebr = row.getCell(j).toString();
	            if(numebr.equals("修改确定供应商")){
	            	start=j;
	            	break;
	            } 
			}
		    int frist=sheet.getFirstRowNum()+1;
			for (int i = frist; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i+1);
				String orderNumber=row.getCell(0).toString();
				Double a=new Double(row.getCell(1).toString());
				 Integer item = a.intValue();
				 Integer clientOrderElementId=clientOrderElementDao.findByOrderNumberAndItem(orderNumber, item.toString());
				
				if(null!=row.getCell(start-1)){
					String status=row.getCell(start-1).toString();
					if(status.equals("取消")){
						SupplierWeatherOrderElement record=new SupplierWeatherOrderElement();
						String weatherOrderNumber=row.getCell(start-3).getStringCellValue();
						String[] weatherOrderNumbers=weatherOrderNumber.split("/");
					    String weatherOrderQuoteElementId=weatherOrderNumbers[1];
						record.setClientOrderElementId(clientOrderElementId);
						record.setAmount(0.0);
						record.setPrice(0.0);
						record.setSupplierStatus(0);
						String remark="";
				          if(null!=row.getCell(start+4)){
				            	remark= row.getCell(start+4).toString();
				           }
				        record.setRemark(remark);
				    	SupplierWeatherOrderElement element=supplierWeatherOrderElementDao.selectByCoeIdAndSqeId(clientOrderElementId, Integer.parseInt(weatherOrderQuoteElementId));
						record.setId(element.getId());
						supplierWeatherOrderElementDao.updateByPrimaryKeySelective(record);
					}
					continue;
				}
				
				if(null==row){
					 continue;
				}
				 Cell p1 = row.getCell(start+1);
				 String p2=p1+"";
				 if(null==p1||p2.equals("")){
					 line++;
					 continue;
				 }else{
				
				
				 String partNumber = ""; 
		            if (row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_STRING) {
						partNumber = row.getCell(2).toString();
					}else if (row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					    partNumber = dataFormatter.formatCellValue(row.getCell(2));
					}
		        String weatherOrderNumber=row.getCell(start-3).getStringCellValue();
	            String supplierquoteNumber=row.getCell(start).getStringCellValue();
	            double price=row.getCell(start+1).getNumericCellValue();
	            double amount=row.getCell(start+2).getNumericCellValue();
	            String leadTime="";
	            String ship="";
	            String destination="";
	            Integer shipId = null;
	            Integer destId = null;
		            if(null!= row.getCell(start+3)){
			            	if (row.getCell(start+3).getCellType()==HSSFCell.CELL_TYPE_STRING) {
			            		leadTime = row.getCell(start+3).toString();
							}else if (row.getCell(start+3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
								leadTime = dataFormatter.formatCellValue(row.getCell(start+3));
							}
		            	}
		            if(null!= row.getCell(start+4)){
		            	List<SystemCode> destinationlist = systemCodeDao.findType("STORE_LOCATION");
		            	destination=row.getCell(start+4).toString();
				            for (SystemCode systemCode : destinationlist) {
								if(destination.equals(systemCode.getValue())){
									destId=systemCode.getId();
								}
							}
		            	}
		            if(null!= row.getCell(start+5)){
		            	List<SystemCode> shiplist = systemCodeDao.findType("LOGISTICS_WAY");
		            	ship=row.getCell(start+5).toString();
		            		for (SystemCode systemCode : shiplist) {
							if(ship.equals(systemCode.getValue())){
								shipId=systemCode.getId();
							}
						}
		            	}
		            SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement(); 
		            SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
		           
		            
		            String[] weatherOrderNumbers=weatherOrderNumber.split("/");
		            String weatherOrderQuoteElementId=weatherOrderNumbers[1];
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
		            		ClientQuote clientQuote=new ClientQuote();
		            		clientQuote.setIds(clientOrderElementId.toString());
//		            		List<ClientOrderElementVo>  orderdatalist=supplierOrderElementDao.ClientOrderData(clientQuote);
//		            		Integer currencyId = orderdatalist.get(0).getCurrencyId();
//		            		Double exchangeRate =orderdatalist.get(0).getExchangeRate();
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
		        			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
		        			if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
		        				if (clientInquiryElement.getElementStatusId().equals(703) || clientInquiryElement.getElementStatusId()==703) {
		        					if (clientQuoteElement != null) {
		        						clientInquiryElement.setElementStatusId(713);
		        					}
		        				}else if (clientInquiryElement.getElementStatusId().equals(701) || clientInquiryElement.getElementStatusId()==701){
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
		            SupplierWeatherOrderElement record=new SupplierWeatherOrderElement();
		            record.setClientOrderElementId(clientOrderElementId);
		            record.setSupplierQuoteElementId(supplierOrderElement.getSupplierQuoteElementId());
		            record.setAmount(amount);
		            record.setPrice(price);
		            record.setLeadTime(leadTime);
		            	if(null!=shipId){
		            record.setShipWayId(shipId);
						}
						if(null!=destId){
					record.setDestination(destId.toString());
						}
						 Pattern pattern = Pattern.compile("[0-9]*"); 
						   Matcher isNum = pattern.matcher(leadTime);
						if(null!=leadTime&&!"".equals(leadTime)&&isNum.matches()){
							GregorianCalendar gc=new GregorianCalendar(); 
							gc.setTime(Calendar.getInstance().getTime()); 
							gc.add(5,Integer.parseInt(leadTime));
	            			
	            			gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
	            			record.setDeadline(gc.getTime());
						}
						SupplierWeatherOrderElement element=supplierWeatherOrderElementDao.selectByCoeIdAndSqeId(clientOrderElementId, Integer.parseInt(weatherOrderQuoteElementId));
						record.setId(element.getId());
						record.setSupplierStatus(1);
						supplierWeatherOrderElementDao.updateByPrimaryKeySelective(record);
					
					messageVo.setMessage("Save success");
					messageVo.setFlag(true);
				 }
			}
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
	
	@Override
	public List<SupplierWeatherOrderElement> selectByClientOrderElementId(Integer id) {
		return supplierWeatherOrderElementDao.selectByClientOrderElementId(id);
	}

	@Override
	public ClientOrderElementVo findById(Integer id) {
		return supplierWeatherOrderElementDao.findById(id);
	}
	
	public List<SupplierWeatherOrder> getFeeInfo(PageModel<SupplierWeatherOrder> page){
		return supplierWeatherOrderDao.getFeeInfo(page);
	}
	
	public boolean saveFee(List<SupplierWeatherOrder> list){
		try {
			for (SupplierWeatherOrder supplierWeatherOrder : list) {
				SupplierWeatherOrder supplierWeatherOrder2 = supplierWeatherOrderDao.getByOrderIdAndSupplier(supplierWeatherOrder.getClientWeatherOrderId(), supplierWeatherOrder.getSupplierId());
				if (supplierWeatherOrder2 != null) {
					supplierWeatherOrder.setId(supplierWeatherOrder2.getId());
					supplierWeatherOrderDao.updateByPrimaryKey(supplierWeatherOrder);
				}else {
					supplierWeatherOrderDao.insertSelective(supplierWeatherOrder);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public SupplierWeatherOrder getByOrderIdAndSupplier(Integer clientWeatherOrderId,Integer supplierId){
		return supplierWeatherOrderDao.getByOrderIdAndSupplier(clientWeatherOrderId, supplierId);
	}
	
	public Double getAmontBySupplier(Integer clientWeatherOrderId,Integer supplierId){
		return supplierWeatherOrderElementDao.getAmontBySupplier(clientWeatherOrderId, supplierId);
	}

	public Double getAmountByClientOrder(Integer clientWeatherOrderElementId){
		return supplierWeatherOrderElementDao.getAmountByClientOrder(clientWeatherOrderElementId);
	}
}
