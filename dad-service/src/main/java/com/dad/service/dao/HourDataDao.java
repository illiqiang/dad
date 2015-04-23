package com.dad.service.dao;

import java.util.Date;
import java.util.List;

import com.dad.common.entity.PollutantsCountData;

public interface HourDataDao {
	public void saveHourDatas(final List<PollutantsCountData> counts,
			String dataTime);
	
	public List<PollutantsCountData> getHourDatas(String deviceId, String dataCode, Date start, Date end, String dayTime);
	
}
