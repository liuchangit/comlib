package com.liuchangit.comlib.sign;

import java.io.ByteArrayOutputStream;


public class Base62 {
	
	private static char[] BASE62_DIGIT = "zxWb26vJKGq0HDtaIL3EQosf8M4uApNk9dRw7ZmcyPOjSClTigU1FVrY5hXeBn+/".toCharArray();
	private static byte[] BASE62_POS = new byte[256];
	static {
		for (int i = 0; i < BASE62_DIGIT.length; i++) {
			BASE62_POS[BASE62_DIGIT[i]] = (byte)i;
		}
	}
	
	public static String encode(long num) {
		StringBuilder buf = new StringBuilder();
		int n = (int)num;
		if (n < 0) {
			n >>>= 1;
			int r = (int)(n % 31);
			buf.append(BASE62_DIGIT[2*r + (int)(num & 0x01)]);
			n = n/31;
		}
		while (n >= 62) {
			int r = (int)(n % 62);
			buf.append(BASE62_DIGIT[r]);
			n = n/62;
		}
		buf.append(BASE62_DIGIT[(int)n]);
		return buf.toString();
	}
	
	public static String encode(byte[] data) {
		StringBuffer sb = new StringBuffer(data.length * 2);
		int pos = 0, val = 0;
		for (int i = 0; i < data.length; i++) {
			val = (val << 8) | (data[i] & 0xFF);
			pos += 8;
			while (pos > 5) {
				char c = BASE62_DIGIT[val >> (pos -= 6)];
				sb.append(
				/**/c == 'c' ? "cp" :
				/**/c == '+' ? "cT" :
				/**/c == '/' ? "cR" : c);
				val &= ((1 << pos) - 1);
			}
		}
		if (pos > 0) {
			char c = BASE62_DIGIT[val << (6 - pos)];
			sb.append(
			/**/c == 'c' ? "cp" :
			/**/c == '+' ? "cT" :
			/**/c == '/' ? "cR" : c);
		}
		return sb.toString();
	}

	/**
	 * 将字符串解码成byte数组
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decode(String string) {
		if (string == null) {
			return null;
		}
		char[] data = string.toCharArray();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				string.toCharArray().length);
		int pos = 0, val = 0;
		for (int i = 0; i < data.length; i++) {
			char c = data[i];
			if (c == 'c') {
				c = data[++i];
				c =
				/**/c == 'p' ? 'c' :
				/**/c == 'T' ? '+' :
				/**/c == 'R' ? '/' : data[--i];
			}
			val = (val << 6) | BASE62_POS[c];
			pos += 6;
			while (pos > 7) {
				baos.write(val >> (pos -= 8));
				val &= ((1 << pos) - 1);
			}
		}
		return baos.toByteArray();
	}
	
}
