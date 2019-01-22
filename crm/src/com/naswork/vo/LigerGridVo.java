/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ligerGrid数据对象
 * 
 * @since 2016年4月22日 上午11:57:39
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class LigerGridVo implements Serializable {

	private static final long serialVersionUID = -329397908147125038L;

	@JsonProperty("Rows")
	private List<Map<String, Object>> rows;
	
	@JsonProperty("Total")
	private Integer total;

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
