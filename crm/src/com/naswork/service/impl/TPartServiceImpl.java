package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.naswork.dao.PartTypeSubsetDao;
import com.naswork.dao.TManufactoryDao;
import com.naswork.dao.TPartDao;
import com.naswork.dao.TPartUploadBackupDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.CrmStock;
import com.naswork.model.PartTypeSubset;
import com.naswork.model.SystemCode;
import com.naswork.model.TManufactory;
import com.naswork.model.TPart;
import com.naswork.model.TPartUploadBackup;
import com.naswork.module.marketing.controller.clientinquiry.BlackList;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.TPartService;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;

@Service("tPartService")
public class TPartServiceImpl implements TPartService {

	@Resource
	private TPartDao tPartDao;
	@Resource 
	private TManufactoryDao tManufactoryDao;
	@Resource
	private TPartUploadBackupDao tPartUploadBackupDao;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private PartTypeSubsetDao partTypeSubsetDao;
	
	@Override
	public int insertSelective(TPart record) {
		/*TManufactory tManufactory = tManufactoryDao.selectByMsn(record.getMsn());
		if (record.getMsn()==null || "".equals(record.getMsn())) {
			record.setMsn(tManufactory.getMsn());
		}*/
		/*if (record.getWeightUnit() != null && record.getWeight() != null) {
			record.setWeight(record.getWeight()+record.getWeightUnit());
		}
		if (record.getDimentions() != null && record.getDimentionsUnit() != null) {
			record.setDimentions(record.getDimentions()+record.getDimentionsUnit());
		}*/
		record.setCageCode(record.getMsn().substring(2));
		record.setMsnFlag(record.getMsn().substring(0, 1));
		String shortPart = getCodeFromPartNumber(record.getPartNum());
		List<String> list = tPartDao.getBsn(shortPart);
		String sequence = String.format("%0"+4+"d", list.size());
		StringBuffer bsn = new StringBuffer();
		bsn.append(record.getMsn()).append("-").append(sequence).append("-").append(shortPart);
		record.setBsn(bsn.toString());
		record.setShortPartNum(shortPart);
		return tPartDao.insertSelective(record);
	}

	@Override
	public TPart selectByPrimaryKey(String bsn) {
		return tPartDao.selectByPrimaryKey(bsn);
	}

	@Override
	public int updateByPrimaryKeySelective(TPart record) {
		//Integer partType = null;
		//List<PartTypeSubset> list = partTypeSubsetDao.selectByCode(code)
		//list tManufactory = tManufactoryDao.getMsnByCageCode(record.getCageCode());
		//record.setMsn(tManufactory.getMsn());
		if (record.getWeight() == null || "".equals(record.getWeight())) {
			record.setWeightUnitId(null);
		}
		if (record.getDimentions() == null || "".equals(record.getDimentions())) {
			record.setDimentionsUnitId(null);
		}
		if (record.getCorrelationBsn() != null && !"".equals(record.getCorrelationBsn())) {
			TPart tPart = tPartDao.selectByPrimaryKey(record.getCorrelationBsn());
			TPart tPartb = tPartDao.selectByPrimaryKey(record.getBsn());
			if (tPart != null) {
				if (tPart.getCorrelationMark() != null && tPartb.getCorrelationMark() != null) {
					List<CrmStock> list = tPartDao.selectByMark(tPart.getCorrelationMark());
					TPart part = new TPart();
					for (int i = 0; i < list.size(); i++) {
						part.setBsn(list.get(i).getBsn());
						part.setCorrelationMark(tPartb.getCorrelationMark());
						tPartDao.updateByPrimaryKeySelective(part);
					}
				}else{
					if (tPart.getCorrelationMark() != null && !"".equals(tPart.getCorrelationMark())) {
						record.setCorrelationMark(tPart.getCorrelationMark());
					}else if (tPartb.getCorrelationMark() != null && !"".equals(tPartb.getCorrelationMark())) {
						tPart.setCorrelationMark(tPartb.getCorrelationMark());
					}else {
						Integer maxSeq = tPartDao.getMaxCorrelationMark();
						if (maxSeq != null) {
							tPart.setCorrelationMark(maxSeq+1);
							record.setCorrelationMark(maxSeq+1);
						}else {
							tPart.setCorrelationMark(1);
							record.setCorrelationMark(1);
						}
					}
					tPartDao.updateByPrimaryKeySelective(tPart);
				}
			}
		}
		record.setCageCode(record.getMsn().substring(2));
		String shortPart = getCodeFromPartNumber(record.getPartNum());
		List<String> list = tPartDao.getBsn(shortPart);
		String sequence = String.format("%0"+4+"d", list.size());
		record.setShortPartNum(shortPart);
		return tPartDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<String> getBsn(String partNumberCode) {
		return tPartDao.getBsn(partNumberCode);
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
		return buffer.toString().replaceAll("Ω", "");
    }
	
	public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
    }
	
