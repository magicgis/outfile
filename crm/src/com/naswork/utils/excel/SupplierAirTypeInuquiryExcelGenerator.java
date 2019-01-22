package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.std.InetAddressSerializer;
import com.naswork.common.constants.FileConstant;
import com.naswork.dao.SupplierInquiryElementDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;

@Service("supplierAirTypeInuquiryExcelGenerator")
public class SupplierAirTypeInuquiryExcelGenerator extends ExcelGeneratorBase{
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	
	String number = null;


	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"SupplierInquiryExcel.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		//ClientInquiry clientInquiry = clientInquiryService.findById(new Integer(ywId));
		SupplierInquiry supplierInquiry = supplierInquiryService.selectByPrimaryKey(new Integer(ywId));
		
		
		number = supplierInquiry.getQuoteNumber();
		List<ClientInquiryElement> elementList = clientInquiryElementService.findCieBySi(new Integer(ywId));
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
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
		
		if (elementList != null
				&& elementList.size() > 0) {
			for (int i = 0; i < elementList.size(); i++) {
				//lastRowNum = sheet.getLastRowNum();
				Row elementRow = sheet.createRow(lastRowNum + i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					ClientInquiryElement clientInquiryElement = elementList.get(i);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							//Object value = null;
							if (key.equals("ITEM")) {
								Object value = clientInquiryElement.getItem();
								this.write(elementCell, value);
							} else if(key.equals("PART_NUMBER")){
								Object value = clientInquiryElement.getPartNumber();
								this.write(elementCell, value);
							} else if (key.equals("DESCRIPTION")) {
								Object value = clientInquiryElement.getDescription();
								this.write(elementCell, value);
							} else if(key.equals("UNIT")){
								Object value = clientInquiryElement.getUnit();
								this.write(elementCell, value);
							} else if (key.equals("AMOUNT")) {
								Object value = clientInquiryElement.getAmount();
								this.write(elementCell, value);
							}
						}
					}
				}
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
		
		return number;
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "supplier_air_relation";
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
		return "supplierAirRelationExcel";
	}
	
}
