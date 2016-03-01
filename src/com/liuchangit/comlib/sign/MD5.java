package com.liuchangit.comlib.sign;

import java.security.MessageDigest;

public class MD5 {

	private MD5() {}
	
	public final static String getMD5(String text) {
		return getMD5(text.getBytes());
	}
	
	public final static String getMD5(byte[] buffer) {
		try {
			byte[] md = getMD5Bytes(buffer);
			return byte2Hex(md);
		} catch (Exception e) {
			return null;
		}
	}
	
	public final static byte[] getMD5Bytes(String text) {
		try {
			return getMD5Bytes(text.getBytes("UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public final static byte[] getMD5Bytes(byte[] buffer) {
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			return md;
		} catch (Exception e) {
			return null;
		}
	}
	

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String byte2Hex(byte[] md) {
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
}
