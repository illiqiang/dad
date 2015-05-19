package com.dad.common.service;

import java.util.List;

import com.dad.common.entity.MonthCountData;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.entity.PollutantsRtdData;
import com.dad.common.entity.QuarterCountData;

public interface DadDataService {
	
	public void sendRtdDatas(List<PollutantsRtdData> rtds, String deviceId) throws Exception;
	
	public void saveMinuteDatas(List<PollutantsCountData> counts, String dataTime) throws Exception;
	
	public void saveHourDatas(List<PollutantsCountData> counts, String dataTime) throws Exception;
	
	public void saveDayDatas(List<PollutantsCountData> counts, String dataTime) throws Exception;
	
	public PollutantsRtdData getRtdData(String deviceId, String dataCode) throws Exception;
	
	public List<PollutantsCountData> getMinuteDatas(String deviceId, String dataCode, String hourTime)throws Exception;
	
	public List<PollutantsCountData> getMinuteDatasByDay(String deviceId, String dataCode, String dayTime)throws Exception;
	
	public List<PollutantsCountData> getHourDatas(String deviceId, String dataCode, String dayTime)throws Exception;
	
	public List<PollutantsCountData> getDayDatas(String deviceId, String dataCode, String mouthTime)throws Exception;
	
	public List<MonthCountData> getMonthDatas(String deviceId, String dataCode, String year) throws Exception ;
	
	public List<QuarterCountData> getQuarterDatas(String deviceId, String dataCode, String year) throws Exception ;
}
