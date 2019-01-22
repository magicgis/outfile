package com.naswork.utils.excel;

import java.io.File;
import java.math.BigDecimal;
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
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.SupplierCommissionSaleDao;
import com.naswork.dao.SupplierCommissionSaleElementDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
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
@Service("supplierWeatherQuoteExcelGeneratorForCommission")
public class SupplierWeatherQuoteExcelGeneratorForCommission extends ExcelGeneratorBase {

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
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private ImportPackageElementDao importpackageElementDao;
	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private SupplierCommissionSaleElementDao supplierCommissionSaleElementDao;
	@Resource
	private SupplierCommissionSaleDao supplierCommissionSaleDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private SupplierQuoteDao supplierQuoteDao;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"SupplierWeatherQuoteForCommission.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		ListDateVo vo=new ListDateVo();
		String[] c=ywId.split("-")[1].split("//");
		String clientId=c[1];
		String[] d=c[0].split("/");
		String airTypeId =d[0];
		String clientinquiryIds=d[1];
		String[] clientinquiryId=clientinquiryIds.split(",");
		vo.setAirTypeId(airTypeId);
		vo.setClientId(clientId);
		List<ClientInquiry> listdatas=new ArrayList<ClientInquiry>();
		for (int i = 0; i < clientinquiryId.length; i++) {
			vo.setClientinquiryId(clientinquiryId[i]);
			List<ClientInquiry> listdata=supplierQuoteService.findClientInquiry(vo);
			listdatas.addAll(listdata);
		}
		List<String> supplierCode=new ArrayList<String>();
		Map<String,Double> supplierPrice=new HashMap<String, Double>();
		Map<String,Double>  storageList =new HashMap<String, Double>();
		Map<String,Double>  storageAmoutList =new HashMap<String, Double>();
		List<ClientInquiry> arraylist=new ArrayList<ClientInquiry>();
		
		//供应商寄卖信息
		/*SupplierCommissionSale supplierCommissionSale = supplierCommissionSaleDao.selectByCrawlId(new Integer(clientinquiryId[0]));
		Supplier supplier = supplierDao.selectByPrimaryKey(supplierCommissionSale.getSupplierId());
		supplierCode.add(supplier.getCode());*/
		
		int size=0;
		for (ClientInquiry clientInquiry : listdatas) {
			Double maxPrice = 0.0;
			Double minPrice = 0.0;
			List<ClientInquiry> slist=supplierQuoteDao.findSupplierQuoteForCom(clientInquiry.getId(),clientInquiry.getItem());
			/*if (slist.size() > 0) {
				SupplierCommissionSaleElement supplierCommissionSaleElement = supplierCommissionSaleElementDao.getSourcePrice(supplierCommissionSale.getClientInquiryId().toString(), slist.get(0).getPartNumber());
				if (supplierCommissionSaleElement != null) {
					supplierPrice.put(supplierCommissionSaleElement.getId()+"-"+supplier.getCode(), supplierCommissionSaleElement.getPrice());
					if (supplierCommissionSaleElement.getPrice() > 0) {
						minPrice = supplierCommissionSaleElement.getPrice();
					}else if (slist.size() > 0 && slist.get(0).getPrice() != null) {
						minPrice = slist.get(0).getPrice();
					}
					if (supplierCommissionSaleElement.getPrice() > maxPrice) {
						maxPrice = supplierCommissionSaleElement.getPrice();
					}
				}
			}*/
			if (slist.size() > 0) {
				for (int i = 0; i < slist.size(); i++) {
					if (slist.get(i).getPrice() != null && !"".equals(slist.get(i).getPrice())) {
						minPrice = slist.get(i).getPrice();
					}
				}
			}
			for (ClientInquiry supplierQuoteVo : slist) {
				if (supplierQuoteVo.getPrice() != null) {
					if(null!=supplierQuoteVo.getSupplierCode()&&!supplierCode.contains(supplierQuoteVo.getSupplierCode())){
						supplierCode.add(supplierQuoteVo.getSupplierCode());
					}
					if (supplierQuoteVo.getPrice() > maxPrice) {
						maxPrice = supplierQuoteVo.getPrice();
					}
					if (supplierQuoteVo.getPrice() < minPrice) {
						minPrice = supplierQuoteVo.getPrice();
					}
					supplierPrice.put(supplierQuoteVo.getId()+"-"+supplierQuoteVo.getSupplierCode(), supplierQuoteVo.getPrice());
				}
			}
			Collections.sort(supplierCode);
			if(slist.size()>0){
				slist.get(0).setMinPrice(minPrice);
				slist.get(0).setMaxPrice(maxPrice);
				Integer cieElementId = clientInquiry.getElementId();
				Integer sqeElementId = slist.get(0).getElementId();
				List<ImportPackageElementVo> elementVos=importpackageElementDao.findStorageByElementId(cieElementId, sqeElementId);
				int i=1;
				 for (ImportPackageElementVo importPackageElementVo : elementVos) {
					 List<StorageFlowVo> flowVos=importpackageElementDao.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
					 	if(flowVos.size()>0){
					 		for (StorageFlowVo storageFlowVo : flowVos) {
					 			 Double useamount=orderApprovalDao.useStorageAmout(storageFlowVo.getId(), storageFlowVo.getImportPackageElementId());
					 			 storageFlowVo.setStorageAmount(storageFlowVo.getStorageAmount()-useamount);
								if(storageFlowVo.getStorageAmount()>0){
									if(size<i){
										size=i;
									}
									storageList.put(slist.get(0).getId()+"-"+i, storageFlowVo.getPrice());
									storageAmoutList.put(slist.get(0).getId()+"-"+"amount"+i, storageFlowVo.getStorageAmount());
									i++;
								}
							}
					 	}
				 }
			}
			if (slist.size() > 0) {
				arraylist.add(slist.get(0));
			}
		}
		
