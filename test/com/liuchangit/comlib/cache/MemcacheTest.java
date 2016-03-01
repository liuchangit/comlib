package com.liuchangit.comlib.cache;

import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Test;

import com.liuchangit.comlib.cache.CacheService;
import com.liuchangit.comlib.cache.CacheServiceManager;

public class MemcacheTest {
	
	@Test
	public void testGet() {
		CacheServiceManager cacheManager = new CacheServiceManager("memcache");
		CacheService cache = cacheManager.getCacheService("test");
		cache.set("key1", "hello");
		Object obj = cache.get("key1");
		assertEquals("hello", obj);
	}
	
	@Test
	public void testMultiGet() {
		CacheServiceManager cacheManager = new CacheServiceManager("memcache");
		CacheService cache = cacheManager.getCacheService("test");
		cache.set("key1", "hello");
		cache.set("key2", "world");
		cache.set("key3", "there");
		Object[] objs = cache.getMulti("key1", "key2", "key3");
		System.out.println(Arrays.toString(objs));
		assertArrayEquals(new String[] { "hello", "world", "there" }, objs);

		objs = cache.getMulti("key1", "key5", "key3");
		System.out.println(Arrays.toString(objs));
		assertArrayEquals(new String[] { "hello", null, "there" }, objs);
		//TODO 调查 每次打出来的结果不一样 113.105.248.216:12221
	}
	
	@Test
	public void testStats() {
		CacheServiceManager cacheManager = new CacheServiceManager("memcache");
		CacheService cache = cacheManager.getCacheService("test");
		cache.getStats();
		System.out.println(cache.getStats());
	}
}
