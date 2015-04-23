package com.dad.device.server.util;

import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;

public class ChannelUtil {
	private static final ConcurrentMap<String, Channel> channelMaps = PlatformDependent.newConcurrentHashMap();
	
	public static Channel getChanel(String deviceId) {
		return channelMaps.get(deviceId);
	}

	
	public static void putChannel(String deviceId, Channel channel) {
		channelMaps.put(deviceId, channel);
	}
	
	public static void removeChanel(String deviceId) {
		channelMaps.remove(deviceId);
	}
	
	public static int channelSize() {
		return channelMaps.size();
	}
	
}
