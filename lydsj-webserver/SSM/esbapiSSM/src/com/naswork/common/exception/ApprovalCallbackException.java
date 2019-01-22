package com.naswork.common.exception;

public class ApprovalCallbackException extends ExceptionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7056896949331972739L;

	private String callbackErrorMsg;
	
	public ApprovalCallbackException(){
		this.callbackErrorMsg = "";		
	}
	
	public ApprovalCallbackException(String callbackErrorMsg){
		this.callbackErrorMsg = callbackErrorMsg;
	}

	public String getCallbackErrorMsg() {
		return callbackErrorMsg;
	}
	
	
}
