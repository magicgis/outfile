package com.naswork.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ExportPackage;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ExportPackageService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;

/**
 * 这是例子程序
 * 在写实际的excel处理时，如下几个步骤
 * 1）写一个类集成ExcelGeneratorBase并提供所有abstract函数的实现，具体每个函数的需求请看具体函数的注释
 * 2）把这个类注册到ExcelGeneratorMapConstant类当中
 * 
 * @author eyaomai
 *
 */
@Service("exportPackageExcelGenerator")
public class ExportPackageExcelGenerator extends ExcelGeneratorBase {

	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private ExportPackageService exportPackageService;
	@Resource
	private ClientService clientService;

	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"ExportPackage.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		PageModel<ExportPackageElementVo> page=new PageModel<ExportPackageElementVo>();
		page.put("exportPackageId", ywId);
		List<ExportPackageElementVo> exportPackageElementVos = exportPackageElementService.creatExcel(new Integer(ywId));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(new Integer(ywId));
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		//Object value =null;
		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {
				Cell cell = row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {
						String key = str.substring(1);
						Object value = null;
						if (key.equals("EXPORT_NUMBER")) {
							value = exportPackage.getExportNumber();
							this.write(cell, value);
						} else if(key.equals("EXPORT_DATE")){
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							value = df.format(exportPackage.getExportDate());
							this.write(cell, value);
						}else if (key.equals("CLIENT_CODE")) {
							Client client = clientService.selectByPrimaryKey(exportPackage.getClientId());
							value = client.getCode();
							this.write(cell, value);
						}
					}
				}
			}
		}
		// 供应商询价明细
		Row lastRow = sheet.getRow(lastRowNum);
		short firstCellNum = lastRow.getFirstCellNum();
		short lastCellNum = lastRow.getLastCellNum();
		String[] cellKeys = new String[lastCellNum - firstCellNum + 1];
		for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
			Cell cell = lastRow.getCell(cellnum);
			if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String str = cell.getStringCellValue();
				if (str == null) {
					continue;
				}
				if (str.startsWith("$")) {
					String key = str.substring(1);
					cellKeys[cellnum - firstCellNum] = key;
				}
			}
		}
		sheet.removeRow(lastRow);
		
		if (exportPackageElementVos != null
				&& exportPackageElementVos.size() > 0) {
			for (int i = 0; i < exportPackageElementVos.size(); i++) {
				//lastRowNum = sheet.getLastRowNum();
				Row elementRow = sheet.createRow(lastRowNum + i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					ExportPackageElementVo exportPackageElementVo = exportPackageElementVos.get(i);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							//Object value = null;
							if (key.equals("PART_NUMBER")) {
								Object value = exportPackageElementVo.getPartNumber();
								this.write(elementCell, value);
							} else if(key.equals("DESCRIPTION")){
								Object value = exportPackageElementVo.getDescription();
								this.write(elementCell, value);
							} else if (key.equals("UNIT")) {
								Object value = exportPackageElementVo.getUnit();
								this.write(elementCell, value);
							} else if(key.equals("AMOUNT")){
								Object value = exportPackageElementVo.getAmount();
								this.write(elementCell, value);
							} else if (key.equals("BASE_PRICE")) {
								Object value = exportPackageElementVo.getBasePrice();
								this.write(elementCell, value);
							} else if(key.equals("ORDER_NUMBER")){
								Object value = exportPackageElementVo.getOrderNumber();
								this.write(elementCell, value);
							}else if (key.equals("SOURCE_ORDER_NUMBER")) {
								Object value = exportPackageElementVo.getSourceOrderNumber();
								this.write(elementCell, value);
							}else if (key.equals("SOURCE_NUMBER")) {
								Object value = exportPackageElementVo.getSourceNumber();
								this.write(elementCell, value);
							}else if (key.equals("LOCATION")) {
								Object value = exportPackageElementVo.getLocation();
								this.write(elementCell, value);
							}else if (key.equals("REMARK")) {
								Object value = exportPackageElementVo.getRemark();
								this.write(elementCell, value);
							}else if (key.equals("TOTAL_PRICE")) {
								Object value = exportPackageElementVo.getAmount() * exportPackageElementVo.getBasePrice();
								this.write(elementCell, value);
							}
						}
					}
				}
			}
		}
		
	}
	
	
	
	private static void copyCell(Cell oriCell, Cell newCell){
		if(oriCell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
			newCell.setCellValue(oriCell.getBooleanCellValue());
		}else if(oriCell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			newCell.setCellValue(oriCell.getNumericCellValue());
		}else if(oriCell.getCellType() == Cell.CELL_TYPE_STRING){
			newCell.setCellValue(oriCell.getStringCellValue());
		}else if(oriCell.getCellType() == Cell.CELL_TYPE_FORMULA){
			newCell.setCellFormula(oriCell.getCellFormula());
		}
		newCell.setCellStyle(oriCell.getCellStyle());
	}
	
	private static void createCell(Row row, int index, int numOfCell){
		int lastCellNum = row.getLastCellNum();
		for(int dynamicIndex=0; dynamicIndex<numOfCell; dynamicIndex++){
			Cell oriCell = row.getCell(index + dynamicIndex);
			row.createCell(dynamicIndex+lastCellNum, oriCell.getCellType());
		}
		//int newLastCellNum = row.getLastCellNum();
		
		for(int dynamicIndex=lastCellNum-1; dynamicIndex>=index; dynamicIndex--){
			Cell oriCell = row.getCell(dynamicIndex);
			Cell newCell = row.getCell(dynamicIndex + numOfCell);
			copyCell(oriCell, newCell);
		}
	}
	
	public BigDecimal caculateProfitMargin(BigDecimal revenueRate) {
		return new BigDecimal(100.00).divide(
				new BigDecimal(100).subtract(revenueRate), 2,
				BigDecimal.ROUND_HALF_UP);
	}
	
	public static void testWrite(int beginColIndex, int dynamicColNum) throws FileNotFoundException, IOException{
		System.out.println("Begin to write");
		String fileName = "F:\\tmp\\1118113-B0704.xlsx";
		File file  = new File(fileName);
		FileInputStream fs = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);
		createCell(row, beginColIndex, dynamicColNum);
		for(int dynamicIndex=0; dynamicIndex<dynamicColNum;  dynamicIndex++){
			int cellnum = beginColIndex+dynamicIndex;
			Cell cell = row.getCell(cellnum);
			cell.setCellValue("HEADER"+String.valueOf(dynamicIndex));				
		}
		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, dynamicColNum);
			for(int dynamicIndex=0; dynamicIndex<dynamicColNum;  dynamicIndex++){
				int cellnum = beginColIndex+dynamicIndex;
				Cell cell = row.getCell(cellnum);
				cell.setCellValue(String.valueOf(rowIndex*10+dynamicIndex));				
			}
		}
		String outputFileName = "F:\\tmp\\1118113-B0704_update.xlsx";
		File outputFile = new File(outputFileName);
		FileOutputStream fos = new FileOutputStream(outputFile);
		wb.write(fos);
		fos.flush();
		fos.close();
		
		System.out.println("Write Complete");
	}

	
	

	/**
	 * 获取输出文件全路径，默认来说其父路径为C:\mis\excel\
	 * 注意，一般来说只需替换后面部分，比如将sampleoutput换为其他路径，也可以按照具体需求给多层路径，比如"f1"+File.seperator+"f2"
	 * 注意，最后无需给路径分隔符
	 */
	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_FOLDER+File.separator+"sampleoutput";
		
	}

	/**
	 * 获取输出文件名称（不包含路径和后缀），路径由fetchOutputFilePath方法给出，而后缀则由template文件决定
	 * 建议格式是<模块>_<用户登录名>_<日期>_<时间>，对于具体的类，可以根据需求增加更多信息
	 */
	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		
		return "ExportPackage"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "export_package";
	}

	/**
	 * 获取所关联表的主键列列名
	 */
	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}


	/**
	 * 获取mapping的key，用于页面设置
	 */
	@Override
	protected String fetchMappingKey() {
		return "ExportPackageExcel";
	}

}
