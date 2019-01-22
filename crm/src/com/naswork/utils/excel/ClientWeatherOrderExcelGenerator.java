package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ClientWeatherOrderElementDao;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
@Service("clientWeatherOrderExcelGenerator")
public class ClientWeatherOrderExcelGenerator extends ExcelGeneratorBase {
	
	@Resource
	private ClientWeatherOrderElementDao clientWeatherOrderElementDao;
	
	
	@Override
	protected String fetchMappingKey() {
		return "ClientWeatherOrderExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"weatherorder.xls";
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		String[] clientOrderElementIds=ywId.split("-");
		ClientQuote clientQuote=new ClientQuote();
		clientQuote.setIds(clientOrderElementIds[1]);
		List<ClientOrderElementVo> clientOrderElementVos=clientWeatherOrderElementDao.findOrderElementFinal(clientQuote);
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
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
		
		if (clientOrderElementVos != null && clientOrderElementVos.size() > 0) {
			int dsn=1;
			for (int i = 0; i < clientOrderElementVos.size(); i++) {
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
								value =clientOrderElementVos.get(i).getItem();
							}else if(str.equals("DSN")){
								value =clientOrderElementVos.get(i).getCsn();
							}else if(str.equals("PN")){
								value =clientOrderElementVos.get(i).getQuotePartNumber();
							}else if(str.equals("DESC")){
								value =clientOrderElementVos.get(i).getQuoteDescription();
							}else if(str.equals("UNIT")){
								value =clientOrderElementVos.get(i).getQuoteUnit();
							}else if(str.equals("AMOUNT")){
								value =clientOrderElementVos.get(i).getClientOrderAmount();
							}else if(str.equals("PRICE")){
								value =clientOrderElementVos.get(i).getClientOrderPrice();
							}else if(str.equals("LEATTIME")){
								value =clientOrderElementVos.get(i).getClientOrderLeadTime();
							}else if(str.equals("REMARK")){
								value =clientOrderElementVos.get(i).getClientQuoteRemark();
							}else if(str.equals("FIXEDCOST")){
								value =clientOrderElementVos.get(i).getFixedCost();
							}else if(str.equals("CERT")){
								if(null!=clientOrderElementVos.get(i).getCertificationCode()){
									value =clientOrderElementVos.get(i).getCertificationCode();
								}
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
		}
		
	}
	
	

	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_FOLDER+File.separator+"sampleoutput";
		
	}

	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		
		 return "clientorder"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "client_weather_order";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
