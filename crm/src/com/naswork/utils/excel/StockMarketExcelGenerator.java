package com.naswork.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;







//import org.apache.catalina.deploy.ContextHandler;
import com.naswork.dao.*;
import com.naswork.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.formula.functions.Subtotal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.filter.ContextHolder;
import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.HistoricalOrderPriceService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

/**
 * 这是例子程序
 * 在写实际的excel处理时，如下几个步骤
 * 1）写一个类集成ExcelGeneratorBase并提供所有abstract函数的实现，具体每个函数的需求请看具体函数的注释
 * 2）把这个类注册到ExcelGeneratorMapConstant类当中
 * 
 * @author eyaomai
 *
 */
@Service("stockMarketExcelGenerator")
public class StockMarketExcelGenerator extends ExcelGeneratorBase {

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
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private SupplierCommissionSaleElementDao supplierCommissionSaleElementDao;
	@Resource
	private StockMarketCrawlDao stockMarketCrawlDao;
	@Resource
	private SupplierCommissionForStockmarketDao supplierCommissionForStockmarketDao;
	@Resource
	private SupplierCommissionForStockmarketElementDao supplierCommissionForStockmarketElementDao;

	@Resource
	private ArPricePartMappingDao arPricePartMappingDao;

	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"StockMarket.xlsx";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		ListDateVo vo=new ListDateVo();
		//int amount = new Integer(ywId.split("-")[2]);
		List<String> supplierCodes = supplierCommissionSaleElementDao.getSupplierCodeByCrawlMessageId(new Integer(ywId));
		
