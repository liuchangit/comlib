package com.liuchangit.comlib.cache;

import java.util.Date;
import java.util.Map;

import com.danga.MemCached.CacheGetter;
import com.danga.MemCached.CacheSetter;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.liuchangit.comlib.config.Config;

public class MemcacheService implements CacheService {

	private static final int DEFAULT_TIMEOUT = 1800000;

	private MemCachedClient mcc = null;
	private int timeout = 1800000; // 单位为毫秒

	public MemcacheService(Config config) throws Exception {
		String poolName = config.get("poolname");
		String serverlist = config.get("serverlist", null);
		if (poolName == null || "".equals(poolName.trim())
				|| serverlist == null || serverlist.trim().length() == 0) {
			throw new RuntimeException(
					"ERROR : The poolname or serverlist for cache "
							+ config.get()
							+ " is empty in the wapSearchMemCache.properties");
		}
		String[] servers = serverlist.split(",");
		SockIOPool pool = SockIOPool.getInstance(poolName);
		pool.setServers(servers);
		pool.setFailover(config.getBool("failover", true));
		pool.setInitConn(config.getInt("initconn", 10));
		pool.setMinConn(config.getInt("minconn", 5));
		pool.setMaxConn(config.getInt("maxconn", 250));
		pool.setMaintSleep(config.getLong("maintsleep", 30));
		pool.setNagle(config.getBool("nagle", false));
		pool.setSocketTO(config.getInt("socketTO", 3000));
		pool.setAliveCheck(config.getBool("alivecheck", true));
		pool.setHashingAlg(SockIOPool.CONSISTENT_HASH);
		pool.initialize();

		MemCachedClient mcc = new MemCachedClient();
		mcc.setPoolName(poolName);
		mcc.setCompressEnable(true);
		mcc.setCompressThreshold(1000000);
		this.mcc = mcc;
		this.timeout = config.getInt("timeout", DEFAULT_TIMEOUT);
	}

	@Override
	public Object get(String key) {
		return mcc.get(key);
	}

	@Override
	public Object[] getMulti(String[] keys) {
		return mcc.getMultiArray(keys);
	}

	@Override
	public Map getStats() {
		return mcc.stats();
	}

	@Override
	public boolean remove(String key) {
		return mcc.delete(key);
	}

	@Override
	public boolean set(String key, Object value) {
		return set(key, value, timeout);
	}

	@Override
	public boolean set(String key, Object value, int timeout) {
		return mcc.set(key, value, new Date(System.currentTimeMillis() + timeout));
	}

	@Override
	public Map dumpKey(int slabNumber, int limit) {
		return mcc.statsCacheDump(null, slabNumber, limit);
	}
	@Override
	public Map dumpItem() {
		return mcc.statsItems();
	}

	@Override
	public void get(String key, CacheGetter getter, int timeout) {
		mcc.get(key, getter, timeout);
	}

	@Override
	public void set(String key, Object value, CacheSetter setter, int timeout) {
		mcc.set(key, value, new Date(System.currentTimeMillis() + this.timeout), setter, timeout);
	}
	
	public boolean invalidate() {
		return mcc.flushAll();
	}

}
