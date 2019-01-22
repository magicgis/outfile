/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;

/**
 * @since 2015年8月19日 上午11:48:18
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class LigerTreeVo implements Serializable {
	
	private static final long serialVersionUID = 6507751430548098350L;
	private String id;
	private String pid;
	private String text;
	private String url;
	private Boolean ischecked = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}
	
}
