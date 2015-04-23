package com.dad.app.server.msg;

public class LoginOut extends BasicMessage {
	
	private int code;
	
	private long userId;
	
	private String type;

	public LoginOut(long userId) {
		this.userId = userId;
		type = MsgType.login;
	}
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
