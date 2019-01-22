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
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.TPartService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;

@Service("lotSupplierUnfinishElementExcelGenerator")
public class LotSupplierUnfinishElementExcelGenerator extends ExcelGeneratorBase{
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private TPartService tPartService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ClientWeatherOrderElementService clientWeatherOrderElementService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	String number = null;


	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"LotImportElement.xlsx";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		PageModel<String> page=new PageModel<String>();
		page.put("ids", ywId);
		List<SupplierQuoteElement> orderElements = supplierQuoteElementService.getByOrderELementId(page);
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		//Object value =null;
		// 客户询价明细
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
		
		if (orderElements != null
				&& orderElements.size() > 0) {
			for (int i = 0; i < orderElements.size(); i++) {
				//lastRowNum = sheet.getLastRowNum();
				Row elementRow = sheet.createRow(lastRowNum + i);
				for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
					Cell cell = lastRow.getCell(cellnum);
					Cell elementCell = elementRow.createCell(cellnum);
					SupplierQuoteElement supplierQuoteElement = orderElements.get(i);
					if (cell != null) {
						//elementCell.setCellStyle(cell.getCellStyle());
						if (!StringUtils.isEmpty(cellKeys[cellnum
								- firstCellNum])) {
							String key = cellKeys[cellnum - firstCellNum];
							//Object value = null;
							if(key.equals("PART_NUMBER")){
								Object value = supplierQuoteElement.getPartNumber();
								this.write(elementCell, value);
							} else if (key.equals("DESCRIPTION")) {
								Object value = supplierQuoteElement.getDescription();
								this.write(elementCell, value);
							} else if (key.equals("SHORT_PART_NUMBER")) {
								Object value = clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber());
								this.write(elementCell, value);
							} else if(key.equals("REMARK")){
								Object value = supplierQuoteElement.getRemark();
								this.write(elementCell, value);
							} else if (key.equals("CAGECODE")) {
								//List<String> cages = tPartService.getCageCodeByShortCode(clientInquiryElementService.getShortPartNumber(manageElementVo.getPartNumber()));
								List<String> cages = tPartService.getCageCodeByShortCode(clientInquiryService.getCodeFromPartNumber(supplierQuoteElement.getPartNumber()));
								if (cages.size() > 0) {
									StringBuffer cage = new StringBuffer();
									for (int j = 0; j < cages.size(); j++) {
										cage.append(cages.get(j)).append(",");
									}
									cage.deleteCharAt(cage.length()-1);
									Object value = cage;
									this.write(elementCell, value);
								}
							}
						}
					}
				}
			}
			/*sheet.addValidationData(getConditionValidation(lastRowNum,
					lastRowNum + inquiryElements.size() - 1));
			sheet.addValidationData(getCertificationValidation(lastRowNum,
					lastRowNum + inquiryElements.size() - 1));*/
		}
	}
	
	private HSSFDataValidation getConditionValidation(int firstRow, int lastRow) {
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,
				lastRow, 7, 7);
		String[] textList = new String[] { "FN", "NE", "NS", "SV", "OH", "AR","Exchange" };
		DVConstraint constraint = DVConstraint
				.createExplicitListConstraint(textList);
		return new HSSFDataValidation(regions, constraint);

	}
	
	private HSSFDataValidation getCertificationValidation(int firstRow,
			int lastRow) {
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,
				lastRow, 8, 8);
		String[] textList = new String[] { "OEM COC", "FAA 8130-3",
				"EASA Form One", "Vendor COC","MFR CERT", "CAAC","FAA+CAAC","EASA+CAAC","TCCA FORM ONR","FAA Dual Release","EASA Dual Release","FAA+EASA","Dual Release","Other" };
		DVConstraint constraint = DVConstraint
				.createExplicitListConstraint(textList);
		return new HSSFDataValidation(regions, constraint);

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
		
		return "LotSupplierUnfinishElement"+"_"+this.fetchUserName()+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "lot_supplier_unfinish_element";
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
		return "LotSupplierUnfinishElementExcel";
	}
	
}
