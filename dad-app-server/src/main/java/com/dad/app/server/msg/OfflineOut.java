package com.dad.app.server.msg;

public class OfflineOut extends BasicMessage {
	private String type;
	private Long userId;
	public OfflineOut() {
		type = MsgType.offline;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
