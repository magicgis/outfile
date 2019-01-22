/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;

/**
 * 结果
 * 
 * @since 2015年8月20日 上午9:39:50
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class ResultVo implements Serializable {

	private static final long serialVersionUID = 7817835401807226092L;
	private boolean success;
	private String message;
	private Object data;

	public ResultVo(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ResultVo(boolean success, String message,Object data){
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public ResultVo setData(Object data) {
		this.data = data;
		return this;
	}

}
