package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientInquiryAlterElementDao;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.ElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SupplierQuoteElementUploadDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.WarnMessageDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.Element;
import com.naswork.model.ExchangeRate;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierQuoteElementUpload;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.model.WarnMessage;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierInquiryEmelentVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.pdf.AirBusPDFParser;
import com.naswork.pdf.AirBusPartParser;
import com.naswork.pdf.PDFBoxExtractor;
import com.naswork.pdf.PDFExtractor;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.GyFjService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.StringUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UploadFileVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONObject;

@Service("supplierQuoteElementService")
public class SupplierQuoteElementServiceImpl implements SupplierQuoteElementService {
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private ElementDao elementDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	protected GyFjService gyFjService;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientInquiryAlterElementDao clientInquiryAlterElementDao;
	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private SupplierQuoteElementUploadDao supplierQuoteElementUploadDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource 
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private WarnMessageDao warnMessageDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	
	@Override
	public void updateByPrimaryKey(SupplierQuoteElement record) {
		supplierQuoteElementDao.updateByPrimaryKey(record);
	}

   
    
    public ResultVo insertSelective(List<SupplierQuoteElement> list,Integer id) throws Exception {
		boolean success = false;
		String message = "";
//		Integer maxItem = supplierQuoteElementDao.findMaxItem(id)+1;
		//记录行数
		int line=1;
		int rightline=0;
		int price =0;
//		int leadtime=0;
		List<Integer> errorLine = new ArrayList<Integer>();
		List<SupplierInquiryEmelentVo> sielist=new ArrayList<SupplierInquiryEmelentVo>();
		List<SupplierQuoteElement> elementList = new ArrayList<SupplierQuoteElement>();
		List<SupplierQuoteElement> sqeList = new ArrayList<SupplierQuoteElement>();
		Map<Integer, String> map=new HashMap<Integer, String>();
			List<SupplierQuoteVo> supplierQuote=supplierQuoteElementDao.findsupplierquote(id);
//			for (SupplierQuoteVo supplierQuoteVo : supplierQuote) {
				  sielist=supplierQuoteElementDao.findsupplierinquiryelement(supplierQuote.get(0).getSupplierInquiryId(),id);
			
//			}
			List<SupplierQuoteVo> sqelist=supplierQuoteDao.findSupplierQuoteElement(id);
			
		for (SupplierQuoteElement quoteElement : list) {
			if(null==quoteElement.getPrice()||quoteElement.getPrice().equals("")){
				price++;
//				if(null==quoteElement.getLeadTime()||quoteElement.getLeadTime().equals("")){
//					leadtime++;
//				}
				line++;
	        	rightline++;
	        	continue;
			}else{
	//			SupplierInquiryEmelentVo supplierQuoteElement=new SupplierInquiryEmelentVo();
				SupplierQuoteElement Element=new SupplierQuoteElement();
				if(null!=quoteElement.getItem()&&!quoteElement.getItem().equals("")){
	//				if(quoteElement.getItem()==maxItem){
					int maxItem=supplierQuoteDao.findsupplier(supplierQuote.get(0).getClientInquiryId());
				for (SupplierInquiryEmelentVo supplierInquiryEmelentVo : sielist) {
					if(supplierInquiryEmelentVo.getItem().equals(quoteElement.getItem())&& quoteElement.getPrice() != null 
							&& quoteElement.getPrice() != 0){
						map.put(supplierInquiryEmelentVo.getItem(), supplierInquiryEmelentVo.getPartNumber());
						if(map.get(quoteElement.getItem()).equals(quoteElement.getPartNumber())){
						
						 for (SupplierQuoteVo supplierQuoteVo : sqelist) {
								if(supplierQuoteVo.getItem().equals(quoteElement.getItem())){
									errorLine.add(line);
									break;
								}
							}
						 rightline++;
	//					if(maxItem==1&&maxItem!=quoteElement.getItem()){
	//						maxItem=quoteElement.getItem();
	//					}
						quoteElement.setSupplierInquiryElementId(supplierInquiryEmelentVo.getId());
						quoteElement.setSupplierQuoteStatusId(70);
						String quotePartNumber =supplierInquiryEmelentVo.getPartNumber();
						String inquiryPartNumber =quoteElement.getPartNumber();
					if(quotePartNumber.trim().equals(inquiryPartNumber.trim())){//查询件号是否存在，没有更新明细表
						quoteElement.setElementId(supplierInquiryEmelentVo.getElementId());
					}else{
						List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(quoteElement.getPartNumber()));
						if (element.size() == 0) {
							Element.setSupplierInquiryElementId(supplierInquiryEmelentVo.getId());
							Element.setPartNumber(getCodeFromPartNumber(quoteElement.getPartNumber()));
						}else{
							quoteElement.setElementId(element.get(0).getId());
						}
					}
					
						}
							else if(!map.get(quoteElement.getItem()).equals(quoteElement.getPartNumber())){//序号0，说明是另件号
							
							for (SupplierInquiryEmelentVo supplierInquiryEmelentVo2 : sielist) {
								if(quoteElement.getItem().equals(supplierInquiryEmelentVo2.getItem())){
	//								if(quoteElement.getCsn().equals(supplierInquiryEmelentVo2.getCsn())){//匹配主件号
	//									 for (SupplierQuoteVo supplierQuoteVo : sqelist) {
	//											if(supplierQuoteVo.getItem().equals(quoteElement.getItem())){
	//												errorLine.add(line);
	//												break;
	//											}6
	//										}
										 rightline++;
	//									 if(errorLine.size()>0){
	//											break;
	//										} 
									
								            List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(quoteElement.getPartNumber()).toString());
								            Element element2 = new Element();
								            if (element.size()==0) {
								            	byte[] p = getCodeFromPartNumber(quoteElement.getPartNumber()).getBytes();
								            	Byte[] pBytes = new Byte[p.length];
								            	for(int j=0;j<p.length;j++){
								            		pBytes[j] = Byte.valueOf(p[j]);
								            	}
								            	element2.setPartNumberCode(pBytes);
								            	element2.setUpdateTimestamp(new Date());
												elementDao.insert(element2);
											}else{
												element2.setId(element.get(0).getId());
											}
								            
								         //ClientInquiryElement clientInquiryElement= clientInquiryElementDao.findByElementAndMian(element2.getId(),supplierQuote.get(0).getClientInquiryId());
								         //if(null==clientInquiryElement){
								             ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
											 List<TPart> plist=tPartDao.selectByPartNumberCode(getCodeFromPartNumber(quoteElement.getPartNumber()));
												for (TPart tPart : plist) {
													if(tPart.getIsBlacklist().equals(1)){
														clientInquiryElement.setIsBlacklist(1);
														break;
													}
												}
												clientInquiryElement.setItem(maxItem+1);
												clientInquiryElement.setCsn(supplierInquiryEmelentVo2.getCsn());
									            clientInquiryElement.setPartNumber(quoteElement.getPartNumber());
		//							            clientInquiryElement.setAlterPartNumber(alterPartNumber);
									            clientInquiryElement.setDescription(quoteElement.getDescription());
									            clientInquiryElement.setUnit(supplierInquiryEmelentVo2.getUnit());
									            clientInquiryElement.setAmount(supplierInquiryEmelentVo2.getAmount());
		//							            clientInquiryElement.setRemark(quoteElement.getRemark());
									            clientInquiryElement.setUpdateTimestamp(new Date());
									            clientInquiryElement.setClientInquiryId(supplierQuote.get(0).getClientInquiryId());
												clientInquiryElement.setElementId(element2.getId());
												clientInquiryElement.setIsDelete(0);
												clientInquiryElement.setIsMain(1);
												clientInquiryElement.setElementStatusId(701);
												clientInquiryElement.setMainId(supplierInquiryEmelentVo2.getClientInquiryElementId());
												clientInquiryElement.setShortPartNumber(getCodeFromPartNumber(quoteElement.getPartNumber()));
												clientInquiryElement.setSource("采购");
												clientInquiryElementDao.insertSelective(clientInquiryElement);//从客户询价开始，插入一条数据
								         //}
								         
								         //SupplierInquiryElement supplierInquiryElement= supplierInquiryElementDao.findByElementIdAndMian(element2.getId(),supplierQuote.get(0).getSupplierInquiryId());
								         //if(null==supplierInquiryElement){
											SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
											supplierInquiryElement.setSupplierInquiryId(supplierInquiryEmelentVo2.getSupplierInquiryId());
											supplierInquiryElement.setClientInquiryElementId(new Integer(clientInquiryElement.getId()));
											supplierInquiryElement.setUpdateTimestamp(new Date());
											supplierInquiryElementDao.insertSelective(supplierInquiryElement);//供应商询价明细
											Element.setSupplierInquiryElementId(supplierInquiryElement.getId());
											clientInquiryElement.setElementStatusId(702);
											clientInquiryElementDao.updateByPrimaryKeySelective(clientInquiryElement);
								        
								         //}
											quoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
											quoteElement.setSupplierQuoteStatusId(70);
	//										List<Element> element4 = elementDao.findIdByPn(getCodeFromPartNumber(quoteElement.getPartNumber()).toString());
	//										if (element4.size()==0) {
	//											Element.setSupplierInquiryElementId(supplierInquiryEmelentVo.getId());
	//											Element.setPartNumber(getCodeFromPartNumber(quoteElement.getPartNumber()));
	//										}else{
												quoteElement.setElementId(element2.getId());
	//										}
											 break;
							}
									 }
									 break;
								}
						break;
						}
					
					}
				quoteElement.setSupplierQuoteId(id);
				if(null!=Element.getPartNumber()){
					elementList.add(Element);
				}
	            sqeList.add(quoteElement);
				}else{
					break;
				}
			}
			
			
//			if(element.getItem()!=null){
//				if(element.getItem()==maxItem){
//					
//				}
//			}
			
