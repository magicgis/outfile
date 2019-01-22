package com.naswork.utils.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.naswork.vo.excel.ExcelColumnJsonModel;
import com.naswork.vo.excel.ExcelDataVo;
import com.naswork.vo.excel.JqGridExportModel;
import com.naswork.vo.excel.ReportGridDataModel;
import com.naswork.vo.excel.ReportGridHeaderModel;
import com.naswork.vo.excel.ReportGridModel;
import com.naswork.vo.excel.ReportTitleModel;

/**
 * @ClassName: XlsUtil
 * @Description: 导入excel工具类
 * @author chenguojun
 * @date 2012-10-20 下午2:30:51
 */
public class XlsUtil {
	
	/**
	* @Title: readXls
	* @Description: 从xls中读取数据放入list,每个map包含一行数据
	* @param @param file
	* @param @return
	* @param @throws FileNotFoundException
	* @param @throws BiffException
	* @param @throws IOException
	* @return List<Map<String,String>>
	* @throws
	 */
	public static List<Map<String, String>> readXls(File file) throws FileNotFoundException, BiffException, IOException {
		FileReader reader = new FileReader();
		return reader.readXls(file);
	}
	
	/**
	* @Title: readXls
	* @Description: 从xls中读取数据放入list,每个map包含一行数据
	* @param @param file
	* @param @return
	* @param @throws FileNotFoundException
	* @param @throws BiffException
	* @param @throws IOException
	* @return List<Map<String,String>>
	* @throws
	 */
	public static List<Map<String, String>> readXls(InputStream file) throws FileNotFoundException, BiffException, IOException {
		FileReader reader = new FileReader();
		return reader.readXls(file);
	}
	
