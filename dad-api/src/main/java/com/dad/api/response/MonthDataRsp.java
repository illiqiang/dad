package com.dad.api.response;

import java.util.List;

import com.dad.common.entity.MonthCountData;

public class MonthDataRsp extends BasicResponse {
	
	private List<MonthCountData> datas;

	public List<MonthCountData> getDatas() {
		return datas;
	}

	public void setDatas(List<MonthCountData> datas) {
		this.datas = datas;
	}
	
}