			if(line!=rightline){
				errorLine.add(line);
				line++;
			}else{
			line++;
			}
		}
//			maxItem++;
			
			
			
//			}
		if (errorLine.size()>0) {
			StringBuffer error = new StringBuffer();
			error.append("第 ");
			for (Integer integer : errorLine) {
				error.append(integer).append(",");
			}
			error.deleteCharAt(error.length()-1);
			error.append(" 行有错误！");
			message = error.toString();
			throw new Exception(message);
//			return new ResultVo(success, message);
		}else {
		for (SupplierQuoteElement supplierQuoteElement : elementList) {
			if(supplierQuoteElement.getPartNumber()!=null){
				SupplierInquiryElement supplierInquiryElement = supplierInquiryElementDao.selectByPrimaryKey(supplierQuoteElement.getSupplierInquiryElementId());
				ClientInquiryElement Element = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
				supplierQuoteElementDao.insertelement(supplierQuoteElement);
				ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(Element.getId());
				if (!"".equals(Element.getElementStatusId()) && Element.getElementStatusId()!=null) {
					if (Element.getElementStatusId().equals(703) || Element.getElementStatusId()==703) {
						if (clientQuoteElement != null) {
							Element.setElementStatusId(713);
						}
					}else if (Element.getElementStatusId().equals(701) || Element.getElementStatusId()==701){
						Element.setElementStatusId(702);
					}
					clientInquiryElementDao.updateByPrimaryKeySelective(Element);
					if (Element.getMainId() !=null && !"".equals(Element.getMainId())) {
            			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(Element.getMainId());
            			if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
            				mainElement.setElementStatusId(702);
	            			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
						}
            		}else {
            			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(Element.getId());
            			for (int j = 0; j < alterList.size(); j++) {
            				if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
            					alterList.get(j).setElementStatusId(702);
	            				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
							}
            				
            			}
            		}
				}
				ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectBySupplierInuqiryElementId(supplierQuoteElement.getSupplierInquiryElementId());
				ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
				WarnMessage warnMessage = new WarnMessage();
				warnMessage.setClientInquiryId(clientInquiryElement.getClientInquiryId());
				warnMessage.setPartNumber(clientInquiryElement.getPartNumber());
				warnMessage.setReadStatus(0);
				warnMessage.setClientId(clientInquiry.getClientId());
				warnMessageDao.insertSelective(warnMessage);
//				 ElementID=supplierQuoteElement.getId();
			}
			
		}
		for (SupplierQuoteElement supplierQuoteElement : sqeList) {
			if(supplierQuoteElement.getElementId()==null){
				for (SupplierQuoteElement Element : elementList) {
					if(Element.getPartNumber().equals(getCodeFromPartNumber(supplierQuoteElement.getPartNumber()))){
						supplierQuoteElement.setElementId(Element.getId());
					}
				}
			}
			if(null!=supplierQuoteElement.getTagDate()&&supplierQuoteElement.getTagDate().equals("")){
				supplierQuoteElement.setTagDate(null);
			}
			supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
			supplierQuoteElementDao.insertSelective(supplierQuoteElement);
			clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
			SupplierInquiryElement supplierInquiryElement = supplierInquiryElementDao.selectByPrimaryKey(supplierQuoteElement.getSupplierInquiryElementId());
			ClientInquiryElement Element = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(Element.getId());
			if (!"".equals(Element.getElementStatusId()) && Element.getElementStatusId()!=null) {
				if (Element.getElementStatusId().equals(703) || Element.getElementStatusId()==703) {
					if (clientQuoteElement != null) {
						Element.setElementStatusId(713);
					}
				}else if (Element.getElementStatusId().equals(701) || Element.getElementStatusId()==701){
					Element.setElementStatusId(702);
				}
				clientInquiryElementDao.updateByPrimaryKeySelective(Element);
				if (success) {
					
				}
				if (Element.getMainId() !=null && !"".equals(Element.getMainId())) {
        			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(Element.getMainId());
        			if (mainElement.getElementStatusId() != null) {
        				if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
            				mainElement.setElementStatusId(702);
                			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
    					}
					}
        		}else {
        			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(Element.getId());
        			for (int j = 0; j < alterList.size(); j++) {
        				if (alterList.get(j).getElementStatusId() != null) {
        					if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
            					alterList.get(j).setElementStatusId(702);
                				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
    						}
						}
        			}
        		}
			}
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectBySupplierInuqiryElementId(supplierQuoteElement.getSupplierInquiryElementId());
			ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
			WarnMessage warnMessage = new WarnMessage();
			warnMessage.setClientInquiryId(clientInquiryElement.getClientInquiryId());
			warnMessage.setPartNumber(clientInquiryElement.getPartNumber());
			warnMessage.setReadStatus(0);
			warnMessage.setClientId(clientInquiry.getClientId());
			warnMessageDao.insertSelective(warnMessage);
		}
