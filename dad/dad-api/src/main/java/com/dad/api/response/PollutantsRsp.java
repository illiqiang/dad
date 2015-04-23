package com.dad.api.response;

import java.util.List;

import com.dad.common.entity.DevicePollutants;

public class PollutantsRsp extends BasicResponse {
	
	private List<DevicePollutants> pollutants;

	public List<DevicePollutants> getPollutants() {
		return pollutants;
	}

	public void setPollutants(List<DevicePollutants> pollutants) {
		this.pollutants = pollutants;
	}
	
}
