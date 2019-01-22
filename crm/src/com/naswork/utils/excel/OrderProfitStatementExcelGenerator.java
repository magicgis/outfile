package com.naswork.utils.excel;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.marketing.controller.clientquote.ProfitStatementVo;
import com.naswork.service.ClientQuoteService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;

/**
 * 这是例子程序 在写实际的excel处理时，如下几个步骤
 * 1）写一个类集成ExcelGeneratorBase并提供所有abstract函数的实现，具体每个函数的需求请看具体函数的注释
 * 2）把这个类注册到ExcelGeneratorMapConstant类当中
 * 
 * @author eyaomai
 *
 */
@Service("orederPprofitStatementExcelGenerator")
public class OrderProfitStatementExcelGenerator extends ExcelGeneratorBase {
	@Resource
	private ClientQuoteService clientQuoteService;
//	private List<ClientQuoteVo> clientQuote;
//	private List<ProfitStatementVo> cqelist;
//	private List<ClientQuoteElementVo> sqeList;
//	private List<ClientOrderVo> clientOrderList;
//	private List<String> supplierCodeList;

	/**
	 * 获取数据，即根据ywId来获取数据，因为对于每个实体类，表名和主键列名实际上已知，无需作为参数传入
	 */
//	@Override
//	public void fetchData(String ywId) {
//		clientQuote = clientQuoteService.findbyclientquoteid(ywId);
//		cqelist = clientQuoteService.findProfitStatement(ywId);
//		sqeList = new ArrayList<ClientQuoteElementVo>();
//		supplierCode = new ArrayList<String>();
//		Set<String> supplierCodeSet = new TreeSet<String>();
//		for (ProfitStatementVo profitStatementVo : cqelist) {
//			List<ClientQuoteElementVo> List = clientQuoteService
//					.findByCieId(profitStatementVo.getClientInquiryElementId());
//			
//			sqeList.addAll(List);
//			for (ClientQuoteElementVo clientQuoteElementVo : sqeList) {
//				if (!supplierCode.contains(clientQuoteElementVo.getSupplierCode())) {
//					supplierCode.add(clientQuoteElementVo.getSupplierCode());
//				}
//			}
//			sqeMap = new HashMap<String, Map>();
//			if (sqeList != null) {
//				for (int j = 0; j < sqeList.size(); j++) {
//					Map sqe = (Map) sqeList.get(j);
//					String supplierCode =MapUtils
//							.getString(
//									sqe,
//									"supplier_code");
//					sqeMap.put(supplierCode, sqe);
//					supplierCodeSet.add(supplierCode);
//				}
//				String supplierCode = profitStatementVo.getSupplierCode();
//				if (supplierCode != null) {
//					Map map = new HashMap();
//					map.put("supplier_code", supplierCode);
//					map.put("base_price", profitStatementVo.getQuoteBasePrice());
//					sqeMap.put(supplierCode, map);
//					supplierCodeSet.add(supplierCode);
//				}
//			}

//		}
//		if (cqelist != null) {
//			clientOrderList = clientQuoteService.findByCqId(ywId);
//		}
//	}

