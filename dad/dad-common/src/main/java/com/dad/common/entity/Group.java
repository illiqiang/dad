package com.dad.common.entity;

import java.io.Serializable;

public class Group implements Serializable {

	private static final long serialVersionUID = 6574131025778754603L;
	
	private long groupId;
	
	private String groupName;
	
	private int deviceSize;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getDeviceSize() {
		return deviceSize;
	}

	public void setDeviceSize(int deviceSize) {
		this.deviceSize = deviceSize;
	}
	
	
	
}
