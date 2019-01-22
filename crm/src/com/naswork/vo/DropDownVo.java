/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;

/**
 * @since 2015年9月18日 下午12:02:51
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class DropDownVo implements Serializable {
	private static final long serialVersionUID = 3138592879145732155L;
	private String text;
	private String id;
	
	public DropDownVo() {
	}
	
	public DropDownVo(String id,String text) {
		super();
		this.text = text;
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
