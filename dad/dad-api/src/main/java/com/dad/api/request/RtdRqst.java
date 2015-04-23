package com.dad.api.request;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class RtdRqst extends AbstractRopRequest {
	@NotEmpty
	private String deviceId;
	@NotEmpty
	private String dataCode;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	
}