//		if(leadtime==list.size()&&price==list.size()){
//			success = false;
//			message="新增失败，单价或周期为空！";
//		}else if(leadtime==list.size()){
//			success = false;
//			message="新增失败，周期为空！";
//		}else 
			if(price==list.size()){
			success = false;
			message="新增失败，单价为空！";
		}else{
			success = true;
			message="新增成功！";
		}
		return new ResultVo(success, message);
	}
		
}

    public static String getCodeFromPartNumber(String partNumber) {
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
		}
		return buffer.toString();
	}
    public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
	}

	@Override
	public SupplierQuoteElement selectByPrimaryKey(Integer id) {
		return supplierQuoteElementDao.selectByPrimaryKey(id);
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
		
		return "SuppllierQuoteupload"+"_"+this.fetchUserName()+"_"+format.format(now);
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
		return "supplier_quote_element";
	}
    
    public String fetchYwTablePkName() {
		return "id";
	}
    
    public String fetchMappingKey() {
		return "SupplierQuoteExcel";
	}

	@Override
	public void insert(SupplierQuoteElement supplierQuoteElement) {
		supplierQuoteElementDao.insert(supplierQuoteElement);
		clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
		SupplierInquiryElement supplierInquiryElement = supplierInquiryElementDao.selectByPrimaryKey(supplierQuoteElement.getSupplierInquiryElementId());
		ClientInquiryElement Element = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
		ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(Element.getId());
		if (!"".equals(Element.getElementStatusId()) && Element.getElementStatusId()!=null) {
			if (Element.getElementStatusId().equals(703) || Element.getElementStatusId()==703) {
				if (clientQuoteElement != null) {
					Element.setElementStatusId(713);
				}
			}else if (Element.getElementStatusId().equals(701) || Element.getElementStatusId()==701){
				Element.setElementStatusId(702);
			}
			clientInquiryElementDao.updateByPrimaryKeySelective(Element);
			if (Element.getMainId() !=null && !"".equals(Element.getMainId())) {
    			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(Element.getMainId());
    			if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
    				mainElement.setElementStatusId(702);
        			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
				}
    		}else {
    			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(Element.getId());
    			for (int j = 0; j < alterList.size(); j++) {
    				if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
    					alterList.get(j).setElementStatusId(702);
        				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
					}
    			}
    		}
		}
	}

	@Override
	public List<SupplierInquiryEmelentVo> findsupplierinquiryelement(String supplierinquiryid,String supplierQuoteId) {
		return supplierQuoteElementDao.findsupplierinquiryelement(Integer.parseInt(supplierinquiryid),Integer.parseInt(supplierQuoteId));
	}

	@Override
	public List<SupplierQuoteVo> findSupplierQuoteElement(Integer id) {
		return supplierQuoteDao.findSupplierQuoteElement(id);
	}
 
	@Override
	public void updateByPrimaryKeySelective(SupplierQuoteElement record) {
		supplierQuoteElementDao.updateByPrimaryKeySelective(record);
		if (record.getUpdateUserId() != null && !"".equals(record.getUpdateUserId())) {
			SupplierQuoteElement supplierQuoteElement = supplierQuoteElementDao.selectByPrimaryKey(record.getId());
			SupplierQuote supplierQuote = new SupplierQuote();
			supplierQuote.setLastUpdateUser(record.getUpdateUserId());
			supplierQuote.setId(supplierQuoteElement.getSupplierQuoteId());
			supplierQuoteDao.updateByPrimaryKeySelective(supplierQuote);
		}
	}

	@Override
	public void updateBySupplierQuoteId(SupplierQuoteElement record) {
		supplierQuoteElementDao.updateBySupplierQuoteId(record);
	}
	
	 /*
     * excel上传
     */
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id) throws Exception{
    	boolean success = false;
		String message = "";
		InputStream fileStream = null;
		List<SupplierInquiryEmelentVo> sielist=new ArrayList<SupplierInquiryEmelentVo>();
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
			
			List<SupplierQuoteVo> supplierQuote=supplierQuoteElementDao.findsupplierquote(id);
//			for (SupplierQuoteVo supplierQuoteVo : supplierQuote) {
				  sielist=supplierQuoteElementDao.findsupplierinquiryelement(supplierQuote.get(0).getSupplierInquiryId(), id);
//			}
				  Map<Integer, String> map=new HashMap<Integer, String>();
			List<SupplierQuoteVo> sqelist=supplierQuoteDao.findSupplierQuoteElement(id);
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			 //定义行
		    Row row;
			//记录错误行数
			List<Integer> list = new ArrayList<Integer>();
			List<SupplierQuoteElement> elementList = new ArrayList<SupplierQuoteElement>();
			List<SupplierQuoteElement> sqeList = new ArrayList<SupplierQuoteElement>();
//			Integer ElementID=null;
//			Integer rows = supplierQuoteElementDao.findMaxItem(id)+1;
			List<ListDateVo> cond=supplierQuoteDao.findcond();
			List<ListDateVo> cert=supplierQuoteDao.findcert();

			//获取供应商询价单号
			int sheetmergeCount = sheet.getNumMergedRegions();
			String inquiryNumber = null;
			//循环遍历每个合并单元格
			for(int i = 0;i<sheetmergeCount;i++) {
				CellRangeAddress ca = sheet.getMergedRegion(i);
				//获取询价单号
				if (ca.getFirstRow() == 4 && ca.getLastRow() == 5) {
					Row frow = sheet.getRow(ca.getFirstRow());
					Cell cell = frow.getCell(ca.getFirstColumn());
					inquiryNumber = cell.getStringCellValue();
				}
			}
			System.out.println("询价单号"+supplierQuote.get(0).getSupplierInquiryQuoteNumber());
			System.out.println("excel询价单号"+inquiryNumber);
			MessageVo result = new MessageVo();
			if(inquiryNumber == null || "".equals(inquiryNumber)){
				success = false;
				message = "type1";
				result.setFlag(success);
				result.setMessage(message);
				return result;
			}else if (!supplierQuote.get(0).getSupplierInquiryQuoteNumber().toString().trim().equals(inquiryNumber.trim())){
				success = false;
				message = "type2";
				result.setFlag(success);
				result.setMessage(message);
				return result;
			}

			//错误行数集合
			StringBuffer lines=new StringBuffer();
			int line=0;
			int errorline=1;
			int rightline=0;
			for (int i = sheet.getFirstRowNum()+7; i < sheet.getPhysicalNumberOfRows(); i++) {
				 row = sheet.getRow(i);
				 if (row != null && row.getCell(5) != null && !"".equals(row.getCell(5).toString())) {
					 Cell p1= row.getCell(5);
						String p2= p1+"";
						if(p2.equals("")){
								line++;
					        	rightline++;
					        	errorline++;
					        	continue;
						}else{
							
			            Double a = new Double(row.getCell(0).toString());
			            Integer item = a.intValue();
			            
//			            String partNumber = row.getCell(1).toString();
			            Cell oneCell = row.getCell(1);
			            String partNumber = ""; 
			            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
							partNumber = oneCell.toString();
						}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
							HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						    partNumber = dataFormatter.formatCellValue(oneCell);
						}
//			            String alterPartNumber = row.getCell(2).toString();
 						String description = "";
			            String unit = "";
			            Double amount = null;
			            Double price = null;
			            String leadTime="";
			            String conditionCode="";
			            String certificationCode="";
						if(row.getCell(2)!=null){
							 description = row.getCell(2).toString();
						}
						if(row.getCell(3)!=null){
							 unit = row.getCell(3).toString();
						}
						if(row.getCell(4)!=null && !"".equals(row.getCell(4).toString().trim())){
							 amount = new Double(row.getCell(4).toString());
						}
						if(row.getCell(5)!=null && !"".equals(row.getCell(5).toString().trim())){
							 price = new Double(row.getCell(5).toString());
						}
						if(row.getCell(6)!=null){
							 leadTime=row.getCell(6).toString();
						}
						if(row.getCell(7)!=null){
							 conditionCode=row.getCell(7).toString();
						}
						if(row.getCell(8)!=null){
							 certificationCode=row.getCell(8).toString();
						}
//						String description = row.getCell(2).toString();
//			            String unit = row.getCell(3).toString();
//			            Double amount = new Double(row.getCell(4).toString());
//			            Double price = new Double(row.getCell(5).toString());
//			            String leadTime=row.getCell(6).toString();
//			            String conditionCode=row.getCell(7).toString();
//			            String certificationCode=row.getCell(8).toString();
//			            String feeForExchangeBill = "0";
//			            if (row.getCell(9) != null) {
//			            	HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
//							feeForExchangeBill = dataFormatter.formatCellValue(row.getCell(9));
//						}
//			            if ("".equals(feeForExchangeBill)) {
//			            	feeForExchangeBill = "0";
//						}
//			            String bankCost = "0";
//			            if (row.getCell(10) != null) {
//			            	HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
//			            	bankCost = dataFormatter.formatCellValue(row.getCell(10));
//						}
//			            if ("".equals(bankCost)) {
//			            	bankCost = "0";
//						}
			            String remark = "";
			            if (row.getCell(9) != null) {
			            	remark = row.getCell(9).toString();
						}
//			            String location = row.getCell(12).toString();
			            String smoq =row.getCell(10).toString();
			            Integer moq=null;
			            
			            if(!"".equals(smoq)&&null!=smoq){
			            	 Double dmoq = new Double(smoq);
			            	 moq= dmoq.intValue();
			            }
			            SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
			            SupplierQuoteElement Element=new SupplierQuoteElement();
			            SupplierQuoteElementUpload supplierQuoteElementUpload=new SupplierQuoteElementUpload();
			            supplierQuoteElementUpload.setSn(item);
			            supplierQuoteElementUpload.setPartNumber(partNumber);
			            supplierQuoteElementUpload.setUserId(Integer.parseInt(userId));
			            int maxItem=supplierQuoteDao.findsupplier(supplierQuote.get(0).getClientInquiryId());
			            for (SupplierInquiryEmelentVo supplierInquiryEmelentVo : sielist) {
			            	if(null!=supplierInquiryEmelentVo.getValidity()){
								 supplierQuoteElement.setValidity(supplierInquiryEmelentVo.getValidity());
								}
			            	map.put(supplierInquiryEmelentVo.getItem(), supplierInquiryEmelentVo.getPartNumber());
							if(supplierInquiryEmelentVo.getItem().equals(item)&& price != null && price != 0){
								if(map.get(item).trim().equals(partNumber.trim())){
								 for (SupplierQuoteVo supplierQuoteVo : sqelist) {
										if(supplierQuoteVo.getItem().equals(item)){
											supplierQuoteElementUpload.setMessage("已新增报价");
											supplierQuoteElementUploadDao.insert(supplierQuoteElementUpload);
											list.add(errorline);//供应商报价序号等于现在插入序号，说明序号已经插入过，错误行
											break;
										}
									}
								line++;
//								if(rows==1&&rows!=item){
//									rows=item;
//								}
								supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryEmelentVo.getId());
								supplierQuoteElement.setSupplierQuoteStatusId(70);
								String quotePartNumber =supplierInquiryEmelentVo.getPartNumber();
								String inquiryPartNumber =partNumber;
							if(quotePartNumber.trim().equals(inquiryPartNumber.trim())){//查询主键号是否有，没有更新明细表
								supplierQuoteElement.setElementId(supplierInquiryEmelentVo.getElementId());
							}else{
								List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(partNumber));
								if (element.size() == 0) {
									Element.setPartNumber(getCodeFromPartNumber(partNumber));
								}else{
									supplierQuoteElement.setElementId(element.get(0).getId());
								}
							}
						break;
								}else if(!map.get(item).trim().equals(partNumber.trim())){//序号0，说明是另件号
									 for (SupplierInquiryEmelentVo supplierInquiryEmelentVo2 : sielist) {
										 if(item.equals(supplierInquiryEmelentVo2.getItem())){
										
//											if(csn.equals(supplierInquiryEmelentVo2.getCsn())){//对比当前供应商询价单的件号的csn
//												 for (SupplierQuoteVo supplierQuoteVo : sqelist) {
//														if(supplierQuoteVo.getItem().equals(item)){
//															list.add(errorline);
//															break;
//														}
//													}
												line++;
//												if(list.size()>0){
//													break;
//												}
												   List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(partNumber));
										            Element element2 = new Element();
										            if (element.size()==0) {
										            	byte[] p = getCodeFromPartNumber(partNumber).getBytes();
										            	Byte[] pBytes = new Byte[p.length];
										            	for(int j=0;j<p.length;j++){
										            		pBytes[j] = Byte.valueOf(p[j]);
										            	}
										            	element2.setPartNumberCode(pBytes);
										            	element2.setUpdateTimestamp(new Date());
														elementDao.insert(element2);
													}
										            Element element3 = elementDao.findIdByPn(getCodeFromPartNumber(partNumber)).get(0);
										            
										            ClientInquiryElement clientInquiryElement= clientInquiryElementDao.findByElementAndMian(element3.getId(),supplierQuote.get(0).getClientInquiryId());
											         if(null==clientInquiryElement){
													  clientInquiryElement = new ClientInquiryElement();
													 List<TPart> plist=tPartDao.selectByPartNumberCode(getCodeFromPartNumber(partNumber));
														for (TPart tPart : plist) {
															if(tPart.getIsBlacklist().equals(1)){
																clientInquiryElement.setIsBlacklist(1);
																break;
															}
														}
														clientInquiryElement.setItem(maxItem+1);
														clientInquiryElement.setCsn(supplierInquiryEmelentVo2.getCsn());
											            clientInquiryElement.setPartNumber(partNumber);
			//								            clientInquiryElement.setAlterPartNumber(alterPartNumber);
											            clientInquiryElement.setDescription(description);
											            clientInquiryElement.setUnit(unit);
											            clientInquiryElement.setAmount(amount);
			//								            clientInquiryElement.setRemark(remark);
											            clientInquiryElement.setUpdateTimestamp(new Date());
											            clientInquiryElement.setClientInquiryId(supplierQuote.get(0).getClientInquiryId());
														clientInquiryElement.setElementId(element3.getId());
														clientInquiryElement.setIsDelete(0);
														clientInquiryElement.setIsMain(1);
														clientInquiryElement.setMainId(supplierInquiryEmelentVo2.getClientInquiryElementId());
														clientInquiryElement.setShortPartNumber(getCodeFromPartNumber(partNumber));
														clientInquiryElement.setSource("采购");
														clientInquiryElementDao.insertSelective(clientInquiryElement);//从客户询价开始更新
											         }
												   
											         SupplierInquiryElement supplierInquiryElement= supplierInquiryElementDao.findByElementIdAndMian(element3.getId(),supplierQuote.get(0).getSupplierInquiryId());
											         if(null==supplierInquiryElement){
													    supplierInquiryElement = new SupplierInquiryElement();
														supplierInquiryElement.setSupplierInquiryId(supplierInquiryEmelentVo2.getSupplierInquiryId());
														supplierInquiryElement.setClientInquiryElementId(new Integer(clientInquiryElement.getId()));
														supplierInquiryElement.setUpdateTimestamp(new Date());
														supplierInquiryElementDao.insertSelective(supplierInquiryElement);//更新供应商询价明细
											         }
													
													supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
													supplierQuoteElement.setSupplierQuoteStatusId(70);
													List<Element> element4 = elementDao.findIdByPn(getCodeFromPartNumber(partNumber));
													if (element4.size()==0) {
														Element.setPartNumber(getCodeFromPartNumber(partNumber));
													}else{
														supplierQuoteElement.setElementId(element4.get(0).getId());
													}
													 break;
													 
										 }
											 }
											 break;
										}
						
						}
							
						}
			            supplierQuoteElement.setPartNumber(partNumber);
