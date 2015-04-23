package com.dad.device.server.task;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.service.DadService;
import com.dad.common.util.DateUtil;
import com.dad.device.server.util.ChannelUtil;

public class DeviceOfflineCheckTask {
	private static final Logger log = LoggerFactory.getLogger(DeviceOfflineCheckTask.class);
	private DadService dadService;
	
	public void setDadService(DadService dadService) {
		this.dadService = dadService;
	}

	public void execute() throws Exception {
		log.debug("execute DeviceOfflineCheckTask at :{}", DateUtil.getStringByDate(new Date(), DateUtil.commonDatefmt));
		List<String> deviceIds = dadService.getOnlineDeviceIds();
		if(CollectionUtils.isEmpty(deviceIds)) {
			return;
		}
		for(String deviceId : deviceIds) {
			Channel c = ChannelUtil.getChanel(deviceId);
			if(c==null) {
				dadService.setDeviceOnline(deviceId, false);
				log.info("device {} check offline .", deviceId);
			}
		}
	}
}
