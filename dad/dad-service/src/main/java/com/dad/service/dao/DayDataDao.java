package com.dad.service.dao;

import java.util.Date;
import java.util.List;

import com.dad.common.entity.PollutantsCountData;

public interface DayDataDao {
	public void saveDayDatas(final List<PollutantsCountData> counts,
			String dataTime);
	public List<PollutantsCountData> getDayDatas(final String deviceId,
			final String dataCode, final Date start, final Date end,
			String monthTime);
}
