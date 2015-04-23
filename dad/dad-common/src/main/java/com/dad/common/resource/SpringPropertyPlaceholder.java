package com.dad.common.resource;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SpringPropertyPlaceholder extends PropertyPlaceholderConfigurer {

	private static Logger logger = LoggerFactory
			.getLogger(SpringPropertyPlaceholder.class);

	private String confProperty;
	
	
	/**
	 * 生产环境加载{server}/etc/server-debug.properties文件
	 * 开发环境加载classpath的server-debug.properties文件
	 * 
	 * @param location
	 *            资源文件
	 */
	@Override
	public void setLocation(Resource location) {
		super.setLocation(checkResource(location));
	}
	
	private Resource checkResource(Resource location) {
		if(StringUtils.isEmpty(confProperty)) {
			confProperty = "app.home";
		}
		String serverHome = System.getProperty(confProperty);

		if (StringUtils.isNotBlank(serverHome)) {
			String fileName = location.getFilename();
			StringBuilder path = new StringBuilder(serverHome);
			path.append(File.separator).append("conf").append(File.separator)
					.append(fileName);
			Resource resource = new FileSystemResource(path.toString());

			if (resource.exists()) {
				//logger.info("Loading resource file: [{}]", path.toString());
				return resource;
			} else {
				throw new RuntimeException(String.format(
						"Resource File not find ：%s", path.toString()));
			}
		} else {
			logger.info("Loading properties file from class path resource [{}] !", location.getFilename());
			return location;
		}
	}
	
	@Override
	public void setLocations(Resource... locations) {
		for(int i=0; i<locations.length;i++) {
			locations[i] = checkResource(locations[i]);
		}
		super.setLocations(locations);
	}

	public void setConfProperty(String confProperty) {
		this.confProperty = confProperty;
	}

}
