package com.liuchangit.comlib.dict;

import java.util.HashMap;
import java.util.Map;

public class DictRegistry {
	private static Map<String, BasicDict> dictMap = new HashMap<String, BasicDict>();
	
	public static void register(String filename) {
		register(filename, filename);
	}
	
	public static void register(String filename, String dictname) {
		dictMap.put(dictname, DictFactory.getBasicDict(filename));
	}
	
	public static void register(String filename, String dictname, DictReloadPeriod per) {
		dictMap.put(dictname, DictFactory.getBasicDict(filename));
	}
	
	public static void registerClassPath(String filename) {
		registerClassPath(filename, filename);
	}
	
	public static void registerClassPath(String filename, String dictname) {
		dictMap.put(dictname, DictFactory.getClassPathBasicDict(filename));
	}
}
