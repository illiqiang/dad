package com.dad.device.server;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.entity.Device;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.entity.PollutantsRtdData;
import com.dad.common.service.DadDataService;
import com.dad.common.service.DadService;
import com.dad.common.util.DataType;
import com.dad.device.server.util.ChannelUtil;
import com.dad.device.server.util.CrcUtil;
import com.dad.device.server.util.IllegalRequestException;
import com.dad.device.server.util.PatternUtil;

public class ServerHandler extends ChannelHandlerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
	
	//private static final Logger dataLog = LoggerFactory.getLogger("data");
	
	private static final Logger unknowdataLog = LoggerFactory.getLogger("unknowdata");

	
	private static final String bodyDelimiter = "CP=&&";
	private static final String startChars = "##";
	private static final String endChar = "\r\n";
	private String channelDeviceId = null;
	
	private DadService dadService;
	
	private DadDataService dadDataService;

	public void setDadService(DadService dadService) {
		this.dadService = dadService;
	}

	public void setDadDataService(DadDataService dadDataService) {
		this.dadDataService = dadDataService;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		log.info("channel Disconnect! now channels : {}", ChannelUtil.channelSize());
		if(StringUtils.isNotBlank(channelDeviceId)) {
			dadService.setDeviceOnline(channelDeviceId, false);
			ChannelUtil.removeChanel(channelDeviceId);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String message = (String) msg;
		String[] msgs = null;
		boolean check = false;
		if(message.startsWith(startChars) && message.length() > 10) {
			String text = message.substring(6, message.length()-4);
			check = CrcUtil.checkCrc(text, message.substring(message.length()-4));
			msgs = text.substring(0, text.length()).split(bodyDelimiter);
		}
		
		if(!check) {
			throw new IllegalRequestException(IllegalRequestException.crcError, message);
		} 
		if(msgs.length == 2) {
			String dataType = PatternUtil.getString(PatternUtil.cnPtn, msgs[0]); 
			DataType type = DataType.getByCode(dataType);
			
			if(type == null){
				log.warn("{} unknown CN={}, msg:{}", ctx.channel().id(),dataType,message);
			} else if(type == DataType.HEARTBEAT) {
				ctx.channel().writeAndFlush(message+endChar);
			} else {
				Device device = PatternUtil.parseHeader(msgs[0]);
				String newId = device.getDeviceId();
				//每次请求的deviceId必须一样
				if(channelDeviceId != null && channelDeviceId.equals(newId)) {
					
				//第一次访问
				} else if(channelDeviceId == null && newId != null){
					if(dadService.checkAndUpdateDevice(device)) {
						channelDeviceId = newId;
						dadService.setDeviceOnline(newId, true);
						ChannelUtil.putChannel(newId, ctx.channel());;
					} else {
						throw new IllegalRequestException(IllegalRequestException.notRegister, message);
					}
					
				} else {
					throw new IllegalRequestException(IllegalRequestException.deviceChange, message);
				}
				if(msgs[1].length()<2) {
					log.warn("{} error msg : {}", ctx.channel().id(), message);
					return;
				}
				msgs[1] = msgs[1].substring(0, msgs[1].length()-2);
				Matcher m = PatternUtil.dataTimePtn.matcher(msgs[1]);
				String body = null, dataTime=null;
				if (m.find()) {
					dataTime = m.group(2);
					body = msgs[1].replace(m.group(1), "");
				} else {
					log.warn("{} msg dataTime miss: {}", ctx.channel().id(), message);
					return;
				}
				
				if(type == DataType.RTD) {
					List<PollutantsRtdData> datas = PatternUtil.parseRtdData(body, dataTime, newId);
					if(CollectionUtils.isNotEmpty(datas)) {
						dadDataService.sendRtdDatas(datas, newId);
					}
				} else if (type == DataType.MINUTE){
					List<PollutantsCountData> datas = PatternUtil.parseCountData(body, dataTime, newId);
					if(CollectionUtils.isNotEmpty(datas)) {
						dadDataService.saveMinuteDatas(datas, dataTime);
					}
					
				} else if (type == DataType.HOUR) {
					List<PollutantsCountData> datas = PatternUtil.parseCountData(body, dataTime, newId);
					if(CollectionUtils.isNotEmpty(datas)) {
						dadDataService.saveHourDatas(datas, dataTime);
					}
				} else if (type == DataType.DAY) {
					List<PollutantsCountData> datas = PatternUtil.parseCountData(body, dataTime, newId);
					if(CollectionUtils.isNotEmpty(datas)) {
						dadDataService.saveDayDatas(datas, dataTime);
					}
				}
			}
			
		} else {
			unknowdataLog.info("{} unknown msg: {}", ctx.channel().id(), msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//super.exceptionCaught(ctx, cause);
		log.error(String.format("%s handler exception", ctx.channel().id()), cause);
		if(cause instanceof IllegalRequestException) {
			ctx.close();
		}
	}

}
