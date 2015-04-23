package com.dad.api.request;

import com.rop.AbstractRopRequest;

public class PollutantsRqst extends AbstractRopRequest {

	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
}
