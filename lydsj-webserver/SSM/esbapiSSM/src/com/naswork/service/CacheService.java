package com.naswork.service;

import java.util.Map;

import org.springframework.cache.Cache;

public interface CacheService {

	/**
	 * 获取cache池
	 * @param cacheName
	 * @return
	 * @since 2015年9月10日 下午5:32:48
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public Cache getCache(String cacheName);

	/**
	 * 从缓存中获取对象
	 * @param cacheName
	 * @param key
	 * @return
	 * @since 2015年9月10日 下午5:33:07
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public Object get(String cacheName, String key);

	/**
	 * 将对象放入缓存
	 * @param cacheName
	 * @param key
	 * @param value
	 * @return
	 * @since 2015年9月10日 下午5:33:25
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void put(String cacheName, String key,
			Object value);

	/**
	 * 从默认缓存池中，获取指定key对象
	 * @param key
	 * @return
	 * @since 2015年9月10日 下午5:33:37
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public Object get(String key);

	/**
	 * 将对象放入默认缓存池中
	 * @param key
	 * @param value
	 * @return
	 * @since 2015年9月10日 下午5:34:07
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void put(String key, Object value);

	/**
	 * 删除指定缓存池中的指定对象
	 * @param cacheName
	 * @param key
	 * @return
	 * @since 2015年9月10日 下午5:34:26
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void remove(String cacheName, String key);

	/**
	 * 删除默认缓存池中的指定对象
	 * @param key
	 * @return
	 * @since 2015年9月10日 下午5:34:26
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void remove(String key);
	
	/**
	 * 取得代码对应的中文名称
	 * @param columnName
	 * @param dm
	 * @return
	 * @since 2015年9月12日 上午11:32:26
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public String getDmMc(String columnName,String dm);
	
	public Object getDmObj(String columnName, String dm);
	
	/**
	 * 清空缓存
	 * @param cacheName
	 * @since 2015年9月12日 上午11:39:43
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void cleanCache(String cacheName);
	
	/**
	 * 初始化代码cache
	 * @param cacheName
	 * @param data
	 * @since 2015年9月12日 上午11:43:00
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void initCache(String cacheName,Map<String, Object> data);

}