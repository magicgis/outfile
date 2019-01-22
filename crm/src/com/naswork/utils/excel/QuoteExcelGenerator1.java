package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.service.ClientQuoteService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
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
@Service("quoteExcelGenerator")
public class QuoteExcelGenerator1 extends ExcelGeneratorBase {
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	String number = null;
//	private List<ClientQuoteVo> clientQuote;
//	private List<ClientQuoteElementVo> cqeList;
//	private List<ClientQuoteElementVo> cieList;
	/**
	 * 获取数据，即根据ywId来获取数据，因为对于每个实体类，表名和主键列名实际上已知，无需作为参数传入
	 */
//	@Override
//	public void fetchData(String ywId) {
//		 clientQuote=clientQuoteService.findbyclientquoteid(ywId);//客户报价信息
//		 cqeList=clientQuoteService.findElementDate(ywId);//客户报价明细信息
//		 cieList=clientQuoteService.findClientInquiry(clientQuote.get(0).getClient_inquiry_id());
//	}

	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		if(currencyId.equals("10")){
			return File.separator+"exceltemplate"+File.separator+"QuotationCn.xls";
		}else
		{
			return File.separator+"exceltemplate"+File.separator+"Quotation.xls";
		}
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		List<ClientQuoteVo> clientQuote=new ArrayList<ClientQuoteVo>();
		List<ClientQuoteElementVo> cqeList=new ArrayList<ClientQuoteElementVo>();
		if(ywId.indexOf("-")>-1){
			String[] ids=ywId.split("-");
			ClientQuote quote=new ClientQuote();
			quote.setIds(ids[1]);
			 cqeList=clientQuoteService.findElementDateByids(quote);//客户报价明细信息
			 clientQuote=clientQuoteService.findbyclientquoteid(ids[1].split(",")[0]);//客户报价信息
		}else{
			 cqeList=clientQuoteService.findElementDate(ywId);//客户报价明细信息
			 clientQuote=clientQuoteService.findbyclientquoteid(ywId);//客户报价信息
		}
		
		
		List<ClientQuoteElementVo> cieList=clientQuoteService.findClientInquiry(clientQuote.get(0).getClient_inquiry_id());
		number=clientQuote.get(0).getQuoteNumber();
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		Object value =null;
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		int startRowNum=0;
		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {
			Row row=sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {
				Cell cell=row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if(str.equals("Item")){
						startRowNum=rowIndex;
					}
					if (str.startsWith("$")) {//客户信息
						String key = str.substring(1);
						if(key.equals("CLIENT_CONTACT_NAME")){
							value = clientQuote.get(0).getClient_contact_name(); this.write(cell, value);
							
						}else if(key.equals("inquiryDate")){
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							Date today = new Date();
							//value =  clientQuote.get(0).getInquiryDate(); 
							this.write(cell, today);
						}else if(key.equals("clientCode")){
							value =  clientQuote.get(0).getClient_code(); this.write(cell, value);
						}else if(key.equals("sourceNumber")){
							value =  clientQuote.get(0).getSourceNumber(); this.write(cell, value);
//							if(value.toString().length()>28){
//								sheet.setColumnWidth(cellnum, value.toString().length()*216);
//							}
						}else if(key.equals("deadline")){
							//value =  clientQuote.get(0).getDeadline(); 
							Date today = new Date();
							Calendar rightNow = Calendar.getInstance();
							rightNow.setTime(today);
							rightNow.add(Calendar.DAY_OF_YEAR,30);
							Date validay=rightNow.getTime();
							this.write(cell, validay);
						}else if(key.equals("quoteNumber")){
							value =  clientQuote.get(0).getQuoteNumber(); this.write(cell, value);
						}else if(key.equals("delivery")){
							value =  clientQuote.get(0).getTermsOfDeliveryValue(); this.write(cell, value);
						}else if(key.equals("payment")){
							if(clientQuote.get(0).getReceivePayPeriod()==30&&clientQuote.get(0).getReceivePayRate()==1){
								value ="NET 30 DAYS";
							}else if(clientQuote.get(0).getReceivePayPeriod()>=45&&clientQuote.get(0).getReceivePayRate()==1){
								value ="NET 45 DAYS EOM";
							}else if(clientQuote.get(0).getPrepayRate()==1){
								value ="100% Pay In Advance";
							}else if(clientQuote.get(0).getShipPayRate()==1){
								value ="100% Pay before shipment";
							}else if(clientQuote.get(0).getPrepayRate()==0&&clientQuote.get(0).getShipPayPeriod()==30&&clientQuote.get(0).getReceivePayPeriod()==30){
								Double spr=clientQuote.get(0).getShipPayRate()*100;
								Double rpr=clientQuote.get(0).getReceivePayRate()*100;
								value =spr.intValue()+"%Pay within 30days after receipt"+"\n"+" of parts + "
							+rpr.intValue()+"%"+ "Pay within 30days"+"\n"+" after receipt of all parts of this PO";
							}else if(clientQuote.get(0).getPrepayRate()==0.05&&clientQuote.get(0).getShipPayRate()==0.95){
								value ="5% of total amount pay in advance, 95%  before shipment";
							}else{
								value="";
							}
							int rwsTemp = value.toString().split("\n").length;  
							if(rwsTemp==3){
								row.setHeight((short) ((short)rwsTemp*100));
							}
						 this.write(cell, value);
						}
					}
				}
			}
		}
		String[] cellKeys = null ;
		short firstCellNum = 0;
		short lastCellNum = 0;
		for (int i = 0; i < lastRowNum-startRowNum-1; i++) {
			Row startRow=sheet.getRow(startRowNum+i);
			 firstCellNum = startRow.getFirstCellNum();
			 lastCellNum = startRow.getLastCellNum();
			 if(null==cellKeys){
				 cellKeys = new String[lastCellNum - firstCellNum + 1];
			 }
			for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
				Cell cell=startRow.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str=cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {
						cellKeys[cellnum - firstCellNum] = str.substring(1);
					}
				}
			}
			if(!StringUtils.isEmpty(cellKeys[lastCellNum- firstCellNum-1])){
				break;
			}
		}
		
		Row backwardRow1=sheet.getRow(lastRowNum-1);
		Row backwardRow2=sheet.getRow(lastRowNum-2);
		Row backwardRow3=sheet.getRow(lastRowNum-3);
		Row backwardRow4=sheet.getRow(lastRowNum-4);
		Row backwardRow5=sheet.getRow(lastRowNum-5);
		Row backwardRow6=sheet.getRow(lastRowNum-6);
		Row backwardRow7=sheet.getRow(lastRowNum-7);
		Row backwardRow8=sheet.getRow(lastRowNum-8);
		Row backwardRow9=sheet.getRow(lastRowNum-9);
		Row backwardRow10=sheet.getRow(lastRowNum-10);
		Row lastRow=sheet.getRow(lastRowNum);
		
		sheet.removeRow(backwardRow1);
		sheet.removeRow(backwardRow2);
		sheet.removeRow(backwardRow3);
		sheet.removeRow(backwardRow4);
		sheet.removeRow(backwardRow5);
		sheet.removeRow(backwardRow6);
		sheet.removeRow(backwardRow7);
		sheet.removeRow(backwardRow8);
		sheet.removeRow(backwardRow9);
