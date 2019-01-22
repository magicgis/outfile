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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientShip;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ExportPackage;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.storage.controller.clientship.ClientShipVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.ClientShipService;
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
@Service("clientShipExcelGenerator")
public class ClientShipExcelGenerator extends ExcelGeneratorBase {

	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private ExportPackageService exportPackageService;
	@Resource
	private ClientService clientService;
	@Resource
	private ClientShipService clientShipService;
	String number = null;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		String templet = clientShipService.getTemplet(currencyId);
		Client client = clientService.selectByPrimaryKey(new Integer(currencyId));
		if (client.getCode().equals("370")) {
			templet = File.separator+"exceltemplate"+File.separator+"clientshipfor70.xls";
		}else {
			if (templet.equals("模板一")) {
				templet = File.separator+"exceltemplate"+File.separator+"ClientShip.xls";
			}else if (templet.equals("模板二")) {
				templet = File.separator+"exceltemplate"+File.separator+"Client_Ship_2.xls";
			}
		}
		number="";
		return templet;
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		ClientShipVo clientShipVo = clientShipService.findById(new Integer(ywId));
		number = clientShipVo.getShipNumber();
		PageModel<ExportPackageElementVo> page=new PageModel<ExportPackageElementVo>();
		List<ExportPackageElementVo> exportPackageElementVos = exportPackageElementService.creatExcel(clientShipVo.getExportPackageId());
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
						if (key.equals("SHIP_DATE")) {
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							value = df.format(clientShipVo.getShipDate());
							this.write(cell, value);
						} else if(key.equals("WEIGHT")){
							value =clientShipVo.getWeight();
							this.write(cell, value);
						} else if (key.equals("DIMENSIONS")) {
							value = clientShipVo.getDimensions();
							this.write(cell, value);
						} else if(key.equals("SHIP_NUMBER")){
							value = clientShipVo.getShipNumber();
							this.write(cell, value);
						} else if(key.equals("SHIP_INVOICE_NUMBER")){
							value = clientShipVo.getShipInvoiceNumber();
							this.write(cell, value);
						} else if(key.equals("CLIENT_CONTACT_ADDRESS")){
							value = clientShipVo.getClientContactAddress();
							this.write(cell, value);
						} else if(key.equals("SHIP_CONTACT_ADDRESS")){
							value = clientShipVo.getShipContactAddress();
							this.write(cell, value);
						} else if(key.equals("CLIENT_CONTACT_NAME")){
							value = clientShipVo.getClientContactName();
							this.write(cell, value);
						} else if(key.equals("SHIP_CONTACT_NAME")){
							value = clientShipVo.getShipContactName();
							this.write(cell, value);
						} else if(key.equals("CLIENT_CONTACT_PHONE")){
							value = clientShipVo.getClientContactPhone();
							this.write(cell, value);
						} else if(key.equals("SHIP_CONTACT_PHONE")){
							value = clientShipVo.getShipContactPhone();
							this.write(cell, value);
						} else if(key.equals("CLIENT_NAME")){
							value = clientShipVo.getClientName();
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
							if (key.equals("DETAIL")) {
								Object value = "";
								if ("313".equals(clientShipVo.getClientCode()) || "320".equals(clientShipVo.getClientCode()) || "340".equals(clientShipVo.getClientCode())
										|| "370".equals(clientShipVo.getClientCode())) {
									value = exportPackageElementVo.getOrderDetail();
								}else {
									value = exportPackageElementVo.getDetail();
								}
								this.write(elementCell, value);
							} else if(key.equals("UNIT")){
								Object value = exportPackageElementVo.getUnit();
								this.write(elementCell, value);
							} else if (key.equals("AMOUNT")) {
								Object value = exportPackageElementVo.getAmount();
								this.write(elementCell, value);
							} else if(key.equals("CONDITION_VALUE")){
								Object value = exportPackageElementVo.getConditionCode();
								this.write(elementCell, value);
							} else if (key.equals("SOURCE_ORDER_NUMBER")) {
								Object value = exportPackageElementVo.getSourceOrderNumber();
								this.write(elementCell, value);
							} else if(key.equals("REMARK")){
								Object value = clientShipVo.getRemark();
								this.write(elementCell, value);
							}else if(key.equals("PN")){
								Object value = exportPackageElementVo.getPartNumber();
								this.write(elementCell, value);
							}else if(key.equals("DESC")){
								Object value = "";
								if ("313".equals(clientShipVo.getClientCode()) || "320".equals(clientShipVo.getClientCode()) || "340".equals(clientShipVo.getClientCode())
										|| "370".equals(clientShipVo.getClientCode())) {
									value = exportPackageElementVo.getOrderDescription();
								}else {
									value = exportPackageElementVo.getDescription();
								}
								this.write(elementCell, value);
							}else if(key.equals("ITEM")){
								Object value = "";
								if (exportPackageElementVo.getCsn() != 0) {
									value = exportPackageElementVo.getCsn();
								}
								this.write(elementCell, value);
							}
						}
					}
				}
			}
		}
		
		if (clientShipVo.getShipTemplet().equals("模板二") || clientShipVo.getClientCode().equals("370")) {
			Sheet sheet2=wb.getSheetAt(wb.getActiveSheetIndex()+1);//复制第二页到第一页
			sheet2.setForceFormulaRecalculation(true);
			wb.removeSheetAt(wb.getActiveSheetIndex() + 1);
			int lastRow2 = sheet.getLastRowNum();
			if (clientShipVo.getClientCode().equals("370")) {
				int pageCount = lastRow2/29;
				if (pageCount+1*29 <lastRow2) {
					lastRow2+=35;
				}
			}
			for (int i = 0; i < sheet2.getNumMergedRegions(); i++) {
				CellRangeAddress cra2 = sheet2.getMergedRegion(i);
				CellRangeAddress cra = new CellRangeAddress(cra2.getFirstRow()
						+ lastRow2 + 1, cra2.getLastRow() + lastRow2 + 1,
						cra2.getFirstColumn(), cra2.getLastColumn());
				sheet.addMergedRegion(cra);
			}
			for (int rowIndex = sheet2.getFirstRowNum(); rowIndex <= sheet2
					.getLastRowNum(); rowIndex++) {
				Row row1 = sheet.createRow(lastRow2 + rowIndex + 1);
				Row row2 = sheet2.getRow(rowIndex);
				short height = row2.getHeight();
				row1.setHeight(height);
				for (int cellnum = row2.getFirstCellNum(); cellnum <= row2
						.getLastCellNum(); cellnum++) {
					Cell cell1 = row1.createCell(cellnum);
					Cell cell2 = row2.getCell(cellnum);
					if (cell2 != null) {
						cell1.setCellStyle(cell2.getCellStyle());
						cell1.getCellStyle().setFillBackgroundColor(
								new HSSFColor.WHITE().getIndex());
						switch (cell2.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							cell1.setCellFormula(cell2.getCellFormula());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							cell1.setCellValue(cell2.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING:
							String str = cell2.getStringCellValue();
							if ("$TODAY".equals(str)) {
								SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
								String value = df.format(new Date());
								cell1.setCellValue(value);
							}else {
								cell1.setCellValue(str);
							}
							break;
						}
					}
				}
			}
			int lastRow3 = sheet.getLastRowNum()+1;
			wb.setPrintArea(0, "$A$1:$G$"+lastRow3);
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
		
		return number+"_"+"ClientShip"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "client_ship";
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
		return "ClientShipExcel";
	}

}
