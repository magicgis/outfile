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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientQuote;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OrderApproval;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.FlowService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.OrderApprovalService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
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
@Service("supplierWeatherOrderExcelGenerator")
public class SupplierWeatherOrderExcelGenerator extends ExcelGeneratorBase {

	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private SupplierOrderService supplierOrderService;
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
	private ImportpackageElementService importpackageElementService;
	@Resource
	private OrderApprovalService orderApprovalService;
	@Resource
	private FlowService flowService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"SupplierWeatherOrder1.xlsx";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		String[] clientOrderElementIds=ywId.split("-");
		ClientQuote clientQuote=new ClientQuote();
		
		String elementId =clientOrderElementIds[1];
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		
		//clientQuote.setIds(clientOrderElementIds[1]);
		clientQuote.setIds(idsForTask);
		List<ClientOrderElementVo>  list=supplierOrderService.ClientOrderData(clientQuote);
		List<String> supplierCode=new ArrayList<String>();
		List<String> storageSupplierCode=new ArrayList<String>();
		List<String> quotenumber=new ArrayList<String>();
		List<String> quoteamount=new ArrayList<String>();
		List<String> sqeremark=new ArrayList<String>();
		Map<String,Double> supplierPrice=new HashMap<String, Double>();
		Map<String,Double> amount=new HashMap<String, Double>();
		Map<String,String> supplierRemark=new HashMap<String, String>();
		Map<String,String> supplierNumber=new HashMap<String, String>();
		Map<String,Object> lowestPriceMap=new HashMap<String, Object>();
		
		List<SupplierListVo> supplierListVo=new ArrayList<SupplierListVo>();
		List<SupplierListVo> arrayList=new ArrayList<SupplierListVo>();
		for (ClientOrderElementVo clientOrderElementVo : list) {
			String lowestCode="";
			Double lowestPrice=0.0;
//			Integer cieElementId = supplierOrderService.getElementId(clientOrderElementVo.getClientQuoteElementId());
//			Integer sqeElementId = supplierOrderService.getSqeElementId(clientOrderElementVo.getClientQuoteElementId());
//			supplierListVo=supplierOrderService.SupplierListData(sqeElementId,cieElementId,cieElementId);
			List<ClientInquiry> slist=supplierQuoteService.findSupplierQuote(clientOrderElementVo.getClientInquiryId(),clientOrderElementVo.getItem());
			for (ClientInquiry clientInquiry : slist) {
				if(!supplierCode.contains(clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId())){
					supplierCode.add(clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId());
				}
				sqeremark.add(clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId()+"_remark");
				quotenumber.add(clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId()+"_quoteNumber");
				supplierPrice.put(clientOrderElementVo.getId()+"-"+clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId(), clientInquiry.getQuotePrice());
				supplierRemark.put(clientOrderElementVo.getId()+"-"+clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId()+"_remark", clientInquiry.getQuoteRemark());
				supplierNumber.put(clientOrderElementVo.getId()+"-"+clientInquiry.getSupplierCode()+"-"+clientInquiry.getSupplierQuoteId()+"_quoteNumber", clientInquiry.getSupplierInquiryQuoteNumber()+"/"+clientInquiry.getSupplierQuoteElementId());
				if(clientInquiry.getQuotePrice()<lowestPrice||lowestPrice==0){
					lowestCode=clientInquiry.getSupplierCode();
					lowestPrice=clientInquiry.getQuotePrice();
				}
			}
			lowestPriceMap.put(clientOrderElementVo.getId()+"lowestCode", lowestCode);
			lowestPriceMap.put(clientOrderElementVo.getId()+"lowestPrice", lowestPrice);
			lowestPriceMap.put(clientOrderElementVo.getId()+"currency_id", clientOrderElementVo.getSupplierCurrencyId());
			Collections.sort(supplierCode);
//			arrayList.addAll(supplierListVo);
			
			
//			List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
//			List<ImportPackageElementVo> elementVos=importpackageElementService.findStorageByElementId(cieElementId, sqeElementId);
//			 for (ImportPackageElementVo importPackageElementVo : elementVos) {
//				 Double storageAmount =0.0;
//				 List<StorageFlowVo> flowVos=importpackageElementService.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
//				 	if(flowVos.size()>0){
//				 		for (StorageFlowVo storageFlowVo : flowVos) {
//							if(storageFlowVo.getStorageAmount()>0){
//								storageAmount+=storageFlowVo.getStorageAmount();
//								supplierList.add(storageFlowVo);
//							}
//						}
//				 		if(storageAmount>0){
//				 			if(null!=importPackageElementVo.getSupplierCode()&&!storageSupplierCode.contains(importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId())){
//				 				storageSupplierCode.add(importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId());
//				 			}
//				 			sqeremark.add(importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId()+"_storageremark");
//							quotenumber.add(importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId()+"_storagequoteNumber");
//							quoteamount.add(importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId()+"_storageamount");
//							supplierPrice.put(clientOrderElementVo.getId()+"-"+importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId(), importPackageElementVo.getPrice());
//							amount.put(clientOrderElementVo.getId()+"-"+importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId()+"_storageamount", storageAmount);
//							supplierRemark.put(clientOrderElementVo.getId()+"-"+importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId()+"_storageremark", importPackageElementVo.getRemark());
//							supplierNumber.put(clientOrderElementVo.getId()+"-"+importPackageElementVo.getSupplierCode()+"-"+importPackageElementVo.getSupplierQuoteId()+"_storagequoteNumber", importPackageElementVo.getQuoteNumber()+"/"+importPackageElementVo.getSupplierQuoteElementId());
//				 		}
//				 	}
//			 }
			
		}
		
		
		int beginColIndex = 8;
		
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(firstRowNum);
		int codeCount = supplierCode.size()*3;
		sheet.setForceFormulaRecalculation(true);
		createCell(row, beginColIndex, codeCount);
		int a = 0;
		int beginCol = 8;
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
			
