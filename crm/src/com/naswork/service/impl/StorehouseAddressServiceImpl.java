package com.naswork.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.StorehouseAddressDao;
import com.naswork.dao.TPartDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Element;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.StorehouseAddress;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.module.crmstock.controller.CaacVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.GyFjService;
import com.naswork.service.ImportStorageLocationListService;
import com.naswork.service.StorehouseAddressService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("storehouseAddressService")
public class StorehouseAddressServiceImpl implements StorehouseAddressService {

	@Resource
	private StorehouseAddressDao storehouseAddressDao;
	@Resource
	private ImportStorageLocationListService importStorageLocationListService;
	@Resource
	private GyFjService gyFjService;
	@Resource
	private TPartDao tPartDao;
	
	@Override
	public int insertSelective(StorehouseAddress record) {
		return storehouseAddressDao.insertSelective(record);
	}

	@Override
	public StorehouseAddress selectByPrimaryKey(Integer id) {
		return storehouseAddressDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StorehouseAddress record) {
		return storehouseAddressDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<StorehouseAddress> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(storehouseAddressDao.listPage(page));
	}
	
	public List<StorehouseAddress> selectByName(String name){
		return storehouseAddressDao.selectByName(name);
	}
	
	public List<StorehouseAddress> selectAll(){
		return storehouseAddressDao.selectAll();
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
			List<ImportStorageLocationList> elementList = new ArrayList<ImportStorageLocationList>();
			//test
			List<CaacVo> caac = new ArrayList<CaacVo>();
			int ab = sheet.getPhysicalNumberOfRows();
			
			for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            row = sheet.getRow(i);
	            if (row!=null) {
	            		/*String partNumber = row.getCell(0).toString();
	            		if (row.getCell(1) != null) {
							if (!"".equals(row.getCell(1).toString())) {
								tPartDao.updateWrongPart(row.getCell(1).toString(), partNumber);
							}
						}*/
	            	 	String location = row.getCell(0).toString();
			            
			            ImportStorageLocationList importStorageLocationList = new ImportStorageLocationList();
			            importStorageLocationList.setLocation(location);
			            importStorageLocationList.setStorehouseAddressId(id);
			            //存起读取的数据等待保存进数据库
			            if (!elementList.contains(importStorageLocationList)) {
			            	elementList.add(importStorageLocationList);
						}
				}
	        }
			
			//判断是否有错误行
			MessageVo messageVo = new MessageVo();
			if (elementList.size()>0){
				//存档
				excelBackup(wb,this.fetchOutputFilePath(),this.fetchOutputFileName(),id.toString(),
						this.fetchYwTableName(),this.fetchYwTablePkName());
				for (ImportStorageLocationList importStorageLocationList : elementList) {
					importStorageLocationListService.insertSelective(importStorageLocationList);
				}
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
		
		return "ImportStorageLocationList"+"_"+this.fetchUserName()+"_"+format.format(now);
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
		return "import_storage_location_list";
	}
    
    public String fetchYwTablePkName() {
		return "id";
	}
    

}
