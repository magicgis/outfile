package com.naswork.module.marketing.controller.clientinquiry;

import java.util.List;

import com.naswork.model.TPart;

public class MessageVo {

	private Boolean flag;
	
	private String message;
	
	private List<TPart> list;
	
	public MessageVo() {
		super();
	}

	public MessageVo(Boolean flag, String message) {
		super();
		this.flag = flag;
		this.message = message;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<TPart> getList() {
		return list;
	}

	public void setList(List<TPart> list) {
		this.list = list;
	}
	
	
}
