package com.dad.common.util;

public enum StType {
	
	st31("31", "烟囱"),st32("32","出水口");
	private String code;
	private String name;
	
	StType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	public static String getNameByCode(String code) {
		String a = "未知";
		for(StType s:StType.values()) {
			if(s.code.equals(code)) {
				a=s.getName();
				break;
			}
		}
		return a;
	} 
}
