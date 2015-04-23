package com.dad.api.response;

import java.util.List;

import com.dad.common.entity.PollutantsCountData;

public class DataRsp extends BasicResponse {
	
	private List<PollutantsCountData> datas;

	public List<PollutantsCountData> getDatas() {
		return datas;
	}

	public void setDatas(List<PollutantsCountData> datas) {
		this.datas = datas;
	}
	
}
