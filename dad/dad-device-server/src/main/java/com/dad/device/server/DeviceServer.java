package com.dad.device.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dad.common.resource.Log4jConfigurator;

public class DeviceServer {

	private static final Logger log = LoggerFactory.getLogger(DeviceServer.class);

/*	public static ChannelGroup allChannels = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);*/

	private AbstractApplicationContext ctx = null;

	public DeviceServer() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ctx.registerShutdownHook();
	}

	public void run() throws Exception {
		Integer port = ctx.getBean("serverPort", Integer.class);
		log.info("################## start server on {}:{}", InetAddress
				.getLocalHost().getHostAddress(), port);
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		final Integer heartbeatTime =  ctx.getBean("heartbeatTime", Integer.class);
		try {
			ServerBootstrap b = new ServerBootstrap(); 
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ch.pipeline()
											.addLast(
													new LineBasedFrameDecoder(
															1040),
													new StringDecoder(
															CharsetUtil.US_ASCII),
													// 20分钟没有消息就断开链接
													new ReadTimeoutHandler(heartbeatTime),
													ctx.getBean(
															"serverHandler",
															ServerHandler.class),
													new StringEncoder(CharsetUtil.US_ASCII));
								}

							}).option(ChannelOption.SO_BACKLOG, 512)
					.option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();

			if (ctx != null) {
				ctx.close();
			}

		}
	}

	public static void main(String[] args) throws Exception {
		Log4jConfigurator.init(null);
		new DeviceServer().run();
	}
}
