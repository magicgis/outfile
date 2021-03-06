package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
@Service("importPackgePaymentExcelGenerator")
public class ImportPackgePaymentExcelGenerator extends ExcelGeneratorBase {
	
	@Resource
	private ImportPackageService importPackageService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ImportPackagePaymentElementService importPackagePaymentElementService;
	
	String number = null;
	
	@Override
	protected String fetchMappingKey() {
		return "ImportPackgePaymentExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"ImportPackagePayment.xls";
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		List<ImportPackagePaymentElement> elements=importPackagePaymentElementService.elementList(Integer.parseInt(ywId));
//		List<ImportPackageListVo> importPackage=importPackageService.findImportPackageDate(ywId);//YWid是前台传到后台查数据的主键
//		List<ImportPackageElementVo> ipelist=importpackageElementService.findByIpid(importPackage.get(0).getId());//查出来了需要填到EXCEL的数据
		number=elements.get(0).getPaymentNumber();
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());//拿到第一页sheet
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum=sheet.getFirstRowNum();
		int lastRowNum=sheet.getLastRowNum();
		
		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {//循环第一行到最后一行，有数据的行
			Row row=sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {//每一行的第一列到最后一列，有数据列
				Cell cell=row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {//拿到以$开头的变量
						Object value = null ;
						String key = str.substring(1);
						if(key.equals("date")){//当变量等于这个的时候，就把数据写进去
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							Date now = new Date();
							 value =format.format(now)+"付款清单";
						}
						this.write(cell, value);//写数据的方法
					}
				}
			}
		}
		
		Row lastRow=sheet.getRow(lastRowNum);//拿到最后一行，这里和上面的拿所有行不一样，因为我模板需要导出的变量都在最后一行，而且这行需要循环
		short firstCellNum = lastRow.getFirstCellNum();
		short lastCellNum = lastRow.getLastCellNum();
		String[] cellKeys = new String[lastCellNum - firstCellNum + 1];
		for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
			Cell cell=lastRow.getCell(cellnum);
			if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String str = cell.getStringCellValue();
				if (str == null) {
					continue;
				}
				if (str.startsWith("$")) {//同样是拿变量，这里要装起来
					cellKeys[cellnum - firstCellNum] = str.substring(1);
				}
			}
		}
		sheet.removeRow(lastRow);//循环的做法是，删除有变量的最后一行，下面再创建这一行
		
		if (elements != null && elements.size() > 0) {
			for (int i = 0; i < elements.size(); i++) {
				Row elementRow=sheet.createRow(lastRowNum+i);//创建行
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {//循环这一行的列，拿到每一格的变量值，匹配写数据
					Cell cell=lastRow.getCell(cellnum);
					Cell elementCell=elementRow.createCell(cellnum);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
						String str=	cellKeys[cellnum - firstCellNum];
						Object value =null;
							if(str.equals("PART_NUMBER")){
								value =elements.get(i).getPartNumber();
							}else if(str.equals("DESCRIPTION")){
								value =elements.get(i).getDescription();
							}else if(str.equals("UNIT")){
								value =elements.get(i).getUnit();
							}else if(str.equals("AMOUNT")){
								value =elements.get(i).getAmount();
							}else if(str.equals("BASE_PRICE")){
								value =elements.get(i).getPrice();
							}else if(str.equals("ORDER_NUMBER")){
								value =elements.get(i).getOrderNumber();
							}else if(str.equals("REMARK")){
								value =elements.get(i).getRemark();
							}else if(str.equals("SERIAL_NUMBER")){
								value =elements.get(i).getSerialNumber();
							}else if(str.equals("QUOTE_NUMBER")){
								value =elements.get(i).getQuoteNumber();
							}
							this.write(elementCell, value);
						}
							if (cellnum == 0) {
								this.write(elementCell, i + 1);
							} else if (cellnum == 6) {
								elementCell.setCellFormula("E"
										+ (lastRowNum + i + 1) + "*F"
										+ (lastRowNum + i + 1));
							}
						
					}
				}
			}
		}
		
		Sheet sheet2=wb.getSheetAt(wb.getActiveSheetIndex() + 1);
		wb.removeSheetAt(wb.getActiveSheetIndex() + 1);
		int lastRow2 = sheet.getLastRowNum();
		
		if (lastRow2 < 3) {
			return;
		}
		for (int i = 0; i < sheet2.getNumMergedRegions(); i++) {
			CellRangeAddress cra2 = sheet2.getMergedRegion(i);
			CellRangeAddress cra = new CellRangeAddress(cra2.getFirstRow()
					+ lastRow2 + 1, cra2.getLastRow() + lastRow2 + 1,
					cra2.getFirstColumn(), cra2.getLastColumn());
			sheet.addMergedRegion(cra);
		}
		for (int rowIndex2 = sheet2.getFirstRowNum(); rowIndex2 <= sheet2
				.getLastRowNum(); rowIndex2++) {
			Row row1 = sheet.createRow(lastRow2 + rowIndex2 + 1);
			Row row2 = sheet2.getRow(rowIndex2);
			short height = row2.getHeight();
			row1.setHeight(height);
			for (int cellnum = row2.getFirstCellNum(); cellnum <= row2
					.getLastCellNum(); cellnum++) {
				Cell cell1 = row1.createCell(cellnum);
				Cell cell2 = row2.getCell(cellnum);
				if (cell2 != null) {
					cell1.setCellStyle(cell2.getCellStyle());
					switch (cell2.getCellType()) {
					case Cell.CELL_TYPE_FORMULA:
						cell1.setCellFormula(cell2.getCellFormula());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cell1.setCellValue(cell2.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						String str = cell2.getStringCellValue();
						if ("$SUM".equals(str)) {
							cell1.setCellFormula("SUM(E3:E" + (lastRow2 + 1)
									+ ")");
						} else if ("$TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(G3:G" + (lastRow2 + 1)
									+ ")");
						} else {
							cell1.setCellValue(str);
						}
						break;
					}
				}
			}
		}
	}

	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_FOLDER+File.separator+"sampleoutput";
		
	}

	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		
		 return number+"_"+"供应商付款清单"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "import_package_payment";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
