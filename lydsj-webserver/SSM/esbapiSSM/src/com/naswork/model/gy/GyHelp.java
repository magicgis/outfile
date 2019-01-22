package com.naswork.model.gy;

import java.io.Serializable;

/**
 * 
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class GyHelp implements Serializable {
	private static final long serialVersionUID = 5744311192282770151L;

	private String helpUuid;
	
	private String code;
	
	private String title;
	
	private String content;
	
	
	public String getHelpUuid() {
		return helpUuid;
	}

	public void setHelpUuid(String helpUuid) {
		this.helpUuid = helpUuid;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}