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
import com.naswork.module.marketing.controller.partsinformation.PartsInformationVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
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
@Service("partinformationExcelGenerator")
public class PartinformationExcelGenerator extends ExcelGeneratorBase {

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
	private ClientOrderService clientOrderService;
	
	
	String number = null;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"partinformation.xlsx";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		String[] search = ywId.split(",");
		PageModel<PartsInformationVo> page = new PageModel<PartsInformationVo>();
		page.put("download", 1);
		
		if (search.length>1) {
			String where = search[1].replaceAll("/", "'");
			where = where.replaceAll(":", ".");
			where = where.replaceAll("~", "%");
			String[] wheres = where.split(" ");
			StringBuffer whereString = new StringBuffer();
			for (int i = 0; i < wheres.length; i++) {
				if (wheres[i].equals("e.PART_NUMBER_CODE")) {
					wheres[i] = "(e.PART_NUMBER_CODE";
					StringBuffer codeString = new StringBuffer();
					String partNumber = wheres[i+2];
					String code = clientInquiryService.getCodeFromPartNumber(partNumber);
					if (wheres[i+1].equals("=")) {
						codeString.append("'").append(code).append("'").append(" OR ciiee.PART_NUMBER_CODE = '").append(code).append("'").append(")");
					}else if (wheres[i+1].equals("like")) {
						codeString.append("'%").append(code).append("%'").append("OR ciiee.PART_NUMBER_CODE like '%").append(code).append("%'").append(")");
					}
					wheres[i+2] = codeString.toString();
				}
				whereString.append(wheres[i]).append(" ");
			}
			page.put("where", whereString.toString());
		}
		if (!"null".equals(search[0])) {
			page.put("userId", search[0]);
		}
		
