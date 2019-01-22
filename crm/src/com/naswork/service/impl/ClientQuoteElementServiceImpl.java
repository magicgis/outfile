package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ClientQuoteElementUploadDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.model.OrderApproval;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientorder.orderReview;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.GyFjService;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;



@Service("clientQuoteElementService")
public class ClientQuoteElementServiceImpl implements ClientQuoteElementService {

	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	protected GyFjService gyFjService;
	@Resource
	private ClientQuoteElementUploadDao clientQuoteElementUploadDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ClientDao clientDao;
	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientQuoteDao clientQuoteDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	
	 /*
     * 根据询价明细ID查询报价明细ID
     */
    public ClientQuoteElement findCqe(Integer clientInquiryElementId,Integer clientQuoteId){
    	return clientQuoteElementDao.findCqe(clientInquiryElementId,clientQuoteId);
    }

    @Override
	public void insertSelective(ClientQuoteElement record) {
    	clientQuoteElementDao.insertSelective(record);
	}

	@Override
	public void insert(ClientQuoteElement record) {
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(record.getClientInquiryElementId());
		
		 clientQuoteElementDao.insert(record);
		 if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
				if (clientInquiryElement.getElementStatusId().equals(702) || clientInquiryElement.getElementStatusId()==702){
					 clientInquiryElement.setElementStatusId(703);
					 clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
				}
		 }
		 List<ClientInquiryElement> list = new ArrayList<ClientInquiryElement>();
		 if (clientInquiryElement.getMainId() != null && !"".equals(clientInquiryElement.getMainId())) {
			  list = clientInquiryElementDao.selectByMainIdAndId(clientInquiryElement.getMainId(), clientInquiryElement.getId());
		 }else {
			 list = clientInquiryElementDao.selectByMainIdAndId(clientInquiryElement.getId(), clientInquiryElement.getId());
		 }
		 for (int i = 0; i < list.size(); i++) {
			  if (!"".equals(list.get(i).getElementStatusId()) && list.get(i).getElementStatusId()!=null) {
					if (list.get(i).getElementStatusId().equals(702) || list.get(i).getElementStatusId()==702){
						list.get(i).setElementStatusId(703);
						 clientInquiryElementDao.updateByPrimaryKeySelective(list.get(i));
					}
			  }
			
		  }
	}

	@Override
	public void updateByPrimaryKey(ClientQuoteElement record) {
		clientQuoteElementDao.updateByPrimaryKey(record);
	}

	@Override
	public MessageVo uploadExcel(MultipartFile multipartFile, String clientinquiryquotenumber) {
		boolean success = false;
		String message = "";
		InputStream fileStream = null;
		List<ClientQuoteElementVo> cielist=clientQuoteElementDao.findclientquote(clientinquiryquotenumber);
		List<ClientQuoteElement> list=new ArrayList<ClientQuoteElement>();
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
			//记录错误行数
		    StringBuffer lines=new StringBuffer();
			int line=1;
			int iten=1;
			int k=0;
			List<Integer> errorLine = new ArrayList<Integer>();
			int start=0;
			int coloumNum=sheet.getRow(1).getPhysicalNumberOfCells();
			for (int j = 0; j < coloumNum; j++) {
				row = sheet.getRow(1);
				
	            String lowprice = row.getCell(j).toString();
	            if(lowprice.equals("最低")){
	            	start=j;
	            	break;
	            }
			}
			for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i+1);
				if (row != null  && row.getCell(start+10) != null && !"".equals(row.getCell(start+10).toString())) {
					 Cell p1 = row.getCell(start+9);
					 String p2=p1+"";
					 if(null==p1||p2.equals("")){
						 line++;
						 continue;
					 }else{
					
					boolean contains=false;
					String quoteNumber=row.getCell(0).toString();
					if(!quoteNumber.equals(cielist.get(0).getQuoteNumber())){
						 line++;
						continue;
					}
					Double a=new Double(row.getCell(1).toString());
					 Integer item = a.intValue();
					 String partNumber = ""; 
			            if (row.getCell(3).getCellType()==HSSFCell.CELL_TYPE_STRING) {
							partNumber = row.getCell(3).toString();
						}else if (row.getCell(3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    partNumber = dataFormatter.formatCellValue(row.getCell(3));
						}
					String amount=row.getCell(6).toString();
//		            String supplierCode = row.getCell(start+6).toString();
//		            Double b = new Double(row.getCell(start+7).toString());
//		            Integer supplierquoteNumber = b.intValue();
		            String getsupplierquoteNumber=row.getCell(start+9).toString();
		            
		            char  [] stringArr = getsupplierquoteNumber.toCharArray();//拿Exeecl公式
		            System.out.print(i+",/n");
		            if (i == 1987) {
						int ab = 0;
						ab = 1;
					}
		            List<String> code=new ArrayList<String>();
		            int number = 0;
		            
		            String colStr="";
		            for (int j = 0; j < stringArr.length; j++) {
		            	String c=String.valueOf(stringArr[j]);
		            	if(isInteger(c)){
		            		break;
		            	}else{
		            		colStr+=c;
		            	}
					}
		           
		            int colIndex = excelColStrToNum(colStr, colStr.length());
		            if (colIndex == 1951) {
		            	int ab = 0;
						ab = 1;
					}
		            number=colIndex;
		            
//		            
//		            String[] array = new String[] { "A", "B", "C", "D", "E", "F", "G", "H","I", "J", "K", "L", "M", "N", "O", "P", "Q",
//		            	    "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
//		            		  for(int f = 0; f < code.size(); f++){
//		            			  for (int j = 0; j < array.length; j++) {
//		            			  if(code.get(f).equals(array[j])){
//		            				  if(code.size()>1&&f==0&&code.get(0).equals("A")){
//		            					  number=26;
//		            					  break;
//		            				  }else if(code.size()>1&&f==0&&code.get(0).equals("B")){
//		            					  number=52;
//		            					  break;
//		            				  }else{
//		            					  number+=j;
//		            					  break;
//		            				  }
//		            			  }
//		            		  }
//							
//						}
//		            		  Double d = new Double(row.getCell(number+1).toString());
//		            		  Integer supplierquoteNumber = d.intValue();
		            		  String supplierquoteNumber = ""; 
		            		 
		            		  
		  		            if (row.getCell(number).getCellType()==HSSFCell.CELL_TYPE_STRING) {
		  		            	supplierquoteNumber = row.getCell(number).toString();
		  					}else if (row.getCell(number).getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
		  						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
		  						supplierquoteNumber = dataFormatter.formatCellValue(row.getCell(number));
		  					}
		            Double c=new Double(row.getCell(start+8).toString());
		            Integer leadTime = c.intValue();
		            Double price = row.getCell(start+10).getNumericCellValue();
		            String remark ="";
		            if(null!=row.getCell(start+13)){
		            	 remark = row.getCell(start+13).toString();
		            }
		            Double fixedCost=0.0;
		            if(null!=row.getCell(start+14)&&!"".equals(row.getCell(start+14)+"")){
		            	fixedCost=new Double(row.getCell(start+14).toString());
		            }
		            Double bankCharges=0.0;
		            if(null!=row.getCell(start+15)&&!"".equals(row.getCell(start+15)+"")){
		            	bankCharges=new Double(row.getCell(start+15).toString());
		            }
		            ClientQuoteElementVo sqe= clientQuoteElementDao.findsupplier(supplierquoteNumber+"", partNumber,item+"");
		        	ClientQuoteElement cqe=new ClientQuoteElement();
		        	ClientQuoteElementUpload clientQuoteElementUpload=new ClientQuoteElementUpload();
		        	clientQuoteElementUpload.setSn(item);
	 				clientQuoteElementUpload.setPartNumber(partNumber);
	 				clientQuoteElementUpload.setUserId(Integer.parseInt(userId));
		            for (ClientQuoteElementVo clientQuoteElementVo : cielist) {
		            	if(item.equals(clientQuoteElementVo.getItem())&&sqe!=null){
		        			iten++;
		        			List<ClientQuoteElementVo> sqelist= clientQuoteElementDao.findbyelementid(clientQuoteElementVo.getElementId());
		        			if(sqelist==null||sqelist.size()==0){
		        				clientQuoteElementUpload.setMessage("没有供应商报价");
		        				clientQuoteElementUploadDao.insert(clientQuoteElementUpload);
		        				errorLine.add(line);
		        				break;
		        			}else{
		        			for (ClientQuoteElementVo clientQuoteElementVo2 : sqelist) {
		        				if(clientQuoteElementVo2.getId().equals(sqe.getId())){
		        					contains=true;
		        					break;
		        				}
							}
		        			if(contains){
		        				 ClientQuoteElementVo cqelist=clientQuoteElementDao.findClientQuoteElement(clientQuoteElementVo.getClientQuoteId(), clientQuoteElementVo.getClientInquiryElementId());
		        				if(null==cqelist){
		        					cqe.setPrice(price);
		        	 				cqe.setAmount(Double.parseDouble(amount));
		        	 				cqe.setRemark(remark);
		        	 				cqe.setFixedCost(fixedCost);
		        	 				cqe.setBankCharges(bankCharges);
		        	 				cqe.setItem(item);
		        	 				cqe.setLeadTime(leadTime);
		 	        				cqe.setClientQuoteId(clientQuoteElementVo.getClientQuoteId());
		 	        				cqe.setClientInquiryElementId(clientQuoteElementVo.getClientInquiryElementId());
		 	        				Integer sqeId= sqe.getId();
		 	    		            cqe.setSupplierQuoteElementId(sqeId);
		// 	    		            list.add(cqe);
		 	    		            list.add(k, cqe);
		 	    		            k++;
		        				}
		        				else {
		        					clientQuoteElementUpload.setMessage("已新增报价");
			        				clientQuoteElementUploadDao.insert(clientQuoteElementUpload);
		        					 errorLine.add(line);
		        					 break;
		        				 	}
		        				}
		        				else{
		        					clientQuoteElementUpload.setMessage("没有此供应商报价");
			        				clientQuoteElementUploadDao.insert(clientQuoteElementUpload);
		        					 errorLine.add(line);
		        					 break;
		        				}
		        			}
		        		}else if (sqe == null) {
		        			clientQuoteElementUpload.setMessage("没有供应商报价");
	        				clientQuoteElementUploadDao.insert(clientQuoteElementUpload);
	        				errorLine.add(line);
	        				break;
						}
		        		
		        	}
		            if(iten==1){
		            	clientQuoteElementUpload.setMessage("序号错误或没有供应商报价");
	    				clientQuoteElementUploadDao.insert(clientQuoteElementUpload);
		            	 errorLine.add(line);
		            	 iten=1;
		            }
			           line++;
					}
				}
			}
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (errorLine.size()>0) {
//				lines.append("Line ");
//				for (Integer a : errorLine) {
//					lines.append(a).append(",");
//				}
//				lines.deleteCharAt(lines.length()-1);
//				lines.append("have mistake!");
//				String result = lines.toString();
//				messageVo.setFlag(success);
//				messageVo.setMessage(result);
				
				return messageVo;
			} 	
			if (errorLine.size()==0){
				excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),cielist.get(0).getClientQuoteId().toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());
				
			for (ClientQuoteElement record : list) {
				 clientQuoteElementDao.insert(record);
				 ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(record.getClientInquiryElementId());
				 clientInquiryElement.setElementStatusId(703);
				 clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
			}
			success=true;
			message="save successful!";
			messageVo.setFlag(success);
			messageVo.setMessage(message);
			return messageVo;
			}
		} catch (IOException e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
			}
			e.printStackTrace();
		}
		return null;
	}
	
	 public static boolean isInteger(String str) {    
		    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    //是否是数字
		    return pattern.matcher(str).matches();    
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
			
			return "ClinetQuoteupload"+"_"+this.fetchUserName()+"_"+format.format(now);
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
	    
	    /*
	     * 存档
	     */
	    public void excelBackup(POIExcelWorkBook wb,String FilePath,String FileName,String ywId,String ywTableName,String ywTablePkName) {
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
	    
	    
	    public String fetchYwTableName() {
			return "client_quote_element";
		}
	    
	    public String fetchYwTablePkName() {
			return "id";
		}
	    
	    public String fetchMappingKey() {
			return "ClientQuoteExcel";
		}

		@Override
		public ClientQuoteElement selectByPrimaryKey(Integer id) {
			return clientQuoteElementDao.selectByPrimaryKey(id);
		}

		@Override
		public Object inspectProfitMargin(String businessKey, String taskId, String outcome, String assignee,
				String comment) {
			Map<String, Object> variables = new HashMap<String, Object>();
//			String[] ids=businessKey.split("\\.");
//			List<orderReview> list=clientOrderDao.orderReviewData(Integer.parseInt(ids[ids.length-1]));
//			Client  client= clientDao.selectByPrimaryKey(list.get(0).getClientId());
//			for (orderReview orderReview : list) {
//				Double orderPrice = orderReview.getOrderPrice();
//				Double quotePrice = orderReview.getQuotePrice();
//				Double freight=orderReview.getFreight();
//				Double fixedCost = orderReview.getFixedCost();
//				Double profitMargin=(orderPrice*(1-fixedCost/100)-(quotePrice+freight))/orderPrice*(1-fixedCost/100);
//				 if(profitMargin<client.getProfitMargin()){
////					 OrderApproval quoteapproval=orderApprovalDao.selectByCoeIdAndState(orderReview.getClientQuoteElementId(),0,0);
////					 if(null!=quoteapproval){
////						 OrderApproval profitapproval=orderApprovalDao.selectByCoeIdAndState(orderReview.getClientQuoteElementId(),0,1);
////						 if(null!=profitapproval){
////							 orderApprovalDao.deleteByPrimaryKey(profitapproval.getId());
////						 }
//						 continue;
////					 }else{
//					 OrderApproval orderApproval=new OrderApproval();
//					 orderApproval.setClientOrderId(Integer.parseInt(ids[ids.length-1]));
//					 orderApproval.setClientOrderElementId(orderReview.getClientOrderElementId());
//					 orderApproval.setSupplierQuoteElementId(orderReview.getSupplierQuoteElementId());
//					 orderApproval.setClientQuoteElementId(orderReview.getClientQuoteElementId());
//					 orderApproval.setState(0);
//					 orderApproval.setType(1);
//					 orderApprovalDao.insert(orderApproval);
//					 }
//				}
////			}
////			List<OrderApproval> orderApproval=orderApprovalDao.selectByIdAndState(Integer.parseInt(ids[ids.length-1]),0);
////			if(orderApproval.size()>0){
////				variables.put("to", "利润低于标准");
//			}else{
//				ClientOrder record=new ClientOrder();
////				record.setId(Integer.parseInt(ids[ids.length-1]));
//				record.setSpzt(235);
//				clientOrderDao.updateByPrimaryKeySelective(record);
////				variables.put("to", "结束");
//			}
			return variables;
		}
	    
		public List<ClientQuoteElementVo> selectByElementId(String partCode){
			return clientQuoteElementDao.selectByElementId(partCode);
		}
		  public static int excelColStrToNum(String colStr, int length) {
		        int num = 0;
		        int result = 0;
		        for(int i = 0; i < length; i++) {
		            char ch = colStr.charAt(length - i - 1);
		            num = (int)(ch - 'A' + 1) ;
		            num *= Math.pow(26, i);
		            result += num;
		        }
		        return result;
		    }
		  
		/*public List<ClientQuoteElement> getByClientInquiryElementId(Integer clientInquiryElementId){
			return clientQuoteElementDao.getByClientInquiryElementId(clientInquiryElementId);
		}*/
		
		public boolean addInHistoryQuote(ClientQuoteElement clientQuoteElement){
			try {
				SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
				ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
				if (!clientInquiryElement.getPartNumber().trim().equals(supplierQuoteElement.getPartNumber().trim())) {
					clientInquiryElement.setPartNumber(supplierQuoteElement.getPartNumber().trim());
					clientInquiryElement.setDescription(supplierQuoteElement.getDescription());
					int maxIndex = clientInquiryElementDao.findMaxItem(clientInquiryElement.getClientInquiryId()) + 1;
					clientInquiryElement.setItem(maxIndex);
					clientInquiryElement.setMainId(clientInquiryElement.getId());
					clientInquiryElement.setId(null);
					clientInquiryElement.setAlterPartNumber(" ");
					clientInquiryElement.setElementStatusId(703);
					clientInquiryElement.setSource("销售");
					clientInquiryElement.setTypeCode(null);
					clientInquiryElement.setConditionId(null);
					clientInquiryElement.setIsMain(1);
					clientInquiryElement.setRemark(" ");
					clientInquiryElementDao.insertSelective(clientInquiryElement);
					clientQuoteElement.setClientInquiryElementId(clientInquiryElement.getId());
				}
				List<ClientQuote> quoteList = clientQuoteDao.getByClientInquiryId(clientQuoteElement.getClientInquiryId());
				if (quoteList.size() > 0) {
					boolean finish = false;
					for (int i = 0; i < quoteList.size(); i++) {
						List<ClientQuoteElement> elementList = clientQuoteElementDao.getByClientInquiryElementId(clientQuoteElement.getClientInquiryElementId(), quoteList.get(i).getId());
						if (elementList.size() == 0) {
							clientQuoteElement.setClientQuoteId(quoteList.get(i).getId());
							finish = true;
							break;
						}
					}
					if (!finish) {
						ResultVo mistake = addQuote(clientQuoteElement.getClientInquiryId(),clientQuoteElement.getProfitMargin());//,clientQuoteElement.getProfitMargin()
						if (!mistake.isSuccess()) {
							return false;
						}else {
							clientQuoteElement.setClientQuoteId(new Integer(mistake.getMessage()));
						}
					}
				}else {
					ResultVo mistake = addQuote(clientQuoteElement.getClientInquiryId(),clientQuoteElement.getProfitMargin());
					if (!mistake.isSuccess()) {
						return false;
					}
					clientQuoteElement.setClientQuoteId(new Integer( mistake.getMessage()));
				}
				if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
					if (clientInquiryElement.getElementStatusId().equals(702) || clientInquiryElement.getElementStatusId()==702){
						clientInquiryElement.setElementStatusId(703);
						clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
					}
				}
				List<ClientInquiryElement> list = new ArrayList<ClientInquiryElement>();
				if (clientInquiryElement.getMainId() != null && !"".equals(clientInquiryElement.getMainId())) {
					 list = clientInquiryElementDao.selectByMainIdAndId(clientInquiryElement.getMainId(), clientInquiryElement.getId());
				}else {
					list = clientInquiryElementDao.selectByMainIdAndId(clientInquiryElement.getId(), clientInquiryElement.getId());
				}
				for (int i = 0; i < list.size(); i++) {
					if (!"".equals(list.get(i).getElementStatusId()) && list.get(i).getElementStatusId()!=null) {
						if (list.get(i).getElementStatusId().equals(702) || list.get(i).getElementStatusId()==702){
							list.get(i).setElementStatusId(703);
							clientInquiryElementDao.updateByPrimaryKeySelective(list.get(i));
						}
					}
				}
				clientQuoteElementDao.insertSelective(clientQuoteElement);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		}
		
		public ResultVo addQuote(Integer clientInquiryId,Double profitMargin){
			try {
				ClientQuote clientQuote = clientQuoteDao.getMessageForAddQuote(clientInquiryId);
				clientQuote.setProfitMargin(clientQuoteService.caculateProfitMargin(new BigDecimal(profitMargin)).doubleValue());
				clientQuoteService.insertInHistoryQuote(clientQuote);
				return new ResultVo(true, clientQuote.getId().toString());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultVo(false, "");
			}
		}
		
		public Double getTotalById(Integer id){
			return clientQuoteElementDao.getTotalById(id);
		}
		
		public ClientQuoteElement selectByClientInquiryElementId(Integer clientInquiryElementId){
			return clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElementId);
		}
		
		public void insertInHis(ClientQuoteElement clientQuoteElement){
			SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			if (clientInquiryElement.getPartNumber().trim().equals(supplierQuoteElement.getPartNumber().trim())) {
				insert(clientQuoteElement);
			}else {
				clientInquiryElement.setPartNumber(supplierQuoteElement.getPartNumber().trim());
				clientInquiryElement.setDescription(supplierQuoteElement.getDescription());
				int maxIndex = clientInquiryElementDao.findMaxItem(clientInquiryElement.getClientInquiryId()) + 1;
				clientInquiryElement.setItem(maxIndex);
				clientInquiryElementDao.insertSelective(clientInquiryElement);
				clientQuoteElement.setClientInquiryElementId(clientInquiryElement.getId());
				insert(clientQuoteElement);
			}
		}
		

}
