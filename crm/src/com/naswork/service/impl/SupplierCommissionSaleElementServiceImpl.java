package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jcifs.dcerpc.rpc.sid_t;
import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientContactDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.SupplierCommissionSaleDao;
import com.naswork.dao.SupplierCommissionSaleElementDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Element;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.purchase.controller.supplierquote.CrawerVo;
import com.naswork.module.storage.controller.suppliercommissionsale.CountStockMarketVo;
import com.naswork.service.ClientContactService;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientService;
import com.naswork.service.GyFjService;
import com.naswork.service.SupplierCommissionSaleElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.CrawerUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Service("supplierCommissionSaleElementService")
public class SupplierCommissionSaleElementServiceImpl implements
		SupplierCommissionSaleElementService {

	@Resource
	private SupplierCommissionSaleElementDao supplierCommissionSaleElementDao;
	@Resource
	private GyFjService gyFjService;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private SupplierCommissionSaleDao supplierCommissionSaleDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private ElementDao elementDao;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private ClientService clientService;
	@Resource
	private ClientContactDao clientContactDao;
	
	@Override
	public int insertSelective(SupplierCommissionSaleElement record) {
		return supplierCommissionSaleElementDao.insertSelective(record);
	}

	@Override
	public SupplierCommissionSaleElement selectByPrimaryKey(Integer id) {
		return supplierCommissionSaleElementDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierCommissionSaleElement record) {
		if (record.getQuoteFeeForExchangeBill() != null && !"".equals(record.getQuoteFeeForExchangeBill())) {
			record.setFeeForExchangeBill(new Double(record.getQuoteFeeForExchangeBill().replace("$", "")));
		}
		if (record.getQuoteBankCost() != null && !"".equals(record.getQuoteBankCost())) {
			record.setBankCost(new Double(record.getQuoteBankCost().replace("$", "")));
		}
		if (record.getQuoteHazmatFee() != null && !"".equals(record.getQuoteHazmatFee())) {
			record.setHazmatFee(new Double(record.getQuoteHazmatFee().replace("$", "")));
		}
		if (record.getQuoteOtherFee() != null && !"".equals(record.getQuoteOtherFee())) {
			record.setOtherFee(new Double(record.getQuoteOtherFee().replace("$", "")));
		}
		return supplierCommissionSaleElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(PageModel<SupplierCommissionSaleElement> page) {
		page.setEntities(supplierCommissionSaleElementDao.listPage(page));
	}
	
	public MessageVo uploadExcelSecond(MultipartFile multipartFile, Integer id){
		boolean success = false;
		String message = "保存成功！";
		InputStream fileStream = null;
		int line = 0;
		try {
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			//定义行
		    Row row;
			List<SupplierCommissionSaleElement> elementList = new ArrayList<SupplierCommissionSaleElement>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            	if (row.getCell(0) != null && !"".equals(row.getCell(0).toString().trim())) {
	            		line = i+1;
	            		SupplierCommissionSaleElement supplierCommissionSaleElement = new SupplierCommissionSaleElement();
		            	String partNumber = "";
		            	String shortPartNumber = "";
		            	Cell oneCell = row.getCell(0);
		            	if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
							partNumber = oneCell.toString();
						}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    partNumber = dataFormatter.formatCellValue(oneCell);
						}
		            	shortPartNumber = clientInquiryService.getCodeFromPartNumber(partNumber);
		            	String alt = "";
		            	Cell twoCell = row.getCell(1);
		            	if (twoCell != null) {
		            		if (twoCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
			            		alt = twoCell.toString().trim();
							}else if (twoCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								Integer b = new Double(twoCell.toString()).intValue();
								alt = b.toString();
							}
						}
		            	String description = "";
		            	if (row.getCell(2) != null) {
		            		description = row.getCell(2).toString();
						}
		            	String pri = "0";
		            	if (row.getCell(3) != null && !"".equals(row.getCell(3).toString())) {
		            		String price = clientInquiryService.getNumber(row.getCell(3).toString().trim());
		            		pri = price;
						}
		            	Double price = new Double(pri);
		            	if (row.getCell(4) != null) {
		            		supplierCommissionSaleElement.setLeadTime(row.getCell(4).toString());
						}/*else {
							supplierCommissionSaleElement.setLeadTime("0");
						}*/
		            	 
				        Double amount = 1.0;
				        if (row.getCell(5) != null && !"".equals(row.getCell(5).toString())) {
							String am = clientInquiryService.getNumber(row.getCell(5).toString());
							amount = new Double(am);
						}
				        Integer moq = 0;
				        if (row.getCell(6) != null && !"".equals(row.getCell(6).toString())) {
							String am = clientInquiryService.getNumber(row.getCell(6).toString());
							moq = new Double(am).intValue();
						}
				        String unit = row.getCell(7).toString();
				        String location = "";
				        if (row.getCell(8) != null) {
							if (row.getCell(8).toString() != null) {
								location = row.getCell(8).toString();
							}
						}
				        
				        String serialNumber = "";
				        if (row.getCell(16) != null) {
				        	serialNumber = row.getCell(16).toString();
						}
				        Integer conditionId = null;
				        String con = row.getCell(9).toString();
				        if (con != null) {
				        	List<SystemCode> list = systemCodeDao.getByCode(con.trim());
				        	if (list.size() > 0) {
								conditionId = list.get(0).getId();
							}
				        	if (list.size() == 0) {
				        		 throw new Exception("condition,"+line);
							}
						}
				        Integer certificationId = null;
				        String cert = row.getCell(10).toString();
				        if (cert != null) {
				        	List<SystemCode> list = systemCodeDao.getByCode(cert.trim());
				        	if (list.size() > 0) {
								certificationId = list.get(0).getId();
							}
				        	if (list.size() == 0) {
				        		 throw new Exception("certification,"+line);
							}
						}
				        String remark = "";
				        if (row.getCell(11) != null) {
				            remark = row.getCell(11).toString();
						}
				        if (row.getCell(22) != null && !"".equals(row.getCell(22).toString())) {
							supplierCommissionSaleElement.setCsn(new Double(row.getCell(22).toString()).intValue());
						}
				        if (row.getCell(23) != null && !"".equals(row.getCell(23).toString())) {
				        	supplierCommissionSaleElement.setTsn(new Double(row.getCell(23).toString()).intValue());
						}
						if(row.getCell(24) != null && !"".equals(row.getCell(24).toString())){
							supplierCommissionSaleElement.setAirType(row.getCell(24).toString());
						}
				        supplierCommissionSaleElement.setPartNumber(partNumber);
				        supplierCommissionSaleElement.setShortPartNumber(shortPartNumber);
				        supplierCommissionSaleElement.setSupplierCommissionSaleId(id);
				        supplierCommissionSaleElement.setAmount(amount);
				        supplierCommissionSaleElement.setRemark(remark);
				        supplierCommissionSaleElement.setAlt(alt);
				        supplierCommissionSaleElement.setDescription(description);
				        supplierCommissionSaleElement.setPrice(price);
				        supplierCommissionSaleElement.setUnit(unit);
				        supplierCommissionSaleElement.setSerialNumber(serialNumber);
				        supplierCommissionSaleElement.setConditionId(conditionId);
				        supplierCommissionSaleElement.setCertificationId(certificationId);
				        supplierCommissionSaleElement.setUpdateTimestamp(new Date());
				        supplierCommissionSaleElement.setLocation(location);
				        supplierCommissionSaleElement.setMoq(moq);
				        if (row.getCell(12) != null) {
				        	String value = row.getCell(12).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setFeeForExchangeBill(new Double(value));
							}
				        	
						}
				        if (row.getCell(13) != null) {
				        	String value = row.getCell(13).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setBankCost(new Double(value));
							}
				        	
						}
				        if (row.getCell(14) != null) {
				        	String value = row.getCell(14).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setHazmatFee(new Double(value));
							}
				        	
						}
				        if (row.getCell(15) != null) {
				        	String value = row.getCell(15).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setOtherFee(new Double(value));
							}
				        	
						}
				        if (row.getCell(17) != null) {
				        	String value = row.getCell(17).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setTagSrc(value);
							}
				        	
						}
				        if (row.getCell(18) != null) {
				        	String value = row.getCell(18).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setTagDate(value);
							}
				        	
						}
				        if (row.getCell(19) != null) {
				        	String value = row.getCell(19).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setTrace(value);
							}
				        	
						}
				        if (row.getCell(20) != null) {
				        	String value = row.getCell(20).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setWarranty(value);
							}
				        	
						}
				        if (row.getCell(21) != null) {
				        	String value = row.getCell(21).toString();
				        	if (value != null && !"".equals(value)) {
				        		supplierCommissionSaleElement.setCoreCharge(value);;
							}
				        	
						}
				        //存起读取的数据等待保存进数据库
				        if (!elementList.contains(supplierCommissionSaleElement)) {
				            elementList.add(supplierCommissionSaleElement);
						}
					}
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (elementList.size()>0){
				//存档
				excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());
				//是否有另件号
				for (SupplierCommissionSaleElement supplierCommissionSaleElement : elementList) {
					supplierCommissionSaleElementDao.insertSelective(supplierCommissionSaleElement);
				}
				try {
					addQuote(id);
				} catch (Exception e) {
					e.printStackTrace();
					SupplierCommissionSale supplierCommissionSale = new SupplierCommissionSale();
					supplierCommissionSale.setId(id);
					supplierCommissionSale.setSaleStatus(0);
					supplierCommissionSaleDao.updateByPrimaryKeySelective(supplierCommissionSale);
					return new MessageVo(false, "save supplier quote unsuccessful!");
				}
				success=true;
				message=elementList.size()+"";
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
			if (e.getMessage() != null && (e.getMessage().indexOf("certification") >= 0 || e.getMessage().indexOf("condition") >= 0)) {
				message = e.getMessage();
			}else {
				message="all,"+line;
			}
			success = false;
			SupplierCommissionSale supplierCommissionSale = new SupplierCommissionSale();
			supplierCommissionSale.setId(id);
			supplierCommissionSale.setSaleStatus(0);
			supplierCommissionSaleDao.updateByPrimaryKeySelective(supplierCommissionSale);
			return new MessageVo(success, message);
		}
		
		return null;
	}
	
	public MessageVo uploadExcel(MultipartFile multipartFile, Integer id){
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
			List<SupplierCommissionSaleElement> elementList = new ArrayList<SupplierCommissionSaleElement>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            	SupplierCommissionSaleElement supplierCommissionSaleElement = new SupplierCommissionSaleElement();
	            	String partNumber = "";
	            	Cell oneCell = row.getCell(0);
	            	if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
						partNumber = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					    partNumber = dataFormatter.formatCellValue(oneCell);
					}
	            	String alt = "";
	            	Cell twoCell = row.getCell(1);
	            	if (twoCell != null) {
	            		if (twoCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            		alt = twoCell.toString().trim();
						}else if (twoCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							Integer b = new Double(twoCell.toString()).intValue();
							alt = b.toString();
						}
					}
	            	String description = row.getCell(2).toString();
	            	Double price = new Double(row.getCell(3).toString());
	            	if (row.getCell(4) != null) {
	            		supplierCommissionSaleElement.setLeadTime(row.getCell(4).toString());
					}
	            	 
			        Double amount = new Double(row.getCell(5).toString());
			        String unit = row.getCell(6).toString();
			        String location = row.getCell(7).toString();
			        String serialNumber = "";
			        if (row.getCell(8) != null) {
			        	serialNumber = row.getCell(8).toString();
					}
			        Integer conditionId = null;
			        String con = row.getCell(9).toString();
			        if (con != null) {
			        	List<SystemCode> list = systemCodeDao.getByCode(con.trim());
			        	if (list.size() > 0) {
							conditionId = list.get(0).getId();
						}
					}
			        Integer certificationId = null;
			        String cert = row.getCell(10).toString();
			        if (cert != null) {
			        	List<SystemCode> list = systemCodeDao.getByCode(cert.trim());
			        	if (list.size() > 0) {
							certificationId = list.get(0).getId();
						}
					}
			        String remark = "";
			        if (row.getCell(11) != null) {
			            remark = row.getCell(11).toString();
					}
			        if (row.getCell(12) != null && !"".equals(row.getCell(12).toString())) {
						supplierCommissionSaleElement.setCsn(new Double(row.getCell(12).toString()).intValue());
					}
			        if (row.getCell(13) != null && !"".equals(row.getCell(12).toString())) {
			        	supplierCommissionSaleElement.setCsn(new Double(row.getCell(13).toString()).intValue());
					}
			        supplierCommissionSaleElement.setPartNumber(partNumber);
			        supplierCommissionSaleElement.setSupplierCommissionSaleId(id);
			        supplierCommissionSaleElement.setAmount(amount);
			        supplierCommissionSaleElement.setRemark(remark);
			        supplierCommissionSaleElement.setAlt(alt);
			        supplierCommissionSaleElement.setDescription(description);
			        supplierCommissionSaleElement.setPrice(price);
			        supplierCommissionSaleElement.setUnit(unit);
			        supplierCommissionSaleElement.setSerialNumber(serialNumber);
			        supplierCommissionSaleElement.setConditionId(conditionId);
			        supplierCommissionSaleElement.setCertificationId(certificationId);
			        supplierCommissionSaleElement.setUpdateTimestamp(new Date());
			        supplierCommissionSaleElement.setLocation(location);
			        //存起读取的数据等待保存进数据库
			        if (!elementList.contains(supplierCommissionSaleElement)) {
			            elementList.add(supplierCommissionSaleElement);
					}
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (elementList.size()>0){
				//存档
				excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());
				//是否有另件号
				for (SupplierCommissionSaleElement supplierCommissionSaleElement : elementList) {
					supplierCommissionSaleElementDao.insertSelective(supplierCommissionSaleElement);
				}
				addQuote(id);
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
	
	public void addWithTable(List<SupplierCommissionSaleElement> list,Integer id) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSupplierCommissionSaleId(id);;
			supplierCommissionSaleElementDao.insertSelective(list.get(i));
		}
		addQuote(id);
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
		return "SupplierCommission"+"_"+this.fetchUserName()+"_"+format.format(now);
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
		return "client_inquiry_element";
	}
    
    public String fetchYwTablePkName() {
		return "id";
	}
    
    public String fetchMappingKey() {
		return "ClientInquieyElementExcel";
	}
    
    public void addQuote(Integer supplierCommissionSaleId){
    	SupplierCommissionSale supplierCommissionSale = supplierCommissionSaleDao.selectByPrimaryKey(supplierCommissionSaleId);
    	List<SupplierCommissionSaleElement> list = supplierCommissionSaleElementDao.selectBySupplierCommissionSaleId(supplierCommissionSaleId);
    	//新增客户询价
    	Client client = clientService.findByCode("-1");
    	List<ClientContact> cclist = clientContactDao.selectByClientId(client.getId());
    	ClientInquiry clientInquiry = new ClientInquiry();
    	clientInquiry.setClientContactId(cclist.get(0).getId());
    	clientInquiry.setClientId(client.getId());
    	clientInquiry.setBizTypeId(120);
    	clientInquiry.setAirTypeId(123);
    	clientInquiry.setInquiryDate(new Date());
    	clientInquiry.setSourceNumber(" ");
    	clientInquiry.setInquiryStatusId(35);
    	clientInquiry.setCreateUser(1);
    	clientInquiry.setRemark("系统新增,寄卖数据");
    	clientInquiry.setUpdateTimestamp(new Date());
    	clientInquiryService.add(clientInquiry);
    	supplierCommissionSale.setClientInquiryId(clientInquiry.getId());
    	supplierCommissionSaleDao.updateByPrimaryKeySelective(supplierCommissionSale);
    	//新增供应商询价
    	SupplierInquiry supplierInquiry = new SupplierInquiry();
    	supplierInquiry.setClientInquiryId(clientInquiry.getId());
    	supplierInquiry.setSupplierId(supplierCommissionSale.getSupplierId());
    	supplierInquiry.setInquiryDate(new Date());
    	supplierInquiry.setQuoteNumber(supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierCommissionSale.getSupplierId()));
    	supplierInquiryService.insertSelective(supplierInquiry);
    	//新增供应商报价
    	//Supplier supplier = supplierDao.selectByPrimaryKey(supplierCommissionSale.getSupplierId());
    	
    	SupplierQuote supplierQuote = new SupplierQuote();
    	supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
    	supplierQuote.setCurrencyId(supplierCommissionSale.getCurrencyId());
    	ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(supplierCommissionSale.getCurrencyId());
    	supplierQuote.setExchangeRate(exchangeRate.getRate());
    	supplierQuote.setQuoteDate(new Date());
    	supplierQuote.setQuoteNumber(supplierInquiry.getQuoteNumber());
    	if (supplierCommissionSale.getValidity() != null) {
    		supplierQuote.setValidity(supplierCommissionSale.getValidity());
		}
    	supplierQuoteService.insertSelective(supplierQuote);
    	for (int i = 0; i < list.size(); i++) {
			ClientInquiryElement clientInquiryElement = addClientInquiryElement(list.get(i), clientInquiry, i+1);
			SupplierInquiryElement supplierInquiryElement = addSupplierInquiryElement(clientInquiryElement, supplierInquiry);
			SupplierQuoteElement supplierQuoteElement = addSupplierQuoteElement(supplierInquiryElement, supplierQuote, list.get(i), clientInquiryElement);
		}
    }
    
    public ClientInquiryElement addClientInquiryElement(SupplierCommissionSaleElement supplierCommissionSaleElement,ClientInquiry clientInquiry,int index){
    	ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
    	clientInquiryElement.setClientInquiryId(clientInquiry.getId());
    	clientInquiryElement.setPartNumber(supplierCommissionSaleElement.getPartNumber());
    	clientInquiryElement.setItem(index);
    	clientInquiryElement.setCsn(index);
    	clientInquiryElement.setDescription(supplierCommissionSaleElement.getDescription());
    	clientInquiryElement.setUnit(supplierCommissionSaleElement.getUnit());
    	clientInquiryElement.setAmount(supplierCommissionSaleElement.getAmount());
    	clientInquiryElement.setElementStatusId(700);
    	if (supplierCommissionSaleElement.getAlt() != null) {
    		clientInquiryElement.setAlterPartNumber(supplierCommissionSaleElement.getAlt());
		}
    	List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
	 	Element element2 = new Element();
	 	if (element.size()==0) {
	 		byte[] p = clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()).getBytes();
        	Byte[] pBytes = new Byte[p.length];
        	for(int j=0;j<p.length;j++){
        		pBytes[j] = Byte.valueOf(p[j]);
        	}
        	element2.setPartNumberCode(pBytes);
        	element2.setUpdateTimestamp(new Date());
			elementDao.insert(element2);
			clientInquiryElement.setElementId(element2.getId());
		}else {
			clientInquiryElement.setElementId(element.get(0).getId());
		}
	 	clientInquiryElement.setPartNumber(clientInquiryElement.getPartNumber().trim());
		clientInquiryElement.setUpdateTimestamp(new Date());
		clientInquiryElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
		clientInquiryElement.setSource("采购");
		clientInquiryElementDao.insertSelective(clientInquiryElement);
		return clientInquiryElement;
    }
    
    public SupplierInquiryElement addSupplierInquiryElement(ClientInquiryElement clientInquiryElement,SupplierInquiry supplierInquiry){
    	SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
    	supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
    	supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
    	supplierInquiryElement.setUpdateTimestamp(new Date());
    	supplierInquiryElementDao.insertSelective(supplierInquiryElement);
    	return supplierInquiryElement;
    }
    
    public SupplierQuoteElement addSupplierQuoteElement(SupplierInquiryElement supplierInquiryElement,SupplierQuote supplierQuote,SupplierCommissionSaleElement supplierCommissionSaleElement,ClientInquiryElement clientInquiryElement){
    	SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
    	supplierQuoteElement.setAmount(supplierCommissionSaleElement.getAmount());
    	supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
    	supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
    	supplierQuoteElement.setPartNumber(clientInquiryElement.getPartNumber());
    	supplierQuoteElement.setElementId(clientInquiryElement.getElementId());
    	supplierQuoteElement.setDescription(supplierCommissionSaleElement.getDescription());
    	supplierQuoteElement.setUnit(supplierCommissionSaleElement.getUnit());
    	if (supplierCommissionSaleElement.getPrice() != null) {
    		supplierQuoteElement.setPrice(supplierCommissionSaleElement.getPrice());
		}else {
			supplierQuoteElement.setPrice(0.01);
		}
    	if (supplierCommissionSaleElement.getRemark() != null) {
			supplierQuoteElement.setRemark(supplierCommissionSaleElement.getRemark());
		}
    	supplierQuoteElement.setLeadTime(supplierCommissionSaleElement.getLeadTime());
    	supplierQuoteElement.setConditionId(supplierCommissionSaleElement.getConditionId());
    	supplierQuoteElement.setCertificationId(supplierCommissionSaleElement.getCertificationId());
    	if (supplierCommissionSaleElement.getLocation() != null) {
    		supplierQuoteElement.setLocation(supplierCommissionSaleElement.getLocation());
		}
    	if (supplierCommissionSaleElement.getRemark() != null) {
    		supplierQuoteElement.setRemark(supplierCommissionSaleElement.getRemark());
		}
    	supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
    	if (supplierCommissionSaleElement.getCoreCharge() != null) {
    		supplierQuoteElement.setCoreCharge(supplierCommissionSaleElement.getCoreCharge());
		}
    	if (supplierCommissionSaleElement.getWarranty() != null) {
    		supplierQuoteElement.setWarranty(supplierCommissionSaleElement.getWarranty());
		}
    	if (supplierCommissionSaleElement.getSerialNumber() != null) {
    		supplierQuoteElement.setSerialNumber(supplierCommissionSaleElement.getSerialNumber());
		}
    	if (supplierCommissionSaleElement.getTagDate() != null) {
    		supplierQuoteElement.setTagDate(supplierCommissionSaleElement.getTagDate());
		}
    	if (supplierCommissionSaleElement.getTagSrc() != null) {
    		supplierQuoteElement.setTagSrc(supplierCommissionSaleElement.getTagSrc());
		}
    	if (supplierCommissionSaleElement.getFeeForExchangeBill() != null) {
    		supplierQuoteElement.setFeeForExchangeBill(supplierCommissionSaleElement.getFeeForExchangeBill());
		}
    	if (supplierCommissionSaleElement.getBankCost() != null) {
    		supplierQuoteElement.setBankCost(supplierCommissionSaleElement.getBankCost());
		}
    	if (supplierCommissionSaleElement.getHazmatFee() != null) {
    		supplierQuoteElement.setHazmatFee(supplierCommissionSaleElement.getHazmatFee());
		}
    	if (supplierCommissionSaleElement.getOtherFee() != null) {
    		supplierQuoteElement.setOtherFee(supplierCommissionSaleElement.getOtherFee());
		}
    	if (supplierCommissionSaleElement.getTrace() != null) {
    		supplierQuoteElement.setTrace(supplierCommissionSaleElement.getTrace());
		}
    	if (supplierCommissionSaleElement.getMoq() != null) {
    		supplierQuoteElement.setMoq(supplierCommissionSaleElement.getMoq());
		}
    	supplierQuoteElementDao.insertSelective(supplierQuoteElement);
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
    
    public List<SupplierCommissionSaleElement> getDistinctWithSaleId(Integer id){
    	return supplierCommissionSaleElementDao.getDistinctWithSaleId(id);
    }
    
    /**
     * 生成爬虫询价单，并爬去相应的报价信息
     * @param id
     * @return
     */
    public ResultVo AddInquiryAndCrawlElement(Integer id,Integer airType){
    	try {
			List<SupplierCommissionSaleElement> list = supplierCommissionSaleElementDao.getDistinctWithSaleId(id);
			Client client = clientService.findByCode("-1");
			List<ClientContact> ccList = clientContactDao.selectByClientId(client.getId());
			List<SystemCode> bizs = systemCodeDao.findType("BIZ_TYPE");
			List<SystemCode> airs = systemCodeDao.findType("AIR_TYPE");
			ClientInquiry clientInquiry = new ClientInquiry();
			clientInquiry.setClientId(client.getId());
			clientInquiry.setClientContactId(ccList.get(0).getId());
			clientInquiry.setBizTypeId(bizs.get(0).getId());
			clientInquiry.setAirTypeId(airType);
			clientInquiry.setInquiryDate(new Date());
			clientInquiry.setSourceNumber("storage");
			clientInquiry.setInquiryStatusId(30);
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryService.add(clientInquiry);
			for (int i = 0; i < list.size(); i++) {
				ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
				clientInquiryElement.setClientInquiryId(clientInquiry.getId());
				clientInquiryElement.setPartNumber(list.get(i).getPartNumber());
				clientInquiryElement.setUnit(list.get(i).getUnit());
				clientInquiryElement.setDescription(list.get(i).getDescription());
				clientInquiryElement.setItem(i+1);
				clientInquiryElement.setCsn(i+1);
				clientInquiryElement.setAmount(new Double(1));
				clientInquiryElement.setSource("供应商寄卖");
				List<Element> element = elementDao.findIdByPn(clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
			 	Element element2 = new Element();
			 	if (element.size()==0) {
			 		byte[] p = clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()).getBytes();
	            	Byte[] pBytes = new Byte[p.length];
	            	for(int j=0;j<p.length;j++){
	            		pBytes[j] = Byte.valueOf(p[j]);
	            	}
	            	element2.setPartNumberCode(pBytes);
	            	element2.setUpdateTimestamp(new Date());
					elementDao.insert(element2);
					clientInquiryElement.setElementId(element2.getId());
				}else {
					clientInquiryElement.setElementId(element.get(0).getId());
				}
				clientInquiryElementDao.insertSelective(clientInquiryElement);
			}
			SupplierCommissionSale supplierCommissionSale = new SupplierCommissionSale();
			supplierCommissionSale.setId(id);
			supplierCommissionSale.setCrawlClientInquiryId(clientInquiry.getId());
			supplierCommissionSale.setCrawlStatus(1);
			supplierCommissionSaleDao.updateByPrimaryKeySelective(supplierCommissionSale);
			return new ResultVo(true, "发起成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "发起异常！");
		}
    }
    
    public void getCrawlSupplierList(PageModel<SupplierCommissionSaleElement> page,GridSort sort){
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(supplierCommissionSaleElementDao.getCrawlSupplierList(page));
    }
    
    public Integer checkRecord(Integer supplierCommissionSaleElementId,Integer supplierId){
    	return supplierCommissionSaleElementDao.checkRecord(supplierCommissionSaleElementId, supplierId);
    }
    
    public List<Map<String, Object>> stockMarketCountMessage(PageModel<String> page){
    	return supplierCommissionSaleElementDao.stockMarketCountMessage(page);
    }
    
    public List<String> getSuppliers(Integer id){
    	return supplierCommissionSaleElementDao.getSuppliers(id);
    }
    
    public List<CountStockMarketVo> getSvAmount(PageModel<String> page){
    	return supplierCommissionSaleElementDao.getSvAmount(page);
    }
    
    public List<CountStockMarketVo> getOhAmount(PageModel<String> page){
    	return supplierCommissionSaleElementDao.getOhAmount(page);
    }
    
    public List<CountStockMarketVo> getNeAmount(PageModel<String> page){
    	return supplierCommissionSaleElementDao.getNeAmount(page);
    }
    
    public List<CountStockMarketVo> getArAmount(PageModel<String> page){
    	return supplierCommissionSaleElementDao.getArAmount(page);
    }
    
    public List<String> getDateAmount(PageModel<String> page){
    	return supplierCommissionSaleElementDao.getDateAmount(page);
    }
    
    public List<String> getSupplierList(Integer id){
    	return supplierCommissionSaleElementDao.getSupplierList(id);
    }
    
    public Integer checkCrawlRecord(Integer id){
    	return supplierCommissionSaleElementDao.checkCrawlRecord(id);
    }

    public void crawlStockMarket(Integer id){
		final Integer supplierCommissionId =id;
		new Thread() {
	   		 public void run() {
	   			 List<CrawerVo> list = new ArrayList<CrawerVo>();
	   			 CrawerUtil crawerStockMarket = new CrawerUtil("http://127.0.0.1:8018/crawStockMarket", list, supplierCommissionId, new Integer(0), "", "", new Integer(0));
	   			 crawerStockMarket.crawMessage();
	   		 }
	    }.start();
    }
    
    public void getStockCrawlListPage(PageModel<CountStockMarketVo> page,GridSort sort){
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(supplierCommissionSaleElementDao.getStockCrawlListPage(page));
    }
    
    public void getStockCrawlElementPage(PageModel<CountStockMarketVo> page,GridSort sort,String where){
    	if (where != null && !"".equals(where)) {
			page.put("where", where);
		}
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(supplierCommissionSaleElementDao.getStockCrawlElementPage(page));
    }
    
    public Integer checkCrawlRecordById(Integer id){
    	return supplierCommissionSaleElementDao.checkCrawlRecordById(id);
    }
    
    public void insertStockMarket(String today,String id){
    	supplierCommissionSaleElementDao.insertStockMarket(today, id);
    }
    
    public Integer getStockLastInsert(){
    	return supplierCommissionSaleElementDao.getStockLastInsert();
    }
    
    public Double getDataForPie(PageModel<String> page){
    	return supplierCommissionSaleElementDao.getDataForPie(page);
    }
    
}