	public MessageVo excelUpload(MultipartFile multipartFile,Integer userId) {
		boolean success = false;
		String message = "save seccessful！！";
		InputStream fileStream = null;
		MessageVo messageVo = new MessageVo();
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
	            	Integer sn = new Integer(dataFormatter.formatCellValue(row.getCell(0)));
			    	Cell oneCell = row.getCell(1);
		            String partNum = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
		            	partNum = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						partNum = dataFormatter.formatCellValue(oneCell);
					}
		            if(partNum.startsWith("科学")){
		            	partNum=partNum.replace("科学", "");
		            }
		            String partName = row.getCell(2).toString();
		          
		            String cageCode = dataFormatter.formatCellValue(row.getCell(3));
		            String msn = "";
		            String msnFlag = "";
		            if (row.getCell(4) != null) {
						msnFlag = dataFormatter.formatCellValue(row.getCell(4));
						
						msn = msnFlag+"-"+cageCode;
					}
		            String nsn = "";
		            if(row.getCell(5) != null){
		            	nsn = row.getCell(5).toString();
		            }
		            String replacedNsn = "";
		            if (row.getCell(6) != null) {
		            	replacedNsn = row.getCell(6).toString();
					}
		            String weight = "";
		            if (row.getCell(7) != null) {
		            	weight = row.getCell(7).toString();
					}
		            Integer weightUnit = null;
		            if (row.getCell(8) != null && !"".equals(weight)) {
		            	String weightunit = row.getCell(8).toString();
		            	if (weightunit != null && !"".equals(weightunit)) {
							List<SystemCode> sysList = tPartDao.getSystemCodeByValue(weightunit);
							if (sysList.size() > 0) {
								weightUnit = sysList.get(0).getId();
							}
						}
					}
		            String dimentions = "";
		            if (row.getCell(9) != null) {
		            	dimentions = row.getCell(9).toString();
					}
		            Integer dimentionsUnit = null;
		            if (row.getCell(10) != null && !"".equals(dimentions)) {
		            	String dimentionsunit = row.getCell(10).toString();
		            	if (dimentionsunit != null && !"".equals(dimentionsunit)) {
							List<SystemCode> sysList = tPartDao.getSystemCodeByValue(dimentionsunit);
							if (sysList.size() > 0) {
								dimentionsUnit = sysList.get(0).getId();
							}
						}
					}
		            String countryOfOrigin = "";
		            if (row.getCell(11) != null) {
		            	countryOfOrigin = row.getCell(9).toString();
		            	if (countryOfOrigin.length() > 10) {
							int a = 1;
							a = 3;
						}
					}
		            String eccn = "";
		            if (row.getCell(12) != null) {
		            	eccn = row.getCell(12).toString();
					}
		            String scheduleBCode = "";
		            if (row.getCell(13) !=  null) {
		            	if (!"".equals(row.getCell(13).toString())) {
		            		Cell secCell = row.getCell(13);
				            if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				            	scheduleBCode = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								scheduleBCode = dataFormatter.formatCellValue(secCell);
							}
						}
					}
		            Integer shelfLife = null;
		            if (row.getCell(14) != null) {
		            	if (!"".equals(row.getCell(14).toString().trim())) {
		            		shelfLife = new Integer(dataFormatter.formatCellValue(row.getCell(14)));
						}
					}
		            Integer ataChapterSection = null;
		            if (row.getCell(15) != null) {
			            if (!"".equals(row.getCell(15).toString().trim())) {
			            	ataChapterSection = new Integer(dataFormatter.formatCellValue(row.getCell(15)));
						}
		            }
		            Integer categoryNo = null;
		            if (row.getCell(16) != null) {
			            if (!"".equals(row.getCell(16).toString().trim())) {
			            	categoryNo = new Integer(dataFormatter.formatCellValue(row.getCell(16)));
						}
		            }
		            String usml = "";
		            if (row.getCell(17) != null) {
		            	usml = row.getCell(17).toString();
					}
		            String hazmatCode = "";
		            if (row.getCell(18) != null) {
		            	hazmatCode = row.getCell(18).toString();
					}
		            String imgPath = "";
		            if (row.getCell(19) != null) {
		            	imgPath = row.getCell(19).toString();
					}
		            String hsCode = "";
		            if (row.getCell(21) != null) {
		            	hsCode = dataFormatter.formatCellValue(row.getCell(21));
					}
		            
		            TPart tPart = new TPart(partNum, partName, line, cageCode, nsn, replacedNsn, weight, dimentions, countryOfOrigin, eccn, scheduleBCode, shelfLife, ataChapterSection, categoryNo, usml, hazmatCode, imgPath,msn);
		            if (weightUnit != null) {
		            	tPart.setWeightUnitId(weightUnit);
					}
		            if (dimentionsUnit != null) {
						tPart.setDimentionsUnitId(dimentionsUnit);
					}
		            if (row.getCell(23) != null) {
		            	tPart.setRemark(dataFormatter.formatCellValue(row.getCell(23)));
					}
		            tPart.setHsCode(hsCode);
		            Integer partType = null;
		            if (row.getCell(20) != null) {
		            	if (!"".equals(row.getCell(20).toString())) {
		            		String partT = "";
			            	Cell secCell = row.getCell(20);
				            if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				            	partT = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								partT = dataFormatter.formatCellValue(secCell);
							}
				            List<PartTypeSubset> partTypeSubsets = partTypeSubsetDao.selectByCode(partT);
				            partType = partTypeSubsets.get(i).getId();
				            if (partType != null) {
				            	StringBuffer desc = new StringBuffer();
								desc.append(tPart.getDescription()).append(",").append("没有匹配类型！");
								tPart.setDescription(desc.toString());
							}else {
								tPart.setPartType(partType);
							}
						}
					}
		            if (row.getCell(22) != null) {
		            	if (!"".equals(row.getCell(22).toString())) {
		            		String black = dataFormatter.formatCellValue(row.getCell(22));
		            		tPart.setIsBlacklist(new Integer(black));
						}
		            	
					}
		            List<TManufactory> tManufactory = tManufactoryDao.selectByMsn(tPart.getMsn());
		            if (tManufactory.size() == 0) {
		            	tPart.setDescription("MSN不存在！");
					}
		            tPart.setMsnFlag(msnFlag);
		            String[] des = partName.split(",");
		            List<TPart> parts = tPartDao.getTPart(tPart);
		            int ifMatch = 0;
		            for (int j = 0; j < parts.size(); j++) {
						String[] nameArray = parts.get(j).getPartName().split(",");
						for (int k = 0; k < nameArray.length; k++) {
							for (int k2 = 0; k2 < des.length; k2++) {
								if (nameArray[k].toLowerCase().equals(des[k2].toLowerCase())) {
									ifMatch = 1;
								}
							}
						}
						if (ifMatch == 0) {
							StringBuffer name = new StringBuffer();
			            	if (parts.get(j).getPartName()!=null && !"".equals(parts.get(j).getPartName())) {
			            		name.append(parts.get(j).getPartName()).append(",");
							}
			            	if (parts.get(j).getPartName()!=null && !"".equals(parts.get(j).getPartName())) {
			            		name.append(partName);
							}
			            	parts.get(j).setPartName(name.toString());
			            	tPartDao.updateByPrimaryKeySelective(parts.get(j));
			            	
						}
					}
		            /*if (ifMatch==0) {
		            	StringBuffer desc = new StringBuffer();
		            	if (tPart.getDescription()!=null && !"".equals(tPart.getDescription())) {
		            		desc.append(tPart.getDescription()).append(",");
						}
		            	desc.append("件号已存在但描述不一样！");
		            	tPart.setDescription(desc.toString());
					}*/
		            /*if (partType != null) {
		            	StringBuffer desc = new StringBuffer();
						desc.append(tPart.getDescription()).append(",").append("没有匹配机型！");
						tPart.setDescription(desc.toString());
		            	tPart.setPartType(partType);
					}else {
						;
					}*/
		            ifMatch = 0;
		            List<Integer> shelfLifes = tPartDao.getShelfLife(tPart);
		            for (int j = 0; j < shelfLifes.size(); j++) {
						if (shelfLifes.get(j).equals(shelfLife)) {
							ifMatch = 1;
						}
					}
		            /*if (ifMatch==0) {
		            	StringBuffer desc = new StringBuffer();
		            	if (tPart.getDescription()!=null && !"".equals(tPart.getDescription())) {
		            		desc.append(tPart.getDescription()).append(",");
						}
		            	desc.append("ShelfLife不匹配！");
		            	tPart.setDescription(desc.toString());
					}*/
		            List<String> bsns = tPartDao.selectByPartNumAndCageCode(tPart.getPartNum(), tPart.getCageCode(),tPart.getMsnFlag());
		            if (bsns.size() > 0) {
		            	TPart tPart2 = tPartDao.selectByPrimaryKey(bsns.get(0));
		            	if (tPart2.getCorrelationMark() != null && !"".equals(tPart2.getCorrelationMark())) {
							tPart.setCorrelationMark(tPart2.getCorrelationMark());
						}
					}
		            tPart.setSn(sn);
		            all.add(tPart);
		            if (parts.size() == 0) {
		            	if (tPart.getDescription()==null || "".equals(tPart.getDescription())) {
			            	list.add(tPart);
						}else {
							unknow.add(tPart);
						}
					}
		            
		            
	            }
	            line++;
		    }
		    if (unknow.size()!=0) {
				for (int i = 0; i < unknow.size(); i++) {
					unknow.get(i).setUserId(userId);
					tPartUploadBackupDao.insertSelective(unknow.get(i));
				}
				return new MessageVo(false, message);
			}else {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getCorrelationMark() != null && !"".equals(list.get(i).getCorrelationMark())) {
						for (int j = 0; j < list.size(); j++) {
							if (list.get(i).getSn().equals(list.get(j).getSn()) && i != j) {
								list.get(j).setCorrelationMark(list.get(i).getCorrelationMark());
							}
						}
					}
				}
				for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getCorrelationMark() != null) {
							List<String> bsns = tPartDao.selectByPartNumAndCageCode(list.get(i).getPartNum(), list.get(i).getCageCode(),list.get(i).getMsnFlag());
							if (bsns.size() == 0) {
								insertSelective(list.get(i));
							}
						}else {
							Integer seq = tPartDao.getMaxCorrelationMark();
							for (int j = 0; j < list.size(); j++) {
								if (list.get(i).getSn().equals(list.get(j).getSn()) && i != j) {
									if (list.get(i).getCorrelationMark() != null) {
										list.get(j).setCorrelationMark(list.get(i).getCorrelationMark());
									}else {
										if (seq != null) {
											list.get(i).setCorrelationMark(seq + 1);
											list.get(j).setCorrelationMark(seq + 1);
										}else {
											list.get(i).setCorrelationMark(1);
											list.get(j).setCorrelationMark(1);
										}
									}
									
								}
							}
							insertSelective(list.get(i));
						}
				}
