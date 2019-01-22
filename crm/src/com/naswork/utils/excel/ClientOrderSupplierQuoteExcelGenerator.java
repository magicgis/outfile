package com.naswork.utils.excel;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SupplierWeatherOrderElementDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.HistoricalOrderPriceService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierQuoteElementService;
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
@Service("clientOrderSupplierQuoteExcelGenerator")
public class ClientOrderSupplierQuoteExcelGenerator extends ExcelGeneratorBase {

	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientService clientService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private HistoricalOrderPriceService historicalOrderPriceService;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private ClientInquiryElementDao clientInquiryElementDao;
	@Resource
	private SupplierWeatherOrderElementDao supplierWeatherOrderElementDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	String number = null;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"CompletedSupplierWeatherOrder.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		List<ClientOrderElementVo>  list=supplierOrderService.CompletedSupplierWeatherOrderData(Integer.parseInt(ywId));
		number=list.get(0).getOrderNumber();
		List<String> supplierCode=new ArrayList<String>();
		Map<String,Double> supplierPrice=new HashMap<String, Double>();
		List<ElementVo> arraylist=new ArrayList<ElementVo>();
		for (ClientOrderElementVo clientOrderElementVo : list) {
				
					List<ClientInquiry> slist=supplierQuoteService.findSupplierQuote(clientOrderElementVo.getClientInquiryId(),clientOrderElementVo.getItem());
					for (ClientInquiry clientInquiry : slist) {
						if(!supplierCode.contains(clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId())){
							supplierCode.add(clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId());
						}
						supplierPrice.put(clientOrderElementVo.getId()+"-"+clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId(), clientInquiry.getQuotePrice());
					}
					Collections.sort(supplierCode);
			Collections.sort(supplierCode);
		}
		int beginColIndex = 9;
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum+2);
		int codeCount = supplierCode.size();
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginColIndex, codeCount);
		
		//生成模板
		for(int dynamicIndex=0; dynamicIndex<supplierCode.size();  dynamicIndex++){
			int cellnum = beginColIndex+dynamicIndex;
			Cell cell = row.getCell(cellnum);
				cell.setCellValue(supplierCode.get(dynamicIndex));
		}
				
		for (int rowIndex = firstRowNum+3; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, codeCount);
			for(int dynamicIndex=0; dynamicIndex<supplierCode.size();  dynamicIndex++){
				int cellnum = beginColIndex+dynamicIndex;
				Cell cell = row.getCell(cellnum);
					cell.setCellValue("$"+supplierCode.get(dynamicIndex));
			}
		}
		//int c = 0;
		//插入数据
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
//		sheet.removeRow(lastRow);
		if (list != null
				&& list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row elementRow = sheet.createRow(lastRowNum + i);
				ClientOrderElementVo elementVo = list.get(i);
				
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					if (cell != null) {
//						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("QUOTE_NUMBER")){
								Object value = elementVo.getQuoteNumber();
								this.write(elementCell, value);
							}else if (key.equals("ITEM")) {
								Object value = elementVo.getItem();
								this.write(elementCell, value);
							} else if(key.equals("CSN")){
								Object value = elementVo.getCsn();
								this.write(elementCell, value);
							} else if(key.equals("AIR_TYPE")){
								Object value = elementVo.getAirTypeCode();
								this.write(elementCell, value);
							} else if (key.equals("PART_NUMBER")) {
								Object value = elementVo.getPartNumber();
								this.write(elementCell, value);
							} else if(key.equals("DESCRIPTION")){
								Object value = elementVo.getDescription();
								this.write(elementCell, value);
							}else if (key.equals("UNIT")) {
								Object value = elementVo.getUnit();
								this.write(elementCell, value);
							}else if (key.equals("AMOUNT")) {
								Object value = elementVo.getQuoteAmount();
								this.write(elementCell, value);
							}else if (key.equals("ORDER_AMOUNT")) {
								Object value = elementVo.getClientOrderAmount();
								this.write(elementCell, value);
							}else if (key.equals("WEATHER_QUOTE_SUPPLIER")) {
								Object value = elementVo.getSupplierCode();
								this.write(elementCell, value);
							}else if (key.equals("WEATHER_QUOTE_PRICE")) {
								BigDecimal sqePrice=new BigDecimal(elementVo.getSupplierQuotePrice());
								BigDecimal price=new BigDecimal(elementVo.getClientQuotePrice());
								BigDecimal sqER=new BigDecimal(elementVo.getSupplierQuoteExchangeRate());
								BigDecimal er=new BigDecimal(elementVo.getClientQuoteExchangeRate());
								Integer sqcurrencyId=elementVo.getSqCurrencyId();
								Integer cqcurrencyId=elementVo.getCurrencyId();
								if(!sqcurrencyId.equals(cqcurrencyId)){
									sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,er);//算出利润
								}
								Object value = sqePrice;
								this.write(elementCell, value);
							}else if (key.equals("WEATHER_ORDER_SUPPLIER")) {
								if(null!=elementVo.getWeatherOrderSupplierQuoteElementId()){
									SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(elementVo.getWeatherOrderSupplierQuoteElementId());
									Object value = element.getCode();
									this.write(elementCell, value);
								}
							}else if (key.equals("WEATHER_ORDER_PRICE")) {
								if(null!=elementVo.getWeatherOrderPrice() && elementVo.getWeatherOrderSupplierQuoteElementId() != null){
									BigDecimal soePrice=new BigDecimal(elementVo.getWeatherOrderPrice());
									BigDecimal price=new BigDecimal(elementVo.getClientOrderPrice());
									SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(elementVo.getWeatherOrderSupplierQuoteElementId());
									BigDecimal soER=new BigDecimal(element.getExchangeRate());
									BigDecimal er=new BigDecimal(elementVo.getExchangeRate());
									Integer socurrencyId=element.getCurrencyId();
									Integer cocurrencyId=elementVo.getCoCurrencyId();
									if(!socurrencyId.equals(cocurrencyId)){
										soePrice = clientQuoteService.caculatePrice(soePrice, soER,er);//算出利润
									}
									Object value = soePrice;
									this.write(elementCell, value);
								}
							}else if (key.equals("WEATHER_ORDER_AMOUNT")) {
								Object value = elementVo.getWeatherOrderAmount();
								this.write(elementCell, value);
							}else if (key.equals("TOTAL_ORDER")) {
								if(null!=elementVo.getWeatherOrderPrice()&&null!=elementVo.getWeatherOrderAmount() && elementVo.getWeatherOrderSupplierQuoteElementId() != null){
									BigDecimal soePrice=new BigDecimal(elementVo.getWeatherOrderPrice());
									SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(elementVo.getWeatherOrderSupplierQuoteElementId());
									BigDecimal soER=new BigDecimal(element.getExchangeRate());
									BigDecimal coer=new BigDecimal(elementVo.getExchangeRate());
									Integer socurrencyId=element.getCurrencyId();
									Integer cocurrencyId=elementVo.getCoCurrencyId();
									if(!socurrencyId.equals(cocurrencyId)){
										soePrice = clientQuoteService.caculatePrice(soePrice, soER,coer);//算出利润
									}
									Object value =  soePrice.doubleValue()* elementVo.getWeatherOrderAmount();
									this.write(elementCell, value);
								}
							}else if (key.equals("CLIENT_QUOTE_PRICE")) {
								Object value = elementVo.getClientQuotePrice();
								this.write(elementCell, value);
							}else if (key.equals("WEATHER_ORDER_REMARK")) {
								Object value = elementVo.getWeatherOrderRemark();
								this.write(elementCell, value);
							}else if (key.equals("TOTAL_QUOTE")) {
								BigDecimal sqePrice=new BigDecimal(elementVo.getSupplierQuotePrice());
								BigDecimal sqER=new BigDecimal(elementVo.getSupplierQuoteExchangeRate());
								BigDecimal cqer=new BigDecimal(elementVo.getClientQuoteExchangeRate());
								Integer sqcurrencyId=elementVo.getSqCurrencyId();
								Integer cqcurrencyId=elementVo.getCurrencyId();
								if(!sqcurrencyId.equals(cqcurrencyId)){
									sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,cqer);//算出利润
								}
								Object value = elementVo.getClientQuotePrice()* elementVo.getQuoteAmount();
								this.write(elementCell, value);
							}else if (key.equals("FIXED_COST")) {
								Object value = elementVo.getFixedCost();
								this.write(elementCell, value);
							}else if (key.equals("PRICE_DIFFERENCE")) {
								if(null!=elementVo.getWeatherOrderPrice() && elementVo.getWeatherOrderSupplierQuoteElementId() != null){
									BigDecimal sqePrice=new BigDecimal(elementVo.getSupplierQuotePrice());
									BigDecimal sqER=new BigDecimal(elementVo.getSupplierQuoteExchangeRate());
									BigDecimal cqer=new BigDecimal(elementVo.getClientQuoteExchangeRate());
									Integer sqcurrencyId=elementVo.getSqCurrencyId();
									Integer cqcurrencyId=elementVo.getCurrencyId();
									if(!sqcurrencyId.equals(cqcurrencyId)){
										sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,cqer);//算出利润
									}
									BigDecimal soePrice=new BigDecimal(elementVo.getWeatherOrderPrice());
									SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(elementVo.getWeatherOrderSupplierQuoteElementId());
									BigDecimal soER=new BigDecimal(element.getExchangeRate());
									BigDecimal coer=new BigDecimal(elementVo.getExchangeRate());
									Integer socurrencyId=element.getCurrencyId();
									Integer cocurrencyId=elementVo.getCoCurrencyId();
									if(!socurrencyId.equals(cocurrencyId)){
										soePrice = clientQuoteService.caculatePrice(soePrice, soER,coer);//算出利润
									}
									Object value =sqePrice.subtract(soePrice);
									this.write(elementCell, value);
								}
							}else if (key.equals("PROFIT_MARGIN")) {
								BigDecimal freight=new BigDecimal(elementVo.getFreight());
								BigDecimal bankCharges=new BigDecimal(elementVo.getCqeBankCharges());
								BigDecimal counterFee=new BigDecimal(elementVo.getCounterFee());
								BigDecimal fixedCost=new BigDecimal(elementVo.getCqeFixedCost());
								BigDecimal sqePrice=new BigDecimal(elementVo.getSupplierQuotePrice());
								BigDecimal price=new BigDecimal(elementVo.getClientQuotePrice());
								BigDecimal sqER=new BigDecimal(elementVo.getSupplierQuoteExchangeRate());
								BigDecimal er=new BigDecimal(elementVo.getClientQuoteExchangeRate());
								Integer sqcurrencyId=elementVo.getSqCurrencyId();
								Integer cqcurrencyId=elementVo.getCurrencyId();
								sqePrice=sqePrice.add(freight).add(counterFee);
								if(!sqcurrencyId.equals(cqcurrencyId)){
									sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,er);//算出利润
								}
								BigDecimal profitMargit=new BigDecimal(1.00).subtract((sqePrice).divide(price,2,BigDecimal.ROUND_HALF_UP)).subtract(fixedCost).subtract(bankCharges);
