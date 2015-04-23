package com.dad.api.response;

import com.dad.common.entity.PollutantsRtdData;

public class RtdRsp extends BasicResponse {
	
	private PollutantsRtdData rtd;

	public PollutantsRtdData getRtd() {
		return rtd;
	}

	public void setRtd(PollutantsRtdData rtd) {
		this.rtd = rtd;
	}
	
}
