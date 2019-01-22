package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.ImportPackageElement;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.service.ImportpackageElementService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;

@Service("certificationExcelGenerator")
public class CertificationExcelGenerator extends ExcelGeneratorBase {

	
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Override
	protected String fetchMappingKey() {
		return "CertificationExcel";
	}

	@Override
	protected String fetchTemplateFileName(String currencyId) {
		if (currencyId.equals("934")) {
			return File.separator+"exceltemplate"+File.separator+"CertificationFor934.xls";
		}else {
			return File.separator+"exceltemplate"+File.separator+"Certification.xls";
		}
	}

	@Override
	protected void writeData(POIExcelWorkBook wb, String ywId) {
		String[] ywIds = ywId.split("-");
		if (ywIds.length == 1) {
			ywId = ywId+"-"+ywId;
		}
		String[] ids=ywId.split("-")[1].split(",");
		ImportPackageElement importPackageElement=new ImportPackageElement();
		ImportPackageElementVo ipelist1=importpackageElementService.findimportpackageelement(ids[0]);
		if (ipelist1.getClientCode().startsWith("3")) {
			ipelist1.setDescription(ipelist1.getOrderDescription());
		}
		ImportPackageElementVo ipelist2 = null;
		if(ids.length==2||ids.length>2){
			ipelist2=importpackageElementService.findimportpackageelement(ids[1]);
			if (ipelist2.getClientCode().startsWith("3")) {
				ipelist2.setDescription(ipelist2.getOrderDescription());
			}
		}
		ImportPackageElementVo ipelist3 = null;
		if(ids.length==3||ids.length>3){
			ipelist3=importpackageElementService.findimportpackageelement(ids[2]);
			if (ipelist3.getClientCode().startsWith("3")) {
				ipelist3.setDescription(ipelist3.getOrderDescription());
			}
		}
		ImportPackageElementVo ipelist4=null;
		if(ids.length==4||ids.length>4){
			ipelist4=importpackageElementService.findimportpackageelement(ids[3]);
			if (ipelist4.getClientCode().startsWith("3")) {
				ipelist4.setDescription(ipelist4.getOrderDescription());
			}
		}
		
		for (String id : ids) {
			importPackageElement.setId(Integer.parseInt(id));
			importPackageElement.setCertificationStatusId(1);
			importpackageElementService.updateByPrimaryKeySelective(importPackageElement);
		}
		
//		Date certificationDate = (Date) ipelist1.getCertificationDate();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(certificationDate);
		String on="";
		Sheet sheet=wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {
				Cell cell=row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {
						String key = str.substring(1);
						Object value = null;
						
						if(str.endsWith("1")){
						if(key.equals("SOURCE_ORDER_NUMBER1")){
							value =ipelist1.getSourceOrderNumber();
						}
						else if(key.equals("DESCRIPTION1")){
							value =ipelist1.getDescription();
						}
						else if(key.equals("PART_NUMBER1")){
							value =ipelist1.getPartNumber();
						}
						else if(key.equals("SERIAL_NUMBER1")){
							value =ipelist1.getSerialNumber();
						}
						else if(key.equals("SHELF_LIFE1") && !"934".equals(ipelist1.getClientCode())){
							if (ipelist1.getShelfLife() != null) {
								Integer shelfLife = ipelist1.getShelfLife()/30;
								String life = "";
								if (shelfLife >= 12) {
									shelfLife = shelfLife/12;
									life = shelfLife+"YEARS";
								}else {
									life = shelfLife+"MONTHS";
								}
								value =life;
							}
						}
						else if(key.equals("AMOUNT1")){
							value =ipelist1.getAmount().intValue();
							String amount=ipelist1.getAmount().toString();
							String last=amount.substring(amount.length()-1);
							if(!last.equals("0")){
								value =ipelist1.getAmount();
							}
						}
						else if(key.equals("UNIT1")){
							value =ipelist1.getUnit();
						}
						String manDate="";	
						SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
						if(null!=ipelist1.getInspectionDate()){
							manDate=simpleDateFormat.format(ipelist1.getInspectionDate()).toString();
						}
						else	if(null!=ipelist1.getManufactureDate()){
							manDate=simpleDateFormat.format(ipelist1.getManufactureDate()).toString();
						} 
						if(!"".equals(manDate)){
						
							String[] Dates=manDate.split("-");
							if(key.equals("YEAR1")){
								value =Dates[0];
							}else if(key.equals("MONTH1")){
								value =Dates[1];
							}else if(key.equals("DAY1")){
								value = Dates[2];
							}else if(key.equals("年1")){
								value = "年";
							}else if(key.equals("月1")){
								value = "月";
							}else if(key.equals("日1")){
								value = "日";
							}else if(key.equals("Date1")){
								value = "Date";
							}
						}else{
							if(key.equals("YEAR1")){
								value ="";
							}else if(key.equals("MONTH1")){
								value ="";
							}else if(key.equals("DAY1")){
								value = "";
							}else if(key.equals("年")){
								value = "";
							}else if(key.equals("月")){
								value = "";
							}else if(key.equals("日")){
								value = "";
							}else if(key.equals("Date1")){
								value = "";
							}
						}
						}
						else if(str.endsWith("2")){
							if(ids.length==1){
								ipelist2=ipelist1;
								on="2";
							}
							if(key.equals("SOURCE_ORDER_NUMBER2")){
								value =ipelist2.getSourceOrderNumber();
							}
							else if(key.equals("DESCRIPTION2")){
								value =ipelist2.getDescription();
							}
							else if(key.equals("PART_NUMBER2")){
								value =ipelist2.getPartNumber();
							}
							else if(key.equals("SERIAL_NUMBER2")){
								value =ipelist2.getSerialNumber();
							}
							else if(key.equals("SHELF_LIFE2") && !"934".equals(ipelist2.getClientCode())){
								if (ipelist2.getShelfLife() != null) {
									Integer shelfLife = ipelist2.getShelfLife()/30;
									String life = "";
									if (shelfLife >= 12) {
										shelfLife = shelfLife/12;
										life = shelfLife+"YEARS";
									}else {
										life = shelfLife+"MONTHS";
									}
									value =life;
								}
							}
							else if(key.equals("AMOUNT2")){
								value =ipelist2.getAmount().intValue();
								String amount=ipelist2.getAmount().toString();
								String last=amount.substring(amount.length()-1);
								if(!last.equals("0")){
									value =ipelist2.getAmount();
								}
							}
							else if(key.equals("UNIT2")){
								value =ipelist2.getUnit();
							}
							String manDate="";
							SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
							 if(null!=ipelist2.getInspectionDate()){
									manDate=simpleDateFormat.format(ipelist2.getInspectionDate()).toString();
								}
							 else if(null!=ipelist2.getManufactureDate()){
								manDate=simpleDateFormat.format(ipelist2.getManufactureDate()).toString();
							}
							if(!"".equals(manDate)){
								String[] Dates=manDate.split("-");
								if(key.equals("YEAR2")){
									value =Dates[0];
								}else if(key.equals("MONTH2")){
									value =Dates[1];
								}else if(key.equals("DAY2")){
									value = Dates[2];
								}else if(key.equals("年2")){
									value = "年";
								}else if(key.equals("月2")){
									value = "月";
								}else if(key.equals("日2")){
									value = "日";
								}else if(key.equals("Date2")){
									value = "Date";
								}
							}else{
								if(key.equals("YEAR2")){
									value ="";
								}else if(key.equals("MONTH2")){
									value ="";
								}else if(key.equals("DAY2")){
									value = "";
								}else if(key.equals("年")){
									value = "";
								}else if(key.equals("月")){
									value = "";
								}else if(key.equals("日")){
									value = "";
								}else if(key.equals("Date2")){
									value = "";
								}
							}
							}
						
						else if(str.endsWith("3")){
							if(ids.length==2||ids.length==1){
								ipelist3=ipelist2;
								on="3";
							}
							if(key.equals("SOURCE_ORDER_NUMBER3")){
								value =ipelist3.getSourceOrderNumber();
							}
							else if(key.equals("DESCRIPTION3")){
								value =ipelist3.getDescription();
							}
							else if(key.equals("PART_NUMBER3")){
								value =ipelist3.getPartNumber();
							}
							else if(key.equals("SERIAL_NUMBER3")){
								value =ipelist3.getSerialNumber();
							}
							else if(key.equals("SHELF_LIFE3") && !"934".equals(ipelist3.getClientCode())){
								if (ipelist3.getShelfLife() != null) {
									Integer shelfLife = ipelist3.getShelfLife()/30;
									String life = "";
									if (shelfLife >= 12) {
										shelfLife = shelfLife/12;
										life = shelfLife+"YEARS";
									}else {
										life = shelfLife+"MONTHS";
									}
									value =life;
								}
							}
							else if(key.equals("AMOUNT3")){
								value =ipelist3.getAmount().intValue();
								String amount=ipelist3.getAmount().toString();
								String last=amount.substring(amount.length()-1);
								if(!last.equals("0")){
									value =ipelist3.getAmount();
								}
							}
							else if(key.equals("UNIT3")){
								value =ipelist3.getUnit();
							}
							String manDate="";
							SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
							if(null!=ipelist3.getInspectionDate()){
								manDate=simpleDateFormat.format(ipelist3.getInspectionDate()).toString();
							}
							else 	if(null!=ipelist3.getManufactureDate()){
								manDate=simpleDateFormat.format(ipelist3.getManufactureDate()).toString();
							}
							if(!"".equals(manDate)){
								String[] Dates=manDate.split("-");
								if(key.equals("YEAR3")){
									value =Dates[0];
								}else if(key.equals("MONTH3")){
									value =Dates[1];
								}else if(key.equals("DAY3")){
									value = Dates[2];
								}else if(key.equals("年3")){
									value = "年";
								}else if(key.equals("月3")){
									value = "月";
								}else if(key.equals("日3")){
									value = "日";
								}else if(key.equals("Date3")){
									value = "Date";
								}
							}else{
								if(key.equals("YEAR3")){
									value ="";
								}else if(key.equals("MONTH3")){
									value ="";
								}else if(key.equals("DAY3")){
									value = "";
								}else if(key.equals("年")){
									value = "";
								}else if(key.equals("月")){
									value = "";
								}else if(key.equals("日")){
									value = "";
								}else if(key.equals("Date3")){
									value = "";
								}
							}
							}
						
						else if(str.endsWith("4")){
							if(ids.length==3||ids.length==1||ids.length==2){
								ipelist4=ipelist3;
								on="4";
							}
							if(key.equals("SOURCE_ORDER_NUMBER4")){
								value =ipelist4.getSourceOrderNumber();
							}
							else if(key.equals("DESCRIPTION4")){
								value =ipelist4.getDescription();
							}
							else if(key.equals("PART_NUMBER4")){
								value =ipelist4.getPartNumber();
							}
							else if(key.equals("SERIAL_NUMBER4")){
								value =ipelist4.getSerialNumber();
							}
							else if(key.equals("SHELF_LIFE4") && !"934".equals(ipelist4.getClientCode())){
								if (ipelist4.getShelfLife() != null) {
									Integer shelfLife = ipelist4.getShelfLife()/30;
									String life = "";
									if (shelfLife >= 12) {
										shelfLife = shelfLife/12;
										life = shelfLife+"YEARS";
									}else {
										life = shelfLife+"MONTHS";
									}
									value =life;
								}
							}
							else if(key.equals("AMOUNT4")){
								value =ipelist4.getAmount().intValue();
								String amount=ipelist4.getAmount().toString();
								String last=amount.substring(amount.length()-1);
								if(!last.equals("0")){
									value =ipelist4.getAmount();
								}
							}
							else if(key.equals("UNIT4")){
								value =ipelist4.getUnit();
							}
							String manDate="";
							SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
							if(null!=ipelist4.getInspectionDate()){
								manDate=simpleDateFormat.format(ipelist4.getInspectionDate()).toString();
							}
							else  if(null!=ipelist4.getManufactureDate()){
								manDate=simpleDateFormat.format(ipelist4.getManufactureDate()).toString();
							}
							if(!"".equals(manDate)){
								String[] Dates=manDate.split("-");
								if(key.equals("YEAR4")){
									value =Dates[0];
								}else if(key.equals("MONTH4")){
									value =Dates[1];
								}else if(key.equals("DAY4")){
									value = Dates[2];
								}else if(key.equals("年4")){
									value = "年";
								}else if(key.equals("月4")){
									value = "月";
								}else if(key.equals("日4")){
									value = "日";
								}else if(key.equals("Date4")){
									value = "Date";
								}
							}else{
								if(key.equals("YEAR4")){
									value ="";
								}else if(key.equals("MONTH4")){
									value ="";
								}else if(key.equals("DAY4")){
									value = "";
								}else if(key.equals("年")){
									value = "";
								}else if(key.equals("月")){
									value = "";
								}else if(key.equals("日")){
									value = "";
								}else if(key.equals("Date4")){
									value = "";
								}
							}
							}
						
						
						if (value == null) {
							this.write(cell, "");
						} else {
							this.write(cell, value);
						}
					}
				}
			}
		}
//		if(on.equals("2")){
//			partNumber=ipelist1.getPartNumber();
//		}else if(on.equals("3")){
//			partNumber=ipelist1.getPartNumber()+", "+ipelist2.getPartNumber();
//		}else if(on.equals("4")){
//			partNumber=ipelist1.getPartNumber()+", "+ipelist2.getPartNumber()+", "+ipelist3.getPartNumber();
//		}else{
//			partNumber=ipelist1.getPartNumber()+", "+ipelist2.getPartNumber()+", "+ipelist3.getPartNumber()+", "+ipelist4.getPartNumber();
//		}
		
		
	}

	@Override
	protected String fetchOutputFilePath() {
		return FileConstant.UPLOAD_REALPATH+File.separator+FileConstant.EXCEL_FOLDER+File.separator+"sampleoutput";
		
	}

	@Override
	protected String fetchOutputFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
//		partNumber=	partNumber.replace("/", "╱");
//		partNumber=	partNumber.replace("\\", "＼");
		return "Certification"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	@Override
	protected String fetchYwTableName() {
		return "import_package_element";
	}

	@Override
	protected String fetchYwTablePkName() {
		return "id";
	}

}