//			            supplierQuoteElement.setAlterPartNumber(alterPartNumber);
			            supplierQuoteElement.setDescription(description);
			            supplierQuoteElement.setAmount(amount);
			            supplierQuoteElement.setUnit(unit);
			            supplierQuoteElement.setPrice(price);
			            supplierQuoteElement.setLeadTime(leadTime);
			            supplierQuoteElement.setRemark(remark);
//			            supplierQuoteElement.setLocation(location);
			            supplierQuoteElement.setMoq(moq);
			            supplierQuoteElement.setSupplierQuoteId(id);
			            if (success) {
							
						}
//			            supplierQuoteElement.setFeeForExchangeBill(new Double(feeForExchangeBill));
//			            supplierQuoteElement.setBankCost(new Double(bankCost));
//			           
			            for (ListDateVo listDateVo : cond) {
							if(listDateVo.getCode().equals(conditionCode)){
								supplierQuoteElement.setConditionId(listDateVo.getId());
							}
						}
			        	String in="";
			            for (ListDateVo listDateVo : cert) {
							if(listDateVo.getCode().equals(certificationCode)){
								in="right";
								supplierQuoteElement.setCertificationId(listDateVo.getId());
							}
							
						}
			            if(!in.equals("right")){
			            	list.add(errorline);
							supplierQuoteElementUpload.setMessage("证书错误");
							supplierQuoteElementUploadDao.insert(supplierQuoteElementUpload);
							}
			            
			            //存起读取的数据等待保存进数据库
			            if(null!=Element.getPartNumber()){
			            	 elementList.add(Element);
			            }
			            sqeList.add(supplierQuoteElement);
			            rightline++;
			          //如果行数与对不上保存行数
						if (errorline!=line||line!=rightline) {
							supplierQuoteElementUpload.setMessage("供应商询价没有询此件");
							supplierQuoteElementUploadDao.insert(supplierQuoteElementUpload);
							list.add(errorline);
							line++;
						}