		StringBuffer string = new StringBuffer();
		for (int i = 0; i < supplierCodes.size(); i++) {
			//SV
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(supplierCodes.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'SV',smcm.AMOUNT,0)) AS '");
			string.append(getCodeFromPartNumber(supplierCodes.get(i))).append("_SV',");
			//AR
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(supplierCodes.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'AR',smcm.AMOUNT,0)) AS '");
			string.append(getCodeFromPartNumber(supplierCodes.get(i))).append("_AR',");
			//OH
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(supplierCodes.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'OH',smcm.AMOUNT,0)) AS '");
			string.append(getCodeFromPartNumber(supplierCodes.get(i))).append("_OH',");
			//NE
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(supplierCodes.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'NE',smcm.AMOUNT,0)) AS '");
			string.append(getCodeFromPartNumber(supplierCodes.get(i))).append("_NE',");
		}
		
		PageModel<String> sqlPage = new PageModel<String>();
		sqlPage.put("append", string);
		sqlPage.put("id", ywId);
		StockMarketCrawl stockMarketCrawl = stockMarketCrawlDao.selectByPrimaryKey(new Integer(ywId));
		SupplierCommissionForStockmarket supplierCommissionForStockmarket = supplierCommissionForStockmarketDao.selectByPrimaryKey(stockMarketCrawl.getSupplierCommissionSaleId());
		sqlPage.put("clientInquiryId", supplierCommissionForStockmarket.getClientInquiryId());
		sqlPage.put("assetId", stockMarketCrawl.getSupplierCommissionSaleId());
		//导出信息
		List<Map<String, String>> listMap = supplierCommissionSaleElementDao.getCountMessageForExcel(sqlPage);
		//非替换件的记录
		List<SupplierCommissionForStockmarketElement> notReplaceList = supplierCommissionForStockmarketElementDao.getNotReplaceRecord(new Integer(ywId));
		
		int beginColIndex = 27;
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);
		int codeCount = supplierCodes.size()*4;
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginColIndex, codeCount,wb);
		int a = 0;
		
		//生成模板
		for(int dynamicIndex=0; dynamicIndex<supplierCodes.size()*4;  dynamicIndex++){
			int cellnum = beginColIndex+dynamicIndex;
			Cell cell = row.getCell(cellnum);
			CellStyle cellStyle = wb.createCellStyle();
			/*Font font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);*/
			cell.setCellStyle(cellStyle);
			if (dynamicIndex==4*a+3) {
				cell.setCellValue("NE");
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				a++;
			}else if (dynamicIndex==4*a+2) {
				cell.setCellValue("OH");
			}else if (dynamicIndex==4*a+1) {
				cell.setCellValue("SV");
			}else if (dynamicIndex==4*a && a<supplierCodes.size()){
				cell.setCellValue("AR");
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			}
							
		}
		
		CellStyle leftCellStyle = wb.createCellStyle();
		leftCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		
		CellStyle leftTopStyle = wb.createCellStyle();
		leftTopStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		leftTopStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
		
		CellStyle rightCellStyle = wb.createCellStyle();
		rightCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		CellStyle rightTopStyle = wb.createCellStyle();
		rightTopStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		rightTopStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
		
		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, codeCount,wb);
			int b = 0;
			for(int dynamicIndex=0; dynamicIndex<supplierCodes.size()*4;  dynamicIndex++){
				int cellnum = beginColIndex+dynamicIndex;
				Cell cell = row.getCell(cellnum);
				Integer index = dynamicIndex/4;
				//for (int i = 0; i < supplierCodes.size(); i++) {
					if (dynamicIndex==4*b+3) {
						StringBuffer message = new StringBuffer();
						message.append(getCodeFromPartNumber(supplierCodes.get(index))).append("_NE");
						cell.setCellValue("$"+message);
						b++;
					}else if (dynamicIndex==4*b+2) {
						StringBuffer message = new StringBuffer();
						message.append(getCodeFromPartNumber(supplierCodes.get(index))).append("_OH");
						cell.setCellValue("$"+message);
					}else if (dynamicIndex==4*b+1) {
						StringBuffer message = new StringBuffer();
						message.append(getCodeFromPartNumber(supplierCodes.get(index))).append("_SV");
						cell.setCellValue("$"+message);
						//b++;
					}else if (dynamicIndex==4*b && b<supplierCodes.size()){
						StringBuffer message = new StringBuffer();
						message.append(getCodeFromPartNumber(supplierCodes.get(index))).append("_AR");
						cell.setCellValue("$"+message);
					}
				//}
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
		CellStyle repalceStyle = wb.createCellStyle();
		repalceStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
		//sheet.removeRow(lastRow);
		if (listMap != null
				&& listMap.size() > 0) {
			int index = 1;
			for (int i = 0; i < listMap.size(); i++) {
				Row elementRow = sheet.createRow(lastRowNum + i);
				Map<String, String> map = listMap.get(i);
				String avgNew = "0";
				String min = "";
				String max = "0";
				if (map.get("MAX_AND_MIN") != null) {
					String maxAndMin = String.valueOf(map.get("MAX_AND_MIN"));
					String[] maxAndMinStrings = maxAndMin.split(",");
					max = maxAndMinStrings[0];
					min = maxAndMinStrings[1];
				}
				List<Double> priceNewList = new ArrayList<Double>();
				if (map.get("prices_new_main") != null) {
					String[] prices = String.valueOf(map.get("prices_new_main")).split(",");
					for (int j = 0; j < prices.length; j++) {
						if (!"".equals(prices[j]) && prices[j] != null) {
							priceNewList.add(new Double(prices[j]));
						}
					}
				}
				if (map.get("prices_new_alter") != null) {
					String[] prices = String.valueOf(map.get("prices_new_alter")).split(",");
					for (int j = 0; j < prices.length; j++) {
						if (!"".equals(prices[j]) && prices[j] != null) {
							priceNewList.add(new Double(prices[j]));
						}
					}
				}
				if (map.get("prices_new") != null) {
					String[] prices = String.valueOf(map.get("prices_new")).split(",");
					for (int j = 0; j < prices.length; j++) {
						if (!"".equals(prices[j]) && prices[j] != null) {
							priceNewList.add(new Double(prices[j]));
						}
					}
				}
				Collections.sort(priceNewList);
				if (priceNewList.size() > 5) {
					//已排序，去掉最高最低
					Double total = 0.0;
					for (int j = 1; j < priceNewList.size()-1; j++) {
						if (priceNewList.get(j) != null && !"".equals(priceNewList.get(j))) {
							total = total + new Double(priceNewList.get(j));
						}
					}
					BigDecimal totalBig = new BigDecimal(total);
					BigDecimal amountBig = new BigDecimal(priceNewList.size()-2);
					avgNew = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
				}else if (priceNewList.size() > 0) {
					Double total = 0.0;
					for (int j = 0; j < priceNewList.size(); j++) {
						if (priceNewList.get(j) != null && !"".equals(priceNewList.get(j))) {
								total = total + new Double(priceNewList.get(j));
						}
					}
					BigDecimal totalBig = new BigDecimal(total);
					BigDecimal amountBig = new BigDecimal(priceNewList.size());
					avgNew = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
				}
				/*if (map.get("prices_new") != null) {
					String[] prices = String.valueOf(map.get("prices_new")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgNew = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgNew = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
						
						
					}
				}*/
				String avgOverHaul = "0";
				if (map.get("mro_overhaul") != null) {
					String[] prices = String.valueOf(map.get("mro_overhaul")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgOverHaul = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgOverHaul = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
						
						
					}
				}
				String avgRepair = "0";
				if (map.get("mro_repair") != null) {
					String[] prices = String.valueOf(map.get("mro_repair")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgRepair = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgRepair = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
					}
				}
				//TEST_INSPECTED
				String avgTestInspected = "0";
				if (map.get("test_inspected") != null) {
					String[] prices = String.valueOf(map.get("test_inspected")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgTestInspected = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgTestInspected = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
					}
				}
				//SV
				String avgSV = "0";
				if (map.get("price_sv") != null) {
					String[] prices = String.valueOf(map.get("price_sv")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgSV = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgSV = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
					}
				}
				//oh
				String avgOH = "0";
				if (map.get("price_oh") != null) {
					String[] prices = String.valueOf(map.get("price_oh")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgOH = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgOH = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
					}
				}
				//mro_test
				String avgMRO_TEST = "0";
				if (map.get("mro_test") != null) {
					String[] prices = String.valueOf(map.get("mro_test")).split(",");
					if (prices.length > 0) {
						Double total = 0.0;
						if (prices.length > 5) {
							//已排序，去掉最高最低
							for (int j = 1; j < prices.length-1; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									total = total + new Double(prices[j]);
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length-2);
							avgMRO_TEST = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}else {
							for (int j = 0; j < prices.length; j++) {
								if (prices[j] != null && !"".equals(prices[j])) {
									if (!prices[j].equals(max) && !prices[j].equals(min)) {
										total = total + new Double(prices[j]);
									}
								}
							}
							BigDecimal totalBig = new BigDecimal(total);
							BigDecimal amountBig = new BigDecimal(prices.length);
							avgMRO_TEST = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
						}
					}
				}

				//old
				String avgOld = "0";
				List<Double> priceOldList = new ArrayList<Double>();
				if (map.get("prices_old_main") != null) {
					String[] prices = String.valueOf(map.get("prices_old_main")).split(",");
					for (int j = 0; j < prices.length; j++) {
						if (!"".equals(prices[j]) && prices[j] != null) {
							priceOldList.add(new Double(prices[j]));
						}
					}
				}
				if (map.get("prices_old_alter") != null) {
					String[] prices = String.valueOf(map.get("prices_old_alter")).split(",");
					for (int j = 0; j < prices.length; j++) {
						if (!"".equals(prices[j]) && prices[j] != null) {
							priceOldList.add(new Double(prices[j]));
						}
					}
				}
				if (map.get("prices_old") != null) {
					String[] prices = String.valueOf(map.get("prices_old")).split(",");
					for (int j = 0; j < prices.length; j++) {
						if (!"".equals(prices[j]) && prices[j] != null) {
							priceOldList.add(new Double(prices[j]));
						}
					}
				}
				Collections.sort(priceOldList);
				if (priceOldList.size() > 5) {
					Double total = 0.0;
					//已排序，去掉最高最低
					for (int j = 1; j < priceOldList.size()-1; j++) {
						if (priceOldList.get(j) != null && !"".equals(priceOldList.get(j))) {
							total = total + new Double(priceOldList.get(j));
						}
					}
					BigDecimal totalBig = new BigDecimal(total);
					BigDecimal amountBig = new BigDecimal(priceOldList.size() - 2);
					avgOld = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
				}else if (priceOldList.size() > 0) {
					Double total = 0.0;
					for (int j = 0; j < priceOldList.size(); j++) {
						if (priceOldList.get(j) != null && !"".equals(priceOldList.get(j))) {
							total = total + new Double(priceOldList.get(j));
						}
					}
					BigDecimal totalBig = new BigDecimal(total);
					BigDecimal amountBig = new BigDecimal(priceOldList.size());
					avgOld = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
				}
				/*if (map.get("prices_old") != null) {
					String[] prices = String.valueOf(map.get("prices_old")).split(",");
					if (prices.length > 5) {
						Double total = 0.0;
						//已排序，去掉最高最低
						for (int j = 1; j < prices.length-1; j++) {
							if (prices[j] != null && !"".equals(prices[j])) {
								total = total + new Double(prices[j]);
							}
						}
						BigDecimal totalBig = new BigDecimal(total);
						BigDecimal amountBig = new BigDecimal(prices.length - 2);
						avgOld = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
					}else {
						Double total = 0.0;
						for (int j = 0; j < prices.length; j++) {
							if (prices[j] != null && !"".equals(prices[j])) {
								total = total + new Double(prices[j]);
							}
						}
						BigDecimal totalBig = new BigDecimal(total);
						BigDecimal amountBig = new BigDecimal(prices.length);
						avgOld = totalBig.divide(amountBig, 2, BigDecimal.ROUND_HALF_UP).toString();
					}
				}*/
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					if (cell != null) {
						//elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							if (notReplaceList.size()+1 == index) {
								elementCell.setCellStyle(repalceStyle);
							}
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("PART_NUMBER")){
								Object value = "";
								if (map.get("PART_NUMBER") != null) {
									value = map.get("PART_NUMBER");
								}
								this.write(elementCell, value);
							} else if (key.equals("ITEM")) {
								Object value = "";
								if (map.get("ITEM") != null) { 
									value = map.get("ITEM");
								}
								this.write(elementCell, value);
							} else if (key.equals("NUMBER")) {
								Object value = index;
								this.write(elementCell, value);
							} else if (key.equals("DESC")) {
								Object value = "";
								if (map.get("DESCRIPTION") != null) {
									value = map.get("DESCRIPTION");
								}
								this.write(elementCell, value);
							} else if(key.equals("AMOUNT")){
								Object value = "";
								if (map.get("AMOUNT") != null) {
									value = map.get("AMOUNT");
								}
								this.write(elementCell, value);
							} else if(key.equals("TOTAL")){
								Object value = "0";
								if (map.get("total") != null) {
									value = map.get("total");
								}
								if (notReplaceList.size()+1 == index) {
									elementCell.setCellStyle(leftTopStyle);
								}else {
									elementCell.setCellStyle(leftCellStyle);
								}
								this.write(elementCell, value);
							} else if(key.equals("AR")){
								Object value = "0";
								if (map.get("ar") != null) {
									value = map.get("ar");
								}
								this.write(elementCell, value);
							} else if(key.equals("AR_PER")){
								Object value = "0";
								if (map.get("AR_PER") != null) {
									value = map.get("AR_PER")+"%";
								}
								this.write(elementCell, value);
							} else if(key.equals("SV")){
								Object value = "0";
								if (map.get("sv") != null) {
									value = map.get("sv");
								}
								this.write(elementCell, value);
							} else if(key.equals("SV_PER")){
								Object value = "0";
								if (map.get("SV_PER") != null) {
									value = map.get("SV_PER")+"%";
								}
								this.write(elementCell, value);
							} else if(key.equals("OH")){
								Object value = "0";
								if (map.get("oh") != null) {
									value = map.get("oh");
								}
								this.write(elementCell, value);
							} else if(key.equals("OH_PER")){
								Object value = "0";
								if (map.get("OH_PER") != null) {
									value = map.get("OH_PER")+"%";
								}
								this.write(elementCell, value);
							} else if(key.equals("NE")){
								Object value = "0";
								if (map.get("ne") != null) {
									value = map.get("ne");
								}
								this.write(elementCell, value);
							} else if(key.equals("NE_PER")){
								Object value = "0";
								if (map.get("NE_PER") != null) {
									value = map.get("NE_PER")+"%";
								}
								this.write(elementCell, value);
							} else if(key.equals("OH_SV_PER")){
								Object value = "0";
								if (map.get("OH_SV_PER") != null) {
									value = map.get("OH_SV_PER")+"%";
								}
								if (notReplaceList.size()+1 == index) {
									elementCell.setCellStyle(rightTopStyle);
								}else {
									elementCell.setCellStyle(rightCellStyle);
								}
								this.write(elementCell, value);
							}else if(key.equals("MIN") && !"".equals(map.get("PART_NUMBER"))){
								Object value = min;
								this.write(elementCell, value);
							}else if(key.equals("MAX") && !"".equals(map.get("PART_NUMBER"))){
								Object value = max;
								if (max.equals("0")) {
									value = "";
								}
								this.write(elementCell, value);
							}else if(key.equals("INQUIRY_AMOUNT")){
								Object value = "";
								if (map.get("inquiry_amount") != null && !"".equals(map.get("PART_NUMBER"))) {
									value = map.get("inquiry_amount");
								}
								this.write(elementCell, value);
							}else if(key.equals("INQUIRY_COUNT") && !"".equals(map.get("PART_NUMBER"))){
								Object value = "";
								if (map.get("inquiry_count") != null) {
									value = map.get("inquiry_count");
								}
								this.write(elementCell, value);
							}else if(key.equals("CLIENT") && !"".equals(map.get("PART_NUMBER"))){
								Object value = "";
								if (map.get("client_code") != null) {
									value = map.get("client_code");
								}
								this.write(elementCell, value);
							}else if(key.equals("AVG_OLD") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgOld;
								this.write(elementCell, value);
							}else if(key.equals("AVG_NEW") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgNew;
								this.write(elementCell, value);
							}else if(key.equals("MRO_REAPIR") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgRepair;
								this.write(elementCell, value);
							}else if(key.equals("MRO_OVERHAUL") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgOverHaul;
								this.write(elementCell, value);
							}else if(key.equals("TEST_INSPECTED") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgTestInspected;
								this.write(elementCell,value);
							}else if(key.equals("PRICE_SV") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgSV;
								this.write(elementCell,value);
							}else if(key.equals("PRICE_OH") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgOH;
								this.write(elementCell,value);
							}else if(key.equals("MRO_TEST") && !"".equals(map.get("PART_NUMBER"))){
								Object value = avgMRO_TEST;
								this.write(elementCell,value);
							}
							else if(key.equals("SN")){
								Object value = "";
								if (map.get("SERIAL_NUMBER") != null) {
									value = map.get("SERIAL_NUMBER");
								}
								this.write(elementCell, value);
							}else if(key.equals("DOM")){
								Object value = "";
								if (map.get("DOM") != null) {
									value = map.get("DOM");
								}
								this.write(elementCell, value);
							}else if(key.equals("CONDITION")){
								Object value = "";
								if (map.get("CONDITION_VALUE") != null) {
									value = map.get("CONDITION_VALUE");
								}
								this.write(elementCell, value);
							}else if(key.equals("TSN")){
								Object value = "";
								if(map.get("TSN") != null){
									value = map.get("TSN");
								}
								this.write(elementCell,value);
							}else if(key.equals("CSN")){
								Object value = "";
								if(map.get("CSN") != null){
									value = map.get("CSN");
								}
								this.write(elementCell,value);
							}else if(key.equals("ATA")){
								Object value = "";
								if(map.get("ATA") != null){
									value = map.get("ATA");
								}
								this.write(elementCell,value);
							}
							else if(key.equals("MANUFACTURER")){
								Object value = "";
								if (map.get("MANUFACTURER") != null) {
									value = map.get("MANUFACTURER");
								}
								this.write(elementCell, value);
							}else if(key.equals("AR_PRICE")){
								//从预测价表中获取 判断是否有该件号的预测评估价
								ArPricePartMapping value = arPricePartMappingDao.getNewArPriceByPartNumber(map.get("PART_NUMBER").toString());
								if(value != null){
									this.write(elementCell, value.getArPrice());
								}
							}else if(key.equals("AR_SALE_PRICE")){
								//从预测价表中获取 判断是否有该件号的预测销售价
								ArPricePartMapping value = arPricePartMappingDao.getNewArPriceByPartNumber(map.get("PART_NUMBER").toString());
								if(value != null){
									this.write(elementCell, value.getArSalePrice());
								}
							}
							else {
								Object amount = "";
								if (key.indexOf("_AR") >= 0) {
									if (notReplaceList.size()+1 == index) {
										elementCell.setCellStyle(leftTopStyle);
									}else {
										elementCell.setCellStyle(leftCellStyle);
									}
								}
								if (map.get(key) != null) {
									amount = map.get(key.toString());
								}/*else {
									amount = "0";
								}*/
								this.write(elementCell, amount);
							}
						}
					}
				}
				index++;
			}
		}
		Row totleRow = sheet.createRow(0);
		Font font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		for (int i = 0; i < supplierCodes.size(); i++) {
			sheet.addMergedRegion(new CellRangeAddress(0,0,27+i*4,27+i*4+3));
		}
		sheet.addMergedRegion(new CellRangeAddress(0,0,27+supplierCodes.size()*4,27+supplierCodes.size()*4+9));
		Cell totalFirstCell = totleRow.createCell(27+supplierCodes.size()*4);
		CellStyle totalFirstStyle = wb.createCellStyle();
		totalFirstStyle.setFont(font);
		totalFirstStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		totalFirstStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		totalFirstCell.setCellStyle(totalFirstStyle);
		this.write(totalFirstCell, "TOTAL");
		
		Cell totalLastCell = totleRow.createCell(27+supplierCodes.size()*4+9);
		CellStyle totalLastStyle = wb.createCellStyle();
		totalLastStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		totalLastCell.setCellStyle(totalLastStyle);
		
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中  
		cellStyle.setFont(font);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		CellStyle lastCellStyle = wb.createCellStyle();
		lastCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		for (int i = 0; i < supplierCodes.size(); i++) {
			Cell cell = totleRow.createCell(27+i*4);
			cell.setCellStyle(cellStyle);
			Cell lastCell = totleRow.createCell(27+i*4+3);
			lastCell.setCellStyle(lastCellStyle);
			this.write(cell, supplierCodes.get(i));
		}
		String sql = sqlPage.get("append").toString();
