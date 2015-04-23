package com.dad.app.server.msg.listener;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.app.server.msg.RtdWarnOut;
import com.dad.app.server.util.ChannelUtil;
import com.dad.common.msg.RtdWarnMsg;
import com.lq.msg.AbstractMessageListener;

public class RtdWarnMsgListener extends AbstractMessageListener<RtdWarnMsg>{
	
	private static Logger log = LoggerFactory.getLogger(RtdWarnMsgListener.class);

	@Override
	public void handle(RtdWarnMsg t) {
		RtdWarnOut out = new RtdWarnOut();
		out.setMsgId(System.currentTimeMillis());
		out.setWarns(t.getRtdWarns());
		out.setDeviceId(t.getDeviceId());
		
		for(Long user : t.getUserIds()) {
			Channel channel = ChannelUtil.getChanel(user);
			if(channel != null) {
				channel.writeAndFlush(out);
			} else {
				log.info("recive a RtdWarnMsg,but user {} not online", user);
			}
			
		}
	}

}
