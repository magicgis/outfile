package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.Client;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientQuote;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.service.ClientInvoiceService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
@Service("clientInvoiceExcelGenerator")
public class ClientInvoiceExcelGenerator extends ExcelGeneratorBase {
	
	@Resource
	private ClientInvoiceService clientInvoiceService;
	@Resource
	private ClientService clientService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private ClientOrderService clientOrderService;
	
	String number = null;
	
	@Override
	protected String fetchMappingKey() {
		return "ClientInvoiceExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		String templet = clientService.getTemplet(new Integer(currencyId));
		if (templet.equals("模板一")) {
			templet = File.separator+"exceltemplate"+File.separator+"INVOICE-1.xls";
		}else if (templet.equals("模板二")) {
			templet = File.separator+"exceltemplate"+File.separator+"INVOICE-2.xls";
		}else if (templet.equals("模板三")) {
			templet = File.separator+"exceltemplate"+File.separator+"INVOICE-3.xls";
		}else if (templet.equals("模板四")) {
			templet = File.separator+"exceltemplate"+File.separator+"INVOICE-4.xls";
		}else if (templet.equals("模板五")) {
			templet = File.separator+"exceltemplate"+File.separator+"INVOICE-5.xls";
		}
		return templet;
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		ClientInvoiceExcelVo clientInvoiceExcelVo=clientInvoiceService.getMessage(new Integer(ywId));//发票信息
		ClientInvoice clientInvoice = clientInvoiceService.selectByPrimaryKey(new Integer(ywId));
		ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(clientInvoice.getClientOrderId());
		Integer latestInvoiceId = clientInvoiceService.getInvoiceIdByCoId(clientInvoiceExcelVo.getClientOrderId());
		int latest = 0;
		if (!clientInvoice.getInvoiceType().equals(0)) {
			ClientInvoiceElement clientInvoiceElement = clientInvoiceService.getTotalByCoId(clientInvoiceExcelVo.getClientOrderId());
			if (latestInvoiceId.equals(clientInvoiceExcelVo.getId()) && clientInvoiceElement.getInvoiceTotal().equals(clientInvoiceElement.getOrderTotal())) {
				latest = 1;
			}
		}
		List<ClientInvoiceExcelVo> invoiceELementList=clientInvoiceService.getEleMessage(new Integer(ywId));//发票明细信息
		number=clientInvoiceExcelVo.getInvoiceNumber();
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		
		
		
		/*Row lastRow=sheet.getRow(lastRowNum);
		short firstCellNum = lastRow.getFirstCellNum();
		short lastCellNum = lastRow.getLastCellNum();
		String[] cellKeys = new String[lastCellNum - firstCellNum + 1];*/
		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {
				Cell cell=row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {
						String key = str.substring(1);
						Object value = null;
						if (key.equals("CONTACT_SHIP_ADDRESS")) {
							value = clientInvoiceExcelVo.getShipAddress();
							this.write(cell, value);
						}else if(key.equals("CONTACT_ADDRESS")){
							value = clientInvoiceExcelVo.getAddress();
							this.write(cell, value);
						}else if (key.equals("INVOICE_DATE")) {
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							value = df.format(clientInvoiceExcelVo.getInvoiceDate());
							this.write(cell, value);
						}else if (key.equals("SOURCE_ORDER_NUMBER")) {
							value = clientInvoiceExcelVo.getSourceOrderNumber();
							this.write(cell, value);
						}else if(key.equals("INVOICE_NUMBER")){
							value = clientInvoiceExcelVo.getInvoiceNumber();
							this.write(cell, value);
						}else if (key.equals("TERMS")) {
							value = clientInvoiceExcelVo.getTerms();
							this.write(cell, value);
						}else if(key.equals("TERMS_1")){
							value = clientInvoiceExcelVo.getTerms_1();
							this.write(cell, value);
						}else if (key.equals("CONTACT_NAME")) {
							value = clientInvoiceExcelVo.getContactName();
							this.write(cell, value);
						}else if(key.equals("SHIP_WAY")){
							value = clientInvoiceExcelVo.getShipWay();
							this.write(cell, value);
						}else if(key.equals("PREPAY_RATE")){
							value = (int)(clientInvoiceExcelVo.getPrepayRate()*100);
							this.write(cell, value);
						}else if(key.equals("SHIP_PAY_RATE")){
							value = (int)(clientInvoiceExcelVo.getShipPayRate()*100);
							this.write(cell, value);
						}else if(key.equals("RECEIVE_PAY_RATE")){
							value = (int)(clientInvoiceExcelVo.getReceivePayRate()*100);
							this.write(cell, value);
						}else if(key.equals("USER_NAME")){
							value = clientInvoiceExcelVo.getUserName();
							this.write(cell, value);
						}else if(key.equals("ORDER_DATE")){
							value = clientInvoiceExcelVo.getOrderDate();
							this.write(cell, value);
						}else if(key.equals("L/C")){
							value = clientInvoiceExcelVo.getLc();
							this.write(cell, value);
						}else if(key.equals("IMPORTERS_REGISTRATION")){
							value = clientInvoiceExcelVo.getImportersRegistration();
							this.write(cell, value);
						}
						
					}
				}
			}
		}	
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
		
		if (invoiceELementList != null && invoiceELementList.size() > 0) {
			for (int i = 0; i < invoiceELementList.size(); i++) {
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
							if(str.equals("QUOTE_PART_NUMBER")){
								value =invoiceELementList.get(i).getQuotePartNumber();
								this.write(elementCell, value);
							}else if(str.equals("QUOTE_DESCRIPTION")){
								if ("313".equals(clientInvoiceExcelVo.getClientCode()) || "320".equals(clientInvoiceExcelVo.getClientCode()) || "340".equals(clientInvoiceExcelVo.getClientCode())
										|| "370".equals(clientInvoiceExcelVo.getClientCode())) {
									value =invoiceELementList.get(i).getOrderDescription();
								}else {
									value =invoiceELementList.get(i).getQuoteDescription();
								}
								this.write(elementCell, value);
							}else if(str.equals("QUOTE_UNIT")){
								value =invoiceELementList.get(i).getQuoteUnit();
								this.write(elementCell, value);
							}else if(str.equals("INVOICE_AMOUNT")){
								value =invoiceELementList.get(i).getInvoiceAmount();
								this.write(elementCell, value);
							}else if(str.equals("CLIENT_ORDER_PRICE")){
								value =invoiceELementList.get(i).getClientOrderPrice();
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(invoiceELementList.get(i).getCurrencyValue(),wb,cell.getCellStyle()));
							}else if(str.equals("ELEMENT_TERMS")){
								value =(invoiceELementList.get(i).getElementTerms()*0.01);
								this.write(elementCell, value);
							}else if(str.equals("SUM_1")){
								//elementCell.setCellFormula("E"+(lastRowNum + 1)+"*F" + (lastRowNum + 1));
								value = invoiceELementList.get(i).getInvoiceAmount()*invoiceELementList.get(i).getClientOrderPrice();
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(invoiceELementList.get(i).getCurrencyValue(),wb,cell.getCellStyle()));
							}else if(str.equals("SUM_2")){
								//elementCell.setCellFormula("F"+(lastRowNum + 1)+"*E" + (lastRowNum + 1));
								value = invoiceELementList.get(i).getInvoiceAmount()*invoiceELementList.get(i).getClientOrderPrice();
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(invoiceELementList.get(i).getCurrencyValue(),wb,cell.getCellStyle()));
							}else if(str.equals("SUM_3")){
								//elementCell.setCellFormula("F"+(lastRowNum + 1)+"*G" + (lastRowNum + 1)+"*H"+(lastRowNum + 1)+"*0.01");
								value = invoiceELementList.get(i).getInvoiceAmount()*invoiceELementList.get(i).getClientOrderPrice()*invoiceELementList.get(i).getElementTerms()*0.01;
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(invoiceELementList.get(i).getCurrencyValue(),wb,cell.getCellStyle()));
							}else if(str.equals("SUM_4")){
								//elementCell.setCellFormula("F"+(lastRowNum + 1)+"*G" + (lastRowNum + 1)+"*H"+(lastRowNum + 1)+"*0.01");
								value = invoiceELementList.get(i).getInvoiceAmount()*invoiceELementList.get(i).getClientOrderPrice()*invoiceELementList.get(i).getElementTerms()*0.01;
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(invoiceELementList.get(i).getCurrencyValue(),wb,cell.getCellStyle()));
							}else if(str.equals("SUM_L/C")){
								value =invoiceELementList.get(i).getInvoiceAmount()*invoiceELementList.get(i).getClientOrderPrice();
								this.write(elementCell, value);
								elementCell.setCellStyle(currencyFormat(invoiceELementList.get(i).getCurrencyValue(),wb,cell.getCellStyle()));
							}else if(str.equals("ITEM")){
								if (invoiceELementList.get(i).getCsn() >= 0) {
									value =invoiceELementList.get(i).getCsn();
								}else {
									value = "";
								}
								
								this.write(elementCell, value);
							}
							
							
						}
						/*if (cellnum == 0) {
							this.write(elementCell, i + 1);
						}*/
						/*// INVOICE TERMS
						if (cellnum == 7) {
							this.write(elementCell, percentage);
						}
						// EXT PRICE
						if (cellnum == 8) {
							elementCell.setCellFormula("F"
									+ (lastRowNum + i + 1) + "*G"
									+ (lastRowNum + i + 1) + "*H"
									+ (lastRowNum + i + 1));
						}*/
					}
				}
			}
		}
		Sheet sheet2=wb.getSheetAt(wb.getActiveSheetIndex()+1);//复制第二页到第一页
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
			/*if (rowIndex == 0) {
				Region region1 = new Region((lastRow2 + rowIndex + 1), (short)0, 0, (short)8);
				sheet.addMergedRegion(region1); 
			}*/
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
						if ("$SUM_1_TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(G16:G" + (lastRow2 + 1)
									+ ")");
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$TODAY".equals(str)) {
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							String value = df.format(new Date());
							cell1.setCellValue(value);
						}else if ("$SUM_2_TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(G13:G" + (lastRow2 + 1)
									+ ")");
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$SUM_3_TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(I14:I" + (lastRow2 + 1)
									+ ")");
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$SUM_4_TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(I14:I" + (lastRow2 + 1)
									+ ")");
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$SUM_TERMS".equals(str)) {
							cell1.setCellFormula("SUM(G16:G" + (lastRow2 + 1)
									+ ")*"+clientInvoiceExcelVo.getInvoiceTerms()+"*0.01");
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$TOTAL_3".equals(str)) {
							Integer clientQuoteId = clientInvoiceExcelVo.getClientQuoteId();
							ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientQuoteId);
							Double orderCount = clientOrderElementService.getTotalAmount(clientInvoiceExcelVo.getClientOrderId());
							Double count = clientQuoteService.getQuoteCount(clientQuoteId);
