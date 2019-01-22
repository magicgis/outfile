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
import com.naswork.dao.CrmStockDao;
import com.naswork.model.ClientInquiry;
import com.naswork.module.crmstock.controller.LiluVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
@Service("liluExcelGenerator")
public class LiluExcelGenerator extends ExcelGeneratorBase {
	
	@Resource
	private  CrmStockDao crmStockDao;
	String number = null;
	
	@Override
	protected String fetchMappingKey() {
		return "LiluExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"test2.xlsx";
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		List<LiluVo> list = crmStockDao.getLiluList();
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
		//sheet.removeRow(lastRow);
		
		if (list != null && list.size() > 0) {
			int dsn=1;
			for (int i = 0; i < list.size(); i++) {
				Row elementRow=sheet.createRow(lastRowNum+i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell=lastRow.getCell(cellnum);
					Cell elementCell=elementRow.createCell(cellnum);
					if (cell != null) {
						//elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
						String str=	cellKeys[cellnum - firstCellNum];
						Object value =null;
							if(str.equals("SN")){
								value =list.get(i).getSn();
							}else if(str.equals("PART")){
								value =list.get(i).getPart();
							}else if(str.equals("SHORT")){
								value =list.get(i).getShortPart();
							}else if(str.equals("IFDOUBLE")){
								value =list.get(i).getIfDouble();
							}else if(str.equals("DESC")){
								value =list.get(i).getDesc();
							}else if(str.equals("UNIT")){
								value =list.get(i).getUnit();
							}else if(str.equals("AMOUNT")){
								value =list.get(i).getAmount();
							}else if(str.equals("PRICE")){
								value =list.get(i).getPrice();
							}else if(str.equals("LEADTIME")){
								value =list.get(i).getLeadtime();
							}else if(str.equals("CON")){
								value =list.get(i).getCon();
							}else if(str.equals("CER")){
								value =list.get(i).getCer();
							}else if(str.equals("REMARK")){
								value =list.get(i).getRemark();
							}else if(str.equals("MOQ")){
								value =list.get(i).getMoq();
							}else if(str.equals("ADDRESS")){
								value =list.get(i).getAddress();
							}else if(str.equals("BLACK")){
								value =list.get(i).getBlack();
							}else if(str.equals("OEM")){
								value =list.get(i).getOem();
							}else if(str.equals("TYPE")){
								value =list.get(i).getType();
							}else if(str.equals("IFCON")){
								value =list.get(i).getIfCon();
							}
							this.write(elementCell, value);
						}
							 /*if (cellnum == 7) {
								elementCell.setCellFormula("G"
										+ (lastRowNum + i + 1) + "*F"
										+ (lastRowNum + i + 1));
							}*/
						
					}
				}
			}
			/*sheet.addValidationData(getCertificationValidation(lastRowNum,
					lastRowNum + cqeList.size() - 1));*/
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
		
		 return number+"_"+"lilu"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "lilu";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
