/**
 * 
 */
package com.naswork.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.naswork.service.CacheService;

/**
 * @since 2015年9月10日 下午2:00:09
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private CacheManager cacheManager;
	
	@Override
	public Cache getCache(String cacheName){
		Cache cache = cacheManager.getCache(cacheName);
		return cache;
	}
	
	@Override
	public Object get(String cacheName, String key) {
		Cache cache = getCache(cacheName);
		if(cache==null)return null;
		ValueWrapper element = cache.get(key);
		if (element == null) {
			return null;
		}
		return element.get();
	}

	@Override
	public void put(String cacheName, String key, Object value) {
		Cache cache = getCache(cacheName);
		if(cache!=null)
			cache.put(key, value);
	}
	
	@Override
	public Object get(String key) {
		Cache cache = getCache("COMMON_CACHE");
		if(cache==null)return null;
		ValueWrapper element = cache.get(key);
		if (element == null) {
			return null;
		}
		return element.get();
	}
	
	@Override
	public void put(String key, Object value) {
		Cache cache = getCache("COMMON_CACHE");
		if(cache!=null){
			cache.put(key,value);
		}
	}
	
	@Override
	public void remove(String cacheName, String key){
		Cache cache = getCache(cacheName);
		if(cache!=null){
			ValueWrapper elem = cache.get(key);
			if(null != elem){
				cache.evict(key);
			}
		}
	}

	
	@Override
	public void remove(String key){
		Cache cache = getCache("COMMON_CACHE");
		if(cache!=null){
			ValueWrapper elem = cache.get(key);
			if(null != elem){
				cache.evict(key);
			}
		}
	}

	@Override
	public String getDmMc(String columnName, String dm) {
		return (String) get(columnName+"_CACHE", dm);
	}

	@Override
	public void cleanCache(String cacheName) {
		Cache cache = getCache(cacheName);
		if(cache!=null)
			cache.clear();
	}

	@Override
	public void initCache(String cacheName,Map<String, Object> data) {
		Cache cache = getCache(cacheName);
		if(cache!=null){
			cache.clear();
			for (String key : data.keySet()) {
				cache.put(key, data.get(key));
			}
		}
	}

	@Override
	public Object getDmObj(String columnName, String dm) {
		return get(columnName+"_CACHE", dm);
		
	}

}
