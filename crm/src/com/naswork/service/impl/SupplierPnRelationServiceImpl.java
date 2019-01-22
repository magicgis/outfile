package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierPnRelationBackUpDao;
import com.naswork.dao.SupplierPnRelationDao;
import com.naswork.dao.TManufactoryDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.TPartUploadBackupDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.SupplierPnRelationBackUp;
import com.naswork.model.SupplierPnRelationKey;
import com.naswork.model.TManufactory;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.partsinformation.SupplierAbilityVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.service.SupplierPnRelationService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Service("supplierPnRelationService")
public class SupplierPnRelationServiceImpl implements SupplierPnRelationService {
	@Resource
	private SupplierPnRelationDao supplierPnRelationDao;
	@Resource
	private UserDao userDao;
	@Resource
	private TManufactoryDao tManufactoryDao;
	@Resource
	private TPartDao tPartDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private TPartUploadBackupDao tPartUploadBackupDao;
	@Resource
	private SupplierPnRelationBackUpDao supplierPnRelationBackUpDao;
	
	
	@Override
	public void deleteByPrimaryKey(SupplierPnRelationKey key) {
		supplierPnRelationDao.deleteByPrimaryKey(key);
	}

	@Override
	public void insert(SupplierPnRelationKey record) {
		supplierPnRelationDao.insert(record);
	}

	@Override
	public void insertSelective(SupplierPnRelationKey record) {
		
	}

	@Override
	public void selectByPrimaryKeyPage(PageModel<SupplierPnRelationKey> page) {
		List<SupplierPnRelationKey> suppliers=supplierPnRelationDao.selectByPrimaryKeyPage(page);
		page.setEntities(suppliers);
		
	}

	@Override
	public List<SupplierPnRelationKey> selectByNameAndNum(String partName) {
		return supplierPnRelationDao.selectByNameAndNum(partName);
	}
	
	public List<Integer> selectByBsn(String bsn){
		return supplierPnRelationDao.selectByBsn(bsn);
	}

	@Override
	public SupplierPnRelationKey selectBybsn(String bsn,String supplierId) {
		return supplierPnRelationDao.selectBybsn(bsn,supplierId);
	}
	
	public void listPage(PageModel<SupplierAbilityVo> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(sort.getName());
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(supplierPnRelationDao.listPage(page));
	}
	
