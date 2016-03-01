package com.liuchangit.comlib.dict;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class DictFactory {
	private static final Logger LOG = Logger.getLogger("common");
	
	public static BasicDict getBasicDict(String filename) {
		return getBasicDict(filename, "UTF-8");
	}

	public static BasicDict getBasicDict(String filename, String encoding) {
		return getBasicDict(filename, encoding, CaseSensitive.No, MatchMode.Exact);
	}

	public static BasicDict getBasicDict(String filename, CaseSensitive cs, MatchMode mm) {
		return getBasicDict(filename, "UTF-8", CaseSensitive.No, MatchMode.Exact);
	}

	public static BasicDict getBasicDict(String filename, String encoding, CaseSensitive cs, MatchMode mm) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding));
			return newBasicDict(br, cs == CaseSensitive.Yes, mm == MatchMode.Fuzzy);
		} catch (Exception e) {
			LOG.error("error loading dict: " + filename);
			return null;
		}
	}

	public static BasicDict getBasicDict(InputStream in) {
		return getBasicDict(in, "UTF-8");
	}

	public static BasicDict getBasicDict(InputStream in, String encoding) {
		return getBasicDict(in, encoding, CaseSensitive.No, MatchMode.Exact);
	}

	public static BasicDict getBasicDict(InputStream in, CaseSensitive cs, MatchMode mm) {
		return getBasicDict(in, "UTF-8", CaseSensitive.No, MatchMode.Exact);
	}

	public static BasicDict getBasicDict(InputStream in, String encoding, CaseSensitive cs, MatchMode mm) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
			return newBasicDict(br, cs == CaseSensitive.Yes, mm == MatchMode.Fuzzy);
		} catch (Exception e) {
			LOG.error("error loading dict");
			return null;
		}
	}
	
	public static BasicDict getClassPathBasicDict(String filename) {
		return getClassPathBasicDict(filename, "UTF-8");
	}

	public static BasicDict getClassPathBasicDict(String filename, String encoding) {
		return getClassPathBasicDict(filename, encoding, CaseSensitive.No, MatchMode.Exact);
	}

	public static BasicDict getClassPathBasicDict(String filename, CaseSensitive cs, MatchMode mm) {
		return getClassPathBasicDict(filename, "UTF-8", CaseSensitive.No, MatchMode.Exact);
	}

	public static BasicDict getClassPathBasicDict(String filename, String encoding, CaseSensitive cs, MatchMode mm) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(DictFactory.class.getClassLoader().getResourceAsStream(filename), encoding));
			return newBasicDict(br, cs == CaseSensitive.Yes, mm == MatchMode.Fuzzy);
		} catch (Exception e) {
			LOG.error("error loading dict: " + filename);
			return null;
		}
	}

	private static BasicDict newBasicDict(BufferedReader br, boolean caseSensitive, boolean fuzzyMatch) throws Exception {
		BasicDict dict = new BasicDict(br, caseSensitive, fuzzyMatch);
		return dict;
	}
}
