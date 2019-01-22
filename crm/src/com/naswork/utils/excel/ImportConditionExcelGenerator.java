package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.ImportPackagePaymentElementPrepareDao;
import com.naswork.dao.SupplierDao;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.service.SupplierContactService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierService;
import com.naswork.service.UserService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("importConditionExcelGenerator")
public class ImportConditionExcelGenerator extends ExcelGeneratorBase{
	
	@Resource
	private ImportPackagePaymentElementPrepareDao importPackagePaymentElementPrepareDao;
	@Resource
	private ImportPackageDao importPackageDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	
	private String number; 
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"ImportCondition.xls";
		
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		String[] index = ywId.split(",");
		List<ImportPackagePaymentElementPrepare> list = importPackagePaymentElementPrepareDao.getImportElementByImportId(new Integer(index[0]));
		ImportPackage importPackage = importPackageDao.selectByPrimaryKey(new Integer(index[0]));
		number = importPackage.getImportNumber();
		Supplier supplier = supplierDao.selectByPrimaryKey(importPackage.getSupplierId());
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		/*//判断模板
		if (!currencyId.equals(RMB)) {
			createRow(sheet,lastRowNum,elementList.size());
		}*/
		
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
					}
				}
			}
		}
		// 供应商订单明细
		
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
		
		if (list != null
				&& list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				//lastRowNum = sheet.getLastRowNum();
				Row elementRow = sheet.createRow(lastRowNum + i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare = list.get(i);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("PART_NUMBER")){
								Object value = importPackagePaymentElementPrepare.getPartNumber();
								this.write(elementCell, value);
							}else if (key.equals("IMPORT_AMOUNT")) {
								Object value = importPackagePaymentElementPrepare.getAmount();
								this.write(elementCell, value);
							} else if(key.equals("EXCEPTION_AMOUNT")){
								Double exception = importPackageElementDao.getTotalAmountByOrderELementId(importPackagePaymentElementPrepare.getSupplierOrderElementId());
								if (exception == null) {
									exception = new Double(0);
								}
								Object value = exception;
								this.write(elementCell, value);
							}
						}
						
					}
				}
			}
		}	
/*		int lastRow3 = sheet.getLastRowNum()+1;
		wb.setPrintArea(0, "$A$1:$J$"+lastRow3);*/
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
	
	
	private static void createRow(Sheet sheet, int beginRowNum, int numOfCell){
		int lastRowNum = sheet.getLastRowNum();
		for(int dynamicIndex=0; dynamicIndex<numOfCell; dynamicIndex++){
			sheet.createRow(lastRowNum + dynamicIndex);
		}
		
		for(int dynamicIndex=0; dynamicIndex<10; dynamicIndex++){
			Row oriRow = sheet.getRow(lastRowNum-dynamicIndex);
			Row newRow = sheet.getRow(lastRowNum + numOfCell-dynamicIndex-1);
			int lastCellNum = oriRow.getLastCellNum();
			for(int dynamicIndex2=0; dynamicIndex2<lastCellNum; dynamicIndex2++){
				Cell oriCell = oriRow.getCell(dynamicIndex2);
				newRow.createCell(dynamicIndex2, oriCell.getCellType());
			}
			
			for(int dynamicIndex2=0; dynamicIndex2<lastCellNum; dynamicIndex2++){
				Cell oriCell = oriRow.getCell(dynamicIndex2);
				Cell newCell = newRow.getCell(dynamicIndex2);
				copyCell(oriCell, newCell);
			}
		}
		
		
		
		
	}
	
	
	
	/**
	 * 获取输出文件全路径，默认来说其父路径为C:\mis\excel\
	 * 注意，一般来说只需替换后面部分，比如将sampleoutput换为其他路径，也可以按照具体需求给多层路径，比如"f1"+File.seperator+"f2"
	 * 注意，最后无需给路径分隔符
	 */
	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_EMAIL+File.separator+"sampleoutput";
		
	}

	/**
	 * 获取输出文件名称（不包含路径和后缀），路径由fetchOutputFilePath方法给出，而后缀则由template文件决定
	 * 建议格式是<模块>_<用户登录名>_<日期>_<时间>，对于具体的类，可以根据需求增加更多信息
	 */
	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		return number+"入库单到货情况";
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "import_condition";
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
		return "ImportConditionExcel";
	}
	
}
