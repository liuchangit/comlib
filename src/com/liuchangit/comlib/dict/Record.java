package com.liuchangit.comlib.dict;

import java.util.ArrayList;
import java.util.List;

class Record {
	private List<String> keys = new ArrayList<String>();
	private List<String> values = new ArrayList<String>();
	
	public Record() {
		
	}
	
	public void put(String key, String value) {
		int i = 0;
		for (; i < keys.size(); i++) {
			if (keys.get(i).equals(key)) {
				values.set(i, value);
				break;
			}
		}
		if (i >= keys.size()) {
			keys.add(key);
			values.add(value);
		}
	}
	
	public String get(String key) {
		int i = 0;
		for (; i < keys.size(); i++) {
			if (keys.get(i).equals(key)) {
				return values.get(i);
			}
		}
		return null;
	}
	
	public int geti(String key) {
		String strVal = get(key);
		return Integer.parseInt(strVal.trim());
	}
}
