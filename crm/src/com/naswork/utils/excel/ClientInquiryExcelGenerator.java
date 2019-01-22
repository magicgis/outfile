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
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.HistoricalOrderPrice;
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
@Service("clientInquiryExcelGenerator")
public class ClientInquiryExcelGenerator extends ExcelGeneratorBase {

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
	String number = null;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"ClientInquiry.xlsx";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		ListDateVo vo=new ListDateVo();
		//int amount = new Integer(ywId.split("-")[2]);
		String[] c=ywId.split("-")[1].split("//");
		String clientId=c[1];
		String[] d=c[0].split("/");
		String airTypeId =d[0];
		String clientinquiryId=d[1];
		vo.setAirTypeId(airTypeId);
		vo.setClientinquiryId(clientinquiryId);
		vo.setClientId(clientId);
		List<ClientInquiry> listdata=supplierQuoteService.findClientInquiry(vo);
		List<String> supplierCode=new ArrayList<String>();
		Map<String,Double> supplierPrice=new HashMap<String, Double>();
		List<ElementVo> arraylist=new ArrayList<ElementVo>();
		List<ClientQuoteElementVo> hislist = new ArrayList<ClientQuoteElementVo>();
		List<String> historyQuote = new ArrayList<String>();
		PageModel<ClientQuoteElementVo> page = new PageModel<ClientQuoteElementVo>();
		for (ClientInquiry clientInquiry : listdata) {
			List<ElementVo> eleList = clientInquiryElementService.getEle(clientInquiry.getId());
			List<ElementVo> supplierCodes = clientInquiryElementService.getSupplierCode(clientInquiry.getId());
			for (int i = 0; i < eleList.size(); i++) {
				if (eleList.get(i).getBsn() != null && !"".equals(eleList.get(i).getBsn())) {
					HistoricalOrderPrice historicalOrderPrice = historicalOrderPriceService.getPriceByBsn(eleList.get(i).getBsn());
					if (historicalOrderPrice != null) {
						if (historicalOrderPrice.getYear() != null && !"".equals(historicalOrderPrice.getYear())) {
							eleList.get(i).setHistoryYear(historicalOrderPrice.getYear());
						}
						if (historicalOrderPrice.getPrice() != null && !"".equals(historicalOrderPrice.getPrice())) {
							eleList.get(i).setHistoryPrice(historicalOrderPrice.getPrice());
						}
					}
				}
				if (!arraylist.contains(eleList.get(i))) {
					arraylist.add(eleList.get(i));
				}
				//加入历史报价信息
				/*UserVo userVo = ContextHolder.getCurrentUser();
				if (userVo.getUserId().equals("11")) {
					String partCode = clientInquiryService.getCodeFromPartNumber(eleList.get(i).getPartNumber());
					//page.put("partCode", partCode);
					//clientQuoteService.findQuoteDatePage(page, "", null);
					List<ClientQuoteElementVo> pageList = clientQuoteElementService.selectByElementId(partCode);
					for (int p = 0; p < pageList.size(); p++) {
						if (!clientInquiry.getId().equals(pageList.get(p).getClientInquiryId())) {
							if(null!=pageList.get(p).getSupplierCode()
									&&!historyQuote.contains(pageList.get(p).getSupplierCode())
										&&pageList.get(p)!=null){
								historyQuote.add(pageList.get(p).getSupplierCode());
							}
							hislist.add(pageList.get(p));
						}
					}
				}*/
			}
			for (int j = 0; j < supplierCodes.size(); j++) {
				if(null!=supplierCodes.get(j).getSupplierCode()&&!supplierCode.contains(supplierCodes.get(j).getSupplierCode())){
					supplierCode.add(supplierCodes.get(j).getSupplierCode());
				}
			}
				//supplierPrice.put(supplierQuoteVo.getId()+"-"+supplierQuoteVo.getSupplierCode(), supplierQuoteVo.getPrice());
				
			
			Collections.sort(supplierCode);
			Collections.sort(historyQuote);
		}
		int beginColIndex = 13;
//		List<String> quoteNumbers = supplierInquiryService.getQuoteNumbers(new Integer(ywId));
//		ClientInquiry clientInquiry = clientInquiryService.findById(new Integer(ywId));
//		number = clientInquiry.getQuoteNumber();
//		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
//		List<ElementVo> eleList = clientInquiryElementService.getEle(new Integer(ywId));
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);
		int codeCount = (supplierCode.size()+historyQuote.size())*3;
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginColIndex, codeCount);
		int a = 0;
		
		//生成模板
		for(int dynamicIndex=0; dynamicIndex<supplierCode.size()*3;  dynamicIndex++){
			int cellnum = beginColIndex+dynamicIndex;
			Cell cell = row.getCell(cellnum);
			if (dynamicIndex==3*a+2) {
				cell.setCellValue("备注");
				a++;
			}else if (dynamicIndex==3*a+1) {
				cell.setCellValue("供应商询价单号");
				//a++;
			}else if (dynamicIndex==3*a && a<supplierCode.size()){
				cell.setCellValue(supplierCode.get(a));
				//a++;
			}
							
		}
		int r = 0;
		//生成历史模板
		for(int dynamicIndex=supplierCode.size()*3; dynamicIndex<codeCount;  dynamicIndex++){
			int cellnum = beginColIndex+dynamicIndex;
			Cell cell = row.getCell(cellnum);
			if (dynamicIndex==3*(r+a)+2) {
				cell.setCellValue("历史-备注");
				r++;
			}else if (dynamicIndex==3*(r+a)+1) {
				cell.setCellValue("历史-供应商询价单号");
				//a++;
			}else if (dynamicIndex==3*(r+a) && r<historyQuote.size()){
				cell.setCellValue("历史-"+historyQuote.get(r));
				//a++;
			}
							
		}		
				
		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, codeCount);
			int b = 0;
			for(int dynamicIndex=0; dynamicIndex<supplierCode.size()*3;  dynamicIndex++){
				int cellnum = beginColIndex+dynamicIndex;
				Cell cell = row.getCell(cellnum);
				if (dynamicIndex==3*b+2) {
					StringBuffer remark = new StringBuffer();
					remark.append(supplierCode.get(b)).append("_remark");
					cell.setCellValue("$"+remark);
					b++;
				}else if (dynamicIndex==3*b+1) {
					StringBuffer quoteNumber = new StringBuffer();
					quoteNumber.append(supplierCode.get(b)).append("_quoteNumber");
					cell.setCellValue("$"+quoteNumber);
					//b++;
				}else if (dynamicIndex==3*b && b<supplierCode.size()){
					cell.setCellValue("$"+supplierCode.get(b));
					
				}
								
			}
			int rq = 0;
			for(int dynamicIndex=supplierCode.size()*3; dynamicIndex<codeCount;  dynamicIndex++){
				int cellnum = beginColIndex+dynamicIndex;
				Cell cell = row.getCell(cellnum);
				if (dynamicIndex==3*(rq+b)+2) {
					StringBuffer remark = new StringBuffer();
					remark.append(historyQuote.get(rq)).append("_remark_his");
					cell.setCellValue("$"+remark);
					rq++;
				}else if (dynamicIndex==3*(rq+b)+1) {
					StringBuffer quoteNumber = new StringBuffer();
					quoteNumber.append(historyQuote.get(rq)).append("_quoteNumber_his");
					cell.setCellValue("$"+quoteNumber);
					//b++;
				}else if (dynamicIndex==3*(rq+b) && rq<historyQuote.size()){
					cell.setCellValue("$"+historyQuote.get(rq)+"_his");
					
				}
								
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
		//sheet.removeRow(lastRow);
		if (arraylist != null
				&& arraylist.size() > 0) {
			/*if (amount > arraylist.size()) {
				amount = arraylist.size();
			}*/
			for (int i = 0; i < arraylist.size(); i++) {
				Row elementRow = sheet.createRow(lastRowNum + i);
				ElementVo elementVo = arraylist.get(i);
				Integer cieId = elementVo.getClientInquiryId();
				List<ClientDownLoadVo> list = clientInquiryElementService.getPricesByElementVo(elementVo);
				//最高和最低报价
				Double lowestPrice = 0.0;
				Double highestPrice = 0.0;
				String compare = "";
				Double different = 0.0;
				Double sum = 0.0;
				if (list.size()>0 || hislist.size()>0) {
					
					DecimalFormat df = new DecimalFormat("0.00");
					for (int j=0; j<list.size();  j++) {
						List<Integer> supplierCurrencyIds = supplierQuoteService.getCurrencyId(list.get(j).getId());
						for (Integer supplierCurrencyId : supplierCurrencyIds) {
							if (supplierCurrencyId.equals(list.get(j).getCurrencyId())) {
								Integer clientCurrencyId = clientQuoteService.getCurrencyId(list.get(j).getClientInquiryId());
								Double price = null;
								ExchangeRate supplierRate = null;
								ExchangeRate clientRate = null;
								supplierRate = clientInquiryElementService.getValue(supplierCurrencyId);
								clientRate = clientInquiryElementService.getValue(clientCurrencyId);
								Double counterFee=0.0;
								if(null!=list.get(j).getCounterFee()&&0!=list.get(j).getCounterFee()){
									counterFee=list.get(j).getCounterFee();
									if(counterFee<1){
										counterFee=list.get(j).getPrice()*counterFee;
										
									}
									/*if (list.get(j).getCode().equals("0058")) {
										int abc=0;
									}*/
									 BigDecimal bg = new BigDecimal(counterFee);  
									 BigDecimal pg = new BigDecimal(list.get(j).getPrice()); 
							         counterFee = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
							         list.get(j).setPrice(bg.add(pg).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
								}
								if (!supplierCurrencyId.equals(clientCurrencyId)) {
									Double rate = null;
									//汇率变动
									if (supplierRate.getRate()>clientRate.getRate()) {
										rate = supplierRate.getRate()/clientRate.getRate();
										/*BigDecimal b1 = new BigDecimal(rate.toString());  
										BigDecimal b2 = new BigDecimal(supplierRate.getTransferRange().toString());
										rate = new Double((b1.add(b2)).toString());*/
									}else if (supplierRate.getRate()<clientRate.getRate()) {
										rate = supplierRate.getRate()/clientRate.getRate();
										/*BigDecimal b1 = new BigDecimal(rate.toString());  
										BigDecimal b2 = new BigDecimal(clientRate.getTransferRange().toString());
										rate = new Double((b1.subtract(b2)).toString());*/
									}
									Double p = list.get(j).getPrice();
									price = new Double(df.format(p*rate));
									if(lowestPrice.equals(0.0)){
										lowestPrice = new Double(df.format(list.get(0).getPrice()*rate));
									}
								}else {
									price = list.get(j).getPrice();
									if(lowestPrice.equals(0.0)){
										lowestPrice = list.get(0).getPrice();
									}	
								}
								
								if (price>highestPrice) {
									highestPrice = price;
								}
								
								if (price<lowestPrice) {
									lowestPrice = price;
								}
							}
						}
					}
					
					/*for (int j=0; j<hislist.size();  j++) {
						if (hislist.get(j).getInquiryElementId().equals(elementVo.getElementId())) {
							List<Integer> supplierCurrencyIds = supplierQuoteService.getCurrencyId(hislist.get(j).getSupplierInquiryId());
							for (Integer supplierCurrencyId : supplierCurrencyIds) {
								if (supplierCurrencyId.equals(list.get(j).getCurrencyId())) {
									Integer clientCurrencyId = clientQuoteService.getCurrencyId(elementVo.getClientInquiryId());
									Double price = null;
									ExchangeRate supplierRate = null;
									ExchangeRate clientRate = null;
									supplierRate = clientInquiryElementService.getValue(supplierCurrencyId);
									clientRate = clientInquiryElementService.getValue(clientCurrencyId);
									if (!supplierCurrencyId.equals(clientCurrencyId)) {
										Double rate = null;
										//汇率变动
										if (supplierRate.getRate()>clientRate.getRate()) {
											rate = supplierRate.getRate()/clientRate.getRate();
											BigDecimal b1 = new BigDecimal(rate.toString());  
											BigDecimal b2 = new BigDecimal(supplierRate.getTransferRange().toString());
											rate = new Double((b1.add(b2)).toString());
										}else if (supplierRate.getRate()<clientRate.getRate()) {
											rate = supplierRate.getRate()/clientRate.getRate();
											BigDecimal b1 = new BigDecimal(rate.toString());  
											BigDecimal b2 = new BigDecimal(clientRate.getTransferRange().toString());
											rate = new Double((b1.subtract(b2)).toString());
										}
										Double p = hislist.get(j).getPrice();
										price = new Double(df.format(p*rate));
										if(lowestPrice.equals(0.0)){
											lowestPrice = new Double(df.format(hislist.get(0).getPrice()*rate));
										}
									}else {
										price = hislist.get(j).getPrice();
										if(lowestPrice.equals(0.0)){
											lowestPrice = hislist.get(0).getPrice();
										}	
									}
									
									if (price>highestPrice) {
										highestPrice = price;
									}
									
									if (price<lowestPrice) {
										lowestPrice = price;
									}
								}
							}
						}
					}*/
					
					compare = Math.round((lowestPrice)/(highestPrice)*100)+"%";
					different = highestPrice-lowestPrice;
					sum = elementVo.getAmount()*different;
					String result = df.format(different);
					different = new Double(result);
					String result2 = df.format(sum);
					sum = new Double(result2);
				}
				
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					if (cell != null) {
						//elementCell.setCellStyle(cell.getCellStyle());
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
							} else if (key.equals("PART_NUMBER")) {
								Object value = elementVo.getPartNumber();
								this.write(elementCell, value);
							}else if (key.equals("HISTORYPRICE")) {
								Object value = elementVo.getHistoryPrice();
								this.write(elementCell, value);
							}else if (key.equals("HISTORYYEAR")) {
								Object value = elementVo.getHistoryYear();
								this.write(elementCell, value);
							} else if(key.equals("DESCRIPTION")){
								Object value = elementVo.getDescription();
								this.write(elementCell, value);
							}else if (key.equals("UNIT")) {
								Object value = elementVo.getUnit();
								this.write(elementCell, value);
							}else if (key.equals("AMOUNT")) {
								Object value = elementVo.getAmount();
								this.write(elementCell, value);
							}else if (key.equals("LOWESTPRICE")) {
								Object value = lowestPrice;
								this.write(elementCell, value);
							}else if (key.equals("HIGHESTPRICE")) {
								Object value = highestPrice;
								this.write(elementCell, value);
							}else if (key.equals("COMPARE")) {
								Object value = compare;
								this.write(elementCell, value);
							}else if (key.equals("DIFFERENT")) {
								Object value = different;
								this.write(elementCell, value);
							}else if (key.equals("IS_BLACKLIST")) {
								Integer black = elementVo.getIsBlacklist();
								Object value = null;
								if (black.equals(0)) {
									value = "否";
								}else {
									value = "是";
								}
								this.write(elementCell, value);
							}else if (key.equals("SUM")) {
								Object value = sum;
								this.write(elementCell, value);
							}else if (key.equals("IS_MAIN")) {
								Object value = null;
								Integer isMain = elementVo.getIsMain();
								if (isMain.equals(1)) {
									value = "否";
								} else {
									value = "是";
								}
								this.write(elementCell, value);
							}else if (key.equals("FIXED_COST")) {
								Object value = elementVo.getFixedCost();
								this.write(elementCell, value);
							}else if (key.equals("BANK_COST")) {
								Object value = elementVo.getBankCost();
								this.write(elementCell, value);
							}else if (key.equals("REMARK")) {
								Object value = elementVo.getRemark();
								this.write(elementCell, value);
							}else {
								if (list.size()>0 || hislist.size()>0) {
										//int c = 0;
									for (int h = 0; h < supplierCode.size(); h++) {
										if(key.equals(supplierCode.get(h))){
											for (int j=0; j<list.size();  j++) {
												if (list.get(j).getCode().equals(supplierCode.get(h))) {
													List<Integer> supplierCurrencyIds = supplierQuoteService.getCurrencyId(list.get(j).getId());
													for (Integer supplierCurrencyId : supplierCurrencyIds) {
														if (supplierCurrencyId.equals(list.get(j).getCurrencyId())) {
															Integer clientCurrencyId = clientQuoteService.getCurrencyId(list.get(j).getClientInquiryId());
															Double price = null;
															if (!supplierCurrencyId.equals(clientCurrencyId)) {
																ExchangeRate supplierRate = clientInquiryElementService.getValue(supplierCurrencyId);
																ExchangeRate clientRate = clientInquiryElementService.getValue(clientCurrencyId);
																Double p = list.get(j).getPrice();
																DecimalFormat df = new DecimalFormat("0.00");
																Double rate = null;
																//汇率变动
																/*if (list.get(j).getCode().equals("0058")) {
																	int abc=0;
																}*/
																if (supplierRate.getRate()>clientRate.getRate()) {
																	rate = supplierRate.getRate()/clientRate.getRate();
																	/*BigDecimal b1 = new BigDecimal(rate.toString());      
																	BigDecimal b2 = new BigDecimal(supplierRate.getTransferRange().toString());
																	rate = new Double((b1.add(b2)).toString());*/
																}else if (supplierRate.getRate()<clientRate.getRate()) {
																	/*BigDecimal b1 = new BigDecimal(clientRate.toString());  
																	BigDecimal b2 = new BigDecimal(clientRate.getTransferRange().toString());
																	rate = new Double((b1.subtract(b2)).toString());*/
																	rate = supplierRate.getRate()/clientRate.getRate();
																}
																price = new Double(df.format((p*rate)));
															}else {
																price = list.get(j).getPrice();
															}
															
															Object value = price;
															this.write(elementCell, value);
														}
													}
												}
											}
										}else if (key.equals(supplierCode.get(h)+"_remark")) {
											for (int j=0; j<list.size();  j++) {
												if (list.get(j).getCode().equals(supplierCode.get(h))) {
													Object value = list.get(j).getRemark();
													this.write(elementCell, value);
												}
											}
											//c++;
										}else if (key.equals(supplierCode.get(h)+"_quoteNumber")) {
											for (int j=0; j<list.size();  j++) {
												if (list.get(j).getCode().equals(supplierCode.get(h))) {
													Object value = list.get(j).getQuoteNumber();
													this.write(elementCell, value);
												}
											}
										}
									}
									
									for (int h = 0; h < historyQuote.size(); h++) {
										/*page.put("elementid", elementVo.getElementId());
										clientQuoteService.findQuoteDatePage(page, "", null);*/
										
										List<ClientQuoteElementVo> pageList = clientQuoteElementService.selectByElementId(clientInquiryService.getCodeFromPartNumber(elementVo.getPartNumber()));
										if (pageList.size()>0) {
											if(key.equals(historyQuote.get(h)+"_his")){
												for (int j=0; j<hislist.size();  j++) {
													if (hislist.get(j).getSupplierCode().equals(historyQuote.get(h)) && hislist.get(j).getInquiryElementId().equals(elementVo.getElementId())) {
														List<Integer> supplierCurrencyIds = supplierQuoteService.getCurrencyId(hislist.get(j).getSupplierInquiryId());
														for (Integer supplierCurrencyId : supplierCurrencyIds) {
															if (supplierCurrencyId.equals(hislist.get(j).getCurrencyId())) {
																Integer clientCurrencyId = clientQuoteService.getCurrencyId(cieId);
																Double price = null;
																if (!supplierCurrencyId.equals(clientCurrencyId)) {
																	ExchangeRate supplierRate = clientInquiryElementService.getValue(supplierCurrencyId);
																	ExchangeRate clientRate = clientInquiryElementService.getValue(clientCurrencyId);
																	Double p = hislist.get(j).getPrice();
																	DecimalFormat df = new DecimalFormat("0.00");
																	Double rate = null;
																	//汇率变动
																	if (supplierRate.getRate()>clientRate.getRate()) {
																		rate = supplierRate.getRate()/clientRate.getRate();
																		/*BigDecimal b1 = new BigDecimal(rate.toString());      
																		BigDecimal b2 = new BigDecimal(supplierRate.getTransferRange().toString());
																		rate = new Double((b1.add(b2)).toString());*/
																	}else if (supplierRate.getRate()<clientRate.getRate()) {
																		rate = supplierRate.getRate()/clientRate.getRate();
																		/*BigDecimal b1 = new BigDecimal(rate.toString());  
																		BigDecimal b2 = new BigDecimal(clientRate.getTransferRange().toString());
																		rate = new Double((b1.subtract(b2)).toString());*/
																	}
																	price = new Double(df.format((p*rate)));
																}else {
																	price = hislist.get(j).getPrice();
																}
																Object value = price;
																this.write(elementCell, value);
															}
														}
													}
												}
											}else if (key.equals(historyQuote.get(h)+"_remark_his")) {
												for (int j=0; j<hislist.size();  j++) {
													if (hislist.get(j).getSupplierCode().equals(historyQuote.get(h)) && hislist.get(j).getInquiryElementId().equals(elementVo.getElementId())) {
														Object value = hislist.get(j).getQuoteRemark();
														this.write(elementCell, value);
													}
												}
												//c++;
											}else if (key.equals(historyQuote.get(h)+"_quoteNumber_his")) {
												for (int j=0; j<hislist.size();  j++) {
													if (hislist.get(j).getSupplierCode().equals(historyQuote.get(h)) && hislist.get(j).getInquiryElementId().equals(elementVo.getElementId())) {
														Object value = hislist.get(j).getSupplierInquiryQuoteNumber();
														this.write(elementCell, value);
													}
												}
											}
										}
										
									}
								}
							}
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
		
		return number+"_"+"ClientInquiry"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "client_inquiry";
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
		return "ClientInquieyExcel";
	}

}
