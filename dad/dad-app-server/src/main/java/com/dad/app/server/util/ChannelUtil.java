package com.dad.app.server.util;

import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;

public class ChannelUtil {
	private static final ConcurrentMap<Long, Channel> channelMaps = PlatformDependent.newConcurrentHashMap();
	
	public static Channel getChanel(Long userId) {
		return channelMaps.get(userId);
	}

	
	public static void putChannel(Long userId, Channel channel) {
		channelMaps.put(userId, channel);
	}
	
	public static void removeChanel(Long userId) {
		channelMaps.remove(userId);
	}
	
	public static int channelSize() {
		return channelMaps.size();
	}
	
}
