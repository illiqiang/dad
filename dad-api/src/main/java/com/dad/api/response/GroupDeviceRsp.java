package com.dad.api.response;

import java.util.List;

public class GroupDeviceRsp extends BasicResponse {
	
	private List<ApiDevice> devices;

	public List<ApiDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<ApiDevice> devices) {
		this.devices = devices;
	}
	
}