//								BigDecimal pm=profitMargit.multiply(new BigDecimal(100));//乘以100
								Object value = (profitMargit.multiply(new BigDecimal(100))).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								this.write(elementCell, value);
//								CellStyle cellStyle=wb.createCellStyle();
//								DataFormat format= wb.createDataFormat();
//								cellStyle.setDataFormat(format.getFormat("0.000%"));
//								elementCell.setCellStyle(cellStyle);
							}else if (key.equals("WEATHER_ORDER_PROFIT_MARGIN")) {
								if(null!=elementVo.getWeatherOrderPrice() && elementVo.getWeatherOrderSupplierQuoteElementId() != null){
									Double fixedCost=elementVo.getFixedCost();
									DecimalFormat df = new DecimalFormat("#.###");
										if (fixedCost < new Double(1)) {
											elementVo.setFixedCost(new Double(df.format(elementVo.getClientOrderPrice()*fixedCost)));
										}
									Double bankCharges=elementVo.getBankCharges()*elementVo.getClientOrderPrice();
									SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(elementVo.getWeatherOrderSupplierQuoteElementId());
									Double counterFee=element.getCounterFee();
									BigDecimal soePrice=new BigDecimal(elementVo.getWeatherOrderPrice()+counterFee);
									BigDecimal price=new BigDecimal(elementVo.getClientOrderPrice());
									BigDecimal soER=new BigDecimal(element.getExchangeRate());
									BigDecimal er=new BigDecimal(elementVo.getExchangeRate());
									Integer socurrencyId=element.getCurrencyId();
									Integer cocurrencyId=elementVo.getCoCurrencyId();
									if(!socurrencyId.equals(cocurrencyId)){
										soePrice = clientQuoteService.caculatePrice(soePrice, soER,er);//算出利润
									}
									Double profitMargin = ((elementVo.getClientOrderPrice()-elementVo.getFixedCost()-bankCharges-(soePrice.doubleValue()))/elementVo.getClientOrderPrice());
//									BigDecimal profitMargit=new BigDecimal(1.00).subtract((soePrice).divide(price,2,BigDecimal.ROUND_HALF_UP)).subtract(fixedCost);
	//								BigDecimal pm=profitMargit.multiply(new BigDecimal(100));//乘以100
									Object value = new BigDecimal(profitMargin*100).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
									this.write(elementCell, value);
//									CellStyle cellStyle=wb.createCellStyle();
//									DataFormat format= wb.createDataFormat();
//									cellStyle.setDataFormat(format.getFormat("0.000%"));
//									elementCell.setCellStyle(cellStyle);
								}
							}else if (key.equals("CLIENT_QUOTE_REMARK")) {
								Object value = elementVo.getQuoteRemark();
								this.write(elementCell, value);
							}else if (key.equals("STORAGE_AMOUNT")) {
								if(null!=elementVo.getOccupy()&&elementVo.getOccupy().equals(1)){
									Object value = elementVo.getStorageAmount();
									this.write(elementCell, value);
								}
							}else if (key.equals("STORAGE_PRICE")) {
								if(null!=elementVo.getOccupy()&&elementVo.getOccupy().equals(1)){
									Double price= elementVo.getStoragePrice();
									Double storageEr=0.0;
									Integer scurrencyId=0;
									Integer ccurrencyId=elementVo.getCurrencyId();
									if(elementVo.getStorageType().equals(1)||elementVo.getStorageType()==1){
										AddSupplierOrderElementVo supplierOrderElementVo=supplierOrderElementService.findByElementId(elementVo.getOaSupplierOrderElementId());
										 storageEr=supplierOrderElementVo.getExchangeRate();
										 scurrencyId=supplierOrderElementVo.getCurrencyId();
									}else{
										ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(elementVo.getOaImportPackageElementId().toString());
										 storageEr=importPackageElementVo.getExchangeRate();
										 scurrencyId=importPackageElementVo.getCurrencyId();
									}
									BigDecimal sqePrice=new BigDecimal(price);
									BigDecimal sqER=new BigDecimal(storageEr);
									BigDecimal er=new BigDecimal(elementVo.getExchangeRate());
								
									if(!scurrencyId.equals(ccurrencyId)){
										BigDecimal	storagePrice = clientQuoteService.caculatePrice(sqePrice, sqER,er);//算出利润
										price=storagePrice.doubleValue();
									}
									Object value =price;
									this.write(elementCell, value);
								}
							}else if (key.equals("STORAGE_TYPE")) {
								if(null!=elementVo.getOccupy()&&elementVo.getOccupy().equals(1)){
									Object value =null;
									if(elementVo.getStorageType().equals(1)){
										 value ="在途库存";
									}else{
										 value ="自有库存";
									}
									
									this.write(elementCell, value);
								}
							}else if(supplierCode.contains(key)){
								Object value=supplierPrice.get(elementVo.getId()+"-"+key);
								if(null!=value){
									BigDecimal sqePrice=new BigDecimal(value.toString());
									BigDecimal sqER=new BigDecimal(elementVo.getSupplierQuoteExchangeRate());
									BigDecimal er=new BigDecimal(elementVo.getClientQuoteExchangeRate());
									Integer sqcurrencyId=elementVo.getSqCurrencyId();
									Integer cqcurrencyId=elementVo.getCurrencyId();
									if(!sqcurrencyId.equals(cqcurrencyId)){
										value = clientQuoteService.caculatePrice(sqePrice, sqER,er);//算出利润
									}
								}
								this.write(elementCell, value);
							}
						}
					}
				}
				CellStyle cellStyle = wb.createCellStyle();
					Row row1 = sheet.getRow(0);
					Font font=wb.createFont();
					font.setFontHeightInPoints((short) 11);
					font.setBoldweight(Font.BOLDWEIGHT_BOLD);
					cellStyle.setFont(font);
					CellRangeAddress clientWeatherOrder=new CellRangeAddress(0, 1, 0, 8);     
					 sheet.addMergedRegion(clientWeatherOrder);  
					
					 CellRangeAddress supplierWeatherQuote=new CellRangeAddress(0, 1, 9, 8+supplierCode.size());     
					 sheet.addMergedRegion(supplierWeatherQuote);  
					 
					 CellRangeAddress clientWeatherQuote=new CellRangeAddress(0, 1, 9+supplierCode.size(), 8+8+supplierCode.size());     
					 sheet.addMergedRegion(clientWeatherQuote);  
					
					 CellRangeAddress storage=new CellRangeAddress(0, 1,  9+8+supplierCode.size(), 3+8+8+supplierCode.size());     
					 sheet.addMergedRegion(storage);  
				
					 CellRangeAddress supplierWeatherOrder=new CellRangeAddress(0, 1, 4+8+8+supplierCode.size(), 4+3+9+8+supplierCode.size());     
					 sheet.addMergedRegion(supplierWeatherOrder);  
					
					 CellRangeAddress other1=new CellRangeAddress(0, 1, 5+3+9+8+supplierCode.size(),2+4+3+9+8+supplierCode.size());     
					 sheet.addMergedRegion(other1);  
					
					 CellRangeAddress other2=new CellRangeAddress(0, 1,  3+4+3+9+8+supplierCode.size(),  1+2+4+3+9+8+supplierCode.size());     
					 sheet.addMergedRegion(other2);  
					
						Cell cell1=row1.createCell(0);
						cell1.setCellValue("预订单基本资料部分");
						cell1.setCellStyle(cellStyle);
						Cell cell2=row1.createCell(9);
						cell2.setCellValue("供应商预报价部分");
						cell2.setCellStyle(cellStyle);
						 Cell cell3=row1.createCell( 9+supplierCode.size());
						 cell3.setCellValue("客户预报价部分");
						cell3.setCellStyle(cellStyle);
						Cell cell4=row1.createCell(  4+4+9+supplierCode.size());
						cell4.setCellValue("库存订单部分");
						cell4.setCellStyle(cellStyle);
						Cell cell5=row1.createCell( 3+4+4+9+supplierCode.size());
						cell5.setCellValue("供应商预订单部分");
						cell5.setCellStyle(cellStyle);
//						Cell cell6=row1.createCell( 5+3+4+4+9+supplierCode.size());
//						cell6.setCellValue("利润率");
//						Cell cell7=row1.createCell( 3+5+4+4+9+supplierCode.size());
//						cell7.setCellValue("回复客户明细");
						for (int cellnum = 0; cellnum <= lastCellNum+6; cellnum++) {
							Row row2=sheet.getRow(2);
							Cell cell = row2.getCell(cellnum);
						if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							String str = cell.getStringCellValue();
							if (str == null) {
								continue;
							}
							if (str.indexOf("-")>-1) {
								String[] keys=str.split("-");
								str=keys[0];
								this.write(cell, str);
							}
						}
					
					}
			}
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
		number=number.substring(4, number.length());
		return number+"预订单"+format.format(now);
//		return this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "client_order_supplier_quote";
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
		return "ClientOrderSupplierQuoteExcel";
	}

}
