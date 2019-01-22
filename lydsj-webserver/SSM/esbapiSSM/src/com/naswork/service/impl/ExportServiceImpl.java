package com.naswork.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.naswork.service.ExportService;
import com.naswork.utils.excel.ExportExcel;
import com.naswork.utils.excel.XlsUtil;
import com.naswork.vo.excel.ExcelDataVo;
import com.naswork.vo.excel.JqGridExportModel;
@Service("exportService")
public class ExportServiceImpl implements ExportService {

	
	@Override
	public void exportGridToXlsx(String fileName,String exportModel,List<Map<String,Object>> dataList,HttpServletResponse response)throws Exception{
		if(StringUtils.isEmpty(exportModel))throw new IllegalArgumentException("exportModel不能为空!");
		JSONArray json = JSONArray.fromObject(exportModel);
		List<JqGridExportModel> modelList = (List<JqGridExportModel>)JSONArray.toList(json, JqGridExportModel.class);
		
		ExcelDataVo excelDataVo =  XlsUtil.createSimpleExcelDataVo(dataList,modelList);
		ExportExcel export;
		try {
			fileName=fileName+".xlsx";
			export = new ExportExcel(excelDataVo,response,fileName);
			export.export();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void exportGridToXls(String fileName,String exportModel,List<Map<String,Object>> dataList,HttpServletResponse response)throws Exception{
		if(StringUtils.isEmpty(exportModel))throw new IllegalArgumentException("exportModel不能为空!");
		JSONArray json = JSONArray.fromObject(exportModel);
		List<JqGridExportModel> modelList = (List<JqGridExportModel>)JSONArray.toList(json, JqGridExportModel.class);
		
		ExcelDataVo excelDataVo =  XlsUtil.createSimpleExcelDataVo(dataList,modelList);
		ExportExcel export;
		try {
			fileName=fileName+".xls";
			export = new ExportExcel(excelDataVo,response,fileName);
			export.export();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
