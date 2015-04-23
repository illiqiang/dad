package com.dad.app.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.app.server.msg.MsgType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ObjectDecoder extends MessageToMessageDecoder<String> {
	
	private static final String typeString = "type";
	private static final Logger log = LoggerFactory
			.getLogger(ObjectDecoder.class);
	
	private static final JsonParser parser = new JsonParser();
	
	@Override
	protected void decode(ChannelHandlerContext ctx, String msg,
			List<Object> out) throws Exception {
		String type = null;
		JsonObject jsonObj =  null;
		try {
			jsonObj = parser.parse(msg).getAsJsonObject();
			type = jsonObj.get(typeString).getAsString();
			Class<?> classType = MsgType.getMsgClass(type);
			if(classType != null) {
				out.add(new Gson().fromJson(jsonObj, classType));
			} else {
				log.error("channel {}, unknow message: {}, decoder error", ctx.channel().id(), msg);
			}
		} catch (Exception e) {
			log.error("channel {}, unknow message: {}, decoder error", ctx.channel().id(), msg);
		}
	}

}
