package com.dad.common.service;

import java.io.Serializable;

public interface CacheService {
	public abstract Object set(String key, Object value) throws Exception;

	public abstract Object set(String key, Object value, int timeToLiveSeconds)
			throws Exception;

	public abstract <T extends Serializable> T get(String key) throws Exception;

	public abstract void remove(String key) throws Exception;

	public abstract boolean isKeyInCache(String key) throws Exception;

}