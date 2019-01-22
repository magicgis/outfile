package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
//import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.ElseIf;
import com.naswork.common.constants.FileConstant;
import com.naswork.dao.AuthorityRelationDao;
import com.naswork.dao.ClientContactDao;
import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientInquiryAlterElementDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientInquiryElementUploadDao;
import com.naswork.dao.CompetitorDao;
import com.naswork.dao.CompetitorQuoteDao;
import com.naswork.dao.CompetitorQuoteElementDao;
import com.naswork.dao.ElementDao;
import com.naswork.dao.EmailMessageDao;
import com.naswork.dao.OnpostEmailDao;
import com.naswork.dao.SupplierAirRelationDao;
import com.naswork.dao.SupplierCommissionSaleDao;
import com.naswork.dao.SupplierContactDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInquiryElementUpload;
import com.naswork.model.Competitor;
import com.naswork.model.CompetitorQuote;
import com.naswork.model.CompetitorQuoteElement;
import com.naswork.model.Element;
import com.naswork.model.EmailMessage;
import com.naswork.model.OnpostEmail;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.CompetitorVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.EmailTemplateVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientinquiry.TenderVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.GyExcelService;
import com.naswork.service.GyFjService;
import com.naswork.service.SupplierAnnualOfferService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.utils.ExchangeMail;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("clientInquiryService")
public class ClientInquiryServiceImpl implements ClientInquiryService {
	@Resource
	private ClientInquiryDao clientInquiryDao; 
	@Resource
	private ClientContactDao clientContactDao;
	@Resource
	private ClientDao clientDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private ElementDao elementDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private CompetitorDao competitorDao;
	@Resource
	private CompetitorQuoteDao competitorQuoteDao;
	@Resource
	private CompetitorQuoteElementDao competitorQuoteElementDao;
	@Resource
	protected GyExcelService gyExcelService;
	@Resource
	private GyFjService gyFjService;
	@Resource
	private ClientInquiryAlterElementDao clientInquiryAlterElementDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientInquiryElementUploadDao clientInquiryElementUploadDao;
	@Resource
	private SupplierAirRelationDao supplierAirRelationDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private UserDao userDao;
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Resource
	private SupplierContactDao supplierContactDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private SupplierCommissionSaleDao supplierCommissionSaleDao;
	@Resource
	private SupplierAnnualOfferService supplierAnnualOfferService;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private EmailMessageDao emailMessageDao;
	@Resource
	private OnpostEmailDao onpostEmailDao;
	
