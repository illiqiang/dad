package com.dad.api.request;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class DayRqst extends AbstractRopRequest {
	
	@NotEmpty
	private String deviceId;
	@NotEmpty
	private String dataCode;
	
	@NotEmpty
	@Pattern(regexp = "\\d{6}")
	private String month;

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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
}