//						rows++;
							errorline++;
						}
				 }
	        }
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (list.size()>0) {
//				lines.append("Line ");
//				for (Integer a : list) {
//					lines.append(a).append(",");
//				}
//				lines.deleteCharAt(lines.length()-1);
//				lines.append("have mistake!");
//				String result = lines.toString();
//				messageVo.setFlag(success);
//				messageVo.setMessage(result);
//				throw new Exception(result);
				messageVo.setFlag(success);
				return messageVo;
			} 	
			if (list.size()==0){
				excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());
				for (SupplierQuoteElement supplierQuoteElement : elementList) {
					if(supplierQuoteElement.getPartNumber()!=null){
						supplierQuoteElementDao.insertelement(supplierQuoteElement);
						SupplierInquiryElement supplierInquiryElement = supplierInquiryElementDao.selectByPrimaryKey(supplierQuoteElement.getSupplierInquiryElementId());
						ClientInquiryElement Element = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
						ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(Element.getId());
						if (!"".equals(Element.getElementStatusId()) && Element.getElementStatusId()!=null) {
							if (Element.getElementStatusId().equals(703) || Element.getElementStatusId()==703) {
								if (clientQuoteElement != null) {
									Element.setElementStatusId(713);
								}
							}else if (Element.getElementStatusId().equals(701) || Element.getElementStatusId()==701){
								Element.setElementStatusId(702);
							}
							clientInquiryElementDao.updateByPrimaryKeySelective(Element);
							if (Element.getMainId() !=null && !"".equals(Element.getMainId())) {
		            			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(Element.getMainId());
		            			if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
		            				mainElement.setElementStatusId(702);
			            			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
								}
		            		}else {
		            			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(Element.getId());
		            			for (int j = 0; j < alterList.size(); j++) {
		            				if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
		            					alterList.get(j).setElementStatusId(702);
			            				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
									}
		            			}
		            		}
						}
