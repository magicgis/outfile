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
import com.naswork.model.SupplierOrder;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;

@Service("importpackageExcelGenerator")
public class ImportpackageExcelGenerator extends ExcelGeneratorBase {

	@Resource
	private ImportPackageService importPackageService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private SupplierOrderService supplierOrderService;
	
	String number = null;
	
	@Override
	protected String fetchMappingKey() {
		return "ImportpackageExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"Importpackage.xls";
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		List<ImportPackageListVo> importPackage=importPackageService.findImportPackageDate(ywId);
		List<ImportPackageElementVo> ipelist=importpackageElementService.findByIpid(importPackage.get(0).getId());
		number=importPackage.get(0).getImportNumber();
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum=sheet.getFirstRowNum();
		int lastRowNum=sheet.getLastRowNum();
		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {
			Row row=sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {
				Cell cell=row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {
						Object value = null ;
						String key = str.substring(1);
						if(key.equals("IMPORT_NUMBER")){
							 value =importPackage.get(0).getImportNumber();
						}else if(key.equals("IMPORT_DATE")){
							 value =importPackage.get(0).getImportDate();
						}else if(key.equals("SUPPLIER_CODE")){
							 value =importPackage.get(0).getSupplierCode();
						}else if (value == null) {
							this.write(cell, "");
						}
						this.write(cell, value);
					}
				}
			}
		}
		Row lastRow=sheet.getRow(lastRowNum);
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
				if (str.startsWith("$")) {
					cellKeys[cellnum - firstCellNum] = str.substring(1);
				}
			}
		}
		sheet.removeRow(lastRow);
		
		if (ipelist != null && ipelist.size() > 0) {
			for (int i = 0; i < ipelist.size(); i++) {
				Row elementRow=sheet.createRow(lastRowNum+i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell=lastRow.getCell(cellnum);
					Cell elementCell=elementRow.createCell(cellnum);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
						String str=	cellKeys[cellnum - firstCellNum];
						Object value =null;
							if(str.equals("PART_NUMBER")){
								value =ipelist.get(i).getPartNumber();
							}else if(str.equals("DESCRIPTION")){
								value =ipelist.get(i).getDescription();
							}else if(str.equals("UNIT")){
								value =ipelist.get(i).getUnit();
							}else if(str.equals("AMOUNT")){
								value =ipelist.get(i).getAmount();
							}else if(str.equals("BASE_PRICE")){
								value =ipelist.get(i).getPrice();
							}else if(str.equals("ORDER_NUMBER")){
								if(null!=ipelist.get(i).getIpeSupplierOrderElementId()){
									SupplierOrder supplierOrder=supplierOrderService.selectBySupplierOrderElementId(ipelist.get(i).getIpeSupplierOrderElementId());
									if(null!=supplierOrder){
										ipelist.get(i).setOrderNumber(supplierOrder.getOrderNumber());
									}
								}
								value =ipelist.get(i).getOrderNumber();
							}else if(str.equals("SOURCE_ORDER_NUMBER")){
								value =ipelist.get(i).getSourceOrderNumber();
							}else if(str.equals("SOURCE_NUMBER")){
								value =ipelist.get(i).getQuoteNumber();
							}else if(str.equals("LOCATION")){
								value =ipelist.get(i).getLocation();
							}else if(str.equals("REMARK")){
								value =ipelist.get(i).getRemark();
							}else if(str.equals("SERIAL_NUMBER")){
								value =ipelist.get(i).getSerialNumber();
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
							cell1.setCellFormula("SUM(E4:E" + (lastRow2 + 1)
									+ ")");
						} else if ("$TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(G4:G" + (lastRow2 + 1)
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
		
		return number+"_"+"Importpackage"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "import_package";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
