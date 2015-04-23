package com.dad.app.server;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ObjectEncoder extends MessageToMessageEncoder<Object> {
	
	private static final Logger log = LoggerFactory.getLogger(ObjectEncoder.class);
	
	private static final String endChar = "\r\n";
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg,
			List<Object> out) throws Exception {
		String m = new Gson().toJson(msg);
		if(m != null) {
			if(log.isDebugEnabled()) {
				log.debug(ctx.channel().id() + " server out msg :"+m);
			}
			out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(m+endChar), CharsetUtil.UTF_8));
		}
	}

}
