package com.liuchangit.comlib.cache;

import java.util.Map;

import com.danga.MemCached.CacheGetter;
import com.danga.MemCached.CacheSetter;

public interface CacheService {
	
	public Object get(String key);
	
	public Object[] getMulti(String... keys);
	
	public boolean set(String key, Object value);
	
	public boolean set(String key, Object value, int expireTime);

	public void get(String key, CacheGetter getter, int timeout);
	
	public void set(String key, Object value, CacheSetter setter, int timeout);
	
	public boolean remove(String key);
	
	public Map getStats();
	
	public Map dumpKey(int slabNumber, int limit);
	
	public Map dumpItem();
	
	public boolean invalidate();
}