	private String a = "Dear";
	private String b = "Tanoy";
	private String c = "a";
	private String d = "b";
	private String[] formate = {a,b,c,d};

	
	/*
     * 列表页面数据
     * 
     */
    public void listPage(PageModel<ClientInquiryVo> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientInquiryDao.listPage(page));
    }
    
    /*
     * 新增数据
     * 
     */
    public ClientInquiry add(ClientInquiry clientInquiry) {
		StringBuffer quoteNumber=new StringBuffer();
		
		//ClientContact clientContact=clientContactDao.selectByPrimaryKey(clientInquiry.getClientContactId());
		Client client=clientDao.selectByPrimaryKey(clientInquiry.getClientId());
		SystemCode systemCode=systemCodeDao.findById(clientInquiry.getAirTypeId());
		SystemCode systemCode2=systemCodeDao.findById(clientInquiry.getBizTypeId());
		int maxSep=clientInquiryDao.findMaxSeq();
		Integer nextSeq=new Integer(maxSep+1);
		clientInquiry.setQuoteNumberSeq(nextSeq);
		//拼装询价单号
		quoteNumber.append(client.getCode());
		quoteNumber.append(systemCode.getCode());
		quoteNumber.append(systemCode2.getCode());
		quoteNumber.append(getDateStr((Date)clientInquiry.getInquiryDate()));
		quoteNumber.append(StringUtils.leftPad(nextSeq.toString(), 2, '0'));
		
		clientInquiry.setQuoteNumber(quoteNumber.toString());
		if (clientInquiry.getInquiryStatusId() == null) {
			clientInquiry.setInquiryStatusId(30);
		}
		
		clientInquiryDao.insert(clientInquiry);
		return clientInquiry;
		//quoteNumber.append(clientInquiry.getsy)
	}
    
    /*
     * 时间拼接
     * 
     */
    public String getDateStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		StringBuffer buffer = new StringBuffer();
		buffer.append(getYear(calendar));
		buffer.append('-');
		buffer.append(getMonth(calendar));
		buffer.append(getDay(calendar));
		return buffer.toString();
	}
    
    private String getYear(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR) % 100;
		return StringUtils.leftPad(Integer.toString(year), 2, '0');
	}

	private String getMonth(Calendar calendar) {
		int month = calendar.get(Calendar.MONTH) + 1;
		return Integer.toHexString(month).toUpperCase();
	}

	private String getDay(Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return StringUtils.leftPad(Integer.toString(day), 2, '0');
	}
	
	/*
	 * 根据主键查询
	 */
	public ClientInquiry findById(Integer id) {
		return clientInquiryDao.selectByPrimaryKey(id);
	}
	
	/*
	 * 更新
	 */
	public int updateByPrimaryKeySelective(ClientInquiry record){
		return clientInquiryDao.updateByPrimaryKeySelective(record);
	}
	
	 /*
     * 明细列表页面数据
     * 
     */
    public void ElePage(PageModel<ElementVo> page,GridSort sort){
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(clientInquiryDao.ElePage(page));
    }
    
    /*
     * 明细信息
     */
    public List<ElementVo> elelmentList(PageModel<ElementVo> page) {
		return clientInquiryDao.ElePage(page);
	}
    
    /*
     * 手动增加信息
     */
    public List<ElementVo> tenderElelment(Integer id) {
		return clientInquiryDao.tenderEle(id);
	}
    
    /*
     * excel上传
     */
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id,Integer userId,UserVo userVo){
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
			List<ClientInquiryElement> list = new ArrayList<ClientInquiryElement>();
			List<ClientInquiryElement> elementList = new ArrayList<ClientInquiryElement>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			int errorLine = 2;
			Integer maxItem = clientInquiryElementDao.findMaxItem(id);
			ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(id);
			Integer rows;
			if (maxItem!=null&&!"".equals(maxItem)) {
				rows = clientInquiryElementDao.findMaxItem(id)+1;
			}else {
				rows = 1;
			}
			SystemCode biz = systemCodeDao.selectByPrimaryKey(clientInquiry.getBizTypeId());
			/*if (biz.getCode().equals("4") && sheet.getPhysicalNumberOfRows()>2) {
				return new MessageVo(false, "only one");
			}*/
			//错误行数集合
			StringBuffer lines=new StringBuffer();
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
	            	 	String aString = row.getCell(0).toString();
			            Double a = new Double(row.getCell(0).toString());
			            Integer item = a.intValue();
			            Integer csn = new Double(row.getCell(1).toString()).intValue();
			            Cell oneCell = row.getCell(2);
			            String partNumber = ""; 
			            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
							partNumber = oneCell.toString();
						}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    partNumber = dataFormatter.formatCellValue(oneCell);
						}
			            partNumber = partNumber.trim().replaceAll("\r|\t|\n", "").replaceAll("\\xc2|\\xa0", "").replaceAll(" ", "");
			            Cell twoCell = row.getCell(3);
			            String alterPartNumber = ""; 
			            if (twoCell != null) {
			            	if (twoCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				            	alterPartNumber = twoCell.toString().trim();
							}else if (twoCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								Integer b = new Double(twoCell.toString()).intValue();
								alterPartNumber = b.toString();
							}
						}
			            String description = row.getCell(4).toString();
			            String unit = row.getCell(5).toString();
			            Double amount = 1.0;
			            if (row.getCell(6) != null && !"".equals(row.getCell(6).toString())) {
			            	amount = new Double(getNumber(row.getCell(6).toString()));
						}
			            //Double amount = new Double(row.getCell(6).toString());
			            Integer typeNumber = null;
			            if (row.getCell(7) != null) {
			            	if (row.getCell(7).toString() != null && !"".equals(row.getCell(7).toString())) {
				            	typeNumber = new Double(row.getCell(7).toString()).intValue();
							}
						}
			            
			            /*SystemCode systemCode = systemCodeDao.findByAirCode(typeNumber);
			            List<TPart> partList = tPartDao.selectByPartNumberCode(getCodeFromPartNumber(partNumber));
			            for (int j = 0; j < partList.size(); j++) {
							partList.get(j).setPartType(systemCode.getId());
							tPartDao.updateByPrimaryKey(partList.get(j));
						}*/
			            Cell twoCell12 = row.getCell(8);
			            String remark = "";
			            if (row.getCell(8) != null) {
			            	remark = row.getCell(8).toString();
						}
			            
			            
			            //
			            List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(partNumber));
			            Element element2 = new Element();
			            ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
			            if (element.size()==0) {
			            	byte[] p = getCodeFromPartNumber(partNumber).getBytes();
			            	Byte[] pBytes = new Byte[p.length];
			            	for(int j=0;j<p.length;j++){
			            		pBytes[j] = Byte.valueOf(p[j]);
			            	}
			            	element2.setPartNumberCode(pBytes);
			            	element2.setUpdateTimestamp(new Date());
							elementDao.insert(element2);
							clientInquiryElement.setElementId(element2.getId());
						}else {
							Element element3 = elementDao.findIdByPn(getCodeFromPartNumber(partNumber)).get(0);
							clientInquiryElement.setElementId(element3.getId());
						}
			            String condition = "";
			            if (row.getCell(9) != null) {
			            	condition = row.getCell(9).toString();
			            	if (condition!=null && !"".equals(condition)) {
				            	List<SystemCode> systemCodes = systemCodeDao.getByCode(condition);
					            if (systemCodes.size() > 0) {
									clientInquiryElement.setConditionId(systemCodes.get(0).getId());
								}
							}
						}
			            //
						clientInquiryElement.setItem(item);
						clientInquiryElement.setCsn(csn);
			            clientInquiryElement.setPartNumber(partNumber);
			            clientInquiryElement.setAlterPartNumber(alterPartNumber);
			            clientInquiryElement.setDescription(description);
			            clientInquiryElement.setUnit(unit);
			            clientInquiryElement.setAmount(amount);
			            clientInquiryElement.setRemark(remark);
			            clientInquiryElement.setUpdateTimestamp(new Date());
			            clientInquiryElement.setClientInquiryId(id);
			            clientInquiryElement.setTypeCode(typeNumber);
						clientInquiryElement.setIsDelete(0);
			            
			            //存起读取的数据等待保存进数据库
			            elementList.add(clientInquiryElement);
			            //如果行数与item对不上保存行数
						if (!rows.equals(item)) {
							clientInquiryElement.setLine(errorLine);
							clientInquiryElement.setError("S/N不连续");
							clientInquiryElement.setUserId(userId);
							list.add(clientInquiryElement);
						}
						errorLine++;
						rows++;
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (list.size()>0) {
				lines.append("Line ");
				for (int i = 0; i < list.size(); i++) {
					clientInquiryElementUploadDao.insertSelective(list.get(i));
				}
				return new MessageVo(false, "新增失败！");
			}
			
			if (list.size()==0){
				//存档
				excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());
				//是否有另件号
				for (ClientInquiryElement clientInquiryElement2 : elementList) {
					String partNumberCode=getCodeFromPartNumber(clientInquiryElement2.getPartNumber());
					List<ClientInquiryElement> relationship = new ArrayList<ClientInquiryElement>();
					clientInquiryElement2.setElementStatusId(700);
					clientInquiryElement2.setShortPartNumber(partNumberCode);
					clientInquiryElementDao.insertSelective(clientInquiryElement2);
					for (int i = 0; i < elementList.size(); i++) {
						if (clientInquiryElement2.getCsn().equals(elementList.get(i).getCsn()) 
								&& !clientInquiryElement2.getPartNumber().equals(elementList.get(i).getPartNumber()) && elementList.get(i).getMainId()==null) {
							//&& !elementList.get(i).getPartNumber().equals(clientInquiryElement2.getPartNumber())
							elementList.get(i).setIsMain(1);
							elementList.get(i).setMainId(clientInquiryElement2.getId());
						}
					}
				}
				success=true;
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				/*StringBuffer businessKey = new StringBuffer();
				businessKey.append("supplier_air_relation.id.").append(id).append(".supplierAirRelationExcel");
				gyExcelService.generateExcel(businessKey.toString());*/
				Calendar rightNow=Calendar.getInstance();
				int day=rightNow.get(rightNow.DAY_OF_WEEK);
				int hour = rightNow.get(rightNow.HOUR_OF_DAY);
