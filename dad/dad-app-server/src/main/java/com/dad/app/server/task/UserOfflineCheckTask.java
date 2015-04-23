package com.dad.app.server.task;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.app.server.util.ChannelUtil;
import com.dad.common.service.UserService;
import com.dad.common.util.DateUtil;

public class UserOfflineCheckTask {
	
	private static Logger log = LoggerFactory.getLogger(UserOfflineCheckTask.class);
	
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void execute() throws Exception {
		log.debug("execute UserOfflineCheckTask at :{}", DateUtil.getStringByDate(new Date(), DateUtil.commonDatefmt));
		List<Long> userIds = userService.onlineUserIds();
		if(CollectionUtils.isEmpty(userIds)) {
			return;
		}
		for(Long uId : userIds) {
			Channel c = ChannelUtil.getChanel(uId);
			if(c == null) {
				userService.setUserOnline(uId, false);
				log.info("user {} check offline .", uId);
			}
		}
	}
}
