package com.dad.service.datasource;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private String driverClass;
	private Integer acquireIncrement;
	private Integer initialPoolSize;
	private Integer maxPoolSize;
	private Integer minPoolSize;
	private Integer maxIdleTime;
	private Integer acquireRetryDelay;
	private Integer acquireRetryAttempts;
	private Boolean breakAfterAcquireFailure;

	private String dbKeys;
	private String urlTemplet;
	private String urls;
	private String usernames;
	private String passwords;
	
	private Map<Object, Object> targetDataSources = new HashMap<>();

	@Override
	public void afterPropertiesSet() {
		
		String[] dbKeyArray = dbKeys.split(",");
		String[] urlArray = urls.split(",");
		String[] userArray = usernames.split(",");
		String[] passArray = passwords.split(",");

		try {
			for (int i = 0; i < dbKeyArray.length; i++) {
				String url = String.format(urlTemplet, urlArray[i]);
				ComboPooledDataSource db = createDataSource(url, userArray[i],
						passArray[i]);
				targetDataSources.put(dbKeyArray[i], db);
				DataSourceUtil.addSupportDb(dbKeyArray[i]);
			}
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		super.setTargetDataSources(targetDataSources);
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceUtil.getDataSourceKey();
	}

	public ComboPooledDataSource createDataSource(String url, String username,
			String password) throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driverClass);
		dataSource.setAcquireIncrement(acquireIncrement);
		dataSource.setInitialPoolSize(initialPoolSize);
		dataSource.setMaxPoolSize(maxPoolSize);
		dataSource.setMinPoolSize(minPoolSize);
		dataSource.setMaxIdleTime(maxIdleTime);
		dataSource.setAcquireRetryDelay(acquireRetryDelay);
		dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
		dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
		dataSource.setJdbcUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	public void close() {
		for(Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
			Object ds = entry.getValue();
			if(ds instanceof ComboPooledDataSource) {
				ComboPooledDataSource datasource = (ComboPooledDataSource)ds;
				datasource.close();
			}
		}
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public void setAcquireIncrement(Integer acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public void setInitialPoolSize(Integer initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public void setMaxIdleTime(Integer maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public void setAcquireRetryDelay(Integer acquireRetryDelay) {
		this.acquireRetryDelay = acquireRetryDelay;
	}

	public void setAcquireRetryAttempts(Integer acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}

	public void setBreakAfterAcquireFailure(Boolean breakAfterAcquireFailure) {
		this.breakAfterAcquireFailure = breakAfterAcquireFailure;
	}

	public void setDbKeys(String dbKeys) {
		this.dbKeys = dbKeys;
	}

	public void setUrlTemplet(String urlTemplet) {
		this.urlTemplet = urlTemplet;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}

	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}

	public void setMinPoolSize(Integer minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

}
