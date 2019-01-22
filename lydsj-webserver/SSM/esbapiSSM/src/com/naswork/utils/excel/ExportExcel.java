/**
 * 
 */
package com.naswork.utils.excel;

import javax.servlet.http.HttpServletResponse;

import com.naswork.vo.excel.ExcelDataVo;

/**
 * @since 2015年9月10日 上午10:44:37
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ExportExcel extends ExportToExcelAbstract {

	private ExcelDataVo excelDataVo;
	private HttpServletResponse response;
	private String fileName;
	
	public ExportExcel(ExcelDataVo excelDataVo, HttpServletResponse response,
			String fileName) {
		this.excelDataVo = excelDataVo;
		this.response = response;
		this.fileName = fileName;
	}

	@Override
	public ExcelDataVo setData() {
		return excelDataVo;
	}

	@Override
	public String setName() {
		return fileName;
	}

	@Override
	public HttpServletResponse setResponse() {
		return response;
	}
}
