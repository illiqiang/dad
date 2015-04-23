package com.dad.common.util;

import java.util.HashMap;
import java.util.Map;

public enum DataType {
	
	RTD("2011"), MINUTE("2051"), HOUR("2061"), DAY("2031"), HEARTBEAT("9021");
	
	private static Map<String, DataType> codeMap = new HashMap<String, DataType>();
	
	static {
		for(DataType d : DataType.values()) {
			codeMap.put(d.getCode(), d);
		}
	}

	private String code;
	
	private DataType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static boolean isRtd(String code) {
		return RTD.getCode().equals(code);
	}
	
	public static DataType getByCode(String code) {
		return codeMap.get(code);
	}
}
