package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientQuoteElementDao;
import com.naswork.dao.CrmStockDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.SupplierAnnualOfferDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.TManufactoryDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.TPartUploadBackupDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAnnualOffer;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.TManufactory;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.SupplierAnnualOfferService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
@Service("supplierAnnualOfferService")
public class SupplierAnnualOfferServiceImpl implements SupplierAnnualOfferService {

	@Resource
	private SupplierAnnualOfferDao supplierAnnualOfferDao;
	@Resource
	private UserDao userDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private TManufactoryDao tManufactoryDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private TPartUploadBackupDao tPartUploadBackupDao;
	@Resource
	private SupplierInquiryDao supplierInquiryDao;
	@Resource
	private SupplierInquiryElementDao supplierInquiryElementDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private CrmStockDao crmStockDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private ClientQuoteElementDao clientQuoteElementDao;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return supplierAnnualOfferDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SupplierAnnualOffer record) {
		return supplierAnnualOfferDao.insert(record);
	}

	@Override
	public int insertSelective(SupplierAnnualOffer record) {
		return supplierAnnualOfferDao.insertSelective(record);
	}

	@Override
	public SupplierAnnualOffer selectByPrimaryKey(Integer id) {
		return supplierAnnualOfferDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierAnnualOffer record) {
		return supplierAnnualOfferDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SupplierAnnualOffer record) {
		return supplierAnnualOfferDao.updateByPrimaryKey(record);
	}

	@Override
	public void annualOfferListPage(PageModel<SupplierAnnualOffer> page) {
		page.setEntities(supplierAnnualOfferDao.annualOfferListPage(page));   
	}

	public MessageVo excelUpload(MultipartFile multipartFile,Integer id,String supplierPnType) {
		boolean success = true;
		String message = "save unseccessful";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		messageVo.setFlag(success);
		messageVo.setMessage(message);
		UserVo user=ContextHolder.getCurrentUser();
		List<ListDateVo> cond=supplierQuoteDao.findcond();
		List<ListDateVo> cert=supplierQuoteDao.findcert();
		try {
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
		    //定义行
		    Row row;
		    List<TPart> list = new ArrayList<TPart>();
		    List<TPart> all = new ArrayList<TPart>();
		    List<TPart> unknow = new ArrayList<TPart>();
		    int line = 2;
		    for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
		    	
		    	row = sheet.getRow(i);
	            if (row!=null) {
	            	HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
//	            	Integer sn = new Integer(dataFormatter.formatCellValue(row.getCell(0)));
			    	Cell oneCell = row.getCell(0);
			    	 TPart tPart=new  TPart();
			    	 tPart.setLine(line);
			    	 SupplierAnnualOffer record =new SupplierAnnualOffer();
//			    	 record.setSupplierPnType(Integer.parseInt(supplierPnType));
			    	
			    	 record.setSupplierId(id);
		            String partNum = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	partNum = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						partNum = dataFormatter.formatCellValue(oneCell);
					}
		            tPart.setPartNum(partNum);
		            record.setPartNumber(partNum);
		            String partName = row.getCell(1).toString();
		            record.setDesc(partName);
		            tPart.setPartName(partName);
		            String unit = row.getCell(2).toString();
		            record.setUnit(unit);
		         
		            String cageCode = row.getCell(3).getStringCellValue();
		            tPart.setCageCode(cageCode);
		            
		            String amount = "";
		            if (row.getCell(4) != null) {
		            	amount = row.getCell(4).toString();
		            	if(!"".equals(amount)){
		            		record.setAmount(new Double(amount));
		            	}
					}
		         
		            String price = "";
		            if (row.getCell(5) != null) {
		            	price = row.getCell(5).toString();
		            	if(!"".equals(price)){
		            		record.setPrice(new Double(price));
		            	}
					}
		            
		            String leadtime = "";
		            if (row.getCell(6) != null) {
		            	leadtime = dataFormatter.formatCellValue(row.getCell(6));
		            	record.setLeadTime(leadtime);
					}
		            
		            String condition = "";
		            if (row.getCell(7) != null) {
		            	condition = row.getCell(7).toString();
		            	boolean in=false;
		            	 for (ListDateVo listDateVo : cond) {
								if(listDateVo.getCode().equals(condition)){
									record.setConditionId(listDateVo.getId());
									in=true;
								}
							}
		            	 if(!in){
		            			tPart.setDescription("状态错误");
		            	  }
					}
		            
		            String certificationCode = "";
		            if (row.getCell(8) != null) {
		            	certificationCode = row.getCell(8).toString();
		            	boolean in=false;
		            	  for (ListDateVo listDateVo : cert) {
								if(listDateVo.getCode().equals(certificationCode)){
									record.setCertificationId(listDateVo.getId());
									in=true;
								}
							}
		            	  if(!in){
		            			tPart.setDescription("证书错误");
		            	  }
					}
		            
		            String remark = "";
		            if (row.getCell(9) != null) {
		            	remark = row.getCell(9).toString();
		            	record.setRemark(remark);;
					}
		            
		            String location = "";
		            if (row.getCell(10) != null) {
		            	location = row.getCell(10).toString();
		            	record.setLocation(location);
					}
		            
		            String moq = "";
		            if (row.getCell(11) != null) {
		            	moq = dataFormatter.formatCellValue(row.getCell(11));
		            	if(!"".equals(moq)){
		            		record.setMoq(Integer.parseInt(moq));
		            	}
					}
		            
		            String validity = "";
		            if (row.getCell(12) != null) {
		            	
		            	  DecimalFormat df = new DecimalFormat("#");
		                 
		              Cell cellvalue = row.getCell(12);
		            	if(!"".equals(cellvalue.toString())){
		            		if(cellvalue.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
		            			 if(HSSFDateUtil.isCellDateFormatted(cellvalue)){
			                          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			                          validity=sdf.format(HSSFDateUtil.getJavaDate(cellvalue.getNumericCellValue()));
			                      }
		            		}else if(cellvalue.getCellType()==HSSFCell.CELL_TYPE_STRING){
		            			validity=cellvalue.getStringCellValue();
		            		}else if(cellvalue.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
		            			validity=cellvalue.getCellFormula();
		            		}
		            		
		            		
		            		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		            		record.setValidity(dateFormat.parse(validity));
		            	}
					}
		            
		            RoleVo role=userDao.getPower(Integer.parseInt(user.getUserId()));
		            String msn="";
		            if(role.getRoleName().equals("国外采购")){
		            	msn=0+"-"+cageCode;
		            }else if(role.getRoleName().equals("国内采购")){
		            	msn=6+"-"+cageCode;
		            }else {
		            	List<String> list2=supplierDao.getRoleNameBySupplierId(id);
		            	for (String string : list2) {
							if(string.equals("国外采购")){
								msn=0+"-"+cageCode;
							}else if(string.equals("国内采购")){
								msn=6+"-"+cageCode;
							}
						}
		            	 List<TManufactory> manufactories = tManufactoryDao.selectByCageCode(cageCode);
		            	 if(manufactories.size()<=0){
		            		 tPart.setDescription("Cage Code不存在！");
		            	 }
		            }
		            record.setMsn(msn);
		            if(!"".equals(msn)){
			            List<TManufactory> tManufactory = tManufactoryDao.selectByMsn(msn);
			            if (tManufactory.size() == 0) {
			            	tPart.setDescription("Cage Code不存在！");
						}  
		            }
		            
		            List<TPart> parts= tPartDao.getTPart(tPart);
		            if(parts.size()<=0){
		            	  tPart.setPartNum(getCodeFromPartNumber(tPart.getPartNum())); 
				          parts= tPartDao.getTPartByShort(tPart);
		            }
		            if(parts.size()<=0){
		            	 if(null==tPart.getDescription()||"".equals(tPart.getDescription())){
		            		   	tPart.setDescription("Cage Code下没有此件号");
		            	   }
		            }
		            if(parts.size()>0){
		            	
		          	boolean in=false;
		            	for (TPart part : parts) {
							if(part.getPartName().toUpperCase().indexOf(partName.toUpperCase())>-1){
								record.setBsn(part.getBsn());
								in=true;
								break;
							}
						}
		            	
		            	if(!in){
		            		record.setBsn(parts.get(0).getBsn());
		            	}  /**/
		            }
		            
		            if(null!=tPart.getDescription()&&!"".equals(tPart.getDescription())){
		            	 unknow.add(tPart);
		            }else {
		            	List<SupplierAnnualOffer>  annualOffer=supplierAnnualOfferDao.selectByBn(record.getBsn());
		 				if(null==annualOffer){
		 					supplierAnnualOfferDao.insert(record);
		 				}else{
		 					boolean in =false;
		 					for (SupplierAnnualOffer supplierAnnualOffer : annualOffer) {
								if(supplierAnnualOffer.getSupplierId().equals(id)||supplierAnnualOffer.getSupplierId()==id){
									record.setId(supplierAnnualOffer.getId());
									supplierAnnualOfferDao.updateByPrimaryKey(record);
									in=true;
								}
							}
		 					if(!in){
		 						supplierAnnualOfferDao.insert(record);
		 					}
		 					
		 				}
		            
		            }
	            }
	            line++;
		    }
		    
		    if(unknow.size()>0){
		    		for (int i = 0; i < unknow.size(); i++) {
						unknow.get(i).setUserId(Integer.parseInt(user.getUserId()));
						tPartUploadBackupDao.insertSelective(unknow.get(i));
					}
		    		messageVo.setFlag(false);
		    		messageVo.setMessage("");
					return messageVo;
		    }
		  
		} catch (Exception e) {
			messageVo.setFlag(false);
    		messageVo.setMessage("save unsuccessful!");
			e.printStackTrace();
			
		}
		return messageVo;
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
		public void addSupplierQuote(ClientInquiryElement clientInquiryElement) throws ParseException {
			
			if(null!=clientInquiryElement.getBsn()&&!"".equals(clientInquiryElement.getBsn())){
				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");  
				 List<SupplierAnnualOffer> annualOffers=supplierAnnualOfferDao.selectByBn(clientInquiryElement.getBsn());
			if(null!=annualOffers&&annualOffers.size()>0){
			for (SupplierAnnualOffer supplierAnnualOffer : annualOffers) {
					if(null!=supplierAnnualOffer.getValidity()){
//						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
						Date validity=supplierAnnualOffer.getValidity();
						Date now =new Date();
						if(validity.getTime()<now.getTime()){ 
							continue;
						}
					}else{
						continue;
					}
						
				Supplier supplier=supplierDao.selectByPrimaryKey(supplierAnnualOffer.getSupplierId());
					SupplierInquiry supplierinquiry=supplierInquiryDao.findSupplierByImportPackage(clientInquiryElement.getClientInquiryId(), supplierAnnualOffer.getSupplierId());
					ClientInquiry clientInquiry=clientInquiryDao.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
					if(null==supplierinquiry){
						supplierinquiry=new SupplierInquiry();
						Integer id=clientInquiryElement.getClientInquiryId();
						supplierinquiry.setClientInquiryId(id);
						supplierinquiry.setSupplierId(supplierAnnualOffer.getSupplierId());
						supplierinquiry.setInquiryDate(clientInquiry.getInquiryDate());
						String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierinquiry.getInquiryDate(),supplierAnnualOffer.getSupplierId());
						supplierinquiry.setQuoteNumber(quoteNumber);
						supplierinquiry.setDeadline(clientInquiry.getDeadline());
						supplierinquiry.setRemark(clientInquiry.getRemark());
						supplierInquiryDao.insertSelective(supplierinquiry);
					}
					
					Integer supplierInquiryId = supplierinquiry.getId();
					SupplierInquiryElement supplierInquiryElement=new SupplierInquiryElement();
					supplierInquiryElement.setSupplierInquiryId(supplierInquiryId);
					supplierInquiryElement.setClientInquiryElementId(clientInquiryElement.getId());
					supplierInquiryElementDao.insertSelective(supplierInquiryElement);
					
					Integer supplierInquiryElementId =supplierInquiryElement.getId();
					Integer currencyId = supplier.getCurrencyId();
					ExchangeRate rate=exchangeRateDao.selectByPrimaryKey(currencyId);
					Double exchangeRate =rate.getRate();
					
					SupplierQuote supplierQuote=supplierQuoteDao.findSupplierInquiry(supplierInquiryId);
					if(null==supplierQuote){
						supplierQuote=new SupplierQuote();
						supplierQuote.setCurrencyId(currencyId);
						supplierQuote.setExchangeRate(exchangeRate);
						supplierQuote.setSupplierInquiryId(supplierInquiryId);
						Calendar c=Calendar.getInstance();
						int year=c.get(Calendar.YEAR);
						String data=year+"-1"+"-1";
						Date day=dateFormat.parse(data);
						supplierQuote.setQuoteDate(day);
						supplierQuote.setQuoteNumber(supplierinquiry.getQuoteNumber());
						supplierQuote.setValidity(supplierAnnualOffer.getValidity());
						supplierQuoteDao.insertSelective(supplierQuote);
					}
					Integer supplierQuoteId =supplierQuote.getId();
					
					SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
					supplierQuoteElement.setSupplierQuoteId(supplierQuoteId);
					supplierQuoteElement.setSupplierInquiryElementId(supplierInquiryElementId);
					supplierQuoteElement.setElementId(clientInquiryElement.getElementId());
					supplierQuoteElement.setPartNumber(supplierAnnualOffer.getPartNumber());
					supplierQuoteElement.setDescription(supplierAnnualOffer.getDesc());
					supplierQuoteElement.setAmount(supplierAnnualOffer.getAmount());
					supplierQuoteElement.setUnit(supplierAnnualOffer.getUnit());
					supplierQuoteElement.setPrice(supplierAnnualOffer.getPrice());
					supplierQuoteElement.setLeadTime(supplierAnnualOffer.getLeadTime());
					supplierQuoteElement.setRemark(supplierAnnualOffer.getRemark());
					supplierQuoteElement.setConditionId(supplierAnnualOffer.getConditionId());
					supplierQuoteElement.setCertificationId(supplierAnnualOffer.getCertificationId());
					supplierQuoteElement.setSupplierQuoteStatusId(70);
					supplierQuoteElement.setValidity(supplierAnnualOffer.getValidity());
					supplierQuoteElement.setLocation(supplierAnnualOffer.getLocation());
					supplierQuoteElement.setMoq(supplierAnnualOffer.getMoq());
					supplierQuoteElement.setShortPartNumber(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
					supplierQuoteElementDao.insertSelective(supplierQuoteElement);
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
					}
				}
			
			}
			
		}
}
