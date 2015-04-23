package com.dad.app.server.msg;

import java.util.List;

import com.dad.common.msg.RtdWarn;

public class RtdWarnOut extends BasicMessage {
	private String type;
	private String deviceId;
	private List<RtdWarn> warns;

	public RtdWarnOut() {
		this.type = MsgType.rtdWarn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<RtdWarn> getWarns() {
		return warns;
	}

	public void setWarns(List<RtdWarn> warns) {
		this.warns = warns;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