	/**
	 * 导出excel
	 * @param fileName
	 * @param excelDateVo
	 * @throws Exception
	 * @since 2013-12-5 下午1:42:50
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static void exportToExcel(String fileName ,ExcelDataVo excelDateVo, HttpServletResponse response) throws Exception {
		ExcelBuilder excelBuilder = new ExcelBuilder();
		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
		Sheet sheet = excelBuilder.createSheet(workbook, "Sheet1");
		int startRowNum = 0;
		exportToExcel(fileName, excelDateVo, response, workbook, sheet, startRowNum, true);
	}
	
	/**
	 * 导出excel
	 * @param fileName
	 * @param excelDateVo
	 * @param startRowNum 开始插入数据的行号
	 * @param isAutoWrite 是否自动输出
	 * @throws Exception
	 * @since 2013-12-5 下午1:42:50
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static void exportToExcel(String fileName ,ExcelDataVo excelDateVo, HttpServletResponse response, Workbook workbook, Sheet sheet, int startRowNum, boolean isAutoWrite) throws Exception {
		List<ExcelColumnJsonModel> columns = excelDateVo.getColumns();
		List<Map<String, Object>> datas = excelDateVo.getDatas();
		
		List<ReportGridModel> reportGridModels = new ArrayList<ReportGridModel>();
		ReportGridModel reportGridModel = new ReportGridModel();
		
		List<ReportGridHeaderModel> headers = new ArrayList<ReportGridHeaderModel>();
		ReportGridHeaderModel header = null;
		for (ExcelColumnJsonModel excelColumnJsonModel : columns) {
			header = new ReportGridHeaderModel();
			if(excelColumnJsonModel.isHasParent()){
				//在现有的headers中找到是否已经存在这个父
				String parentname = excelColumnJsonModel.getParent();
				if(hasParentElem(parentname,headers)){
					for (int i=0;i<headers.size();i++) {
						ReportGridHeaderModel reportGridHeaderModel = headers.get(i);
						if(reportGridHeaderModel.getColumnName().equals(parentname)){
							//更新
							List<ReportGridHeaderModel> subheaders = reportGridHeaderModel.getHeaders();
							ReportGridHeaderModel subheader = new ReportGridHeaderModel();
							subheader.setLevel(2);
							subheader.setAlign(1);
							subheader.setLabel(excelColumnJsonModel.getCaption());
							subheader.setColumnName(excelColumnJsonModel.getParent()+excelColumnJsonModel.getCaption());
							subheader.setFontSize(8);
							subheader.setWidth(80);
							subheaders.add(subheader);
							subheader.setParent(reportGridHeaderModel);//设置关联
							reportGridHeaderModel.setHeaders(subheaders);//继续加入子
							break;
						}
					}
				}else{
					//不存在就把父加入header中
					header.setLevel(1);
					header.setAlign(1);
					header.setLabel(parentname);
					header.setColumnName(parentname);
					header.setFontSize(8);
					List<ReportGridHeaderModel> subheaders = new ArrayList<ReportGridHeaderModel>();
					ReportGridHeaderModel subheader = new ReportGridHeaderModel();
					subheader.setLevel(2);
					subheader.setAlign(1);
					subheader.setLabel(excelColumnJsonModel.getCaption());
					subheader.setColumnName(excelColumnJsonModel.getParent()+excelColumnJsonModel.getCaption());
					subheader.setFontSize(8);
					subheader.setWidth(80);
					subheaders.add(subheader);
					header.setHeaders(subheaders);//加入子
					subheader.setParent(header);//设置关联
					headers.add(header);//加入父
				}
			}else{
				header.setLevel(1);
				header.setAlign(1);//表头水平对齐方式
				header.setDataAlign(excelColumnJsonModel.getAlign());//数据水平对齐方式
				header.setLabel(excelColumnJsonModel.getCaption());//标题
				header.setColumnName(excelColumnJsonModel.getProperty()==null?excelColumnJsonModel.getCaption():excelColumnJsonModel.getProperty());//属性,添加非空判断,是为了兼容旧数据
				header.setFontSize(8);
				header.setWidth(excelColumnJsonModel.getWidth()<=0?80:excelColumnJsonModel.getWidth());
				headers.add(header);
			}
		}
		
		
		ReportGridDataModel gridDataModel = new ReportGridDataModel();
		gridDataModel.setDatas(datas);
		//数据
		reportGridModel.setGridDataModel(gridDataModel);
		//表头
		reportGridModel.setGridHeaderModelList(headers);
		reportGridModels.add(reportGridModel);
		
		//创建excel
		ExcelBuilder excelBuilder = new ExcelBuilder();
//		Workbook workbook = excelBuilder.createWorkBook(fileName) ;
//		Sheet sheet = excelBuilder.createSheet(workbook, "Sheet1");
		if(fileName.equals("供应商询价单统计.xls")){
			sheet.createFreezePane( 1, 1, 1, 1 );
		}else{
			sheet.createFreezePane( 0, 1, 0, 1 );
		}
		
	
		int nextRowNo = excelBuilder.addExcelModelTitleToSheet(new ReportTitleModel(), workbook, sheet, 1);
		nextRowNo = excelBuilder.addComplexGridToSheet(reportGridModels, workbook, sheet, nextRowNo + startRowNum);
		
		if (isAutoWrite) {
			try {
				response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1"));
				if(workbook instanceof XSSFWorkbook)
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				else
					response.setContentType("application/vnd.ms-excel");
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 把简单的表头从map格式转成规定的格式
	 * @param columns
	 * @return
	 * @since 2013-12-5 下午2:05:34
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static List<ExcelColumnJsonModel> createSimpleColumn(Map<String, String> columns){
		List<ExcelColumnJsonModel> list = new ArrayList<ExcelColumnJsonModel>();
		for (Map.Entry<String,String> entry: columns.entrySet()) {
			ExcelColumnJsonModel excelColumnJsonModel = new ExcelColumnJsonModel();
			excelColumnJsonModel.setCaption(entry.getValue());
			excelColumnJsonModel.setHasParent(false);
			excelColumnJsonModel.setProperty(entry.getKey());
			list.add(excelColumnJsonModel);
		}
		return list;
	}
	
	private static boolean hasParentElem(String parentname,List<ReportGridHeaderModel> headers) {
		boolean flag = false;
		for (ReportGridHeaderModel reportGridHeaderModel : headers) {
			if(reportGridHeaderModel.getLabel().equals(parentname)){
				flag =true;
				break;
			}
		}
		return flag;
	}

	public static ExcelDataVo createSimpleExcelDataVo(
			List<Map<String, Object>> datalist) {
		ExcelDataVo excelDataVo = new ExcelDataVo();
		if(datalist!=null && datalist.size()>0){
			List<ExcelColumnJsonModel> list = new ArrayList<ExcelColumnJsonModel>();
			//取出表头
			Map<String, Object> columns = datalist.get(0);
			for (Map.Entry<String,Object> entry: columns.entrySet()) {
				ExcelColumnJsonModel excelColumnJsonModel = new ExcelColumnJsonModel();
				excelColumnJsonModel.setCaption((String) entry.getKey());
				excelColumnJsonModel.setHasParent(false);
				excelColumnJsonModel.setProperty(null);
				list.add(excelColumnJsonModel);
			}
			excelDataVo.setColumns(list);
			
			//数据
			excelDataVo.setDatas(datalist);
		}
		return excelDataVo;
	}
	
	public static ExcelDataVo createSimpleExcelDataVo(
			List<Map<String, Object>> datalist,List<JqGridExportModel> header) {
		ExcelDataVo excelDataVo = new ExcelDataVo();
		if(datalist!=null && datalist.size()>0){
			List<ExcelColumnJsonModel> list = new ArrayList<ExcelColumnJsonModel>();
			//取出表头
			for (JqGridExportModel model: header) {
				ExcelColumnJsonModel excelColumnJsonModel = new ExcelColumnJsonModel();
				if(model.getName().equals("")){
					continue;
				}
				excelColumnJsonModel.setCaption(model.getName());
				excelColumnJsonModel.setHasParent(false);
				excelColumnJsonModel.setProperty(model.getProperty());//属性名,用于取数据的KEY
				excelColumnJsonModel.setAlign(model.getAlign());
				excelColumnJsonModel.setWidth(model.getWidth());
				list.add(excelColumnJsonModel);
			}
			excelDataVo.setColumns(list);
			
			//数据
			excelDataVo.setDatas(datalist);
		}
		return excelDataVo;
	}
}
