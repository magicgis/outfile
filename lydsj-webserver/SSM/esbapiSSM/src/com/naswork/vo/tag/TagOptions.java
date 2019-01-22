/**
 * 
 */
package com.naswork.vo.tag;

import java.io.Serializable;
import java.util.List;

/**
 * @since 2016年3月31日 上午11:01:03
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class TagOptions implements Serializable {
	private static final long serialVersionUID = -4996011466598507352L;
	
	private String url = "";
	private List<TextValueVo> data;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<TextValueVo> getData() {
		return data;
	}

	public void setData(List<TextValueVo> data) {
		this.data = data;
	}

}
