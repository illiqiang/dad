package com.dad.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dad.api.request.DayRqst;
import com.dad.api.request.GroupDeviceRqst;
import com.dad.api.request.HourRqst;
import com.dad.api.request.MinuteByDayRqst;
import com.dad.api.request.MinuteRqst;
import com.dad.api.request.MonthRqst;
import com.dad.api.request.PollutantsRqst;
import com.dad.api.request.QuarterRqst;
import com.dad.api.request.RtdRqst;
import com.dad.api.request.SetPollutantRqst;
import com.dad.api.request.UserGroupRqst;
import com.dad.api.response.ApiDevice;
import com.dad.api.response.BasicResponse;
import com.dad.api.response.DataRsp;
import com.dad.api.response.GroupDeviceRsp;
import com.dad.api.response.MonthDataRsp;
import com.dad.api.response.PollutantsRsp;
import com.dad.api.response.QuarterDataRsp;
import com.dad.api.response.RtdRsp;
import com.dad.api.response.UserGroupRsp;
import com.dad.common.entity.Device;
import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.Group;
import com.dad.common.entity.MonthCountData;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.entity.PollutantsRtdData;
import com.dad.common.entity.QuarterCountData;
import com.dad.common.service.DadDataService;
import com.dad.common.service.DadService;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.util.StType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
import com.rop.response.BusinessServiceErrorResponse;
import com.rop.response.ServiceUnavailableErrorResponse;

@ServiceMethodBean
public class DadWebApi {

	private static Logger log = LoggerFactory.getLogger(DadWebApi.class);

	@Autowired
	private DadService dadService;

	@Autowired
	private DadDataService dadDataService;
	
	public void init() {
	}

	@ServiceMethod(method = "dad.usergroup", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getUserGroup(UserGroupRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getUserGroup({})", request.getUserId());
		}
		Object resp = null;
		try {
			List<Group> groups = dadService
					.getGroupsByUser(request.getUserId());
			UserGroupRsp rsp = new UserGroupRsp();
			rsp.setGroups(groups);
			resp = rsp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.groupdevice", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getGroupDevice(GroupDeviceRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getGroupDevice({})", request.getGroupId());
		}
		Object resp = null;
		try {
			List<Device> devices = dadService.getDeviceByGroup(request
					.getGroupId());
			GroupDeviceRsp rsp = new GroupDeviceRsp();
			if(CollectionUtils.isNotEmpty(devices)) {
				List<ApiDevice> ads = new ArrayList<>();
				for(Device d:devices) {
					ApiDevice ad = new ApiDevice();
					ad.setDeviceId(d.getDeviceId());
					ad.setSt(d.getSt());
					ad.setType(StType.getNameByCode(d.getSt()));
					ads.add(ad);
				}
				rsp.setDevices(ads);
			}
			
			resp = rsp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.pollutants", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getPollutants(PollutantsRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getPollutants({})", request.getDeviceId());
		}
		Object resp = null;
		try {
			List<DevicePollutants> values = dadService
					.getDevicePollutants(request.getDeviceId());
			PollutantsRsp rsp = new PollutantsRsp();
			if (CollectionUtils.isNotEmpty(values)) {
				rsp.setPollutants(values);
			}
			resp = rsp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.setpollutants", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object setPollutants(SetPollutantRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("setPollutants({},{},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getMin(), request.getMax());
		}
		Object resp = null;
		try {
			if (request.getMin() == null && request.getMax() == null) {

			} else {
				dadService.setPollutantWarn(request.getDeviceId(),
						request.getDataCode(), request.getMin(),
						request.getMax());
			}
			resp = new BasicResponse();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.rtddata", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getRtdData(RtdRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getRtdData({},{})", request.getDeviceId(),
					request.getDataCode());
		}
		Object resp = null;
		try {
			PollutantsRtdData data = dadDataService.getRtdData(
					request.getDeviceId(), request.getDataCode());
			RtdRsp rsp = new RtdRsp();
			rsp.setRtd(data);
			resp = rsp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.minutedata", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getMinuteData(MinuteRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getMinuteData({},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getHour());
		}
		Object resp = null;
		try {
			List<PollutantsCountData> datas = dadDataService.getMinuteDatas(request.getDeviceId(),
					request.getDataCode(), request.getHour());
			DataRsp rsp = new DataRsp();
			rsp.setDatas(datas);
			resp = rsp;
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.hourdata", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getHourData(HourRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getHourData({},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getDay());
		}
		Object resp = null;
		try {
			List<PollutantsCountData> datas = dadDataService.getHourDatas(request.getDeviceId(),request.getDataCode(), request.getDay());
			DataRsp rsp = new DataRsp();
			rsp.setDatas(datas);
			resp = rsp;
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.daydata", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getDayData(DayRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getDayData({},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getMonth());
		}
		Object resp = null;
		try {
			List<PollutantsCountData> datas = dadDataService.getDayDatas(request.getDeviceId(),request.getDataCode(), request.getMonth());
			DataRsp rsp = new DataRsp();
			rsp.setDatas(datas);
			resp = rsp;
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}
	
	@ServiceMethod(method = "dad.monthdata", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getMonthData(MonthRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getMonthData({},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getYear());
		}
		Object resp = null;
		try {
			List<MonthCountData> datas = dadDataService.getMonthDatas(request.getDeviceId(),
					request.getDataCode(), request.getYear());
			MonthDataRsp rsp = new MonthDataRsp();
			rsp.setDatas(datas);
			resp = rsp;
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}
	
	@ServiceMethod(method = "dad.quarterdata", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getQuarterData(QuarterRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getQuarterData({},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getYear());
		}
		Object resp = null;
		try {
			List<QuarterCountData> datas = dadDataService.getQuarterDatas(request.getDeviceId(),
					request.getDataCode(), request.getYear());
			QuarterDataRsp rsp = new QuarterDataRsp();
			rsp.setDatas(datas);
			resp = rsp;
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}

	@ServiceMethod(method = "dad.minutedatabyday", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object getMinuteDataByDay(MinuteByDayRqst request) {
		if (log.isDebugEnabled()) {
			log.debug("getMinuteDataByDay({},{},{})", request.getDeviceId(),
					request.getDataCode(), request.getDay());
		}
		Object resp = null;
		try {
			List<PollutantsCountData> datas = dadDataService.getMinuteDatasByDay(request.getDeviceId(),
					request.getDataCode(), request.getDay());
			DataRsp rsp = new DataRsp();
			rsp.setDatas(datas);
			resp = rsp;
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
	}
	
}
