package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.MpiMessage;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.Address;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.service.MpiService;
import com.naswork.service.SupplierContactService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierService;
import com.naswork.service.UserService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("supplierOrderExcelGenerator")
public class SupplierOrderExcelGenerator extends ExcelGeneratorBase{
	
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private SupplierContactService supplierContactService;
	@Resource
	private UserService userService;
	@Resource
	private MpiService mpiService;
	
	String number = null;
	
	String currencyId = null;



	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		this.currencyId = currencyId;
		String RMB = "10";
		if (currencyId.equals(RMB)) {
			return File.separator+"exceltemplate"+File.separator+"SupplierOrder.xls";
		}else {
			return File.separator+"exceltemplate"+File.separator+"SupplierOrderEnglish.xls";
		}
		
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		String RMB = "10";
		PageModel<AddSupplierOrderElementVo> page = new PageModel<AddSupplierOrderElementVo>();
		String[] index = ywId.split("-");
		page.put("id", index[0]);
		UserVo userVo = userService.findById(new Integer(index[1]));
		SupplierOrderManageVo supplierOrderManageVo = supplierOrderService.findByOrderId(new Integer(index[0]));
		List<SupplierContact> supplierContacts = supplierContactService.findBySupplierId(supplierOrderManageVo.getSupplierId());
		
		number = supplierOrderManageVo.getSupplierOrderNumber();
		List<AddSupplierOrderElementVo> elementList = supplierOrderService.Elements(new Integer(index[0]));
		Address address = new Address();
		MpiMessage mpiMessage = new MpiMessage();
		List<String> mpiAddress = new ArrayList<String>();
		Integer destinationId = supplierOrderService.getDestination(supplierOrderManageVo.getId());
		if (destinationId != null) {
			address = supplierOrderService.getAddress(destinationId);
			if (address == null) {
				mpiMessage = mpiService.selectByPrimaryKey(destinationId);
				if (mpiMessage != null) {
					if (mpiMessage.getAddress() != null) {
						String[] add = mpiMessage.getAddress().split(",");
						for (int i = 0; i < add.length; i++) {
							mpiAddress.add(add[i]);
						}
					}
				}
			}
		}
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
						if (key.equals("SUPPLIER_NAME")) {
							value = supplierOrderManageVo.getSupplierName();
							this.write(cell, value);
						} else if(key.equals("ORDER_NUMBER")){
							value = supplierOrderManageVo.getSupplierOrderNumber();
							this.write(cell, value);
						}if (key.equals("ORDER_DATE")) {
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							value = df.format(supplierOrderManageVo.getOrderDate());
							this.write(cell, value);
						}if (key.equals("CONTACT_NAME")) {
							if (supplierContacts.size()>0) {
								value = supplierContacts.get(0).getFullName();
							}else {
								value = " ";
							}
							//value = supplierOrderManageVo.getContactName();
							this.write(cell, value);
						} else if(key.equals("ADDRESS")){
							value = supplierOrderManageVo.getAddress();
							this.write(cell, value);
						}if (key.equals("PHONE")) {
							if (supplierContacts.size()>0) {
								value = supplierContacts.get(0).getPhone();
							}else {
								value = " ";
							}
							//value = supplierOrderManageVo.getPhone();
							this.write(cell, value);
						} else if(key.equals("FAX")){
							if (supplierContacts.size()>0) {
								value = supplierContacts.get(0).getFax();
							}else {
								value = " ";
							}
							//value = supplierOrderManageVo.getFax();
							this.write(cell, value);
						}if (key.equals("BANK")) {
							value = supplierOrderManageVo.getBank();
							this.write(cell, value);
						} else if(key.equals("BANK_ACCOUNT_NUMBER")){
							value = supplierOrderManageVo.getBankAccountNumber();
							this.write(cell, value);
						} else if (key.equals("TAX_PAYER_NUMBER")) {
							value = supplierOrderManageVo.getTaxPayerNumber();
							this.write(cell, value);
						}else if (key.equals("QUOTE_NUMBER")) {
							value = elementList.get(0).getSupplierQuoteNumber();
							this.write(cell, value);
						}else if (key.equals("PAYMENT_RULE")) {
							value = supplierOrderManageVo.getPaymentRule();
							this.write(cell, value);
						}else if (key.equals("CURRENCY")) {
							value = supplierOrderManageVo.getCurrencyCode();
							this.write(cell, value);
						}else if (key.equals("USERNAME")) {
							value = userVo.getLoginName();
							this.write(cell, value);
						}else if (key.equals("USEREMAIL")) {
							value = userVo.getEmail();
							this.write(cell, value);
						}else if (key.equals("LINE_ONE")) {
							if (address != null) {
								if (address.getId() != null) {
									value = address.getLineOne();
									this.write(cell, value);
								}
							}else if (mpiMessage != null) {
								value = mpiMessage.getName();
								this.write(cell, value);
							}else {
								this.write(cell, "");
							}
						}else if (key.equals("LINE_TWO")) {
							if (address != null) {
								if (address.getId() != null) {
									value = address.getLineTwo();
									this.write(cell, value);
								}
							}else if (mpiMessage != null) {
								value = "";
								for (int i = 0; i < mpiAddress.size(); i++) {
									if (value.toString().length() > 45) {
										break;
									}else {
										int now = value.toString().length();
										int next = mpiAddress.get(i).length();
										if ((now+next) > 55) {
											break;
										}else {
											if ("".equals(value.toString())) {
												value = value + mpiAddress.get(i);
											}else {
												value = value + "," + mpiAddress.get(i);
											}
										}
										mpiAddress.remove(i);
										i = i-1;
									}
								}
								//value = mpiMessage.getAddress();
								this.write(cell, value);
							}else {
								this.write(cell, "");
							}
						}else if (key.equals("LINE_THREE")) {
							if (address != null) {
								if (address.getId() != null) {
									value = address.getLineThree();
									this.write(cell, value);
								}
							}else if (mpiMessage != null) {
								value = "";
								for (int i = 0; i < mpiAddress.size(); i++) {
									if ("".equals(value.toString())) {
										value = value + mpiAddress.get(i);
									}else {
										value = value + "," + mpiAddress.get(i);
									}
								}
								this.write(cell, value);
							}else {
								this.write(cell, "");
							}
							
						}else if (key.equals("LINE_FOUR")) {
							if (address != null) {
								if (address.getId() != null) {
									value = address.getLineFour();
									this.write(cell, value);
								}
							}else if (mpiMessage != null) {
								value = mpiMessage.getContact();
								this.write(cell, value);
							}else {
								this.write(cell, "");
							}
						}else if (key.equals("LINE_FIVE")) {
							if (address != null) {
								if (address.getId() != null) {
									value = address.getLineFive();
									this.write(cell, value);
								}
							}else if (mpiMessage != null) {
								value = mpiMessage.getTel();
								this.write(cell, value);
							}else {
								this.write(cell, "");
							}
						}
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
		
