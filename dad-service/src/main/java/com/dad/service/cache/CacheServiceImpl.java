package com.dad.service.cache;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.service.CacheService;

/**
 * 缓存服务
 * @author Administrator
 *
 */
public class CacheServiceImpl implements CacheService {

	private static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

	private Cache cache;

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * 加入缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @return
	 * @throws Exception
	 */
	public Object set(String key, Object value) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(".set(key={}, value={})", key, value.getClass()
					.getName());
		}

		Element prev = null;
		Element ele = cache.get(key);
		if (ele == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("{} miss ehcache!", key);
			}
			ele = new Element(key, value);
			cache.put(ele);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("{} hit ehcache!", key);
			}
			ele = new Element(key, value);
			cache.remove(key);
			cache.put(ele);
		}

		return prev;
	}

	/**
	 * 加入缓存，指定过期时间（不会大于最大过期时间）
	 * @param key 键
	 * @param value 值
	 * @param timeToLiveSeconds 过期秒数
	 * @return
	 * @throws Exception
	 */
	public Object set(String key, Object value, int timeToLiveSeconds)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(".set(key={}, value=Object, timeToLiveSeconds={})",
					key, timeToLiveSeconds);
		}
		Element prev = null;
		Element ele = cache.get(key);
		if (ele == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("{} miss ehcache!", key);
			}
			ele = new Element(key, value);
			ele.setTimeToLive(timeToLiveSeconds);
			cache.put(ele);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("{} hit ehcache!", key);
			}
			cache.remove(key);

			ele = new Element(key, value);
			ele.setTimeToLive(timeToLiveSeconds);
			cache.put(ele);
		}

		return prev;
	}

	/**
	 * 从缓存中获取数据
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T get(String key) throws Exception {

		logger.debug(String.format(".get(key=%s)", key));

		Element ele = cache.get(key);
		if (ele != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("{} hit ehcache!", key);
			}
			return (T) ele.getObjectValue();
		}

		return null;
	}

	/**
	 * 从缓存中删除数据
	 * @param key
	 * @throws Exception
	 */
	public void remove(String key) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(".remove({})", key);
		}
		cache.remove(key);
	}

	/**
	 * 判断缓存中有没有数据
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean isKeyInCache(String key) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(".isKeyInCache({})", key);
		}
		return cache.isKeyInCache(key);
	}

}
