package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientInquiryDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.marketing.controller.clientquote.ProfitStatementVo;
import com.naswork.service.ClientQuoteService;
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
@Service("quoteExcelGenerator2")
public class QuoteExcelGenerator2 extends ExcelGeneratorBase {
	@Resource
	private ClientQuoteService clientQuoteService;
	String number = null;
	@Resource
	private ClientInquiryDao clientInquiryDao;
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
			return File.separator+"exceltemplate"+File.separator+"QuotationCn2.xls";
		}else
		{
			return File.separator+"exceltemplate"+File.separator+"Quotation2.xls";
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
					if (str.startsWith("$")) {//客户信息
						String key = str.substring(1);
						if(key.equals("CLIENT_CONTACT_NAME")){
							value = clientQuote.get(0).getClient_contact_name(); this.write(cell, value);
							
						}else if(key.equals("QUOTE_DATE")){
							value =  clientQuote.get(0).getInquiryDate(); this.write(cell, value);
						}else if(key.equals("CLIENT_NAME")){
							value =  clientQuote.get(0).getClient_name(); this.write(cell, value);
						}else if(key.equals("QUOTE_NUMBER")){
							value =  clientQuote.get(0).getQuoteNumber(); this.write(cell, value);
						}else if(key.equals("CLIENT_CONTACT_PHONE")){
							value =  clientQuote.get(0).getClient_contact_phone(); this.write(cell, value);
						}else if(key.equals("SOURCE_NUMBER")){
							value =  clientQuote.get(0).getSourceNumber(); this.write(cell, value);
						}else if(key.equals("CLIENT_CONTACT_FAX")){
							value =  clientQuote.get(0).getClient_contact_fax(); this.write(cell, value);
						}else if(key.equals("DEADLINE")){
							value = clientQuote.get(0).getDeadline(); this.write(cell, value);
						}
					}
				}
			}
		}
		Row  lastRow = sheet.getRow(lastRowNum);
		
		short firstCellNum = lastRow.getFirstCellNum();
		short lastCellNum =lastRow.getLastCellNum();
		String[] cellKeys = new String[lastCellNum - firstCellNum + 1];
			for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
				Cell cell=lastRow.getCell(cellnum);
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
			sheet.removeRow(lastRow);
	
			int line=9;
		if(cqeList!=null&cqeList.size()>0){
			for (int i = 0; i < cqeList.size(); i++) {
			
				Row elementRow=sheet.createRow(lastRowNum+i);
				elementRow.setHeight(lastRow.getHeight());
				
				line=line+1;
				String startcol="$B$";
				String lastcol="$C$";
				String region=startcol+line+":"+lastcol+line;
				sheet.addMergedRegion(CellRangeAddress.valueOf(region));  //合并单元格
				
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell=lastRow.getCell(cellnum);
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
						 	}else if(key.equals("DETAIL")){
//						 		 int start=0;
//					 			 int end=20;
//					 			 String DESC=cqeList.get(i).getQuoteDescription();
//					 			 String RE=cqeList.get(i).getClientQuoteRemark();
//					 			 String Description="";
//					 			 String Remark="";
//					 			 if(null!=DESC){
//					 			 int dlength=DESC.length();
//					 			 if(end<dlength){
//					 				 int max = dlength/end;
//					 				 for (int j = 0; j <= max; j++) {
//					 					 String b="";
//					 					 if(j==max){
//					 						 b= DESC.substring(start);
//					 					 }else{
//					 						 b= DESC.substring(start,end);
//					 					 }
//					 					if(!b.equals("")){
//					 						Description=Description+b+"\n";
//					 					}		
//					 					start=end;
//					 					String c=DESC.substring(end);
//					 					 end=start+40;
//					 					 if(c.length()<40){
//					 						 end=start+c.length();
//					 					 }
//									}
//					 			 }else{
//					 				Description=DESC+"\n";
//					 			 }
//					 			 }else{
//					 				Description="\n";
//					 			 }
//					 			 
//					 			 start=0;
//					 			  end=20;
//					 			 if(null!=RE&&!RE.equals("")){
//					 			 int rlength=RE.length();
//					 			 if(end<rlength){
//
//					 				  int max = rlength/end;
//					 				 for (int j = 0; j <= max; j++) {
//					 					 String b="";
//					 					 if(j==max){
//					 						  b=RE.substring(start);
//					 					 }else{
//					 					  b=RE.substring(start,end);
//					 					 }
//					 					 if(!b.equals("")){
//					 						Remark=Remark+b+"\n";
//					 					 }
//					 					 start=end;
//					 					String c=RE.substring(end);
//					 					 end=start+40;
//					 					 if(c.length()<40){
//					 						 end=start+c.length();
//					 					 }
//					 					
//									}
//					 				Remark="Remark:"+Remark;
//					 			 }else{
//						 				Remark="Remark:"+RE+"\n";
//						 			 }
//					 			 }else{
//					 				Remark="";
//					 			 }
						 		 String other="";
						 		 String Remark="";
						 		 if(null!=cqeList.get(i).getClientQuoteRemark()&&!cqeList.get(i).getClientQuoteRemark().equals("")){
						 			Remark="Remark:"+cqeList.get(i).getClientQuoteRemark();
						 			other+="\n"+Remark;
						 		 }
						 		 String warranty="";
						 		if(!StringUtils.isEmpty(cqeList.get(i).getWarranty())){
						 			warranty="Warranty:"+cqeList.get(i).getWarranty();
						 			other+="\n"+warranty;
						 		}
						 		 String serialNumber="";
						 		if(!StringUtils.isEmpty(cqeList.get(i).getSerialNumber())){
						 			serialNumber="S/N#:"+cqeList.get(i).getSerialNumber();
						 			other+="\n"+serialNumber;
						 		}
						 		 String tagSrc="";
						 		if(!StringUtils.isEmpty(cqeList.get(i).getTagSrc())){
						 			tagSrc="Tag Source:"+cqeList.get(i).getTagSrc();
						 			other+="\n"+tagSrc;
						 		}
						 		 String tagDate="";
						 		if(!StringUtils.isEmpty(cqeList.get(i).getTagDate())){
						 			tagDate="Tag Date:"+cqeList.get(i).getTagDate();
						 			other+="\n"+tagDate;
						 		}
						 		 String trace="";
						 		if(!StringUtils.isEmpty(cqeList.get(i).getTrace())){
						 			trace="Trace:"+cqeList.get(i).getTrace();
						 			other+="\n"+trace;
						 		}
						 		String description=cqeList.get(i).getQuoteDescription();
							 	String code=clientQuote.get(0).getClient_code();
							 	if(code.equals("313")||code.equals("320")||code.equals("340")||code.equals("370")){
							 		description=cqeList.get(i).getInquiryDescription();
							 	}
						 		 if(null!=cqeList.get(i).getIsMain()&&cqeList.get(i).getIsMain()==1){
						 			value =  cqeList.get(i).getQuotePartNumber()+"\n"+description+"\n"+
										 	cqeList.get(i).getCertificationCode()+other
						 			+"RFQ P/N#"+cqeList.get(i).getMainPartNumber()+"\n"+"QUOTE ALT P/N#"+cqeList.get(i).getQuotePartNumber();
						 		 }else{
						 			value =  cqeList.get(i).getQuotePartNumber()+"\n"+description+"\n"+
										 	cqeList.get(i).getCertificationCode()+other;
						 		 }
						 		int rwsTemp= value.toString().split("\n").length; 
						 		int relenght=Remark.length()/40;
						 		int desclenght=cqeList.get(i).getQuoteDescription().length()/40;
						 		int pnlenght=cqeList.get(i).getQuotePartNumber().length()/40;
						 		rwsTemp=rwsTemp+relenght+desclenght+pnlenght;
								elementRow.setHeight((short) ((short)rwsTemp*230));
						 		this.write(elementCell, value);
						 	}else if(key.equals("QUOTE_UNIT")){
						 		value =  cqeList.get(i).getQuoteUnit(); this.write(elementCell, value);
						 	}else if(key.equals("CLIENT_QUOTE_AMOUNT")){
						 		value =  cqeList.get(i).getClientQuoteAmount(); this.write(elementCell, value);
						 	}else if(key.equals("MOQ")){
						 		if(null==cqeList.get(i).getMoq()||"".equals(cqeList.get(i).getMoq())||cqeList.get(i).getMoq()==0){
						 			value ="";
						 		}else{
						 		value =  cqeList.get(i).getMoq().intValue();
						 		}
						 		this.write(elementCell, value);
						 	}else if(key.equals("CONDITION_CODE")){
						 		value =  cqeList.get(i).getConditionCode(); this.write(elementCell, value);
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
						if (cellnum == 8) {
							int amount= cqeList.get(i).getClientQuoteAmount().intValue();
							int moq=0;
							if(null!=cqeList.get(i).getMoq()&&!"".equals(cqeList.get(i).getMoq())&&cqeList.get(i).getMoq()!=0){
							 moq=cqeList.get(i).getMoq().intValue();
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
				
				
			
				
			}
		}
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
						if ("$SUM".equals(str)) {
							cell1.setCellFormula("SUM(I10:I" + (lastRow2 + 1)
									+ ")");
						} else if ("$FREIGHT".equals(str)) {
//							cell1.setCellFormula("I" + (lastRow2 + 2)
//									+ "*0.1");
							cell1.setCellValue(clientQuote.get(0).getFreight());
						} else if ("$TOTAL".equals(str)) {
							cell1.setCellFormula("SUM(I" + (lastRow2 + 2) + ":I"
									+ (lastRow2 + 3) + ")");
						} else if("$MINFREIGHT".equals(str)){
							cell1.setCellValue(clientQuote.get(0).getLowestFreight());
						}
						else {
							cell1.setCellValue(str);
						}
						break;
					}
				}
			}
		}
		int lastRow3 = sheet.getLastRowNum()+1;
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
		return "QuotationExcel2";
	}

}
