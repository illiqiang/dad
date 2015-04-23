package com.dad.api.response;

public class LoginRsp extends BasicResponse {
	
	private long userId;
	
	private String sessionId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
