/**
 * 
 */
package com.naswork.vo.tag;

import java.io.Serializable;

/**
 * @since 2016年3月31日 上午11:01:52
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class TextValueVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2017060839034095374L;
	
	private String text;
	private String value;

	public TextValueVo() {
	}
	
	public TextValueVo(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
