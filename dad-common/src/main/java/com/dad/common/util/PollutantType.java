package com.dad.common.util;

public enum PollutantType {
	FS("B01","污水"),FQ("B02","废气"),ZS("B03","噪声");
	private String code;
	private String name;
	
	PollutantType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	
}