//				for (int i = 0; i < list.size(); i++) {
//					List<String> bsns = tPartDao.selectByPartNumAndCageCode(list.get(i).getPartNum(), list.get(i).getCageCode(),list.get(i).getMsnFlag());
//	            	if (bsns.size() == 0) {
//	            		insertSelective(list.get(i));
//	            	}else {
//	            		list.get(i).setBsn(bsns.get(0));
//					}
//				}
				success = true;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "save unseccessful！";
		}
		return new MessageVo(success, message);
	}
	
	public void listPage(PageModel<TPartUploadBackup> page){
		page.setEntities(tPartUploadBackupDao.findByUserIdPage(page));
    }
	
	public void delete(Integer userId){
		tPartUploadBackupDao.delete(userId);
	}

	@Override
	public List<TPart> selectByPartNumberCode(String partNumberCode) {
		return tPartDao.selectByPartNumberCode(partNumberCode);
	}

	@Override
	public void blacklist(PageModel<BlackList> page) {
		page.put("partNumberCode", getCodeFromPartNumber(page.get("partNumberCode").toString()));
		List<BlackList> list=tPartDao.blacklist(page);
		page.setEntities(list);
	}

	@Override
	public void update(TPart record) {
		tPartDao.updateByPrimaryKeySelective(record);
	}

	public void add(Integer clientInquiryElementId,String msn) {
		ClientInquiryElement clientInquiryElement = clientInquiryElementDao.selectByPrimaryKey(clientInquiryElementId);
		String cageCode = msn.substring(2);
		TPart tPart = new TPart();
		tPart.setPartNum(clientInquiryElement.getPartNumber());
		tPart.setPartName(clientInquiryElement.getDescription());
		tPart.setMsn(msn);
		tPart.setMsnFlag(msn.substring(0, 1));
		tPart.setCageCode(cageCode);
		insertSelective(tPart);
				
	}
	
	public MessageVo updateType(MultipartFile multipartFile){
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
		    HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			//定义行
		    Row row;
			List<PartTypeSubset> elementList = new ArrayList<PartTypeSubset>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            		TPart tPart = new TPart();
	            	 	String bsn = row.getCell(0).toString();
	            	 	tPart.setBsn(bsn);
	            	 	String code = "";
	            	 	List<PartTypeSubset> typeList = new ArrayList<PartTypeSubset>();
	            	 	if (row.getCell(6) != null) {
	            	 		Cell secCell = row.getCell(6);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			code = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								code = dataFormatter.formatCellValue(secCell);
							}
	            	 		//code = row.getCell(6).toString();
						}
	            	 	if (!"".equals(code)) {
	            	 		typeList = partTypeSubsetDao.selectByCode(code);
	            	 		if (typeList.size() > 0) {
	            	 			tPart.setPartType(typeList.get(0).getId());
							}
						}
	            	 	if (row.getCell(2) != null) {
	            	 		tPart.setPartName(row.getCell(2).toString());
						}
	            	 	if (row.getCell(3) != null) {
	            	 		if (row.getCell(3).toString().trim().equals("是")) {
	            	 			tPart.setIsBlacklist(1);
							}else if (row.getCell(3).toString().trim().equals("否")) {
								tPart.setIsBlacklist(0);
							}
	            	 		
						}
	            	 	if (row.getCell(8) != null) {
	            	 		Cell secCell = row.getCell(8);
	            	 		if (!"".equals(secCell.toString())) {
	            	 			Integer shelf = new Integer(dataFormatter.formatCellValue(secCell));
								tPart.setShelfLife(shelf);
							}
	            	 		//code = row.getCell(6).toString();
						}
	            	 	String ata = null;
	            	 	if (row.getCell(9) != null) {
	            	 		Cell secCell = row.getCell(9);
	            	 		if (!"".equals(dataFormatter.formatCellValue(secCell))) {
								ata = dataFormatter.formatCellValue(secCell);
								tPart.setAtaChapterSection(new Integer(ata));
							}
	            	 		
						}
	            	 	String weight = null;
	            	 	if (row.getCell(10) != null) {
	            	 		Cell secCell = row.getCell(10);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			weight = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								weight = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setWeight(weight);
						}
	            	 	String dimentions = null;
	            	 	if (row.getCell(11) != null) {
	            	 		Cell secCell = row.getCell(11);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			dimentions = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								dimentions = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setDimentions(dimentions);
						}
	            	 	String country = null;
	            	 	if (row.getCell(12) != null) {
	            	 		Cell secCell = row.getCell(12);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			country = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								country = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setCountryOfOrigin(country);
						}
	            	 	String eccn = null;
	            	 	if (row.getCell(13) != null) {
	            	 		Cell secCell = row.getCell(13);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			eccn = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								eccn = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setEccn(eccn);
						}
	            	 	String scheduleBCode = null;
	            	 	if (row.getCell(14) != null) {
	            	 		Cell secCell = row.getCell(14);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			scheduleBCode = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								scheduleBCode = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setScheduleBCode(scheduleBCode);
						}
	            	 	String category = null;
	            	 	if (row.getCell(15) != null) {
	            	 		Cell secCell = row.getCell(15);
	            	 		if (!"".equals(dataFormatter.formatCellValue(secCell))) {
								category = dataFormatter.formatCellValue(secCell);
								tPart.setCategoryNo(new Integer(category));
							}
	            	 		
						}
	            	 	String usml = null;
	            	 	if (row.getCell(16) != null) {
	            	 		Cell secCell = row.getCell(16);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			usml = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								usml = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setUsml(usml);
						}
	            	 	String hazmat = null;
	            	 	if (row.getCell(17) != null) {
	            	 		Cell secCell = row.getCell(17);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			hazmat = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								hazmat = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setHazmatCode(hazmat);
						}
	            	 	String hsCode = null;
	            	 	if (row.getCell(18) != null) {
	            	 		Cell secCell = row.getCell(18);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			hsCode = secCell.toString();
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								hsCode = dataFormatter.formatCellValue(secCell);
							}
	            	 		tPart.setHsCode(hsCode);
						}
	            	 	if (row.getCell(19) != null) {
	            	 		Cell secCell = row.getCell(19);
	            	 		if (secCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
	            	 			tPart.setRemark(secCell.toString());
							}else if (secCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
								tPart.setRemark(dataFormatter.formatCellValue(secCell));
							}
	            	 	}
	            	 	tPartDao.updateByPrimaryKeySelective(tPart);
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			success=true;
			message="save successful!";
			messageVo.setFlag(success);
			messageVo.setMessage(message);
			return messageVo;
		} catch (Exception e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			e.printStackTrace();
			success = false;
			message="save unsuccessful!";
			return new MessageVo(success, message);
		}
		
	}
	
	public List<String> getCageCodeByShortCode(String shortCode){
		return tPartDao.getCageCodeByShortCode(shortCode);
	}
	
	public String getBsnByPartAndMsn(String shortPartNum,String msn){
		return tPartDao.getBsnByPartAndMsn(shortPartNum, msn);
	}
	
	public List<CrmStock> selectByMark(Integer correlationMark){
		return tPartDao.selectByMark(correlationMark);
	}
	
	public List<CrmStock> selectByMarkAndBsn(PageModel<CrmStock> page){
		return tPartDao.selectByMarkAndBsn(page);
	}
	
	public List<TPart> getByImportElementId(Integer importElementId){
		return tPartDao.getByImportElementId(importElementId);
	}
	
	public List<SystemCode> getSystemByType(String type){
		return tPartDao.getSystemByType(type);
	}
	
	public List<TPart> selectByPart(String partNumber){
		return tPartDao.selectByPart(partNumber);
	}
}
