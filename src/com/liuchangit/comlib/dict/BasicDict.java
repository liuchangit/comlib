package com.liuchangit.comlib.dict;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a basic dict has at most 2 fields. if it has 2 fields, then the 1st is key, the 2nd is value.
 * 2 fields of a record must be seperated by a tab. we can use this to judge how many fields it has.
 * 
 * @author charles
 *
 */
public class BasicDict {
	
	private boolean ignoreCase;
	private boolean fuzzyMatch;
	private boolean singleField;
	
	private Map<String, String> table = new HashMap<String, String>();
	private Map<String, List<String>> firstLetterIndex = new HashMap<String, List<String>>();
	
	BasicDict(BufferedReader r, boolean caseSensitive, boolean fuzzyMatch) throws Exception {
		this.ignoreCase = !caseSensitive;
		this.fuzzyMatch = fuzzyMatch;
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = r.readLine()) != null) {
			line = line.trim();
			if (line.length() > 0) {
				lines.add(line);
			}
		}
		this.singleField = judgeSingleField(lines);
		if (this.singleField) {
			for (String ln : lines) {
				ln = normalizeKey(ln);
				table.put(ln, "");
				if (fuzzyMatch) {
					addFirstLetterIndex(ln);
				}
			}
		} else {
			for (String ln : lines) {
				String[] fields = ln.split("\\t");
				if (fields.length < 2) {
					continue;
				}
				String key = normalizeKey(fields[0]);
				table.put(key, fields[1]);
				if (fuzzyMatch) {
					addFirstLetterIndex(key);
				}
			}
		}
		if (fuzzyMatch) {
			for (List<String> list : firstLetterIndex.values()) {
				Collections.sort(list, new Comparator<String>() {
					public int compare(String o1, String o2) {
						int len1 = o1.length();
						int len2 = o2.length();
						if (len1 > len2) {
							return -1;
						} else if (len1 < len2) {
							return 1;
						} else {
							return 0;
						}
					}
				});
			}
		}
	}
	
	private static boolean judgeSingleField(List<String> lines) {
		boolean twoField = true;
		for (int i = 0; i < 5 && i < lines.size(); i++) {
			boolean hasTab = lines.get(i).indexOf("\t") > 0;
			twoField = twoField && hasTab;
			if (!twoField) {
				return true;
			}
		}
		return false;
	}
	
	private void addFirstLetterIndex(String key) {
		String firstChar = key.substring(0, 1);
		List<String> list = firstLetterIndex.get(firstChar);
		if (list == null) {
			list = new ArrayList<String>();
			firstLetterIndex.put(firstChar, list);
		}
		if (!list.contains(key)) {
			list.add(key);
		}
	}

	public boolean contains(String key) {
		String k = getContainedKey(key);
		return k != null;
	}

	private String getContainedKey(String word) {
		String k = normalizeKey(word);
		if (fuzzyMatch) {
			for (int i = 0; i < k.length(); i++) {
				String ch = k.substring(i, i+1);
				List<String> words = firstLetterIndex.get(ch);
				if (words == null) {
					continue;
				}
				for (String w : words) {
					if (k.indexOf(w) >= 0) {
						return w;
					}
				}
			}
			return null;
		} else {
			return table.containsKey(k) ? k : null;
		}
	}
	
	private String normalizeKey(String key) {
		String k = key.trim();
		if (ignoreCase) {
			k = k.toLowerCase();
		}
		return k;
	}
	
	public String search(String key) {
		String k = getContainedKey(key);
		if (singleField) {
			return k;
		} else {
			return table.get(k);
		}
	}
	
	public int size() {
		return table.size();
	}
}
