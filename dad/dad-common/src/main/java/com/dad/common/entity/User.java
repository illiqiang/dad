package com.dad.common.entity;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -1343513473361194824L;
	private long userId;
	private String userName;
	private String passWord;
	private boolean status;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
