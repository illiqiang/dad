package primefaces.common;

import java.util.HashMap;
import java.util.Map;


public class Constants {
	public static final String sessionKey = "username";
	public static final String loginView = "/login.do";
	public final static String SUCCESS = "success";
	
	public static Map<String, String> rtdDataFlags = new HashMap<>();
	
	static {
		rtdDataFlags.put("P", "电源故障");
		rtdDataFlags.put("F", "排放源停运");
		rtdDataFlags.put("C", "校验");
		rtdDataFlags.put("M", "维护");
		rtdDataFlags.put("T", "超测上限");
		rtdDataFlags.put("D", "故障");
		rtdDataFlags.put("S", "设定值");
		rtdDataFlags.put("N", "正常");
		rtdDataFlags.put("0", "校准数据");
		rtdDataFlags.put("1", "气象参数");
		rtdDataFlags.put("2", "异常数据");
		rtdDataFlags.put("3", "正常数据");
	}
}
