/**
 * 
 */
package com.naswork.vo.excel;

import java.util.List;
import java.util.Map;

/**
 * @since 2013-12-5 下午1:34:21
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ExcelDataVo {
	private List<ExcelColumnJsonModel> columns;
	private List<Map<String, Object>> datas;
	
	public List<ExcelColumnJsonModel> getColumns() {
		return columns;
	}
	public void setColumns(List<ExcelColumnJsonModel> columns) {
		this.columns = columns;
	}
	public List<Map<String, Object>> getDatas() {
		return datas;
	}
	public void setDatas(List<Map<String, Object>> datas) {
		this.datas = datas;
	}
	
	
}
