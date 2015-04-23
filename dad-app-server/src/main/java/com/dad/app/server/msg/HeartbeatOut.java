package com.dad.app.server.msg;

public class HeartbeatOut extends BasicMessage {
	
	private String type;
	

	public HeartbeatOut(Long msgId) {
		super.setMsgId(msgId);
		type = MsgType.heartbeat;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
