package com.dad.common.service.exception;

public class BusinessServiceException extends Exception {

	private static final long serialVersionUID = -2954478457354396629L;
	
	private String errorCode;
	private Object[] params;
	
	public BusinessServiceException(Throwable cause,String errorCode, Object... params) {
		super(cause);
		this.errorCode = errorCode;
		this.params = params;
	}
	
	public BusinessServiceException(String errorCode, Object... params) {
		this.errorCode = errorCode;
		this.params = params;
	}
	
	public BusinessServiceException(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public Object[] getParams() {
		return params;
	}
	
	public void setParams(Object[] params) {
		this.params = params;
	}
}