	/**
	 * 获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator + "exceltemplate" + File.separator + "Profit.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		List<ClientQuoteVo> clientQuote = clientQuoteService.findbyclientquoteid(ywId);
		List<ProfitStatementVo> cqelist = clientQuoteService.findOrderProfitStatement(ywId);
		Double cqex=0.0;
		Double coex=0.0;
		Double sqex=0.0;
		String coCurrency="";
		String sqCurrency="";
		if(cqelist.size()>0){
		 cqex=cqelist.get(0).getCqExchangeRate();
		 coex=cqelist.get(0).getCoExchangeRate();
		  sqex=cqelist.get(0).getSqExchangeRate();
		  coCurrency=cqelist.get(0).getCoCurrencyValue();
		  sqCurrency=cqelist.get(0).getSqCurrencyValue();
		}
		CellStyle cellStyle = wb.createCellStyle();  
        DataFormat format= wb.createDataFormat();  
		List<ClientQuoteElementVo> sqeList = new ArrayList<ClientQuoteElementVo>();
		List<ClientOrderVo> clientOrderList=new ArrayList<ClientOrderVo>();
		List<String> supplierCodeList = new ArrayList<String>();
		Set<String> supplierCodeSet = new TreeSet<String>();
		for (ProfitStatementVo profitStatementVo : cqelist) {
			List<ClientQuoteElementVo> List = clientQuoteService
					.findOrderDataByCieId(profitStatementVo.getClientInquiryElementId());
			
			sqeList.addAll(List);
			for (ClientQuoteElementVo clientQuoteElementVo : sqeList) {
				if (!supplierCodeList.contains(clientQuoteElementVo.getSupplierCode())) {
					supplierCodeList.add(clientQuoteElementVo.getSupplierCode());
				}
			}
		
		}
		if (cqelist != null) {
			clientOrderList = clientQuoteService.findByCqId(ywId);
		}
		
		boolean hasClientOrder = false;
		if (clientOrderList != null && !clientOrderList.isEmpty() && !clientOrderList.equals("")) {
			hasClientOrder = true;
		}
		Sheet sheet = wb.getSheetAt(0);
		sheet.setForceFormulaRecalculation(true);
		Sheet supplierSheet = wb.getSheetAt(1);
		Sheet totalSheet = wb.getSheetAt(2);

		// set header
		sheet.getHeader().setCenter(clientQuote.get(0).getQuoteNumber());

		// set table header
		int firstRowNum = 0;
		Row headRow = sheet.getRow(firstRowNum);
		Row supplierSheetFirstRow = supplierSheet.getRow(0);
		Row supplierSheetLastRow = supplierSheet.getRow(1);
		Cell supplierCellTmpl = supplierSheetFirstRow.getCell(0);
		Collections.sort(supplierCodeList);
		String[] supplierCodes = supplierCodeList.toArray(new String[0]);
		char quoteTotalPriceColumn = (char) ('G' + supplierCodes.length);
		char storagePriceColumn = (char) (quoteTotalPriceColumn + 1);
		char clientQuotePriceColumn = (char) (quoteTotalPriceColumn + 2);
		char clientOrderPriceColumn = (char) (quoteTotalPriceColumn + 3);
		char clientOrderTotalPriceColumn = (char) (quoteTotalPriceColumn + 5);
		for (int i = 0; i < supplierCodes.length; i++) {
			Cell supplierCodeCell = headRow.createCell(6 + i);
			supplierCodeCell.setCellStyle(supplierCellTmpl.getCellStyle());
			this.write(supplierCodeCell, supplierCodes[i]);
		}
		for (int i = 0; i < 6; i++) {
			Cell cell = headRow.createCell(6 + supplierCodes.length + i);
			this.copy(supplierSheetFirstRow.getCell(i + 1), cell);
		}
		int lastRowNum = 1;
		Row lastRow = sheet.getRow(lastRowNum);
		sheet.removeRow(lastRow);
		BigDecimal[] sqeTotalPrices = new BigDecimal[supplierCodes.length];
		for (int i = 0; i < sqeTotalPrices.length; i++) {
			sqeTotalPrices[i] = BigDecimal.ZERO;
		}
		BigDecimal[] soeTotalPrices = new BigDecimal[supplierCodes.length];
		for (int i = 0; i < sqeTotalPrices.length; i++) {
			soeTotalPrices[i] = BigDecimal.ZERO;
		}
		BigDecimal totalQuotePrice = BigDecimal.ZERO;
		BigDecimal totalClientQuotePrice = BigDecimal.ZERO;
		BigDecimal totalOrderPrice = BigDecimal.ZERO;
		BigDecimal totalQuoteOrderPrice = BigDecimal.ZERO;
		BigDecimal totalStoragePrice = BigDecimal.ZERO;
		
		if (cqelist != null && cqelist.size() > 0) {
			for (int i = 0; i < cqelist.size(); i++) {
				
				if(coCurrency.equals(sqCurrency)&&coCurrency.equals("美元")&&sqCurrency.equals("美元")){
					cqelist.get(i).setQuoteBasePrice(cqelist.get(i).getQuoteBasePrice()/sqex);
					cqelist.get(i).setQuoteTotalPrice(cqelist.get(i).getQuoteTotalPrice()/sqex);
					if(null!=cqelist.get(i).getBaseStoragePrice()){
						cqelist.get(i).setBaseStoragePrice(cqelist.get(i).getBaseStoragePrice()/sqex);
					}
					cqelist.get(i).setClientQuoteBasePrice(cqelist.get(i).getClientQuoteBasePrice()/cqex);
					cqelist.get(i).setOrderBasePrice(cqelist.get(i).getOrderBasePrice()/coex);
					cqelist.get(i).setOrderTotalPrice(cqelist.get(i).getOrderTotalPrice()/coex);
					cqelist.get(i).setClientQuoteTotalPrice(cqelist.get(i).getClientQuoteTotalPrice()/cqex);
				}
				
				Map map=new HashMap();
				for (int k = 0; k < sqeList.size(); k++) {
					if(sqeList.get(k).getClientInquiryElementId().equals(cqelist.get(i).getClientInquiryElementId())){
						map.put(sqeList.get(k).getSupplierCode(), sqeList.get(k).getSupplierCode());
						
						if(coCurrency.equals(sqCurrency)&&coCurrency.equals("美元")&&sqCurrency.equals("美元")){
							sqeList.get(k).setBasePrice(sqeList.get(k).getBasePrice()/sqex);
						}
					
						if(map.containsKey(sqeList.get(k).getSupplierCode()+"baseprice")){
							if(sqeList.get(k).getId().equals(cqelist.get(i).getSupplierQuoteElementId())){
								map.put(sqeList.get(k).getSupplierCode()+"baseprice", sqeList.get(k).getBasePrice());
							}
						}else{
							map.put(sqeList.get(k).getSupplierCode()+"baseprice", sqeList.get(k).getBasePrice());
						}
						
					}
				}
//				ProfitStatementVo l=cqelist.get(i);
				String supplierCode = cqelist.get(i).getSupplierCode();
				BigDecimal orderAmount =null;
				if(null!=cqelist.get(i).getOrderAmount()){
					 orderAmount = new BigDecimal(cqelist.get(i).getOrderAmount());
				}
				
				if (orderAmount == null) {
					orderAmount = BigDecimal.ZERO;
				}
				BigDecimal storageAmount =null;
				if(null!=cqelist.get(i).getStorageAmount()){
					storageAmount = new BigDecimal(cqelist.get(i).getStorageAmount());
				}
//				storageAmount = new BigDecimal(cqelist.get(i).getStorageAmount());
				if (storageAmount == null) {
					storageAmount = BigDecimal.ZERO;
				}
				BigDecimal quoteAmount = new BigDecimal(cqelist.get(i).getQuoteAmount());
				BigDecimal quoteBasePrice = new BigDecimal(cqelist.get(i).getQuoteBasePrice());
				BigDecimal clientQuoteBasePrice = new BigDecimal(cqelist.get(i).getClientQuoteBasePrice());
				BigDecimal orderBasePrice =null;
				if(null!=cqelist.get(i).getOrderBasePrice()){
					 orderBasePrice = new BigDecimal(cqelist.get(i).getOrderBasePrice());
				}
				BigDecimal storageBasePrice =null;
				if(null!=cqelist.get(i).getBaseStoragePrice()){
					storageBasePrice = new BigDecimal(cqelist.get(i).getBaseStoragePrice());
				}
				BigDecimal quoteTotalPrice = new BigDecimal(cqelist.get(i).getQuoteTotalPrice());
				BigDecimal clientQuoteTotalPrice = new BigDecimal(cqelist.get(i).getClientQuoteTotalPrice());
				BigDecimal orderTotalPrice = null;
				if(null!=cqelist.get(i).getOrderTotalPrice()){
					orderTotalPrice = new BigDecimal(cqelist.get(i).getOrderTotalPrice());
				}
				BigDecimal storageTotalPrice = storageBasePrice;
				if (quoteTotalPrice != null) {
					totalQuotePrice = totalQuotePrice.add(quoteTotalPrice);
				}
				if (clientQuoteTotalPrice != null) {
					totalClientQuotePrice = totalClientQuotePrice.add(clientQuoteTotalPrice);
				}
				if (orderTotalPrice != null) {
					totalOrderPrice = totalOrderPrice.add(orderTotalPrice);
				}
				if (storageTotalPrice != null) {
					totalStoragePrice = totalStoragePrice.add(storageTotalPrice);
				}
				Row elementRow = sheet.createRow(lastRowNum + i);
				elementRow.setHeight(lastRow.getHeight());
				int rowNum = lastRowNum + i + 1;
				for (int cellnum = 0; cellnum <= 5; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						elementCell.setCellType(cell.getCellType());
//						switch (cellnum) {
//						case 0:
//							this.write(elementCell, cqelist.get(i).getItem());
//							break;
//						case 1:
//							this.write(elementCell, cqelist.get(i).getInquiryPartNumber());
//							break;
//						case 2:
//							this.write(elementCell, cqelist.get(i).getInquiryDescription());
//							break;
//						case 3:
//							this.write(elementCell, orderAmount);
//							break;
//						case 4:
//							this.write(elementCell, storageAmount);
//							break;
//						case 5:
//							this.write(elementCell, quoteAmount);
//							break;
//						}
						if(cellnum==0){
							this.write(elementCell, cqelist.get(i).getItem());
						}else if(cellnum==1){
							this.write(elementCell, cqelist.get(i).getInquiryPartNumber());
						}else if(cellnum==2){
							this.write(elementCell, cqelist.get(i).getInquiryDescription());
						}else if(cellnum==3){
							this.write(elementCell, orderAmount);
						}else if(cellnum==4){
							this.write(elementCell, storageAmount);
						}else if(cellnum==5){
							this.write(elementCell, quoteAmount);
						}
					}
				}
//				l.getSupplierCode();
//				l.getQuoteBasePrice();
				Cell supplierQuotePriceCellTmpl = supplierSheetLastRow.getCell(0);
				Cell boldPriceCellTmpl = supplierSheetLastRow.getCell(6);
				Font font=wb.createFont();
				Font font2=wb.createFont();
				CellStyle boldPriceCellTmplStyle = wb.createCellStyle(); 
				font2.setFontName("Verdana");    
				font2.setFontHeightInPoints((short) 9);
				cellStyle.setFont(font2);
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
				boldPriceCellTmplStyle.setFont(font);
				if(coCurrency.equals("美元")&&sqCurrency.equals("美元")){
					boldPriceCellTmplStyle.setDataFormat(format.getFormat("$#,##0.00"));  
				}else{
					boldPriceCellTmplStyle.setDataFormat(format.getFormat("¥#,##0.00"));  
				}
				boldPriceCellTmpl.setCellStyle(boldPriceCellTmplStyle);
				
				if(coCurrency.equals("美元")&&sqCurrency.equals("美元")){
		            cellStyle.setDataFormat(format.getFormat("$#,##0.00"));  
				}else{
					 cellStyle.setDataFormat(format.getFormat("¥#,##0.00"));  
				}
				for (int j = 0; j < supplierCodes.length; j++) {
//					if(sqeList.get(i).getSupplierCode().equals(supplierCodes[j])){
					Object sqe = map.get(supplierCodes[j]);
					Cell supplierCell = elementRow.createCell(6 + j);
//					supplierCell.setCellStyle(supplierQuotePriceCellTmpl.getCellStyle());
					  supplierCell.setCellStyle(cellStyle); 
					supplierCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					if (sqe != null) {
						BigDecimal sqePrice = new BigDecimal(map.get(sqe.toString()+"baseprice").toString());
						BigDecimal sqePriceT = sqePrice.multiply(orderAmount.subtract(storageAmount));
						sqeTotalPrices[j] = sqeTotalPrices[j].add(sqePriceT);
						if (supplierCodes[j].equals(supplierCode)) {
							if (orderTotalPrice != null&&!"".equals(orderTotalPrice)&&orderTotalPrice!=BigDecimal.ZERO) {
								soeTotalPrices[j] = soeTotalPrices[j].add(sqePriceT);
							}
						}
						if (supplierCodes[j].equals(supplierCode)) {
							supplierCell.setCellStyle(boldPriceCellTmpl.getCellStyle());
						}
						this.write(supplierCell, sqePrice);
//					}
				}};
				for (int j = 0; j < 6; j++) {
					Cell cell = elementRow.createCell(6 + supplierCodes.length + j);
					
					if(j!=4){
						cell.setCellStyle(cellStyle);
						}else{
							cell.setCellStyle(supplierSheetLastRow.getCell(j + 1).getCellStyle());
						}
					cell.setCellType(supplierSheetLastRow.getCell(j + 1).getCellType());
//					switch (j) {
//					case 0:
//						this.write(cell, quoteTotalPrice == null ? BigDecimal.ZERO : quoteTotalPrice);
//						break;
//					case 1:
//						this.write(cell, storageTotalPrice == null ? BigDecimal.ZERO : storageTotalPrice);
//						break;
//					case 2:
//						this.write(cell, clientQuoteBasePrice == null ? BigDecimal.ZERO : clientQuoteBasePrice);
//						break;
//					case 3:
//						this.write(cell, orderBasePrice == null ? BigDecimal.ZERO : orderBasePrice);
//						break;
//					case 4:
//						if (hasClientOrder && orderBasePrice != null) {
//							cell.setCellFormula("(" + clientOrderTotalPriceColumn + rowNum + "-" + storagePriceColumn
//									+ rowNum + "*E" + rowNum + "-" + quoteTotalPriceColumn + rowNum + ")/"
//									+ clientOrderTotalPriceColumn + rowNum);
//						} else {
//							cell.setCellFormula(
//									"(" + clientQuotePriceColumn + rowNum + "*F" + rowNum + "-" + quoteTotalPriceColumn
//											+ rowNum + ")/(" + clientQuotePriceColumn + rowNum + "*F" + rowNum + ")");
//						}
//						break;
//					case 5:
//						cell.setCellFormula("" + clientOrderPriceColumn + rowNum + "*D" + rowNum);
//						break;
//					}
					if(j==0){
						this.write(cell, quoteTotalPrice == null ? "" : quoteTotalPrice);
					}else if(j==1){
						this.write(cell, storageTotalPrice == null ? "" : storageTotalPrice);
					}else if(j==2){
						this.write(cell, clientQuoteBasePrice == null ? "" : clientQuoteBasePrice);
					}else if(j==3){
						this.write(cell, orderBasePrice == null ? "" : orderBasePrice);
					}else if(j==4){
						if (hasClientOrder && orderBasePrice != null) {
							cell.setCellFormula("(" + clientOrderTotalPriceColumn + rowNum + "-" + /*storagePriceColumn
									+ rowNum + "*E" + rowNum + "-" +*/ quoteTotalPriceColumn + rowNum + ")/"
									+ clientOrderTotalPriceColumn + rowNum);
						} else {
							cell.setCellFormula(
									"(" + clientQuotePriceColumn + rowNum + "*F" + rowNum + "-" + quoteTotalPriceColumn
											+ rowNum + ")/(" + clientQuotePriceColumn + rowNum + "*F" + rowNum + ")");
						}
					}else if(j==5){
						cell.setCellFormula("" + clientOrderPriceColumn + rowNum + "*D" + rowNum);
					}
				}
			}
			Row totalRowTmpl = totalSheet.getRow(0);
			Row totalRow = sheet.createRow(cqelist.size() + 1);
			for (int cellnum = 0; cellnum <= 5; cellnum++) {
				Cell cell = totalRow.createCell(cellnum);
				this.copy(totalRowTmpl.getCell(cellnum), cell);
			}
			for (int j = 0; j < sqeTotalPrices.length; j++) {
				Cell cell = totalRow.createCell(6 + j);
				cell.setCellStyle(cellStyle);
				this.write(cell, sqeTotalPrices[j]);
			}
			for (int j = 0; j < 6; j++) {
				Cell cell = totalRow.createCell(6 + sqeTotalPrices.length + j);
			
				
				if(j!=4){
					cell.setCellStyle(cellStyle);
					}else{
						cell.setCellStyle(totalRowTmpl.getCell(7 + j).getCellStyle());
					}
//				switch (j) {
//				case 0:
//					cell.setCellFormula("SUM(" + quoteTotalPriceColumn + "2:" + quoteTotalPriceColumn
//							+ (totalRow.getRowNum()) + ")");
//					break;
//				case 1:
//					this.write(cell, totalStoragePrice == null ? BigDecimal.ZERO : totalStoragePrice);
//					break;
//				case 2:
//					this.write(cell, totalClientQuotePrice == null ? BigDecimal.ZERO : totalClientQuotePrice);
//				case 3:
//					break;
//				case 4:
//					break;
//				case 5:
//					cell.setCellFormula("SUM(" + clientOrderTotalPriceColumn + "2:" + clientOrderTotalPriceColumn
//							+ (totalRow.getRowNum()) + ")");
//					break;
//				}
				if(j==0){
					cell.setCellFormula("SUM(" + quoteTotalPriceColumn + "2:" + quoteTotalPriceColumn
							+ (totalRow.getRowNum()) + ")");
				}else if(j==1){
					this.write(cell, totalStoragePrice == null ? BigDecimal.ZERO : totalStoragePrice);
				}else if(j==2){
					this.write(cell, totalClientQuotePrice == null ? BigDecimal.ZERO : totalClientQuotePrice);
				}else if(j==3){
					
				}else if(j==4){
					
				}else if(j==5){
					cell.setCellFormula("SUM(" + clientOrderTotalPriceColumn + "2:" + clientOrderTotalPriceColumn
							+ (totalRow.getRowNum()) + ")");
				}
			}
			Row orderTotalRowTmpl = totalSheet.getRow(1);
			Row orderTotalRow = sheet.createRow(cqelist.size() + 2);
			for (int cellnum = 0; cellnum <= 5; cellnum++) {
				Cell cell = orderTotalRow.createCell(cellnum);
				this.copy(orderTotalRowTmpl.getCell(cellnum), cell);
			}
			for (int j = 0; j < soeTotalPrices.length; j++) {
				Cell cell = orderTotalRow.createCell(6 + j);
				cell.setCellStyle(cellStyle);
				this.write(cell, soeTotalPrices[j]);
				totalQuoteOrderPrice = totalQuoteOrderPrice.add(soeTotalPrices[j]);
			}
			for (int j = 0; j < 6; j++) {
				Cell cell = orderTotalRow.createCell(6 + sqeTotalPrices.length + j);
				
				if(j!=4){
					cell.setCellStyle(cellStyle);
					}else{
						cell.setCellStyle(orderTotalRowTmpl.getCell(7 + j).getCellStyle());
					}

				//				switch (j) {
//				case 0:
//					cell.setCellFormula("SUM(G" + (orderTotalRow.getRowNum() + 1) + ":"
//							+ (char) ('A' + cell.getColumnIndex() - 1) + ((orderTotalRow.getRowNum() + 1)) + ")");
//					break;
//				case 1:
//					this.write(cell, totalStoragePrice == null ? BigDecimal.ZERO : totalStoragePrice);
//					break;
//				case 2:
//					break;
//				case 3:
//					break;
//				case 4:
//					cell.setCellFormula("(" + clientOrderTotalPriceColumn + (orderTotalRow.getRowNum() + 1) + "-"
//							+ quoteTotalPriceColumn + (orderTotalRow.getRowNum() + 1) + "-" + storagePriceColumn
//							+ (orderTotalRow.getRowNum() + 1) + ")/" + clientOrderTotalPriceColumn
//							+ (orderTotalRow.getRowNum() + 1));
//					break;
//				case 5:
//					cell.setCellFormula("" + (char) ('A' + cell.getColumnIndex()) + orderTotalRow.getRowNum());
//					break;
//				}
				if(j==0){
					cell.setCellFormula("SUM(G" + (orderTotalRow.getRowNum() + 1) + ":"
							+ (char) ('A' + cell.getColumnIndex() - 1) + ((orderTotalRow.getRowNum() + 1)) + ")");
				}else if(j==1){
					this.write(cell, totalStoragePrice == null ? "" : totalStoragePrice);
				}else if(j==2){
					
				}else if(j==3){
					
				}else if(j==4){
					cell.setCellFormula("(" + clientOrderTotalPriceColumn + (orderTotalRow.getRowNum() + 1) + "-"
							+ quoteTotalPriceColumn + (orderTotalRow.getRowNum() + 1) +/* "-" + storagePriceColumn
							+ (orderTotalRow.getRowNum() + 1) +*/ ")/" + clientOrderTotalPriceColumn
							+ (orderTotalRow.getRowNum() + 1));
				}else if(j==5){
					cell.setCellFormula("" + (char) ('A' + cell.getColumnIndex()) + orderTotalRow.getRowNum());
				}

			}
			Row row2Tmpl = totalSheet.getRow(2);
			Row row2 = sheet.createRow(cqelist.size() + 3);
			for (int cellNum = 0; cellNum < 3; cellNum++) {
				Cell cell = row2.createCell(cellNum);
				this.copy(row2Tmpl.getCell(cellNum), cell);
				if (cellNum == 2) {
					this.write(cell, Calendar.getInstance().getTime());
				}
			}
			Cell profitLabelCell = row2.createCell(supplierCodes.length + 10);
			this.copy(row2Tmpl.getCell(11), profitLabelCell);
			Cell profitCell = row2.createCell(supplierCodes.length + 11);
