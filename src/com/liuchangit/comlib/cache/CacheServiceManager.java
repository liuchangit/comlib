package com.liuchangit.comlib.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.liuchangit.comlib.config.Config;
import com.liuchangit.comlib.config.PropertiesConfig;

public class CacheServiceManager {
    private static final Logger LOG = Logger.getLogger("common");
    
	private final Map<String, CacheService> cacheMap;
	
	public CacheServiceManager(String confName) {
		Config config = new PropertiesConfig(confName);
		String[] preloadNames = config.gets("preload.cache");
		Map<String, CacheService> cacheMap = new HashMap<String, CacheService>();
		if (preloadNames != null) {
			for (String name : preloadNames) {
				try {
					MemcacheService cache = new MemcacheService(config.getBranch(name));
					cacheMap.put(name, cache);
				} catch (Exception e) {
					LOG.error("MemcacheService init error: cacheName:" + name, e);
				}
			}
		}
		this.cacheMap = cacheMap;
	}
	
	public CacheService getCacheService(String cacheName) {
		return cacheMap.get(cacheName);
	}
	
	public Collection<String> getCacheNames() {
		return cacheMap.keySet();
	}

}
