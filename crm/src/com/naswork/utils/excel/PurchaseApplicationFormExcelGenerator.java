package com.naswork.utils.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.ClientOrder;
import com.naswork.model.PurchaseApplicationForm;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.service.ClientOrderService;
import com.naswork.service.PurchaseApplicationFormService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.UserService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("purchaseApplicationFormExcelGenerator")
public class PurchaseApplicationFormExcelGenerator extends ExcelGeneratorBase{
	@Resource
	private PurchaseApplicationFormService purchaseApplicationFormService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private UserService userService;


	
	/**
	 *  获取Excel template所在路径，一般来说放在WebRoot/exceltemplate/路径下面
	 */
	@Override
	protected String fetchTemplateFileName(String currencyId) {
		return File.separator+"exceltemplate"+File.separator+"PurchaseApplicationFormMarket.xls";
	}

	/**
	 * 根据传入的sheet来将fetchData获得的数据写入到表格
	 */
	@Override
	protected void writeData(POIExcelWorkBook wb,String ywId) {
		PurchaseApplicationForm purchaseApplicationForm = purchaseApplicationFormService.findByClientOrderId(new Integer(ywId));
		ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(purchaseApplicationForm.getClientOrderId());
		Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
		sheet.setForceFormulaRecalculation(true);
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		//Object value =null;
		for (int rowIndex = firstRowNum; rowIndex < lastRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			for (int cellnum = row.getFirstCellNum(); cellnum <= row
					.getLastCellNum(); cellnum++) {
				Cell cell = row.getCell(cellnum);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String str = cell.getStringCellValue();
					if (str == null) {
						continue;
					}
					if (str.startsWith("$")) {
						String key = str.substring(1);
						Object value = null;
						if (key.equals("SOURCE_NUMBER")) {
							value = clientOrder.getSourceNumber();
							this.write(cell, value);
						} else if(key.equals("APPLICATION_NUMBER")){
							value = purchaseApplicationForm.getApplicationNumber();
							this.write(cell, value);
						} else if (key.equals("APPLICATION_DATE")) {
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							value = df.format(purchaseApplicationForm.getApplicationDate());
							this.write(cell, value);
						} else if(key.equals("ORDER_DATE")){
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
							value = df.format(clientOrder.getOrderDate());
							this.write(cell, value);
						} else if(key.equals("APPLICATION_USER_NAME")){
							UserVo userVo = userService.findById(purchaseApplicationForm.getUserId());
							value = userVo.getUserName();
							this.write(cell, value);
						} else if(key.equals("ORDER_NUMBER")){
							value = clientOrder.getOrderNumber();
							this.write(cell, value);
						}
					}
				}
			}
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
		
		return "PurchaseApplicationForm"+"_"+format.format(now);
	}

	/**
	 * 获取所关联的表的表名
	 */
	@Override
	protected String fetchYwTableName() {
		return "purchase_application_form";
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
		return "PurchaseApplicationFormExcel";
	}
	
}
