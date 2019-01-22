/**
 * 
 */
package com.naswork.utils.excel;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naswork.vo.excel.ExcelDataVo;

/**
 * @since 2013-12-5 上午11:37:50
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public abstract class ExportToExcelAbstract {
	
	//日志
	protected static final Logger logger = LoggerFactory.getLogger(ExportToExcelAbstract.class);

	/**
	 * 导出
	 * @since 2013-12-5 上午11:52:59
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void export() {
		ExcelDataVo excelDataVo = setData();
		HttpServletResponse response = setResponse();
		//导出内容为excel文件
		if(excelDataVo!=null && response!=null){
			try {
				XlsUtil.exportToExcel(setName(), excelDataVo, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 设置内容
	 * @param data
	 * @return
	 * @since 2013-12-5 上午11:53:09
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public abstract ExcelDataVo setData();
	
	/**
	 * response
	 * @return
	 * @since 2013-12-5 下午2:00:29
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public abstract HttpServletResponse setResponse();
	
	/**
	 * 文件名
	 * @return
	 * @since 2013-12-5 下午2:02:47
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public abstract String setName();
}
