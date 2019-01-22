/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;

/**
 * @since 2015-4-20 上午10:04:08
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class UploadFileVo implements Serializable {
	private static final long serialVersionUID = 4110906759448623178L;

	private String id;
	public String name;
	public String path;
	private int imgHeight; // 上传图片的高
	private int imgWidth; // 宽

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	@Override
	public String toString() {
		return "{\"path\":\"" + path + "\", \"imgHeight\":\"" + imgHeight
				+ "\", \"imgWidth\":\"" + imgWidth + "\",\"name\":\""+name+"\",\"id\":\""+id+"\"}";
	}
}