			beginCol=cellnum+1;
		}
		
		int storageCodeCount=storageSupplierCode.size()*4;
		createCell(row, beginCol, storageCodeCount);
		int c = 0;
		//生成模板
				for(int dynamicIndex=0; dynamicIndex<storageCodeCount;  dynamicIndex++){
					int cellnum = beginCol+dynamicIndex;
					Cell cell = row.getCell(cellnum);
					if (dynamicIndex==4*c+3) {
						cell.setCellValue("备注");
						c++;
					}else if (dynamicIndex==4*c+2) {
						cell.setCellValue("库存数量");
						//a++;
					}else if (dynamicIndex==4*c+1) {
						cell.setCellValue("库存单号");
						//a++;
					}else if (dynamicIndex==4*c && c<supplierCode.size()){
						cell.setCellValue(storageSupplierCode.get(c));
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
		

		for (int rowIndex = firstRowNum+1; rowIndex <= lastRowNum; rowIndex++) {
			row = sheet.getRow(rowIndex);
			createCell(row, beginCol, storageCodeCount);
			int b = 0;
			for(int dynamicIndex=0; dynamicIndex<storageCodeCount;  dynamicIndex++){
				int cellnum = beginCol+dynamicIndex;
				Cell cell = row.getCell(cellnum);
				if (dynamicIndex==4*b+3) {
					StringBuffer remark = new StringBuffer();
					remark.append(storageSupplierCode.get(b)).append("_storageremark");
					cell.setCellValue("$"+remark);
					b++;
				}else if (dynamicIndex==4*b+2) {
					StringBuffer quoteAmount = new StringBuffer();
					quoteAmount.append(storageSupplierCode.get(b)).append("_storageamount");
					cell.setCellValue("$"+quoteAmount);
					//b++;
				}else if (dynamicIndex==4*b+1) {
					StringBuffer quoteNumber = new StringBuffer();
					quoteNumber.append(storageSupplierCode.get(b)).append("_storagequoteNumber");
					cell.setCellValue("$"+quoteNumber);
					//b++;
				}else if (dynamicIndex==4*b && b<supplierCode.size()){
					cell.setCellValue("$"+storageSupplierCode.get(b));
					
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
//		sheet.removeRow(lastRow);
		if (list != null
				&& list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row elementRow = sheet.createRow(lastRowNum + i);
				ClientOrderElementVo elementVo = list.get(i);
					   List<OrderApproval> passStock=orderApprovalService.selectByCoeIdAndState(elementVo.getId(), 1, 0);
					   List<OrderApproval> onpassPassStock=orderApprovalService.selectByCoeIdAndState(elementVo.getId(), 1, 1);
					 Double storageAmount=0.0;
					 Double onpassStorageAmount=0.0;
					   for (OrderApproval orderApproval : passStock) {
							if(orderApproval.getOccupy().equals(1)){
								storageAmount=storageAmount+orderApproval.getAmount();
							}
					   }
					   
					   for (OrderApproval orderApproval : onpassPassStock) {
							if(orderApproval.getOccupy().equals(1)){
								onpassStorageAmount=onpassStorageAmount+orderApproval.getAmount();
							}
					   }
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					int cellNumber=0;
					if (cell != null) {
//						elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							if(key.equals("ORDER_NUMBER")){
								Object value = elementVo.getOrderNumber();
								this.write(elementCell, value);
							}else if (key.equals("SN")) {
								Object value = i+1;
								this.write(elementCell, value);
							}else if (key.equals("CSN")) {
								Object value =  elementVo.getItem();
								this.write(elementCell, value);
							}
							else if (key.equals("ITEM")) {
								Object value = elementVo.getItem();
								this.write(elementCell, value);
							}  
							else if (key.equals("PART_NUMBER")) {
								Object value = elementVo.getQuotePartNumber();
								this.write(elementCell, value);
							} else if(key.equals("DESCRIPTION")){
								Object value = elementVo.getQuoteDescription();
								this.write(elementCell, value);
							}else if (key.equals("UNIT")) {
								Object value = elementVo.getQuoteUnit();
								this.write(elementCell, value);
							}else if (key.equals("AMOUNT")) {
								Object value = elementVo.getClientOrderAmount();
								this.write(elementCell, value);
							}else if (key.equals("LEAD_TIME")) {
								Row f=sheet.getRow(firstRowNum);
								Cell fcell = f.getCell(cellnum);
								elementCell.setCellStyle(fcell.getCellStyle());
								Object value = elementVo.getClientOrderLeadTime();
								this.write(elementCell, value);
							}else if (key.equals("DEADLINE")) {
								Row f=sheet.getRow(firstRowNum);
								Cell fcell = f.getCell(cellnum);
								elementCell.setCellStyle(fcell.getCellStyle());
								Object value = elementVo.getClientOrderDeadline();
								this.write(elementCell, value);
							}else if (key.equals("QUOTE_SUPPLIER")) {
								Object value = elementVo.getSupplierCode();
								this.write(elementCell, value);
							}else if (key.equals("SUPPLIER_PRICE")) {
								Object value = elementVo.getSupplierQuotePrice();
								this.write(elementCell, value);
								elementCell.setCellStyle(symbol(elementVo.getSupplierCurrencyId().toString(), wb));
							}else if (key.equals("QUOTE_REMARK")) {
								Object value = elementVo.getRemark();
								this.write(elementCell, value);
							}else if (key.equals("QUOTE_INQUIRY_NUMBER")) {
								Object value = elementVo.getSupplierInquiryQuoteNumber()+"/"+elementVo.getSupplierQuoteElementId();
								this.write(elementCell, value);
							}else if (key.equals("LOWEST_CODE")) {
								Object value = lowestPriceMap.get(elementVo.getId()+"lowestCode");
								this.write(elementCell, value);
							}else if (key.equals("LOWEST_PRICE")) {
								Object value = lowestPriceMap.get(elementVo.getId()+"lowestPrice");
								this.write(elementCell, value);
								elementCell.setCellStyle(symbol(lowestPriceMap.get(elementVo.getId()+"currency_id").toString(), wb));
							}else if (key.equals("STORAGE_AMOUNT")) {
								Object value = storageAmount;
								this.write(elementCell, value);
							}else if (key.equals("ONPASS_STORAGE_AMOUNT")) {
								Object value = onpassStorageAmount;
								this.write(elementCell, value);
							}else if (key.equals("STATUS")) {
								Object value = "执行中";
								this.write(elementCell, value);
							}else if (key.equals("SUPPLIER_ORDER_AMOUNT")) {
								Object value = elementVo.getClientOrderAmount()-storageAmount-onpassStorageAmount;
								cellNumber=cellnum;
								String[] priceColumns = getColumnLabels(cellNumber+4);
								String[] amountColumns = getColumnLabels(cellNumber+5);
								String priceCell=priceColumns[priceColumns.length-1]+(elementRow.getRowNum()+1);
								String amountCell=amountColumns[amountColumns.length-1]+(elementRow.getRowNum()+1);
								Cell totalCell = elementRow.createCell(cellNumber+5);
								totalCell.setCellFormula(priceCell+"*"+amountCell);
								this.write(elementCell, value);
							}
							else if(supplierCode.contains(key)){
									Object value=supplierPrice.get(elementVo.getId()+"-"+key);
										this.write(elementCell, value);
										elementCell.setCellStyle(symbol(elementVo.getSupplierCurrencyId().toString(), wb));
							}else if(quotenumber.contains(key)){
								Object value=supplierNumber.get(elementVo.getId()+"-"+key);
								this.write(elementCell, value);
						    }else if(storageSupplierCode.contains(key)){
								Object value=supplierPrice.get(elementVo.getId()+"-"+key);
									this.write(elementCell, value);
									elementCell.setCellStyle(symbol(elementVo.getSupplierCurrencyId().toString(), wb));
						    }else if(sqeremark.contains(key)){
								Object value=supplierRemark.get(elementVo.getId()+"-"+key);
								this.write(elementCell, value);
						    }else if(quoteamount.contains(key)){
								Object value=amount.get(elementVo.getId()+"-"+key);
								this.write(elementCell, value);
						    }
							
//							else  if(key.indexOf("_remark")>-1){
//								String[] keys=key.split("_");
//								for (int j = 0; j < arrayList.size(); j++) {
//									Integer cieElementId = supplierOrderService.getElementId(elementVo.getClientQuoteElementId());
//									Integer sqeElementId = supplierOrderService.getSqeElementId(elementVo.getClientQuoteElementId());
//									
//									if(arrayList.get(j).getInquiryElementId().equals(cieElementId)||arrayList.get(j).getQuoteElementId().equals(sqeElementId)){
//										if(arrayList.get(j).getSupplierCode().equals(keys[0])){
//											Object value = arrayList.get(j).getQuoteRemark();
//											this.write(elementCell, value);
//											break;
//										}
//									}
//								}
//							}
						}
					}
				}
//				short elementlastCellNum = elementRow.getLastCellNum();
				
//				for (int k = 0; k < supplierCode.size(); k++) {
//					Cell cell = elementRow.createCell(elementlastCellNum+8+k);
//					Object value =sqeId.get(supplierCode.get(k)+elementVo.getId());
//					if(null!=value){
//					cell.setCellValue(value.toString());
//					}
//				}
			}
//			for (int k = 0; k < supplierCode.size(); k++) {
//				sheet.setColumnHidden(19+supplierCode.size()*3+k, true);
//			}
			
			List<SystemCode> sl=systemCodeService.findType("STORE_LOCATION");
			List<SystemCode> lwl=systemCodeService.findType("LOGISTICS_WAY");
			List<SystemCode> cel=systemCodeService.findType("CERT");
			List<SystemCode> col=systemCodeService.findType("COND");
			String[] slarr =  new String[sl.size()];
			String[] lwarr =  new String[lwl.size()];
			String[] cearr =  new String[cel.size()];
			String[] coarr =  new String[col.size()];
			for (int i = 0; i < sl.size(); i++) {
				slarr[i]=sl.get(i).getValue();	
			}
			for (int i = 0; i < lwl.size(); i++) {
				lwarr[i]=lwl.get(i).getValue();	
			}
			for (int i = 0; i < cel.size(); i++) {
				cearr[i]=cel.get(i).getCode();
			}
			for (int i = 0; i < col.size(); i++) {
				coarr[i]=col.get(i).getCode();
			}
			//创建sheet2数据，以供下拉框读取 modify by tanoy
			AddSheetRow(wb,100);
			AddValidaData(wb,cearr,0);
			AddValidaData(wb,coarr,1);
			AddValidaData(wb,lwarr,2);
			int firstRow=lastRowNum;
			int lastRow1=lastRowNum + list.size() - 1;
			int other=(supplierCode.size()*3)+(storageSupplierCode.size()*4);
			CellRangeAddressList regions1 = new CellRangeAddressList(firstRow,
					lastRow1, 24+other, 24+other);
			CellRangeAddressList regions2 = new CellRangeAddressList(firstRow,
					lastRow1, 25+other, 25+other);
			CellRangeAddressList regions3 = new CellRangeAddressList(firstRow,
					lastRow1, 26+other, 26+other);
			CellRangeAddressList regions4 = new CellRangeAddressList(firstRow,
					lastRow1, 27+other, 27+other);
			CellRangeAddressList regions5 = new CellRangeAddressList(firstRow,
					lastRow1, 17+other, 17+other);
			String[] textList = new String[] { "执行中", "取消"};
			DataValidationHelper helper = sheet.getDataValidationHelper();  
			DataValidationConstraint constraint1 = helper.createExplicitListConstraint(slarr); 
			//在sheet2拿到下拉框的数据源  modify by tanoy
			DataValidationConstraint constraint2 = helper.createFormulaListConstraint("sheet2!$C$1:$C$"+lwarr.length);
			DataValidationConstraint constraint3 = helper.createFormulaListConstraint("sheet2!$B$1:$B$"+coarr.length); 
			DataValidationConstraint constraint4 = helper.createFormulaListConstraint("sheet2!$A$1:$A$"+cearr.length); 
			//DataValidationConstraint constraint2 = helper.createExplicitListConstraint(lwarr); 
			//DataValidationConstraint constraint3 = helper.createExplicitListConstraint(coarr); 
			//DataValidationConstraint constraint4 = helper.createExplicitListConstraint(cearr); 
			DataValidationConstraint constraint5 = helper.createExplicitListConstraint(textList); 
			sheet.addValidationData(sheet.getDataValidationHelper().createValidation(constraint1, regions1));
			sheet.addValidationData(sheet.getDataValidationHelper().createValidation(constraint2, regions2));
			sheet.addValidationData(sheet.getDataValidationHelper().createValidation(constraint3, regions3));
			sheet.addValidationData(sheet.getDataValidationHelper().createValidation(constraint4, regions4));
			sheet.addValidationData(sheet.getDataValidationHelper().createValidation(constraint5, regions5));
//			sheet.addValidationData(getShip(lastRowNum,
//					lastRowNum + list.size() - 1,supplierCode.size()));
//			sheet.addValidationData(getAwb(lastRowNum,
//					lastRowNum + list.size() - 1,supplierCode.size()));
			
		}
		for (int cellnum = 0; cellnum <= lastCellNum+10; cellnum++) {
				Row row2=sheet.getRow(0);
				Row f=sheet.getRow(firstRowNum);
				Cell fcell = f.getCell(0);
				Cell cell = row2.getCell(cellnum);
				cell.setCellStyle(fcell.getCellStyle());
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
		String[] columns = getColumnLabels(lastCellNum);
		CellRangeAddress bwtten = CellRangeAddress.valueOf("A1:"+columns[columns.length-1]+"1");
		sheet.setAutoFilter(bwtten);
	}
	
	private CellStyle symbol(String key,POIExcelWorkBook wb){
		/*String[] keys=key.split("-");
		String code=keys[0];
		Integer currency=supplierService.getCurrencyId(code);*/
		DataFormat format= wb.createDataFormat();  
		CellStyle cellStyle = wb.createCellStyle(); 
		Integer currency = new Integer(key);
		if(null!=currency){
			if(currency.equals(10)||currency==10){
				cellStyle.setDataFormat(format.getFormat("¥#,##0.00"));  
			}else if(currency.equals(11)||currency==11){
				cellStyle.setDataFormat(format.getFormat("$#,##0.00"));  
			}else if(currency.equals(12)||currency==12){
				cellStyle.setDataFormat(format.getFormat("€#,##0.00"));  
			}else if(currency.equals(13)||currency==13){
				cellStyle.setDataFormat(format.getFormat("￡#,##0.00"));  
			}else if(currency.equals(14)||currency==14){
				cellStyle.setDataFormat(format.getFormat("HK$#,##0.00"));  
			}
		}
		return cellStyle;
	}
	
    private static String[] sources = new String[]{  
            "A","B","C","D","E","F","G","H",  
            "I","J","K","L","M","N","O","P",  
            "Q","R","S","T","U","V","W","X","Y","Z"  
        };  
      
        /** 
         * (256 for *.xls, 16384 for *.xlsx) 
         * @param columnNum 列的个数，至少要为1 
         * @throws IllegalArgumentException 如果 columnNum 超出该范围 [1,16384] 
         * @return 返回[1,columnNum]共columnNum个对应xls列字母的数组 
         */  
        public static String[] getColumnLabels(int columnNum) {  
            if(columnNum<1||columnNum>16384)  
                throw new IllegalArgumentException();  
            String[] columns = new String[columnNum];  
            if(columnNum<27){    //小于27列 不用组合  
                System.arraycopy(sources, 0, columns, 0, columnNum);  
                return columns;  
            }         
            System.arraycopy(sources, 0, columns, 0, 26);   //前26列不需要进行组合  
      
            //因为基于数组是从0开始，每到新一轮letterIdx 会递增，所以第一轮 在递增前是-1  
            int letterIdx = -1;  
            int currentLen = 26;//第一轮组合(2个字母的组合)是分别与A-Z进行拼接 所以是26  
            int remainder;  
            int lastLen = 0;    //用于定位上文提到的i%currentLen实际在数组中的位置          
            int totalLen = 26;  //totalLen=currentLen+lastLen  
            int currentLoopIdx = 0; //用来记录当前组合所有情形的个数  
      
            for(int i=26;i<columnNum;i++){ //第27列(对应数组的第26个位置)开始组合  
      
        //currentLen是上个组合所有情形的个数，与它取余找到要与上个组合的哪种情形进行拼接  
                remainder = currentLoopIdx%currentLen;  
      
                if(remainder==0){  
                    letterIdx++; //完成一次上个组合的遍历，转到下个字母进行拼接  
                    int j = letterIdx%26;  
      
                //A-Z 26个子母都与上个组合所有情形都进行过拼接了，需要进行下个组合的拼接  
                    if(j==0&&letterIdx!=0){   
                        lastLen = totalLen; //下个组合的lastLen是上个组合的totalLen  
      
                    /** 
                     * 下个组合的currentLen是上个组合的所有组合情形的个数 
                     * （等于上个组合的currentLen*26)，26也就是拼接在前面的A-Z的个数 
                     */            
                        currentLen = 26*currentLen;  
      
                        totalLen = currentLen+lastLen; //为下一轮的开始做准备  
                        currentLoopIdx = 0; //到下一轮了 因此需要重置  
                    }  
                }  
                /** 
                 * sources[letterIdx%26]是该轮要拼接在前面的字母 
                 * columns[remainder+lastLen]是上个组合被拼接的情形 
                 */       
                columns[i] = sources[letterIdx%26]+columns[remainder+lastLen];  
                currentLoopIdx++;  
            }  
            return columns;  
        }
	
//private HSSFDataValidation getShip(int firstRow,
//		int lastRow1,int other) {
//	CellRangeAddressList regions = new CellRangeAddressList(firstRow,
//			lastRow1, 15+other*3, 15+other*3);
//	String[] textList = new String[] { "其他", "香港仓库","美国仓库"};
//	DVConstraint constraint = DVConstraint
//			.createExplicitListConstraint(textList);
//	return new HSSFDataValidation(regions, constraint);
//
//}
//
//private HSSFDataValidation getAwb(int firstRow,
//		int lastRow1,int other) {
//	CellRangeAddressList regions = new CellRangeAddressList(firstRow,
//			lastRow1, 16+other*3, 16+other*3);
//	String[] textList = new String[] { "FEDEX", "DHL",
//			"UPS", "EMS","顺丰快递", "德邦物流","天地华宇","空运","其他物流","Dropship FedEx","Dropship DHL","Dropship other","ForWarder"};
//	DVConstraint constraint = DVConstraint
//			.createExplicitListConstraint(textList);
//	return new HSSFDataValidation(regions, constraint);
//
//}
	
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
	
	public void AddSheetRow(POIExcelWorkBook wb,int amount){
		Sheet sheet2 = wb.getSheetAt(1);
		for (int i = 0; i < amount; i++) {
			sheet2.createRow(i);
		}
	}
	
	public void AddValidaData(POIExcelWorkBook wb,String[] strs,int index){
		Sheet sheet = wb.getSheetAt(1);
		for (int i = 0, length = strs.length; i < length; i++) { // 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
			sheet.getRow(i).createCell(index).setCellValue(strs[i]);
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
		
		return "SupplierWeatherOrder"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "supplier_weather_order";
	}

	/**
	 * 获取所关联表的主键列列名
	 */
	@Override
	protected String fetchYwTablePkName() {
		return "clientOrderId";
	}


	/**
	 * 获取mapping的key，用于页面设置
	 */
	@Override
	protected String fetchMappingKey() {
		return "SupplierWeatherOrderExcel";
	}

}
