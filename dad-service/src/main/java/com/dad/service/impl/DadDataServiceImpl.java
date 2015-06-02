package com.dad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.MonthCountData;
import com.dad.common.entity.Pollutants;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.entity.PollutantsRtdData;
import com.dad.common.entity.QuarterCountData;
import com.dad.common.msg.RtdWarn;
import com.dad.common.msg.RtdWarnMsg;
import com.dad.common.service.CacheService;
import com.dad.common.service.DadDataService;
import com.dad.common.service.DadService;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.service.exception.ErrorConstant;
import com.dad.common.util.DateUtil;
import com.dad.service.dao.DayDataDao;
import com.dad.service.dao.HourDataDao;
import com.dad.service.dao.MinuteDataDao;
import com.dad.service.msg.RtdWarnMsgSender;
import com.dad.service.util.CacheKeyUtil;

public class DadDataServiceImpl implements DadDataService {

	private static Logger log = LoggerFactory
			.getLogger(DadDataServiceImpl.class);

	private MinuteDataDao minuteDataDao;

	private HourDataDao hourDataDao;

	private DayDataDao dayDataDao;

	private DadService dadService;
	
	private CacheService dadDataCacheService;

	private RtdWarnMsgSender rtdWarnMsgSender;

	@Override
	public void sendRtdDatas(List<PollutantsRtdData> rtds, String deviceId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("sendRtdDatas({},{})", rtds.size(), deviceId);
		}
		RtdWarnMsg msg = new RtdWarnMsg();
		List<RtdWarn> warns = new ArrayList<>();
		List<Long> userIds = dadService.getDeviceUserIds(deviceId);
		//实时数据入缓存
		dadDataCacheService.set(CacheKeyUtil.getLastRtdListKey(deviceId), rtds);
		for (PollutantsRtdData rtd : rtds) {	
			if(CollectionUtils.isEmpty(userIds)) {
				break;
			}
			Double value = rtd.getRtd();
			if (value != null) {
				DevicePollutants setting = dadService.getDevicePollutants(
						deviceId, rtd.getCode());
				if (setting == null
						|| (setting.getWarnMin() == null && setting
								.getWarnMax() == null)) {
					continue;
				}

				Double min = setting.getWarnMin();
				Double max = setting.getWarnMax();
				String type = null;
				if (min != null && value < min) {
					// 低于告警
					type = RtdWarn.LESS;
				} else if (max != null && value > max) {
					// 高于告警
					type = RtdWarn.OVER;
				}
				
				if(type != null) {
					Pollutants p = dadService.getPollutantById(rtd.getCode());
					RtdWarn warn = new RtdWarn();
					warn.setCode(rtd.getCode());
					warn.setName(p.getDataName());
					warn.setMax(max);
					warn.setMin(min);
					warn.setRtd(value);
					warn.setType(type);
					warns.add(warn);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(warns)) {
			msg.setRtdWarns(warns);
			msg.setDeviceId(deviceId);
			msg.setUserIds(userIds);
			rtdWarnMsgSender.send(msg);
			if(log.isDebugEnabled()) {
				log.debug("sendRtdWarnMsg to : {}", userIds);
			}
			
		}
	}

	@Override
	public PollutantsRtdData getRtdData(String deviceId, String dataCode)
			throws Exception {
		List<PollutantsRtdData> rtdList = dadDataCacheService.get(CacheKeyUtil.getLastRtdListKey(deviceId));
		PollutantsRtdData rst = null;
		for(PollutantsRtdData prt: rtdList) {
			if(dataCode.equals(prt.getCode())) {
				rst = prt;
				break;
			}
		}
		return rst;
	}

	@Override
	public void saveMinuteDatas(List<PollutantsCountData> counts,
			String dataTime) throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("saveMinuteDatas({},{})", counts.size(), dataTime);
		}
		dadService.saveDevicePltByCountDatas(counts);
		minuteDataDao.saveMinuteDatas(counts, dataTime);
	}

	@Override
	public void saveHourDatas(List<PollutantsCountData> counts, String dataTime)
			throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("saveHourDatas({},{})", counts.size(), dataTime);
		}
		dadService.saveDevicePltByCountDatas(counts);
		hourDataDao.saveHourDatas(counts, dataTime);
	}

	@Override
	public void saveDayDatas(List<PollutantsCountData> counts, String dataTime)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("saveDayDatas({}, {})", counts.size(), dataTime);
		}
		dadService.saveDevicePltByCountDatas(counts);
		dayDataDao.saveDayDatas(counts, dataTime);
	}

	@Override
	public List<PollutantsCountData> getMinuteDatas(String deviceId,
			String dataCode, String hourTime) throws Exception {
		Date start = null, end = null;
		try {
			start = DateUtil.getDateByString(hourTime, DateUtil.hourFmt);
			end = DateUtil.getLastTimeOfHour(start);
		} catch (Exception e) {
			throw new BusinessServiceException(ErrorConstant.DATE_TIME_ERROR,
					hourTime);
		}

		return minuteDataDao.getMinuteDatas(deviceId, dataCode, start, end,
				hourTime);
	}

	@Override
	public List<PollutantsCountData> getHourDatas(String deviceId,
			String dataCode, String dayTime) throws Exception {
		Date start = null, end = null;
		try {
			start = DateUtil.getDateByString(dayTime, DateUtil.dayFmt);
			end = DateUtil.getLastTimeOfDay(start);
		} catch (Exception e) {
			throw new BusinessServiceException(ErrorConstant.DATE_TIME_ERROR,
					dayTime);
		}

		return hourDataDao
				.getHourDatas(deviceId, dataCode, start, end, dayTime);
	}

	@Override
	public List<PollutantsCountData> getDayDatas(String deviceId,
			String dataCode, String mouthTime) throws Exception {
		Date start = null, end = null;
		try {
			start = DateUtil.getDateByString(mouthTime, DateUtil.mouthFmt);
			end = DateUtil.getLastTimeOfMonth(start);
		} catch (Exception e) {
			throw new BusinessServiceException(ErrorConstant.DATE_TIME_ERROR,
					mouthTime);
		}

		return dayDataDao
				.getDayDatas(deviceId, dataCode, start, end, mouthTime);
	}

	public void setMinuteDataDao(MinuteDataDao minuteDataDao) {
		this.minuteDataDao = minuteDataDao;
	}

	public void setHourDataDao(HourDataDao hourDataDao) {
		this.hourDataDao = hourDataDao;
	}

	public void setDayDataDao(DayDataDao dayDataDao) {
		this.dayDataDao = dayDataDao;
	}

	public void setDadService(DadService dadService) {
		this.dadService = dadService;
	}

	public void setDadDataCacheService(CacheService dadDataCacheService) {
		this.dadDataCacheService = dadDataCacheService;
	}

	public void setRtdWarnMsgSender(RtdWarnMsgSender rtdWarnMsgSender) {
		this.rtdWarnMsgSender = rtdWarnMsgSender;
	}

	@Override
	public List<MonthCountData> getMonthDatas(String deviceId, String dataCode,
			String year) throws Exception {
		return dayDataDao.getMonthDatas(deviceId, dataCode, year);
	}

	@Override
	public List<QuarterCountData> getQuarterDatas(String deviceId,
			String dataCode, String year) throws Exception {
		
		return dayDataDao.getQuarterDatas(deviceId, dataCode, year);
	}

	@Override
	public List<PollutantsCountData> getMinuteDatasByDay(String deviceId,
			String dataCode, String dayTime) throws Exception {
		Date start = null, end = null;
		try {
			start = DateUtil.getDateByString(dayTime, DateUtil.dayFmt);
			end = DateUtil.getLastTimeOfDay(start);
		} catch (Exception e) {
			throw new BusinessServiceException(ErrorConstant.DATE_TIME_ERROR,
					dayTime);
		}

		return minuteDataDao.getMinuteDatas(deviceId, dataCode, start, end,
				dayTime);
	}

	@Override
	public List<PollutantsRtdData> getRtdList(String deviceId) throws Exception {
		List<PollutantsRtdData> rtdList = dadDataCacheService.get(CacheKeyUtil.getLastRtdListKey(deviceId));
		
		if(CollectionUtils.isEmpty(rtdList)) {
			return null;
		}
		for(PollutantsRtdData prt : rtdList){
			Pollutants p = dadService.getPollutantById(prt.getCode());
			prt.setName(p.getDataName());
			prt.setDataUnit(p.getDataUnit());
		}
		return rtdList;
	}

}