	public MessageVo excelUpload(MultipartFile multipartFile,Integer id,String supplierPnType) {
		boolean success = false;
		String message = "save unseccessful！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
		messageVo.setFlag(success);
		messageVo.setMessage(message);
		UserVo user=ContextHolder.getCurrentUser();
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
		    List<SupplierPnRelationKey> unknow = new ArrayList<SupplierPnRelationKey>();
		    int line = 2;
		    for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
		    	
		    	row = sheet.getRow(i);
	            if (row!=null) {
	            	HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
//	            	Integer sn = new Integer(dataFormatter.formatCellValue(row.getCell(0)));
			    	Cell oneCell = row.getCell(0);
			    	 TPart tPart=new  TPart();
			    	 tPart.setLine(line);
			    	 SupplierPnRelationKey record =new SupplierPnRelationKey();
//			    	 record.setSupplierPnType(Integer.parseInt(supplierPnType));
			    	 if(supplierPnType.equals("404")){//供应商能力
			    		 record.setSupplyAbility(1);
			    	 }else  if(supplierPnType.equals("405")){//维修能力
			    		 record.setRepairAbility(1);
			    	 }else  if(supplierPnType.equals("406")){//库存能力
			    		 record.setStockAbility(1);
			    	 }else  if(supplierPnType.equals("407")){//交换能力
			    		 record.setExchangeAbility(1);
			    	 }
			    	 record.setSupplierId(id);
		            String partNum = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	partNum = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						partNum = dataFormatter.formatCellValue(oneCell);
					}
		            tPart.setPartNum(partNum);
		            record.setPartNum(partNum);
		            String partName = row.getCell(1).toString();
		            record.setPartName(partName);
		            tPart.setPartName(partName);
		            String ATA = "";
		            if (row.getCell(3) != null) {
		            	ATA = row.getCell(3).toString();
		            	record.setAta(ATA);
					}
		            String Aircraft = "";
		            if(row.getCell(4) != null){
		            	Aircraft = row.getCell(4).toString();
		            	record.setAircraft(Aircraft);
		            }
		            String msn = dataFormatter.formatCellValue(row.getCell(5));
		            tPart.setCageCode(msn.split("-")[1]);
		            record.setCageCode(msn.split("-")[1]);
		            String condition = "";
		            if (row.getCell(6) != null) {
		            	condition = row.getCell(6).toString();
		            	record.setCondition(condition);
					}
		            String qty = "";
		            if (row.getCell(7) != null) {
		            	qty = row.getCell(7).toString();
		            	record.setQty(new Double(qty));
					}
		            
		            String sn = "";
		            if (row.getCell(8) != null) {
		            	sn = row.getCell(8).toString();
		            	record.setSn(sn);;
					}
		            
		            String repair = "";
		            if (row.getCell(9) != null) {
		            	repair = row.getCell(9).toString();
		            	record.setRepair(repair);;
					}
		            
		            String cert = "";
		            if (row.getCell(10) != null) {
		            	cert = row.getCell(10).toString();
		            	record.setCert(cert);;
					}
		            
		            String remark = "";
		            if (row.getCell(11) != null) {
		            	remark = row.getCell(11).toString();
		            	record.setRemark(remark);;
					}
		         
		            RoleVo role=userDao.getPower(Integer.parseInt(user.getUserId()));
		            //String msn="";
		            /*if(role.getRoleName().equals("国外采购")){
		            	msn=0+"-"+cageCode;
		            }else if(role.getRoleName().equals("国外采购")){
		            	msn=6+"-"+cageCode;
		            }else {
		            	List<String> list2=supplierDao.getRoleNameBySupplierId(id);
		            	for (String string : list2) {
							if(string.equals("国外采购")){
								msn=0+"-"+cageCode;
							}else if(string.equals("国外采购")){
								msn=6+"-"+cageCode;
							}
						}
		            	 List<TManufactory> manufactories = tManufactoryDao.selectByCageCode(cageCode);
		            	 if(manufactories.size()<=0){
		            		 tPart.setDescription("Cage Code不存在！");
		            	 }
		            }*/
		            
		            if(!"".equals(msn)){
			            List<TManufactory> tManufactory = tManufactoryDao.selectByMsn(msn);
			            if (tManufactory.size() == 0) {
			            	tPart.setDescription("Cage Code不存在！");
						}else {
							List<String> bsns = tPartDao.getBsnByPartNumAndMsn(record.getPartNum(),msn);
							if (bsns.size() > 0) {
								record.setBsn(bsns.get(0));
							}
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
		            		   if(null==tPart.getDescription()||"".equals(tPart.getDescription())){
		            			   tPart.setDescription("描述不匹配");
		            		   }
		            	}
		            }
		            
		            if(record.getBsn() != null && !"".equals(record.getBsn())){
		            	 SupplierPnRelationKey  pnRelationKeys=supplierPnRelationDao.selectBybsn(record.getBsn(),id.toString());
			 			 if(null==pnRelationKeys){
			 				 supplierPnRelationDao.insert(record);
			 			 }else{
			 				 supplierPnRelationDao.updateByPrimaryKey(record);
			 			 }
		            }else {
		            	unknow.add(record);
		            }
	            }
	            line++;
		    }
		    
		    if(unknow.size()>0){
		    		for (int i = 0; i < unknow.size(); i++) {
		    			SupplierPnRelationBackUp supplierPnRelationBackUp = new SupplierPnRelationBackUp();
		    			supplierPnRelationBackUp.setAta(unknow.get(i).getAta());
		    			supplierPnRelationBackUp.setAircraft(unknow.get(i).getAircraft());
		    			supplierPnRelationBackUp.setCondition(unknow.get(i).getCondition());
		    			supplierPnRelationBackUp.setQty(unknow.get(i).getQty());
		    			supplierPnRelationBackUp.setSupplyAbility(unknow.get(i).getSupplyAbility());
	    				supplierPnRelationBackUp.setStockAbility(unknow.get(i).getStockAbility());
	    				supplierPnRelationBackUp.setRepairAbility(unknow.get(i).getRepairAbility());
	    				supplierPnRelationBackUp.setExchangeAbility(unknow.get(i).getExchangeAbility());
	    				supplierPnRelationBackUp.setSn(unknow.get(i).getSn());
	    				supplierPnRelationBackUp.setRepair(unknow.get(i).getRepair());
	    				supplierPnRelationBackUp.setCert(unknow.get(i).getCert());
	    				supplierPnRelationBackUp.setRemark(unknow.get(i).getRemark());
	    				supplierPnRelationBackUp.setPartNumber(unknow.get(i).getPartNum());
	    				supplierPnRelationBackUp.setSupplierId(unknow.get(i).getSupplierId());
	    				supplierPnRelationBackUp.setDescription(unknow.get(i).getPartName());
		    				/*this.aircraft = supplierPnRelationKey.getAircraft();
		    				this.condition = supplierPnRelationKey.getCondition();
		    				this.qty = supplierPnRelationKey.getQty();
		    				//this.type = supplierPnRelationKey.get;
		    				this.supplyAbility = supplierPnRelationKey.getSupplyAbility();
		    				this.stockAbility = supplierPnRelationKey.getStockAbility();
		    				this.repairAbility = supplierPnRelationKey.getRepairAbility();
		    				this.exchangeAbility = supplierPnRelationKey.getExchangeAbility();
		    				this.sn = supplierPnRelationKey.getSn();
		    				this.repair = supplierPnRelationKey.getRepair();
		    				this.cert = supplierPnRelationKey.getCert();
		    				this.remark = supplierPnRelationKey.getRemark();
		    				this.partNumber = supplierPnRelationKey.getPartNum();
		    				this.description = supplierPnRelationKey.getPartName();
		    				this.supplierId = supplierPnRelationKey.getSupplierId();*/
		    			
		    			supplierPnRelationBackUp.setUserId(Integer.parseInt(user.getUserId()));
						supplierPnRelationBackUpDao.insertSelective(supplierPnRelationBackUp);
					}
		    		messageVo.setFlag(false);
		    		messageVo.setMessage("");
					return messageVo;
		    }
		    messageVo.setFlag(true);
    		messageVo.setMessage("新增成功！");
			return messageVo;
		} catch (Exception e) {
			e.printStackTrace();
			messageVo.setFlag(false);
    		messageVo.setMessage(message);
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
	public void updateByPrimaryKey(SupplierPnRelationKey record) {
		supplierPnRelationDao.updateByPrimaryKey(record);			
	}

	@Override
	public void updateByPrimarySelectiveKey(SupplierPnRelationKey record) {
		supplierPnRelationDao.updateByPrimarySelectiveKey(record);			
	}
	
	public boolean saveMatch(String[] bsn,String[] ids){
		try {
			for (int i = 0; i < ids.length; i++) {
				SupplierPnRelationBackUp supplierPnRelationBackUp = supplierPnRelationBackUpDao.selectByPrimaryKey(new Integer(ids[i]));
				SupplierPnRelationKey supplierPnRelationKey = new SupplierPnRelationKey();
				supplierPnRelationKey.setAta(supplierPnRelationBackUp.getAta());
				supplierPnRelationKey.setAircraft(supplierPnRelationBackUp.getAircraft());
				supplierPnRelationKey.setCondition(supplierPnRelationBackUp.getCondition());
				supplierPnRelationKey.setQty(supplierPnRelationBackUp.getQty());
				supplierPnRelationKey.setSupplierId(supplierPnRelationBackUp.getSupplierId());
				supplierPnRelationKey.setSn(supplierPnRelationBackUp.getSn());
				supplierPnRelationKey.setCert(supplierPnRelationBackUp.getCert());
				supplierPnRelationKey.setRemark(supplierPnRelationBackUp.getRemark());
				supplierPnRelationKey.setSupplyAbility(supplierPnRelationBackUp.getSupplyAbility());
				supplierPnRelationKey.setStockAbility(supplierPnRelationBackUp.getStockAbility());
				supplierPnRelationKey.setRepairAbility(supplierPnRelationBackUp.getRepairAbility());
				supplierPnRelationKey.setExchangeAbility(supplierPnRelationBackUp.getExchangeAbility());
				supplierPnRelationKey.setBsn(bsn[i]);
				supplierPnRelationDao.insert(supplierPnRelationKey);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
