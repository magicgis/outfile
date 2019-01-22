package com.naswork.utils.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.naswork.common.constants.FileConstant;
import com.naswork.filter.ContextHolder;
import com.naswork.model.gy.GyExcel;
import com.naswork.service.GyExcelService;
import com.naswork.utils.StringUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class ExcelGeneratorBase {
	
	@Resource
	protected GyExcelService gyExcelService;

	public static void deleteFile(String fileName){
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}				
	}
	/**
	 * 根据业务id生成excel
	 * @param ywId
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String generate(String ywId,String currencyId) throws FileNotFoundException, IOException{
		//1. 根据业务id以及对应的业务，获取数据
//		this.fetchData(ywId);
		
		//2.获取template
		String templateFileName = this.fetchTemplateFileName(currencyId);
//		File templateFile = new File(ContextHolder.getRequest().getServletContext().getRealPath(templateFileName));
//		InputStream filePath = ContextHolder.getRequest().getServletContext().getResourceAsStream(templateFileName); 
		File templateFile1 =new File((ContextHolder.getRequest()).getSession().getServletContext().getRealPath(templateFileName));
		File templateFile2 = new File(ContextHolder.getRequest().getRealPath(templateFileName));
		POIExcelReader reader = new POIExcelReader(new FileInputStream(templateFile2),templateFileName);
		POIExcelWorkBook wb = reader.getWorkbook(); 
//		Sheet sheet = wb.getSheetAt(0);
//		sheet.setForceFormulaRecalculation(true);
		
		//3.将数据回填到template
		this.writeData(wb,ywId);
		
		//4.生成文件
		String outputFilePath = this.fetchOutputFilePath();
		String excelType = templateFileName.substring(templateFileName.lastIndexOf(".")+1);
		String outputFileName = outputFilePath + File.separator + this.fetchOutputFileName() + "." + excelType;
		String fileShortName = outputFileName.substring(outputFileName.lastIndexOf(File.separator)+1);
		File outputPath = new File(outputFilePath);
		if (!outputPath.exists()) {
			outputPath.mkdirs();
		}
		File outputFile = new File(outputFileName);
		FileOutputStream fos = new FileOutputStream(outputFile);
		wb.write(fos);
		fos.flush();
		fos.close();
		
		//5.写入数据库
		PageModel<GyExcel> page = new PageModel<GyExcel>();
		page.put("ywTableName", this.fetchYwTableName());
		boolean isNum = ywId.matches("[0-9]+");   
		if(isNum){
			page.put("ywId", ywId);
		}else{
			String[] ids=ywId.split("-");
			if(ids[0].equals("0")){
				page.put("ywId", 0);
			}else{
				String Id = ids[0].replaceAll(",", "");
				page.put("ywId", Id);
			}
			
		}
		GyExcel lastExcel = gyExcelService.findLatestExcel(page);
		GyExcel newExcel = new GyExcel();
		if(lastExcel==null){
			newExcel.setXh(1);
		}else{
			newExcel.setXh(lastExcel.getXh()+1);
		}
		newExcel.setExcelFileName(fileShortName);
		newExcel.setExcelFilePath(outputFileName);
		newExcel.setExcelType(excelType);
		newExcel.setExcelTemplateName(this.fetchMappingKey());
		newExcel.setExcelFileLength(outputFile.length());
		newExcel.setLrsj(new Date());
		UserVo user = ContextHolder.getCurrentUser(); 
		if(user!=null){
			newExcel.setUserId(user.getUserId());
		}else{
			newExcel.setUserId("0");
		}
		if(isNum){
			newExcel.setYwId(ywId);
		}else{
			String[] ids=ywId.split("-");
			if(ids[0].equals("0")){
				newExcel.setYwId("0");
			}else{
				String Id = ids[0].replaceAll(",", "");
				newExcel.setYwId(Id);
			}
		}
		newExcel.setYwTableName(this.fetchYwTableName());
		newExcel.setYwTablePkName(this.fetchYwTablePkName());
		gyExcelService.add(newExcel);
		
		//6.返回结果
		return "{\"path\":\"" + "/excel/download/"+newExcel.getExcelFileId() + 
				"\",\"name\":\""+fileShortName+"\",\"id\":\""+newExcel.getExcelFileId()+"\"}";
	}
	
	/**
	 * 获取mapping表的key
	 */
	protected abstract String fetchMappingKey();
	
	/**
	 * 根据业务id获取数据
	 * 数据放在实体类的私有成员变量
	 * @param ywId
	 */
//	protected abstract void fetchData(String ywId);
	
	/**
	 * 获取模板名称（不带路径）
	 * @return
	 */
	protected abstract String fetchTemplateFileName(String currencyId);
	
	/**
	 * 将数据写入sheet
	 * @param sheet
	 */
	protected abstract void writeData(POIExcelWorkBook wb,String ywId);
	
	/**
	 * 获取输出文件路径
	 * @return
	 */
	protected abstract String fetchOutputFilePath();
	
	/**
	 * 获取输出文件名称（不带后缀，后缀由template文件决定）
	 * @return
	 */
	protected abstract String fetchOutputFileName();
	
	protected abstract String fetchYwTableName();
	
	protected abstract String fetchYwTablePkName();
		
	
	/**
	 * 往cell写入数据
	 * @param cell
	 * @param obj
	 */
	protected void write(Cell cell, Object obj) {
		if (obj == null) {
			cell.setCellValue("");
		} else if (obj instanceof Integer) {
			cell.setCellValue(((Integer) obj).intValue());
		} else if (obj instanceof BigDecimal) {
			cell.setCellValue(((BigDecimal) obj).doubleValue());
		} else if (obj instanceof Date) {
			cell.setCellValue((Date) obj);
		} else if (obj instanceof String) {
			cell.setCellValue(StringUtil.delHTMLTag((String) obj));
		} else if(obj instanceof Double){
			cell.setCellValue(((Double) obj).doubleValue());
		}
		else {
			cell.setCellValue(obj.toString());
		}
	}
	
	protected String fetchUserName(){
		UserVo user = ContextHolder.getCurrentUser();
		if(user!=null){
			return user.getUserName();
		}else{
			return "";
		}
	}
	
	public static void copy(Cell srcCell, Cell destCell) {
		if (srcCell != null) {
			destCell.setCellStyle(srcCell.getCellStyle());
			switch (srcCell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				destCell.setCellValue(srcCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				destCell.setCellFormula(srcCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				destCell.setCellValue(srcCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				destCell.setCellValue(srcCell.getStringCellValue());
				break;
			}
		}
	}
}