//						 ElementID=supplierQuoteElement.getId();
					}
					
				}
				for (SupplierQuoteElement supplierQuoteElement : sqeList) {
					if(supplierQuoteElement.getElementId()==null){
						for (SupplierQuoteElement Element : elementList) {
							if(Element.getPartNumber().equals(getCodeFromPartNumber(supplierQuoteElement.getPartNumber()))){
								supplierQuoteElement.setElementId(Element.getId());
							}
						}
					}
					supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
					supplierQuoteElementDao.insertSelective(supplierQuoteElement);
					clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
					SupplierInquiryElement supplierInquiryElement = supplierInquiryElementDao.selectByPrimaryKey(supplierQuoteElement.getSupplierInquiryElementId());
					ClientInquiryElement Element = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
					ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(Element.getId());
					if (!"".equals(Element.getElementStatusId()) && Element.getElementStatusId()!=null) {
						if (Element.getElementStatusId().equals(703) || Element.getElementStatusId()==703) {
							if (clientQuoteElement != null) {
								Element.setElementStatusId(713);
							}
						}else if (Element.getElementStatusId().equals(701) || Element.getElementStatusId()==701){
							Element.setElementStatusId(702);
						}
						clientInquiryElementDao.updateByPrimaryKeySelective(Element);
						if (Element.getMainId() !=null && !"".equals(Element.getMainId())) {
			    			ClientInquiryElement mainElement = clientInquiryElementDao.selectByPrimaryKey(Element.getMainId());
			    			if (mainElement.getElementStatusId().equals(701) || mainElement.getElementStatusId()==701 || mainElement.getElementStatusId().equals(700) || mainElement.getElementStatusId()==700){
			    				mainElement.setElementStatusId(702);
			        			clientInquiryElementDao.updateByPrimaryKeySelective(mainElement);
							}
			    		}else {
			    			List<ClientInquiryElement> alterList = clientInquiryElementDao.getByMainId(Element.getId());
			    			for (int j = 0; j < alterList.size(); j++) {
			    				if (alterList.get(j).getElementStatusId().equals(701) || alterList.get(j).getElementStatusId()==701 || alterList.get(j).getElementStatusId().equals(700) || alterList.get(j).getElementStatusId()==700){
			    					alterList.get(j).setElementStatusId(702);
			        				clientInquiryElementDao.updateByPrimaryKeySelective(alterList.get(j));
								}
			    			}
			    		}
					}
				}
				
				success=true;
				message="save successful!";
				messageVo.setFlag(success);
				messageVo.setMessage(message);
				return messageVo;
			}
		return null;
    }

	@Override
	public MessageVo quoteUploadExcel(MultipartFile multipartFile, String data) throws Exception {
		boolean success = false;
		String message = "";
		InputStream fileStream = null;
		List<SupplierQuoteElement> dataList=new ArrayList<SupplierQuoteElement>();
		List<Integer> cieIdList=new ArrayList<Integer>();
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		  ClientInquiryElement clientInquiryElement=new ClientInquiryElement();
		List<ListDateVo> cond=supplierQuoteDao.findcond();
		List<ListDateVo> cert=supplierQuoteDao.findcert();
	
		byte[] bytes = multipartFile.getBytes();
		String fileName = multipartFile.getOriginalFilename();
		fileStream = new ByteArrayInputStream(bytes);
		POIExcelReader reader = new POIExcelReader(fileStream, fileName);
		POIExcelWorkBook wb = reader.getWorkbook();
	    Sheet sheet = wb.getSheetAt(0);
		 //定义行
	    Row row;
	    
	    String[] supplierIds = data.split(",");
		  Row quoteNumber=sheet.getRow(4);
		  Cell quoteNumberCell=quoteNumber.getCell(0);
		  Integer clientInquiryId=clientInquiryDao.findByQuoteNumber(quoteNumberCell.toString());
		    
		  SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(clientInquiryId);
	    
	    for (int i = sheet.getFirstRowNum()+7; i < sheet.getPhysicalNumberOfRows(); i++) {
			 row = sheet.getRow(i);
			 if (row != null && row.getCell(5) != null && !"".equals(row.getCell(5).toString())) {
				 Cell p1= row.getCell(5);
					String p2= p1+"";
					if(p2.equals("")){
				        	continue;
					}else{
						  Double a = new Double(row.getCell(0).toString());
				            Integer item = a.intValue();
				            
//				            String partNumber = row.getCell(1).toString();
				            Cell oneCell = row.getCell(1);
				            String partNumber = ""; 
				            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
								partNumber = oneCell.toString();
							}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
							    partNumber = dataFormatter.formatCellValue(oneCell);
							}
//				            String alterPartNumber = row.getCell(2).toString();
				            String description = row.getCell(2).toString();
				            String unit = row.getCell(3).toString();
				            Double amount = new Double(row.getCell(4).toString());
				            Double price = new Double(row.getCell(5).toString());
				            String leadTime=row.getCell(6).toString();
				            String conditionCode=row.getCell(7).toString();
				            String certificationCode=row.getCell(8).toString();
				            String remark = row.getCell(9).toString();
				            String location = row.getCell(10).toString();
				            String smoq =row.getCell(11).toString(); 
				            Integer moq=null;
				            
				            if(!"".equals(smoq)&&null!=smoq){
				            	 Double dmoq = new Double(row.getCell(11).toString());
				            	 moq= dmoq.intValue();
				            }
				            
				          
				            clientInquiryElement.setItem(item);
				            clientInquiryElement.setClientInquiryId(clientInquiryId);
				            Integer cieid=clientInquiryElementDao.getId(clientInquiryElement);
				            clientInquiryElement=clientInquiryElementDao.selectByPrimaryKey(cieid);
				            if(!clientInquiryElement.getPartNumber().trim().equals(partNumber.trim())){
				            	List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(partNumber));
					            Element element2 = new Element();
					            if (element.size()==0) {
					            	byte[] p = getCodeFromPartNumber(partNumber).getBytes();
					            	Byte[] pBytes = new Byte[p.length];
					            	for(int j=0;j<p.length;j++){
					            		pBytes[j] = Byte.valueOf(p[j]);
					            	}
					            	element2.setPartNumberCode(pBytes);
					            	element2.setUpdateTimestamp(new Date());
									elementDao.insert(element2);
								}
					            Element element3 = elementDao.findIdByPn(getCodeFromPartNumber(partNumber)).get(0);
							 ClientInquiryElement clientInquiryElement2 = new ClientInquiryElement();
							 List<TPart> plist=tPartDao.selectByPartNumberCode(getCodeFromPartNumber(partNumber));
								for (TPart tPart : plist) {
									if(tPart.getIsBlacklist().equals(1)){
										clientInquiryElement.setIsBlacklist(1);
										break;
									}
								}
								int maxItem=supplierQuoteDao.findsupplier(clientInquiryId);
								clientInquiryElement2.setItem(maxItem+1);
								clientInquiryElement2.setCsn(item);
					            clientInquiryElement2.setPartNumber(partNumber);
//					            clientInquiryElement2.setAlterPartNumber(alterPartNumber);
					            clientInquiryElement2.setDescription(description);
					            clientInquiryElement2.setUnit(unit);
					            clientInquiryElement2.setAmount(amount);
//					            clientInquiryElement2.setRemark(remark);
					            clientInquiryElement2.setUpdateTimestamp(new Date());
					            clientInquiryElement2.setClientInquiryId(clientInquiryId);
								clientInquiryElement2.setElementId(element3.getId());
								clientInquiryElement2.setIsDelete(0);
								clientInquiryElement2.setIsMain(1);
								clientInquiryElement2.setMainId(clientInquiryElement.getId());
								clientInquiryElement2.setShortPartNumber(getCodeFromPartNumber(partNumber));
								clientInquiryElement2.setSource("采购");
								clientInquiryElementDao.insertSelective(clientInquiryElement2);
								cieid=clientInquiryElement2.getId();
								clientInquiryElement.setElementId(clientInquiryElement2.getElementId());
				            }
				            SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
				            supplierQuoteElement.setClientInquiryElementId(cieid);
				            supplierQuoteElement.setElementId(clientInquiryElement.getElementId());
				            supplierQuoteElement.setPartNumber(partNumber);
				            supplierQuoteElement.setDescription(description);
				            supplierQuoteElement.setAmount(amount);
				            supplierQuoteElement.setUnit(unit);
				            supplierQuoteElement.setPrice(price);
				            supplierQuoteElement.setLeadTime(leadTime);
				            supplierQuoteElement.setRemark(remark);
				            supplierQuoteElement.setLocation(location);
				            supplierQuoteElement.setMoq(moq);
				            supplierQuoteElement.setSupplierQuoteStatusId(70);
				            for (ListDateVo listDateVo : cond) {
								if(listDateVo.getCode().equals(conditionCode)){
									supplierQuoteElement.setConditionId(listDateVo.getId());
								}
							}
				            for (ListDateVo listDateVo : cert) {
								if(listDateVo.getCode().equals(certificationCode)){
									supplierQuoteElement.setCertificationId(listDateVo.getId());
								}
								
							}
				            
				          
				            dataList.add(supplierQuoteElement);
				            cieIdList.add(cieid);
						}
			 }
	    }
	    
		for (int k=0;k<supplierIds.length;k++) {
			if (supplierIds[k]!=null) {
				List<SupplierInquiryElement> inquiryList=new ArrayList<SupplierInquiryElement>();
				String InquiryQuoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),new Integer(supplierIds[k]));
				supplierInquiry.setQuoteNumber(InquiryQuoteNumber);
				
				clientInquiryId = supplierInquiry.getClientInquiryId();
				supplierInquiry.setUpdateTimestamp(new Date());
				supplierInquiry.setSupplierId(new Integer(supplierIds[k]));
				supplierInquiryService.insertSelective(supplierInquiry);
				
				for (int j=0;j<cieIdList.size();j++) {
					if (cieIdList.get(j)!=null) {
						SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
						supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
						supplierInquiryElement.setClientInquiryElementId(cieIdList.get(j));
						supplierInquiryElement.setUpdateTimestamp(new Date());
						supplierInquiryElementService.insertSelective(supplierInquiryElement);
						inquiryList.add(supplierInquiryElement);
					}	
				
				}
				SupplierQuote vo=new SupplierQuote();
				 int count = supplierQuoteDao.findBySupplierInquiryId(supplierInquiry.getId());
					String inquiryQuoteNumber = supplierInquiryDao.selectByPrimaryKey(supplierInquiry.getId()).getQuoteNumber();
					if (count==0) {
						vo.setQuoteNumber(inquiryQuoteNumber);
					}else {
						vo.setQuoteNumber(inquiryQuoteNumber+"-"+(count+1));
					}
					Supplier supplier=supplierDao.selectByPrimaryKey(new Integer(supplierIds[k]));
					ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
					vo.setExchangeRate(exchangeRate.getRate());
					Integer id = supplierInquiryDao.findClientInquiryId(supplierInquiry.getId());
					ClientInquiry clientInquiry =clientInquiryDao.selectByPrimaryKey(id);
					if (clientInquiry.getInquiryStatusId().equals(31) || clientInquiry.getInquiryStatusId() == 31) {
						clientInquiry.setId(id);
						clientInquiry.setInquiryStatusId(35);
						clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
					}
					vo.setSupplierInquiryId(supplierInquiry.getId());
					vo.setCurrencyId(supplier.getCurrencyId());
					 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
					 String str = sdf.format(new Date());
					 String[] date=str.split("-");
					 int year=Integer.parseInt(date[0]);
					 int month=Integer.parseInt(date[1]);
					 int day=Integer.parseInt(date[2])+30;
					 
					 if(day>30){
						 month=month+1;
						 day=day-30;
					 }
					 
					 if(month>12){
						 month=month-12;
						 year=year+1;
					 }
					 
					 Date time=sdf.parse(year+"-"+month+"-"+day);
					 
					 vo.setValidity(time);
					vo.setQuoteDate(new Date());
					vo.setUpdateTimestamp(new Date());
					supplierQuoteDao.insertSelective(vo);
					
						 for (SupplierQuoteElement supplierQuoteElement2 : dataList) {
							 for (SupplierInquiryElement inquiryElement : inquiryList) {
								 if(inquiryElement.getClientInquiryElementId().equals(supplierQuoteElement2.getClientInquiryElementId())){
								 supplierQuoteElement2.setSupplierInquiryElementId(inquiryElement.getId());
								 break;
								 }
							 }
							 supplierQuoteElement2.setSupplierQuoteId(vo.getId());
							 supplierQuoteElement2.setValidity(time);
							 supplierQuoteElement2.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement2.getPartNumber()));
							 supplierQuoteElementDao.insertSelective(supplierQuoteElement2);
							 clientInquiryElementDao.updateByPartNumber(supplierQuoteElement2.getPartNumber());
							 SupplierInquiryElement supplierInquiryElement = supplierInquiryElementDao.selectByPrimaryKey(supplierQuoteElement2.getSupplierInquiryElementId());
							 ClientInquiryElement Element = clientInquiryElementDao.selectByPrimaryKey(supplierInquiryElement.getClientInquiryElementId());
							 ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(Element.getId());
							 if (!"".equals(Element.getElementStatusId()) && Element.getElementStatusId()!=null) {
								 if (Element.getElementStatusId().equals(703) || Element.getElementStatusId()==703) {
									 if (clientQuoteElement != null) {
										 Element.setElementStatusId(713);
									 }
								 }else if (Element.getElementStatusId().equals(701) || Element.getElementStatusId()==701){
									 Element.setElementStatusId(702);
								 }
								 clientInquiryElementDao.updateByPrimaryKeySelective(Element);
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
					 }
				
			}
		}
	    
		  