//							if (count.equals(orderCount)) {
								if (clientOrder.getFreight()!=null && (latest == 1 || clientInvoice.getInvoiceType().equals(0))) {
									cell1.setCellFormula("SUM(I14:I" + (lastRow2 + 1)
											+ ")+"+clientOrder.getFreight());
								}else {
									cell1.setCellFormula("SUM(I14:I" + (lastRow2 + 1)
											+ ")");
								}
								
//							}else if (count>orderCount) {
//								if (clientQuote.getLowestFreight()!=null && latest == 1) {
//									cell1.setCellFormula("SUM(I14:I" + (lastRow2 + 1)
//											+ ")+"+clientQuote.getLowestFreight());
//								}else {
//									cell1.setCellFormula("SUM(I14:I" + (lastRow2 + 1)
//											+ ")");
//								}
//								
//							}
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$TERMS".equals(str)) {
							cell1.setCellValue(clientInvoiceExcelVo.getInvoiceTerms());
						}else if ("$FERIGHT".equals(str)) {
							if(latest == 0 && !clientInvoice.getInvoiceType().equals(0)){
								cell1.setCellValue(0);
							}else {
								Integer clientQuoteId = clientInvoiceExcelVo.getClientQuoteId();
								ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientQuoteId);
								Double orderCount = clientOrderElementService.getTotalAmount(clientInvoiceExcelVo.getClientOrderId());
								Double count = clientQuoteService.getQuoteCount(clientQuoteId);
//								if (count.equals(orderCount)) {
									if(clientOrder.getFreight()!=null){
										cell1.setCellValue(clientOrder.getFreight());
									}else {
										cell1.setCellValue(0);
									}
//								}else if (count>orderCount) {
//									if (clientQuote.getLowestFreight()!=null) {
//										cell1.setCellValue(clientQuote.getLowestFreight());
//									}else {
//										cell1.setCellValue(0);
//									}
//								}
							}
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else if ("$TOTAL_L/C".equals(str)) {
							cell1.setCellFormula("SUM(G14:G" + (lastRow2 + 1)
									+ ")");
							cell1.setCellStyle(currencyFormat(clientInvoiceExcelVo.getCurrencyValue(),wb,cell2.getCellStyle()));
						}else {
							cell1.setCellValue(str);
						}
						break;
					}
				}
			}
		}
		if (clientInvoiceExcelVo.getInvoiceTempletValue().equals("模板二") ||
				clientInvoiceExcelVo.getInvoiceTempletValue().equals("模板五") || clientInvoiceExcelVo.getInvoiceTempletValue().equals("模板一")) {
			int lastRow3 = sheet.getLastRowNum()+1;
			wb.setPrintArea(0, "$A$1:$G$"+lastRow3);
		}else{
			int lastRow3 = sheet.getLastRowNum()+1;
			wb.setPrintArea(0, "$A$1:$J$"+lastRow3);
		}
		
	}
	
	private CellStyle currencyFormat(String value,POIExcelWorkBook wb,CellStyle Style){
		DataFormat format= wb.createDataFormat();  
		CellStyle cellStyle = wb.createCellStyle(); 
		if(null!=value){
			if(value.equals("人民币")||value=="人民币"){
				Style.setDataFormat(format.getFormat("¥#,##0.00"));  
				Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			}else if(value.equals("美元")||value=="美元"){
				Style.setDataFormat(format.getFormat("$#,##0.00"));
				Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			}else if(value.equals("欧元")||value=="欧元"){
				Style.setDataFormat(format.getFormat("€#,##0.00"));  
				Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			}else if(value.equals("英镑")||value=="英镑"){
				Style.setDataFormat(format.getFormat("￡#,##0.00"));
				Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			}else if(value.equals("港币")||value=="港币"){
				Style.setDataFormat(format.getFormat("HK$#,##0.00"));  
				Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
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

	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_FOLDER+File.separator+"sampleoutput";
		
	}

	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		
		 return number+"_"+"client_invoice"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "client_invoice";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
