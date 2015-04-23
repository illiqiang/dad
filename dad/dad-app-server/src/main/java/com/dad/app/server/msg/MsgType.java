package com.dad.app.server.msg;

public class MsgType {
	public static final String login = "login";
	public static final String offline = "offline";
	public static final String heartbeat = "heartbeat";
	public static final String rtdWarn = "rtd-warn";

	public static final String unknow = "unknow";
	
	public static Class<?> getMsgClass(String type) {
		Class<?> c = null;
		if(login.equals(type)) {
			c = LoginMsg.class;
		} else if(heartbeat.equals(type)) {
			c = HeartbeatMsg.class;
		} else if(rtdWarn.equals(type)) {
			c = RtdWarnMsg.class;
		} else if(offline.equals(type)) {
			c = OfflineMsg.class;
		}
		return c;
	}
}