		if (elementList != null
				&& elementList.size() > 0) {
			for (int i = 0; i < elementList.size(); i++) {
				//lastRowNum = sheet.getLastRowNum();
				Row elementRow = sheet.createRow(lastRowNum + i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					AddSupplierOrderElementVo addSupplierOrderElementVo = elementList.get(i);
					if (cell != null) {
						CellStyle cellStyle = cell.getCellStyle();
						cellStyle.setWrapText(true);
						elementCell.setCellStyle(cellStyle);
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("DETAIL")){
								StringBuffer stringBuffer = new StringBuffer();
								stringBuffer.append(addSupplierOrderElementVo.getQuotePartNumber()).append("/r/n")
								.append(addSupplierOrderElementVo.getQuoteDescription()).append("/r/n")
								.append(addSupplierOrderElementVo.getCertificationCode());
								Object value = addSupplierOrderElementVo.getDetail();
								//Object value = stringBuffer.toString();
								this.write(elementCell, value);
							}else if (key.equals("QUOTE_UNIT")) {
								Object value = addSupplierOrderElementVo.getQuoteUnit();
								this.write(elementCell, value);
							} else if(key.equals("SUPPLIER_ORDER_AMOUNT")){
								Object value = addSupplierOrderElementVo.getSupplierOrderAmount();
								this.write(elementCell, value);
							}else if (key.equals("CONDITION_VALUE")) {
								Object value = addSupplierOrderElementVo.getConditionValue();
								this.write(elementCell, value);
							} else if(key.equals("SUPPLIER_ORDER_LEAD_TIME")){
								StringBuffer leadTime = new StringBuffer();
								Pattern pattern = Pattern.compile("[0-9]*");
								Matcher isNum = pattern.matcher(addSupplierOrderElementVo.getLeadTime());
								if (isNum.matches()) {
									if (new Integer(addSupplierOrderElementVo.getLeadTime()) > 0) { 
										leadTime.append(addSupplierOrderElementVo.getLeadTime()).append("DAYS");
									}else {
										leadTime.append("STK");
									}
								}else {
									leadTime.append(addSupplierOrderElementVo.getLeadTime());
								}
								
								Object value = leadTime;
								this.write(elementCell, value);
							}else if (key.equals("SUPPLIER_ORDER_PRICE")) {
								Object value = addSupplierOrderElementVo.getSupplierOrderPrice();
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(supplierOrderManageVo.getCurrencyValue(),wb,cell.getCellStyle()));
							}else if (key.equals("CONDITION_CODE")) {
								Object value = addSupplierOrderElementVo.getConditionCode();
								this.write(elementCell, value);
							}/*else if (key.equals("ITEM")) {
								Object value = addSupplierOrderElementVo.getItem();
								this.write(elementCell, value);
							}*/
						}
						if (cellnum == 1) {
							this.write(elementCell, i + 1);
						}
						if (cellnum == 8) {
							elementCell.setCellFormula("E"
									+ (lastRowNum + i + 1) + "*H"
									+ (lastRowNum + i + 1));
							elementCell.setCellStyle(currencyFormat(supplierOrderManageVo.getCurrencyValue(),wb,cell.getCellStyle()));
						}
					}
				}
			}
		}
		//判断模板
		if (currencyId.equals(RMB)) {
			Sheet sheet2=wb.getSheetAt(wb.getActiveSheetIndex()+1);
			sheet2.setForceFormulaRecalculation(true);
			wb.removeSheetAt(wb.getActiveSheetIndex() + 1);
			int lastRow2 = sheet.getLastRowNum();
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
							if ("$SUM".equals(str)) {
								cell1.setCellFormula("SUM(I38:I" + (lastRow2 + 1)
										+ ")");
								cell1.setCellStyle(currencyFormat(supplierOrderManageVo.getCurrencyValue(),wb,cell2.getCellStyle()));
							} else if ("$SUPPLIER_QUOTE_NUMBER".equals(str)) {
								cell1.setCellValue(elementList.get(0).getSupplierQuoteNumber());
							} else {
								cell1.setCellValue(str);
							}
							break;
						}
					}
				}
			}
			Cell sumCell = sheet.getRow(35).getCell(8);
			sumCell.setCellFormula("I" + (lastRow2 + 2));
		}else {
			Sheet sheet2=wb.getSheetAt(wb.getActiveSheetIndex()+1);//拿第二个表格复制过来第一个表格
			sheet2.setForceFormulaRecalculation(true);
			wb.removeSheetAt(wb.getActiveSheetIndex() + 1);
			int lastRow2 = sheet.getLastRowNum();
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
						switch (cell2.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							cell1.setCellFormula(cell2.getCellFormula());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							cell1.setCellValue(cell2.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING:
							String str = cell2.getStringCellValue();
							if ("$TOTAL".equals(str)) {
								cell1.setCellFormula("SUM(I" + 25 + ":I"
										+ (25+elementList.size()-1) + ")");
								cell1.setCellStyle(currencyFormat(supplierOrderManageVo.getCurrencyValue(),wb,cell2.getCellStyle()));
//								 String c=cell1.getNumericCellValue()+"";
//								sheet.setColumnWidth(11, c.length()*216);
							}else {
								cell1.setCellValue(str);
							}
						}
					}
				}
			}
		}	
		int lastRow3 = sheet.getLastRowNum()+1;
		wb.setPrintArea(0, "$A$1:$I$"+lastRow3);
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
	
	private CellStyle currencyFormat(String value,POIExcelWorkBook wb,CellStyle Style){
		DataFormat format= wb.createDataFormat();  
		CellStyle cellStyle = wb.createCellStyle(); 
		if(null!=value){
			if(value.equals("人民币")||value=="人民币"){
				Style.setDataFormat(format.getFormat("¥#,##0.00"));  
			}else if(value.equals("美元")||value=="美元"){
				Style.setDataFormat(format.getFormat("$#,##0.00"));
			}else if(value.equals("欧元")||value=="欧元"){
				Style.setDataFormat(format.getFormat("€#,##0.00"));  
			}else if(value.equals("英镑")||value=="英镑"){
				Style.setDataFormat(format.getFormat("￡#,##0.00"));
			}else if(value.equals("港币")||value=="港币"){
				Style.setDataFormat(format.getFormat("HK$#,##0.00"));  
			}
			/*cellStyle.setBorderBottom(Style.getBorderBottom()); //下边框    
			cellStyle.setBorderLeft(Style.getBorderLeft());//左边框    
			cellStyle.setBorderTop(Style.getBorderTop());//上边框    
			cellStyle.setBorderRight(Style.getBorderRight());//右边框    
*/			/*Font font = wb.createFont();
			font.setFontName("Arial Narrow");  
			font.setFontHeightInPoints((short) 12); 
			Style.setFont(font);*/
		}
		return Style;
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
		return number+"_"+"SupplierOrder"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "supplier_order";
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
		return "SupplierOrderExcel";
	}
	
}
