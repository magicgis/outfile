package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.dao.TManufactoryDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.TManufactory;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.TManufactoryService;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Service("tManufactoryService")
public class TManufactoryServiceImpl implements TManufactoryService {
	
	@Resource
	private TManufactoryDao tManufactoryDao;
	@Resource
	private UserDao userDao;
	
	@Override
	public int insertSelective(TManufactory record) {
		return tManufactoryDao.insertSelective(record);
	}

	@Override
	public TManufactory selectByPrimaryKey(String msn) {
		return tManufactoryDao.selectByPrimaryKey(msn);
	}

	@Override
	public int updateByPrimaryKeySelective(TManufactory record) {
		return tManufactoryDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<TManufactory> getMsnByCageCode(String cageCode) {
		return tManufactoryDao.getMsnByCageCode(cageCode);
	}

	@Override
	public List<TManufactory> selectByCageCode(String cageCode) {
		return tManufactoryDao.selectByCageCode(cageCode);
	}

	@Override
	public MessageVo uploadExcel(MultipartFile multipartFile) throws Exception {
		boolean success = false;
		InputStream fileStream = null;
		UserVo user = ContextHolder.getCurrentUser();
		RoleVo roleVo = userDao.getPower(new Integer(user.getUserId()));
		byte[] bytes = multipartFile.getBytes();
		String fileName = multipartFile.getOriginalFilename();
		fileStream = new ByteArrayInputStream(bytes);
		POIExcelReader reader = new POIExcelReader(fileStream, fileName);
		POIExcelWorkBook wb = reader.getWorkbook();
	    Sheet sheet = wb.getSheetAt(0);
		 //定义行
	    Row row;
	    
		for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
			 row = sheet.getRow(i);
			 Cell oneCell = row.getCell(0);
			 HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
			 if(null==oneCell||"".equals(oneCell+"")){
				 continue;
			 }
			 String cageCode= "";
			 if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				 cageCode = oneCell.toString();
				}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
					
					cageCode = dataFormatter.formatCellValue(oneCell);
				}
			 
			 String manName= row.getCell(1).toString();
			 String msnFlag = dataFormatter.formatCellValue(row.getCell(2));
			 String capMan= row.getCell(3)+"";
			 String capInspection= row.getCell(4)+"";
			 String capRepair= row.getCell(5)+"";
			 String capModification= row.getCell(6)+"";
			 String capOverhaul= row.getCell(7)+"";
			 TManufactory tManufactory=new TManufactory();
			 tManufactory.setManName(manName);
			 tManufactory.setCageCode(cageCode);
			 String msn= msnFlag+"-"+cageCode;
				/*if (roleVo.getRoleName().equals("国内采购")) {
			  msn="1-"+cageCode;
				}else {
					  msn="0-"+cageCode;
				}*/
			 tManufactory.setMsn(msn);
			 if(capMan.equals("否")){
				 tManufactory.setCapMan("0");
			 }
			 if(capInspection.equals("是")){
				 tManufactory.setCapInspection("1");
			 }
			 if(capRepair.equals("是")){
				 tManufactory.setCapRepair("1");
			 }
			 if(capModification.equals("是")){
				 tManufactory.setCapModification("1");
			 }
			 if(capOverhaul.equals("是")){
				 tManufactory.setCapOverhaul("1");
			 }
			 
//			 String msn=tManufactory.getMsnFlag().toString().trim()+"-"+tManufactory.getCageCode();
//				tManufactory.setMsn(msn);
				List<TManufactory> data=tManufactoryDao.selectByMsn(msn);

//			 TManufactory data=tManufactoryDao.selectByCageCode(cageCode);
				if(data.size() == 0){	
					 tManufactoryDao.insertSelective(tManufactory);
				}
			
		}
		MessageVo messageVo = new MessageVo();
		success = true;
		messageVo.setFlag(success);
		messageVo.setMessage("save successful!");
		return messageVo;
	}
	
	
	public List<TManufactory> selectByMsn(String msn){
		return tManufactoryDao.selectByMsn(msn);
	}
	

}