//			profitCell.setCellStyle(row2Tmpl.getCell(12).getCellStyle());
			profitCell.setCellStyle(cellStyle);
			profitCell.setCellFormula("(" + clientOrderTotalPriceColumn + (orderTotalRow.getRowNum() + 1) + "-"
					+ storagePriceColumn + (orderTotalRow.getRowNum() + 1) + "-" + quoteTotalPriceColumn
					+ (orderTotalRow.getRowNum() + 1) + ")");

			Row row6Tmpl = totalSheet.getRow(6);
			Row row6 = sheet.createRow(cqelist.size() + 8);
			for (int cellNum = 0; cellNum < supplierCodes.length + 12; cellNum++) {
				Cell cell = row6.createCell(cellNum);
				this.copy(row6Tmpl.getCell(cellNum), cell);
			}
		}

		wb.setActiveSheet(0);
		wb.setSheetHidden(1, true);
		wb.setSheetHidden(2, true);
	}

	/**
	 * 获取输出文件全路径，默认来说其父路径为C:\mis\excel\
	 * 注意，一般来说只需替换后面部分，比如将sampleoutput换为其他路径，也可以按照具体需求给多层路径，比如"f1"+File.
	 * seperator+"f2" 注意，最后无需给路径分隔符
	 */
	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH + File.separator + FileConstant.EXCEL_FOLDER + File.separator
				+ "sampleoutput";

	}

	/**
	 * 获取输出文件名称（不包含路径和后缀），路径由fetchOutputFilePath方法给出，而后缀则由template文件决定 建议格式是
	 * <模块>_<用户登录名>_<日期>_<时间>，对于具体的类，可以根据需求增加更多信息
	 */
	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();

		return "Profit" + "_" + this.fetchUserName() + "_" + format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "client_order_pro";
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
		return "OrderProfitExcel";
	}

}
