package com.dad.common.msg;

import java.io.Serializable;
import java.util.List;

public class RtdWarnMsg implements Serializable {

	private static final long serialVersionUID = -5994563384944975442L;
	
	private String deviceId;
	
	private List<RtdWarn> rtdWarns;
	
	private List<Long> userIds;
	
	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<RtdWarn> getRtdWarns() {
		return rtdWarns;
	}

	public void setRtdWarns(List<RtdWarn> rtdWarns) {
		this.rtdWarns = rtdWarns;
	}
	
	
}
