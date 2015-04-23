package com.dad.app.server.msg;

public class UnknowOut extends BasicMessage {
	
	private String type;
	private String msg;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
