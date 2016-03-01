package com.liuchangit.comlib.config;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.liuchangit.comlib.config.Config;
import com.liuchangit.comlib.config.ConfigException;
import com.liuchangit.comlib.config.PropertiesConfig;

public class PropertiesConfigTest {
	
	private static Config config;
	
	@BeforeClass
	public static void setUp() {
		config = new PropertiesConfig("test");
	}
	
	@Test
	public void testGetString() {
		assertNull(config.get());
	}
	
	@Test(expected = ConfigException.class)
	public void testGetBool() {
		assertFalse(config.getBool());
	}
	
	@Test(expected = ConfigException.class)
	public void testGetInt() {
		config.getInt();
	}
	
	@Test(expected = ConfigException.class)
	public void testGetLong() {
		config.getLong();
	}
	
	@Test(expected = ConfigException.class)
	public void testGetFloat() {
		config.getFloat();
	}
	
	@Test(expected = ConfigException.class)
	public void testGetDouble() {
		config.getDouble();
	}
	
	@Test
	public void testGetOption() {
		assertEquals("test", config.get("env"));
		assertTrue(config.getBool("wap.result.redirect"));
		assertEquals(6, config.getInt("PAGE_SIZE"));
		assertEquals(6, config.getInt("PAGE_SIZE", 8));
		assertEquals(1.0f, config.getFloat("idf", 1.0f), 0.00001f);
		assertArrayEquals(new String[] { "http://192.168.13.96:9100/", "http://192.168.13.85:9100/" }, config.gets("service.address.novel", " "));
	}
	
	@Test
	public void testGetDateOption() {
		Date d = new Date(2011-1900, 6, 1);
		assertEquals(d, config.getDate("THIS_MONTH"));
	}
	
	@Test
	public void testGetBranch() {
		assertEquals(config.getBranch("service.address").get("dict"), config.get("service.address.dict"));
		assertEquals(config.get("service.address.dict"), config.getBranch("service.address.dict").get());
		assertEquals(config.getBranch("service").getBranch("address").getBranch("dict").get(), config.getBranch("service.address.dict").get());
	}
	
	@Test
	public void testGetArrays() {
		assertArrayEquals(new String[] { "high", "low" }, config.gets("levels"));
		assertArrayEquals(null, config.gets("option.not.exists"));
		assertEquals(0, config.gets("empty.value").length);
		assertEquals(0, config.gets("space.value").length);
	}
	
	@Test(expected = ConfigException.class)
	public void testGetInts() {
		config.getInts("levels");
	}
	
	@Test
	public void testGetOptionNames() {
		System.out.println(Arrays.toString(config.getOptionNames()));
		assertArrayEquals(new String[] { "dict", "novel" }, config.getBranch("service.address").getOptionNames());
	}
	
	@Test
	public void testGetInetSocketAddress() {
		assertEquals(new InetSocketAddress("121.43.29.23", 9832), config.getInetSocketAddress("ad.server"));
		assertEquals(3, config.getInetSocketAddresses("memcache.servers").length);
	}
	
	@Test
	public void testToMap() {
		assertTrue(config.toMap().size() > 0);
		Map<String, String> map = config.getBranch("service.address").toMap();
		assertEquals(2, map.size());
		assertTrue(map.containsKey("dict"));
		assertTrue(map.containsKey("novel"));
		System.out.println(map);
		
		Map<String, Integer> map2 = config.getBranch("type").toMap(Integer.class);
		
		assertEquals(0, (int)map2.get("general"));
		assertEquals(1, (int)map2.get("quick"));
		assertEquals(2, (int)map2.get("news"));
	}
	
	@Test
	public void testVar() {
		assertEquals("aa.b", config.get("a.b"));
		assertEquals("aa.b.c", config.get("a.b.c"));
	}

}
