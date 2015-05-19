package com.dad.api.response;

import java.util.List;

import com.dad.common.entity.QuarterCountData;

public class QuarterDataRsp extends BasicResponse {
	
	private List<QuarterCountData> datas;

	public List<QuarterCountData> getDatas() {
		return datas;
	}

	public void setDatas(List<QuarterCountData> datas) {
		this.datas = datas;
	}
	
}
