package com.dad.common.entity;

import java.io.Serializable;


public class QuarterCountData implements Serializable {
	
	private static final long serialVersionUID = 199041241061194060L;

	private String code;

	private String deviceId;

	private String quarter;

	private Double cou;

	private Double min;

	private Double max;

	private Double avg;

	private Double zsMin;

	private Double zsAvg;

	private Double zsMax;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Double getCou() {
		return cou;
	}

	public void setCou(Double cou) {
		this.cou = cou;
	}

	public Double getMin() {
		return min;
	}
	
	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

	public Double getZsMin() {
		return zsMin;
	}

	public void setZsMin(Double zsMin) {
		this.zsMin = zsMin;
	}

	public Double getZsAvg() {
		return zsAvg;
	}

	public void setZsAvg(Double zsAvg) {
		this.zsAvg = zsAvg;
	}

	public Double getZsMax() {
		return zsMax;
	}

	public void setZsMax(Double zsMax) {
		this.zsMax = zsMax;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	
}
