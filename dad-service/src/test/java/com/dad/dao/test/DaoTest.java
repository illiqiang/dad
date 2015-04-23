package com.dad.dao.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoTest {
	
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

}
