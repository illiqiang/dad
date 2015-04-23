package com.dad.app.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.app.server.msg.HeartbeatMsg;
import com.dad.app.server.msg.HeartbeatOut;
import com.dad.app.server.msg.LoginMsg;
import com.dad.app.server.msg.LoginOut;
import com.dad.app.server.msg.OfflineOut;
import com.dad.app.server.util.ChannelUtil;
import com.dad.common.service.UserService;

public class ServerHandler extends ChannelHandlerAdapter {

	private static final Logger log = LoggerFactory
			.getLogger(ServerHandler.class);

	private static final AttributeKey<String> SESSION_KEY = AttributeKey
			.valueOf("sessionId");
	private static final AttributeKey<Long> user_KEY = AttributeKey
			.valueOf("userId");
	private UserService userService;

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Long userId = ctx.channel().attr(user_KEY).get();
		if (userId != null) {
			log.info("{} user {} offline ", ctx.channel().id(), userId);
			Channel c = ChannelUtil.getChanel(userId);
			if (c != null && ctx.channel().equals(c)) {
				ChannelUtil.removeChanel(userId);
			} 
			userService.setUserOnline(userId, false);
		}

		log.info("{} channel Disconnect! now channels : {}", ctx.channel().id(),
				ChannelUtil.channelSize());
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.error(String.format("{} Channel throw Exception ", ctx.channel().id()), cause);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof LoginMsg) {
			login(ctx, (LoginMsg) msg);
		} else if (msg instanceof HeartbeatMsg) {
			heartbeatMsg(ctx, (HeartbeatMsg) msg);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("{} channelRead({})", ctx.channel().id(), msg);
			}
		}
	}
	
	public void heartbeatMsg(ChannelHandlerContext ctx, HeartbeatMsg hb) {
		if (log.isDebugEnabled()) {
			log.debug("{} heartbeatMsg({})", ctx.channel().id(), hb);
		}
		HeartbeatOut out = new HeartbeatOut(hb.getMsgId());
		ctx.channel().writeAndFlush(out);
	}

	public void login(final ChannelHandlerContext ctx, final LoginMsg login)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("{} login({})", ctx.channel().id(),login);
		}
		final Long nowUserId = login.getUserId();
		final String newSession = login.getSessionId();
		if(nowUserId == null || newSession==null) {
			ctx.close();
			return;
		}
		LoginOut out = new LoginOut(nowUserId);
		out.setMsgId(login.getMsgId());
		if (userService.exitsSession(newSession)) {
			
			ctx.channel().attr(SESSION_KEY).set(newSession);
			ctx.channel().attr(user_KEY).set(nowUserId);
			
			out.setCode(0);
			ChannelFuture future = ctx.channel().writeAndFlush(out);
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					
					final Channel oldChannel = ChannelUtil.getChanel(nowUserId);
					if (oldChannel != null && !oldChannel.equals(ctx.channel())) {
						String oldSession = oldChannel.attr(SESSION_KEY).get();
						log.info(
								"{} user {} login at tow channel,new={},old={}, and old will offline.",
								ctx.channel().id(), nowUserId, newSession, oldSession);
						OfflineOut change = new OfflineOut();
						change.setMsgId(login.getMsgId());
						change.setUserId(login.getUserId());
						ChannelFuture f = oldChannel.writeAndFlush(change);
						// 发送下线消息，成功后关闭连接
						f.addListener(new ChannelFutureListener() {

							@Override
							public void operationComplete(ChannelFuture future)
									throws Exception {
								//不再走下线逻辑
								oldChannel.attr(user_KEY).set(null);
								oldChannel.close();
							}
						});

						ChannelUtil.removeChanel(nowUserId);
						// 老的session失效
						if (oldSession != null
								&& !oldSession.equals(newSession)) {
							userService.removeSession(oldSession);
						}
						
					}
					ChannelUtil.putChannel(nowUserId, ctx.channel());
					//上线
					userService.setUserOnline(nowUserId, true);			
					
				}
			});

		} else {
			// session 失效
			if (log.isDebugEnabled()) {
				log.debug("{} session : {} Expired )", ctx.channel().id(),newSession);
			}
			out.setCode(1);
			ChannelFuture sf = ctx.channel().writeAndFlush(out);
			sf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					ctx.channel().close();
				}
			});
		}
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