//	    for (SupplierQuoteElement supplierQuoteElement : dataList) {
//	    	  supplierQuoteElement.setSupplierQuoteId(id);
//			supplierQuoteElementDao.insertSelective(supplierQuoteElement);
//		}
		MessageVo messageVo = new MessageVo();
		success=true;
		message="save successful!";
		messageVo.setFlag(success);
		messageVo.setMessage(message);
		return messageVo;
	}
	
	public SupplierQuoteElement selectBySupplierOrderElementId(Integer supplierOrderElementId){
		return supplierQuoteElementDao.selectBySupplierOrderElementId(supplierOrderElementId);
	}
	
	public Integer getTaxReimbursementId(Integer supplierQuoteElementId){
		return supplierQuoteElementDao.getTaxReimbursementId(supplierQuoteElementId);
	}
	
	public Double getLatestPrice(String partNumber){
		return supplierQuoteElementDao.getLatestPrice(partNumber);
	}



	@Override
	public MessageVo uploadPdf(MultipartFile multipartFile, Integer id) throws Exception {
		String originalFilename = multipartFile.getOriginalFilename(); // 文件全名
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1); // 后缀
		//上传文件
				String randomKey = RandomStringUtils.randomNumeric(6);
				String relativePath = "";
				Date date = new Date();
				String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
				String timeStr = new SimpleDateFormat("HHmmssSSS").format(date);
			
				relativePath = StringUtil.join(FileConstant.UPLOAD_FOLDER, "/",dateStr, "/"+suffix+"/");
				
				String realPath  = StringUtil.join(FileConstant.UPLOAD_REALPATH,relativePath);
				File saveFile = new File(realPath);
				if (!saveFile.exists()) {//目录不存在则创建
					saveFile.mkdirs();
				}
				// 图片文件名: 时间戳 + 随机串 + 高宽
				String fileName = StringUtil.join(timeStr, randomKey, "_", "_", ".", suffix);
				File file = new File(StringUtil.join(realPath, fileName));
				multipartFile.transferTo(file);//保存文件
//				AirBusPDFParser parser = new AirBusPartParser(file.getPath());
//				parser.parse(1, "G:\\CRM项目\\CRM文档\\");
				PDFExtractor extractor= new PDFBoxExtractor();
				extractor.open(file.getPath());
				String[] lines = extractor.extract(1);
				extractor.close();
				int in=0;
				SupplierQuoteElement record=new SupplierQuoteElement();
				for (String string : lines) {
					if(in==5){
						break;
					}
					if(string.indexOf("Cert Source:")>-1){
						String[] traces=string.split("Cert Source:");
						String trace=traces[1].replace("\r", "");
						record.setTrace(trace);
						in++;
					}else if(string.indexOf("Trace:")>-1){
						String[] traces=string.split("Trace:");
						String trace=traces[1].replace("\r", "");
						record.setTrace(trace);
						in++;
					}else if(string.indexOf("Traceable To.:")>-1){
						String[] traces=string.split("Traceable To.:");
						String trace=traces[1].replace("\r", "");
						record.setTrace(trace);
						in++;
					}else if(string.indexOf("Traceable ToS:")>-1){
						String[] traces=string.split("Traceable ToS:");
						String trace=traces[1].replace("\r", "");
						record.setTrace(trace);
						in++;
					}else if(string.indexOf("Serial Number:")>-1){
						String[] serialNumbers=string.split("Serial Number:");
						String serialNumber=serialNumbers[1].replace("\r", "");
						record.setSerialNumber(serialNumber);
						in++;
					}else if(string.indexOf("Tag Src:")>-1){
						String[] trgSrcs=string.split("Tag Src:");
						String trgSrc=trgSrcs[1].replace("\r", "");
						record.setTagSrc(trgSrc);
						in++;
					}else if(string.indexOf("Tag Date:")>-1){
						String[] tagDates=string.split("Tag Date:");
						String tagDate=tagDates[1].replace("\r", "");
						record.setTagDate(tagDate);
						in++;
					}else if(string.indexOf("Delivery Terms:")>-1){
						String[] leadtimes=string.split("Delivery Terms:");
						int leadtime=0;
							if(leadtimes.length>1){
							if(leadtimes[1].indexOf("DAYS")>-1){
								String[] days=leadtimes[1].split("DAYS");
								String day=days[0];
								int increasing=1;
								 char  [] stringArr =day.toCharArray();
								 for (int i = 0; i < stringArr.length; i++) {
									 	String c=String.valueOf(stringArr[stringArr.length-i-1]);
									 	if(isInteger(c)){
									 		int value=Integer.parseInt(c);
									 		value=increasing*value;
									 		leadtime+=value;
									 		increasing=increasing*10;
						            	}else if(c.equals(" ")){
						            		continue;
						            	}else{
						            		break;
						            	}
								}
								 record.setLeadTime(leadtime+"");
							}else if(leadtimes[1].indexOf("STK")>-1||leadtimes[1].indexOf("STOCK")>-1){
								 record.setLeadTime(leadtime+"");
							}
	//						String leadtime=leadtimes[0].replace("\r", "");
							
							}
						in++;
						}
				}
