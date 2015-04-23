package com.dad.service.dao;

import java.util.Date;
import java.util.List;

import com.dad.common.entity.PollutantsCountData;

public interface MinuteDataDao {

	public void saveMinuteDatas(List<PollutantsCountData> counts, String dataTime);
	
	public List<PollutantsCountData> getMinuteDatas(String deviceId, String dataCode, Date start, Date end, String hourTime);
	
}
