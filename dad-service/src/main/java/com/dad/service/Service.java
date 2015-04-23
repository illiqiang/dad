package com.dad.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dad.common.resource.Log4jConfigurator;

public class Service {
	
	private static final Logger log = LoggerFactory.getLogger(Service.class);
	private static Object lock = new Object();

	public static void main(String[] args) throws Exception {
		Log4jConfigurator.init(null);
		AbstractApplicationContext ctx = null;
		try {
			String contextName = "applicationContext.xml";
			ctx = new ClassPathXmlApplicationContext(contextName);
			ctx.registerShutdownHook();
			log.info("=========== start success!===========");
			synchronized (lock) {
				lock.wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			if(ctx!=null) {
				ctx.close();
			}
			log.info("=========== system out!===========");
		}

	}
}
