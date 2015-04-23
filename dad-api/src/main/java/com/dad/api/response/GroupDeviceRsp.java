package com.dad.api.response;

import java.util.List;

import com.dad.common.entity.Device;

public class GroupDeviceRsp extends BasicResponse {
	
	private List<Device> devices;

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	
}
