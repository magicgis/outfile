package com.naswork.utils.excel.poi;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIExcelReader {
	public static final String FILE_EXTENSION_XLS = ".xls";
	public static final String FILE_EXTENSION_XLSX = ".xlsx";
	private POIExcelWorkBook workbook = null;
	public POIExcelWorkBook getWorkbook() {
		return workbook;
	}

	private boolean wb2003 = false;
	public boolean isWb2003() {
		return wb2003;
	}
	public POIExcelReader(InputStream fileStream, String fileName) throws IOException {
		if(fileName.toLowerCase().endsWith(FILE_EXTENSION_XLS)){
			this.wb2003 = true;
		}
		else if(fileName.toLowerCase().endsWith(FILE_EXTENSION_XLSX)){
			this.wb2003 = false;
		}else{
			this.workbook = null;
			return;
		}
		
		this.readExcel(fileStream);
	}
	
	private void readExcel(InputStream fileStream) throws IOException{
		if(this.wb2003){
			POIFSFileSystem fs = new POIFSFileSystem(fileStream);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			this.workbook = new POIExcelWorkBook(wb);
		}else{
			XSSFWorkbook wb = new XSSFWorkbook(fileStream);
			this.workbook = new POIExcelWorkBook(wb);
		}
	}
	public static String read(Cell cell) {
		String str;
		if (cell == null) {
			str = "";
		} else {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				str = "";
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				str = Boolean.toString(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				str = Byte.toString(cell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				str = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				double dValue = cell.getNumericCellValue();
				int iValue = (int) dValue;
				if (iValue == dValue) {
					str = Integer.toString(iValue);
				} else {
					str = Double.toString(dValue);
				}
				break;
			case Cell.CELL_TYPE_STRING:
			default:
				str = cell.getStringCellValue();
				break;
			}
		}
		return str;
	}
	
}
