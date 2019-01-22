package com.naswork.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface ExportService {

	/**
	 * 导出EXCEL(xlsx格式 )
	 * @param fileName
	 * @param exportModel
	 * @param dataList
	 * @param response
	 * @throws Exception
	 * @since 2014-4-29 上午11:06:14
	 * @author konglinghong
	 * @version v1.0
	 */
	void exportGridToXlsx(String fileName, String exportModel,
			List<Map<String, Object>> dataList, HttpServletResponse response)
			throws Exception;
	
	/**
	 * 导出xls格式的excel
	 * @param fileName
	 * @param exportModel
	 * @param dataList
	 * @param response
	 * @throws Exception
	 * @since 2016年5月5日 上午10:37:33
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void exportGridToXls(String fileName, String exportModel,
			List<Map<String, Object>> dataList, HttpServletResponse response)
					throws Exception;

}
