package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.ClientInquiry;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
@Service("clientOrderExcelGenerator")
public class ClientOrderExcelGenerator extends ExcelGeneratorBase {
	
	@Resource
	private ClientQuoteService clientQuoteService;
	String number = null;
	
	@Override
	protected String fetchMappingKey() {
		return "ClientOrderExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"clientorder.xls";
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		List<ClientQuoteVo> clientQuote=clientQuoteService.findbyclientquoteid(ywId);//客户报价信息
		List<ClientQuoteElementVo> cqeList=clientQuoteService.findElementDate(ywId);//客户报价明细信息
		List<ClientQuoteElementVo> cieList=clientQuoteService.findClientInquiry(clientQuote.get(0).getClient_inquiry_id());
		number=clientQuote.get(0).getQuoteNumber();
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		
		
		
		Row lastRow=sheet.getRow(lastRowNum);
		short firstCellNum = lastRow.getFirstCellNum();
		short lastCellNum = lastRow.getLastCellNum();
		String[] cellKeys = new String[lastCellNum - firstCellNum + 1];
		for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
			Cell cell=lastRow.getCell(cellnum);
			if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String str = cell.getStringCellValue();
				if (str == null) {
					continue;
				}
				if (str.startsWith("$")) {
					cellKeys[cellnum - firstCellNum] = str.substring(1);
				}
			}
		}
		sheet.removeRow(lastRow);
		
		if (cqeList != null && cqeList.size() > 0) {
			int dsn=1;
			for (int i = 0; i < cqeList.size(); i++) {
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
							if(str.equals("SN")){
								value =cqeList.get(i).getItem();
							}else if(str.equals("DSN")){
								value =cqeList.get(i).getCsn();
							}else if(str.equals("PN")){
								value =cqeList.get(i).getQuotePartNumber();
							}else if(str.equals("DESC")){
								if (clientQuote.get(0).getClient_code().equals("340") || clientQuote.get(0).getClient_code().equals("313") ||
										clientQuote.get(0).getClient_code().equals("370") || clientQuote.get(0).getClient_code().equals("320")) {
									value =cqeList.get(i).getInquiryDescription();
								}else {
									value =cqeList.get(i).getQuoteDescription();
								}
							}else if(str.equals("UNIT")){
								value =cqeList.get(i).getQuoteUnit();
							}else if(str.equals("AMOUNT")){
								value =cqeList.get(i).getClientQuoteAmount();
							}else if(str.equals("PRICE")){
								value =cqeList.get(i).getClientQuotePrice();
							}else if(str.equals("LEATTIME")){
								value =cqeList.get(i).getLeadTime();
							}else if(str.equals("REMARK")){
								value =cqeList.get(i).getClientQuoteRemark();
							}else if(str.equals("FIXEDCOST")){
								value =cqeList.get(i).getFixedCost();
							}else if(str.equals("CERT")){
								value =cqeList.get(i).getCertificationCode();
							}
							
							this.write(elementCell, value);
						}
							 if (cellnum == 7) {
								elementCell.setCellFormula("G"
										+ (lastRowNum + i + 1) + "*F"
										+ (lastRowNum + i + 1));
							}
						
					}
				}
			}
			sheet.addValidationData(getCertificationValidation(lastRowNum,
					lastRowNum + cqeList.size() - 1));
		}
		
	}
	
	private HSSFDataValidation getCertificationValidation(int firstRow,
			int lastRow1) {
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,
				lastRow1, 11, 11);
		String[] textList = new String[] { "OEM COC", "FAA 8130-3",
				"EASA Form One", "Vendor COC","MFR CERT", "CAAC","FAA+CAAC","EASA+CAAC","TCCA FORM ONR","Other" };
		DVConstraint constraint = DVConstraint
				.createExplicitListConstraint(textList);
		return new HSSFDataValidation(regions, constraint);

	}

	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_FOLDER+File.separator+"sampleoutput";
		
	}

	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		
		 return number+"_"+"clientorder"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "client_order";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