//				System.out.print("today:"+day+",");
//				System.out.print("time"+hour);
				/*if (day == 7 || day == 0 || (day == 6 && hour > 18)) {
					boolean ifSuccess1 = sendEmail(id,0,false);
					//boolean ifSuccess2 = supplierCommissionSale(id);
					if (!ifSuccess1) {
						messageVo.setMessage("save successful! email sended unsuccessful!");
					}
				}else {
					boolean email = sendEmail(id,1,);
					//boolean commission = clientInquiryService.supplierCommissionSale(clientInquiryId);
					if (!email) {
						messageVo.setMessage("save successful! email sended unsuccessful!");
					}
				}*/
				//boolean email = sendEmail(id,1,false);
				/*if (!email) {
					messageVo.setMessage("save successful! email sended unsuccessful!");
				}*/
				int count = supplierInquiryDao.getCountByCLientInuqiryId(id);
				if ((clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId() == 30) && count>0) {
					clientInquiry.setInquiryStatusId(31);
					clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
				}
				return messageVo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
				success=false;
				message="save unsuccessful!";
				return new MessageVo(success, message);
			}
			success=false;
			message="save unsuccessful!";
			return new MessageVo(success, message);
		}
		
		return new MessageVo(success, message);
    }
    
    /**
     * 生成询价单并发送邮件
     * @param id
     * @param userVo
     * @param elementList
     */
    public boolean sendEmail(Integer id,Integer specialCode,Boolean free){
    	ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(id);
		try {
			if (clientInquiry.getEmailStatus() != 1) {
	    		boolean success = true;
	    		PageModel<ClientInquiryElement> page = new PageModel<ClientInquiryElement>();
	    		page.put("id", id);
	    		//List<ClientInquiryElement> list = new ArrayList<ClientInquiryElement>();
	    		if (specialCode.equals(1)) {
	    			page.put("special", 1);
				}
	    		//List<ClientInquiryElement> list = clientInquiryElementDao.getTypeCode(page);
	    		List<Integer> storeSupplier = supplierCommissionSaleDao.getSupplierIdByInquiryId(id);
	    		//for (int a = 0; a < list.size(); a++) {
	    			//List<ClientInquiryElement> elementList = clientInquiryElementDao.findByTypeCode(list.get(a));
	    		List<SupplierAirRelationKey> supplierList = supplierAirRelationDao.selectByClientInuqiryId(id);
	    		/*if (free) {
	    			supplierList = supplierAirRelationDao.selectByClientInuqiryIdForFree(id);
				}else {
					supplierList = supplierAirRelationDao.selectByClientInuqiryId(id);
				}*/
	    		if (supplierList.size()>0) {
    				SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(id);
    				for (int i = 0; i < supplierList.size(); i++) {
    					List<ClientInquiryElement> elementList = clientInquiryElementDao.getListBySupplierAbility(id, supplierList.get(i).getSupplierId());
    					for (int j = 0; j < storeSupplier.size(); j++) {
    						if (null!=storeSupplier.get(j)&&storeSupplier.get(j).equals(supplierList.get(i).getSupplierId())) {
    							storeSupplier.remove(j);
    						}
    					}
    					for (int j = 0; j < elementList.size(); j++) {
							int count = supplierQuoteElementDao.getByPartInTwoWeeks(elementList.get(j).getPartNumber(), supplierList.get(i).getSupplierId().toString());
							if (count > 0) {
								elementList.remove(j);
								j=j-1;
							}
						}
    					if (elementList.size() > 0) {
    						String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierList.get(i).getSupplierId());
	    					supplierInquiry.setQuoteNumber(quoteNumber);
	    					
	    					supplierInquiry.setUpdateTimestamp(new Date());
	    					supplierInquiry.setSupplierId(supplierList.get(i).getSupplierId());
	    					supplierInquiry.setAutoAdd(1);
	    					supplierInquiryService.insertSelective(supplierInquiry);
	    						
	    					//List<ClientInquiryElement> inquiryList = clientInquiryElementDao.findByTypeCode(list.get(a));
	    					for (int j = 0; j < elementList.size(); j++) {
	    						SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
	    						supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
	    						supplierInquiryElement.setClientInquiryElementId(elementList.get(j).getId());
	    						supplierInquiryElement.setUpdateTimestamp(new Date());
	    						ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(elementList.get(j).getId());
	    						supplierInquiryElementService.insertSelective(supplierInquiryElement);
	    						if (!"".equals(clientInquiryElement.getElementStatusId()) && clientInquiryElement.getElementStatusId()!=null) {
	    							if (clientInquiryElement.getElementStatusId().equals(700) || clientInquiryElement.getElementStatusId()==700) {
	    								clientInquiryElement.setElementStatusId(701);
	    								clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
	    							}
	    						}
	    						
	    					}
	    					if (elementList.size()>20) {
	    						StringBuffer businessKey = new StringBuffer();
	    						businessKey.append("supplier_air_relation.id.").append(supplierInquiry.getId()).append(".supplierAirRelationExcel");
	    						try {
	    							gyExcelService.generateExcel(businessKey.toString());
	    						} catch (Exception e1) {
	    							// TODO Auto-generated catch block
	    							e1.printStackTrace();
	    							success = false;
	    						} 
	    					}
	    					
		    				//}
		    				//发送邮件
		    				//for (int i = 0; i < supplierList.size(); i++) {
		    				ExchangeMail exchangeMail = new ExchangeMail();
		    				List<Integer> users = authorityRelationDao.getUserId(supplierList.get(i).getSupplierId());
							UserVo userVo2 = userDao.findUserByUserId(users.get(0).toString());
							exchangeMail.setUsername("purchaser@betterairgroup.com");
							exchangeMail.setPassword("GZBAcom@)!&0817");
//							exchangeMail.setUsername("liana@betterairgroup.com");
//							exchangeMail.setPassword("TestZAQ!2wsx");
		    				String userEmail = "";
		    				String fullName = userVo2.getFullName();
		    				String tel = userVo2.getPhone();
		    				/*if (supplierList.get(i).getCode().equals("61")) {
		    					if (users.size() > 1) {
		    						Integer userId = authorityRelationDao.getCassieUserId(supplierList.get(i).getSupplierId());
		    						if (userId != null) {
		    							UserVo userVo = userDao.findUserByUserId(userId.toString());
		    							exchangeMail.setUsername(userVo.getEmail());
		    							exchangeMail.setPassword(userVo.getEmailPassword());
		    							fullName = userVo.getFullName();
		    							tel = userVo.getPhone();
		    						}else {
		    							UserVo userVo = userDao.findUserByUserId(users.get(0).toString());
		    							exchangeMail.setUsername(userVo.getEmail());
		    							exchangeMail.setPassword(userVo.getEmailPassword());
		    							fullName = userVo.getFullName();
		    							tel = userVo.getPhone();
		    						}
		    					}
		    				}*/
		    				
		    				//Supplier supplier = supplierDao.selectByPrimaryKey(supplierList.get(i).getSupplierId());
		    				List<SupplierContact> supplierContacts = supplierContactDao.getEmailPerson(supplierList.get(i).getSupplierId());
		    				if (supplierContacts.size() == 0) {
		    					supplierContacts = supplierContactDao.getEmails(supplierList.get(i).getSupplierId());
							}
		    				EmailTemplateVo[] templateVos = getEmailTemplates();
		    				int index = (int) (Math.random() * templateVos.length);
		    				EmailTemplateVo emailTemplateVo = templateVos[index];
		    					for (int j = 0; j < supplierContacts.size(); j++) {
		    						if (supplierContacts.get(j).getEmail()!=null && !"".equals(supplierContacts.get(j).getEmail())) {
		    							List<String> to = new ArrayList<String>();
		    							if (supplierContacts.get(j).getEmail() != null && !"".equals(supplierContacts.get(j).getEmail())) {
		    								to.add(supplierContacts.get(j).getEmail().trim());
		    							}
		    							List<String> cc = new ArrayList<String>();
		    							List<String> bcc = new ArrayList<String>();
		    							bcc.add(exchangeMail.getUsername());
		    							StringBuffer bodyText = new StringBuffer();
		    							String name = supplierContacts.get(j).getName();
		    							bodyText.append("<div>Dear ");
		    							bodyText.append(name);
		    							bodyText.append("</div>");
		    							bodyText.append("<div>&nbsp;</div>");
		    							String realPath = "";
		    							if (elementList.size()<=20) {
		    								//表头
		    								if (emailTemplateVo.getHeader() != null && !"".equals(emailTemplateVo.getHeader())) {
												bodyText.append(emailTemplateVo.getHeader());
											}
		    								//bodyText.append("<div>Would you please kindly check if you could quote below part number to us? </div>");
		    								bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
		    										+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    										+ "SN"
		    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    										+"Part No."
		    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    										+"Description"
		    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    										+"A/U"
		    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    										+"Qty"
		    										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    										+"remark"
		    										+"</td></tr>"
		    										);
		    								boolean ohOrSv = false;
		    								for (int k = 0; k < elementList.size(); k++) {
		    									String remark = "";
		    									if (elementList.get(k).getRemark() != null) {
		    										remark = elementList.get(k).getRemark();
												}
		    									bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    													+ elementList.get(k).getItem()
		    													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    													+ elementList.get(k).getPartNumber()
		    													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    													+ elementList.get(k).getDescription()
		    													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    													+ elementList.get(k).getUnit()
		    													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    													+ elementList.get(k).getAmount()
		    													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
		    													+ remark
		    													+"</td></tr>"
		    													);
		    									/*if (elementList.get(k).getTypeCode().equals(42) || elementList.get(k).getTypeCode().equals(44) || elementList.get(k).getTypeCode().equals(74)
		    											|| elementList.get(k).getTypeCode().equals(43) || elementList.get(k).getTypeCode().equals(72) || elementList.get(k).getTypeCode().equals(73)){
		    										ohOrSv = true;
												}*/
		    								}
		    								bodyText.append("</table></div>");
		    								/*if (ohOrSv) {
		    									bodyText.append("<div> </div>");
		    									bodyText.append("<div>For OH or SV parts, please kindly provide the Airworthiness Release Certification and "
														+ "shop finding report along with the quotation for our reference. "
														+ "Pls also advise warranty period in your quotation.</div>");
//		    									bodyText.append("<div>For the OH or SV parts, please provide the ARC（certificate）and shop finding report along with your quotation and advise warranty period. "
//		    													+ "Pls also inform if there is any hazmat fee or a special box needed to ship the parts? "
//		    													+ "If yes, please advise the hazmat fee or extra charge."
//		    													+ "For the special container, please quote both outright price and rental price to us. "
//		    													+ "We will decide to buy or rent the box according to the price.</div>");
											}*/
		    								/*SystemCode systemCode = systemCodeDao.findById(supplierList.get(i).getAirId());
		    								if (systemCode.getCode().equals("81")) {
		    									bodyText.append("<div> </div>");
		    									bodyText.append("<div>For OH parts, please kindly provide below information and document along with the quotation.</div>");
		    									bodyText.append("<div>1>	TSN/CSN of the parts.</div>");
		    									bodyText.append("<div>2>	ARC and shop finding report.</div>");
		    									bodyText.append("<div>3>	Last operator.</div>");
		    									bodyText.append("<div>4>	Full traceability with NIS.</div>");
		    								}else if (systemCode.getCode().equals("44")) {
		    									bodyText.append("<div> </div>");
		    									bodyText.append("<div>For the OH or SV parts, please provide the ARC（certificate）and shop finding report along with your quotation and advise warranty period. "
		    													+ "Pls also inform if there is any hazmat fee or a special box needed to ship the parts? "
		    													+ "If yes, please advise the hazmat fee or extra charge."
		    													+ "For the special container, please quote both outright price and rental price to us. "
		    													+ "We will decide to buy or rent the box according to the price.</div>");
		    								}else if (systemCode.getCode().equals("43")) {
		    									bodyText.append("<div> </div>");
		    									bodyText.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
		    													+ "Please kindly inform if there is any hazmat fee to ship the parts? "
		    													+ "If yes, please kindly also quote to us.</div>");
		    								}else if (systemCode.getCode().equals("61") || systemCode.getCode().equals("71")) {
		    									bodyText.append("<div> </div>");
		    									bodyText.append("<div>For the OH or SV parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. ");
		    								}*/
		    								/*bodyText.append("<div>Thank you and look forward to your reply.</div>");*/
		    								if (emailTemplateVo.getBottom() != null && !"".equals(emailTemplateVo.getBottom())) {
												bodyText.append(emailTemplateVo.getBottom());
											}
		    							}else {
		    								bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
		    								realPath = "D:/CRM/Files/mis/email/sampleoutput/"+quoteNumber+".xls";
		    							}
		    							
		    							bodyText.append("<div>&nbsp;</div>");
		    							bodyText.append("<div>Best Regards</div>");
		    							bodyText.append("<div>").append("Purchasing team").append("</div>");
		    							//bodyText.append("<div>Purchasing Specialist</div>");
		    							bodyText.append("<div>Betterair Trade Co., Ltd</div>");
		    							bodyText.append("<div>Email : ").append(exchangeMail.getUsername()).append("</div>");
		    							bodyText.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
		    							bodyText.append("<div>Tel.: ").append("+852 30717759  Fax: +852 30763097").append("</div>");
		    							exchangeMail.init();
		    							try {
		    								exchangeMail.doSend("RFQ"+quoteNumber, to, cc, bcc, bodyText.toString(), realPath);
		    								EmailMessage emailMessage = new EmailMessage(supplierInquiry.getId(), to.toString(), cc.toString(), bcc.toString(),new Date());
		    								emailMessageDao.insertSelective(emailMessage);
		    								supplierInquiry.setEmailStatus(1);
		    								supplierInquiryDao.updateByPrimaryKeySelective(supplierInquiry);
		    							} catch (Exception e) {
		    								success = false;
		    								e.printStackTrace();
		    							}
	    						}
	    						
	    					}
						}
    					
    				}
    			}
	    		//}
	    		return success;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			clientInquiry.setEmailStatus(1);
			clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
		}
    	return false;
    }

    /*
     * 查找是否有未处理的参数处理异常
     */
	public void onpostcheck() {
		List<Map<String , Object>> lm = onpostEmailDao.checkonpost();
		if(lm.size() > 0){
			StringBuffer sbsend = new StringBuffer();
			StringBuffer sbset = new StringBuffer();
			for(Map<String , Object> m : lm){
				sbsend.append((String)m.get("QUOTE_NUMBER"));
				sbsend.append("<br>");
				sbset.append((Integer)m.get("post_id"));
				sbset.append(",");
			}
			String setemails = sbset.toString();
			setemails = setemails.substring(0,setemails.length()-1);
			if(sendpostexc(sbsend.toString())){
				PageModel<String> page = new PageModel<String>();
				page.put("setemails", setemails);
				//将发送状态设为1
				onpostEmailDao.updateById(page);
				
			}
		}
	}
    
	/*
    * 发送通知异常的邮件
    */
    public boolean sendpostexc(String quote){
    	ExchangeMail exchangeMail = new ExchangeMail();
    	exchangeMail.setUsername("cassie@betterairgroup.com");
		exchangeMail.setPassword("GZBAcassie@)!^1031");
		
		exchangeMail.init();
		
		List<String> to = new ArrayList<String>();
		to.add("tanoy@naswork.com");
		
		String bodyText = "<div style=\"word-wrap: break-word;background:#e4e4e4\"><div height=\"100\" >&nbsp;</div>"+
				"<table style=\"border-top:6px solid #ea5d5d;\" align=\"center\" width=\"480\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" bgcolor=\"#f3fbfd\"><tbody>"+
				"<tr> <td width=\"30\" height=\"20\" bgcolor=\"#f3fbfd\">&nbsp;</td> <td style=\"color:#333;font-size:16px;\" align=\"center\"> </td> "+ 
				"<td style=\"\" align=\"left\" width=\"30\" bgcolor=\"#f3fbfd\">&nbsp;</td></tr>"+
				"<tr> <td width=\"30\" height=\"20\" bgcolor=\"#f3fbfd\">&nbsp;</td> <td style=\"color:#333;font-size:16px;\" align=\"center\"> <p style=\"margin:0; line-height:23px;\">"+
				"异常报告"+
				"</p> </td> <td style=\"\" align=\"left\" width=\"30\" bgcolor=\"#f3fbfd\">&nbsp;</td> </tr> <tr> <td width=\"30\" height=\"20\" bgcolor=\"#f3fbfd\">&nbsp;</td> "+
				"<td style=\"color:#333;font-size:16px;\" align=\"left\"> <p style=\"margin:0; line-height:23px;\">&nbsp;</p> <p style=\"margin:0; line-height:23px;margin-left: 30px;\">"+
				"参数处理错误！<br><br>"+
				"出错的订单号列表 : <br>"+
				quote+
				"</p> </td> <td style=\"\" align=\"left\" width=\"30\" bgcolor=\"#f3fbfd\">&nbsp;</td> </tr> <tr> <td width=\"30\" height=\"20\" bgcolor=\"#f3fbfd\">&nbsp;</td> "+
				"<td style=\"color:#333;font-size:16px;line-height:20px;\" align=\"left\"> <br> </td> <td style=\"\" align=\"left\" width=\"30\" bgcolor=\"#f3fbfd\">&nbsp;</td> </tr> "+ 
				"<tr> <td width=\"30\" height=\"20\" bgcolor=\"#f3fbfd\">&nbsp;</td> <td style=\"color:#333;font-size:16px;\" align=\"center\"> </td>"+ 
				"<td style=\"\" align=\"left\" width=\"30\" bgcolor=\"#f3fbfd\">&nbsp;</td> </tr> </tbody> </table><div style=\"height: 50px;\">&nbsp;</div></div>";
		
		List<String> cc = new ArrayList<String>();
		List<String> bcc = new ArrayList<String>();
		String realPath = "";
		boolean s = false;
		try {
			exchangeMail.doSend("EXCEPTION REPORT", to, cc, bcc, bodyText.toString(), realPath);
			s = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
    }
    
    /*
     * 竞争对手上传
     */
    public MessageVo uploadCompetitor(MultipartFile multipartFile, Integer id){
    	boolean success = false;
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
		    
		    Row firstRow = sheet.getRow(sheet.getFirstRowNum());
		    
		    StringBuffer message = new StringBuffer();
		    MessageVo messageVo = new MessageVo();
		    
		    //记录不存在的竞争者
		    List<String> unknowCompetitor = new ArrayList<String>();
		    //记录错误的item
		    List<Integer> errorItem = new ArrayList<Integer>();
		    
		    List<CompetitorQuote> CompetitorQuotes = new ArrayList<CompetitorQuote>();
		    List<CompetitorQuoteElement> competitorQuoteElements = new ArrayList<CompetitorQuoteElement>();
		    
		    //价格
		    Map<String, CompetitorQuoteElement> priceMap =  new HashMap<String, CompetitorQuoteElement>();
		    int cells = firstRow.getLastCellNum()-3;
		    for (int i = 0; i < firstRow.getLastCellNum()
					- firstRow.getFirstCellNum() - 3; i++) {
		    	String code = firstRow.getCell(i+3).toString();
		    	if (!"".equals(code)) {
		    		CompetitorVo competitorVo = competitorDao.findByCode(code);
			    	if (competitorVo==null) {
						unknowCompetitor.add(code);
					}else {
						CompetitorQuote competitorQuote =new CompetitorQuote();
						competitorQuote.setClientInquiryId(id);
						competitorQuote.setCurrencyId(competitorVo.getCurrencyId());
						competitorQuote.setExchangeRate(competitorVo.getExchangeRate());
						competitorQuote.setCompetitorId(competitorVo.getId());
						competitorQuote.setQuoteDate(new Date());
						competitorQuote.setUpdateTimestamp(new Date());
						CompetitorQuotes.add(competitorQuote);
					}
				}
		    }
		    //判断是否存在不存在的竞争者
		    if (unknowCompetitor.size()>0) {
		    	message.append("Competitor ");
				for (String a : unknowCompetitor) {
					message.append(a).append(",");
				}
				message.deleteCharAt(message.length()-1);
				message.append(" do not exist!");
				String result = message.toString();
				messageVo.setFlag(success);
				messageVo.setMessage(result);
				return messageVo;
			}
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
				int ab = sheet.getPhysicalNumberOfRows();
	            row = sheet.getRow(i);
	            int lastCellnum = row.getLastCellNum();
	            Integer clientInquiryElementId = null;
	            if (!row.getCell(0).toString().toUpperCase().equals("FREIGHT")&&!row.getCell(0).toString().toUpperCase().equals("TOTAL")) {
	            	Integer item = new Double(row.getCell(0).toString()).intValue();
		            String partNumber = row.getCell(1).toString();
	            	ClientInquiryElement clientInquiryElement =new ClientInquiryElement();
	 	            clientInquiryElement.setClientInquiryId(id);
	 	            clientInquiryElement.setItem(item);
	 	            clientInquiryElementId = clientInquiryElementDao.getId(clientInquiryElement);
	 	            if (clientInquiryElementId==null) {
	 	            	errorItem.add(item);
	 				}
				}
	           
	            //得到价格
	            for (int j = 0; j < cells; j++) {
	            	if (!"".equals(row.getCell(j+3).toString().trim())) {
	            		String a=(row.getCell(j+3).toString());
	            		Cell c=row.getCell(j+3);
	            		String aString = row.getCell(j+3).toString();
	            		Double price = new Double(row.getCell(j+3).toString());
		            	//判断是否是邮费
		            	if (row.getCell(0).toString().toUpperCase().equals("FREIGHT")) {
							CompetitorQuotes.get(j).setFreight(price);
						}else if(!row.getCell(0).toString().toUpperCase().equals("TOTAL")){
							CompetitorQuoteElement competitorQuoteElement = new CompetitorQuoteElement();
							competitorQuoteElement.setPrice(price);
							competitorQuoteElement.setClientInquiryElementId(clientInquiryElementId);
			            	//competitorQuoteElements.add(competitorQuoteElement);
							priceMap.put(String.valueOf(i)+"-"+String.valueOf(j), competitorQuoteElement);
						}
					}
				}
	        }
			//判断item是否存在错误的item
			if (errorItem.size()>0) {
				message.append("Item ");
				for (Integer a : errorItem) {
					message.append(a).append(",");
				}
				message.deleteCharAt(message.length()-1);
				message.append(" do not exist!");
				String result = message.toString();
				messageVo.setFlag(success);
				messageVo.setMessage(result);
				return messageVo;
			}
			
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows()-2; i++) {
				row = sheet.getRow(i);
				for (int j = 0; j < CompetitorQuotes.size(); j++) {
					if (priceMap.get(String.valueOf(i)+"-"+String.valueOf(j))!=null) {
						CompetitorQuote competitorQuote2 = CompetitorQuotes.get(j);
						competitorQuoteDao.insertSelective(competitorQuote2);
						CompetitorQuoteElement competitorQuoteElement2 = priceMap.get(String.valueOf(i)+"-"+String.valueOf(j));
						Integer competitorQuoteId = competitorQuote2.getId();
						competitorQuoteElement2.setCompetitorQuoteId(competitorQuoteId);
						competitorQuoteElementDao.insertSelective(competitorQuoteElement2);
					}
				}
			}
			success = true;
			message.append("save successful!");
			messageVo.setFlag(success);
			messageVo.setMessage(message.toString());
			return messageVo;
		} catch (Exception e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
    }
    
    /*
     * 根据单号获取id
     */
    public Integer findByQuoteNumber(String quoteNumber){
    	return clientInquiryDao.findByQuoteNumber(quoteNumber);
    }
    
    /*
     * 保存竞争对手报价
     */
    public ResultVo saveTender(TenderVo tenderVo,Integer id) {
		boolean success = false;
		
		List<Competitor> competitorList = tenderVo.getCompetitorList();
		List<CompetitorQuoteElement> competitorQuoteElementList = tenderVo.getCompetitorQuoteElementList();
		List<CompetitorQuote> competitorQuoteList = tenderVo.getCompetitorQuoteList();
		
		//记录不存在的竞争者
	    List<String> unknowCompetitor = new ArrayList<String>();
	    List<CompetitorQuote> CompetitorQuotes = new ArrayList<CompetitorQuote>();
		for (int i = 0; i < competitorList.size(); i++) {
				if (competitorList.get(i).getCode()!=null) {
					String code = competitorList.get(i).getCode();
			    	CompetitorVo competitorVo = competitorDao.findByCode(code);
			    	if (competitorVo==null) {
						unknowCompetitor.add(code);
					}else {
						CompetitorQuote competitorQuote =new CompetitorQuote();
						competitorQuote.setClientInquiryId(id);
						competitorQuote.setCurrencyId(competitorVo.getCurrencyId());
						competitorQuote.setExchangeRate(competitorVo.getExchangeRate());
						competitorQuote.setCompetitorId(competitorVo.getId());
						competitorQuote.setQuoteDate(new Date());
						competitorQuote.setUpdateTimestamp(new Date());
						CompetitorQuotes.add(competitorQuote);
					}
				}
		}
		
		
		StringBuffer message = new StringBuffer();
		 //判断是否存在不存在的竞争者
	    if (unknowCompetitor.size()>0) {
	    	message.append("竞争对手  ");
			for (String a : unknowCompetitor) {
				message.append(a).append(",");
			}
			message.deleteCharAt(message.length()-1);
			message.append(" 不存在!");
			
			return new ResultVo(success, message.toString());
		}
	    
	    for (int i = 0; i < competitorQuoteList.size(); i++) {
	    		if (competitorQuoteList.get(i)!=null) {
					CompetitorQuotes.get(i).setFreight(competitorQuoteList.get(i).getFreight());
				}
			
		}
	    
	    /*for (int i = 0; i < competitorQuoteElementList.size(); i++) {
	    	for (int j = 0; j < competitorQuoteElementList.get(i).size(); j++) {
	    		if (competitorQuoteElementList.get(i)!=null) {
					competitorQuoteElements.add(competitorQuoteElementList.get(i).get(j));
				}
			}
		}*/
		
	    List<CompetitorQuoteElement> list = new ArrayList<CompetitorQuoteElement>();
	    double length = CompetitorQuotes.size();
	    for (int i = 0; i < competitorQuoteElementList.size(); i++) {
			if (competitorQuoteElementList.get(i).getPrice()!=null || 
					competitorQuoteElementList.get(i).getClientInquiryElementId()!=null) {
				list.add(competitorQuoteElementList.get(i));
			}
		}
	    double size = Math.ceil(competitorQuoteElementList.size()/(length+1));
	    int lineCount = (int)size;
	    int lineSize = (int)length;
	    
	    for (int i = 0; i < lineCount; i++) {
	    	for (int j = 0; j < lineSize; j++) {
					if (competitorQuoteElementList.get((lineSize+1)*i+1+j).getPrice()!=null) {
						CompetitorQuote competitorQuote = CompetitorQuotes.get(j);
				    	competitorQuoteDao.insertSelective(competitorQuote);
						CompetitorQuoteElement competitorQuoteElement = competitorQuoteElementList.get((lineSize+1)*i+1+j);
						competitorQuoteElement.setClientInquiryElementId(competitorQuoteElementList.get((lineSize+1)*i).getClientInquiryElementId());
						competitorQuoteElement.setCompetitorQuoteId(competitorQuote.getId());
						competitorQuoteElementDao.insertSelective(competitorQuoteElement);
					}
			}
		}
		success = true;
		message.append("新增成功！");
	    
		return new ResultVo(success, message.toString());
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
		
		return "ClinetInquiryElement"+"_"+this.fetchUserName()+"_"+format.format(now);
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
		return "client_inquiry_element";
	}
    
    public String fetchYwTablePkName() {
		return "id";
	}
    
    public String fetchMappingKey() {
		return "ClientInquieyElementExcel";
	}
    
    public String getCodeFromPartNumber(String partNumber) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < partNumber.length(); i++) {
			char ch = partNumber.charAt(i);
			/*if (isValidCharacter(ch)) {
				buffer.append(Character.toUpperCase(ch));
			}*/
			String regex = "[a-z0-9A-Z\u4e00-\u9fa5]";//其他需要，直接修改正则表达式就好
			if (String.valueOf(ch).matches(regex)) {
				buffer.append(String.valueOf(ch));
			}
			//return str.matches(regex);
		}
		return buffer.toString();
    }
    public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
    }

	@Override
	public List<TPart> findisblacklist(String partnumbercode) {
		List<TPart> plist=tPartDao.selectByPartNumberCode(getCodeFromPartNumber(partnumbercode));
		return plist;
	}
	
	public void getErrorPage(PageModel<ClientInquiryElementUpload> page){
		page.setEntities(clientInquiryElementUploadDao.getErrorPage(page));
	}
	
	public void deleteError(Integer userId){
		clientInquiryElementUploadDao.deleteError(userId);
	}

	@Override
	public ClientInquiry selectByPrimaryKey(Integer id) {
		return clientInquiryDao.selectByPrimaryKey(id);
	}
	
	public boolean supplierCommissionSale (Integer clientInquiryId){
		List<Integer> suppliers = supplierCommissionSaleDao.getSupplierIdByClientInquiryId(clientInquiryId);
		ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientInquiryId);
		for (int i = 0; i < suppliers.size(); i++) {
			List<ClientInquiryElement> elementList = clientInquiryElementDao.getCommissionListByClientInquiryId(clientInquiryId, suppliers.get(i));
			SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(clientInquiryId);
			String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),suppliers.get(i));
			supplierInquiry.setQuoteNumber(quoteNumber);
			
			supplierInquiry.setUpdateTimestamp(new Date());
			supplierInquiry.setSupplierId(suppliers.get(i));
			supplierInquiryService.insertSelective(supplierInquiry);
			for (int j = 0; j < elementList.size(); j++) {
				SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
				supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
				supplierInquiryElement.setClientInquiryElementId(elementList.get(j).getId());
				supplierInquiryElement.setUpdateTimestamp(new Date());
				supplierInquiryElementService.insertSelective(supplierInquiryElement);
			}
			
			List<String> roleNames = supplierDao.getRoleNameBySupplierId(suppliers.get(i));
			if (roleNames.size() > 0) {
				ExchangeMail exchangeMail = new ExchangeMail();
				Integer userId = authorityRelationDao.getUserId(suppliers.get(i)).get(0);
				UserVo userVo2 = userDao.findUserByUserId(userId.toString());
				UserVo current = ContextHolder.getCurrentUser();
				List<String> user = new ArrayList<String>();
				exchangeMail.setUsername("purchaser@betterairgroup.com");
				exchangeMail.setPassword("GZBAcom@)!&0817");
				exchangeMail.init();
				List<String> bcc = new ArrayList<String>();
				bcc.add(userVo2.getEmail());
				List<SupplierContact> supplierContacts = supplierContactDao.getEmails(suppliers.get(i));
				if (roleNames.get(0).indexOf("国外") >= 0) {
					for (int j = 0; j < supplierContacts.size(); j++) {
						if (supplierContacts.get(j).getEmail()!=null && !"".equals(supplierContacts.get(j).getEmail())) {
							List<String> to = new ArrayList<String>();
							if (supplierContacts.get(j).getEmail() != null && !"".equals(supplierContacts.get(j).getEmail())) {
								to.add(supplierContacts.get(j).getEmail().trim());
							}
							List<String> cc = new ArrayList<String>();
							/*List<String> bcc = new ArrayList<String>();
							bcc.add(userVo2.getEmail());*/
							StringBuffer bodyText = new StringBuffer();
							String name = supplierContacts.get(j).getName();
							bodyText.append("<div>Dear ");
							bodyText.append(name);
							bodyText.append("</div>");
							bodyText.append("<div>&nbsp;</div>");
							String realPath = "";
							if (elementList.size()<=20) {
								//表头
								bodyText.append("<div>Would you please kindly check that if you could quote below part number to us? </div>");
								bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
										+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+ "SN"
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+"Part No."
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+"Description"
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+"A/U"
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+"Qty"
										+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
										+"remark"
										+"</td></tr>"
										);
								for (int k = 0; k < elementList.size(); k++) {
									bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
													+ elementList.get(k).getItem()
													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
													+ elementList.get(k).getPartNumber()
													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
													+ elementList.get(k).getDescription()
													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
													+ elementList.get(k).getUnit()
													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
													+ elementList.get(k).getAmount()
													+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
													+ elementList.get(k).getRemark()
													+"</td></tr>"
													);
								}
								bodyText.append("</table></div>");
								/*SystemCode systemCode = systemCodeDao.findById(supplierList.get(i).getAirId());
								if (systemCode.getCode().equals("81")) {
									bodyText.append("<div> </div>");
									bodyText.append("<div>For OH parts, please kindly provide below information and document along with the quotation.</div>");
									bodyText.append("<div>1>	TSN/CSN of the parts.</div>");
									bodyText.append("<div>2>	ARC and shop finding report.</div>");
									bodyText.append("<div>3>	Last operator.</div>");
									bodyText.append("<div>4>	Full traceability with NIS.</div>");
								}else if (systemCode.getCode().equals("44")) {
									bodyText.append("<div> </div>");
									bodyText.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
													+ "And please kindly inform if there is any hazmat fee or a special box needed to ship the parts? "
													+ "If yes, please kindly also quote to us. And for the special container, "
													+ "please kindly quote both outright price and rental price to us. "
													+ "We will decide to buy or rent the box according to the price. </div>");
								}else if (systemCode.getCode().equals("43")) {
									bodyText.append("<div> </div>");
									bodyText.append("<div>For the OH parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
													+ "Please kindly inform if there is any hazmat fee to ship the parts? "
													+ "If yes, please kindly also quote to us.</div>");
								}else if (systemCode.getCode().equals("61") || systemCode.getCode().equals("71")) {
									bodyText.append("<div> </div>");
									bodyText.append("<div>For the OH or SV parts, please kindly provide the ARC and shop finding report along with the quotation for our reference. "
													+ "Thank you and look forward to your reply.</div>");
								}*/
								bodyText.append("<div>Thank you and look forward to your reply.</div>");
							}else {
								if (elementList.size()>20) {
									StringBuffer businessKey = new StringBuffer();
									businessKey.append("supplier_air_relation.id.").append(supplierInquiry.getId()).append(".supplierAirRelationExcel");
									try {
										gyExcelService.generateExcel(businessKey.toString());
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
										return false;
									} 
								}
								bodyText.append("<div>Would you please kindly quote your best price and delivery for the part numbers on the attached excel form? Thank you and look forward to your quote.</div>");
								realPath = "D:/CRM/Files/mis/email/sampleoutput/"+quoteNumber+".xls";
							}
							bodyText.append("<div>&nbsp;</div>");
							bodyText.append("<div>Best Regards</div>");
							bodyText.append("<div>").append("Purchasing team").append("</div>");
							//bodyText.append("<div>Purchasing Specialist</div>");
							bodyText.append("<div>Betterair Trade Co., Ltd</div>");
							bodyText.append("<div>Email : ").append(exchangeMail.getUsername()).append("</div>");
							bodyText.append("<div>Add.:  Unit.04  7/F Bright Way Tower ,33 Mong Kok Road Kowloon, Hongkong</div>");
							bodyText.append("<div>Tel.: ").append("+852 30717759  Fax: +852 30763097").append("</div>");
							exchangeMail.init();
							try {
								exchangeMail.doSend("RFQ"+quoteNumber, to, cc, bcc, bodyText.toString(), realPath);
							} catch (Exception e) {
								e.printStackTrace();
								return false;
							}
						}
					}
				}else if (roleNames.get(0).indexOf("国内") >= 0) {
					
					SystemCode biz = systemCodeDao.selectByPrimaryKey(clientInquiry.getBizTypeId());
					StringBuffer text = new StringBuffer();
					String businessKey = "supplier_inquiry.id."+supplierInquiry.getId()+"-"+0+".SupplierInquieyExcel";
					String realPath = "";
					String path = "";
					if (elementList.size()>0) {
						try {
							SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
							Date now = new Date();
							path = "D:/CRM/Files/mis/excel/sampleoutput/"+supplierInquiry.getQuoteNumber()+"_"+"SupplierInquiry"+"_"+current.getUserName()+"_"+format.format(now)+".xls";
							gyExcelService.generateExcel(businessKey.toString());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					String title = "RFQ"+supplierInquiry.getQuoteNumber();
					for (int j = 0; j < supplierContacts.size(); j++) {
						List<String> to = new ArrayList<String>();
						if (supplierContacts.get(j).getEmail() != null && !"".equals(supplierContacts.get(j).getEmail())) {
							to.add(supplierContacts.get(j).getEmail().trim());
						}
						if (supplierContacts.get(j).getEmail()!=null && !"".equals(supplierContacts.get(j).getEmail())) {
							text.append("<div>").append(supplierContacts.get(j).getSurName()).append(supplierContacts.get(j).getAppellation()).append(",您好！").append("</div>");
							text.append("<div>附件为").append(biz.getValue()).append(supplierInquiry.getQuoteNumber()).append(",</div>");
							/*if (!"".equals(firstText) && firstText != null) {
								text.append("<div>").append(firstText).append(",").append("</div>");
							}
							text.append("<div>").append("请尽快报价，谢谢！").append("</div>");
							if (!"".equals(secondText) && secondText != null) {
								text.append("<div>").append(secondText).append("</div>");
							}*/
							text.append("</div>");
							text.append("<div>&nbsp;</div>");
							text.append("<div>-----------------------------------------------<div>");
							text.append("<div><b>李璐</b></div>");
							text.append("<div></div>");
							text.append("<div><b>广州航润贸易有限公司</b></div>");
							text.append("<div>地址：广东省广州市越秀区竹丝岗二马路39号之一中航大厦4楼  邮编：510080</div>");
							text.append("<div>手机：13926405509 固定电话：020-39177732 传真：020-39177076</div>");
							text.append("<div>E-mail：lilu@hrmy.cn</div>");
						}
						try {
							if (roleNames.get(0).indexOf("采购") > 0 && elementList.size()>0) {
								if (text.length() > 0 || !"".equals(path)) {
									exchangeMail.doSend(title.toString(), to, user, bcc, text.toString(), path);
								}
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	
	public EmailTemplateVo[] getEmailTemplates(){
		EmailTemplateVo template1 = new EmailTemplateVo();
		EmailTemplateVo template2 = new EmailTemplateVo();
		EmailTemplateVo template3 = new EmailTemplateVo();
		EmailTemplateVo template4 = new EmailTemplateVo();
		
		template1.setHeader("<div>Would you please kindly check that if you could quote below part number to us? </div>");
		template1.setBottom("<div>For OH or SV parts, please kindly provide the Airworthiness Release Certification and shop finding report along with the quotation for our reference. Pls also advise warranty period in your quotation.</div><div>Thank you and look forward to your reply.</div>");
		
		template2.setHeader("<div>Good day.</div><div>Pls kindly quote your best price and lead time for below parts,and also advise cert type, warranty and tag date. Pls attach a copy of certificate to us if available. Thank you.</div>");
		template2.setBottom("");
		
		template3.setHeader("<div>Could you please kindly check and quote below part to us?</div><div>Please advise cert type (FAA 8130-3 or EASA FORM 1 or Manufacturer C of C) and lead time.For OH/SV parts, pls send a copy of certificate & trace document to us and advise warranty.</div>");
		template3.setBottom("<div>Thank you and look forward to your quotation.</div>");
		
		template4.setHeader("<div>Would you please kindly quote your best price and delivery time for the part numbers as below:</div>");
		template4.setBottom("<div>For OH/SV parts, please kindly provide below information along with the quotation.</div><div>1> ARC(certificate) and shop finding report.</div><div>2> Full traceability with NIS.</div><div>Thank you and look forward to your reply.</div>");
		
		EmailTemplateVo[] templates = {template1,template2,template3,template4};
		return templates;
	}
	
	/**
	 * 获取字符中的数字
	 * @param partNumber
	 * @return
	 */
	public String getNumber(String partNumber) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < partNumber.length(); i++) {
			char ch = partNumber.charAt(i);
			String regex = "[0-9\\.]";//
			if (String.valueOf(ch).matches(regex)) {
				buffer.append(String.valueOf(ch));
			}
		}
		return buffer.toString();
    }

}
