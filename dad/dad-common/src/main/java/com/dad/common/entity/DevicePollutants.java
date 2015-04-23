package com.dad.common.entity;

import java.io.Serializable;

public class DevicePollutants implements Serializable {
	
	private static final long serialVersionUID = -4377999859901773496L;
	
	private String deviceId;
	private String dataCode;
	private Double warnMin;
	private Double warnMax;
	
	private String dataName;
	private String dataUnit;

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

	public Double getWarnMin() {
		return warnMin;
	}

	public void setWarnMin(Double warnMin) {
		this.warnMin = warnMin;
	}

	public Double getWarnMax() {
		return warnMax;
	}

	public void setWarnMax(Double warnMax) {
		this.warnMax = warnMax;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}

}
