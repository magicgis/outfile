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
import com.naswork.model.ClientOrder;
import com.naswork.model.ExchangeRate;
import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;

/**
 * 这是例子程序
 * 在写实际的excel处理时，如下几个步骤
 * 1）写一个类集成ExcelGeneratorBase并提供所有abstract函数的实现，具体每个函数的需求请看具体函数的注释
 * 2）把这个类注册到ExcelGeneratorMapConstant类当中
 * 
 * @author eyaomai
 *
 */
@Service("pendingExcelGenerator")
public class PendingExcelGenerator extends ExcelGeneratorBase {
	
	@Resource
	private ClientOrderService clientOrderService;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"Pending_PO_Status.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		List<ClientOrderVo> orderList = clientOrderService.getImportAmount(new Integer(ywId));
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		//Object value =null;
//		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {
//			Row row = sheet.getRow(rowIndex);
//			for (int cellnum = row.getFirstCellNum(); cellnum <= row
//					.getLastCellNum(); cellnum++) {
//				Cell cell = row.getCell(cellnum);
//				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
//					String str = cell.getStringCellValue();
//					if (str == null) {
//						continue;
//					}
//					if (str.startsWith("$")) {
//						String key = str.substring(1);
//						Object value = null;
//						if (key.equals("SOURCE_NUMBER")) {
//							value = supplier.getContactName();
//							this.write(cell, value);
//						} else if(key.equals("SUPPLIER_FAX")){
//							value = supplier.getFax();
//							this.write(cell, value);
//						}if (key.equals("SUPPLIER_INQUIRY_DATE")) {
//							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//							value = df.format(supplierInquiry.getInquiryDate());
//							this.write(cell, value);
//						} else if(key.equals("SUPPLIER_INQUIRY_QUOTE_NUMBER")){
//							value = supplierInquiry.getQuoteNumber();
//							this.write(cell, value);
//						}
//					}
//				}
//			}
//		}
		// 数据
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
		
		if (orderList != null
				&& orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				//lastRowNum = sheet.getLastRowNum();
				Row elementRow = sheet.createRow(lastRowNum + i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					ClientOrderVo manageElementVo = orderList.get(i);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							//Object value = null;
							if (key.equals("SOURCE_NUMBER")) {
								Object value = manageElementVo.getSourceOrderNumber();
								this.write(elementCell, value);
							} else if(key.equals("ORDER_DATE")){
								Object value = manageElementVo.getOrderDate();
								this.write(elementCell, value);
							} else if (key.equals("CONTACT")) {
								Object value = manageElementVo.getContactName();
								this.write(elementCell, value);
							} else if(key.equals("CSN")){
								Object value = manageElementVo.getInquiryCsn();
								this.write(elementCell, value);
							} else if (key.equals("ITEM")) {
								Object value = manageElementVo.getInquiryItem();
								this.write(elementCell, value);
							} else if(key.equals("PART_NUMBER")){
								Object value = manageElementVo.getInquiryPartNumber();
								this.write(elementCell, value);
							}else if (key.equals("DESCRIPTION")) {
								Object value = manageElementVo.getInquiryDescription();
								this.write(elementCell, value);
							}else if (key.equals("UNIT")) {
								Object value = manageElementVo.getInquiryUnit();
								this.write(elementCell, value);
							}else if (key.equals("QUANTITY")) {
								Object value = manageElementVo.getOrderAmount();
								this.write(elementCell, value);
							}else if (key.equals("ORDER_NUMBER")) {
								Object value = manageElementVo.getOrderNumber();
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
		
		return "Pending_PO_Status"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "Pending_PO_Status";
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
		return "Pending_PO_StatusExcel";
	}

}
