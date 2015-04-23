package com.dad.common.resource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;


public class Log4jConfigurator {
	
	public static void init(String confProperty) throws Exception {
		if(StringUtils.isEmpty(confProperty)) {
			confProperty = "app.home";
		}
		String serverHome = System.getProperty(confProperty);
		if (StringUtils.isNotBlank(serverHome)) {
			FileInputStream istream = null;
			StringBuilder path = new StringBuilder();
			try {
				Properties props = new Properties();
				path.append(serverHome).append(File.separator);
				path.append("conf").append(File.separator)
						.append("log4j.properties");
				// 指明了获取配置文件的路径
				istream = new FileInputStream(path.toString());
				props.load(istream);
				PropertyConfigurator.configure(props);
				System.out.println(String.format("Load Log4j configuration :%s", path.toString()));

			} catch (Exception ex) {
				throw new Exception(path.append(" : The path was not found!").toString(), ex);
			} finally {
				if(istream != null) {
					istream.close();
				}	
			}
		}

	}
}
