package com.dad.common.msg;

import java.io.Serializable;

public class RtdWarn implements Serializable {

	private static final long serialVersionUID = 8931282302033234381L;
	
	public static final String LESS = "0";
	public static final String OVER = "1";
	
	private String code;
	private String name;
	private Double rtd;
	private Double min;
	private Double max;
	private String type;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getRtd() {
		return rtd;
	}
	public void setRtd(Double rtd) {
		this.rtd = rtd;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