//		System.out.println("打印分割前的sql"+sql);
		if(sql.length()>0){
			sql = sql.substring(0, sql.length()-1);
			sqlPage.put("append", sql);
		}else{
			sqlPage.put("append",sql);
		}

		CellStyle footLeftBorder = wb.createCellStyle();
		footLeftBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		footLeftBorder.setFont(font);
		CellStyle footRightBorder = wb.createCellStyle();
		footRightBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);
		footRightBorder.setFont(font);
		CellStyle footSimpleBorder = wb.createCellStyle();
		footSimpleBorder.setFont(font);
		//subtotal
		List<Map<String, String>> subTotalAmountMaps = supplierCommissionSaleElementDao.getSubTotalBySupplier(sqlPage);
		int lastRowNumber = sheet.getLastRowNum();
		Row subTotalRow = sheet.createRow(lastRowNumber+1);
		Cell cell0 = subTotalRow.createCell(0);
		CellStyle subTotalStyle = wb.createCellStyle();
		subTotalStyle.setFont(font);
		CellStyle forSubTotalStyle = wb.createCellStyle();
		forSubTotalStyle.setFont(font);
		forSubTotalStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
		cell0.setCellStyle(forSubTotalStyle);
		this.write(cell0, "SUBTOTAL");
		CellStyle lineCellStyle = wb.createCellStyle();
		lineCellStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
		for (int i = 0; i < 18; i++) {
			Cell lineCell = subTotalRow.createCell(i+1);
			lineCell.setCellStyle(lineCellStyle);
			this.write(lineCell, "");
		}
		CellStyle cellForSubLeft = wb.createCellStyle();
		cellForSubLeft.setBorderTop(HSSFCellStyle.BORDER_THICK);
		cellForSubLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellForSubLeft.setFont(font);
		
		CellStyle cellForSubSimple = wb.createCellStyle();
		cellForSubSimple.setBorderTop(HSSFCellStyle.BORDER_THICK);
		cellForSubSimple.setFont(font);
		
		CellStyle cellForSubRight = wb.createCellStyle();
		cellForSubRight.setBorderTop(HSSFCellStyle.BORDER_THICK);
		cellForSubRight.setFont(font);
		cellForSubRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		for (int i = 0; i < supplierCodes.size(); i++) {
			String supplierCode = getCodeFromPartNumber(supplierCodes.get(i));
			if (subTotalAmountMaps.get(i).get(supplierCode+"_AR") != null) {
				Cell cell = subTotalRow.createCell(27+i*4);
				cell.setCellStyle(cellForSubLeft);
				this.write(cell, subTotalAmountMaps.get(i).get(supplierCode+"_AR"));
			}
			if (subTotalAmountMaps.get(i).get(supplierCode+"_SV") != null) {
				Cell cell = subTotalRow.createCell(27+i*4+1);
				cell.setCellStyle(cellForSubSimple);
				this.write(cell, subTotalAmountMaps.get(i).get(supplierCode+"_SV"));
			}
			if (subTotalAmountMaps.get(i).get(supplierCode+"_OH") != null) {
				Cell cell = subTotalRow.createCell(27+i*4+2);
				cell.setCellStyle(cellForSubSimple);
				this.write(cell, subTotalAmountMaps.get(i).get(supplierCode+"_OH"));
			}
			if (subTotalAmountMaps.get(i).get(supplierCode+"_NE") != null) {
				Cell cell = subTotalRow.createCell(27+i*4+3);
				cell.setCellStyle(cellForSubRight);
				this.write(cell, subTotalAmountMaps.get(i).get(supplierCode+"_NE"));
			}
		}
		
		//total
		lastRowNumber = sheet.getLastRowNum();
		Row TotalRow = sheet.createRow(lastRowNumber+1);
		Cell cell1 = TotalRow.createCell(0);
		cell1.setCellStyle(subTotalStyle);
		this.write(cell1, "TOTAL");
		List<Map<String, String>> totalAmountMaps = supplierCommissionSaleElementDao.getTotalBySupplier(new Integer(ywId));
		for (int i = 0; i < supplierCodes.size(); i++) {
			for (int j = 0; j < totalAmountMaps.size(); j++) {
				if (totalAmountMaps.get(j).get("SUPPLIER_CODE").equals(supplierCodes.get(i))) {
					for (int j2 = 0; j2 < 4; j2++) {
						Cell cell = TotalRow.createCell(27+i*4+j2);
						if (j2 == 0) {
							cell.setCellStyle(footLeftBorder);
						}else if (j2 == 3) {
							cell.setCellStyle(footRightBorder);
						}else {
							cell.setCellStyle(footSimpleBorder);
						}
						this.write(cell, totalAmountMaps.get(j).get("AMOUNT"));
					}
				}
			}
		}
		
		//totalpercent
		lastRowNumber = sheet.getLastRowNum();
		Row PercentageRow = sheet.createRow(lastRowNumber+1);
		Cell cell2 = PercentageRow.createCell(0);
		cell2.setCellStyle(subTotalStyle);
		this.write(cell2, "Percentage");
		for (int i = 0; i < supplierCodes.size(); i++) {
			for (int j = 0; j < 4; j++) {
				Cell cell = PercentageRow.createCell(27+i*4+j);
				String total = TotalRow.getCell(27+i*4+j).toString();
				String subTotal = subTotalRow.getCell(27+i*4+j).toString();
				BigDecimal totalBig = new BigDecimal(total);
				BigDecimal subTotalBig = new BigDecimal(subTotal);
				Double per = subTotalBig.multiply(new BigDecimal(100)).divide(totalBig, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				if (j == 0) {
					cell.setCellStyle(footLeftBorder);
				}else if (j == 3) {
					cell.setCellStyle(footRightBorder);
				}else {
					cell.setCellStyle(footSimpleBorder);
				}
				this.write(cell, per+"%");
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
	
	private static void createCell(Row row, int index, int numOfCell,POIExcelWorkBook wb){
		int lastCellNum = row.getLastCellNum();
		for(int dynamicIndex=0; dynamicIndex<numOfCell; dynamicIndex++){
			Cell oriCell = row.getCell(index + dynamicIndex);
			if (dynamicIndex == 0) {
				CellStyle cellStyle = oriCell.getCellStyle();    
				oriCell.setCellStyle(cellStyle);
			}
			if (dynamicIndex == (numOfCell-1)) {
				CellStyle cellStyle = oriCell.getCellStyle();    
				oriCell.setCellStyle(cellStyle);
			}
			row.createCell(dynamicIndex+lastCellNum, oriCell.getCellType());
		}
		//int newLastCellNum = row.getLastCellNum();
		
		for(int dynamicIndex=lastCellNum-1; dynamicIndex>=index; dynamicIndex--){
			Cell oriCell = row.getCell(dynamicIndex);
			Cell newCell = row.getCell(dynamicIndex + numOfCell);
			copyCell(oriCell, newCell);
		}
	}
	
	public String getCodeFromPartNumber(String partNumber) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < partNumber.length(); i++) {
			char ch = partNumber.charAt(i);
			/*if (isValidCharacter(ch)) {
				buffer.append(Character.toUpperCase(ch));
			}*/
			String regex = "[a-zA-Z]";//其他需要，直接修改正则表达式就好
			if (String.valueOf(ch).matches(regex)) {
				buffer.append(String.valueOf(ch));
			}
			//return str.matches(regex);
		}
		return buffer.toString();
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
		
		return "StockMarketCrawl"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "stock_market_crawl";
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
		return "StockMarketCrawlExcel";
	}

}