		int beginColIndex = 13;
		
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);
		int codeCount = supplierCode.size()*2+size*2;
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginColIndex, codeCount);
		int a = 0;
		
		//生成模板
		for(int dynamicIndex=0; dynamicIndex<codeCount;  dynamicIndex++){
			int cellnum = beginColIndex+dynamicIndex;
			Cell cell = row.getCell(cellnum);
//				a++;
			int supplierCodeSize=supplierCode.size()*2;
			if(dynamicIndex>=supplierCodeSize){
				if (dynamicIndex==2*a+1) {
					cell.setCellValue("数量");
					a++; 
				 }else if (dynamicIndex==2*a && a<supplierCode.size()+size){
						cell.setCellValue("库存价格");
				 }
			}else{
				if (dynamicIndex==2*a+1) {
					cell.setCellValue("备注");
					a++; 
				 }else if (dynamicIndex==2*a && a<supplierCode.size()){
						cell.setCellValue(supplierCode.get(a));
				 }
			}
			
				
		}
		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginColIndex, codeCount);
			int b = 0;
			int in=1;
			for(int dynamicIndex=0; dynamicIndex<codeCount;  dynamicIndex++){
				int cellnum = beginColIndex+dynamicIndex;
				Cell cell = row.getCell(cellnum);
				
				int supplierCodeSize=supplierCode.size()*2;
				if(dynamicIndex>=supplierCodeSize){
					if (dynamicIndex==2*b+1) {
						cell.setCellValue("$"+"amount"+in);
						in++;
						b++;
					}else if (dynamicIndex==2*b && b<supplierCode.size()+size){
						cell.setCellValue("$"+in);
	//					b++;
					}
					
				}else{
					if (dynamicIndex==2*b+1) {
						StringBuffer remark = new StringBuffer();
						remark.append(supplierCode.get(b)).append("_remark");
						cell.setCellValue("$"+remark);
						b++;
					}else if (dynamicIndex==2*b && b<supplierCode.size()){
						cell.setCellValue("$"+supplierCode.get(b));
	//					b++;
					}
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
		sheet.removeRow(lastRow);
		if (arraylist != null
				&& arraylist.size() > 0) {
			for (int i = 0; i < arraylist.size(); i++) {
				Row elementRow = sheet.createRow(lastRowNum + i);
				ClientInquiry elementVo = arraylist.get(i);
				SupplierOrderElement supplierOrderElement=supplierOrderElementDao.findOrderPrice(elementVo.getElementId());
				SupplierCommissionSaleElement count = supplierCommissionSaleElementDao.getCountMessage(elementVo.getPartNumber());
				List<Double> prices = supplierCommissionSaleElementDao.getCountMessageAverage(elementVo.getPartNumber());
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					if (cell != null) {
						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("SOURCE_NUMBER")){
								Object value = elementVo.getQuoteNumber();
								this.write(elementCell, value);
							}else if (key.equals("MIN")) {
								Object value = elementVo.getMinPrice();
								this.write(elementCell, value);
							}else if (key.equals("MAX")) {
								Object value = elementVo.getMaxPrice();
								this.write(elementCell, value);
							}else if (key.equals("INQUIRY_TIME")) {
								Object value = "";
								if (count != null) {
									value = count.getInquiryCount();
								}
								this.write(elementCell, value);
							}else if (key.equals("INQUIRY_AMOUNT")) {
								Object value = "";
								if (count != null) {
									value = count.getInquiryAmount();
								}
								this.write(elementCell, value);
							}else if (key.equals("INQUIRY_CLIENT")) {
								Object value = "";
								if (count != null) {
									value = count.getClientCode();
								}
								this.write(elementCell, value);
							}else if (key.equals("AVERAGE")) {
								Object value = "";
								Double sum = 0.0;
								if (prices.size() > 0) {
									if (prices.size() > 2) {
										for (int j = 0; j < prices.size(); j++) {
											if (j != 0 && j != (prices.size()-1)) {
												sum = sum + prices.get(j);
											}
										}
										if (sum > 0) {
											BigDecimal sumPrice = new BigDecimal(sum);
											BigDecimal amount = new BigDecimal((prices.size()-2));
											value = sumPrice.divide(amount,2,BigDecimal.ROUND_HALF_UP).doubleValue();
										}
									}else {
										for (int j = 0; j < prices.size(); j++) {
											sum = sum + prices.get(j);
										}
										if (sum > 0) {
											BigDecimal sumPrice = new BigDecimal(sum);
											BigDecimal amount = new BigDecimal((prices.size()));
											value = sumPrice.divide(amount);
										}
									}
								}
								this.write(elementCell, value);
							}else if (key.equals("SN")) {
								Object value = i+1;
								this.write(elementCell, value);
							}else if (key.equals("CSN")) {
								Object value =  elementVo.getCsn();
								this.write(elementCell, value);
							}
							else if (key.equals("ITEM")) {
								Object value = elementVo.getItem();
								this.write(elementCell, value);
							}  
							else if (key.equals("PART_NUMBER")) {
								Object value = elementVo.getPartNumber();
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
							}else if (key.equals("AIRTYPE")) {
								Object value = elementVo.getAirType();
								this.write(elementCell, value);
							}else if (key.equals("ORDER_PRICE")) {
								if(null!=supplierOrderElement){
									Object value = supplierOrderElement.getPrice();
									this.write(elementCell, value);
								}
							}else if (key.equals("ORDER_SUPPLIER")) {
								if(null!=supplierOrderElement){
									Object value = supplierOrderElement.getCode();
									this.write(elementCell, value);
								}
							}else if (key.equals("ORDER_AMOUNT")) {
								if(null!=supplierOrderElement){
									Object value = supplierOrderElement.getAmount();
									this.write(elementCell, value);
								}
							}else if (key.equals("ORDER_DATE")) {
								if(null!=supplierOrderElement){
									Object value = supplierOrderElement.getOrderDate();
									this.write(elementCell, value);
								}
							}else if(null!=storageList.get(elementVo.getId()+"-"+key)){
									Object value=storageList.get(elementVo.getId()+"-"+key);
									this.write(elementCell, value);
							}else if(null!=storageAmoutList.get(elementVo.getId()+"-"+key)){
									Object value=storageAmoutList.get(elementVo.getId()+"-"+key);
									this.write(elementCell, value);
							}else if(supplierCode.contains(key)){
									Object value=supplierPrice.get(elementVo.getId()+"-"+key);
									/*if (value == null || "".equals(value)) {
										SupplierCommissionSaleElement supplierCommissionSaleElement = supplierCommissionSaleElementDao.getSourcePrice(supplierCommissionSale.getClientInquiryId().toString(), elementVo.getPartNumber());
										if (supplierCommissionSaleElement != null) {
											value = supplierPrice.get(supplierCommissionSaleElement.getId()+"-"+key);
										}
									}*/
									this.write(elementCell, value);
							}else  if(key.indexOf("_")>-1){
								String[] keys=key.split("_");
								/*if(null!=elementVo.getSupplierCode()&&elementVo.getSupplierCode().equals(keys[0])){
									Object value = elementVo.getRemark();
									this.write(elementCell, value);
								}*/
								//c++;
								//
								List<SupplierQuoteElement> Codes = supplierQuoteElementDao.findByClientInquiryElementId(elementVo.getId());
								for (int j = 0; j < Codes.size(); j++) {
									if(null!=Codes.get(j).getCode()&&Codes.get(j).getCode().equals(keys[0])){
										Object value = Codes.get(j).getRemark();
										this.write(elementCell, value);
										break;
									}
								}
								//
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
		
		return "SupplierWeatherQuoteForCommission"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "supplier_weather_quote_for_commission";
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
		return "SupplierWeatherQuotationFroCommissionExcel";
	}

}
