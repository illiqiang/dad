package com.dad.common.entity;

import java.io.Serializable;

public class Pollutants implements Serializable {

	private static final long serialVersionUID = -8606793407426215959L;
	
	private String dataCode;
	private String dataName;
	private String type;
	private String dataUnit;
	private String couUnit;
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDataUnit() {
		return dataUnit;
	}
	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}
	public String getCouUnit() {
		return couUnit;
	}
	public void setCouUnit(String couUnit) {
		this.couUnit = couUnit;
	}
	
	
}
