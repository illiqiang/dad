package com.dad.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dad.common.entity.Device;
import com.dad.service.dao.DeviceDao;

public class MainTest {

	private static AbstractApplicationContext ctx = null;

	@BeforeClass
	public static void before() {
		try {
			String contextName = "applicationContext.xml";
			ctx = new ClassPathXmlApplicationContext(contextName);
			ctx.registerShutdownHook();
			System.out.println("=========== start success!===========");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterClass
	public static void after() {
		if (ctx != null) {
			ctx.close();
		}
		System.out.println("=========== system out!===========");
	}

	@Test
	public void test() throws InterruptedException {
		DeviceDao deviceDao = ctx.getBean(DeviceDao.class);
		Device d = deviceDao.getDevice("test2");
		System.out.println(d.getDeviceId());
	}
}