		List<PartsInformationVo> listdata = clientInquiryElementService.marketPartInformation(page);
		List<String> supplierCode=new ArrayList<String>();
		List<ClientOrder> orderList = new ArrayList<ClientOrder>();
		Map<String,Double> supplierPrice=new HashMap<String, Double>();
		//List<ElementVo> arraylist=new ArrayList<ElementVo>();
		for (PartsInformationVo partsInformationVo : listdata) {
//			List<ElementVo> eleList = clientInquiryElementService.getEle(clientInquiry.getId());
			List<ElementVo> supplierCodes = clientInquiryElementService.getSupplierCode(partsInformationVo.getClientInquriyId());
			List<ClientOrder> list = clientOrderService.selectByClientInquiryId(partsInformationVo.getClientInquriyId());
			/*for (int i = 0; i < eleList.size(); i++) {
				if (!arraylist.contains(eleList.get(i))) {
					arraylist.add(eleList.get(i));
				}
			}*/
			for (int j = 0; j < supplierCodes.size(); j++) {
				if(null!=supplierCodes.get(j).getSupplierCode()&&!supplierCode.contains(supplierCodes.get(j).getSupplierCode())){
					supplierCode.add(supplierCodes.get(j).getSupplierCode());
				}
			}
/*			for (int i = 0; i < list.size(); i++) {
				if(null!=list.get(i).getId()&&!orderList.contains(list.get(i))){
					orderList.add(list.get(i));
				}
			}*/
				//supplierPrice.put(supplierQuoteVo.getId()+"-"+supplierQuoteVo.getSupplierCode(), supplierQuoteVo.getPrice());
				
			Collections.sort(supplierCode);
			
		}
		//报价记录
		int beginColIndex = 10;
//		List<String> quoteNumbers = supplierInquiryService.getQuoteNumbers(new Integer(ywId));
//		ClientInquiry clientInquiry = clientInquiryService.findById(new Integer(ywId));
//		number = clientInquiry.getQuoteNumber();
//		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
//		List<ElementVo> eleList = clientInquiryElementService.getEle(new Integer(ywId));
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);
		int codeCount = supplierCode.size()*3;
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginColIndex, codeCount);
		int a = 0;
		
		//生成模板
		for(int dynamicIndex=0; dynamicIndex<codeCount;  dynamicIndex++){
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
		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, codeCount);
			int b = 0;
			for(int dynamicIndex=0; dynamicIndex<codeCount;  dynamicIndex++){
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
		}
		
		//订单记录
		/*int beginIndex = 10+codeCount;
//		List<String> quoteNumbers = supplierInquiryService.getQuoteNumbers(new Integer(ywId));
//		ClientInquiry clientInquiry = clientInquiryService.findById(new Integer(ywId));
//		number = clientInquiry.getQuoteNumber();
//		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
//		List<ElementVo> eleList = clientInquiryElementService.getEle(new Integer(ywId));
		int codeCount = supplierCode.size()*3;
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginIndex, codeCount);
		int a = 0;
		
		//生成模板
		for(int dynamicIndex=0; dynamicIndex<codeCount;  dynamicIndex++){
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
		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, codeCount);
			int b = 0;
			for(int dynamicIndex=0; dynamicIndex<codeCount;  dynamicIndex++){
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
		}*/
		
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
		if (listdata != null
				&& listdata.size() > 0) {
			for (int i = 0; i < listdata.size(); i++) {
				Row elementRow = sheet.createRow(lastRowNum + i);
				PartsInformationVo partsInformationVo = listdata.get(i);
				List<ClientDownLoadVo> list = clientInquiryElementService.getPricesByPartVo(partsInformationVo);
				//最高和最低报价
				Double lowestPrice = 0.0;
				Double highestPrice = 0.0;
				String compare = "";
				Double different = 0.0;
				Double sum = 0.0;
				if (list.size()>0) {
					
					DecimalFormat df = new DecimalFormat("0.00");
					for (int j=0; j<list.size();  j++) {
						//Integer supplierCurrencyId = supplierQuoteService.getCurrencyId(list.get(j).getId());
						//Integer clientCurrencyId = clientQuoteService.getCurrencyId(list.get(j).getClientInquiryId());
						Double price = null;
						ExchangeRate supplierRate = null;
						ExchangeRate clientRate = null;
						//supplierRate = clientInquiryElementService.getValue(supplierCurrencyId);
						//clientRate = clientInquiryElementService.getValue(clientCurrencyId);
						/*if (!supplierCurrencyId.equals(clientCurrencyId)) {
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
						}*/
						price = list.get(j).getPrice();
						if(lowestPrice.equals(0.0)){
							lowestPrice = list.get(0).getPrice();
						}
						if (price>highestPrice) {
							highestPrice = price;
						}
						
						if (price<lowestPrice) {
							lowestPrice = price;
						}
					}
					compare = Math.round((lowestPrice)/(highestPrice)*100)+"%";
					different = highestPrice-lowestPrice;
					sum = partsInformationVo.getInquiryAmount()*different;
					String result = df.format(different);
					different = new Double(result);
					String result2 = df.format(sum);
					sum = new Double(result2);
				}
				
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					if (cell != null) {
//						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("QUOTE_NUMBER")){
								Object value = partsInformationVo.getClientInquiryQuoteNumber();
								this.write(elementCell, value);
							}else if (key.equals("ITEM")) {
								Object value = partsInformationVo.getItem();
								this.write(elementCell, value);
							} else if(key.equals("CSN")){
								Object value = partsInformationVo.getCsn();
								this.write(elementCell, value);
							}else if (key.equals("PART_NUMBER")) {
								Object value = partsInformationVo.getInquiryPartNumber();
								this.write(elementCell, value);
							} else if(key.equals("DESCRIPTION")){
								Object value = partsInformationVo.getInquiryDescription();
								this.write(elementCell, value);
							}else if (key.equals("UNIT")) {
								Object value = partsInformationVo.getInquiryUnit();
								this.write(elementCell, value);
							}else if (key.equals("CLIENT_CODE")) {
								Object value = partsInformationVo.getClientCode();
								this.write(elementCell, value);
							}else if (key.equals("INQUIRY_DATE")) {
								Object value = partsInformationVo.getInquiryDate();
								this.write(elementCell, value);
							}else if (key.equals("AMOUNT")) {
								Object value = partsInformationVo.getInquiryAmount();
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
							}else if (key.equals("SUM")) {
								Object value = sum;
								this.write(elementCell, value);
							}else {
								if (list.size()>0) {
										//int c = 0;
									for (int h = 0; h < supplierCode.size(); h++) {
										if(key.equals(supplierCode.get(h))){
											for (int j=0; j<list.size();  j++) {
												if (list.get(j).getCode().equals(supplierCode.get(h))) {
													List<Integer> supplierCurrencyIds = supplierQuoteService.getCurrencyId(list.get(j).getId());
													for (Integer supplierCurrencyId : supplierCurrencyIds) {
														Integer clientCurrencyId = clientQuoteService.getCurrencyId(list.get(j).getClientInquiryId());
														Double price = null;
														/*if (!supplierCurrencyId.equals(clientCurrencyId)) {
															ExchangeRate supplierRate = clientInquiryElementService.getValue(supplierCurrencyId);
															ExchangeRate clientRate = clientInquiryElementService.getValue(clientCurrencyId);
															Double p = list.get(j).getPrice();
															DecimalFormat df = new DecimalFormat("0.00");
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
															//price = new Double(df.format((p*rate)));
														}else {
															price = list.get(j).getPrice();
														}*/
														price = list.get(j).getPrice();
														Object value = price;
														this.write(elementCell, value);
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
		
		return "部件资料";
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "part_information";
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
		return "PartInformationExcel";
	}

}
