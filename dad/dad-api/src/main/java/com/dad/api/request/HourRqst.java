package com.dad.api.request;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class HourRqst extends AbstractRopRequest {
	
	@NotEmpty
	private String deviceId;
	@NotEmpty
	private String dataCode;
	
	@NotEmpty
	@Pattern(regexp = "\\d{8}")
	private String day;

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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
	
}