//				record.setId(id);
//				supplierQuoteElementDao.updateByPrimaryKeySelective(record);
				JSONObject jsonObject=JSONObject.fromObject(record);
				String json=jsonObject.toString();
			
				MessageVo messageVo=new MessageVo();
				messageVo.setFlag(true);
				messageVo.setMessage(json);
		return messageVo;
	}
	
	public static boolean isInteger(String str) {    
		 Pattern pattern = Pattern.compile("[0-9]*");   //是否是数字
		 return pattern.matcher(str).matches();    
	}  
	
	public boolean addQuote(SupplierQuoteElement supplierQuoteElement){
		try {
			List<SupplierInquiry> list = supplierInquiryDao.getByInquiryIdAndSupplierId(supplierQuoteElement.getClientInquiryId(), supplierQuoteElement.getSupplierId());
			ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(supplierQuoteElement.getClientInquiryId());
			if (clientInquiry.getInquiryStatusId().equals(30) || 30==clientInquiry.getInquiryStatusId() ||
					clientInquiry.getInquiryStatusId().equals(31) || 31==clientInquiry.getInquiryStatusId()) {
				clientInquiry.setInquiryStatusId(35);
				clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
			}
			SupplierInquiry supplierInquiry = new SupplierInquiry();
			if (list.size() == 0) {
				supplierInquiry.setClientInquiryId(supplierQuoteElement.getClientInquiryId());
				supplierInquiry.setInquiryDate(new Date());
				String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierQuoteElement.getSupplierId());
				supplierInquiry.setQuoteNumber(quoteNumber);
				supplierInquiry.setSupplierId(supplierQuoteElement.getSupplierId());
				supplierInquiryDao.insertSelective(supplierInquiry);
			}else {
				supplierInquiry = list.get(0);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new Date());
			Date today = sdf.parse(date);
			SupplierQuote supplierQuote = new SupplierQuote();
			supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
			supplierQuote.setQuoteDate(today);
			supplierQuote.setSourceNumber(supplierQuoteElement.getSourceNumber());
			supplierQuote.setCurrencyId(supplierQuoteElement.getCurrencyId());
			List<SupplierQuote> quoteList = supplierQuoteDao.getBySupplierInquiryId(supplierQuote);
			if (quoteList.size() == 0) {
				supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
				ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(supplierQuoteElement.getCurrencyId());
				supplierQuote.setCurrencyId(supplierQuoteElement.getCurrencyId());
				supplierQuote.setExchangeRate(exchangeRate.getRate());
				supplierQuote.setQuoteDate(new Date());
				supplierQuote.setCreateUser(supplierQuoteElement.getCreateUser());
				supplierQuote.setSourceNumber(supplierQuoteElement.getSourceNumber());
				//supplierQuote.setBankCost(supplierQuoteElement.getBankCost());
				//supplierQuote.setFeeForExchangeBill(supplierQuoteElement.getFeeForExchangeBill());
				supplierQuoteService.insertSelective(supplierQuote);
			}else {
				if (quoteList.get(0).getBankCost() == null) {
					quoteList.get(0).setBankCost(supplierQuoteElement.getBankCost());
				}
				if (quoteList.get(0).getFeeForExchangeBill() == null) {
					quoteList.get(0).setFeeForExchangeBill(supplierQuoteElement.getFeeForExchangeBill());
				}
				supplierQuoteDao.updateByPrimaryKeySelective(quoteList.get(0));
				supplierQuote = quoteList.get(0);
			}
			SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(supplierQuoteElement.getClientInquiryElementId());
			if (!clientInquiryElement.getPartNumber().equals(supplierQuoteElement.getPartNumber())) {
				clientInquiryElement.setMainId(clientInquiryElement.getId());
				clientInquiryElement.setIsMain(1);
				clientInquiryElement.setId(null);
				clientInquiryElement.setPartNumber(supplierQuoteElement.getPartNumber());
				List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
				int maxItem = clientInquiryElementDao.findMaxItem(clientInquiryElement.getClientInquiryId());
			 	Element element2 = new Element();
			 	if (element.size()==0) {
			 		byte[] p = getCodeFromPartNumber(clientInquiryElement.getPartNumber()).getBytes();
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
			 	clientInquiryElement.setUpdateTimestamp(new Date());
			 	clientInquiryElement.setItem(maxItem+1);
			 	clientInquiryElement.setIsMain(1);
			 	clientInquiryElement.setShortPartNumber(getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
			 	clientInquiryElement.setSource("采购");
			 	clientInquiryElementDao.insertSelective(clientInquiryElement);
			}
			List<SupplierInquiryElement> elementList = supplierInquiryElementDao.getList(clientInquiryElement.getId(), supplierInquiry.getId());
			if (elementList.size() == 0) {
				supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
				supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
				supplierInquiryElementDao.insertSelective(supplierInquiryElement);
			}else {
				supplierInquiryElement = elementList.get(0);
			}
			
			supplierQuoteElement.setSupplierQuoteStatusId(70);
			/*if (supplierQuoteElement.getUnit() == null) {
				supplierQuoteElement.setUnit(clientInquiryElement.getUnit());
			}*/
			List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
		 	Element element2 = new Element();
		 	if (element.size()==0) {
		 		byte[] p = getCodeFromPartNumber(supplierQuoteElement.getPartNumber()).getBytes();
            	Byte[] pBytes = new Byte[p.length];
            	for(int j=0;j<p.length;j++){
            		pBytes[j] = Byte.valueOf(p[j]);
            	}
            	element2.setPartNumberCode(pBytes);
            	element2.setUpdateTimestamp(new Date());
				elementDao.insert(element2);
				supplierQuoteElement.setElementId(element2.getId());
			}else {
				supplierQuoteElement.setElementId(element.get(0).getId());
			}
			//supplierQuoteElement.setAmount(clientInquiryElement.getAmount());
			supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
			supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
			supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
			supplierQuoteElementDao.insertSelective(supplierQuoteElement);
			clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByClientInquiryElementId(clientInquiryElement.getId());
			if (clientInquiryElement.getElementStatusId() != null) {
				if (clientInquiryElement.getElementStatusId().equals(703) || 703 == clientInquiryElement.getElementStatusId()) {
					if (clientQuoteElement != null) {
						clientInquiryElement.setElementStatusId(713);
					}
				}else if (clientInquiryElement.getElementStatusId().equals(700) || 700 == clientInquiryElement.getElementStatusId() ||
						clientInquiryElement.getElementStatusId().equals(701) || 701 == clientInquiryElement.getElementStatusId()) {
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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public void getByShortPartPage(PageModel<SupplierQuoteElement> page,GridSort sort){
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierQuoteElementDao.getByShortPartForHis(page));
	}
	
	public void getByShortPart(PageModel<SupplierQuoteElement> page,GridSort sort){
		if(sort!=null){
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierQuoteElementDao.getByShortPart(page));
	}
	
	public List<SupplierQuoteElement> getByOrderELementId(PageModel<String> page){
		return supplierQuoteElementDao.getByOrderELementId(page);
	}
	
	public List<SupplierQuoteElement> getCompetitorPrice(String clientId,String shortPart){
		return supplierQuoteElementDao.getCompetitorPrice(clientId, shortPart);
	}
	
	public boolean addQuotePriceInOrder(SupplierQuoteElement supplierQuoteElement,Integer clientQuoteElementId,String price,UserVo userVo){
		try {
			ClientQuoteElement clientQuoteElement = clientQuoteElementDao.selectByPrimaryKey(clientQuoteElementId);
			ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
			SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(clientInquiry.getId());
			String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierQuoteElement.getSupplierId());
			//供应商询价单
			supplierInquiry.setQuoteNumber(quoteNumber);
			supplierInquiry.setUpdateTimestamp(new Date());
			supplierInquiry.setSupplierId(supplierQuoteElement.getSupplierId());
			supplierInquiryDao.insertSelective(supplierInquiry);
			//供应商询价明细
			SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
			supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
			supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
			supplierInquiryElementService.insertSelective(supplierInquiryElement);
			//供应商报价单
			SupplierQuote supplierQuote = new SupplierQuote();
			supplierQuote.setSupplierInquiryId(supplierInquiry.getId());
			supplierQuote.setQuoteNumber(quoteNumber);
			supplierQuote.setCreateUser(new Integer(userVo.getUserId()));
			supplierQuote.setUpdateTimestamp(new Date());
			supplierQuote.setCurrencyId(supplierQuoteElement.getCurrencyId());
			supplierQuote.setQuoteDate(new Date());
			supplierQuote.setValidity(new Date());
			supplierQuote.setSourceNumber(supplierQuoteElement.getSourceNumber());
			supplierQuoteService.insertSelective(supplierQuote);
			//供应商报价明细
			supplierQuoteElement.setSupplierQuoteStatusId(70);
			List<Element> element = elementDao.findIdByPn(getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
		 	Element element2 = new Element();
		 	if (element.size()==0) {
		 		byte[] p = getCodeFromPartNumber(supplierQuoteElement.getPartNumber()).getBytes();
            	Byte[] pBytes = new Byte[p.length];
            	for(int j=0;j<p.length;j++){
            		pBytes[j] = Byte.valueOf(p[j]);
            	}
            	element2.setPartNumberCode(pBytes);
            	element2.setUpdateTimestamp(new Date());
				elementDao.insert(element2);
				supplierQuoteElement.setElementId(element2.getId());
			}else {
				supplierQuoteElement.setElementId(element.get(0).getId());
			}
			//supplierQuoteElement.setAmount(clientInquiryElement.getAmount());
			supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElement.getId());
			supplierQuoteElement.setSupplierQuoteId(supplierQuote.getId());
			supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
			supplierQuoteElementDao.insertSelective(supplierQuoteElement);
			clientInquiryElementDao.updateByPartNumber(supplierQuoteElement.getPartNumber());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public Double getCountByQuoteId(Integer quoteId){
		return supplierQuoteElementDao.getCountByQuoteId(quoteId);
	}
	
	public List<SupplierQuoteElement> getBySupplierQuoteId(Integer supplierQuoteId){
		return supplierQuoteElementDao.getBySupplierQuoteId(supplierQuoteId);
	}
	
	public List<SupplierQuoteElement> getStorage(PageModel<SupplierQuoteElement> page){
		return supplierQuoteElementDao.getStorage(page);
	}
	
	public Double getTotalByQuoteId(Integer supplierQuoteId){
		return supplierQuoteElementDao.getTotalByQuoteId(supplierQuoteId);
	}
	
	public void getHighPriceCrawl(PageModel<SupplierQuoteElement> page,GridSort sort){
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(supplierQuoteElementDao.getHighPriceCrawlPage(page));
	}
	
}