//		sheet.removeRow(backwardRow4);
		sheet.removeRow(lastRow);
		
	
		
		int num = lastRowNum-10;
//		if(StringUtils.isEmpty(cqeList.get(0).getWarranty())&&StringUtils.isEmpty(cqeList.get(0).getTagDate())&&
//				StringUtils.isEmpty(cqeList.get(0).getTagSrc())&&StringUtils.isEmpty(cqeList.get(0).getTagSrc())&&
//				StringUtils.isEmpty(cqeList.get(0).getSerialNumber())){
//			num = lastRowNum-4;
//		}
		int line=11;
		int numcount=num+1;
		if(cqeList!=null&cqeList.size()>0){
			for (int i = 0; i < cqeList.size(); i++) {
				if (cqeList.get(i).getCsn().equals(57)) {
					int a = 1;
					a = 2;
				}
				if(cqeList.get(i).getIsBlacklist().equals(1)){
					continue;
				}
				Row coreRow=backwardRow5;
				Row elementRow=backwardRow9;
				Row conRow=backwardRow8;
				Row lcaRow=backwardRow7;
				Row certRow=backwardRow6;
				Row warrRow=backwardRow4;
				Row serialRow=backwardRow3;
				Row tagsrcRow=backwardRow2;
				Row tagdateRow=backwardRow1;
				Row traceRow=lastRow;
				Row toptRow=backwardRow10;
				
				 elementRow=sheet.createRow(numcount);
				 conRow=sheet.createRow(++numcount);
				 lcaRow=sheet.createRow(++numcount);
				 certRow=sheet.createRow(++numcount);
				 line=line+4;
				
				if(!StringUtils.isEmpty(cqeList.get(i).getWarranty())){
					 warrRow=sheet.createRow(++numcount);
					 ++line;
				}
				if(!StringUtils.isEmpty(cqeList.get(i).getSerialNumber())){
					 serialRow=sheet.createRow(++numcount);
					 ++line;
				}
				if(!StringUtils.isEmpty(cqeList.get(i).getTagSrc())){
					 tagsrcRow=sheet.createRow(++numcount);
					 ++line;
				}
						
				if(!StringUtils.isEmpty(cqeList.get(i).getTagDate())){
						 tagdateRow=sheet.createRow(++numcount);
						 ++line;
				}
				
				if(!StringUtils.isEmpty(cqeList.get(i).getTrace())){
						 traceRow=sheet.createRow(++numcount);
						 ++line;
				}
				if(!StringUtils.isEmpty(cqeList.get(i).getCoreCharge())){
					coreRow=sheet.createRow(++numcount);
					 ++line;
				}
				
					 toptRow=sheet.createRow(++numcount);
					 ++line;
					 numcount=numcount+1;
				
				
				String startcol="$C$";
				String lastcol="$D$";
				String region=startcol+line+":"+lastcol+line;
				sheet.addMergedRegion(CellRangeAddress.valueOf(region));  //合并单元格
				
				conRow.setHeight(backwardRow8.getHeight());
				lcaRow.setHeight(backwardRow7.getHeight());
				certRow.setHeight(backwardRow6.getHeight());
				coreRow.setHeight(backwardRow5.getHeight());
				warrRow.setHeight(backwardRow4.getHeight()); 
				serialRow.setHeight(backwardRow3.getHeight());
				tagsrcRow.setHeight(backwardRow2.getHeight());
				tagdateRow.setHeight(backwardRow1.getHeight());
				traceRow.setHeight(lastRow.getHeight());
				toptRow.setHeight(backwardRow10.getHeight());
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell=backwardRow9.getCell(cellnum);
//					if(StringUtils.isEmpty(cqeList.get(i).getWarranty())&&StringUtils.isEmpty(cqeList.get(i).getTagDate())&&
//							StringUtils.isEmpty(cqeList.get(i).getTagSrc())&&StringUtils.isEmpty(cqeList.get(i).getTagSrc())&&
//							StringUtils.isEmpty(cqeList.get(i).getSerialNumber())){
//						 cell=backwardRow3.getCell(cellnum);
//					}
					Cell elementCell=elementRow.createCell(cellnum);
					if(cell!=null){
						elementCell.setCellStyle(cell.getCellStyle());
						
						if (!StringUtils.isEmpty(cellKeys[cellnum- firstCellNum])) {
							String key=cellKeys[cellnum - firstCellNum];
								 	if(key.equals("ITEM")){
								 		value = cqeList.get(i).getCsn()	;
								 		if(value.equals(0)||null==value||"".equals(value)){
								 			value=cqeList.get(i).getItem();
								 		}
								 		 this.write(elementCell, value);
								 	}else if(key.equals("PARTNUMBER")){
//								 			 List<ProfitStatementVo>  ciaeList=clientQuoteService.findAlterNumber(cqeList.get(i).getInquiryElementId());
//								 			if(null!=ciaeList&&ciaeList.size()>0){
//								 				for (ProfitStatementVo ciae : ciaeList) {
//								 					for (int k = 0; k<cieList.size(); k++) {
//													if(ciae.getClientInquiryElementId().equals(cieList.get(k).getId())){
//														value = cqeList.get(i).getQuotePartNumber()+"\n\t\t"+cqeList.get(i).getQuoteDescription()+"\n"
//																+"RFQ P/N#"+cieList.get(k).getPartNumber()+"\n"+"QUOTE ALT P/N#"+cqeList.get(i).getQuotePartNumber();
//														 int rwsTemp = value.toString().split("\n").length;  
//														elementRow.setHeight((short) ((short)rwsTemp*230));//自动列高
//														break;
//													}
//													else{
//														value =  cqeList.get(i).getQuotePartNumber()+"\n\t\t"+cqeList.get(i).getQuoteDescription();
//														int rwsTemp = value.toString().split("\n").length;  
//														elementRow.setHeight((short) ((short)rwsTemp*230));
////														elementRow.setHeight(backwardRow3.getHeight());
//													}
//								 					}
//												}
//								 				this.write(elementCell, value);
//								 			}else{
//								 				value =  cqeList.get(i).getQuotePartNumber()+"\n\t\t"+cqeList.get(i).getQuoteDescription();
//								 				int rwsTemp = value.toString().split("\n").length;  
//												elementRow.setHeight((short) ((short)rwsTemp*230));
//								 				this.write(elementCell, value);
//								 			}
//								 		 int start=0;
//							 			 int end=20;
//							 			 String DESC=cqeList.get(i).getQuoteDescription();
//							 			 String RE=cqeList.get(i).getClientQuoteRemark();
//							 			 String Description="";
//							 			 int dlength=DESC.length();
//							 			 if(end<dlength){
//							 				 int max = dlength/end;
//							 				 for (int j = 0; j <= max; j++) {
//							 					 String b="";
//							 					 if(j==max){
//							 						 b= DESC.substring(start);
//							 					 }else{
//							 						 b= DESC.substring(start,end);
//							 					 }
//							 					if(!b.equals("")){
//							 						Description=Description+b+"\n";
//							 					}		
//							 					start=end;
//							 					String c=DESC.substring(end);
//							 					 end=start+40;
//							 					 if(c.length()<40){
//							 						 end=start+c.length();
//							 					 }
//											}
//							 			 }else{
//							 				Description=DESC+"\n";
//							 			 }
								 	String description=cqeList.get(i).getQuoteDescription();
								 	String code=clientQuote.get(0).getClient_code();
								 	if(code.equals("313")||code.equals("320")||code.equals("340")||code.equals("370")){
								 		description=cqeList.get(i).getInquiryDescription();
								 	}
								 			 if(null!=cqeList.get(i).getIsMain()&&cqeList.get(i).getIsMain()==1){
								 				value = cqeList.get(i).getQuotePartNumber()+"\n"+description+"\n"
														+"RFQ P/N#"+cqeList.get(i).getMainPartNumber()+"\n"+"QUOTE ALT P/N#"+cqeList.get(i).getQuotePartNumber();
								 			 }else{
								 				value =  cqeList.get(i).getQuotePartNumber()+"\n"+description;
								 			 }
								 			
								 			this.write(elementCell, value);
								 			String a=elementCell.getStringCellValue();
								 			int rwsTemp = a.split("\n").length;  
											elementRow.setHeight((short) ((short)rwsTemp*230));
											
								 	}else if(key.equals("QUOTE_UNIT")){
								 		value =  cqeList.get(i).getQuoteUnit(); this.write(elementCell, value);
								 	}else if(key.equals("CLIENT_QUOTE_AMOUNT")){
								 		value =  cqeList.get(i).getClientQuoteAmount().intValue();this.write(elementCell, value);
								 	}else if(key.equals("MOQ")){
								 		if(null==cqeList.get(i).getMoq()||"".equals(cqeList.get(i).getMoq())||"0".equals(cqeList.get(i).getMoq())){
								 			value ="";
								 		}else{
								 		value =  cqeList.get(i).getMoq().toString();
								 		}
								 		this.write(elementCell, value);
								 	}else if(key.equals("CLIENT_QUOTE_PRICE")){
								 		elementCell.setCellStyle(currencyFormat(clientQuote.get(0).getCurrency_value(),wb,cell.getCellStyle()));
								 		value =  cqeList.get(i).getClientQuotePrice(); this.write(elementCell, value);
								 	}else if(key.equals("LEAD_TIME")){
								 		value =  cqeList.get(i).getLeadTime(); this.write(elementCell, value);
								 	}else if(key.equals("CLIENT_QUOTE_REMARK")){
								 		value =  cqeList.get(i).getClientQuoteRemark(); this.write(elementCell, value);
								 	}else{
								 		value =""; this.write(elementCell, value);
								 	}
								 	
						}
						if (cellnum == 9) {
							int amount= cqeList.get(i).getClientQuoteAmount().intValue();
							int moq=0;
							if(null!=cqeList.get(i).getMoq()&&!"".equals(cqeList.get(i).getMoq())&&!"0".equals(cqeList.get(i).getMoq())){
							 moq=new Integer(cqeList.get(i).getMoq());
							}
							elementCell.setCellStyle(currencyFormat(clientQuote.get(0).getCurrency_value(),wb,cell.getCellStyle()));
							if(amount>moq||amount==moq){
								elementCell.setCellFormula("E"//总价，数量*单价
										+ (elementRow.getRowNum()+1 ) + "*H"
										+ (elementRow.getRowNum()+1 ));
							}else if(moq>amount){
								elementCell.setCellFormula("F"//总价，最小订单数量*单价
										+ (elementRow.getRowNum()+1 ) + "*H"
										+ (elementRow.getRowNum()+1 ));
							}
							
						}
					}
				}
			
				if(StringUtils.isEmpty(cqeList.get(i).getWarranty())&&StringUtils.isEmpty(cqeList.get(i).getTagDate())&&
						StringUtils.isEmpty(cqeList.get(i).getTagSrc())&&StringUtils.isEmpty(cqeList.get(i).getTrace())&&
						StringUtils.isEmpty(cqeList.get(i).getSerialNumber()) && StringUtils.isEmpty(cqeList.get(i).getCoreCharge())){
					for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
						Cell cell=backwardRow8.getCell(cellnum);
						if(cell!=null){
						String str=cell.getStringCellValue();
						Cell conCell=conRow.createCell(cellnum);
							conCell.setCellStyle(cell.getCellStyle());
								if(str.equals("Con")){
								value =  "Con"; this.write(conCell, value);
								}
							if(str.equals("$CONDITION_CODE")){
								value =  cqeList.get(i).getConditionCode(); this.write(conCell, value);
									}	
							}
					}
					for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
						Cell cell=backwardRow7.getCell(cellnum);
						if(cell!=null){
						String str=cell.getStringCellValue();
						Cell conCell=lcaRow.createCell(cellnum);
						
							conCell.setCellStyle(cell.getCellStyle());
								if(str.equals("Location")){
								value =  "Location"; this.write(conCell, value);
								}
							if(str.equals("FL, USA")){
								value =  cqeList.get(i).getQuoteLocation(); this.write(conCell, value);
									}	
							}
					}
					CellStyle cellStyle7=wb.createCellStyle();
					CellStyle cellStyle8=wb.createCellStyle();
					CellStyle cellStyle9=wb.createCellStyle();
					Font font=wb.createFont();
					for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
						Cell cell=backwardRow6.getCell(cellnum);
						
						Cell conCell=certRow.createCell(cellnum);
						if(cell!=null){
							String str=cell.getStringCellValue();
							
//							conCell.setCellStyle(cell.getCellStyle());
							if(cellnum==0){
								cellStyle7.setBorderLeft(CellStyle.BORDER_THIN);//左边框
								conCell.setCellStyle(cellStyle7);
							}
							if(cellnum==2||cellnum==3){
								font.setFontName("Lucida Sans Unicode");
								font.setFontHeightInPoints((short) 7);
								cellStyle8.setFont(font);
								conCell.setCellStyle(cellStyle8);
							}
							if(cellnum==8||cellnum==9){						
								cellStyle9.setBorderRight(CellStyle.BORDER_THIN);//右边框
//								conCell.setCellType(cellStyle.getBorderRight());
								conCell.setCellStyle(cellStyle9);
							}
							
								if(str.equals("Cert.Type")){
								value =  "Cert.Type"; this.write(conCell, value);
								}
							if(str.equals("$certificationCode")){
								value =  cqeList.get(i).getCertificationCode(); this.write(conCell, value);
									}	
							}
					}
				}else{
					for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
						Cell cell=backwardRow8.getCell(cellnum);
						if(cell!=null){
						String str=cell.getStringCellValue();
						Cell conCell=conRow.createCell(cellnum);
							conCell.setCellStyle(cell.getCellStyle());
								if(str.equals("Con")){
								value =  "Con"; this.write(conCell, value);
								}
							if(str.equals("$CONDITION_CODE")){
								value =  cqeList.get(i).getConditionCode(); this.write(conCell, value);
									}	
							}
					}
					for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
						Cell cell=backwardRow7.getCell(cellnum);
						if(cell!=null){
						String str=cell.getStringCellValue();
						Cell conCell=lcaRow.createCell(cellnum);
					
							conCell.setCellStyle(cell.getCellStyle());
								if(str.equals("Location")){
								value =  "Location"; this.write(conCell, value);
								}
							if(str.equals("FL, USA")){
								value =  cqeList.get(i).getQuoteLocation(); this.write(conCell, value);
									}	
							}
					}
				
					for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
						Cell cell=backwardRow6.getCell(cellnum);
						if(cell!=null){
						String str=cell.getStringCellValue();
						Cell conCell=certRow.createCell(cellnum);
							conCell.setCellStyle(cell.getCellStyle());
								if(str.equals("Cert.Type")){
								value =  "Cert.Type"; this.write(conCell, value);
								}
							if(str.equals("$certificationCode")){
								value =  cqeList.get(i).getCertificationCode(); this.write(conCell, value);
									}	
							}
					}

					if(!StringUtils.isEmpty(cqeList.get(i).getWarranty())){
						for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
							Cell cell=backwardRow4.getCell(cellnum);
							if(cell!=null){
							String str=cell.getStringCellValue();
							Cell conCell=warrRow.createCell(cellnum);
								conCell.setCellStyle(cell.getCellStyle());
									if(str.equals("Warranty")){
									value =  "Warranty"; this.write(conCell, value);
									}
								if(str.equals("$WARRANTY")){
									value =  cqeList.get(i).getWarranty(); this.write(conCell, value);
										}	
								}
						}
					}
					if(!StringUtils.isEmpty(cqeList.get(i).getSerialNumber())){
						for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
							Cell cell=backwardRow3.getCell(cellnum);
							if(cell!=null){
							String str=cell.getStringCellValue();
							Cell conCell=serialRow.createCell(cellnum);
								conCell.setCellStyle(cell.getCellStyle());
									if(str.equals("Serial Number")){
									value =  "Serial Number"; this.write(conCell, value);
									}
								if(str.equals("$SERIAL_NUMBER")){
									value =  cqeList.get(i).getSerialNumber(); this.write(conCell, value);
										}	
								}
						}
					}
					if(!StringUtils.isEmpty(cqeList.get(i).getTagSrc())){
						for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
							Cell cell=backwardRow2.getCell(cellnum);
							if(cell!=null){
							String str=cell.getStringCellValue();
							Cell conCell=tagsrcRow.createCell(cellnum);
								conCell.setCellStyle(cell.getCellStyle());
									if(str.equals("Tag Src")){
									value =  "Tag Src"; this.write(conCell, value);
									}
								if(str.equals("$TAG_SRC")){
									value =  cqeList.get(i).getTagSrc(); this.write(conCell, value);
										}	
								}
						}
					}
					if(!StringUtils.isEmpty(cqeList.get(i).getCoreCharge())){
						for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
							Cell cell=backwardRow5.getCell(cellnum);
							if(cell!=null){
							String str=cell.getStringCellValue();
							Cell conCell=coreRow.createCell(cellnum);
								conCell.setCellStyle(cell.getCellStyle());
									if(str.equals("Core Charge")){
									value =  "Core Charge"; this.write(conCell, value);
									}
								if(str.equals("$CORE_CHARGE")){
									value =  cqeList.get(i).getCoreCharge(); this.write(conCell, value);
										}	
								}
						}
					}
					if(!StringUtils.isEmpty(cqeList.get(i).getTagDate())){
						for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
							Cell cell=backwardRow1.getCell(cellnum);
							if(cell!=null){
							String str=cell.getStringCellValue();
							Cell conCell=tagdateRow.createCell(cellnum);
								conCell.setCellStyle(cell.getCellStyle());
									if(str.equals("Tag Date")){
									value =  "Tag Date"; this.write(conCell, value);
									}
								if(str.equals("$TAG_DATE")){
									value =  cqeList.get(i).getTagDate(); this.write(conCell, value);
										}	
								}
						}
					}
					if(!StringUtils.isEmpty(cqeList.get(i).getTrace())){
						for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
							Cell cell=lastRow.getCell(cellnum);
							
							Cell conCell=traceRow.createCell(cellnum);
							if(cell!=null){
								String str=cell.getStringCellValue();
								
		//						conCell.setCellStyle(cell.getCellStyle());
								Font font=wb.createFont();
								CellStyle cellStyle3=wb.createCellStyle();
								if(cellnum==0){
									cellStyle3.setBorderLeft(CellStyle.BORDER_THIN);//左边框
									conCell.setCellStyle(cellStyle3);
								}
								CellStyle cellStyle4=wb.createCellStyle();
								if(cellnum==2||cellnum==3){
									font.setFontName("Lucida Sans Unicode");
									font.setFontHeightInPoints((short) 7);
									cellStyle4.setFont(font);
									conCell.setCellStyle(cellStyle4);
								}
								CellStyle cellStyle5=wb.createCellStyle();
								if(cellnum==8||cellnum==9){						
									cellStyle5.setBorderRight(CellStyle.BORDER_THIN);//右边框
		//							conCell.setCellType(cellStyle.getBorderRight());
									conCell.setCellStyle(cellStyle5);
								}
								
									if(str.equals("Trace")){
									value =  "Trace"; this.write(conCell, value);
									}
								if(str.equals("$TRACE")){
									value =  cqeList.get(i).getTrace(); this.write(conCell, value);
										}	
								}
						}
					}
				
			}
				CellStyle cellStyle2=wb.createCellStyle();
				CellStyle cellStyle=wb.createCellStyle();
				for (int cellnum = firstCellNum; cellnum <= lastCellNum-1; cellnum++) {
//					Cell cell=backwardRow4.getCell(cellnum);
					Cell conCell=toptRow.createCell(cellnum);
					if(cellnum==0){
						cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);//左边框
						conCell.setCellStyle(cellStyle2);
					}
					if(cellnum==8||cellnum==9){
//						cellStyle.setBorderLeft(CellStyle.BORDER_THIN);//左边框
						cellStyle.setBorderRight(CellStyle.BORDER_THIN);//右边框
//						conCell.setCellType(cellStyle.getBorderRight());
						if(i==cqeList.size()-1){
							cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
						}
						conCell.setCellStyle(cellStyle);
					}
				}
			}
		}
		Sheet sheet2=wb.getSheetAt(wb.getActiveSheetIndex()+1);//拿第二个表格复制过来第一个表格
		sheet2.setForceFormulaRecalculation(true);
		wb.removeSheetAt(wb.getActiveSheetIndex() + 1);
		int lastRow2 = sheet.getLastRowNum();
		/*for (int i = 0; i < sheet2.getNumMergedRegions(); i++) {
			CellRangeAddress cra2 = sheet2.getMergedRegion(i);
			CellRangeAddress cra = new CellRangeAddress(cra2.getFirstRow()
					+ lastRow2 + 1, cra2.getLastRow() + lastRow2 + 1,
					cra2.getFirstColumn(), cra2.getLastColumn());
			sheet.addMergedRegion(cra);
		}*/
		
		for (int rowIndex = sheet2.getFirstRowNum(); rowIndex <= sheet2
				.getLastRowNum(); rowIndex++) {
			Row row1 = sheet.createRow(lastRow2 + rowIndex +1);
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
						cell1.setCellStyle(currencyFormat(clientQuote.get(0).getCurrency_value(),wb,cell2.getCellStyle()));
						if ("$USER".equals(str.toUpperCase())) {
							UserVo userVo = ContextHolder.getCurrentUser();
							cell1.setCellStyle(signFormat(userVo.getLoginName(),wb,cell2.getCellStyle()));
							cell1.setCellValue(userVo.getLoginName());
						}else if ("$SUM".equals(str)) {
							
							cell1.setCellFormula("SUM(J11:J" + (lastRow2 -3)
									+ ")");
//							 String a=cell1.getNumericCellValue()+"";
//							sheet.setColumnWidth(11, a.length()*216);
						} else if ("$CHARGES".equals(str)) {
							cell1.setCellFormula("J" + (lastRow2 + 2)
									+ "*0.01+800");
//							 String b=cell1.getNumericCellValue()+"";
//							sheet.setColumnWidth(11, b.length()*216);
						} else if ("$TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(J" + (lastRow2 + 2) + ":J"
									+ (lastRow2 + 3) + ")");
//							 String c=cell1.getNumericCellValue()+"";
//							sheet.setColumnWidth(11, c.length()*216);
						} else if ("$TODAY".equals(str)) {
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							Date today = new Date();
							cell1.setCellValue(sdf.format(today));
						} else {
							cell1.setCellValue(str);
						}
						break;
					}
				}
			}
		}
		int lastRow3 = sheet.getLastRowNum()+1;
		String startcol="$A$";
		String lastcol="$I$";
		String region=startcol+(lastRow3 -2)+":"+lastcol+(lastRow3-2);
		sheet.addMergedRegion(CellRangeAddress.valueOf(region)); 
		wb.setPrintArea(0, "$A$1:$J$"+lastRow3);
		for (ClientQuoteVo quote : clientQuote) {
			ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(quote.getClient_inquiry_id());
			clientInquiry.setInquiryStatusId(33);
			clientInquiryDao.updateByPrimaryKeySelective(clientInquiry);
		}
	}
	
	/**
	 * 改变表格币种格式
	 * @param value
	 * @param wb
	 * @param Style
	 * @return
	 * @author tanoy
	 */
	private CellStyle currencyFormat(String value,POIExcelWorkBook wb,CellStyle Style){
		DataFormat format= wb.createDataFormat();  
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
	
	/**
	 * 改变签名格式
	 * @param value
	 * @param wb
	 * @param Style
	 * @return
	 * @author tanoy
	 */
	private CellStyle signFormat(String value,POIExcelWorkBook wb,CellStyle Style){
		DataFormat format= wb.createDataFormat();  
		if(null!=value){
			Font font = wb.createFont();
			if(value.toLowerCase().equals("amber")){
		        font.setFontHeightInPoints((short) 12);  
		        font.setFontName("Arial");
			}else {
		        font.setFontHeightInPoints((short) 16);  
		        font.setFontName("Vladimir Script");  
			}
			Style.setFont(font);
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
		
		return number+"_"+"Quotation"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "client_quote_element";
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
		return "QuotationExcel1";
	}

}
