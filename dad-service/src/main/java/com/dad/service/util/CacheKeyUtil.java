package com.dad.service.util;

public class CacheKeyUtil {
	private static String deviceKey = "c_device+%s";
	private static String deviceUserKey = "c_device_user+%s";
	private static String devicePltKey = "c_device_plt+%s+%s";
	private static String devicePltListKey = "c_plt_list+%s";
	private static String lastRtdListKey = "c_lastrtdlist+%s";
	private static String sessionKey = "c_session+%s";
	private static String userSessionKey = "c_user_session+%s";
	
	private static String pltInfoKey = "c_plt_info+%s";
	
	public static String getPltInfoKey(String code) {
		return String.format(pltInfoKey, code);
	}
	
	public static String getDeviceKey(String deviceId) {
		return String.format(deviceKey, deviceId);
	}
	
	public static String getDeviceUserKey(String deviceId) {
		return String.format(deviceUserKey, deviceId);
	}
	
	public static String getDevicePltKey(String deviceId, String code) {
		return String.format(devicePltKey, deviceId, code);
	}
	
	public static String getDevicePltListKey(String deviceId) {
		return String.format(devicePltListKey, deviceId);
	}
	
	public static String getLastRtdListKey(String deviceId) {
		return String.format(lastRtdListKey, deviceId);
	}
	
	public static String getSessionKey(String session) {
		return String.format(sessionKey, session);
	}
	
	public static String getUserSessionKey(long userId) {
		return String.format(userSessionKey, userId);
	}
}
